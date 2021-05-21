package com.example.clickerrpg;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class AuthActivity extends AppCompatActivity {
    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;
    Spinner profileSpinner;
    Context mContext;
    TextView authMnText, authLvlText, authHPText, authMHPText, authWeaponText,
            authPotionsText, chooseProfileText, profileNameText, chooseNameText;
    ArrayList<String> profile_names;
    ArrayList<Integer> count;
    TreeMap<String, Profile> profiles;
    Button newProfileBtn, authBtn, createButton, delBtn;
    ContentValues cv = new ContentValues();
    private int profile_id;
    ArrayAdapter<String> adapter;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    protected void onDestroy(){
        super.onDestroy();
        /*Cursor cursor = mDb.rawQuery("SELECT * FROM profiles", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(0) == profile_id){
                cv.put(DBHelper.COLUMN_MONEY, cursor.getInt(2));
                cv.put(DBHelper.COLUMN_XP, cursor.getInt(3));
                cv.put(DBHelper.COLUMN_HEALTH, cursor.getInt(4));
                cv.put(DBHelper.COLUMN_MAXHEALTH, cursor.getInt(5));
                cv.put(DBHelper.COLUMN_WEAPON, cursor.getInt(6));
                cv.put(DBHelper.COLUMN_POTIONS, cursor.getInt(7));
            }
            cursor.moveToNext();
        }
        cursor.close();
        mDb.update(DBHelper.TABLE, cv, DBHelper.COLUMN_ID + "=" + profile_id, null);
        mDb.close();*/
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mContext = this;
        profile_names = new ArrayList<>();
        profiles = new TreeMap<>();
        count = new ArrayList<>();
        authMnText = findViewById(R.id.authMnText);
        authLvlText = findViewById(R.id.authLvlText);
        authHPText = findViewById(R.id.authHPText);
        authMHPText = findViewById(R.id.authMHPText);
        authWeaponText = findViewById(R.id.authWeaponText);
        authPotionsText = findViewById(R.id.authPotionsText);
        chooseProfileText = findViewById(R.id.chooseProfileText);
        profileNameText = findViewById(R.id.profileNameText);
        //chooseNameText = findViewById(R.id.chooseNameText);
        authBtn = findViewById(R.id.authBtn);
        createButton = findViewById(R.id.createButton);
        delBtn = findViewById(R.id.delBtn);
        newProfileBtn = findViewById(R.id.newProfileBtn);
        profileSpinner = findViewById(R.id.profileSpinner);


        authBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                intent.putExtra("id", profile_id);
                startActivityForResult(intent, 1);
            }
        });

        /*profileSpinner.setVisibility(View.VISIBLE);
        authMnText.setVisibility(View.VISIBLE);
        authLvlText.setVisibility(View.VISIBLE);
        authHPText.setVisibility(View.VISIBLE);
        authMHPText.setVisibility(View.VISIBLE);
        authWeaponText.setVisibility(View.VISIBLE);
        authPotionsText.setVisibility(View.VISIBLE);
        chooseProfileText.setVisibility(View.VISIBLE);
        newProfileBtn.setVisibility(View.VISIBLE);
        authBtn.setVisibility(View.VISIBLE);
        profileNameText.setVisibility(View.INVISIBLE);
        createButton.setVisibility(View.INVISIBLE);
        chooseNameText.setVisibility(View.INVISIBLE);*/
        newProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileSpinner.setVisibility(View.INVISIBLE);
                authMnText.setVisibility(View.INVISIBLE);
                authLvlText.setVisibility(View.INVISIBLE);
                authHPText.setVisibility(View.INVISIBLE);
                authMHPText.setVisibility(View.INVISIBLE);
                authWeaponText.setVisibility(View.INVISIBLE);
                authPotionsText.setVisibility(View.INVISIBLE);
                chooseProfileText.setVisibility(View.INVISIBLE);
                newProfileBtn.setVisibility(View.INVISIBLE);
                authBtn.setVisibility(View.INVISIBLE);
                profileNameText.setVisibility(View.VISIBLE);
                createButton.setVisibility(View.VISIBLE);
                //chooseNameText.setVisibility(View.VISIBLE);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv.put(DBHelper.COLUMN_NAME, profileNameText.getText().toString());
                cv.put(DBHelper.COLUMN_MONEY, 100);
                cv.put(DBHelper.COLUMN_XP, 0);
                cv.put(DBHelper.COLUMN_HEALTH, 100);
                cv.put(DBHelper.COLUMN_MAXHEALTH, 100);
                cv.put(DBHelper.COLUMN_WEAPON, 1);
                cv.put(DBHelper.COLUMN_POTIONS, 0);
                profile_id = (int)mDb.insert(DBHelper.TABLE, null, cv);
                if (profile_id > 0) {
                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                    intent.putExtra("id", profile_id);
                    startActivityForResult(intent, 1);
                }
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDb.delete(DBHelper.TABLE, "_id = ?", new String[]{String.valueOf(profile_id)});
                /*adapter = new ArrayAdapter<>(mContext, R.layout.spinner_item, profile_names.toArray(new String[profile_names.size()]));
                adapter.setDropDownViewResource(R.layout.spinner_item);
                profileSpinner.setAdapter(adapter);
                AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = (String) parent.getItemAtPosition(position);// Получаем выбранный объект
                        authMnText.setText("Золото: " + profiles.get(item).getMoney());
                        authLvlText.setText("Опыт: " + profiles.get(item).getXp());
                        authHPText.setText("Текущее здоровье: " + profiles.get(item).getHealth());
                        authMHPText.setText("Максимальное здоровье: " + profiles.get(item).getMaxHealth());
                        authWeaponText.setText("Уровень оружия: " + profiles.get(item).getWeaponStage());
                        authPotionsText.setText("Количество зелий: " + profiles.get(item).getPotions());
                        profile_id = profiles.get(item).getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                profileSpinner.setOnItemSelectedListener(itemSelectedListener);*/
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

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
            profile_names.add(cursor.getString(1));
            profiles.put(cursor.getString(1), new Profile(cursor.getInt(0), cursor.getString(1),
                    cursor.getInt(2), cursor.getInt(3),
                    cursor.getInt(4), cursor.getInt(5),
                    cursor.getInt(6), cursor.getInt(7)));
            cursor.moveToNext();
        }
        cursor.close();

        profileSpinner = findViewById(R.id.profileSpinner);
        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, profile_names.toArray(new String[profile_names.size()]));
        adapter.setDropDownViewResource(R.layout.spinner_item);
        profileSpinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);// Получаем выбранный объект
                authMnText.setText("Золото: " + profiles.get(item).getMoney());
                authLvlText.setText("Опыт: " + profiles.get(item).getXp());
                authHPText.setText("Текущее здоровье: " + profiles.get(item).getHealth());
                authMHPText.setText("Максимальное здоровье: " + profiles.get(item).getMaxHealth());
                authWeaponText.setText("Уровень оружия: " + profiles.get(item).getWeaponStage());
                authPotionsText.setText("Количество зелий: " + profiles.get(item).getPotions());
                profile_id = profiles.get(item).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                profileSpinner.setVisibility(View.INVISIBLE);
                authMnText.setVisibility(View.INVISIBLE);
                authLvlText.setVisibility(View.INVISIBLE);
                authHPText.setVisibility(View.INVISIBLE);
                authMHPText.setVisibility(View.INVISIBLE);
                authWeaponText.setVisibility(View.INVISIBLE);
                authPotionsText.setVisibility(View.INVISIBLE);
                chooseProfileText.setVisibility(View.INVISIBLE);
                newProfileBtn.setVisibility(View.INVISIBLE);
                authBtn.setVisibility(View.INVISIBLE);
                profileNameText.setVisibility(View.VISIBLE);
                createButton.setVisibility(View.VISIBLE);
                //chooseNameText.setVisibility(View.VISIBLE);
            }
        };
        profileSpinner.setOnItemSelectedListener(itemSelectedListener);
    }
}
