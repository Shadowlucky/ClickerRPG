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

public class ShopActivity extends Activity {
    int weaponStage, weaponPrice, playerMoney;
    Button backButton;
    TextView shopMoneyText;
    ImageView weaponImage;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        backButton = findViewById(R.id.backButton);
        shopMoneyText = findViewById(R.id.shopMoneyText);
        weaponImage = findViewById(R.id.weaponImage);
        weaponImage.setImageResource(R.drawable.sword);
        weaponStage = getIntent().getIntExtra("weapon", 0);
        weaponPrice = (int)(weaponStage * 150);
        playerMoney = getIntent().getIntExtra("money", 0);
        shopMoneyText.setText(Integer.toString(playerMoney));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Intent intent = new Intent(ShopActivity.this, MainActivity.class);
                //playerMoney = intent.getIntExtra("money", 0);
                //moneyText.setText(Integer.toString(playerMoney));
                //weaponStage = intent.getIntExtra("weapon", 1);
                //attackPower = weaponStage * 10;
                Intent intent = new Intent();
                intent.putExtra("money", playerMoney);
                intent.putExtra("weapon", weaponStage);
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
                    /*getIntent().putExtra("money", playerMoney);
                    getIntent().putExtra("weapon", weaponStage);*/
                }else Toast.makeText(getApplicationContext(), "Недостаточно средств", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
