package com.kitesoft.tpkakaosearchapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    int choiceID = R.id.choice_wc;

    //카카오 검색에 필요한 요청 데이터 : query(검색장소명), x(경도:longitude), y(위도:latitude)
    //1. 검색장소명
    String searchQuery="화장실"; //앱 초기 검색어 - 내주변 개방 화장실
    //2. 현재 내위치 정보 객체 (위도,경도 정보를 멤버로 보유)
    Location mylocation= null;


    //[ Google Fused Location API 사용 :  play-services-location ]
    FusedLocationProviderClient providerClient;

    //Kakao search API response object reference
    public SearchLocalApiResponse searchLocalApiResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().add(R.id.container, new ListFragment()).commit();

        TabLayout tabLayout = findViewById(R.id.layout_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("LIST")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new ListFragment()).commit();
                } else if (tab.getText().equals("MAP")) {
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

        //내 위치 정보제공은 사용자의 동적퍼미션 필요
        String[] permissions= new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if( checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED ) {
            requestPermissions(permissions, 10);
        }else{
            //위치정보에 대해 이미 허용한 적이 있다면 곧바로 내위치 요청기능 호출.
            requestMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==10 && grantResults[0]==PackageManager.PERMISSION_GRANTED) requestMyLocation(); //내 위치 얻어오는 기능 함수 호출
        else Toast.makeText(MainActivity.this, "내 위치정보를 제공하지 않아 검색기능을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();

    }

    void requestMyLocation(){

        providerClient = LocationServices.getFusedLocationProviderClient(this);

        //위치검색 설정값 객체
        LocationRequest request = LocationRequest.create();
        request.setInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //정확도 우선

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        providerClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());

    }

    //위치정보 검색완료 콜백객체
    LocationCallback locationCallback= new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            mylocation= locationResult.getLastLocation();

            //위치 탐색이 끝났으니 내 위치 정보 업데이트는 이제 종료
            providerClient.removeLocationUpdates(locationCallback);

            //위치정보를 얻었으니 이제 검색 시작
            searchPlace();
        }
    };

    //카카오 키워드 로컬 검색 API 호출 메소드
    void searchPlace() {

        //레트로핏으로 검색
        Retrofit.Builder builder= new Retrofit.Builder();
        builder.baseUrl("https://dapi.kakao.com");
        builder.addConverterFactory(ScalarsConverterFactory.create());
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit= builder.build();

        RetrofitService retrofitService= retrofit.create(RetrofitService.class);
        //Call<String> call= retrofitService.searchPlace(searchQuery, mylocation.getLongitude()+"", mylocation.getLatitude()+"");
        Call<SearchLocalApiResponse> call= retrofitService.searchPlace(searchQuery, mylocation.getLongitude()+"", mylocation.getLatitude()+"");
        call.enqueue(new Callback<SearchLocalApiResponse>() {
            @Override
            public void onResponse(Call<SearchLocalApiResponse> call, Response<SearchLocalApiResponse> response) {
                searchLocalApiResponse= response.body();

                //PlaceMeta meta= searchLocalApiResponse.meta;
                //List<Place> documents= searchLocalApiResponse.documents;
                //new AlertDialog.Builder(MainActivity.this).setMessage(meta.total_count +"\n"+documents.get(0).place_name+"\n"+ documents.get(0).distance).show();

                //무조건 먼저 ListFragment 로 보여주기 - 응답받은 결과 객체 전달해주기.
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ListFragment()).commit();

            }

            @Override
            public void onFailure(Call<SearchLocalApiResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "서버 오류가 있습니다.\n잠시뒤에 다시 시도해 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
            }
        });
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String s= response.body();
//                new AlertDialog.Builder(MainActivity.this).setMessage(s).show();
//
//                //모든 검색이 끝났으니 내 위치 정보 업데이트는 이제 종료
//                providerClient.removeLocationUpdates(locationCallback);
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "서버 오류가 있습니다.\n잠시뒤에 다시 시도해 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
//            }
//        });

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

        //초이스한 것에 따라 검색장로를 변경하여 다시 장소요청
        switch (choiceID){
            case R.id.choice_wc: searchQuery="화장실"; break;
            case R.id.choice_movie: searchQuery="영화관"; break;
            case R.id.choice_gas: searchQuery="주유소"; break;
            case R.id.choice_ev: searchQuery="전기차충전소"; break;
            case R.id.choice_pharmacy: searchQuery="약국"; break;
            case R.id.choice1: searchQuery="맛집"; break;
            case R.id.choice2: searchQuery="맛집"; break;
            case R.id.choice3: searchQuery="맛집"; break;
            case R.id.choice4: searchQuery="맛집"; break;
            case R.id.choice5: searchQuery="맛집"; break;
        }
        //새로운 검색 요청.
        searchPlace();
    }
}