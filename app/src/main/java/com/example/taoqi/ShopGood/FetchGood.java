package com.example.taoqi.ShopGood;

import android.view.LayoutInflater;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.taoqi.DataManager.Client;
import com.example.taoqi.FirstFragment;
import com.example.taoqi.R;
import com.example.taoqi.SecondFragment;
import com.example.taoqi.model.GoodViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Http.HttpConnect;
import Variables.ResString;


public class FetchGood {
    public static ArrayList<GoodViewModel.GoodInfo> FetchGoodPhoto(List<Object> data) {
        ArrayList<GoodViewModel.GoodInfo> result = new ArrayList<GoodViewModel.GoodInfo>();
        List<Object> goods = (List<Object>)data;
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for(Object obj : goods) {
            Map<String, String> goodInfo = (Map<String, String>)obj;
            GoodViewModel.GoodInfo good = new GoodViewModel.GoodInfo();
            result.add(good);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        HttpConnect conn = new HttpConnect(Client.getServerURL() +
                                "/photo/" + goodInfo.get(ResString.GoodPhotoPath));
                        byte[] photo = conn.Response().data;

                        good.goodName = goodInfo.get(ResString.GoodName);
                        good.goodDesp =  goodInfo.get(ResString.GoodDesp);
                        good.goodPrice = goodInfo.get(ResString.GoodPrice);
                        good.photo =  photo;
                        good.goodId =  goodInfo.get(ResString.GoodId);
                    }
                    catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            };
            thread.start();
            threads.add(thread);
        }
        for(Thread thread : threads) {
            try {
                thread.join();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
}
