package com.example.clickerrpg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int  enemyMaxHealth = 100, enemyHealth = 100, spriteCount = 0, enemyAttack = 1,
            enemyAttackSpeed = 3000, reward = 100;
    int attackPower = 10, playerHealth = 100, playerMoney = 100, weaponStage = 1;
    boolean enemyDefeated = false;
    ImageView enemyImage;
    String enemyName = "Enemy";
    TextView enemyHealthText, enemyNameText, testText, playerHealthText, moneyText;
    ProgressBar enemyHealthBar;
    ConstraintLayout constraintLayout1;
    Button nextEnemyButton, shopButton;
    Random random = new Random(1);
    int min = 1;
    int max = 200;
    int diff = max - min;
    int r = random.nextInt(diff + 1);
    ProgressBar playerHealthBar;
    Handler handler;


    class AttackThread extends Thread{
        @Override
        public void run(){
            try {
                Thread.sleep(enemyAttackSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);
        }
    }


    @SuppressLint({"HandlerLeak", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        constraintLayout1 = findViewById(R.id.constraintLayout1);
        constraintLayout1.setBackgroundResource(R.drawable.forest);
        playerHealthText = findViewById(R.id.playerHealthText);
        playerHealthText.setText(Integer.toString(playerHealth));
        moneyText = findViewById(R.id.moneyText);
        moneyText.setText(Integer.toString(playerMoney));
        enemyImage = findViewById(R.id.enemyImage);
        enemyHealthText = findViewById(R.id.enemyHealthText);
        enemyNameText = findViewById(R.id.enemyName);
        testText = findViewById(R.id.testText);
        enemyHealthBar = findViewById(R.id.enemyHealthBar);
        enemyHealthBar.setMax(enemyMaxHealth);
        enemyImage.setImageResource(R.drawable.enemy1);
        enemyNameText.setText(enemyName);
        enemyHealthText.setText(Integer.toString(enemyHealth));
        enemyHealthBar.setProgress(enemyHealth);
        nextEnemyButton = findViewById(R.id.nextEnemyButton);
        shopButton = findViewById(R.id.shopButton);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShopActivity.class);
                intent.putExtra("money", playerMoney);
                intent.putExtra("weapon", weaponStage);
                startActivity(intent);
            }
        });
        Attack();
        new AttackThread().start();
        playerHealthBar = findViewById(R.id.playerHealthBar);
        handler = new Handler(){
          @Override
          public void handleMessage(Message message){
              new AttackThread().start();
              super.handleMessage(message);
              if (!enemyDefeated){
                playerHealth -= enemyAttack;
                playerHealthBar.setProgress(playerHealth);
                playerHealthText.setText(Integer.toString(playerHealth));
          }}
        };

    }

    void spriteAnimation(){
        if (r <= 100){
            enemyImage.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_vertical));
        }else enemyImage.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_horizontal));
    }


    void Attack(){
        enemyImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                enemyHealth -= attackPower;
                switch (spriteCount){
                    case 0:
                        spriteCount = 1;
                        enemyImage.setImageResource(R.drawable.enemy2);
                        break;
                    case 1:
                        spriteCount = 2;
                        enemyImage.setImageResource(R.drawable.enemy3);
                        break;
                    case 2:
                        spriteCount = 3;
                        enemyImage.setImageResource(R.drawable.enemy4);
                        break;
                    case 3:
                        spriteCount = 4;
                        enemyImage.setImageResource(R.drawable.enemy5);
                        break;
                    case 4:
                        spriteCount = 0;
                        enemyImage.setImageResource(R.drawable.enemy1);
                        break;
                }
                if (enemyHealth <= 0){
                    enemyDefeated = true;
                    enemyImage.setVisibility(View.INVISIBLE);
                    enemyImage.setClickable(false);
                    nextEnemyButton.setVisibility(View.VISIBLE);
                    nextEnemyButton.setClickable(true);
                    enemyHealthText.setVisibility(View.INVISIBLE);
                    enemyHealthBar.setVisibility(View.INVISIBLE);
                    enemyNameText.setVisibility(View.INVISIBLE);
                    playerMoney += reward;
                    moneyText.setText(Integer.toString(playerMoney));
                }
                enemyHealthText.setText(Integer.toString(enemyHealth));
                enemyHealthBar.setProgress(enemyHealth);
                spriteAnimation();
                r = random.nextInt(diff + 1);
            }
        }
        );
        nextEnemyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enemyDefeated = false;
                enemyImage.setVisibility(View.VISIBLE);
                enemyImage.setClickable(true);
                nextEnemyButton.setVisibility(View.INVISIBLE);
                nextEnemyButton.setClickable(false);
                enemyHealthText.setVisibility(View.VISIBLE);
                enemyHealthBar.setVisibility(View.VISIBLE);
                enemyNameText.setVisibility(View.VISIBLE);
                enemyHealth = 100;
                enemyHealthText.setText(Integer.toString(enemyHealth));
                enemyHealthBar.setProgress(enemyHealth);
            }
        });
    }
}