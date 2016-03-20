package com.youxianwubian.wenpian;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.*;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.youxianwubian.wenpian.gongju.Getcolorview;
import com.youxianwubian.wenpian.gongju.IOtxt;
import com.youxianwubian.wenpian.gongju.viewUtil;
import com.youxianwubian.wenpian.layout.GridViewFaceAdapter;
import com.youxianwubian.wenpian.layout.Zidydhk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhang on 2016/1/30.
 */
public class Bianji extends ActivityBase {

    private EditText bianjiEditText;

    private GridView mGridView;

    private GridViewFaceAdapter mGVFaceAdapter;

    private InputMethodManager imm;

    private LinearLayout bjbq;

   // private TextView bianji_ok;

    //private ScrollView bianjiscr;

    private TextView bianjibt;
   // private String btstring = "第一个";

    private int ljid;

    private Zidydhk dhk_bjqd;
    private List<Uri> tpPath = new ArrayList<Uri>();

    private final int wzcd = 100;//文字截取长度
    //获取数据，，并显示！
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            //如果该消息是本程序所发送的
            if (msg.what == 0x07) {
                toast("文本保存成功!");
                gbzzjzk();

            }
            if (msg.what == 0x001) {
                toast("文本保存失败!");
                gbzzjzk();
            }
            if (msg.what == 2) {
                //toast("图片保存成功!\n图片路径:" + "手机内存/wenpian/pt");
                Intent it = new Intent(Bianji.this, Yulan.class);
                Bundle bundle = new Bundle();
                bundle.putString("tplj", getSDCardPath()+"/wenpian/tp"+"/tp.jpg");
                it.putExtra("jt", bundle);
                startActivity(it);
                gbzzjzk();
            }
            if (msg.what == 3) {
                toast("图片保存失败!请重试!");
                gbzzjzk();
            }
            if (msg.what == 4) {
                toast("您编辑的文本过多，生成图片失败!");
                gbzzjzk();
            }
        }
    };
    private MyTimerTask task=null ;
    private Timer timer = new Timer(true);
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            //保存文本
          baocunwb(false);
        }
    }

    private Bundle savedInstanceState;

    private String nrstring;

    private String wjjid;//编辑文本id

    private ScrollView bianjiscr;
    private LinearLayout bianji_zt,bianji_wz;
    private LinearLayout bjzt,bjwzsz;
    private int ztcolor=100,wzcolor=0x90000000,wzzt;

    private boolean isxztp=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bianji);
        init();
        initzhutirun();
        //bianjiEditText.setSelection(bianjiEditText.length());//调整光标到最后一行
    }
///////////////////////////////////////////////////////////////////////////////////////////init
    private void init() {
        //标题
        bianjibt = (TextView) findViewById(R.id.bianjibt);
        bianjiEditText = (EditText) findViewById(R.id.bianjiEditText);
//软键盘管理类
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        initGridView();

        //编辑器点击事件
        bianjiEditText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {




              /*  //imm.hideSoftInputFromWindow(bianjiEditText.getWindowToken(),0);
                Spanned s=bianjiEditText.getText();
                ImageSpan[] imageSpans=s.getSpans(0, s.length(), ImageSpan.class);
                int selectionStart=bianjiEditText.getSelectionStart();

                for (ImageSpan span : imageSpans)
                {
                    int start=s.getSpanStart(span);
                    int end=s.getSpanEnd(span);
                    float bbbb=(end-start)/2;
                    Log.i("点击事件","起始位置"+start+"终止位置"+end+"点击位置"+selectionStart);
                    if(selectionStart>=start&&selectionStart<start+bbbb)//找到图片  
                    {
					//Bitmap bitmap=((BitmapDrawable)span.getDrawable()).getBitmap();
					//viewPicture(bitmap);
                        //Bianji.show("点击了图片");
                        toast("点击了图片");
                        if(bianjiEditText.length()>selectionStart) {
                            bianjiEditText.setSelection(selectionStart+1);//调整光标到光标+1
                        }
                        else {
                            bianjiEditText.setSelection(bianjiEditText.length());//调整光标到最后一行
                        }
				return;
				}
			}*/
			//sp;imm.showSoftInput(txtEdit, 0);


                //显示软键盘
                showIMM();
            }
        });
        //主题菜单界面
         bianji_zt = (LinearLayout) findViewById(R.id.bianji_zt);
        //文字编辑界面
         bianji_wz = (LinearLayout) findViewById(R.id.bianji_wz);
        //编辑界面
        bianjiscr = (ScrollView) findViewById(R.id.bianjiscr);

        Intent intent = getIntent();
        Bundle bd = intent.getBundleExtra("bd");// 根据bundle的key得到对应的对象
        wjjid = bd.getString("wbid");
        nrstring = bd.getString("nr");
         ztcolor=bd.getInt("ztcolor");
         wzcolor=bd.getInt("wzcolor");
         wzzt=bd.getInt("wzzt");

            if(ztcolor==0||ztcolor==1||ztcolor==2||ztcolor==3||ztcolor==4||ztcolor==5||ztcolor==6||ztcolor==7||ztcolor==8||ztcolor==9||ztcolor==10||ztcolor==11||ztcolor==12||ztcolor==100)
            {seteditbg(ztcolor);}else {
                bianjiEditText.setBackgroundColor(ztcolor);
            }


        if(wzcolor!=0)
        {
           // bianjiEditText.setBackgroundColor(ztcolor);
            bianjiEditText.setTextColor(wzcolor);
        }
        if(wzzt!=0)
        {
            bianjiEditText.setTextSize(wzzt);
        }



        bianjibt.setText("编辑");
        //处理文字
        if (nrstring != null) {
            setedtext(nrstring);
        }


