package com.example.taoqi.model;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.taoqi.user.UserInfo;


public class SharedViewModel extends ViewModel {
    private UserInfo userInfo = null;

    public UserInfo getUserInfo() {
        return userInfo;
    }
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}



