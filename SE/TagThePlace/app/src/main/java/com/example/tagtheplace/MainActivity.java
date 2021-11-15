package com.example.tagtheplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener
{
    //데이터베이스 연결 레포지토리
    private final PlaceRepository placeRepository = new PlaceRepository();

    SupportMapFragment mapFragment;     //지도 프래그먼트
    GoogleMap googleMap;                //지도 객체
    String[] PERMISSIONS = {            //권한(gps)
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    SharedPreferences sharedPreferences;
    int DEFAULT_ZOOM = 15;              //지도 zoom
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
            mapFragment.getMapAsync(this::onMapReady);
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }

    }
    //Permission 결과
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mapFragment.getMapAsync(this);  //지도 실행
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap; //지도 변수에 지도 할당

        //길게 클릭, 권한 소유시에
        if (checkPermission()) {
            googleMap.setOnMapLongClickListener( (latLng) -> {
                //뷰 생성
                View dialogView = View.inflate(this, R.layout.alert_add_marker_layout, null);
                EditText editName = dialogView.findViewById(R.id.edit_name);
                EditText editTag = dialogView.findViewById(R.id.edit_tag);
                //다이얼로그 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("마커 추가하기")
                    .setView(dialogView)
                    .setPositiveButton("추가", (dialog, which) -> {   //현재 본인 위치 인근에 마커를 추가할 때
                        if (getMyLocation().latitude - 0.003 <= latLng.latitude &&
                                latLng.latitude <= getMyLocation().latitude + 0.003 &&
                                getMyLocation().longitude - 0.003 <= latLng.longitude &&
                                latLng.longitude <= getMyLocation().longitude + 0.003
                        ) {
                            if (!editName.getText().toString().equals("") && !editTag.getText().toString().equals("")) {
                                //이름 태그가 모두 작성되어 있을 때,
                                //데이터베이스에 추가하는 쿼리 보내기
                                //placeData 객체 만들어서 쿼리 보내기
                                PlaceData placeData = new PlaceData();
                                placeData.setLike(0);
                                placeData.setDislike(0);
                                placeData.setLat((float) latLng.latitude);
                                placeData.setLng((float) latLng.longitude);
                                placeData.setName(editName.getText().toString());
                                placeData.setTag(editTag.getText().toString());

                                placeRepository.insertModelToDB(placeData);
                                Toast.makeText(this, "장소 정보가 추가되었습니다.\n검색을 통해 장소를 찾아보세요", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "이름, 태그를 입력하세요", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //마커 위치가 멀 때
                            Toast.makeText(this, "인근의 장소를 추가해 주세요", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("취소", null)
                    .show();
            });
        }
        //정보창클릭 가능
        googleMap.setOnInfoWindowClickListener(this);

        //permission check
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
    //마커의 정보창 클릭시
        //다이얼로그 뷰와 하위 버튼 선언
        View dialogueView = View.inflate(this, R.layout.alert_evaluation_layout, null);
        Button positiveButton = dialogueView.findViewById(R.id.btn_positive);
        Button negativeButton = dialogueView.findViewById(R.id.btn_negative);

        //공유 레퍼런스, 좋아요 싫어요를 한번만 설정하기 위함
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

        //boolean 변수로 default 는 false, 이전에 평가를 진행한 적 있는지 가져오기
        boolean checked = sharedPreferences.getBoolean(String.valueOf(marker.getZIndex()), false);

        //장소 데이터 마커를 통해 가져오기 (ZIndex 는 마커가 겹칠 때 위로 어떤게 위로 올라오는지에 대한 변수, 크게 중요하지 않은 변수로, 데이터의 ID를 저장하는데 사용했음)
        PlaceData placeData = placeRepository.getPlaceById((int) marker.getZIndex());
        int like = placeData.getLike();
        int dislike = placeData.getDislike();
        AlertDialog dialog;
        if (!checked) { //평가를 진행한 적이 없으면
            dialog = new AlertDialog.Builder(this)
                    .setTitle(marker.getTitle())
                    .setMessage(marker.getSnippet() +
                            "\n좋아요: " + like +
                            "싫어요: " + dislike)
                    .setView(dialogueView)  //버튼 추가
                    .setNegativeButton("돌아가기", null)
                    .show();
        } else {    //있으면
            dialog = new AlertDialog.Builder(this)
                    .setTitle(marker.getTitle())
                    .setMessage(marker.getSnippet() +
                            "\n좋아요: " + like +
                            "싫어요: " + dislike)
                    .setNegativeButton("돌아가기", null)
                    //버튼은 없음
                    .show();
        }
        //버튼 클릭시
        positiveButton.setOnClickListener(v -> {
            int id = 0;
            id = placeData.getId();
            Log.d("id??", String.valueOf(id));
            placeRepository.increaseLikeById(id);   //좋아요 수 증가시키고
            editor.putBoolean(String.valueOf(marker.getZIndex()), true);    //공유 프리퍼런스에 내용 저장 후
            editor.apply();                                                 //적용
            dialog.dismiss();   //dialogue 닫고
            onInfoWindowClick(marker);  //다시 실행 -> 수치변화 반영
        });
        //이하 싫어요 수 증가 과정 동일
        negativeButton.setOnClickListener(v -> {
            int id = 0;
            id = placeData.getId();
            placeRepository.increaseDisLikeById((int) marker.getZIndex());
            editor.putBoolean(String.valueOf(marker.getZIndex()), true);
            editor.apply();
            dialog.dismiss();
            onInfoWindowClick(marker);
        });
    }

    @Override   //검색 옵션
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //검색 버튼을 눌렀을 때
                googleMap.clear();  //기존의 마커 모두 제거
                List<PlaceData> list =  placeRepository.getPlacesByTag(query);  //쿼리를 통한 데이터 가져오기
                for (int i = 0; i < list.size(); i ++) {
                    //가져온 데이터를 통해 마커 추가
                    Marker marker = googleMap.addMarker(
                            new MarkerOptions().position(new LatLng(list.get(i).getLat(), list.get(i).getLng()))
                                    .title(list.get(i).getName())   //이름
                                    .snippet(list.get(i).getTag())  //설명(태그)
                                    .zIndex(list.get(i).getId())    //id
                    );

                    assert marker != null; //해당 라인은 null 체크를 위한 라인, 굳이 필요 X
                    marker.showInfoWindow();
                    mapFragment.getMapAsync(MainActivity.this); //맵 프래그먼트 업데이트 -> 마커 보여주기
                }
                //검색 완료시
                //해당 부분에 쿼리 처리해서 마커 보여주기 처리 예정
                //googleMap.addMarker(데이터 받아와서 마커 설정)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //Do Nothing
                //검색창의 글씨가 변경될 때, 해당 어플리케이션에서는 아무 동작도 하지 않을 예정
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //내 위치 찾기
    @SuppressLint("MissingPermission")
    private LatLng getMyLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        if (lastKnownLocation == null) {
            final double[] loc = new double[2];
            LocationListener locationListener = location -> {
                loc[0] = location.getLatitude();
                loc[1] = location.getLongitude();
            };

            locationManager.requestLocationUpdates(String.valueOf(locationManager.getBestProvider(new Criteria(), true)), 1000, 0, locationListener);
            return new LatLng(loc[0], loc[1]);
        }

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