package com.example.taoqi;

import android.os.Bundle;

import com.example.taoqi.user.UserInfo;
import com.example.taoqi.model.SharedViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private FragmentManager fragmentManager;
    private SharedViewModel sharedViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appbarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(
                toolbar, navController, appbarConfiguration
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment navFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment);
        if(id == R.id.action_settings ) {
            Integer[] actions = {
                    R.id.action_FirstFragment_to_setting,
                    R.id.action_SecondFragment_to_setting,
                    R.id.action_shop2_to_setting,
                    R.id.action_addGood_to_setting,
            };
            for (Integer action : actions) {
                try {
                    NavHostFragment.findNavController(navFragment).navigate(action);
                    break;
                } catch (Exception e) {
                    continue;
                }
            }
        }
        else if( id == R.id.SignIn) {
            try {
                NavHostFragment.findNavController(navFragment).navigate(R.id.action_FirstFragment_to_loginFragment);
            } catch (Exception e1) {
                try {
                    NavHostFragment.findNavController(navFragment).navigate(R.id.action_SecondFragment_to_loginFragment);
                } catch (Exception e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

}