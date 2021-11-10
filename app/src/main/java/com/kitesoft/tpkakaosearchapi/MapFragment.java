package com.kitesoft.tpkakaosearchapi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    //Google Map - 개발자사이트 가이드 참고.. 맵 SDK - play-services-maps

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                //MainActivity에 있는 현재 내 위치 정보 얻어와서 그 위치의 지도 보이기
                MainActivity ma = (MainActivity) getActivity();

                //구글 지도 위도/경도 좌표객체
                LatLng me = new LatLng(ma.mylocation.getLatitude(), ma.mylocation.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 16));

                //구글 맵에서 내 위치 탑색 및 표시 허용 [파란색의 동그란 마커 표시됨]
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                //몇가지 지도 UI 설정
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);

                //검색장소들의 마커 표시하기
                for( Place place: ma.searchLocalApiResponse.documents ){
                    double latitude= Double.parseDouble(place.y);
                    double longitue= Double.parseDouble(place.x);

                    //마커옵션 객체를 만들어 기본 설정.
                    MarkerOptions marker= new MarkerOptions().position( new LatLng(latitude, longitue) ).title(place.place_name).snippet(place.distance+"m");
                    googleMap.addMarker(marker).setTag(place.place_url);
                }

                //지도의 마커 InfoWindow 클릭에 반응하기
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(@NonNull Marker marker) {
                        //Toast.makeText(getActivity(), ""+marker.getTag().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getActivity(), PlaceUrlActivity.class);
                        intent.putExtra("place_url", marker.getTag().toString());
                        startActivity(intent);
                    }
                });

            }
        });
    }
}
