package com.bupt.monitorpositionsys.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.bupt.model.utils.LogUtil;
import com.bupt.model.utils.SPUtil;
import com.bupt.model.utils.TimeUtil;
import com.bupt.model.utils.assist.Check;
import com.bupt.model.utils.assist.Toastor;
import com.bupt.monitorpositionsys.R;
import com.bupt.monitorpositionsys.event.LocationEvent;
import com.bupt.monitorpositionsys.service.LocationService;
import com.bupt.monitorpositionsys.util.Utils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Kerry on 15/11/26.
 *
 * 首页
 */
public class MainActivity extends SerialActivity implements View.OnClickListener ,
        AMapLocationListener,LocationSource,AMap.InfoWindowAdapter,AMapNaviViewListener,AMapNaviListener {

    private TextView tv_time,tv_speed;
    //声明变量
    private MapView mapView;
    private AMap aMap;

    private LocationSource.OnLocationChangedListener listener;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private boolean isFirstLocation = true;

    private Marker naviMarker;
    private LatLng currentLatlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        mapView.onCreate(savedInstanceState);
        startService(new Intent(MainActivity.this, LocationService.class)); // 开启定位服务
        EventBus.getDefault().register(this);
//        open(); // 打开串口
    }

    private void initUI() {
        tv_speed = (TextView) findViewById(R.id.tv_main_speed);

        tv_time = (TextView) findViewById(R.id.tv_main_time);
        tv_time.setText(TimeUtil.getSysTime());
        handler.sendEmptyMessageAtTime(1, 60 * 1000);

        Button btn_alarm = (Button) findViewById(R.id.btn_main_alarm);
        btn_alarm.setOnClickListener(this);

        mapView = (MapView) findViewById(R.id.mapview_main);
        if(aMap == null){
            aMap = mapView.getMap();
            aMap.setLocationSource(this);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);
            aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
            aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if(naviMarker != null){
                        naviMarker.remove();
                    }
                }
            });
            aMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    if(naviMarker != null){
                        naviMarker.remove();
                    }

                    // 标注起始点
                    MarkerOptions options = new MarkerOptions().anchor(0.5f, 0.5f)
                            .visible(true).position(latLng)
                            .title("点击选择为目的地")
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.mipmap.destination_point))
                            .draggable(false);
                    options.setFlat(true);
                    naviMarker = aMap.addMarker(options);
                    naviMarker.showInfoWindow();
                }
            });
            aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
            AMapNavi.getInstance(this).setAMapNaviListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        // 删除导航监听
        AMapNavi.getInstance(this).removeAMapNaviListener(this);
        EventBus.getDefault().unregister(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_time.setText(TimeUtil.getSysTime());
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main_alarm:
                // 一键拨通电话
                String phone = SPUtil.getStringValue(getApplicationContext(),"phone",null);
                if(Check.isEmpty(phone)){
                    new Toastor(getApplicationContext()).showToast("请先设置报警电话");
                    return;
                }
//
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                Uri data = Uri.parse("tel:" + phone);
//                intent.setData(data);
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                MainActivity.this.startActivity(intent);

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone,null,"车辆警报！",null,null);
                new Toastor(getApplicationContext()).showToast("报警信息已发送");
                break;
        }
    }

    /**
     * 重写返回键
     * */
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && (event.getRepeatCount() == 0)){
            this.finish();
            stopService(new Intent(MainActivity.this, LocationService.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && listener != null) {
            LogUtil.d("MainActivity","定位获取1"+amapLocation.getErrorInfo()+","+amapLocation.getErrorCode());
            if (amapLocation.getErrorCode() == 0) {
                LogUtil.d("MainActivity","定位获取2");

                listener.onLocationChanged(amapLocation);
                currentLatlng = new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());

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
            LogUtil.d("MainActivity","activate");
            mLocationClient = new AMapLocationClient(getApplicationContext());
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
            int speed = (int) (event.getSpeed()*3.6f);
            tv_speed.setText(speed+"km/h");
        }
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        runOnUiThread(new Runnable() {
            public void run() {
            }
        });
    }

    private void open(){
        String Dev = "/dev/ttymxc2";
        int Brt = 9600;
        int i= Serial_Start(Dev,Brt);
        if(i == 0){
            /* Create a receiving thread */
            mReadThread = new ReadThread();
            mReadThread.start();
            new Toastor(getApplicationContext()).showToast("打开串口成功");
        }else{
            new Toastor(getApplicationContext()).showToast("打开串口失败");
        }
    }

    private void send(){
        sendMessage("01040000000131CA", 0);
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = MainActivity.this.getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
                null);

        // Marker头部
        TextView title = (TextView) view.findViewById(R.id.tv_destination_title);
        title.setText(marker.getTitle());

        // 跳转按钮
        ImageButton button = (ImageButton) view
                .findViewById(R.id.ibtn_destination_chose);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng endLatlng = marker.getPosition();
                if(currentLatlng != null){
                    // 开始导航
                    List<NaviLatLng> mStartPoints = new ArrayList<>();
                    List<NaviLatLng> mEndPoints = new ArrayList<>();

                    mStartPoints.add(new NaviLatLng(currentLatlng.latitude,currentLatlng.longitude));
                    mEndPoints.add(new NaviLatLng(endLatlng.latitude,endLatlng.longitude));

                    AMapNavi.getInstance(MainActivity.this).calculateDriveRoute(mStartPoints,
                            mEndPoints, null, AMapNavi.DrivingDefault);
                    new Toastor(getApplicationContext()).showToast("开始导航");
                }else{
                    new Toastor(getApplicationContext()).showToast("当前位置定位失败");
                }

            }
        });

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteSuccess() {
        Intent intent = new Intent(MainActivity.this,
                SimpleNaviActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        Bundle bundle=new Bundle();
        bundle.putInt(Utils.ACTIVITYINDEX, Utils.SIMPLEGPSNAVI);
        bundle.putBoolean(Utils.ISEMULATOR, false);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }
}
