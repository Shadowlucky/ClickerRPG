package com.example.clickerrpg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ShopActivity extends Activity {
    int weaponStage, weaponPrice = (int)(weaponStage * 150), playerMoney, healthPotions, healthPotionsPrice = 200;
    Button backButton;
    TextView shopMoneyText, weaponPriceText;
    ImageView weaponImage, healthPotionShopImage;
    ConstraintLayout constraintLayoutShop;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        /*constraintLayoutShop = findViewById(R.id.constraintLayoutShop);
        constraintLayoutShop.setBackgroundResource(R.drawable.planks);*/
        backButton = findViewById(R.id.backButton);
        shopMoneyText = findViewById(R.id.shopMoneyText);
        weaponImage = findViewById(R.id.weaponImage);
        weaponPriceText = findViewById(R.id.weaponPriceText);
        weaponImage.setImageResource(R.drawable.sword);
        weaponStage = getIntent().getIntExtra("weapon", weaponStage);
        weaponPriceText.setText(String.valueOf((int)(weaponStage * 150)));
        healthPotionShopImage = findViewById(R.id.healthPotionShopImage);
        healthPotionShopImage.setImageResource(R.drawable.healthpotion);
        playerMoney = getIntent().getIntExtra("money", playerMoney);
        healthPotions = getIntent().getIntExtra("healthPotions", 0);
        shopMoneyText.setText(Integer.toString(playerMoney));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.putExtra("money", playerMoney);
                intent.putExtra("weapon", weaponStage);
                intent.putExtra("healthPotions", healthPotions);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        weaponImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerMoney >= weaponPrice){
                    playerMoney -= weaponPrice;
                    shopMoneyText.setText(Integer.toString(playerMoney));
                    weaponStage++;
                    weaponPrice = (int)(weaponStage * 150);
                    weaponPriceText.setText(String.valueOf(weaponPrice));
                    /*getIntent().putExtra("money", playerMoney);
                    getIntent().putExtra("weapon", weaponStage);*/
                }else Toast.makeText(getApplicationContext(), "Недостаточно средств", Toast.LENGTH_SHORT).show();

            }
        });
        healthPotionShopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerMoney >= healthPotionsPrice){
                    playerMoney -= healthPotionsPrice;
                    shopMoneyText.setText(Integer.toString(playerMoney));
                    healthPotions++;
                }else Toast.makeText(getApplicationContext(), "Недостаточно средств", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
