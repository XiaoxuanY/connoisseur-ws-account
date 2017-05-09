package com.connoisseur.services.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

/**
 * Created by Ray Xiao on 4/18/17.
 */

@Entity
public class CnsUser {
    @Id
    @Getter
    @GeneratedValue
    private long id;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String userName;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String zip;
    @Getter
    @Setter
    private String gender;
    @Getter
    @Setter
    private String relationStatus;

    @Getter
    @Setter
    private UserStatus status;

    @Getter
    @Setter
    private long createDate;

    @Getter
    @Setter
    private long modifyDate;

    @Getter
    @Setter
    private String incomeRange;
    @Getter
    @Setter
    private int age;

    private Date birthday;

    public Date getBirthday() {
        return birthday == null ? null : new Date(birthday.getTime());
    }

    public void setBirthday(Date bday) {

        this.birthday = bday != null ? new Date(bday.getTime()) : null;
    }

    @Getter
    @Setter
    private String password;

    @Getter
    private String userType;

    public CnsUser(String userName, String hashedPassword, String email, String firstName, String lastName,UserStatus status) {
        this.userName = userName;
        this.password= hashedPassword;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status=status;
        this.userType = "N";

    }
    CnsUser() { // jpa only
    }

    @Override
    public String toString() {
        return String.format("%s (email %s, fn %s, ln %s, status %s)", userName, email, firstName, lastName,status);
    }

}


