package com.zuomei.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLHomeMapPhonePop;
import com.zuomei.home.MLHomeMapPopView;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.model.MLMapData;
import com.zuomei.model.MLMapResponse;
import com.zuomei.services.MLHomeServices;
import com.zuomei.widget.MLTabGroup;
import com.zuomei.widget.MLTabGroup.IRadioCheckedListener;

import java.util.ArrayList;
import java.util.List;

public class MLBusinessMapAct extends BaseActivity implements  OnGetRoutePlanResultListener{
	
	@ViewInject(R.id.bmapsView)
	private MapView mMapView;
	
	
	@ViewInject(R.id.map_root)
	private RelativeLayout _root;
	
	@ViewInject(R.id.map_next)
	private Button mBtnPre;
	
	@ViewInject(R.id.map_pre)
	private Button mBtnNext;
	
	
	@ViewInject(R.id.iv_exit)
	private ImageView _exitIv;
	
	@ViewInject(R.id.map_tab)
	private MLTabGroup _tab;
	// 定位相关
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
		private LocationMode mCurrentMode;
		BitmapDescriptor mCurrentMarker;
		
		OnCheckedChangeListener radioButtonListener;
		boolean isFirstLoc = true;// 是否首次定位
		
	//地图相关
	private BaiduMap mBaiduMap;
	private Marker mMarkerA;
	private InfoWindow mInfoWindow;
	
	//所有地图标记
	private  MLMapResponse ret ;
	
	 //路径规划
    RoutePlanSearch mParthSearch = null;
    boolean useDefaultIcon = false;
    OverlayManager routeOverlay = null;
    int nodeIndex = -1;//节点索引,供浏览节点时使用
    RouteLine route = null;
    private TextView popupText = null;//泡泡view

	private MLHomeMapPhonePop _phonePop;
	// 初始化全局 bitmap 信息，不用时及时 recycle
		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		BitmapDescriptor bdB = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		BitmapDescriptor bdC = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		BitmapDescriptor bdD = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		BitmapDescriptor bd = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_map_main);
		ViewUtils.inject(this);
	    initView();
		//initOverlayData();
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
		
		 // 初始化搜索模块，注册事件监听
		mParthSearch = RoutePlanSearch.newInstance();
		mParthSearch.setOnGetRoutePlanResultListener(this);
		
	//初始化地图标记
	//	initOverlay();
	//初始化定位
		initLocation();
	//初始化 状态监听
		initStatusListener();
		
	//地图标点 事件
		initMarkerClick();
		
		
		
}
	
private void initView() {
	_tab.setRadioCheckedCallBack(new IRadioCheckedListener() {
		@Override
		public void radioChecked(RadioButton rb, int index) {
					if(index==0){
						mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
					}else{
						mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
					}
		}
	});
	}

