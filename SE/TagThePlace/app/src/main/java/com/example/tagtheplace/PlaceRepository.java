package com.example.tagtheplace;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;


public class PlaceRepository{
    private final String conStr = "jdbc:jtds:sqlserver://patent.database.windows.net:1433;databaseName=placedb;user=byeonggon@patent;password=Kyg542317;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;ssl=require";

    public List<PlaceData> getPlacesByTag(String tagName){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
        List<PlaceData> places = new ArrayList<PlaceData>();

        try (Connection con = DriverManager.getConnection(conStr);) {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            // ?에 나중에 값을 설정함
            String query = "SELECT * FROM Place WHERE Tag Like ?";

            //pst객체는 현재 db와 연결되어 있음
            //pst에 쿼리문을 입력함, 아직 실행하지 않음
            PreparedStatement pst = con.prepareStatement(query);

            //pst객체안에 있는 setString함수로 '?'를 치환한다.
            pst.setString(1,  "%" + tagName + "%"); // '% tagName %' 의미는 문장에 tagName 이 중간에 포함되어있다면 가져온다는 의미

            Log.d("db", "query: " + query);
            //연결된 db에 쿼리문을 실행하는 함수
            //검색결과를 rs에 저장
            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                PlaceData newModel = new PlaceData();
                newModel.setId(rs.getInt("Id"));
                newModel.setName(rs.getString("Name"));
                newModel.setTag(rs.getString("Tag"));
                newModel.setLike(rs.getInt("Like"));
                newModel.setDislike(rs.getInt("DisLike"));
                newModel.setLng(rs.getFloat("Lng"));
                newModel.setLat(rs.getFloat("Lat"));

                places.add(newModel); //나중에 반환할 places에 검색된 결과들을 삽입
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return places;
    }


    public PlaceData getPlaceById(int id){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
        PlaceData place = new PlaceData();
        try (Connection con = DriverManager.getConnection(conStr);) {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            // ?에 나중에 값을 설정함
            String query = "SELECT * FROM Place WHERE Id = ?";
            //pst객체는 현재 db와 연결되어 있음
            //pst에 쿼리문을 입력함, 아직 실행하지 않음
            PreparedStatement pst = con.prepareStatement(query);
            //pst객체안에 있는 setString함수로 '?'를 치환한다.
            pst.setString(1, String.valueOf(id)); // '% tagName %' 의미는 문장에 id 가 중간에 포함되어있다면 가져온다는 의미
            Log.d("db", "query: " + query);
            //연결된 db에 쿼리문을 실행하는 함수
            //검색결과를 rs에 저장
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                place.setId(rs.getInt("Id"));
                place.setName(rs.getString("Name"));
                place.setTag(rs.getString("Tag"));
                place.setLike(rs.getInt("Like"));
                place.setDislike(rs.getInt("DisLike"));
                place.setLng(rs.getFloat("Lng"));
                place.setLat(rs.getFloat("Lat"));
            }
            Log.d("왜왜왜왜왜왜", String.valueOf(place.getId()));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return place;
    }



    public void insertModelToDB(PlaceData model){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
        //db와 연결 시도, 이때 연결 문자열 conStr이 객체 생성할때 사용됨
        try (Connection con = DriverManager.getConnection(conStr);) {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            //데이터베이스에 값들을 추가하기 위한 쿼리문
            //id는 자동 증가되게 db에 설정해둠
            // ?에 나중에 값을 설정함
            String query = "INSERT INTO Place VALUES(?,?,?,?,?,?)";

            //쿼리문을 객체 생성할때 입력으로 넣음
            //pst객체는 현재 db와 연결되어있고 쿼리문을 가지고 있음
            PreparedStatement pst = con.prepareStatement(query);

            //?를 차례대로 값을 넣는 코드,
            // 숫자 1은 첫번째 물음표를 뜻함
            pst.setString(1,  model.getName()); // 첫번째 '?'에 model.name을 삽입
            pst.setString(2,  model.getTag());
            pst.setString(3,  String.valueOf(model.getLike()));
            pst.setString(4,  String.valueOf(model.getDislike()));
            pst.setString(5,  String.valueOf(model.getLng()));
            pst.setString(6,  String.valueOf(model.getLat()));

            //연결된 db에 pst객체 안에 내장되어 있는 쿼리문을 실행
            pst.executeUpdate();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //id와 일치하는 행에 있는 'Like'컬럼을 1증가 하는 함수
    public void increaseLikeById(int id){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
        try (Connection con = DriverManager.getConnection(conStr);) {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            // ?에 나중에 값을 설정함
            String query = "Update Place set [Like]=[Like]+1 where id=?";

            //쿼리문을 객체 생성할때 입력으로 넣음
            //pst객체는 현재 db와 연결되어있고 쿼리문을 가지고 있음
            PreparedStatement pst = con.prepareStatement(query);

            //?를 차례대로 값을 넣는 코드,
            // 숫자 1은 첫번째 물음표를 뜻함
            pst.setString(1,  String.valueOf(id));


            //연결된 db에 pst객체 안에 내장되어 있는 쿼리문을 실행
            pst.executeUpdate();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //id와 일치하는 행에 있는 'DisLike'컬럼을 1증가 하는 함수
    public void increaseDisLikeById(int id){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
        try (Connection con = DriverManager.getConnection(conStr);) {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            // ?에 나중에 값을 설정함
            String query = "Update Place set DisLike=DisLike+1 where id=?";

            //쿼리문을 객체 생성할때 입력으로 넣음
            //pst객체는 현재 db와 연결되어있고 쿼리문을 가지고 있음
            PreparedStatement pst = con.prepareStatement(query);

            //?를 차례대로 값을 넣는 코드,
            // 숫자 1은 첫번째 물음표를 뜻함
            pst.setString(1,  String.valueOf(id));


            //연결된 db에 pst객체 안에 내장되어 있는 쿼리문을 실행
            pst.executeUpdate();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //전체행을 출력, 디버깅용 함수
    public void prnAllRow(){
        try (Connection con = DriverManager.getConnection(conStr);) {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            String query = "SELECT * from Place";

            //쿼리문을 객체 생성할때 입력으로 넣음
            //pst객체는 현재 db와 연결되어있고 쿼리문을 가지고 있음
            PreparedStatement pst = con.prepareStatement(query);

            //연결된 db에 pst객체 안에 내장되어 있는 쿼리문을 실행
            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                PlaceData newModel = new PlaceData();
                newModel.setId(rs.getInt("Id"));
                newModel.setName(rs.getString("Name"));
                newModel.setTag(rs.getString("Tag"));
                newModel.setLike(rs.getInt("Like"));
                newModel.setDislike(rs.getInt("DisLike"));
                newModel.setLng(rs.getFloat("Lng"));
                newModel.setLat(rs.getFloat("Lat"));

                Log.d("query", String.format("Id: %d, Name: %s, Tag: %s, Like: %d, DisLike: %d, Lng: %f, Lat: %f",
                        newModel.getId(), newModel.getName(), newModel.getTag(), newModel.getLike(),
                        newModel.getDislike(), newModel.getLng(), newModel.getLat()));
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
