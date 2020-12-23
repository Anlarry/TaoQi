package com.example.taoqi;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.taoqi.DataManager.GoodManager;
import com.example.taoqi.ShopGood.GoodEntry;
import com.example.taoqi.model.GoodViewModel;
import com.example.taoqi.model.SharedViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import Http.HttpConnect;
import TypeConv.InStreamByte;
import Variables.ResString;
import com.example.taoqi.DataManager.Client;

public class FirstFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private LinearLayout goodList;
    private SearchView searchView;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        goodList = view.findViewById(R.id.GoodList);
        searchView = view.findViewById(R.id.SearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    GoodManager goodManager = new GoodManager(new GoodManager.CallBacks() {
                        @Override
                        public void GetGoodAction(Object data) {
                            UpdateGood(data);
                        }
                    });
                    goodManager.GetGoodByName(query);
                    return true;
                }
                catch (Exception  e) {
                    System.err.println(e.getMessage());
                    return false;
                }
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

        });
//        goodEntry = new GoodEntry(getContext());
//        goodList.addView(goodEntry);
//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(sharedViewModel.getUserInfo() != null) {
//                    System.out.println(sharedViewModel.getUserInfo().username);
//                }
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });

        Drawable add = getResources().getDrawable(R.drawable.add);
        Drawable cart = getResources().getDrawable(R.drawable.cart);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        if(sharedViewModel.getUserInfo() == null || sharedViewModel.getUserInfo().Customer) {
            fab.setImageDrawable(cart);
        }
        else {
            fab.setImageDrawable(add);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if (sharedViewModel.getUserInfo() == null) {
//                    Snackbar.make(view, "Please Login First %#$!@", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    Toast.makeText(getContext().getApplicationContext(),
                            "Please Login First %#$!@", Toast.LENGTH_LONG).show();
                    return ;
                }
                if(sharedViewModel.getUserInfo().Customer) {
                }
                else {
                    if(sharedViewModel.getUserInfo() == null || sharedViewModel.getUserInfo().Customer) {
//                        NavHostFragment.findNavController(FirstFragment.this)
//                                .navigate(R.id.actio);
                    }
                    else {
                        NavHostFragment.findNavController(FirstFragment.this)
                                .navigate(R.id.action_FirstFragment_to_addGood);
                    }
                }
            }
        });

        try {
            GoodManager goodManager = new GoodManager(new GoodManager.CallBacks() {
                @Override
                public void GetGoodAction(Object data) {
                    UpdateGood(data);
                }
            });
            if(sharedViewModel.getUserInfo() == null || sharedViewModel.getUserInfo().Customer ) {
                goodManager.GetAllGood();
            }
            else {
                // TODO
//                goodManager.GetShopGood()
                goodManager.GetGoodByBusiness(sharedViewModel.getUserInfo().userId);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void UpdateGood(Object data)  {
        goodList.removeAllViews();
        List<Object> goods = (List<Object>)data;
        ArrayList<GoodEntry> goodEntryList = new ArrayList<GoodEntry>();
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for(Object obj : goods) {
            Map<String, String> goodInfo = (Map<String, String>)obj;
            GoodEntry goodEntry = new GoodEntry(getContext());
            goodEntryList.add(goodEntry);
//            byte[] photo = Base64.getDecoder().decode(goodInfo.get(ResString.GoodPhoto).getBytes());
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        HttpConnect conn = new HttpConnect(Client.getServerURL() +
                                "/photo/" + goodInfo.get(ResString.GoodPhotoPath));
                        byte[] photo = conn.Response().data;

                        goodEntry.setAttribute(
                                goodInfo.get(ResString.GoodName) + "  ￥"  + goodInfo.get(ResString.GoodPrice),
                                goodInfo.get(ResString.GoodDesp),
                                photo
                        );
                        goodEntry.setGoodInfo(new GoodViewModel.GoodInfo(
                                        goodInfo.get(ResString.GoodName),
                                        goodInfo.get(ResString.GoodKind),
                                        goodInfo.get(ResString.GoodDesp),
                                        goodInfo.get(ResString.GoodPrice),
                                        photo,
                                        goodInfo.get(ResString.GoodId)
                                )
                        );
                        goodEntry.setOnclick(new View.OnClickListener() {
                            @Override
                            public void onClick(View view){
                                Thread thread = new Thread(){
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(200);
                                        }
                                        catch (InterruptedException e) {
                                            System.err.println(e.getMessage());
                                        }
                                    }
                                };
                                thread.start();
                                try {
                                    thread.join();
                                }
                                catch (InterruptedException e) {
                                    System.err.println(e.getMessage());
                                }
                                NavHostFragment.findNavController(FirstFragment.this)
                                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                                System.out.println(goodEntry.getGoodInfo().goodName);
                                SecondFragment.goodViewModel.setGoodInfo(goodEntry.getGoodInfo());
                            }
                        });
                        System.out.println("\033[1;32m" + goodEntry.getGoodInfo().goodName + "\033[0m") ;
                    }
                    catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            };
            thread.start();
            threads.add(thread);
        }
        for(int i = 0; i < threads.size(); i++) {
            try {
                threads.get(i).join();
                goodList.addView(goodEntryList.get(i));
            }
            catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}