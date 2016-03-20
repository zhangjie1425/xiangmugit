package com.youxianwubian.wenpian;

/**
 * 开屏广告模式：
 * 
 *  1.缓存开屏广告
 *  	- 开屏广告第一次只是去请求到广告，不会展现。
 *  	- 当应用第二次启动的时候会展示上一次请求到的广告，广告展示结束请求一次请求。
 *  	- 竖屏请求到的广告，在横屏的时候是不会展现的，因为广告长和宽的尺寸不一致。
 *  
 *  2.实时开屏广告
 * 		- 开屏广告每次都实时请求广告，加载资源，展现广告。
 * 		- 如果广告请求到广告返回时间超出设定的超时时间，则本次请求不会展示。
 * 		- 如果广告资源加载时间超出设定的超时时间，则本次请求不会展示。
 *   
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.umeng.onlineconfig.OnlineConfigAgent;
import com.youxianwubian.wenpian.guangg.PermissionHelper;

import aga.fdf.grd.AdManager;
import aga.fdf.grd.st.SplashView;
import aga.fdf.grd.st.SpotDialogListener;
import aga.fdf.grd.st.SpotManager;


public class SplashScreenActivity extends ActivityBase {

	private static final String TAG = "changwentu";

	private Context mContext;

	private PermissionHelper mPermissionHelper;

	private SharedPreferences sharedPreferences;

	private SharedPreferences.Editor editor;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = this;
		//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//移除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);


//同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
		sharedPreferences= getSharedPreferences("test", Context.MODE_PRIVATE);
		//实例化SharedPreferences对象（第一步）
		SharedPreferences mySharedPreferences=this. getSharedPreferences("test", Context.MODE_PRIVATE);
//实例化SharedPreferences.Editor对象（第二步）
		editor = mySharedPreferences.edit();

		Qjbl.tpzl=sharedPreferences.getInt("tpzl",60);

		//友盟参数
		OnlineConfigAgent.getInstance().updateOnlineConfig(this);
		Qjbl.ggkg = OnlineConfigAgent.getInstance().getConfigParams(this, "ggkg");
		Qjbl.kpggkg = OnlineConfigAgent.getInstance().getConfigParams(this, "kpggkg");
		//toast("插屏"+Qjbl.ggkg+"开屏"+Qjbl.kpggkg);
		Log.i(TAG, "kaip-"+Qjbl.kpggkg+"chap-"+Qjbl.ggkg);


		mPermissionHelper = new PermissionHelper(this);
		mPermissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
			@Override
			public void onAfterApplyAllPermission() {
				Log.i(TAG, "All of requested permissions has been granted, so run app logic.");
				runApp();
			}
		});
		if (Build.VERSION.SDK_INT < 23) {
			//如果系统版本低于23，直接跑应用的逻辑
			Log.d(TAG, "The api level of system is lower than 23, so run app logic directly.");
			runApp();
		} else {
			//如果权限全部申请了，那就直接跑应用逻辑
			if (mPermissionHelper.isAllRequestedPermissionGranted()) {
				Log.d(TAG, "All of requested permissions has been granted, so run app logic directly.");
				runApp();
			} else {
				//如果还有权限为申请，而且系统版本大于23，执行申请权限逻辑
				Log.i(TAG, "Some of requested permissions hasn't been granted, so apply permissions first.");
				mPermissionHelper.applyPermissions();
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mPermissionHelper.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 跑应用的逻辑
	 */
	private void runApp() {
		//初始化SDK
		//AdManager.getInstance(mContext).init("85aa56a59eac8b3d", "a14006f66f58d5d7");
		AdManager.getInstance(this).init("ff8a922e7e6cccbe", "9465fc2093ada80e", false);
     	SpotManager.getInstance(this).loadSpotAds();
//		SpotManager.getInstance(this).loadSplashSpotAds();
		if(Qjbl.kpggkg.equals("k")) {
			//设置开屏
			setupSplashAd();
		}
		else {
			Log.d(TAG, "The app bu yao guang g.");
			//不要广告
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					startActivity(new Intent(SplashScreenActivity.this, Mainwp.class));
					finish();
			}
		}, 1000);
		}
	}

	/**
	 * 设置开屏广告
	 */
	private void setupSplashAd() {
		/**
		 * 自定义模式
		 */
		//SpotManager.getInstance(this).loadSplashSpotAds();
		SplashView splashView = new SplashView(this, null);
		// 设置是否显示倒计时，默认显示
		splashView.setShowReciprocal(true);
		// 设置是否显示关闭按钮，默认不显示
		splashView.hideCloseBtn(true);
		//传入跳转的intent，若传入intent，初始化时目标activity应传入null
		Intent intent = new Intent(this, Mainwp.class);
		splashView.setIntent(intent);
		//展示失败后是否直接跳转，默认直接跳转
		splashView.setIsJumpTargetWhenFail(true);
		//获取开屏视图
		View splash = splashView.getSplashView();

		final RelativeLayout splashLayout = (RelativeLayout) findViewById(R.id.rl_splash);
		//		splashLayout.setVisibility(View.GONE);
		//添加开屏视图到布局中
		RelativeLayout.LayoutParams params =
				new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.ABOVE, R.id.view_divider);
		splashLayout.addView(splash, params);
		//显示开屏
		SpotManager.getInstance(mContext)
				.showSplashSpotAds(mContext, splashView, new SpotDialogListener() {

					@Override
					public void onShowSuccess() {
						Log.i(TAG, "kaipzs");
						splashLayout.setVisibility(View.VISIBLE);
						splashLayout.startAnimation(AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.anim_splash_enter));
					}

					@Override
					public void onShowFailed() {
						Log.i(TAG, "kaipingshibai");
					}

					@Override
					public void onSpotClosed() {
						Log.i(TAG, "kaipingguanbi");
					}

					@Override
					public void onSpotClick(boolean isWebPath) {
						Log.i(TAG, "kaipingbeidianji");
					}
				});

		/**
		 * 默认模式
		 */
		// SpotManager.getInstance(this).showSplashSpotAds(this,
		// MainActivity.class);
	}







