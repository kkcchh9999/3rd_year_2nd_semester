package com.example.tagtheplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener
{

    SupportMapFragment mapFragment;     //지도 프래그먼트
    GoogleMap googleMap;                //지도 객체
    String[] PERMISSIONS = {            //권한(gps)
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    int DEFAULT_ZOOM = 16;              //지도 zoom
    LatLng CITY_HALL = new LatLng(37.5662952, 126.97794509999994);  //위치권한이 없을 때 지도 기본좌표
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //지도 연결
        mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mapView, mapFragment)
                .commit();
        //권한 확인
        if (checkPermission()) {
            mapFragment.getMapAsync(this);
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
    }

    //Permission 결과
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        //길게 클릭
        googleMap.setOnMapLongClickListener( (latLng) -> {
            //뷰 생성
            View dialogView = View.inflate(this, R.layout.alert_add_marker_layout, null);
            EditText editName = dialogView.findViewById(R.id.edit_name);
            EditText editTag = dialogView.findViewById(R.id.edit_tag);
            //다이얼로그 생성
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("마커 추가하기")
                    .setView(dialogView)
                    .setPositiveButton("추가", (dialog, which) -> {
                        if (getMyLocation().latitude - 0.0003 <= latLng.latitude &&
                                latLng.latitude <= getMyLocation().latitude + 0.0003 &&
                                getMyLocation().longitude - 0.0003 <= latLng.longitude &&
                                latLng.longitude <= getMyLocation().longitude + 0.0003
                        ) {
                            Toast.makeText(this, "인근의 장소를 추가해 주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!editName.getText().toString().equals("") && !editTag.getText().toString().equals("")) {
                                //데이터베이스에 추가하는 내용
                                //아래 4개로 데이터베이스에 추가
                                //임시로 마커 추가함
                                //쿼리 보내기
                                Marker marker = googleMap.addMarker(
                                        new MarkerOptions().position(latLng)
                                        .title(editName.getText().toString())
                                        .snippet(editTag.getText().toString())
                                );
                                assert marker != null;
                                marker.showInfoWindow();
                            } else {
                                Toast.makeText(this, "이름, 태그를 입력하세요", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("취소", null)
                    .show();
        });
        //정보창클릭 가능
        googleMap.setOnInfoWindowClickListener(this);
        if (checkPermission()) {
            //내위치 표시
            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getMyLocation(), DEFAULT_ZOOM));
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CITY_HALL, DEFAULT_ZOOM));
        }
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {

        View dialogueView = View.inflate(this, R.layout.alert_evaluation_layout, null);
        Button positiveButton = dialogueView.findViewById(R.id.btn_positive);
        Button negativeButton = dialogueView.findViewById(R.id.btn_negative);
        positiveButton.setOnClickListener(v -> {
            //좋아요 수 늘리기 쿼리
            positiveButton.setVisibility(View.INVISIBLE);
            negativeButton.setVisibility(View.INVISIBLE);
        });
        negativeButton.setOnClickListener(v -> {
            //싫어요 수 늘리기 쿼리
            positiveButton.setVisibility(View.INVISIBLE);
            negativeButton.setVisibility(View.INVISIBLE);
        });
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(marker.getTitle())
                .setMessage(marker.getSnippet())
                .setView(dialogueView)
                .setNegativeButton("돌아가기", null)
                .show();
    }

    @Override   //검색 옵션
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //검색 완료시
                //해당 부분에 쿼리 처리해서 마커 보여주기 처리 예정
                //googleMap.addMarker(데이터 받아와서 마커 설정)
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //검색 단어가 바뀌었을 때
                //해당 부분에 쿼리 처리해서 마커 보여주기 처리 예정
                //googleMap.addMarker(데이터 받아와서 마커 설정)
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //내 위치 찾기
    @SuppressLint("MissingPermission")
    private LatLng getMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
    }

    //권한 여부 확인
    private Boolean checkPermission() {
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}