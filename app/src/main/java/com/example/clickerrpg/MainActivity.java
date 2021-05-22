package com.example.clickerrpg;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;
    int  enemyMaxHealth = 100, enemyHealth = 100, enemyAttack = 10,
            enemyAttackSpeed = 3000, rewardMoney = 100, rewardXp = 50;
    int attackPower = 10, playerLevel = 1, playerXp = 0,
            playerHealth = 100, playerMoney = 100, weaponStage = 1, healthPotions = 0,
            playerMaxHealth = 100, HEALTHPOTIONPLUS = 50;
    boolean enemyDefeated = false, isInShop = false, gameOver = false;
    ImageView enemyImage, plusHealthImage, shopImage, authImage;
    String enemyName = "Enemy", playerName;
    TextView enemyHealthText, enemyNameText, playerHealthText, moneyText,
            healthPotionCounterText, swordLevelText, levelText, expText;
    ProgressBar enemyHealthBar, playerHealthBar, levelBar;
    ConstraintLayout constraintLayout1;
    Button nextEnemyButton;
    Random random = new Random(1), random_id = new Random();
    int min = 1, max = 200, diff = max - min;
    int r = random.nextInt(diff + 1);
    int id, enemy_id, MAXENEMYID = 5;
    Handler handler;
    TreeMap<Integer, Profile> profiles;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ContentValues cv = new ContentValues();


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            playerMoney = data.getIntExtra("money", playerMoney);
            weaponStage = data.getIntExtra("weapon", weaponStage);
            healthPotions = data.getIntExtra("healthPotions", healthPotions);
            attackPower = weaponStage * 2;
            moneyText.setText(String.valueOf(playerMoney));
            swordLevelText.setText(String.valueOf(weaponStage));
            healthPotionCounterText.setText(String.valueOf(healthPotions));
            isInShop = false;
        }
    }


    @SuppressLint({"HandlerLeak", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        constraintLayout1 = findViewById(R.id.constraintLayout1);
        playerHealthText = findViewById(R.id.playerHealthText);
        plusHealthImage = findViewById(R.id.plusHealthImage);
        moneyText = findViewById(R.id.moneyText);
        healthPotionCounterText = findViewById(R.id.healthPotionCounterText);
        swordLevelText = findViewById(R.id.swordLevelText);
        enemyImage = findViewById(R.id.enemyImage);
        enemyHealthText = findViewById(R.id.enemyHealthText);
        enemyNameText = findViewById(R.id.enemyName);
        enemyHealthBar = findViewById(R.id.enemyHealthBar);
        shopImage = findViewById(R.id.shopImage);
        levelBar = findViewById(R.id.levelBar);
        levelText = findViewById(R.id.lvlText);
        expText = findViewById(R.id.expText);
        nextEnemyButton = findViewById(R.id.nextEnemyButton);
        playerHealthBar = findViewById(R.id.playerHealthBar);
        authImage = findViewById(R.id.AuthImage);
        profiles = new TreeMap<>();
        enemies = new ArrayList<>();
        id = getIntent().getIntExtra("id", 1);

        constraintLayout1.setBackgroundResource(R.drawable.forest);
        shopImage.setImageResource(R.drawable.shop);
        mDBHelper = new DBHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        Cursor cursor = mDb.rawQuery("SELECT * FROM profiles", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(0) == id){
                playerName = cursor.getString(1);
                playerMoney = cursor.getInt(2);
                playerXp = cursor.getInt(3);
                playerLevel = 1;
                int[] lvls = countLvl(playerXp, playerLevel);
                playerLevel += lvls[0];
                Update();
                /*for (int i = 100, j = playerXp; j >= 0; i+=100){
                    j -= i;
                    playerLevel++;
                }*/
                playerHealth = cursor.getInt(4);
                playerMaxHealth = cursor.getInt(5);
                weaponStage = cursor.getInt(6);
                healthPotions = cursor.getInt(7);
                attackPower = weaponStage * 2;
            }
            cursor.moveToNext();
        }
        cursor.close();
        if (playerLevel >= MAXENEMYID)enemy_id = random_id.nextInt(MAXENEMYID);
        else if (playerLevel > 0)
            enemy_id = random_id.nextInt(playerLevel);
        else enemy_id = random_id.nextInt(1);
        cursor = mDb.rawQuery("SELECT * FROM enemies", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(0) == enemy_id) {
                enemyName = cursor.getString(1);
                rewardMoney = cursor.getInt(3);
                rewardXp = cursor.getInt(4);
                enemyMaxHealth = cursor.getInt(5);
                enemyHealth = cursor.getInt(5);
                enemyAttack = cursor.getInt(6);
                enemyAttackSpeed = cursor.getInt(7);
                enemyImage.setImageBitmap(BitmapFactory.decodeByteArray(cursor.getBlob(8), 0, cursor.getBlob(8).length));
            }
            cursor.moveToNext();
        }
        cursor.close();
        enemyHealthBar.setMax(enemyHealth);
        enemyNameText.setText(enemyName);
        enemyHealthText.setText(Integer.toString(enemyHealth));
        enemyHealthBar.setProgress(enemyHealth);
        Update();

        
        authImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                SaveResult();
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        shopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInShop = true;
                Intent intent = new Intent(MainActivity.this, ShopActivity.class);
                intent.putExtra("money", playerMoney);
                intent.putExtra("weapon", weaponStage);
                intent.putExtra("healthPotions", healthPotions);
                SaveResult();
                startActivityForResult(intent, 1);
            }
        });
        plusHealthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (healthPotions > 0){
                    healthPotions--;
                    healthPotionCounterText.setText(String.valueOf(healthPotions));
                    playerHealth += HEALTHPOTIONPLUS;
                    if (playerHealth > playerMaxHealth)playerHealth = playerMaxHealth;
                    playerHealthBar.setMax(playerMaxHealth);
                    playerHealthBar.setProgress(playerHealth);
                    playerHealthText.setText(String.valueOf(playerHealth));
                    SaveResult();
                }
            }
        });

        Attack();
        new AttackThread().start();
        handler = new Handler(){
          @Override
          public void handleMessage(Message message){
              new AttackThread().start();
              super.handleMessage(message);
              if (!enemyDefeated && !isInShop && !gameOver){
                playerHealth -= enemyAttack;
                if (playerHealth <= 0)GameOver();
                SaveResult();
                Update();
          }}
        };

    }

    int[] countLvl(int xp, int lvl){
        int[] rtrn = new int[2];
        while (xp >= lvl * 100 * 2){
            xp -= lvl * 100 * 2;
            lvl++;
        }
        rtrn[0] = lvl - 1;
        rtrn[1] = xp;
        return rtrn;
    }

    protected void onPause() {
        super.onPause();
        SaveResult();
        mDb.update(DBHelper.TABLE, cv, DBHelper.COLUMN_ID + "=" + id, null);
    }

    protected void onResume() {
        super.onResume();
        SaveResult();
        mDb.update(DBHelper.TABLE, cv, DBHelper.COLUMN_ID + "=" + id, null);
    }

    void GameOver(){
        Toast.makeText(this, "Вы проиграли", Toast.LENGTH_LONG).show();
        cv.put(DBHelper.COLUMN_MONEY, 100);
        cv.put(DBHelper.COLUMN_XP, 0);
        cv.put(DBHelper.COLUMN_HEALTH, 100);
        cv.put(DBHelper.COLUMN_MAXHEALTH, 100);
        cv.put(DBHelper.COLUMN_WEAPON, 1);
        cv.put(DBHelper.COLUMN_POTIONS, 0);
        mDb.update(DBHelper.TABLE, cv, DBHelper.COLUMN_ID + "=" + id, null);
        playerMoney = 100;
        playerXp = 0;
        playerHealth = 100;
        playerMaxHealth = 100;
        weaponStage = 1;
        playerLevel = 1;
        healthPotions = 0;
        enemyDefeated = true;
        enemyImage.setVisibility(View.INVISIBLE);
        enemyImage.setClickable(false);
        nextEnemyButton.setVisibility(View.VISIBLE);
        nextEnemyButton.setClickable(true);
        enemyHealthText.setVisibility(View.INVISIBLE);
        enemyHealthBar.setVisibility(View.INVISIBLE);
        enemyNameText.setVisibility(View.INVISIBLE);
        Update();
    }

    void Update(){
        playerHealthText.setText(Integer.toString(playerHealth));
        plusHealthImage.setImageResource(R.drawable.plus);
        moneyText.setText(Integer.toString(playerMoney));
        healthPotionCounterText.setText(String.valueOf(healthPotions));
        swordLevelText.setText(String.valueOf(weaponStage));
        levelText.setText(String.valueOf(playerLevel));;
        levelBar.setMax(playerLevel * 100 * 2);

        levelBar.setProgress(playerXp);
        playerHealthBar.setMax(playerMaxHealth);
        playerHealthBar.setProgress(playerHealth);
        expText.setText(playerXp + "/" + levelBar.getMax());
        /*Toast.makeText(getApplicationContext(),String.valueOf(playerLevel), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),String.valueOf(Power(2, playerLevel - 1)), Toast.LENGTH_SHORT).show();*/
    }

    void spriteAnimation(){
        if (r <= 100){
            enemyImage.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.shake_vertical));
        }else enemyImage.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.shake_horizontal));
    }

    void SaveResult(){
        cv.put(DBHelper.COLUMN_MONEY, playerMoney);
        cv.put(DBHelper.COLUMN_XP, playerXp);
        cv.put(DBHelper.COLUMN_HEALTH, playerHealth);
        cv.put(DBHelper.COLUMN_MAXHEALTH, playerMaxHealth);
        cv.put(DBHelper.COLUMN_WEAPON, weaponStage);
        cv.put(DBHelper.COLUMN_POTIONS, healthPotions);
        mDb.update(DBHelper.TABLE, cv, DBHelper.COLUMN_ID + "=" + id, null);
    }

    int Power(int n, int p){
        if (p != 0){
            for (int i = 1; i < p; i++){
                n*=n;
            }
        }else n = 1;
        //Toast.makeText(getApplicationContext(), String.valueOf(p), Toast.LENGTH_SHORT).show();
        return n;
    }

    void Attack(){
        enemyImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                enemyHealth -= attackPower;
                if (enemyHealth <= 0){
                    Toast.makeText(getApplicationContext(), "Вы получили " + rewardMoney +
                            " Золота и " + rewardXp + " Опыта", Toast.LENGTH_SHORT).show();
                    enemyDefeated = true;
                    enemyImage.setVisibility(View.INVISIBLE);
                    enemyImage.setClickable(false);
                    nextEnemyButton.setVisibility(View.VISIBLE);
                    nextEnemyButton.setClickable(true);
                    enemyHealthText.setVisibility(View.INVISIBLE);
                    enemyHealthBar.setVisibility(View.INVISIBLE);
                    enemyNameText.setVisibility(View.INVISIBLE);
                    playerMoney += rewardMoney;
                    playerXp += rewardXp;
                    if (playerXp >= playerLevel * 100 * 2){
                        playerLevel++;
                        Toast.makeText(getApplicationContext(), "Вы получили новый уровень." +
                                " Ваш уровень: " + playerLevel, Toast.LENGTH_SHORT).show();
                        levelBar.setMax(playerLevel * 100 * 2);
                        levelText.setText(String.valueOf(playerLevel));
                    }
                    levelBar.setProgress(playerXp - (playerLevel - 1) * 100);
                    moneyText.setText(Integer.toString(playerMoney));
                }
                Update();
                enemyHealthText.setText(Integer.toString(enemyHealth));
                enemyHealthBar.setProgress(enemyHealth);
                spriteAnimation();
                r = random.nextInt(diff + 1);
                SaveResult();
            }
        }
        );
        nextEnemyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerLevel >= MAXENEMYID)enemy_id = random_id.nextInt(MAXENEMYID);
                else enemy_id = random_id.nextInt(playerLevel);
                Cursor cursor = mDb.rawQuery("SELECT * FROM enemies", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if (cursor.getInt(0) == enemy_id) {
                        enemyName = cursor.getString(1);
                        rewardMoney = cursor.getInt(3);
                        rewardXp = cursor.getInt(4);
                        enemyMaxHealth = cursor.getInt(5);
                        enemyHealth = cursor.getInt(5);
                        enemyAttack = cursor.getInt(6);
                        enemyAttackSpeed = cursor.getInt(7);
                        enemyImage.setImageBitmap(BitmapFactory.decodeByteArray(cursor.getBlob(8), 0, cursor.getBlob(8).length));
                    }
                    cursor.moveToNext();
                }
                enemyDefeated = false;
                enemyImage.setVisibility(View.VISIBLE);
                enemyImage.setClickable(true);
                nextEnemyButton.setVisibility(View.INVISIBLE);
                nextEnemyButton.setClickable(false);
                enemyHealthText.setVisibility(View.VISIBLE);
                enemyHealthBar.setVisibility(View.VISIBLE);
                enemyNameText.setVisibility(View.VISIBLE);
                enemyHealth = enemyMaxHealth;
                enemyHealthBar.setMax(enemyMaxHealth);
                enemyHealthText.setText(String.valueOf(enemyHealth));
                enemyHealthBar.setProgress(enemyHealth);
                enemyNameText.setText(enemyName);
            }
        });


    }
}


/*TODO:
    Сделать GAME OVER
    Добавить новые фоны
    Починить систему уровней
*/