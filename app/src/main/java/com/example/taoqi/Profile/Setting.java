package com.example.taoqi.Profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taoqi.DataManager.GoodManager;
import com.example.taoqi.DataManager.ShopManager;
import com.example.taoqi.DataManager.UserManager;
import com.example.taoqi.DataManager.UserManagerCallBack;
import com.example.taoqi.FirstFragment;
import com.example.taoqi.R;
import com.example.taoqi.SecondFragment;
import com.example.taoqi.ShopGood.FetchGood;
import com.example.taoqi.ShopGood.GoodEntry;
import com.example.taoqi.model.GoodViewModel;
import com.example.taoqi.model.SharedViewModel;
import com.example.taoqi.user.UserInfo;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Variables.ResString;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Setting} factory method to
 * create an instance of this fragment.
 */
public class Setting extends Fragment {
    private SharedViewModel sharedViewModel;
    private TextView UserName ;
    private TextView Email;
    private TextView ShopName;
    private EditText ShopDesp;
    private EditText ShopAddr;
    private TextView ShopTurnover ;
    private CardView ShopProfile;
    private TextView Balance;
    private LinearLayout ProfileCards;
    private LinearLayout PurchasedGoods;
    private CardView PurchaseCard;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        UserName = view.findViewById(R.id.NameTextView);
        Email = view.findViewById(R.id.EmailTextView);
        ShopName = view.findViewById(R.id.ShopNameTextEdit);
        ShopAddr = view.findViewById(R.id.ShopAddrTextEdit);
        ShopDesp = view.findViewById(R.id.ShopDespEditText);
        ShopProfile = view.findViewById(R.id.ShopProfile);
        ShopTurnover = view.findViewById(R.id.TurnoverTextView);
        Balance = view.findViewById(R.id.BalanceTextView);
        ProfileCards = view.findViewById(R.id.ProfileCard);
        PurchaseCard = view.findViewById(R.id.MyBuyed);
        PurchasedGoods = view.findViewById(R.id.PurchasedGoodsList);
        view.findViewById(R.id.CommitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    UserInfo user = sharedViewModel.getUserInfo();
                    if(user != null) {
                        ShopManager shopManager = new ShopManager(new ShopManager.CallBacks());
                        UserManager userManager = new UserManager(new UserManagerCallBack());
                        shopManager.AddShop(
                                user.userId,
                                ShopName.getText().toString(),
                                ShopAddr.getText().toString(),
                                ShopDesp.getText().toString()
                        );
                    }
                }
                catch (Exception  e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        view.findViewById(R.id.BalanceCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(100);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                builder.setTitle("Recharge");
                View recharge_view = inflater.inflate(R.layout.recharge, null);
                builder.setView(recharge_view);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            TextInputEditText text = recharge_view.findViewById(R.id.AmountInput);
                            int amount = Integer.parseInt(
                                text.getText().toString()
                            );
                            UserManager userManager = new UserManager(new UserManagerCallBack(){
                                @Override
                                public void ActionSuccess(Object msg) {
                                    Balance.setText("￥ " + (String)msg);
                                }
                                @Override
                                public void ActionFail() {
                                    Snackbar.make(view, "Recharge Fail", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            });
                            userManager.Recharge(sharedViewModel.getUserInfo().userId, amount);
                        }
                        catch(NumberFormatException e) {
                            Snackbar.make(view, "Number Format Error", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        catch (Exception e) {
                            Snackbar.make(view, "Recharge Fail", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        UpdateText(view);

    }
    void UpdateText(View view) {
        if(sharedViewModel.getUserInfo() != null) {
            UserName.setText(sharedViewModel.getUserInfo().username);
            Email.setText(sharedViewModel.getUserInfo().email);
            if(sharedViewModel.getUserInfo().Customer) {
                try {
                    UserManager userManager = new UserManager(new UserManagerCallBack(){
                        @Override
                        public void ActionSuccess(Object Text) {
                            Balance.setText("￥" + (String)Text);
                        }
                    });
                    userManager.GetCustomerInfoById(sharedViewModel.getUserInfo().userId, ResString.UserBalance);
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                UpdatePurchasedGoods();
            }
            else {
                UpdateShop();
            }
        }
        if(sharedViewModel.getUserInfo() == null || sharedViewModel.getUserInfo().Customer ) {
            ProfileCards.removeView(ShopProfile);
            ProfileCards.removeView(Balance);
        }
        if(sharedViewModel.getUserInfo() == null || !sharedViewModel.getUserInfo().Customer) {
            ProfileCards.removeView(PurchaseCard);
            ProfileCards.removeView(view.findViewById(R.id.BalanceCard));
        }
    }

    private void UpdatePurchasedGoods() {
        try {
            GoodManager goodManager = new GoodManager(new GoodManager.CallBacks() {
                @Override
                public void GetGoodAction(Object obj) {
                    // TODO
                    ArrayList<GoodViewModel.GoodInfo> goods = FetchGood.FetchGoodPhoto((List<Object>) obj);
                    for (GoodViewModel.GoodInfo good : goods) {
                        GoodEntry goodEntry = new GoodEntry(getContext());
                        goodEntry.setGoodInfo(good);
                        goodEntry.setOnclick(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                            System.err.println(e.getMessage());
                                        }
                                    }
                                };
                                thread.start();
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    System.err.println(e.getMessage());
                                }
                                NavHostFragment.findNavController(Setting.this)
                                        .navigate(R.id.action_setting_to_SecondFragment);
                                System.out.println(goodEntry.getGoodInfo().goodName);
                                SecondFragment.goodViewModel.setGoodInfo(goodEntry.getGoodInfo());
                            }
                        });
                        goodEntry.setBackgroundColor(Color.WHITE);
                        PurchasedGoods.addView(goodEntry);
                    }
                }
            });
            goodManager.GetPurchasedGood(sharedViewModel.getUserInfo().userId);

        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void UpdateShop() {
        try {
            ShopManager shopManager = new ShopManager(new ShopManager.CallBacks() {
                    @Override
                    public void ActionSuccess(Object msg) {
                        Map<String, String> map = (Map<String, String>) msg;
                        ShopName.setText(map.get(ResString.ShopName));
                        ShopDesp.setText(map.get(ResString.ShopDesp));
                        ShopAddr.setText(map.get(ResString.ShopAddr));
                        ShopTurnover.setText("￥ " +  map.get(ResString.ShopTurnOver));
                    }
                }
            );
            shopManager.GetShopByUserId(String.valueOf(sharedViewModel.getUserInfo().userId));
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}