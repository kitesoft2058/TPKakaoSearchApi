package com.kitesoft.tpkakaosearchapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PlaceUrlActivity extends AppCompatActivity {

    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_url);

        wv= findViewById(R.id.wv);
        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient());

        wv.getSettings().setJavaScriptEnabled(true);

        String place_url= getIntent().getStringExtra("place_url");
        wv.loadUrl(place_url);
    }

    //장소 정보제공 웹문서에서 해당 장소의 웹사이트로 이동하는 등의 작업시 뒤로가기 버튼 눌렀을때 이전 웹화면이 보이도록..
    @Override
    public void onBackPressed() {
        if(wv.canGoBack()) wv.goBack();
        else super.onBackPressed();
    }
}