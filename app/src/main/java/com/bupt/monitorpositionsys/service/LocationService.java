package com.bupt.monitorpositionsys.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bupt.model.utils.LogUtil;
import com.bupt.model.utils.TimeUtil;
import com.bupt.monitorpositionsys.db.Path;
import com.bupt.monitorpositionsys.db.PathDetail;
import com.bupt.monitorpositionsys.event.LocationEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by Kerry on 15/12/3.
 */
public class LocationService extends Service implements AMapLocationListener{

    private Path path;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        startLocation();

        // 开始记录
        path = new Path();
        path.setDate(TimeUtil.getToday());
        path.save();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocation();
    }

    private void startLocation(){
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(10000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    private void stopLocation(){
        if(mLocationClient != null){
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                if(amapLocation.getAccuracy() < 50){
                    LogUtil.d("LocationService","定位成功，数据保存");
                    // 保存定位点
                    PathDetail pathDetail = new PathDetail();
                    pathDetail.setLatitude(amapLocation.getLatitude());
                    pathDetail.setLongtitude(amapLocation.getLongitude());
                    pathDetail.setPath(path);
                    pathDetail.save();

                    EventBus.getDefault().post(new LocationEvent(amapLocation.getSpeed()));
                }
            }
        }
    }
}
