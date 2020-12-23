package com.example.taoqi.ui.login;

import android.util.Patterns;

import com.example.taoqi.R;

public class RegisterFormState {
    boolean EmailValid = false;
    boolean AddressValid = false;
    boolean NameValid = false;
    boolean PasswordValid = false;

    Integer nameError = R.string.name_has_used;
    Integer emailError = R.string.invalid_email;
    Integer passwordError = R.string.invalid_password;
    Integer addressError = R.string.address_null;
    boolean Valid() {
        return  EmailValid && AddressValid && NameValid && PasswordValid;
    }

    void TextChange(String email, String name, String password, String address, boolean isCustomer) {
        if (email != null && email.contains("@") && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EmailValid = true;
        }
        else {
            EmailValid = false;
        }
        if(name != null && !name.equals("") && CheckName(name)) {
            NameValid = true;
        }
        else {
            NameValid = false;
        }
        if(password != null && password.length() > 5) {
            PasswordValid = true;
        }
        else {
            PasswordValid = false;
        }
        if(address != null && !address.equals("") || !isCustomer) {
            AddressValid = true;
        }
        else {
            AddressValid = false;
        }
    }

    private boolean CheckName(String name) {
        return true;
    }
}
