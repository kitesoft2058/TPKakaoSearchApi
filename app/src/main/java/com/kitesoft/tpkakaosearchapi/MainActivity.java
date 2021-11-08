package com.kitesoft.tpkakaosearchapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int choiceID=R.id.choice_wc;

    //Kakao search API response object reference
    public ApiResponse apiResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().add(R.id.container, new ListFragment()).commit();

        TabLayout tabLayout= findViewById(R.id.layout_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("LIST")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new ListFragment()).commit();
                }else if(tab.getText().equals("MAP")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MapFragment()).commit();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void clickChoice(View view) {
        if(choiceID!=0) findViewById(choiceID).setBackgroundResource(R.drawable.bg_choice);

        view.setBackgroundResource(R.drawable.bg_choice_select);
        choiceID= view.getId();
    }
}