private void initStatusListener(){
     ret = (MLMapResponse) BaseApplication.aCache.getAsObject(MLConstants.PARAM_MAP_OVERLAY);
	if(ret==null){
		showMessage("获取数据失败");
		return;
	}
	mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
		
		@Override
		public void onMapStatusChangeStart(MapStatus arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onMapStatusChangeFinish(MapStatus arg0) {
				
			Message msg = new Message();
			msg.obj="1";
			msg.what = HTTP_MAP_OVERLAY;
			_handler.sendMessage(msg);
		   
		}
		
		@Override
		public void onMapStatusChange(MapStatus arg0) {
			// TODO Auto-generated method stub
			
		}
	});
}

	private void initMarkerClick(){
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {  
				//获取坐标的信息
				MLMapData data = (MLMapData) marker.getExtraInfo().get("data");
				MLHomeMapPopView pop = new MLHomeMapPopView(MLBusinessMapAct.this);
				pop.setData(data.userName,data.Phone);
				final LatLng ll = marker.getPosition();
				Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				p.y -= 47;
				LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
				OnInfoWindowClickListener listener = null;
				_phonePop = new MLHomeMapPhonePop(MLBusinessMapAct.this,new PopOnItemClick(data));
			    listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							//弹出打选项
							_phonePop.showAtLocation(_root, Gravity.BOTTOM, 0, 0); 
							mBaiduMap.hideInfoWindow();
						}
					};
				mInfoWindow = new InfoWindow(pop, llInfo, listener);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
	}
	private void initOverlayData() {
	    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.HOME_MAP, null, null, _handler, HTTP_RESPONSE_MAP, MLHomeServices.getInstance());
	    loadData(this, message1);
		
	}

	private void initLocation() {
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
   		mLocClient.setLocOption(option);        
		mLocClient.start();
		
		//-========测试删除==============================
/*		LatLng llA = new LatLng(36.687932, 117.078866);
		GeoCoder mSearch = GeoCoder.newInstance();
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(llA));
		mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
				System.out.println("");
			}
			
			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
				System.out.println("");
			}
		});*/
		//========================================
	}

	public void initOverlay(List<MLMapData> datas) {
		
		for(MLMapData data : datas){
			Bundle bundle = new Bundle();
			bundle.putSerializable("data", data);
			LatLng llA = new LatLng(data.lat, data.lon);
			OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
					.zIndex(9).draggable(true);
			mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
			mMarkerA.setExtraInfo(bundle);
		}
		
	/*	LatLng llA = new LatLng(36.688255, 117.062678);
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));*/
	}

	private void getOverlay(){
		
		int[] position = new int[2]; 
		mMapView.getLocationOnScreen(position); 


		
		Point point1 = new Point(position[0]+3, position[1]+3);
		Point point2 = new Point(BaseApplication.screenWidth-3, BaseApplication.screenHeight-3);
		
		
		
		LatLng ll1=mBaiduMap.getProjection().fromScreenLocation(point1);
		LatLng ll2=mBaiduMap.getProjection().fromScreenLocation(point2);
		  
		List<MLMapData> olddatas = new ArrayList<MLMapData>();
		for(MLMapData data : ret.datas){
			
			if(data.lon>ll1.longitude&&data.lon<ll2.longitude&&
				data.lat>ll2.latitude&&data.lat<ll1.latitude
					){
				olddatas.add(data);
				Bundle bundle = new Bundle();
				bundle.putSerializable("data", data);
				LatLng llA = new LatLng(data.lat, data.lon);
				OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
						.zIndex(9).draggable(true);
				mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
				mMarkerA.setExtraInfo(bundle);
			}
		
		}
		
		for(MLMapData data : olddatas){
			ret.datas.remove(data);
		}
	}
	
	/**
    *  POP 弹出点击事件 （拨打电话，步行，公交车.....）
	 * @author Marcello
	 *
	 */
	public class  PopOnItemClick  implements OnClickListener{
		LatLng _ll;
		MLMapData _data;
		public PopOnItemClick(MLMapData data) {
			_ll =  new LatLng(data.lat,data.lon);
			_data = data;
		}

		@Override
		public void onClick(View view) {
			_phonePop.dismiss();
			LatLng llA = new LatLng(mBaiduMap.getLocationData().latitude, mBaiduMap.getLocationData().longitude);
			
			  PlanNode stNode = PlanNode.withLocation(llA);
		       PlanNode enNode =PlanNode.withLocation(_ll);
		       
		       if(view.getId()==R.id.map_btn_phone){
		    	   Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+_data.Phone));  
	                startActivity(intent); 
		       }else if(view.getId()==R.id.map_btn_walk){
		    	   showMessage("正在规划步行路线,请稍候...");
		    	   mParthSearch.walkingSearch((new WalkingRoutePlanOption())
		                    .from(stNode)
		                    .to(enNode));
		       }else if(view.getId()==R.id.map_btn_bus){
		    	   showMessage("正在规划公交车路线,请稍候...");
		    	   mParthSearch.transitSearch((new TransitRoutePlanOption())
		                    .from(stNode)
		                    .city("济南")
		                    .to(enNode));
		       }else if(view.getId()==R.id.map_btn_car){
		    	   showMessage("正在规划驾车路线,请稍候...");
		    	   mParthSearch.drivingSearch((new DrivingRoutePlanOption())
		                    .from(stNode)
		                    .to(enNode));
		       }else if(view.getId()==R.id.map_btn_nav){
		    	   startNavi(llA,_ll);
		       }
		}
	};
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		//onBackPressed();
		finish();
	}
	
	
	
	
	  @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		  
		  if(keyCode==KeyEvent.KEYCODE_BACK){
			  finish();
			  return true;
		  }
		  
		return super.onKeyDown(keyCode, event);
	}

	/**
     * 节点浏览示例
     *
     * @param v
     */
    public void nodeClick(View v) {
        if (route == null ||
                route.getAllStep() == null) {
            return;
        }
        if (nodeIndex == -1 && v.getId() == R.id.map_pre) {
        	return;
        }
        //设置节点索引
        if (v.getId() == R.id.map_next) {
            if (nodeIndex < route.getAllStep().size() - 1) {
            	nodeIndex++;
            } else {
            	return;
            }
        } else if (v.getId() == R.id.map_pre) {
        	if (nodeIndex > 0) {
        		nodeIndex--;
        	} else {
            	return;
            }
        }
        //获取节结果信息
        LatLng nodeLocation = null;
        String nodeTitle = null;
        Object step = route.getAllStep().get(nodeIndex);
        if (step instanceof DrivingRouteLine.DrivingStep) {
            nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrace().getLocation();
            nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
        } else if (step instanceof WalkingRouteLine.WalkingStep) {
            nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrace().getLocation();
            nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
        } else if (step instanceof TransitRouteLine.TransitStep) {
            nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrace().getLocation();
            nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
        }

        if (nodeLocation == null || nodeTitle == null) {
            return;
        }
        //移动节点至中心
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
        // show popup
        popupText = new TextView(MLBusinessMapAct.this);
        popupText.setWidth(400);
      //  popupText.setLayoutParams(new LayoutParams(400, LayoutParams.WRAP_CONTENT));
       popupText.setBackgroundResource(R.drawable.home_popup);
       popupText.setGravity(Gravity.CENTER);
        popupText.setTextColor(0xFF000000);
        popupText.setText(nodeTitle);
        mBaiduMap.showInfoWindow(new InfoWindow(popupText, nodeLocation, null));
    }
	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);
		//initOverlay();
	}

	
	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	private boolean isDestroy=false;
	@Override
	protected void onPause() {
		isDestroy = true;
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		isDestroy = false;
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		isDestroy = true;
		// 退出时销毁定位
		mLocClient.stop();
	   // 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
		// 回收 bitmap 资源
		bdA.recycle();
		bdB.recycle();
		bdC.recycle();
		bdD.recycle();
		bd.recycle();
	}
	//路线规划-----驾车
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
        	showMessage("抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            _exitIv.setVisibility(View.VISIBLE);
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            route = result.getRouteLines().get(0);
           DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
		
	}
	//路线规划-----公交车
	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
	    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	    	showMessage("抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            _exitIv.setVisibility(View.VISIBLE);
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            route = result.getRouteLines().get(0);
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
	}
	//路线规划-----步行
	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		 if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			 showMessage("抱歉，未找到结果");
	        }
	        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
	            //result.getSuggestAddrInfo()
	            return;
	        }
	        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	            nodeIndex = -1;
	            _exitIv.setVisibility(View.VISIBLE);
	            mBtnPre.setVisibility(View.VISIBLE);
	            mBtnNext.setVisibility(View.VISIBLE);
	            route = result.getRouteLines().get(0);
	            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
	            mBaiduMap.setOnMarkerClickListener(overlay);
	            routeOverlay = overlay;
	            overlay.setData(result.getRouteLines().get(0));
	            overlay.addToMap();
	            overlay.zoomToSpan();
	        }
		
	}
	
	
	/**
	 * 开始导航
	 * 
	 * @param view
	 */
	public void startNavi(LatLng pt1 , LatLng pt2) {
		// 构建 导航参数
		NaviPara para = new NaviPara();
		para.startPoint = pt1;
		para.startName = "从这里开始";
		para.endPoint = pt2;
		para.endName = "到这里结束";

		try {
			BaiduMapNavigation.openBaiduMapNavi(para, this);
		} catch (BaiduMapAppNotSupportNaviException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new DialogInterface. OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					BaiduMapNavigation.getLatestBaiduMapApp(MLBusinessMapAct.this);
				}
			});

			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builder.create().show();
		}
	}
	
    //定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.home_icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.home_icon_en);
            }
            return null;
        }
    }
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.home_icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.home_icon_en);
            }
            return null;
        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.home_icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.home_icon_en);
            }
            return null;
        }
    }
    
    @OnClick(R.id.iv_exit)
    public void exitOnClick(View view){
    	//退出路线规划
    			mBaiduMap.hideInfoWindow();
    			mBtnPre.setVisibility(View.INVISIBLE);
    			mBtnNext.setVisibility(View.INVISIBLE);
    			   _exitIv.setVisibility(View.INVISIBLE);
    			routeOverlay.removeFromMap();
    			initMarkerClick();
    }
    
    @OnClick(R.id.iv_location)
    public void locationOnClick(View view){
    	//回到定位位置
    	MyLocationData  data = mBaiduMap.getLocationData();
    	if(data==null) return;
    	mBaiduMap.setMyLocationData(data);
    	LatLng ll = new LatLng(data.latitude,
    			data.longitude);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.animateMapStatus(u);
    	
    }
    
    private static final int HTTP_RESPONSE_MAP = 0;
    private static final int HTTP_MAP_OVERLAY = 1;
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
      	 // dismissProgressDialog();
            if (msg == null || msg.obj == null) {
                showMessage(R.string.loading_data_failed);
                return;
            }
            if (msg.obj instanceof ZMHttpError) {
                ZMHttpError error = (ZMHttpError) msg.obj;
                showMessage(error.errorMessage);
                return;
            }
            switch (msg.what) {
           // 获取地图坐标
            case HTTP_RESPONSE_MAP:{
                   MLMapResponse ret = (MLMapResponse) msg.obj;
                	if(ret.state.equalsIgnoreCase("1")){
                		//_datas = ret.datas;
                		
                		if(!isDestroy){
                			initOverlay(ret.datas);
                		}
                	}else{
                		showMessage("获取城市失败!");
                	}
                    break;
            }
            case HTTP_MAP_OVERLAY:{
            	getOverlay();
            	break;
            }

                default:
                    break;
            }
        }
    };
}
