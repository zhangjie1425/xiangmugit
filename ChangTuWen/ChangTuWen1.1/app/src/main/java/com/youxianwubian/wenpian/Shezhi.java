package com.youxianwubian.wenpian;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by zhang on 2016/2/28.
 */

    public class Shezhi extends  ActivityBase
    {
        private  TextView tpzltext;
        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor editor;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.shezhi);
            init();
        }
        private void init()
        {
            //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
            sharedPreferences= getSharedPreferences("test", Context.MODE_PRIVATE);
            //实例化SharedPreferences对象（第一步）
            SharedPreferences mySharedPreferences=this. getSharedPreferences("test", Context.MODE_PRIVATE);
//实例化SharedPreferences.Editor对象（第二步）
            editor = mySharedPreferences.edit();
            ImageButton shezhi_fhtp = (ImageButton) findViewById(R.id.shezhi_fhtp);
            shezhi_fhtp.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //返回
                    Shezhi.this.finish();
                }
            });
            TextView shezhi_fh = (TextView) findViewById(R.id.shezhi_fh);
            shezhi_fh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //返回
                    Shezhi.this.finish();
                }
            });
            //图片质量
            tpzltext = (TextView) findViewById(R.id.tpzltext);
            tpzltext.setText("质量："+Qjbl.tpzl);

            SeekBar bjwzdx = (SeekBar)findViewById(R.id.tpzldx);
            //设置拖动栏数字
            bjwzdx.setProgress(Qjbl.tpzl);

            bjwzdx.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {

                //当拖动条的滑块位置发生改变时触发该方法
                @Override
                public void onProgressChanged(SeekBar arg0
                        , int progre, boolean fromUser)
                {
                    int progress=progre+10;
                  Qjbl.tpzl=progress;
                    editor.putInt("tpzl",Qjbl.tpzl);
                    editor.commit();
                  tpzltext.setText("质量："+Qjbl.tpzl);
                    //dhk_xpc_text.setText(progress+"");
                    //Qjbl.paintWidthb=progress;
                }
                @Override
                public void onStartTrackingTouch(SeekBar bar){}
                @Override
                public void onStopTrackingTouch(SeekBar bar){}
            });
        }

    }
