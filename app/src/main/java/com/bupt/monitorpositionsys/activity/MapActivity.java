package com.bupt.monitorpositionsys.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.bupt.monitorpositionsys.R;
import com.bupt.monitorpositionsys.db.Path;
import com.bupt.monitorpositionsys.db.PathDetail;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends Activity {

	private MapView mapView;
	private AMap aMap;

	/** 标注*/
	private MarkerOptions markerOptions;
	ArrayList<LatLng> latLngs = new ArrayList<>();
	
	private Path path = null;
    private List<PathDetail> pathDetails = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_map);
		initUI();
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		
		getDataAndShow();
	}

	private void initUI() {
		mapView = (MapView) findViewById(R.id.mapview_map);
		if (aMap == null) {
			aMap = mapView.getMap();
		}
	}

	/**
	 * 获取页面传值，查询数据库并显示
	 * */
	private void getDataAndShow(){
		Intent i = getIntent();
		final int id = i.getIntExtra("pathID", 1);
		
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

			}

			@Override
			protected Boolean doInBackground(Void... params) {
				path = DataSupport.find(Path.class, id, true);
			    pathDetails = path.getPathDetails();
			    for (int j = 0; j < pathDetails.size(); j++) {
		    		latLngs.add(new LatLng(pathDetails.get(j).getLatitude(), pathDetails.get(j).getLongtitude()));
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean isSuccess) {
				if (isSuccess) {
					if(latLngs.size() > 0){
						// 标注起始点
					    markerOptions=new MarkerOptions().anchor(0.5f, 0.5f)
					  			.visible(true).position(latLngs.get(0))
					  			.title("起点")
					  			.icon(BitmapDescriptorFactory
					  					.fromResource(R.mipmap.img_path_start))
					  		    .draggable(false);
					    markerOptions.setFlat(true);
				  		aMap.addMarker(markerOptions);	
				  		
				  		// 标注终点
					    markerOptions=new MarkerOptions().anchor(0.5f, 0.5f)
					  			.visible(true).position(latLngs.get(latLngs.size()-1))
					  			.title("终点")
					  			.icon(BitmapDescriptorFactory
					  					.fromResource(R.mipmap.img_path_end))
					  		    .draggable(false);
					    markerOptions.setFlat(true);
				  		aMap.addMarker(markerOptions);	
				  		
						for(int a=0;a<latLngs.size();a++){
							//地图上划线
					  		if(a>0){
								aMap.addPolyline((new PolylineOptions()).add(latLngs.get(a-1),latLngs.get(a)).color(0xb30000ff).width(15));
					  		}
						}
						
						// 设置所有 maker 显示在 View 中
						LatLngBounds bounds = new LatLngBounds.Builder()
						.include(latLngs.get(0)).include(latLngs.get(latLngs.size()-1)).build();
						// 移动地图,所有 marker 自适应显示。LatLngBounds 与地图边缘 10 像素的填充区域 
						aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,1000,800, 10));
					}
				}
			}
		}.execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
}