/*
	
	SplashAd splashAd;
	RTSplashAd rtSplashAd;
//	 缓存开屏广告:true   实时开屏广告:false
//	Cache splash ad:true   Real-time splash ad:false
	private boolean isSplash = false;
	private SharedPreferences sharedPreferences;

	private SharedPreferences.Editor editor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉Activity上面的状态栏
		this.getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);  
		// 去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);

//同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
		sharedPreferences= getSharedPreferences("test", Context.MODE_PRIVATE);
		//实例化SharedPreferences对象（第一步）
		SharedPreferences mySharedPreferences=this. getSharedPreferences("test", Context.MODE_PRIVATE);
//实例化SharedPreferences.Editor对象（第二步）
		editor = mySharedPreferences.edit();

		Qjbl.tpzl=sharedPreferences.getInt("tpzl",60);

		//友盟参数
		OnlineConfigAgent.getInstance().updateOnlineConfig(this);
		Qjbl.ggkg = OnlineConfigAgent.getInstance().getConfigParams(this, "ggkg");
		Qjbl.kpggkg = OnlineConfigAgent.getInstance().getConfigParams(this, "kpggkg");
		//toast("插屏"+Qjbl.ggkg+"开屏"+Qjbl.kpggkg);
		if(Qjbl.kpggkg.equals("k")) {
			*//**
			 *
			 * DomobSplashMode.DomobSplashModeFullScreen 请求开屏广告的尺寸为全屏
			 * DomobSplashMode.DomobSplashModeSmallEmbed 请求开屏广告的尺寸不是全屏，根据设备分辨率计算出合适的小屏尺寸
			 * DomobSplashMode.DomobSplashModeBigEmbed 请求开屏广告的尺寸不是全屏，更具设备分辨率计算出合适的相对SmallMode的尺寸
			 *
			 *//*
			if (isSplash) {
//			 缓存开屏广告
//			Cache splash ad
				splashAd = new SplashAd(this, "56OJwEg4uNx2vzStuv", "16TLuV2lAp9yWNUUcnHTwETk",
						SplashMode.SplashModeFullScreen);
//		    setSplashTopMargin is available when you choose non-full-screen splash mode.
//			splashAd.setSplashTopMargin(200);
				splashAd.setSplashAdListener(new SplashAdListener() {
					@Override
					public void onSplashPresent() {
						Log.i("DomobSDKDemo", "onSplashStart");
					}

					@Override
					public void onSplashDismiss() {
						Log.i("DomobSDKDemo", "onSplashClosed");
//					 开屏回调被关闭时，立即进行界面跳转，从开屏界面到主界面。
//				    When splash ad is closed, jump to the next(main) Activity immediately.
						jump();
//					如果应用没有单独的闪屏Activity，需要调用closeSplash方法去关闭开屏广告
//					If you do not carry a separate advertising activity, you need to call closeRTSplash way to close the splash ad
//					splashAd.closeSplash();
					}

					@Override
					public void onSplashLoadFailed() {
						Log.i("DomobSDKDemo", "onSplashLoadFailed");
					}
				});

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (splashAd.isSplashAdReady()) {
							splashAd.splash(SplashScreenActivity.this, SplashScreenActivity.this.findViewById(R.id.splash_holder));
						} else {
							Toast.makeText(SplashScreenActivity.this, "Splash ad is NOT ready.", Toast.LENGTH_SHORT).show();
							jump();
						}
					}
				}, 1);
			} else {
//			 实时开屏广告
//			Real-time splash ad
				rtSplashAd = new RTSplashAd(this, "56OJwEg4uNx2vzStuv", "16TLuV2lAp9yWNUUcnHTwETk",
						SplashMode.SplashModeFullScreen);
//		    setRTSplashTopMargin is available when you choose non-full-screen splash mode.
//			rtSplashAd.setRTSplashTopMargin(200);
				rtSplashAd.setRTSplashAdListener(new RTSplashAdListener() {
					@Override
					public void onRTSplashDismiss() {
						Log.i("DomobSDKDemo", "onRTSplashClosed");
//					 开屏回调被关闭时，立即进行界面跳转，从开屏界面到主界面。
//					When rtSplash ad is closed, jump to the next(main) Activity immediately.
						jump();
//					如果应用没有单独的闪屏Activity，需要调用closeRTSplash方法去关闭开屏广告
//					If you do not carry a separate advertising activity, you need to call closeRTSplash way to close the splash ad

//					rtSplashAd.closeRTSplash();
					}

					@Override
					public void onRTSplashLoadFailed() {
						Log.i("DomobSDKDemo", "onRTSplashLoadFailed");
					}

					@Override
					public void onRTSplashPresent() {
						Log.i("DomobSDKDemo", "onRTSplashStart");
					}

				});

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						rtSplashAd.splash(SplashScreenActivity.this, SplashScreenActivity.this.findViewById(R.id.splash_holder));
					}
				}, 1);
			}
		}
		else{
			//不加载广告
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					*//*if (splashAd.isSplashAdReady()) {
						splashAd.splash(SplashScreenActivity.this, SplashScreenActivity.this.findViewById(R.id.splash_holder));
					} else {
						Toast.makeText(SplashScreenActivity.this, "Splash ad is NOT ready.", Toast.LENGTH_SHORT).show();
						jump();
					}*//*
					jump();
				}
			}, 1000);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("DomobSDKDemo", "Splash onPause");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("DomobSDKDemo", "Splash onDestroy");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		 Back key disabled
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void jump() {
		startActivity(new Intent(SplashScreenActivity.this, Mainwp.class));
		finish();
	}*/
}
