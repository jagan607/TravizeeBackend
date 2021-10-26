package com.example.travizee.payload;

import com.example.travizee.model.facebook.FacebookPicture;

public class FbLoginResponse {

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    private String userName;

    private String email;

    private String profilePic;

    private boolean isSuccess;

    public FbLoginResponse(boolean isSuccess, String userName, String email, String profilePic) {
        this.userName = userName;
        this.email = email;
        this.profilePic = profilePic;
        this.isSuccess = isSuccess;
    }


}
