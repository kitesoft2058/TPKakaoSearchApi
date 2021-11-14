package com.kitesoft.tpkakaosearchapi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //카카오 키해시 얻어오기
        String keyHash= Utility.INSTANCE.getKeyHash(this);
        Log.i("keyhash", keyHash);

    }

    public void clickGo(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void clickSignUp(View view) {
        //회원가입화면으로 전환
    }

    public void clickLoginEmail(View view) {
        //이메일로 로그인 화면 전환
    }

    public void clickLoginKakao(View view) {

        //build.gradle: Android Studio Artic Fox(최신) 외 버전
        //settings.gradle: Android Studio Artic Fox(최신) 버전

        UserApiClient.getInstance().loginWithKakaoAccount(this, new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if(oAuthToken!=null){
                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                    //로그인 정보 얻어오기 [사용자 식별정보를 위함]
                    UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                        @Override
                        public Unit invoke(User user, Throwable throwable) {
                            if(user!=null){
                                String userid= user.getId()+"";
                                String email= user.getKakaoAccount().getEmail();

                                //new AlertDialog.Builder(LoginActivity.this).setMessage(email).show();
                                G.userAccount= new UserAccount(userid, email);
                                //main 화면으로 전환.
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                            return null;
                        }
                    });


                }else{
                    Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        });


    }

    public void clickLoginGoogle(View view) {

        // 구글에 검색 [ 안드로이드 구글 로그인 구현 ] -- https://developers.google.com/identity/sign-in/android/start-integrating
        // or 구글에 검색 [ google identity sign in android ]

        // project-structure메뉴에서 검색 가능 [ play-services-auth ]

        // 구글검색 [ google developer console ] 에서 사용자 인증 - oAuth 에서 패키지명, SHA-1 등록. -동의화면부터 설정해야 함.


        // 구글 로그인 옵션 [ id, email 요청 ]
        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestId().requestEmail().build();

        GoogleSignInClient googleClient= GoogleSignIn.getClient(this, gso);

        // 구글 로그인 화면(액티비티)를 실행하는 Intent 객체 만들기
        Intent intent=googleClient.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account= task.getResult(ApiException.class);

                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                String userid= account.getId();
                String email = account.getEmail();

                //new AlertDialog.Builder(LoginActivity.this).setMessage(email).show();
                G.userAccount= new UserAccount(userid, email);
                //main 화면으로 전환.
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();



            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void clickLoginNaver(View view) {

    }

}