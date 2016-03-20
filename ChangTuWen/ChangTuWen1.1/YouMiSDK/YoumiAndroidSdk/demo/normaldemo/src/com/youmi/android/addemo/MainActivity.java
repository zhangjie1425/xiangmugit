package com.youmi.android.addemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import aga.fdf.grd.br.AdSize;
import aga.fdf.grd.br.AdView;
import aga.fdf.grd.br.AdViewListener;
import aga.fdf.grd.st.CustomerSpotView;
import aga.fdf.grd.st.SpotDialogListener;
import aga.fdf.grd.st.SpotManager;

/**
 * 主窗口
 * Edited by Alian on 15-11-24.
 */
public class MainActivity extends Activity {

	private static final String TAG = "youmi-demo";

	private Context mContext;

	/**
	 * 原生插屏控件
	 */
	private CustomerSpotView mCustomerSpotView;

	/**
	 * 原生控件容器
	 */
	private RelativeLayout mNativeAdLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;

		// 设置应用版本信息
		setupAppVersionInfo();
		//设置插屏
		setupSpotAd();
		setNativeSpotAd();
		//设置广告条
		setupBannerAd();
	}

	/**
	 * 设置版本号信息
	 */
	private void setupAppVersionInfo() {
		TextView textVersionInfo = (TextView) findViewById(R.id.tv_version_info);
		if (textVersionInfo != null) {
			textVersionInfo.append(getAppVersionName());
		}
	}

	/**
	 * 设置插屏广告
	 */
	private void setupSpotAd() {
		// 预加载插屏广告数据
		SpotManager.getInstance(mContext).loadSpotAds();
		// 设置插屏动画的横竖屏展示方式，如果设置了横屏，则在有广告资源的情况下会是优先使用横屏图
		SpotManager.getInstance(mContext)
				.setSpotOrientation(SpotManager.ORIENTATION_LANDSCAPE);
		// 插屏动画效果，0:ANIM_NONE为无动画，1:ANIM_SIMPLE为简单动画效果，2:ANIM_ADVANCE为高级动画效果
		SpotManager.getInstance(mContext).setAnimationType(SpotManager.ANIM_ADVANCE);

		Button btnShowSpot = (Button) findViewById(R.id.btn_show_spot);
		if (btnShowSpot.getVisibility() == View.GONE) {
			btnShowSpot.setVisibility(View.VISIBLE);
		}

		btnShowSpot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 展示插屏广告，可以不调用预加载方法独立使用
				SpotManager.getInstance(mContext)
						.showSpotAds(mContext, new SpotDialogListener() {
							@Override
							public void onShowSuccess() {
								Log.i(TAG, "插屏展示成功");
							}

							@Override
							public void onShowFailed() {
								Log.i(TAG, "插屏展示失败");
							}

							@Override
							public void onSpotClosed() {
								Log.i(TAG, "插屏被关闭");
							}

							@Override
							public void onSpotClick(boolean isWebPath) {
								Log.i(TAG, "插屏被点击，isWebPath = " + isWebPath);
							}

						});
			}
		});
	}

	/**
	 * 设置原生插屏广告
	 */
	public void setNativeSpotAd() {
		mNativeAdLayout = (RelativeLayout) findViewById(R.id.rl_native_ad);

		// 设置插屏动画的横竖屏展示方式，如果设置了横屏，则在有广告资源的情况下会是优先使用横屏图
		SpotManager.getInstance(mContext)
				.setSpotOrientation(SpotManager.ORIENTATION_LANDSCAPE);

		Button btnShowNativeSpot = (Button) findViewById(R.id.btn_show_native_spot);
		if (btnShowNativeSpot.getVisibility() == View.GONE) {
			btnShowNativeSpot.setVisibility(View.VISIBLE);
		}
		btnShowNativeSpot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mNativeAdLayout != null && mNativeAdLayout.getVisibility() == View.GONE) {
					mNativeAdLayout.setVisibility(View.VISIBLE);
				}
				if (mCustomerSpotView != null && mCustomerSpotView.isAttachedOnWindow()) {
					SpotManager.getInstance(mContext).handlerClick();
				} else {
					new Thread(new Runnable() {
						@Override
						public void run() {
							//切换到子线程获取原生控件
							mCustomerSpotView = SpotManager.getInstance(mContext)
									.cacheCustomerSpot(mContext, new SpotDialogListener() {
										@Override
										public void onShowSuccess() {
											Log.i(TAG, "原生插屏展示成功");
										}

										@Override
										public void onShowFailed() {
											Log.i(TAG, "原生插屏展示失败");
										}

										@Override
										public void onSpotClosed() {
											Log.i(TAG, "原生插屏被关闭");
										}

										@Override
										public void onSpotClick(boolean isWebPath) {
											Log.i(TAG, "原生插屏被点击，isWebPath = " + isWebPath);
										}
									});
							//获取成功
							if (mCustomerSpotView != null) {
								//切换到UI线程
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										RelativeLayout.LayoutParams params =
												new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
														ViewGroup.LayoutParams.MATCH_PARENT);
										params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
										if (mNativeAdLayout != null) {
											mNativeAdLayout.removeAllViews();
											mNativeAdLayout.addView(mCustomerSpotView, params);
										}
									}
								});
							} else {
								//获取失败
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(mContext, "Please try again later.", Toast.LENGTH_SHORT).show();
									}
								});
							}
						}
					}).start();
				}
			}
		});
	}

	/**
	 * 设置广告条广告
	 */
	private void setupBannerAd() {
		//		/**
		//		 * 普通布局
		//		 */
		//		//　实例化广告条
		//		AdView adView = new AdView(mContext, AdSize.FIT_SCREEN);
		//		LinearLayout bannerLayout = (LinearLayout) findViewById(R.id.ll_banner);
		//		bannerLayout.addView(adView);
		/**
		 * 悬浮布局
		 */
		// 实例化LayoutParams(重要)
		FrameLayout.LayoutParams layoutParams =
				new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		//　设置广告条的悬浮位置，这里示例为右下角
		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
		//　实例化广告条
		AdView adView = new AdView(mContext, AdSize.FIT_SCREEN);
		// 监听广告条接口
		adView.setAdListener(new AdViewListener() {

			@Override
			public void onSwitchedAd(AdView adView) {
				Log.i(TAG, "广告条切换");
			}

			@Override
			public void onReceivedAd(AdView adView) {
				Log.i(TAG, "请求广告条成功");
			}

			@Override
			public void onFailedToReceivedAd(AdView adView) {
				Log.i(TAG, "请求广告条失败");
			}
		});
		// 调用Activity的addContentView函数
		((Activity) mContext).addContentView(adView, layoutParams);
	}

	@Override
	public void onBackPressed() {
		//原生控件点击后退关闭
		if (mNativeAdLayout != null && mNativeAdLayout.getVisibility() == View.VISIBLE) {
			mNativeAdLayout.removeAllViews();
			mNativeAdLayout.setVisibility(View.GONE);
			return;
		}
		// 如果有需要，可以点击后退关闭插播广告。
		if (!SpotManager.getInstance(mContext).disMiss()) {
			super.onBackPressed();
		}
	}

	@Override
	protected void onStop() {
		SpotManager.getInstance(mContext).onStop();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// 调用插屏，开屏，退屏时退出
		SpotManager.getInstance(mContext).onDestroy();
		super.onDestroy();
	}

	/**
	 * 获取应用版本号
	 *
	 * @return 应用的当前版本号
	 */
	private String getAppVersionName() {
		try {
			PackageManager packageManager = getPackageManager();
			return packageManager.getPackageInfo(getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			return null;
		}
	}
}
