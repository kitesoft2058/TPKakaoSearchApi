package com.kitesoft.tpkakaosearchapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash); //테마를 통해 화면 구현할 것임.

        //app_logo.png 이미지는 "미리캔버스 - 로고/프로필 [검색:지도] 에서 수정하여 제작

        //단순히 다음 액티비티를 실행하며 일정시간 테마의 그림/색상 이 보여진 후 전환됨. 딜레이 시간 지정하고 싶다면.. Handler의 postDelayed()사용
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity( new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 1500);

    }
}