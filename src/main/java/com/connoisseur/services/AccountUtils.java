package com.connoisseur.services;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Date;

/**
 * Created by Ray Xiao on 4/20/17.
 */
@Component
public class AccountUtils {

    @Value("${encryption.salt}")
    private String SALT;

    public static final String ENCODING_FORMAT_UTF8 = "UTF8";
    public static final String SIGNATURE_METHOD = "HmacSHA256";
    private static final long window = 15 * 60 * 1000L;

    protected static final Logger log = Logger.getLogger(AccountUtils.class);
        private static SecureRandom RANDOM = new SecureRandom();
        static {
            RANDOM.generateSeed(16);
        }

//        public static String prepareJsonResponseForTokens(GetOpenIdTokenForDeveloperIdentityResult result, String key) {
//
//            StringBuilder responseBody = new StringBuilder();
//            responseBody.append("{");
//            responseBody.append("\t\"identityPoolId\": \"").append(Configuration.IDENTITY_POOL_ID).append("\",");
//            responseBody.append("\t\"identityId\": \"").append(result.getIdentityId()).append("\",");
//            responseBody.append("\t\"token\": \"").append(result.getToken()).append("\",");
//            responseBody.append("}");
//
//            // Encrypting the response
//            return AESEncryption.wrap(responseBody.toString(), key);
//        }
//
//        public static String prepareJsonResponseForKey(String data, String key) {
//
//            StringBuilder responseBody = new StringBuilder();
//            responseBody.append("{");
//            responseBody.append("\t\"key\": \"").append(data).append("\"");
//            responseBody.append("}");
//
//            // Encrypting the response
//            return AESEncryption.wrap(responseBody.toString(), key.substring(0, 32));
//        }

        public  String sign(String content, String key) {
            try {
                byte[] data = content.getBytes(ENCODING_FORMAT_UTF8);
                Mac mac = Mac.getInstance(SIGNATURE_METHOD);
                mac.init(new SecretKeySpec(key.getBytes(ENCODING_FORMAT_UTF8), SIGNATURE_METHOD));
                char[] signature = Hex.encodeHex(mac.doFinal(data));
                return new String(signature);
            } catch (Exception e) {
                log.log(Level.ERROR, "Error during sign ", e);
            }
            return null;
        }

        public String getSaltedPassword(String username, String password) {
            return sign((username + SALT), password);
        }

        public  String base64(String data) throws UnsupportedEncodingException {
            byte[] signature = Base64.encodeBase64(data.getBytes(ENCODING_FORMAT_UTF8));
            return new String(signature, ENCODING_FORMAT_UTF8);
        }



        public  boolean isValidUsername(String username) {
            int length = username.length();
            if (length < 3 || length > 128) {
                return false;
            }

            char c = 0;
            for (int i = 0; i < length; i++) {
                c = username.charAt(i);
                if (!Character.isLetterOrDigit(c) && '_' != c && '.' != c && '@' != c) {
                    return false;
                }
            }

            return true;
        }

        public  boolean isValidPassword(String password) {
            int length = password.length();
            return (length >= 6 && length <= 128);
        }

        public  boolean isEmpty(String str) {
            if (null == str || str.trim().length() == 0)
                return true;
            return false;
        }


        /**
         * This method is low performance string comparison function. The purpose of
         * this method is to prevent timing attack.
         */
        public  boolean slowStringComparison(String givenSignature, String computedSignature) {
            if (null == givenSignature || null == computedSignature
                    || givenSignature.length() != computedSignature.length())
                return false;

            int n = computedSignature.length();
            boolean signaturesMatch = true;

            for (int i = 0; i < n; i++) {
                signaturesMatch &= (computedSignature.charAt(i) == givenSignature.charAt(i));
            }

            return signaturesMatch;
        }

        /**
         * Extract element from a JSON string
         *
         * @param json
         *            A string of JSON blob
         * @param element
         *            JSON key
         * @return the corresponding string value of the element
         */
        public  String extractElement(String json, String element) {
            boolean hasElement = (json.indexOf(element) != -1);
            if (hasElement) {
                int elementIndex = json.indexOf(element) + element.length() + 1;
                int startIndex = json.indexOf("\"", elementIndex);
                int endIndex = json.indexOf("\"", startIndex + 1);

                return json.substring(startIndex + 1, endIndex);
            }

            return null;
        }

}
