package com.youxianwubian.wenpian;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.youxianwubian.wenpian.gongju.IOtxt;
import com.youxianwubian.wenpian.layout.Zidydhk;
import com.youxianwubian.wenpian.ylview.LargeImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import aga.fdf.grd.st.SpotManager;


/**
 * Created by zhang on 2016/2/24.
 */
public class Yulan extends ActivityBase {
    private String tplj;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.yulan);


        Intent intent = getIntent();
        Bundle bd = intent.getBundleExtra("jt");// 根据bundle的key得到对应的对象
        tplj = bd.getString("tplj");
        //toast(tplj);
        //Log.v("jjjjjjj", tplj);

        init();

    }
    private void init()
    {


        ImageButton yulan_fhtp = (ImageButton) findViewById(R.id.yulan_fhtp);
        yulan_fhtp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               //返回
                Yulan.this.finish();
            }
        });
        TextView yulan_fh = (TextView) findViewById(R.id.yulan_fh);
        yulan_fh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //返回
                Yulan.this.finish();
            }
        });
        ImageButton yulan_fx = (ImageButton) findViewById(R.id.yulan_fx);
        yulan_fx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(Qjbl.ggkg.equals("k")) {
                    //展示广告
                    SpotManager.getInstance(Yulan.this).showSpotAds(Yulan.this);
                }
                    //分享
                    SharePhoto(tplj,Yulan.this);

            }
        });

        LargeImageView yulanyl = (LargeImageView) findViewById(R.id.iv_photo);
        seyyl(yulanyl);

        Button baocun=(Button)findViewById(R.id.ylbaocun);
        baocun.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toast("已保存到"+IOtxt.baocuntp(Yulan.this));
            }
                                  }

        );
    }

    //显示照片
    private void seyyl(LargeImageView ima)
    {
        toast(tplj);
        File filea = new File(tplj);
        if(filea.exists()) {
            try {
                File ftg = new File(tplj);
                // InputStream in = new FileInputStream(ft);
                FileInputStream inStream = new FileInputStream(ftg);
                ima.setInputStream(inStream, tplj);
            } catch (IOException e) {
                e.printStackTrace();
                //return null;
            }
        }
        else{
            this.finish();
        }
            // ima.setImageURI(Uri.fromFile(new File(tplj)));
        //ima.setImageBitmap(getbit(tplj));
    }


   /* //分享
    private Zidydhk dhk_bjqd;
    private void log_ylsp() {
        dhk_bjqd = new Zidydhk(this, R.layout.dhk_bjqd);
        View mView = dhk_bjqd.mView();
        //dhk_bjqd.setCancelable(false);
        Button dhk_bj_bcwz = (Button) mView.findViewById(R.id.dhk_bj_bcwz);
        dhk_bj_bcwz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button dhk_bj_bctp = (Button) mView.findViewById(R.id.dhk_bj_bctp);
        dhk_bj_bctp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dhk_bjqd.dismiss();

            }
        });

    }*/
    private void SharePhoto(String photoUri,final Activity activity) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        File file = new File(photoUri);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, activity.getTitle()));
    }



}
