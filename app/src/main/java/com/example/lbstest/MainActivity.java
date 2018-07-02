package com.example.lbstest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private LocationClient locationClient;
    private BaiduMap baiduMap;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        locationClient=new LocationClient ( getApplicationContext () );
        locationClient.registerLocationListener ( new setDistrict () );
        SDKInitializer.initialize ( getApplicationContext () );
        setContentView ( R.layout.activity_main );
        AlertDialog.Builder builder=new AlertDialog.Builder ( this );
        builder.setMessage ( "  百度地图调用成功，若出现定位不准的情况，请关掉应用，重新打开即可准确定位" );
        AlertDialog AD=builder.create ();
        AD.show ();
        mapView=(MapView)findViewById ( R.id.bmapView );
        baiduMap=mapView.getMap ();
        baiduMap.setMyLocationEnabled ( true );
        List<String> permissonList=new ArrayList<> (  );
        if(ContextCompat.checkSelfPermission ( MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED)
        {
            permissonList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission ( MainActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
        {
            permissonList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission ( MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED)
        {
            permissonList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissonList.isEmpty ())
        {
            String[]permisssions=permissonList.toArray (new String[permissonList.size ()]);
            ActivityCompat.requestPermissions ( MainActivity.this,permisssions,1 );
        }
        requestLocation ();
    }

    private void requestLocation() {
        locationClient.start ();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        mapView.onDestroy ();
    }

    @Override
    protected void onResume() {
        super.onResume ();
        mapView.onResume ();
    }

    @Override
    protected void onPause() {
        super.onPause ();
        mapView.onPause ();
    }
    private void navigateToWhereYouAre(BDLocation location){
        LatLng ll=new LatLng ( location.getLatitude (),location.getLongitude () );
        MapStatusUpdate mapStatusUpdate= MapStatusUpdateFactory.newLatLng(ll);
        baiduMap.animateMapStatus ( mapStatusUpdate );
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(18f);
        baiduMap.animateMapStatus(mapStatusUpdate);
        if (location.getLatitude () < 24.610804 && location.getLatitude () > 24.608266 && location.getLongitude () > 118.086968 && location.getLongitude () < 118.091504)
                    Toast.makeText ( MainActivity.this, "在指定教学区域内", Toast.LENGTH_LONG ).show ();
                else
                    Toast.makeText ( MainActivity.this, "不在指定教学区域内", Toast.LENGTH_LONG ).show ();
        MyLocationData.Builder builder=new MyLocationData.Builder ();
        builder.latitude ( location.getLatitude () );
        builder.longitude ( location.getLongitude () );
        MyLocationData myLocationData=builder.build ();
        baiduMap.setMyLocationData ( myLocationData );
    }
    public class setDistrict implements BDLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
                double latitude = bdLocation.getLatitude ();
                double longtitude = bdLocation.getLongitude ();
                btn=(Button)findViewById ( R.id.btn );
                btn.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        navigateToWhereYouAre(bdLocation);
                    }
                });
        }
    }

}
