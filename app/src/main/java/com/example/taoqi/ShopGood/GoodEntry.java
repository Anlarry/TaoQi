package com.example.taoqi.ShopGood;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.example.taoqi.FirstFragment;
import com.example.taoqi.R;
import com.example.taoqi.FirstFragment;
import com.example.taoqi.model.GoodViewModel;

public class GoodEntry extends LinearLayout {
    private ImageView imageView;
    private TextView goodNameTextView;
    private TextView goodDespTextView;
    private View view ;
    private GoodViewModel.GoodInfo goodInfo;

    public GoodEntry(Context context) {
        this(context, null);
    }

    public GoodEntry(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public void setOnclick(OnClickListener listener) {
        view.setOnClickListener(listener);
    }
    public GoodEntry(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        view = (View) View.inflate(context, R.layout.good_entry, this);

        imageView = findViewById(R.id.GoodPhotoShow);
        goodNameTextView = findViewById(R.id.GoodNameAndPrice);
        goodDespTextView = findViewById(R.id.GoodDesp);
    }
    
    public void setBackgroundColor(int color) {
        view.findViewById(R.id.GoodLayout).setBackgroundColor(color);
    }
    
    public void setAttribute(String goodName, String goodDesp, byte[] photo) {
        goodNameTextView.setText(goodName);
        goodDespTextView.setText(goodDesp);
        Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        imageView.setImageBitmap(bitmap);
    }
    
    public void setPhoto(byte[] photo) {
        goodInfo.photo = photo;
        Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        imageView.setImageBitmap(bitmap);
    }
    
    public GoodViewModel.GoodInfo getGoodInfo() {
        return this.goodInfo;
    }
    
    public void setGoodInfo(GoodViewModel.GoodInfo goodInfo) {
        this.goodInfo = goodInfo;
        goodDespTextView.setText(goodInfo.goodDesp);
        goodNameTextView.setText(goodInfo.goodName);
        Bitmap bitmap = BitmapFactory.decodeByteArray(goodInfo.photo, 0, goodInfo.photo.length);
        imageView.setImageBitmap(bitmap);
    }
}
