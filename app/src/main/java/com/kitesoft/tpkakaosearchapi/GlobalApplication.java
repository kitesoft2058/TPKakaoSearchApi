package com.kitesoft.tpkakaosearchapi;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //kakao init
        KakaoSdk.init(this, "1ba9b88628d74185a4de1c520fb90f20");
    }
}
