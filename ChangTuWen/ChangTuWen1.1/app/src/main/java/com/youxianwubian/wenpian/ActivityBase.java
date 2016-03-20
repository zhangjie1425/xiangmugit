package com.youxianwubian.wenpian;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;


/**
 * Created by zhang on 2016/1/29.
 *
 */
public class ActivityBase extends Activity
{
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.activity_main);

        //状态栏变色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏透明 需要在创建SystemBarTintManager 之前调用。
            setTranslucentStatus(true);
        }
		/*SystemBarTintManager tintManager = new SystemBarTintManager(this);
		 tintManager.setStatusBarTintEnabled(true);
		 //使StatusBarTintView 和 actionbar的颜色保持一致，风格统一。
		 tintManager.setStatusBarTintResource(0xff232736);
		 // 设置状态栏的文字颜色
		 tintManager.setStatusBarDarkMode(false, this);
*/
        //状态栏变色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏透明 需要在创建SystemBarTintManager 之前调用。
            setTranslucentStatus(true);
        }
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    //实现一个toast
    public void toast(String st)
    {
        Toast.makeText(this,st,Toast.LENGTH_SHORT).show();
    }
    //回收Bitmap
    public void hsbit(Bitmap bitmap)
    {
        // 先判断是否已经回收

        if(bitmap != null && !bitmap.isRecycled()){
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }

    }
    public void kqzzjzk(String bt,String nr)
    {
        progressDialog = ProgressDialog.show(ActivityBase.this, bt, nr, true);
    }
    public void gbzzjzk()
    {
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
    //友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
