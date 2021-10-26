package com.example.travizee.payload;

import com.example.travizee.model.facebook.FacebookPicture;

public class SignupResponse {

    private Boolean success;
    private String message;
    private String token;
    private String id;
    private FacebookPicture facebookPicture;

    public SignupResponse(Boolean success, String message, String token, String id, FacebookPicture facebookPicture) {
        this.success = success;
        this.message = message;
        this.token =  token;
        this.id = id;
        this.facebookPicture = facebookPicture;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FacebookPicture getFacebookPicture() {
        return facebookPicture;
    }

    public void setFacebookPicture(FacebookPicture facebookPicture) {
        this.facebookPicture = facebookPicture;
    }
}
