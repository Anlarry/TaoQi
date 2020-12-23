package com.example.taoqi.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.taoqi.DataManager.UserManagerCallBack;
import com.example.taoqi.R;
import com.example.taoqi.DataManager.UserManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register extends Fragment {
    private Button registerButton;
    private EditText emailEditText ;
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText addressEditText;
    private Switch isCustom;
    private RegisterFormState formState;
    public Register() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        emailEditText = view.findViewById(R.id.editTextTextEmailAddress);
        nameEditText = view.findViewById(R.id.editTextTextPersonName);
        passwordEditText = view.findViewById(R.id.editTextTextPassword);
        addressEditText = view.findViewById(R.id.editTextTextPostalAddress);
        registerButton = view.findViewById(R.id.RegisterButton);
        isCustom = view.findViewById(R.id.isCustomSwitch);
        formState = new RegisterFormState();

        TextWatcher textWatcher =  new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckFormState();
            }
        };
        emailEditText.addTextChangedListener(textWatcher);
        nameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        addressEditText.addTextChangedListener(textWatcher);
        isCustom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    CheckFormState();
                else
                    CheckFormState();
            }
        });
        /* click to register */
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    UserManager userManager = new UserManager();
                    userManager.setUserManagerCallBack(
                            new UserManagerCallBack() {
                                @Override
                                public void RegisterCallBack() {
                                    if(getContext() != null && getContext().getApplicationContext() != null) {
                                        Toast.makeText(getContext().getApplicationContext(),
                                                R.string.register_success,
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                                    NavHostFragment.findNavController(Register.this)
                                            .navigateUp();
                                }
                            }
                    );
                    userManager.Register(
                        emailEditText.getText().toString(),
                        nameEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        addressEditText.getText().toString(),
                        isCustom.isChecked()
                    );
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void CheckFormState() {
        formState.TextChange(
                emailEditText.getText().toString(),
                nameEditText.getText().toString(),
                passwordEditText.getText().toString(),
                addressEditText.getText().toString(),
                isCustom.isChecked()
        );
        RaiseFormState();
    }

    private void RaiseFormState() {
        if(!formState.EmailValid && !emailEditText.getText().toString().equals("") ) {
            emailEditText.setError((getString(formState.emailError)));
        }
        if(!formState.NameValid && !nameEditText.getText().toString().equals("")) {
            nameEditText.setError((getString(formState.nameError)));
        }
        if(!formState.PasswordValid && !passwordEditText.getText().toString().equals("")) {
            passwordEditText.setError((getString(formState.passwordError)));
        }
        if(!formState.AddressValid && isCustom.isChecked()) {
            addressEditText.setError(getString(formState.addressError));
        }
        else {
            addressEditText.setError(null);
        }
        if(formState.Valid()) {
            registerButton.setEnabled(true);
        }
        else {
            registerButton.setEnabled(false);
        }
    }
}