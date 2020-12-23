package com.example.taoqi.user;

import com.example.taoqi.DataManager.UserManager;
import com.example.taoqi.DataManager.UserManagerCallBack;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    public Result<LoggedInUser> login(String username, String password, boolean Customer) {
        try {
            UserManager userManager = new UserManager();
            userManager.setUserManagerCallBack(new UserManagerCallBack() {
                @Override
                public void LoginCallBack() {
                }
            });
            Thread thread =  userManager.Login(username, password, Customer);
            thread.join(5000);

            if(userManager.getLoginStates()) {
                LoggedInUser User =
                        new LoggedInUser(
                                userManager.getUserInfo().userId,
                                userManager.getUserInfo().username,
                                userManager.getUserInfo().email,
                                userManager.getUserInfo().Customer
                                );
    //            throw new Exception("password Error");
                return new Result.Success<>(User);
            }
            else {
                return new Result.Error(new IOException("Error logging in"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}