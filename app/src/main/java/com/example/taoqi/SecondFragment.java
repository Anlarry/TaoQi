package com.example.taoqi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.taoqi.DataManager.GoodManager;
import com.example.taoqi.TypeConv.BitmapByte;
import com.example.taoqi.model.GoodViewModel;
import com.example.taoqi.model.SharedViewModel;
import com.example.taoqi.user.UserInfo;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

import Variables.ResString;

public class SecondFragment extends Fragment {
    public static GoodViewModel goodViewModel = new GoodViewModel();
    private SharedViewModel sharedViewModel;

    ImageView image;
    TextView goodName;
    TextView goodDesp;
    EditText updateGoodName;
    EditText updateGoodDesp;
    EditText updateGoodKind;
    EditText updateGoodPrice;
    LinearLayout goodCardlayout;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        image =  view.findViewById(R.id.GoodPhotoSecond);
        goodName = view.findViewById(R.id.GoodNameSecond);
        goodDesp = view.findViewById(R.id.GoodDespSecond);
        updateGoodName = view.findViewById(R.id.UpdateGoodName);
        updateGoodDesp = view.findViewById(R.id.UpdateGoodDesp);
        updateGoodPrice = view.findViewById(R.id.UpdateGoodPrice);
        updateGoodKind = view.findViewById(R.id.UpdateGoodKind);
        goodCardlayout = view.findViewById(R.id.GoodLayout);
        GoodViewModel.GoodInfo goodInfo = goodViewModel.getGoodInfo();
        Bitmap bitmap = BitmapFactory.decodeByteArray(goodInfo.photo, 0, goodInfo.photo.length);
        image.setImageBitmap(bitmap);


        if(sharedViewModel.getUserInfo() == null || sharedViewModel.getUserInfo().Customer) {
            UpdateUICustomer(view);
        }
        else {
            UpdateUIBusiness(view);
        }
    }
    private  void UpdateUICustomer(View view) {
        GoodViewModel.GoodInfo goodInfo = goodViewModel.getGoodInfo();
        goodName.setText(goodInfo.goodName + " ￥" + goodInfo.goodPrice);
        System.out.println(goodInfo.goodName);
        System.out.println(goodInfo.goodPrice);
        goodDesp.setText(goodInfo.goodDesp);
        goodCardlayout.removeView(view.findViewById(R.id.BusinessGoodShow));
        ConstraintLayout goodPage = view.findViewById(R.id.GoodPage);
        goodPage.removeView(view.findViewById(R.id.BusinessGoodButton));
        view.findViewById(R.id.BuyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo userInfo = sharedViewModel.getUserInfo();
                if (userInfo == null) {
                    Snackbar.make(view, "Please Login First", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                try {
                    GoodManager goodManager = new GoodManager(new GoodManager.CallBacks() {
                        @Override
                        public void ActionSuccess(Object Text) {
                            Snackbar.make(view, (String) Text, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                    goodManager.BuyGood(
                            String.valueOf(sharedViewModel.getUserInfo().userId),
                            goodViewModel.getGoodInfo().goodId
                    );
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        });
        image.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View view) {
           }
        });
        view.findViewById(R.id.ShoppingCartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        view.findViewById(R.id.EnterShop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void UpdateUIBusiness(View view) {
        GoodViewModel.GoodInfo goodInfo = goodViewModel.getGoodInfo();
        updateGoodName.setText(goodInfo.goodName);
        updateGoodDesp.setText(goodInfo.goodDesp);
        updateGoodPrice.setText(goodInfo.goodPrice);
        updateGoodKind.setText(goodInfo.goodKind);
        goodCardlayout.removeView(view.findViewById(R.id.CustomerGoodShow));
        ConstraintLayout goodPage = view.findViewById(R.id.GoodPage);
        goodPage.removeView(view.findViewById(R.id.CustomerGoodButton));
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });
        view.findViewById(R.id.UpdateGoodButton).setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GoodManager goodManager = new GoodManager(new GoodManager.CallBacks() {
                        @Override
                        public  void  ActionSuccess (Object msg) {
                            Map<String, Object> map = (Map<String, Object>)msg;
                            Snackbar.make(view, (String)map.get(ResString.MSG), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        @Override
                        public void ActionFail() {
                            Snackbar.make(view, "Update Fail", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                    Bitmap photo = ((BitmapDrawable)image.getDrawable()).getBitmap();
                    goodManager.UpdateGoodById(
                            goodInfo.goodId,
                            updateGoodName.getText().toString(),
                            updateGoodKind.getText().toString(),
                            updateGoodDesp.getText().toString(),
                            photo,
                            updateGoodPrice.getText().toString()
                    );
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                    Snackbar.make(view, "Update Fail", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }) ;
        view.findViewById(R.id.DeleteGoodButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        }) ;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 2) {
            if (data != null) {
                Uri uri = data.getData();
                image.setImageURI(uri);
            }
        }
    }
}