package com.connoisseur.services.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.security.SecureRandom;

/**
 * Created by Ray Xiao on 4/21/17.
 */
@Entity
public class AuthToken {
    private static final int DEFAULT_TTL = 1000 * 60 * 60;//default ttl=1 hour
    private static final SecureRandom RANDOM = new SecureRandom();

    static {
        RANDOM.generateSeed(16);
    }

    @Id
    @GeneratedValue
    @Getter
    private long id;

    @Getter
    @Setter
    private long userId;

    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private long expiration;

    @Getter
    @Setter
    @Column(columnDefinition = "int default 3600000")

    private int ttl = DEFAULT_TTL;

    @Getter
    @Setter
    private String clientSpec;

    public boolean isExpired() {
        return this.expiration < System.currentTimeMillis();
    }

    public boolean touch() {
        if (isExpired()) {
            return false;
        } else {
            this.expiration = System.currentTimeMillis() + ttl;
            return true;
        }
    }

    public AuthToken(long userId, String token, int ttl, String clientSpec) {
        this.userId = userId;
        this.expiration = System.currentTimeMillis() + ttl;
        this.clientSpec = clientSpec;
        this.token = token;
        this.ttl = ttl;
    }

    public AuthToken(long userId, String token, String clientSpec) {
        this(userId, token, DEFAULT_TTL, clientSpec);
    }

    AuthToken() {
    }//jpa only
}