//将字体文件保存在assets/fonts/目录下，创建Typeface对象
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/LibianSC.ttf");
        //使用字体
        bianjiEditText.setTypeface(typeFace);




        //返回
        TextView bianji_fh = (TextView) findViewById(R.id.bianji_fh);
        bianji_fh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                baocunwb(true);
                //Bianji.this.finish();
            }
        });
        ImageButton bianji_fhtp = (ImageButton) findViewById(R.id.bianji_fhtp);
        bianji_fhtp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                baocunwb(true);
                //Bianji.this.finish();
            }
        });



        //bianjiscr = (ScrollView) findViewById(R.id.bianjiscr);

        ImageButton  fenxiang = (ImageButton) findViewById(R.id.fenxiang);
        fenxiang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //分享
                zhwtp();
            }
        });


    /*	bianjiscr.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
					Toast.makeText(Bianji.this, ".....", Toast.LENGTH_LONG).show();
					//显示软键盘
					showIMM();
				}
			});*/

        //图片
        LinearLayout bjtp = (LinearLayout) findViewById(R.id.bjtp);
        bjtp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isxztp=true;
                ShowPickDialog();
            }
        });
        //表情
        bjbq = (LinearLayout) findViewById(R.id.bjbq);
        bjbq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                djlan(2);
            }
        });
        //主题
        bjzt = (LinearLayout) findViewById(R.id.bjzt);
        bjzt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //toast("我来了");
                djlan(3);
            }
        });
        bjzt.getTag(1);
        //文字设置
        bjwzsz = (LinearLayout) findViewById(R.id.bjwzsz);
        bjwzsz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //toast("我来了");
                djlan(4);
            }
        });
        bjwzsz.getTag(1);

        //每隔一段时间保存一下
        if(task==null)
        {
            task=new MyTimerTask();
        }
        timer.schedule(task,0, 10000);//10秒

    }

    //判断其他按钮是否隐藏
    private void isqtansfyc(int dd)
    {
        switch ( dd ) {
            case 1:

                break;
            case 2:
                //表情
                if(bjwzsz.getTag() == 0){
                    bjwzsz.setTag(1);
                    bianji_wz.setVisibility(View.GONE);
                }
                if(bjzt.getTag()==0)
                {
                    bjzt.setTag(1);
                    bianji_zt.setVisibility(View.GONE);
                }

                break;
            case 3:
                //主题
                if(bjbq.getTag() == 0) {
                    bjbq.setTag(1);
                    mGridView.setVisibility(View.GONE);


                }
                if(bjwzsz.getTag() == 0){
                    bjwzsz.setTag(1);
                    bianji_wz.setVisibility(View.GONE);


                }
                break;
            case 4:
                //设置文字
                if(bjbq.getTag() == 0) {
                    bjbq.setTag(1);
                    mGridView.setVisibility(View.GONE);


                }
                if(bjzt.getTag()==0)
                {
                    bjzt.setTag(1);
                    bianji_zt.setVisibility(View.GONE);

                }
                break;

            //最后都没有
            default:
                break;
        }
    }
    //处理栏按钮点击事件
    private void djlan(int d)
    {


        switch ( d ) {
            case 1:

                break;
            case 2:
                // mGridView.setVisibility(View.GONE);
                if(bjbq.getTag() == 0){
                    bjbq.setTag(1);
                    mGridView.setVisibility(View.GONE);
                    //显示软键盘
                    imm.showSoftInput(bianjiEditText, 0);
                    if(!imm.isActive()){
                        bianjiEditText.setSelection(bianjiEditText.length());//调整光标到最后一行
                    }

                }
                else {
                    //隐藏软键盘
                    imm.hideSoftInputFromWindow(bianjiEditText.getWindowToken(), 0);
                    isqtansfyc(d);
                    mGridView.setVisibility(View.VISIBLE);
                    bjbq.setTag(0);


                }
                break;
            case 3:
                if(bjzt.getTag() == 0){
                    bjzt.setTag(1);
                    bianji_zt.setVisibility(View.GONE);
                    //显示软键盘
                    imm.showSoftInput(bianjiEditText, 0);
                    if(!imm.isActive()){
                        bianjiEditText.setSelection(bianjiEditText.length());//调整光标到最后一行
                    }

                }
                else {
                    //隐藏软键盘
                    imm.hideSoftInputFromWindow(bianjiEditText.getWindowToken(), 0);
                    isqtansfyc(d);
                    bianji_zt.setVisibility(View.VISIBLE);
                    bjzt.setTag(0);

                }
                break;
            case 4:
                if(bjwzsz.getTag() == 0){
                    bjwzsz.setTag(1);
                    bianji_wz.setVisibility(View.GONE);
                    //显示软键盘
                    imm.showSoftInput(bianjiEditText, 0);
                    if(!imm.isActive()){
                        bianjiEditText.setSelection(bianjiEditText.length());//调整光标到最后一行
                    }
                }
                else {
                    //隐藏软键盘
                    imm.hideSoftInputFromWindow(bianjiEditText.getWindowToken(), 0);
                    isqtansfyc(d);
                    bianji_wz.setVisibility(View.VISIBLE);
                    bjwzsz.setTag(0);

                }
                break;
            //最后都没有
            default:
                break;



        }





    }

    //初始化表情控件
    private void initGridView() {
        mGVFaceAdapter = new GridViewFaceAdapter(this);
        mGridView = (GridView) findViewById(R.id.bianji_faces);
        mGridView.setAdapter(mGVFaceAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //插入的表情
                SpannableString ss = new SpannableString(view.getTag().toString());
                Drawable d = getResources().getDrawable((int) mGVFaceAdapter.getItemId(position));
                d.setBounds(0, 0, 50, 50);//设置表情图片的显示大小
                ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
                ss.setSpan(span, 0, view.getTag().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                //在光标所在处插入表情
                bianjiEditText.getText().insert(bianjiEditText.getSelectionStart(), ss);
            }
        });
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////init

    //处理返回事件
    public void onBackPressed() {
        baocunwb(true);
    }
    //activity显示时。。
    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        //再次加载
        if(!isxztp) {
            if(bianjiEditText!=null) {
                //toast("加载");
                if (nrstring != null) {
                    setedtext(nrstring);
                }
            }
        }
        else
        {
            isxztp=false;
        }
        if(bianjiEditText!=null) {
            bianjiEditText.setSelection(bianjiEditText.length());//调整光标到最后一行
        }
        super.onResume() ;
    }
    private void setteicon()
    {
		/*new Thread(new Runnable() {
				public void run() {*/
        String st= bianjiEditText.getText().toString();
        SpannableString ss = new SpannableString(st);
        Pattern p=Pattern.compile(getSDCardPath()+".+?\\.(png|jpg|gif)");
        Matcher m=p.matcher(st);
        while(m.find()){
            //toast(m.group());
            //	String ljst=m.group();
            File filea = new File(m.group());
            if(filea.exists())
            {
                //   Bitmap bm_de = BitmapFactory.decodeFile(ljst);
			/*	SpannableString ss = new SpannableString(ljst);
				ImageSpan span = new ImageSpan(bm_de, ImageSpan.ALIGN_BOTTOM);
				ss.setSpan(span, 0, ljst.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				bianjiEditText.getText().insert(bianjiEditText.getSelectionStart(), ss);
				*/
                //insertIntoEditText(getBitmapMime(bm_de, ljst));
                //System.out.println(m.group());
			/*Bitmap bm = BitmapFactory.decodeFile(m.group());
			//Bitmap rbm = resizeImage(bm, 100, 100);
			ImageSpan span = new ImageSpan(this, bm);
			getBitmapMime(bm,st).setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
			/*	Drawable d=Drawable.createFromPath(new File(ljst).getAbsolutePath());
				//插入的表情
				SpannableString ss = new SpannableString(ljst);
				//Drawable d = getResources().getDrawable((int)mGVFaceAdapter.getItemId(position));

				d.setBounds(0, 0,50, 50);//设置表情图片的显示大小
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
				ss.setSpan(span, 0, ljst.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				//在光标所在处插入表情
				bianjiEditText.getText().insert(bianjiEditText.getSelectionStart(), ss);	*/

                Bitmap bm = BitmapFactory.decodeFile(m.group());
                //Bitmap rbm = resizeImage(bm, 100, 100);
                ImageSpan span = new ImageSpan(this, bm);
                ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
       // Log.v("jjjjjjj", "iiiiiiiiii"+st);
        Pattern pb=Pattern.compile("-b%(.+?)%b-");
        Matcher mb=pb.matcher(st);
        while(mb.find()){
            Pattern pbb=Pattern.compile("-b%(.+?)%b-");
            Matcher mbb=pbb.matcher(mb.group());
           // Log.v("jjjjjjj", mb.group());
            while(mbb.find()){
               // Log.v("jjjjjjj", mbb.group(1));
                //插入的表情
               // SpannableString ssb = new SpannableString(st);
               Drawable d = getResources().getDrawable((int) mGVFaceAdapter.getItemId(Integer.valueOf(mbb.group(1))));
                d.setBounds(0, 0, 50, 50);//设置表情图片的显示大小
                ImageSpan spanb = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
                ss.setSpan(spanb,mb.start(), mb.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                //在光标所在处插入表情
                //bianjiEditText.getText().insert(bianjiEditText.getSelectionStart(), ss);

            }
        }
        bianjiEditText.setText(ss);
    }
	/*
	private OnClickListener textListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			//关闭软键盘  
			//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(bianjiEditText.getWindowToken(),0);
			Spanned s=bianjiEditText.getText();
			ImageSpan[] imageSpans=s.getSpans(0, s.length(), ImageSpan.class);
			int selectionStart=bianjiEditText.getSelectionStart();
			for (ImageSpan span : imageSpans)
			{
				int start=s.getSpanStart(span);
				int end=s.getSpanEnd(span);
				if(selectionStart>=start&&selectionStart<end)//找到图片  
				{
					/*Bitmap bitmap=((BitmapDrawable)span.getDrawable()).getBitmap();
					viewPicture(bitmap);*/
    //Bianji.show("点击了图片");
			/*		return;
				}
			}
			sp;imm.showSoftInput(txtEdit, 0);
			}
			};*/
    /**
     * 图片转成SpannableString加到EditText中
     *
     * @param pic
     * @param smile
     * @return
     */
    private SpannableString getBitmapMime(Bitmap pic, String smile) {
		/*int imgWidth = pic.getWidth();
		int imgHeight = pic.getHeight();
		float scalew = (float) 600 / imgWidth;
		//float scaleh = (float) 40 / imgHeight;
		Matrix mx = new Matrix();
		mx.setScale(scalew, scalew);
		pic = Bitmap.createBitmap(pic, 0, 0, imgWidth, imgHeight, mx, true);*/
        //String smile = uri.getPath();
        SpannableString ss = new SpannableString(smile);
        //ImageSpan span = new ImageSpan(this, pic);
       // Drawable d = getResources().getDrawable((int) mGVFaceAdapter.getItemId(position));
       /* Drawable drawable =new BitmapDrawable(pic);
        drawable.setBounds(0, 0, pic.getWidth(), pic.getHeight());//设置表情图片的显示大小
        ImageSpan spann = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);*/
        ImageSpan spann = new ImageSpan(this,pic);
        ss.setSpan(spann, 0, smile.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 这里是重点
     * 添加图片替换文字
     */
    private void insertIntoEditText(SpannableString ss) {
        Editable et = bianjiEditText.getText();// 先获取Edittext中的内容
        int start = bianjiEditText.getSelectionStart();
        et.insert(start, ss);// 设置ss要添加的位置
        bianjiEditText.setText(et);// 把et添加到Edittext中
        bianjiEditText.setSelection(start + ss.length());// 设置Edittext中光标在最后面显示
    }
    private void crtp(String lj,Bitmap bit)
    {
		/*
		SpannableString ss = new SpannableString(lj);
		ImageSpan span = new ImageSpan(bit, ImageSpan.ALIGN_BOTTOM);
		ss.setSpan(span, 0, lj.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		bianjiEditText.getText().insert(bianjiEditText.getSelectionStart(), ss);
		*/
        //路径
        //lj=lj+" ";
        bianjiEditText.getText().append("\n");//后面加换行符
        insertIntoEditText(getBitmapMime(bit, lj));
        bianjiEditText.getText().append(" ");
        bianjiEditText.getText().append("\n");//后面加换行符



    /*  //  Drawable drawable = getResources().getDrawable(id);
      //  drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //需要处理的文本，[smile]是需要被替代的文本
        SpannableString spannable = new SpannableString(bianjiEditText.getText().toString()+lj);
        //要让图片替代指定的文字就要用ImageSpan
        ImageSpan span = new ImageSpan(this, bit);
        //开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）
        //最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12
        spannable.setSpan(span,bianjiEditText.getText().length(),bianjiEditText.getText().length()+lj.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        bianjiEditText.setText(spannable);*/

        //Editable et = bianjiEditText.getText();// 先获取Edittext中的内容
        //int start = bianjiEditText.getSelectionStart();
        //et.insert(start, ss);// 设置ss要添加的位置
        //bianjiEditText.setText(et);// 把et添加到Edittext中
        //bianjiEditText.setSelection(start + ss.length());// 设置Edittext中光标在最后面显示

		/*
		 2
		 3
		 4
		 5
		 6
		 7
		 private SpannableString getBitmapMime(Bitmap pic, Uri uri) {
		 String path = uri.getPath();
		 SpannableString ss = new SpannableString(path);
		 ImageSpan span = new ImageSpan(this, pic);
		 ss.setSpan(span, 0, path.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		 return ss;
		 }

		 private void insertIntoEditText(SpannableString ss) {
		 Editable et = editText.getText();// 先获取Edittext中的内容
		 int start = editText.getSelectionStart();
		 et.insert(start, ss);// 设置ss要添加的位置
		 editText.setText(et);// 把et添加到Edittext中
		 editText.setSelection(start + ss.length());// 设置Edittext中光标在最后面显示


		 display = (EditText) findViewById(R.id.display);
		 String imagePath = getIntent().getBundleExtra("neirong").getString(
		 "nei");
		 SpannableString ss = new SpannableString(imagePath);
		 Pattern p=Pattern.compile("/mnt/sdcard/.+?\\.\\w{3}");
		 Matcher m=p.matcher(imagePath);
		 while(m.find()){
		 Bitmap bm = BitmapFactory.decodeFile(m.group());
		 Bitmap rbm = resizeImage(bm, 100, 100);
		 ImageSpan span = new ImageSpan(this, rbm);
		 ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		 }
		 display.setText(ss);
		*/

		/*
		SpannableString ss = new SpannableString(lj);//view.getTag().toString());
		Bitmap bm = BitmapFactory.decodeFile(lj);
		//Bitmap rbm = resizeImage(bm, 100, 100);
		ImageSpan span = new ImageSpan(this, bm);
		//ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(span, 0, 100, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		bianjiEditText.getText().insert(bianjiEditText.getSelectionStart(), ss);
		*/
    }


    private void showIMM() {
       // bjbq.setTag(1);
       // showOrHideIMM();

        //处理表情
        if(bjbq.getTag()==0)
        {
            bjbq.setTag(1);
            mGridView.setVisibility(View.GONE);
        }
        //处理主题
        if(bjzt.getTag() == 0) {
            bjzt.setTag(1);
            bianji_zt.setVisibility(View.GONE);
        }
        //处理文字设置
        if(bjwzsz.getTag() == 0) {
            bjwzsz.setTag(1);
            bianji_wz.setVisibility(View.GONE);
        }
    }
/*    private void showFace() {
        //mFace.setImageResource(R.drawable.widget_bar_keyboard);
        bjbq.setTag(2);
        mGridView.setVisibility(View.VISIBLE);
    }
    private void hideFace() {
        //mFace.setImageResource(R.drawable.widget_bar_face);
        bjbq.setTag(null);
        mGridView.setVisibility(View.GONE);
    }*/
    //private void showOrHideIMM() {
      //  imm.hideSoftInputFromWindow(bianjiEditText.getWindowToken(),0);
      /*  Spanned s=bianjiEditText.getText();
        ImageSpan[] imageSpans=s.getSpans(0, s.length(), ImageSpan.class);
        int selectionStart=bianjiEditText.getSelectionStart();
        for (ImageSpan span : imageSpans)
        {
            int start=s.getSpanStart(span);
            int end=s.getSpanEnd(span);
            //int siz=(end-start)*2+start;
            if(selectionStart>=start&&selectionStart<end)//找到图片  
            {
				//Bitmap bitmap=((BitmapDrawable)span.getDrawable()).getBitmap();
				// viewPicture(bitmap);
                toast("点击了图片");
                return;
            }
			else
			{
				//toast("没点击啊");
			}
        }*/

       /* if(bjbq.getTag() == null){
            //隐藏软键盘
            imm.hideSoftInputFromWindow(bianjiEditText.getWindowToken(), 0);
            //显示表情
            showFace();
        }else{
            //显示软键盘
            imm.showSoftInput(bianjiEditText, 0);
            if(!imm.isActive()){
                bianjiEditText.setSelection(bianjiEditText.length());//调整光标到最后一行
            }
            //隐藏表情
            hideFace();
        }
    }*/

    /**
     * 获取SDCard的目录路径功能
     * @return
     */
    private String getSDCardPath(){
        File sdcardDir = null;
        //判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(sdcardExist){
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }


    //生成图片
    private void bcjt(Bitmap bit,String wjname)
    {
        String SavePath = getSDCardPath()+"/wenpian/tp/";
		/*Time time = new Time("GMT+8");
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int day = time.monthDay;
		int minute = time.minute;
		int hour = time.hour;
		int sec = time.second;
		String tim=""+year+month+day+hour+minute+sec;*/
        //3.保存Bitmap
        try {
            File path = new File(SavePath);
            if(!path.exists()){
                path.mkdirs();
            }


            //文件
            String filepath = SavePath +wjname;
            File file = new File(filepath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (null != fos) {
                bit.compress(Bitmap.CompressFormat.JPEG, Qjbl.tpzl, fos);
                fos.flush();
                fos.close();

                //让系统更新图片
                // 其次把文件插入到系统图库
                try {
                    MediaStore.Images.Media.insertImage(this.getContentResolver(),
                            SavePath, wjname, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" +  filepath)));

/*    //得到缩略图
                bit = ThumbnailUtils.extractThumbnail(bit, bit.getWidth(), 400);

                //文件
                String filepathb = SavePath + "/"+wjname+".yl";
                File fileb = new File(filepathb);
                if (!fileb.exists()) {
                    fileb.createNewFile();
                }
                FileOutputStream fosb = null;
                fosb = new FileOutputStream(fileb);
                if (null != fosb) {
                    bit.compress(Bitmap.CompressFormat.PNG, 100, fosb);
                    fosb.flush();
                    fosb.close();*/

                    Message msg = new Message();
                    msg.what = 2;
                    //发送消息
                    myHandler.sendMessage(msg);



                //Toast.makeText(Bianji.this, "转化图片成功", Toast.LENGTH_LONG).show();
                //  Toast.makeText(, "截屏文件已保存至SDCard/zhangjie/ScreenImage/下", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Message msg = new Message();
            msg.what = 3;
            //发送消息
            myHandler.sendMessage(msg);
            //Toast.makeText(Bianji.this, "转化图片失败"+e.toString(), Toast.LENGTH_LONG).show();
        }





    }

    //显示文字及图片
    private void setedtext(String stb)
    {
        File filea = new File(stb);
        if(filea.exists())
        {
            String st= IOtxt.readFile(stb);
            bianjiEditText.setText(st);
            setteicon();
        }

    }
	/*
	 Pattern p=Pattern.compile("/mnt/sdcard/.+?\\.(png|jpg|gif)");
	 Matcher m=p.matcher("/mnt/sdcard/pjjj.iiii/iii.png/ggghh.png");
	 while(m.find()){
	 System.out.println(m.group());
	 Bitmap bm = BitmapFactory.decodeFile(m.group());
	 Bitmap rbm = resizeImage(bm, 100, 100);
	 ImageSpan span = new ImageSpan(this, rbm);
	 ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	*/

    /**
     * 选择提示对话框
     */
    private void ShowPickDialog() {
        /**
         * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
         * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
         */
        Intent intent = new Intent(Intent.ACTION_PICK, null);


        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images
         * .Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 如果朋友们要限制上传到服务器的图片类型时可以直接写如
         * ："image/jpeg 、 image/png等的类型"
         */
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, 1);
       /* new AlertDialog.Builder(this)
                .setTitle("选择图片...")
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        *//**
                         * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
                         * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
                         *//*
                        Intent intent = new Intent(Intent.ACTION_PICK, null);


                        *//**
                         * 下面这句话，与其它方式写是一样的效果，如果：
                         * intent.setData(MediaStore.Images
                         * .Media.EXTERNAL_CONTENT_URI);
                         * intent.setType(""image*//*");设置数据类型
                         * 如果朋友们要限制上传到服务器的图片类型时可以直接写如
                         * ："image/jpeg 、 image/png等的类型"
                         *//*
                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image*//*");
                        startActivityForResult(intent, 1);
                    }
                })
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        *//**
                         * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
                         * 文档，you_sdk_path/docs/guide/topics/media/camera.html
                         * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
                         * 官方文档太长了就不看了，其实是错的
                         *//*
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        // 下面这句指定调用相机拍照后的照片存储的路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                .fromFile(new File(getSDCardPath()+"hc",
                                        "superspace.jpg")));
                        startActivityForResult(intent, 2);
                    }
                }).show();*/
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());}
                break;
            // 如果是调用相机拍照时
            case 2:

                File temp = new File(getSDCardPath()+"/wenpian/hc"
                        + "/superspace.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            // 取得裁剪后的图片
            case 3:
                /**
                 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                 * 当前功能时，会报NullException，只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
                 *
                 */
                //toast("jjjjjjjjj");
                if (data != null) {
                    //setPicToView(data);
                    //toast(data.getAction());


                }
                break;
            default:
                break;


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 裁剪图片方法实现
     *
     * @param
     */

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的
		 */
        //Intent intent = new Intent("com.android.camera.action.CROP");
        //intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        //intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
		/*	intent.putExtra("aspectX", 5);
		 intent.putExtra("aspectY", 2);
		 // outputX outputY 是裁剪图片宽高
		 intent.putExtra("outputX", 500);
		 intent.putExtra("outputY", 200);*/
        //intent.putExtra("return-data", true);
        //startActivityForResult(intent, 3);
        try {

            Bitmap photo= uriOCRb(uri,  bianjiEditText.getWidth()-20);

		/*if(saveMyBitmap(photo,txlja,50))
		 {*/
            //picPath=txlj + "touxiang.png";
		/*if(ljid==0)
		{
			Bitmap photob= uriOCRb(uri,500);
			saveMyBitmap(photob,getSDCardPath()+"/wenpian/wz/"+"yl"+".jpg",30);
		}*/
            if(photo!=null)
            {
                //saveMyBitmap(photo,getSDCardPath()+"/wenpian/wz/"+ljid+".jpg",70);
                //crtp(getSDCardPath()+"/wenpian/wz/"+ljid+".jpg",photo);
                //ljid++;
                //	String tplj=getRealPathFromURI(uri);
                //photo=compressBitmap(photo,100);
                crtp(getRealPathFromURI(uri),photo);
                //添加一个图片路径标记
                tpPath.add(uri);

				/*bitpa=photo;
				fttpa.setImageBitmap(bitpa);
			*/
                //tpxz.setImageBitmap(photo);
            }
        } catch (OutOfMemoryError e) {
            toast("内存不足，添加图片失败!");
            //
        }
        //	}
		/*Bitmap photob= uriOCRb(uri,600);
		 saveMyBitmap(photob,txljb,20);
		 */
    }
    //get bitmap size
    private float getSizeOfBitmap(Bitmap bitmap)
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray().length/1024;
    }
    //set bitmap quality
    private  Bitmap compressBitmap(Bitmap bitmap,float size){

        if(bitmap==null||getSizeOfBitmap(bitmap)<=size){
            return null;//如果图片本身的大小已经小于这个大小了，就没必要进行压缩
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//如果签名是png的话，则不管quality是多少，都不会进行质量的压缩
        int quality=100;
        while (baos.toByteArray().length / 1024f>size) {
            quality=quality-4;// 每次都减少4
            baos.reset();// 重置baos即清空baos
            if(quality<=0){
                break;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            //Logg.e("------质量--------"+baos.toByteArray().length/1024f);
        }

        return bitmap;
    }

    private Bitmap uriOCRb(Uri uri,int siza) {
        Bitmap zBitmap =null;
        if (uri != null) {
            InputStream is = null;

            try{
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(uri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                //File picturePathb = new File(picturePath);

                BitmapFactory.Options o = new BitmapFactory.Options();
                //////
                //////
                o.inJustDecodeBounds = false;
                zBitmap =BitmapFactory.decodeFile(picturePath, o);

                // Find the correct scale value. It should be the power of 2.
				/*  final int REQUIRED_SIZE = 100;
				 int width_tmp = o.outWidth, height_tmp = o.outHeight;
				 */
                //计算缩放比

				/*	int be = (int)(o.outHeight/ (float)300);

				 if (be <= 0)

				 be = 1;*/
                int bmpWidth = zBitmap.getWidth();
                int bmpHeight = zBitmap.getHeight();
                //	Toast.makeText(getApplicationContext(), "高度！"+bmpHeight+"宽度"+bmpWidth, 2000).show();

                float tph=bmpHeight,tpw=bmpWidth;

                //  Qjbl.bx = (float)bmpWidth / bmpHeight;
                if(bmpHeight>siza||bmpWidth>siza)
                {
                    if(bmpHeight>bmpWidth)
                    {
                        tph=siza;
                        tpw= (float) bmpWidth/bmpHeight*siza;
                    }
                    else
                    {
                        tpw=siza;
                        tph=(float)  bmpHeight/bmpWidth*siza;
                    }
                    //	Toast.makeText(getApplicationContext(), "高度bb！"+Qjbl.tph+"宽度bb"+Qjbl.tpw, 2000).show();


                }

                // 缩放图片的尺寸
                float scaleWidth = (float)tpw/ bmpWidth; // 按固定大小缩放 sWidth 写多大就多大
                float scaleHeight = (float) tph/ bmpHeight; //
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);// 产生缩放后的Bitmap对象
                zBitmap = Bitmap.createBitmap(zBitmap, 0, 0, bmpWidth, bmpHeight, matrix, false);




				/*

				BitmapFactory.Options options =new BitmapFactory.Options();

				options.inJustDecodeBounds =true;

				// 获取这个图片的宽和高

				zBitmap =BitmapFactory.decodeFile(picturePath, options); //此时返回bm为空



				options.inJustDecodeBounds =false;

				//计算缩放比

				int be = (int)(options.outHeight/ s);

				if (be <= 0)

					be = 1;

				options.inSampleSize = be;

				//重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦

				zBitmap=BitmapFactory.decodeFile(picturePath,options);
*/

                //mImage.setImageBitmap(bitmap);
                //	doOCR(bitmap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return zBitmap;
    }
	/*
	private Bitmap uriOCRb(Uri uri,int siza) {
		Bitmap zBitmap =null;
		if (uri != null) {
			InputStream is = null;

			try{
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(uri,
														   filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();

				//File picturePathb = new File(picturePath);



				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = false;
				zBitmap =BitmapFactory.decodeFile(picturePath, o);

				int bmpWidth = zBitmap.getWidth();
				int bmpHeight = zBitmap.getHeight();
				//bit.compress(CompressFormat.JPEG, quality, fileOutputStream);
				Bitmap result = null;


				if(bmpHeight>=siza)
				{
					result = Bitmap.createBitmap(siza*bmpWidth/bmpHeight, siza,
												 Config.ARGB_8888);
					Canvas canvas = new Canvas(result);
					Rect rect = new Rect(0, 0, bmpWidth, bmpHeight);// original
					rect = new Rect(0, 0, siza*bmpWidth/bmpHeight, siza);
					canvas.drawBitmap(zBitmap, null, rect, null);

					//saveBitmap(bit, quality, fileName, optimize);
					zBitmap=result;
					//result.recycle();
				}
				if(bmpWidth>= siza)
				{
					result = Bitmap.createBitmap(siza, siza*bmpHeight/bmpWidth,
												 Config.ARGB_8888);//
					Canvas canvas = new Canvas(result);
					Rect rect = new Rect(0, 0, bmpWidth, bmpHeight);// original
					rect = new Rect(0, 0, siza, siza*bmpHeight/bmpWidth);//
					canvas.drawBitmap(zBitmap, null, rect, null);

					//saveBitmap(bit, quality, fileName, optimize);
					zBitmap=result;
					//result.recycle();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return zBitmap;
	}
*/

    private boolean saveMyBitmap(Bitmap mBitmap,String lj,String name,int zl)  {
        File file = new File(lj);
        //如果不存在文件夹则创建文件夹
        if(!file.exists())
        {	file.mkdirs();  }

        File f = new File( lj+name);
        FileOutputStream fOut = null;
        try
        {
            fOut = new FileOutputStream(f);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        // mBitmap.compress(Bitmap.CompressFormat.PNG, 10, fOut);
        mBitmap.compress(Bitmap.CompressFormat.JPEG, zl, fOut);
        try
        {
            fOut.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            fOut.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return true;
    }
    private String getwjjid(String ss)
    {

        File saveFile = new File(ss);
        if (!saveFile.exists())
        {
            saveFile.mkdirs();
        }
        int maxid=0;
        final File[] file = new File(ss).listFiles();//设定扫描路径
        for(int i=0 ; file!= null && i<file.length ;i++) {
            if(file[i].isDirectory()) {
				/*TextWz twz=new TextWz();
				 twz.setbt(file[i].getName());
				 mTextWz.add(twz);*/
                //mPathString.add(file[i].getName());
                try{
                    int x= Integer.valueOf(file[i].getName());
                    if(maxid<x)
                    {
                        maxid=x;
                    }
                } catch (Exception e) {
                    File fileb = new File(getSDCardPath()+"/wenpian/wz/"+file[i].getName());
                    fileb.delete();
                }


            }



        }
        String str = new DecimalFormat("000000").format(maxid+1);
        return str;

    }

    //保存文本
    private void baocunwb(final boolean istuichu)
    {
        if(bianjiEditText.getText().toString()!=null&&!bianjiEditText.getText().toString().equals("")) {
            if (istuichu) {
                kqzzjzk("保存", "正在保存...");

                timer.cancel(); //退出计时器
                timer=null;
                //timer = new Timer(true);
                task=null;
            }

            new Thread(new Runnable() {
                public void run() {
                    if (!bianjiEditText.getText().toString().equals("")) {
                        if (wjjid == null) {
                            wjjid = getwjjid(getSDCardPath() + "/wenpian/wz");
                        }
                        String st = bianjiEditText.getText().toString();
                        bcwbntp(wjjid);
                        xrid(getSDCardPath() + "/wenpian/wz/" + wjjid + "/id.iso", st);
                        if (IOtxt.saveFile(thwblj(st, wjjid), getSDCardPath() + "/wenpian/wz/" + wjjid + "/zj.iso")) {
                            if (istuichu) {
                                //保存成功
                                Message msg = new Message();
                                msg.what = 0x07;
                                //发送消息
                                myHandler.sendMessage(msg);
                                Bianji.this.finish();
                            }
                        } else {
                            if (istuichu) {
                                //失败
                                Message msg = new Message();
                                msg.what = 0x001;
                                //发送消息
                                myHandler.sendMessage(msg);
                            }
                        }
                    }

                }
            }).start();
        }
        else
        {
            if (istuichu) {
                Bianji.this.finish();
            }
        }
    }

    //转化为图片
    private void zhwtp()
    {
        kqzzjzk("转化","正在转化为图片...");
        bianjiEditText.setCursorVisible(false);
        //		int wjjidb=getwjjid(getSDCardPath()+"/wenpian/pt");
        //xrid(getSDCardPath()+"/wenpian/pt/"+wjjidb+"/id.iso");
        new Thread(new Runnable() {
            public void run() {
                if (!bianjiEditText.getText().toString().equals("")) {
                    if (wjjid == null) {
                        wjjid = getwjjid(getSDCardPath() + "/wenpian/wz");
                    }
                    String st = bianjiEditText.getText().toString();
                    bcwbntp(wjjid);
                    xrid(getSDCardPath() + "/wenpian/wz/" + wjjid + "/id.iso", st);
                    if (IOtxt.saveFile(thwblj(st, wjjid), getSDCardPath() + "/wenpian/wz/" + wjjid + "/zj.iso")) {
                        try {
                            Bitmap sctpbit= viewUtil.getBitmapByEditText(bianjiEditText,Bianji.this);
                            bcjt(sctpbit,"tp.jpg");

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            //Toast.makeText(Bianji.this, "亲，您编辑的文字太多，无法生成图片!"+e.toString(), Toast.LENGTH_LONG).show();
                            Message msg = new Message();
                            msg.what = 4;
                            //发送消息
                            myHandler.sendMessage(msg);
                        }
                    } else {
                       //toast("请重试！");
                        //失败
                        Message msg = new Message();
                        msg.what = 1;
                        //发送消息
                        myHandler.sendMessage(msg);
                    }
                }

            }
        }).start();
/*
        new Thread(new Runnable() {
            public void run() {
                try {
                    Bitmap sctpbit= viewUtil.getBitmapByEditText(bianjiEditText);
                    bcjt(sctpbit,"tp.png");
                }
                catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(Bianji.this, "亲，您编辑的文字太多，无法生成图片!"+e.toString(), Toast.LENGTH_LONG).show();
                    Message msg = new Message();
                    msg.what = 4;
                    //发送消息
                    myHandler.sendMessage(msg);
                }
            }
        }).start();*/
    }


  /*  private void log_ylsp()
    {
        dhk_bjqd = new Zidydhk(this, R.layout.dhk_bjqd);
        View mView =dhk_bjqd.mView();
        //dhk_bjqd.setCancelable(false);
        Button dhk_bj_bcwz = (Button) mView.findViewById(R.id.dhk_bj_bcwz);
        dhk_bj_bcwz.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dhk_bjqd.dismiss();
                kqzzjzk("保存","正在保存...");
                new Thread(new Runnable() {
                    public void run() {


                        if(!bianjiEditText.getText().toString().equals(""))
                        {
                            if(wjjid==null) {
                                wjjid = getwjjid(getSDCardPath() + "/wenpian/wz");
                            }


                            String st=bianjiEditText.getText().toString();
                            bcwbntp(wjjid);
                            xrid(getSDCardPath()+"/wenpian/wz/"+wjjid+"/id.iso",st);
                            if(IOtxt.saveFile(thwblj(st,wjjid),getSDCardPath()+"/wenpian/wz/"+wjjid+"/zj.iso"))
                            {
                                //保存成功
                                Message msg = new Message();
                                msg.what = 0;
                                //发送消息
                                myHandler.sendMessage(msg);
                            }
                            else{
                                //失败
                                Message msg = new Message();
                                msg.what = 1;
                                //发送消息
                                myHandler.sendMessage(msg);
                            }
                        }

                    }
                }).start();
            }
        });

        Button dhk_bj_bctp = (Button) mView.findViewById(R.id.dhk_bj_bctp);
        dhk_bj_bctp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dhk_bjqd.dismiss();
                kqzzjzk("保存","正在保存...");
                //		int wjjidb=getwjjid(getSDCardPath()+"/wenpian/pt");
                //xrid(getSDCardPath()+"/wenpian/pt/"+wjjidb+"/id.iso");
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Bitmap sctpbit= viewUtil.getBitmapByEditText(bianjiEditText);
                            bcjt(sctpbit,gettimetr()+".png");
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            //Toast.makeText(Bianji.this, "亲，您编辑的文字太多，无法生成图片!"+e.toString(), Toast.LENGTH_LONG).show();
                            Message msg = new Message();
                            msg.what = 4;
                            //发送消息
                            myHandler.sendMessage(msg);
                        }
                    }
                }).start();
            }
        });

        Button dhk_bj_bcwzhtp = (Button) mView.findViewById(R.id.dhk_bj_bcwzhtp);
        dhk_bj_bcwzhtp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dhk_bjqd.dismiss();
                new Thread(new Runnable() {
                    public void run() {
                        kqzzjzk("保存","正在保存...");
                        if(wjjid==null) {
                            wjjid = getwjjid(getSDCardPath() + "/wenpian/wz");
                        }
                        String st=bianjiEditText.getText().toString();
                        bcwbntp(wjjid);
                        xrid(getSDCardPath()+"/wenpian/wz/"+wjjid+"/id.iso",st);
                        if(IOtxt.saveFile(thwblj(st,wjjid),getSDCardPath()+"/wenpian/wz/"+wjjid+"/zj.iso"))
                        {
                            //保存成功
                            Message msg = new Message();
                            msg.what = 0;
                            //发送消息
                            myHandler.sendMessage(msg);
                        }
                        else{
                            //失败
                            Message msg = new Message();
                            msg.what = 1;
                            //发送消息
                            myHandler.sendMessage(msg);
                        }

                        //int wjjidb=getwjjid(getSDCardPath()+"/wenpian/pt");
                        //xrid(getSDCardPath()+"/wenpian/pt/"+wjjidb+"/id.iso");
                        kqzzjzk("保存","正在保存...");
                        try {
                            bcjt(viewUtil.getBitmapByEditText(bianjiEditText),gettimetr()+".png");
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            //Toast.makeText(Bianji.this, "亲，您编辑的文字太多，无法生成图片!"+e.toString(), Toast.LENGTH_LONG).show();
                            Message msg = new Message();
                            msg.what = 4;
                            //发送消息
                            myHandler.sendMessage(msg);
                        }
                    }
                }).start();
            }
        });
        dhk_bjqd.show();
    }*/
    //保存文本内的图片
    private boolean bcwbntp(String wjjid)
    {
        /*if(tpPath.size()>0)
        {
            Bitmap photo= uriOCRb(tpPath.get(0),500);
            saveMyBitmap(photo,getSDCardPath()+"/wenpian/wz/"+wjjid,"/yl"+".jpg",70);
        }*/
        tpPathbid.clear();
        for(int i=0;i<tpPath.size();i++)
        {
            Bitmap photob= uriOCRb(tpPath.get(i),bianjiEditText.getWidth()-20);
            long timeSeconds = System.currentTimeMillis();
            tpPathbid.add(timeSeconds+"");
            saveMyBitmap(photob,getSDCardPath()+"/wenpian/wz/"+wjjid,"/"+timeSeconds+".jpg",80);


        }
        return true;
    }
    private List<String> tpPathbid=new ArrayList<>();
    //替换图片路径文本并删除多余的图片
    private String thwblj(String  st,String wjjid)
    {
        String stb=st;
       // Log.i("ttttttttttttttttttttttttttttttytplj",""+tpPath.size());
        for(int i=0;i<tpPath.size();i++)
        {
          //  Log.i("ttttttttttttttttttttttttttttttytp",""+getRealPathFromURI(tpPath.get(i)));
            try {
                stb = stb.replace(getRealPathFromURI(tpPath.get(i)), getSDCardPath() + "/wenpian/wz/" + wjjid + "/" + tpPathbid.get(i) + ".jpg");
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            //getRealPathFromURI()
           // Log.i("ttttttttttttttttttttttttttttttytp",""+stb);

        }
        //删除多余图片
        List<String> tpid=new ArrayList<>();
        Pattern p=Pattern.compile(getSDCardPath()+".+?\\.(png|jpg|gif)");
        Matcher m=p.matcher(stb);
        while(m.find()){
            tpid.add(m.group());
           // Log.i("ttttttttttttttttttttttttttttttplist",""+ m.group());

        }
        File fileac = new File(getSDCardPath() + "/wenpian/wz/" + wjjid + "/");
        List<String> tplist=IOtxt.gettp(fileac);
        if(tpid.size()==0)
        {
            for(int i=0;i<tplist.size();i++)
            {
                File filedytp = new File(tplist.get(i));
                IOtxt.RecursionDeleteFile(filedytp);
            }
        }
        if(tplist!=null&&tplist.size()>0&&tpid.size()>0)
        {
          //  twz.seticonlj(tplist.get(0));
            for(int i=0;i<tplist.size();i++)
            {
                boolean b=false;
                for(int j=0;j<tpid.size();j++) {
                    if (tplist.get(i).equals(tpid.get(j)))
                    {
                       b=true;
                    }
                }
                if(!b) {
                    File filedytp = new File(tplist.get(i));
                    IOtxt.RecursionDeleteFile(filedytp);
                    b=false;
                }
            }
            //Log.i("ttttttttttttttttttttttttttttttplist",""+tplist.get(0));
        }
        return stb;
    }
    //写入标记
    private void xrid(String filePath,String sta)
    {
		/*ContentResolver cv = this.getContentResolver();
      String strTimeFormat = android.provider.Settings.System.getString(cv,
																		  android.provider.Settings.System.TIME_12_24);

       if(strTimeFormat.equals("24"))

		{
			Log.i("activity","24");
        }*/
        String stb;

        if(sta.length()<=wzcd)
        {
            stb=sta;
        }
        else
        {
            stb=sta.substring(0,wzcd);
        }
        for(int i=0;i<tpPath.size();i++)
        {
            stb = stb.replaceAll(getRealPathFromURI(tpPath.get(i)),"(图片)");
        }
        //吧图片路径转化为  文字（图片）
        Pattern p=Pattern.compile("([\\s\\S]*)"+getSDCardPath()+".+?\\.(png|jpg|gif)"+"([\\s\\S]*)");
        Matcher m=p.matcher(stb);
        while(m.find()){
            stb=m.group(1)+"（图片）"+m.group(3);
        }


        String st="_time_"+gettimetr()+"_nr_"+stb+"..."+"_ztcolor_"+ztcolor+"_wzcolor_"+wzcolor+"_wzzt_"+wzzt;
        IOtxt.saveFile(st,filePath);
    }
    private String gettimetr()
    {
        SimpleDateFormat sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
        String    date    =    sDateFormat.format(new    java.util.Date());
        return date;
    }

    private void one(String sti)
    {
        SpannableString ss = new SpannableString(sti);
        Pattern p=Pattern.compile(getSDCardPath()+".+?\\.(png|jpg|gif)");
        Matcher m=p.matcher(sti);
        while(m.find()){
            //toast(m.group());
            //	String ljst=m.group();
            File filea = new File(m.group());
            if(filea.exists())
            {
                Bitmap bm = BitmapFactory.decodeFile(m.group());
                //Bitmap rbm = resizeImage(bm, 100, 100);
                ImageSpan span = new ImageSpan(this, bm);
                ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        bianjiEditText.setText(ss);
			/*	}
			}).start();*/
    }





    private int[] bit=new int[14];
    //初始化选择主题控件
    private void initzhutirun()
    {
        bit[0]=R.mipmap.bg_a;
        bit[1]=R.mipmap.bg_b;
        bit[2]=R.mipmap.bg_c;
        bit[3]=R.mipmap.bg_d;
        bit[4]=R.mipmap.bg_e;
        bit[5]=R.mipmap.bg_f;
        bit[6]=R.mipmap.bg_g;
        bit[7]=R.mipmap.bg_h;
        bit[8]=R.mipmap.bg_i;
        bit[9]=R.mipmap.bg_j;
        bit[10]=R.mipmap.bg_k;
        bit[11]=R.mipmap.bg_l;
        bit[12]=R.mipmap.bg_m;
        bit[13]=R.mipmap.tsbtp;

        Gallery   g = (Gallery) findViewById(R.id.bjxzzttp);
        //添加OnItemSelectedListener监听器
        ImageAdapter  i=new ImageAdapter(this);
        g.setAdapter(i);
        //g.setOnItemSelectedListener(Bianji.this);
        //设置监听器
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?>parent, View v, int position, long id) {


                if(position==0||position==1||position==2||position==3||position==4||position==5||position==6||position==7||position==8||position==9||position==10||position==11||position==12) {
                    seteditbg(position);
                    ztcolor=position;
                }
                if(position==13)
                {
                    getcolorlog();
                }
               // toast(position+"");
				/*	if (position>=Qjbl.bit.size()-1)
					{
						pickPhoto();
						BitmapDrawable bd=new BitmapDrawable( Qjbl. bit.get(position));
						mSwitcher.setImageDrawable(bd);
					}
					else
					{
						BitmapDrawable bd=new BitmapDrawable(Qjbl.  bit.get(position));
						mSwitcher.setImageDrawable(bd);
					}*/
                //设置图片资源
               /* BitmapDrawable bd=new BitmapDrawable(Qjbl.  bit.get(position));
                mSwitcher.setImageDrawable(bd);*/

            }
        });

        //文字选择颜色
        Button bjxzwzys = (Button) findViewById(R.id.bjxzwzys);
        bjxzwzys.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getwzcolorlog();
            }});
        //文字选择大小
        SeekBar bjwzdx = (SeekBar)findViewById(R.id.bjwzdx);
        //设置拖动栏数字
        //bjwzdx.setProgress(Qjbl.paintWidthb);

        bjwzdx.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {

            //当拖动条的滑块位置发生改变时触发该方法
            @Override
            public void onProgressChanged(SeekBar arg0
                    , int progress, boolean fromUser)
            {
                wzzt=progress;
                bianjiEditText.setTextSize(wzzt);
                //dhk_xpc_text.setText(progress+"");
                //Qjbl.paintWidthb=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar bar){}
            @Override
            public void onStopTrackingTouch(SeekBar bar){}
        });

//        new Thread(new Runnable() {
//            public void run() {
//                bit[0]=R.mipmap.f010;
//                bit[1]=R.mipmap.f026;
//                bit[2]=R.mipmap.f001;
//                bit[3]=R.mipmap.f005;
//            }
//        }).start();
    }
    private void seteditbg(int position)
    {
        if(position==0)
        {
            bianjiEditText.setBackgroundResource(R.drawable.beijinga);
        }
        if(position==1)
        {
            bianjiEditText.setBackgroundResource(R.drawable.beijingb);
        }
        if(position==2)
        {
            bianjiEditText.setBackgroundResource(R.drawable.beijingc);
        }
        if(position==3)
        {
            bianjiEditText.setBackgroundResource(R.drawable.beijingd);
        }
        if(position==4)
        {
            bianjiEditText.setBackgroundResource(R.drawable.beijinge);
        }
        if(position==5)
        {
            bianjiEditText.setBackgroundResource(R.drawable.beijingf);
        }
        if(position==6)
        {
            bianjiEditText.setBackgroundResource(R.drawable.beijingg);
        }
        if(position==7)
        {
            bianjiEditText.setBackgroundResource(R.drawable.beijingh);
        }
        if(position==8)
        {
            bianjiEditText.setBackgroundResource(R.drawable.beijingi);
        }
        if(position==9)
        {
            bianjiEditText.setBackgroundResource(R.drawable.beijingj);
        }
        if(position==10)
        {
            bianjiEditText.setBackgroundResource(R.drawable.beijingk);
        }
        if(position==11)
        {
            bianjiEditText.setBackgroundResource(R.drawable.beijingl);
        }
        if(position==12)
        {
            bianjiEditText.setBackgroundResource(R.drawable.beijingm);
        }
    }

    //选择主题
    //private static ArrayList<int> bit=new ArrayList<int>();

    //创建内部类ImageAdapter
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        public ImageAdapter(Context c) {
            mContext = c;
        }
        public int getCount() {
            return  bit.length;

        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);
            //    i.setImageResource(mThumbIds[position]);
           // i.setImageBitmap( bit.get(position));
            i.setImageResource(bit[position]);
            //设置边界对齐
            i.setAdjustViewBounds(true);
            //设置布局参数
           // i.setLayoutParams(new Gallery.LayoutParams(
             //       ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            i.setLayoutParams(new Gallery.LayoutParams(
                   200, 200));
            i.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //设置背景资源
            //  i.setBackgroundResource(R.drawable.bj);
            i.setBackgroundColor(0x00000000);

            return i;
        }

    }


    //主题颜色选择 2015 清明节 地里
    private void getcolorlog()
    {
        final Zidydhk dhk_hbcx = new Zidydhk(Bianji.this, R.layout.dhk_getcolor);
        View mView_szwz =dhk_hbcx.mView();
        dhk_hbcx.setCancelable(false);

        WindowManager manager = getWindow().getWindowManager();
        int height = (int) (manager.getDefaultDisplay().getHeight() * 0.65f);
        int width = (int) (manager.getDefaultDisplay().getWidth() * 0.9f);
        final Getcolorview myView = new Getcolorview(Bianji.this, height, width);

        FrameLayout root=(FrameLayout)mView_szwz.findViewById(R.id.dhk_getcolor_root);
        root.addView(myView);
        Button dhk_getcolor_qd = (Button) mView_szwz.findViewById(R.id.dhk_getcolor_qd);
        dhk_getcolor_qd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //自定义颜色
                dhk_hbcx.dismiss();
                //bianjiscr.setBackgroundColor(myView.getcolor());
                ztcolor=myView.getcolor();
                bianjiEditText.setBackgroundColor(ztcolor);

            }});
        Button dhk_getcolor_qx = (Button) mView_szwz.findViewById(R.id.dhk_getcolor_qx);
        dhk_getcolor_qx.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dhk_hbcx.dismiss();
            }});

        dhk_hbcx.show();

    }
    //文字颜色选择 2015 清明节 地里
    private void getwzcolorlog()
    {
        final Zidydhk dhk_hbcx = new Zidydhk(Bianji.this, R.layout.dhk_getcolor);
        View mView_szwz =dhk_hbcx.mView();
        dhk_hbcx.setCancelable(false);

        WindowManager manager = getWindow().getWindowManager();
        int height = (int) (manager.getDefaultDisplay().getHeight() * 0.65f);
        int width = (int) (manager.getDefaultDisplay().getWidth() * 0.9f);
        final Getcolorview myView = new Getcolorview(Bianji.this, height, width);

        FrameLayout root=(FrameLayout)mView_szwz.findViewById(R.id.dhk_getcolor_root);
        root.addView(myView);
        Button dhk_getcolor_qd = (Button) mView_szwz.findViewById(R.id.dhk_getcolor_qd);
        dhk_getcolor_qd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //自定义颜色
                dhk_hbcx.dismiss();
                //bianjiscr.setBackgroundColor(myView.getcolor());
                wzcolor=myView.getcolor();
                bianjiEditText.setTextColor(wzcolor);

            }});
        Button dhk_getcolor_qx = (Button) mView_szwz.findViewById(R.id.dhk_getcolor_qx);
        dhk_getcolor_qx.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dhk_hbcx.dismiss();
            }});

        dhk_hbcx.show();

    }



}







