package com.example.taoqi.ShopGood;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.taoqi.DataManager.GoodManager;
import com.example.taoqi.R;
import com.example.taoqi.model.SharedViewModel;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddGood#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddGood extends Fragment {
    private SharedViewModel sharedViewModel;
    private ImageView imageView;

    private EditText goodNameEdit;
    private EditText goodKindEdit;
    private EditText goodDespEdit;
    private EditText goodPriceEdit;
    private Button addGood;
    private Button addPhoto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_good, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        goodNameEdit = view.findViewById(R.id.GoodNameEdit);
        goodKindEdit = view.findViewById(R.id.GoodKindEdit);
        goodDespEdit = view.findViewById(R.id.GoodDespEdit);
        goodPriceEdit = view.findViewById(R.id.GoodPriceEdit);
        addGood = view.findViewById(R.id.AddGood);
        addPhoto = view.findViewById(R.id.AddPhoto);
        imageView = view.findViewById(R.id.AddGoodImageView);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        } );
        addGood.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    GoodManager goodManager = new GoodManager(new GoodManager.CallBacks() {
                        @Override
                        public void AddGooadAction() {
//                            Toast.makeText(getContext().getApplicationContext(),
//                                    R.string.add_good_success,
//                                    Toast.LENGTH_LONG
//                            ).show();
                            Snackbar.make(view,  R.string.add_good_success, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        @Override
                        public void AddGoodFailAction() {
//                            Toast.makeText(getContext().getApplicationContext(),
//                                    "Add Good Fail",
//                                    Toast.LENGTH_LONG
//                            ).show();
                            Snackbar.make(view,  R.string.add_good_fail, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                    goodManager.AddGood(
                            sharedViewModel.getUserInfo().userId,
                            goodNameEdit.getText().toString(),
                            goodKindEdit.getText().toString(),
                            goodDespEdit.getText().toString(),
                            ((BitmapDrawable)imageView.getDrawable()).getBitmap(),
                            Integer.parseInt(goodPriceEdit.getText().toString())
                    );
                }
                catch (NumberFormatException ne) {
//                    Toast.makeText(getContext().getApplicationContext(),
//                            "Add Good Fail, Check The Price :)",
//                            Toast.LENGTH_LONG
//                    ).show();
                    Snackbar.make(view, "Add Good Fail, Check The Price :)", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
//                    Toast.makeText(getContext().getApplicationContext(),
//                            R.string.add_good_fail,
//                            Toast.LENGTH_LONG
//                    ).show();
                    Snackbar.make(view,  R.string.add_good_fail, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 2) {
            if (data != null) {
                Uri uri = data.getData();
                imageView.setImageURI(uri);
            }
        }
    }
}