package com.bupt.monitorpositionsys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.bupt.model.utils.LogUtil;
import com.bupt.monitorpositionsys.R;
import com.bupt.monitorpositionsys.event.LocationEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by Kerry on 15/12/3.
 *
 */
public class MapFragment extends Fragment implements AMapLocationListener,LocationSource {

    private TextView tv_speed;

    //声明变量
    private MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener listener;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private boolean isFirstLocation = true;

    public MapFragment(){
        super();
    }

    //类加载时就初始化
    private static final MapFragment instance = new MapFragment();

    public static MapFragment getInstance(){
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);

        init(view);
        mapView.onCreate(savedInstanceState);// 必须要写
        LogUtil.d("MapFragment","onResume");

        return  view;
    }

    private void init(View view){
        mapView = (MapView) view.findViewById(R.id.mapview_map);
        if(aMap == null){
            aMap = mapView.getMap();
            aMap.setLocationSource(this);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);
            aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
        }

        tv_speed = (TextView) view.findViewById(R.id.tv_map_speed);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("MapFragment","onResume");
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("MapFragment","onPause");
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        deactivate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.d("MapFragment","onSaveInstanceState");
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("MapFragment","onDestroy");
        mapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && listener != null) {
            if (amapLocation.getErrorCode() == 0) {
                listener.onLocationChanged(amapLocation);

                if(isFirstLocation){
                    // 第一次定位
                    isFirstLocation = false;
                    // Camera的定位位置（中心点、缩放等级、倾斜度、旋转角度）
                    float zoom_level = 14;
                    CameraPosition mCameraPosition = new CameraPosition(
                            new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude()),
                            zoom_level, 0, 0);
                    // 定位Camera
                    aMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
                }
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        listener = onLocationChangedListener;
        if(mLocationClient == null){
            mLocationClient = new AMapLocationClient(getActivity());
            mLocationClient.setLocationListener(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否只定位一次,默认为false
            mLocationOption.setOnceLocation(false);
            //设置是否强制刷新WIFI，默认为强制刷新
            mLocationOption.setWifiActiveScan(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(3000);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        if(mLocationClient != null){
            mLocationClient.stopLocation();//停止定位
        }
    }

    public void onEventMainThread(LocationEvent event) {
        if(tv_speed != null){
            tv_speed.setText(event.getSpeed()+"km/h");
        }
    }
}
