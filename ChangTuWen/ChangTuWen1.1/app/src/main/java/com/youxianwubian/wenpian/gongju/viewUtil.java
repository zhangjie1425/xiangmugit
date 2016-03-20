package com.youxianwubian.wenpian.gongju;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;

import com.youxianwubian.wenpian.R;

import java.io.*;

public class viewUtil
{
	public static byte[] printp(View v,int quality)
	{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache(true);
		Bitmap bi=v.getDrawingCache();
		bi.compress(Bitmap.CompressFormat.JPEG,quality,baos);
		return baos.toByteArray();
	}
	public static Bitmap printp(View v)
	{
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache(true);
		return v.getDrawingCache();
	}
	
	public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;

        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(0xffffff);
        }

        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
									 Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
		
        return bitmap;
    }
	public static Bitmap getBitmapByEditText(EditText ed,Context con) {
		Bitmap  ctwidbit = BitmapFactory.decodeResource(con.getResources(), R.mipmap.ctwidtp);
		//int idw=ctwidbit.getWidth();
		int idh=ctwidbit.getHeight();

		int edw=ed.getWidth();
		int edh=ed.getHeight();
		Bitmap bitmap = null;
		bitmap = Bitmap.createBitmap(edw, edh+idh,
									 Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
		//ed.setBackgroundColor(0xffffff);
		canvas.drawColor(0xffffffff);
        ed.draw(canvas);

		Paint paint=new Paint();

		canvas.drawBitmap(ctwidbit,0,edh,paint);



		return bitmap;
	}


  /*  public static void scrollviewContent2Png(Context context,
											 ScrollView scrollView) {
        Bitmap bmp = null;
        bmp = getBitmapByView(scrollView);
        saveBitmapToCamera(context, bmp, null);
    }*/
//这是webview的，利用了webview的api

	private static Bitmap captureWebView(WebView webView) {
        Picture snapShot = webView.capturePicture();
        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),
										 snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }
}
