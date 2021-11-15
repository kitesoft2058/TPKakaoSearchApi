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
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        //developers.naver.com  -- 네아로. 개발환경 [ 모바일앱 > Android ]
        // naver login sdk 는 별도의 .aar 압축파일로 제공됨. [ 개발자 사이트 > 안드로이드 개발가이드 > 초기상단 [라이브러리 보기]에서 다운로
        // 이를 libs폴더안에 넣고 build.gradle에서 라이브러리를 추가.
        // [주의!! 현재 naver SDK는 target버전을 android 12(api 31)로 하면 exported="true"속성 문제로 run 실행이 안됨.(Android 12버전부터 액티비티들은 필수로 exported속성을 명시(explicit)해야함.)
        // [현재 해결책은 target버전을 30으로 낮추어야 함 - compile SDK 는 그대로 31이어도 됨]

        //1. 네이버 로그인 인스턴스를 초기화
        OAuthLogin oAuthLogin= OAuthLogin.getInstance();
        oAuthLogin.init(this, "HGe4RgZIAuf7XBx6QOmq","X0tjqkfDuR","당근장");
        //OAUTH_CLIENT_ID: 애플리케이션 등록 후 발급받은 클라이언트 아이디
        //OAUTH_CLIENT_SECRET: 애플리케이션 등록 후 발급받은 클라이언트 시크릿
        //OAUTH_CLIENT_NAME: 네이버 앱의 로그인 화면에 표시할 애플리케이션 이름. 모바일 웹의 로그인 화면을 사용할 때는 서버에 저장된 애플리케이션 이름이 표시됩니다.

        //2. 네이버 개발자 센터에서 [내 애플리케이션 등록] - 패키지명만 지정과 동의항목선택만 하면됨. 개발중 상태일때는 "멤버관리"에 등록된 아이디만 사용가능함 (여러 앱 중복등록 가능)

        //3. 로그인버튼 구현
        //방법1. <com.nhn.android.naverlogin.ui.view.OAuthLoginButton> 을 이용하기
        //방법2. startOAuthLoginActivity() 메서드를 이용한 로그인 [ 원하는 버튼모양 가능 ]
        oAuthLogin.startOauthLoginActivity(this, new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                //Toast.makeText(MainActivity.this, ""+success, Toast.LENGTH_SHORT).show();
                if(success){
                    //로그인에 성공하면 [접속토큰] 받기 (사용자정보를 얻기위한 일종의 승차권같은 값 : 요청할때 마다 달라지는 1회용 값 )
                    String accessToken= oAuthLogin.getAccessToken(LoginActivity.this);
                    Log.i("token", accessToken+"");
                    //new AlertDialog.Builder(MainActivity.this).setMessage(accessToken+"").show();

                    //네아로는 이 토큰값을 Request 요청헤더값으로 하여 REST API로 사용자 정보를 가져올 수 있음.
                    //개발자 사이트 왼쪽 사이드 메뉴 [개발자가이드 > 네이버로그인 개발가이드 > " 3.4.5 접근 토큰을 이용하여 프로필 API 호출하기 " 에 GET api URL주소 있음.
                    // 요청예시)
                    // curl  -XGET "https://openapi.naver.com/v1/nid/me" \
                    //      -H "Authorization: Bearer AAAAPIuf0L+qfDkMABQ3IJ8heq2mlw71DojBj3oc2Z6OxMQESVSrtR0dbvsiQbPbP1/cxva23n7mQShtfK4pchdk/rc="

                    Retrofit retrofit= new Retrofit.Builder().baseUrl("https://openapi.naver.com").addConverterFactory(GsonConverterFactory.create()).build();
                    retrofit.create(RetrofitService.class).getUserInfo("Bearer "+accessToken).enqueue(new retrofit2.Callback<UserInfoResponse>() {
                        @Override
                        public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                            UserInfoResponse userInfo= response.body();

//                            StringBuffer buffer= new StringBuffer();
//                            buffer.append(userInfo.response.nickname+"\n");
//                            buffer.append(userInfo.response.profile_image+"\n");
//                            buffer.append(userInfo.response.email+"\n");
//                            new AlertDialog.Builder(LoginActivity.this).setMessage(buffer.toString()).show();

                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                            String userid= userInfo.response.id;
                            String email = userInfo.response.email;

                            //new AlertDialog.Builder(LoginActivity.this).setMessage(email).show();
                            G.userAccount= new UserAccount(userid, email);
                            //main 화면으로 전환.
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Call<UserInfoResponse> call, Throwable t) {

                        }
                    });

                }
                //new AlertDialog.Builder(MainActivity.this).setMessage(oAuthLogin.getLastErrorDesc(MainActivity.this)).show();
            }
        });







    }

}