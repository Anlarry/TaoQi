package com.example.taoqi.model;

import androidx.lifecycle.ViewModel;

import com.example.taoqi.DataManager.Client;

import Http.HttpConnect;

public class GoodViewModel extends ViewModel {
    public  static class GoodInfo {
        public GoodInfo(String goodName, String goodKind,String goodDesp, String goodPrice, byte[] photo, String goodId) {
            this.goodName = goodName;
            this.goodKind = goodKind;
            this.goodDesp = goodDesp;
            this.goodPrice = goodPrice;
            this.photo = photo;
            this.goodId = goodId;
        }
        public GoodInfo() {

        }
        public String goodName;
        public String goodKind;
        public String goodDesp;
        public String goodPrice;
        public String goodId;
        public byte[] photo;
    }
    private GoodInfo goodInfo;
    public void setGoodInfo(GoodInfo goodInfo) {
        this.goodInfo = goodInfo;
    }
    public GoodInfo getGoodInfo(){
        return goodInfo;
    }
}