/*
定义：^$/-~tp   tp^$/-~
^$/-~Ts
^$/-~Tc
^$/-~T

^b~   ~b^
  字体大小  颜色   粗体  斜体  下滑线   删除线    是否为图片   是否为超链接
^$~9999    ffffff   f    f      f        f          f            f            |

//android TextView、EditText对部分内容设置颜色、字体、超链接、图片;
    //这里是以一个TextView为例子，EditText的设置方法和TextView一样

    //TextView对象
    TextView txtInfo = new TextView(this);

    //文本内容
    SpannableString ss = new SpannableString("红色打电话斜体删除线绿色下划线图片:.");

    //设置0-2的字符颜色
    ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 2,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    //设置2-5的字符链接到电话簿，点击时触发拨号
    ss.setSpan(new URLSpan("tel:4155551212"), 2, 5,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    //设置9-11的字符为网络链接，点击时打开页面
    ss.setSpan(new URLSpan("http://www.hao123.com"), 9, 11,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    //设置13-15的字符点击时，转到写短信的界面，发送对象为10086
    ss.setSpan(new URLSpan("sms:10086"), 13, 15,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    //粗体
    ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, 7,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    //斜体
    ss.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 7, 10,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

  //下划线
    ss.setSpan(new UnderlineSpan(), 10, 16,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//用删除线标记文本
   ss.setSpan(new StrikethroughSpan(), 7, 10,
     Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

  //以下代码是在指定位置插入图片
    Drawable d = getResources().getDrawable(R.drawable.icon);

    //设置图片大小
    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

    //插入的位置
    ss.setSpan(new ImageSpan(d, ImageSpan.ALIGN_BASELINE), 18, 19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

    //设置文本内容到textView
    txtInfo.setText(ss);

    //不添加这一句，拨号，http，发短信的超链接不能执行.
    txtInfo.setMovementMethod(LinkMovementMethod.getInstance());
复制代码
做个笔记，参考博客：http://blog.csdn.net/tao_zi7890/article/details/6094211
 */


/*
* 完成。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
* */