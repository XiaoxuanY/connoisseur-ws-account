package com.connoisseur.services.model;

/**
 * Created by Ray Xiao on 4/20/17.
 */
public enum UserStatus {

    P("Pending"),
    A("Active"),
    D("Deleted");

    private String val;

    UserStatus(String val){
        this.val=val;
    }

    public String getValue() {
        return val;
    }

}
