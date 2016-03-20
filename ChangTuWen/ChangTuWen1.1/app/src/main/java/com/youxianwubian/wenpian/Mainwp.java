package com.youxianwubian.wenpian;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.update.UmengUpdateAgent;
import com.youxianwubian.wenpian.gongju.IOtxt;
import com.youxianwubian.wenpian.layout.GridViewFaceAdapter;
import com.youxianwubian.wenpian.layout.SlideLayout;
import com.youxianwubian.wenpian.layout.Zidydhk;
import com.youxianwubian.wenpian.whdui.SwipeMenu;
import com.youxianwubian.wenpian.whdui.SwipeMenuCreator;
import com.youxianwubian.wenpian.whdui.SwipeMenuItem;
import com.youxianwubian.wenpian.whdui.SwipeMenuListView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhang on 2016/1/29.
 */
public class Mainwp extends ActivityBase {
    private static String TAG = "Main Activity";
    private View mMenu;
    private int mMenuWidth;

    private SlideLayout mSlideLayout;
    //private ImageView mMenuButton;
    private AppAdapter mAdapter;
    private List<TextWz> mTextWz;

    private Zidydhk dhk_bjbq;

    private SwipeMenuListView listView;

    private List<String> mPathString = new ArrayList<String>();

    //private boolean istp;

    private RelativeLayout menu_wz;

    // private RelativeLayout menu_tp;

    private boolean isone = false;

    private boolean isstop = true;
    private Handler //获取数据，，并显示！
            myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            //如果该消息是本程序所发送的
            if (msg.what == 0x01) {
                if (listView == null) {
                    listView = (SwipeMenuListView) findViewById(R.id.cards_list);
                }
                mAdapter = new AppAdapter();
                listView.setAdapter(mAdapter);
                if (!isone) {
                    isone = true;
                }
            }
            if (msg.what == 0x02) {
                Book book = (Book) msg.getData().getSerializable("book");
                ViewHolder holder = book.getHo();
                //holder.listwz.setBackgroundColor(book.getZtcolor());
                if(book.getBit()!=null)
                {
                    holder.listwztp.setVisibility(View.VISIBLE);
                    holder.listwztp.setImageBitmap(book.getBit());
                }
                else {
                    holder.listwztp.setVisibility(View.GONE);
                }
                //Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/LibianSC.ttf");
                //使用字体
                //holder.listwznr.setTypeface(typeFace);
                // holder.listwzbt.setText(book.getBt());
                holder.listwznr.setText(book.getSpan());
                //holder.listwznr.setTextColor(book.getWzcolor());
                holder.listwzsj.setText(book.getSj());
                //设置主题
                int ztcolor=book.getZtcolor();
                if(ztcolor==100)
                {
                    holder.listwzbj.setBackgroundColor(Color.WHITE);
                }
                else {
                    if (ztcolor == 0 || ztcolor == 1 || ztcolor == 2 || ztcolor == 3 || ztcolor == 4 || ztcolor == 5 || ztcolor == 6 || ztcolor == 7 || ztcolor == 8 || ztcolor == 9 || ztcolor == 10 || ztcolor == 11 || ztcolor == 12 || ztcolor == 100) {
                        seteditbg(holder.listwzbj, ztcolor);
                    } else {
                        holder.listwzbj.setBackgroundColor(ztcolor);
                    }
                }
                //字体book.getWzzt();
            } else {
            }

        }
    };
    private void seteditbg(LinearLayout bianjiEditText,int position)
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mainwp);

        //检查更新
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);

        if (mThread == null) {
            mThread = new MyThread();
            mThread.run();
        } else {
            //mThread.stop();
            mThread = null;
            mThread = new MyThread();
            mThread.run();
        }
        mMenu = (View) findViewById(R.id.menu);
        mSlideLayout = (SlideLayout) findViewById(R.id.slide_layout);
        ViewTreeObserver vto = mMenu.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mMenu.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mMenuWidth = mMenu.getWidth();
                mSlideLayout.setMaxScrollX(mMenuWidth);
                //Log.v(TAG, "Max Scroll Distance: " + mMenuWidth);
            }
        });

        //菜单
        ImageButton main_list_cd = (ImageButton) findViewById(R.id.main_list_cd);
        main_list_cd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mSlideLayout.isMenuOpen()) {
                    mSlideLayout.closeMenu();
                } else {
                    mSlideLayout.openMenu();
                }
            }
        });
        ImageButton main_list_addnew = (ImageButton) findViewById(R.id.main_list_addnew);
        //main_list_addnew.setAlpha(0.7f);
        main_list_addnew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               //添加便签
                dhk_bq();
            }
        });

        //添加便签
        menu_wz = (RelativeLayout) mMenu.findViewById(R.id.menu_wz);
        menu_wz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //添加便签
                dhk_bq();
            }
        });
        //打开设置
        RelativeLayout menu_sz = (RelativeLayout) mMenu.findViewById(R.id.menu_sz);
        menu_sz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(Mainwp.this,Shezhi.class);
                startActivity(intent);
            }
        });
        //打开评分
        RelativeLayout menu_fx = (RelativeLayout) mMenu.findViewById(R.id.menu_fx);
        menu_fx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               //评分
                Uri uri = Uri.parse("market://details?id="+getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //打开关于
        RelativeLayout menu_gy = (RelativeLayout) mMenu.findViewById(R.id.menu_gy);
        menu_gy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Mainwp.this, Guanyu.class);
                startActivity(intent);
            }
        });

        if (listView == null) {
            listView = (SwipeMenuListView) findViewById(R.id.cards_list);
        }
        mSlideLayout.setlist(listView);
        listView.setFriction(ViewConfiguration.getScrollFriction() * 2);
        // step 1. create a MenuCreator 设置滑动菜单
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.mipmap.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        listView.setMenuCreator(creator);

        // step 2. listener item click event  滑动菜单响应
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                //ApplicationInfo item = mAppList.get(position);
                switch (index) {
                    case 0:
                        // open
                        //	open(item);
                        Intent it = new Intent(Mainwp.this, Bianji.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("wbid", mTextWz.get(position).getid());
                        bundle.putString("nr", getSDCardPath() + "/wenpian/wz/" + mPathString.get(position) + "/zj.iso");

                        String ztcolor= mTextWz.get(position).getztcolor();
                        if(!ztcolor.equals(""))
                        {
                            //bo.setZtcolor(strzint(ztcolor));
                            bundle.putInt("ztcolor",strzint(ztcolor));
                        }
                        String wzcolor= mTextWz.get(position).getwzcolor();
                        if(!wzcolor.equals(""))
                        {
                            //bo.setWzcolor(strzint(wzcolor));
                            bundle.putInt("wzcolor",strzint(wzcolor));
                        }
                        String wzzt= mTextWz.get(position).getwzzt();
                        if(!wzzt.equals(""))
                        {
                            //bo.setWzzt(strzint(wzzt));
                            bundle.putInt("wzzt",strzint(wzzt));
                        }


                        it.putExtra("bd", bundle);
                        startActivity(it);
                        break;
                    case 1:
                        // delete
//					delete(item);
                        //	mAppList.remove(position);
                        //	mAdapter.notifyDataSetChanged();
                        //Toast.makeText(Mainwp.this, "删除.." + index, Toast.LENGTH_LONG).show();
                        File filesc = new File(getSDCardPath() + "/wenpian/wz/" + mPathString.get(position));
                        IOtxt.RecursionDeleteFile(filesc);
                        sxlb();
                        break;
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    //isInit = true;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 滑动停止
                        isstop = true;
						/*	for (; start_index < end_index; start_index++) {
								ImageView img = (ImageView) listView.findViewWithTag(start_index);
								//img.setImageResource(R.drawable.update_log);
							}*/
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        isstop = true;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        isstop = false;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                // 设置当前屏幕显示的起始index和结束index
				/*	start_index = firstVisibleItem;
					end_index = firstVisibleItem + visibleItemCount;*/

            }
        });


        // set SwipeListener
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                //Toast.makeText(getApplicationContext(), position + " long click", 0).show();
                //toast(position + " long click");
                listView.closecd();
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (listView.getisopen()) {
                    listView.closecd();
                } else {

                        Intent it = new Intent(Mainwp.this, Bianji.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("wbid", mTextWz.get(position).getid());
                        bundle.putString("nr", getSDCardPath() + "/wenpian/wz/" + mPathString.get(position) + "/zj.iso");
                    String ztcolor= mTextWz.get(position).getztcolor();
                    if(!ztcolor.equals(""))
                    {
                        //bo.setZtcolor(strzint(ztcolor));
                        bundle.putInt("ztcolor",strzint(ztcolor));
                    }
                    String wzcolor= mTextWz.get(position).getwzcolor();
                    if(!wzcolor.equals(""))
                    {
                        //bo.setWzcolor(strzint(wzcolor));
                        bundle.putInt("wzcolor",strzint(wzcolor));
                    }
                    String wzzt= mTextWz.get(position).getwzzt();
                    if(!wzzt.equals(""))
                    {
                        //bo.setWzzt(strzint(wzzt));
                        bundle.putInt("wzzt",strzint(wzzt));
                    }
                        it.putExtra("bd", bundle);
                        startActivity(it);

                }

            }
        });


    }

    //当再次启动应用时，
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        if (isone) {
            sxlb();
        }
        super.onResume();
    }

    //刷新列表
    private void sxlb() {
        //convertView=null;

        // menu_wz.setBackgroundColor(0x60ffffff);
        //menu_tp.setBackgroundColor(0x00000000);
        if (mThread == null) {
            mThread = new MyThread();
            mThread.run();
        } else {
            //	mThread.stop();
            mThread = null;
            mThread = new MyThread();
            mThread.run();
        }
    }

    private MyThread mThread;
    class MyThread extends Thread {
        //private boolean bo;
        public MyThread() {

        }

        @Override
        public void run() {
            // TODO Auto-generated method stub2
            readFile();
        }
    }

    // 检查扩展名，得到图片格式的文件
    private static boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (//FileEnd.equals("jpg") ||
                FileEnd.equals("yl")//||FileEnd.equals("jpg")
            //|| FileEnd.equals("png") || FileEnd.equals("jpeg")
                ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }

    /**
     * 获取SDCard的目录路径功能
     *
     * @return
     */
    private String getSDCardPath() {
        File sdcardDir = null;
        //判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }

    private void readFile() {
        //开启线程
		/*mthread = new Thread(new Runnable() {
				public void run() {*/

        File filea = new File(getSDCardPath() + "/wenpian/wz");
        //如果不存在文件夹则创建文件夹
        if (!filea.exists()) {
            filea.mkdirs();
        }
        //mPathString=null;
        mPathString = new ArrayList<String>();
        final File[] file = new File(getSDCardPath() + "/wenpian/wz").listFiles();//设定扫描路径
        readFile(file);

        Message msg = new Message();
        msg.what = 0x01;
        //发送消息
        myHandler.sendMessage(msg);
			/*	}
			}).start();*/
    }

    private void readFile(final File[] file) {
        for (int i = 0; file != null && i < file.length; i++) {
//判读是否文件以及文件后缀名
            //	if(file[i].isFile()/* && file[i].getName().endsWith("xml")*/){
            //	fileList.add(file[i].toString());
            //}
//如果是文件夹，递归扫描
            //	else if(file[i].isDirectory()) {
            //	final File[] newFileList = new File(file[i].getAbsolutePath()).listFiles();
            //	readFile(newFileList);
//通过多线程来加速
            //	 new Thread(new Runnable() {
            // public void run() {
            // readFile(newFileList);
            // }
            // }).start();
            //	}
            if (file[i].isDirectory()) {
				/*TextWz twz=new TextWz();
				twz.setbt(file[i].getName());
				mTextWz.add(twz);*/
                mPathString.add(file[i].getName());
            }

        }
        Collections.reverse(mPathString);
       // Log.i("iiiiiiiiiiiiiiiiiiiiimPathString",""+mPathString.size());

        for (int j = 0; j < mPathString.size(); j++) {
            File filea = new File(getSDCardPath() + "/wenpian/wz/" + mPathString.get(j) + "/id.iso");
            //如果不存在文件夹则删除

            if (!filea.exists()) {
                File fileb = new File(getSDCardPath() + "/wenpian/wz/" + mPathString.get(j));
                IOtxt.RecursionDeleteFile(fileb);
                //fileb.delete();
                mPathString.remove(j);
            }
        }
        //mTextWz=null;
        mTextWz = new ArrayList<TextWz>();
        for (int j = 0; j < mPathString.size(); j++) {
            File filea = new File(getSDCardPath() + "/wenpian/wz/" + mPathString.get(j) + "/id.iso");
            //如果存在文件夹则进行
            if (filea.exists()) {
                String hst = IOtxt.readFile(getSDCardPath() + "/wenpian/wz/" + mPathString.get(j) + "/id.iso");
                Pattern p = Pattern.compile("_time_([\\s\\S]*)_nr_([\\s\\S]*)_ztcolor_([\\s\\S]*)_wzcolor_([\\s\\S]*)_wzzt_([\\s\\S]*)");
                Matcher m = p.matcher(hst);
                while (m.find()) {
                    TextWz twz = new TextWz();
                    twz.setid(mPathString.get(j));
                    twz.setsj(m.group(1));
                    twz.setnr(m.group(2));
                    twz.setztcolor(m.group(3));
                    twz.setwzcolor(m.group(4));
                    twz.setwzzt(m.group(5));
                    //twz.seticonlj(getSDCardPath() + "/wenpian/wz/" + mPathString.get(j) + "/yl.jpg");
                    //twz.setZjlj(getSDCardPath()+"/wenpian/wz/"+mPathString.get(j)+"/zj.iso");
                    File fileac = new File(getSDCardPath() + "/wenpian/wz/" + mPathString.get(j) + "/");
                    List<String> tplist=IOtxt.gettp(fileac);
                    if(tplist!=null&&tplist.size()>0)
                    {
                        twz.seticonlj(tplist.get(0));
                        //Log.i("ttttttttttttttttttttttttttttttplist",""+tplist.get(0));
                    }
                    else
                    {
                        twz.seticonlj("0");
                    }
                    mTextWz.add(twz);
                }
                //System.out.println(m.group(1));
            }

        }
        //Log.i("ttttttttttttttttttttttttttttttimPathString",""+mPathString.size());
        //Log.i("cccccccccccccccccccccccccccccccccccccccccccccccccmPathString",""+mTextWz.size());
        // toast(mTextWz.size()+"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(TAG, "Main Touch Here");
        return true;
    }

    class AppAdapter extends BaseAdapter {
	/*	@Override
		public Object getItem(int p1)
		{
			// TODO: Implement this method
			return null;
		}
		*/

        @Override
        public int getCount() {
            return mTextWz.size();
        }

        @Override
        public TextWz getItem(int position) {
            return mTextWz.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {

                convertView = View.inflate(getApplicationContext(),
                        R.layout.list_wz, null);
                new ViewHolder(convertView);

            }
            //ViewHolder holder = (ViewHolder) convertView.getTag();
            //ApplicationInfo item = getItem(position);
            //holder.tv_name.setText(item.loadLabel(getPackageManager()));

			/*holder.listwzbt.setText(mTextWz.get(position).getbt());
			holder.listwzsj.setText(mTextWz.get(position).getsj());
			holder.listwznr.setText(mTextWz.get(position).getnr());*/

            //  String iconlj=mTextWz.get(position).geticonlj();
            //File file = new File(iconlj);
           /* if(!file.exists()){
               // holder.listwztp.setImageURI(Uri.fromFile(file));
				holder.listwztp.setVisibility(View.GONE);

            }
			else
			{
				holder.listwztp.setVisibility(View.VISIBLE);

			}*/
            //if(isstop)
            //{
            MyThreadb th = new MyThreadb(position, convertView);
            th.run();
            //}
			/*
			 new Thread(new Runnable() {
	 public void run() {

	 }
	 }).start();
			*/
            //将线程接口立刻送到线程队列中
            //MyRunnable myr=new MyRunnable(position,convertView);
            //  handler.post(myr.update_thread);


            return convertView;
        }

    }

    public class ViewHolder {
      //TextView listwzbt;
        TextView listwzsj;
        TextView listwznr;
        ImageView listwztp;
        LinearLayout listwzbj;
        public ViewHolder(View view) {
          //listwzbt = (TextView) view.findViewById(R.id.listwzbt);
            listwzbj = (LinearLayout) view.findViewById(R.id.listwzlinbg);
            listwzsj = (TextView) view.findViewById(R.id.listwzsj);
            listwznr = (TextView) view.findViewById(R.id.listwznr);
            listwztp = (ImageView) view.findViewById(R.id.listwztp);
            view.setTag(this);
        }
    }

    /*
        public class ViewHolderb {
            TextView listtpbt;
            TextView listtpsj;
            //TextView listwznr;
            ImageView listtptp;
            public ViewHolderb(View view) {
                listtpbt = (TextView) view.findViewById(R.id.listtpbt);
                listtpsj = (TextView) view.findViewById(R.id.listtpsj);
                //listwznr = (TextView) view.findViewById(R.id.listwznr);
                listtptp = (ImageView) view.findViewById(R.id.listtptp);
                view.setTag(this);
            }
        }*/
    //private MyThreadb mThreadb;
    class MyThreadb extends Thread {
        private int position;
        private View convertView = null;
        private Bitmap bitb = null;

        public MyThreadb(int position, View convertView) {
            this.position = position;
            this.convertView = convertView;
        }

        @Override
        public void run() {
			/*  Message msg = new Message();
			 msg.what = 0;
			 //发送消息
			 myHandler.sendMessage(msg);*/

            ViewHolder holder = (ViewHolder) convertView.getTag();
            String iconlj = mTextWz.get(position).geticonlj();
            if(iconlj!="0") {
                File file = new File(iconlj);
                if (file.exists()) {
                    bitb = getbit(iconlj);
                }
            }
            Message msg = Message.obtain();
            Bundle b = new Bundle();
            //b.putParcelable("bit", (Parcelable) bit);
            Book bo = new Book();
            bo.setBit(bitb);
            bo.setHo(holder);
         // bo.setBt(mTextWz.get(position).getbt());
            bo.setNr(mTextWz.get(position).getnr());
            bo.setSj(mTextWz.get(position).getsj());

            String ztcolor= mTextWz.get(position).getztcolor();
            if(!ztcolor.equals(""))
            {
                bo.setZtcolor(strzint(ztcolor));
            }
            String wzcolor= mTextWz.get(position).getwzcolor();
            if(!wzcolor.equals(""))
            {
                bo.setWzcolor(strzint(wzcolor));
            }
            String wzzt= mTextWz.get(position).getwzzt();
            if(!wzzt.equals(""))
            {
                bo.setWzzt(strzint(wzzt));
            }

            //Integer.parseInt(s);

            //b.putParcelable("book", (Parcelable) bo);
            b.putSerializable("book", bo);
            msg.setData(b);
            msg.what = 0x02;
            myHandler.sendMessage(msg);


        }
    }
    private int strzint(String st)
    {
        return Integer.parseInt(st);
    }
    //设置文本表情
    private SpannableString setbiaoqing(String st)
    {
       // String st= edtext.getText().toString();
        SpannableString ss = new SpannableString(st);

        Pattern pb=Pattern.compile("-b%(.+?)%b-");
        Matcher mb=pb.matcher(st);
        while(mb.find()){
            Pattern pbb=Pattern.compile("-b%(.+?)%b-");
            Matcher mbb=pbb.matcher(mb.group());
            while(mbb.find()){
                //插入的表情
                // SpannableString ssb = new SpannableString(st);
                GridViewFaceAdapter mGVFaceAdapter = new GridViewFaceAdapter(this);
                Drawable d = getResources().getDrawable((int) mGVFaceAdapter.getItemId(Integer.valueOf(mbb.group(1))));
                d.setBounds(0, 0, 50, 50);//设置表情图片的显示大小
                ImageSpan spanb = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
                ss.setSpan(spanb,mb.start(), mb.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //在光标所在处插入表情
                //bianjiEditText.getText().insert(bianjiEditText.getSelectionStart(), ss);
            }
        }
        //bianjiEditText.setText(ss);
        return ss;
    }
    public class Book implements Serializable {
        private ViewHolder ho;
        private Bitmap bit;
        private String nr, sj;
        private SpannableString span;
        private int ztcolor;
        private int wzcolor;
        private int wzzt;

        public void setZtcolor(int ztcolor)
        {
            this.ztcolor=ztcolor;
        }
        public int getZtcolor()
        {
            return ztcolor;
        }
        public void setWzcolor(int wzcolor)
        {
            this.wzcolor=wzcolor;
        }
        public int getWzcolor()
        {
            return wzcolor;
        }
        public void setWzzt(int wzzt)
        {
            this.wzzt=wzzt;
        }
        public int getWzzt()
        {
            return wzzt;
        }

        public void setSpan(SpannableString span) {
            this.span = span;
        }
        public SpannableString getSpan() {
           return span;
        }


        public void setNr(String nr) {
            this.nr = nr;
            setSpan(setbiaoqing(nr));
        }

        public String getNr() {
            return nr;
        }

        public void setSj(String sj) {
            this.sj = sj;
        }

        public String getSj() {
            return sj;
        }

      /*  public void setBt(String bt) {
            this.bt = bt;
        }

        public String getBt() {
            return bt;
        }*/


        public void setHo(ViewHolder ho) {
            this.ho = ho;
        }

        public ViewHolder getHo() {
            return ho;
        }

        public void setBit(Bitmap bit) {
            this.bit = bit;
        }

        public Bitmap getBit() {
            return bit;
        }
    }

    private Bitmap getbit(String iconlj) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        // 获取这个图片的宽和高

        Bitmap bitmap = BitmapFactory.decodeFile(iconlj, options); //此时返回bm为空

        options.inJustDecodeBounds = false;

        //计算缩放比

        int be = (int) (options.outHeight / (float) 300);

        if (be <= 0)

            be = 1;

        options.inSampleSize = be;

        //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦

        bitmap = BitmapFactory.decodeFile(iconlj, options);


        //判断是否有图片

        if (bitmap != null) {
            return bitmap;
        } else {
            return null;
        }

    }


    //日记文字版
    private class TextWz {
        private String id;
        private String nr;
        private String sj;
        private String iconlj;
        private String ztcolor,wzcolor;
        private String wzzt;

        public void setztcolor(String ztcolor) {
            this.ztcolor = ztcolor;
        }
        public String getztcolor() {
            return this.ztcolor;
        }
        public void setwzcolor(String wzcolor) {
            this.wzcolor = wzcolor;
        }
        public String getwzcolor() {
            return this.wzcolor;
        }
        public void setwzzt(String wzzt) {
            this.wzzt = wzzt;
        }
        public String getwzzt() {
            return this.wzzt;
        }


        public void setid(String id) {
            this.id = id;
        }

        public String getid() {
            return this.id;
        }

        public void setsj(String sj) {
            this.sj = sj;
        }

        public String getsj() {
            return this.sj;
        }

        public void setnr(String nr) {
            this.nr = nr;
        }

        public String getnr() {
            return this.nr;
        }

        public void seticonlj(String iconlj) {
            this.iconlj = iconlj;
        }

        public String geticonlj() {
            return this.iconlj;
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void dhk_bq() {
        /*dhk_bjbq = new Zidydhk(this, R.layout.dhk_bjbq);
        View mView = dhk_bjbq.mView();
        //dhk_bjqd.setCancelable(false);
        final EditText ed = (EditText) mView.findViewById(R.id.dhkbjbqet);
        Button dhkbjbqbu = (Button) mView.findViewById(R.id.dhkbjbqbu);
        dhkbjbqbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st = ed.getText().toString();

                if (st.equals("")) {
                    Toast.makeText(Mainwp.this, "标签不能为空", Toast.LENGTH_LONG).show();
                } else {
                    Intent it = new Intent(Mainwp.this, Bianji.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", st);
                    it.putExtra("bd", bundle);
                    startActivity(it);
                    dhk_bjbq.dismiss();
                }

            }
        });


        dhk_bjbq.show();*/


        Intent it = new Intent(Mainwp.this, Bianji.class);
        Bundle bundle = new Bundle();
        bundle.putString("name",this.getString(R.string.app_name) );
        bundle.putInt("ztcolor",100);
        it.putExtra("bd", bundle);
        startActivity(it);
    }
    //返回退出
    long current = 0;
    public void onBackPressed() {
        if(mSlideLayout.isMenuOpen()) {
            mSlideLayout.closeMenu();
        } else {
            if ((System.currentTimeMillis() - current) > 2000) {
                Toast.makeText(this, "亲，你要离我而去吗？\n" + "再次按返回键退出",
                        Toast.LENGTH_SHORT).show();
                current = System.currentTimeMillis();
            } else {

                this.finish();
                System.exit(0);}
        }
    }

}

/*
	Message msg = Message.obtain();

	Bundle b = new Bundle();
	b.putParcelable("MyObject", (Parcelable) object);
	msg.setData(b);

	handler.sendMessage(msg);
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		MyObject objectRcvd = (MyObject) msg.getData().getParcelable("MyObject");
		。。。。。。。。
    }
*/
/*
    private void sxtp()
    {
        mTextWz=new ArrayList<TextWz>();
        mPathString = new ArrayList<String>();
        File filea = new File(getSDCardPath()+"/wenpian/pt");
        //如果不存在文件夹则创建文件夹
        if(!filea.exists())
        {filea.mkdirs();}
        // 得到该路径文件夹下所有的文件
        File mfile = new File(getSDCardPath()+"/wenpian/pt");
        File[] files = mfile.listFiles();
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                //picList.add(file.getPath());
                mPathString.add(file.getName());


                //file.getPath();

            }
        }
        Collections.reverse(mPathString);
        for(int j=0;j<mPathString.size();j++)
        {
            //File fi=new File(mPathString.get(j));
            Pattern p=Pattern.compile("([\\s\\S]*)@&([\\s\\S]*).yl");
            Matcher m=p.matcher(mPathString.get(j));
            while(m.find()){
                TextWz twz=new TextWz();
                twz.setsj(m.group(1));
                twz.setbt(m.group(2));
                twz.seticonlj(getSDCardPath()+"/wenpian/pt/"+mPathString.get(j));

                mTextWz.add(twz);
                //twz.seticonlj(getSDCardPath()+"/wenpian/wz/"+mPathString.get(j)+"/yl.jpg");
                //mTextWz.add(twz);
                //System.out.println(m.group(1));
            }
        }
        Message msg = new Message();
        msg.what = 0x01;
        //发送消息
        myHandler.sendMessage(msg);

        // 返回得到的图片列表
    }
    */



	/*
	 new Thread(new Runnable() {
	 public void run() {

	 }
	 }).start();
	*/



    /*private void delete(ApplicationInfo item) {
        // delete app
        try {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.fromParts("package", item.packageName, null));
            startActivity(intent);
        } catch (Exception e) {
        }
    }*/
/*
	private void open(ApplicationInfo item) {
		// open app
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(item.packageName);
		List<ResolveInfo> resolveInfoList = getPackageManager()
			.queryIntentActivities(resolveIntent, 0);
		if (resolveInfoList != null && resolveInfoList.size() > 0) {
			ResolveInfo resolveInfo = resolveInfoList.get(0);
			String activityPackageName = resolveInfo.activityInfo.packageName;
			String className = resolveInfo.activityInfo.name;

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName componentName = new ComponentName(
				activityPackageName, className);

			intent.setComponent(componentName);
			startActivity(intent);
		}

	}
*/

/*
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.menuButton: {
					Intent intent = new Intent(MainActivity.this, Bianji.class);
					startActivity(intent);

				/*	if(mSlideLayout.isMenuOpen()) {
						mSlideLayout.closeMenu();
					} else {
						mSlideLayout.openMenu();
					}*/
		/*			break;
				}
			default:
				break;
		}
	}
	*/


	/*
	 @Override
	 　　public View getView(int position, View convertView, ViewGroup parent) {
	 　　if(convertView == null){
	 　　convertView = mInflater.inflate(R.layout.book_item_adapter, null);
	 　　}
	 　　BookModel model = mModels.get(position);
	 　　convertView.setTag(position);
	 　　ImageView iv = (ImageView) convertView.findViewById(R.id.sItemIcon);
	 　　TextView sItemTitle = (TextView) convertView.findViewById(R.id.sItemTitle);
	 　　TextView sItemInfo = (TextView) convertView.findViewById(R.id.sItemInfo);
	 　　sItemTitle.setText(model.book_name);
	 　　sItemInfo.setText(model.out_book_url);
	 　　iv.setBackgroundResource(R.drawable.rc_item_bg);
	 　　syncImageLoader.loadImage(position,model.out_book_pic,imageLoadListener);
	 　　return convertView;
	 　　}
	 　　SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener(){
	 　　@Override
	 　　public void onImageLoad(Integer t, Drawable drawable) {
	 　　//BookModel model = (BookModel) getItem(t);
	 　　View view = mListView.findViewWithTag(t);
	 　　if(view != null){
	 　　ImageView iv = (ImageView) view.findViewById(R.id.sItemIcon);
	 　　iv.setBackgroundDrawable(drawable);
	 　　}
	 　　}
	 　　@Override
	 　　public void onError(Integer t) {
	 　　BookModel model = (BookModel) getItem(t);
	 　　View view = mListView.findViewWithTag(model);
	 　　if(view != null){
	 　　ImageView iv = (ImageView) view.findViewById(R.id.sItemIcon);
	 　　iv.setBackgroundResource(R.drawable.rc_item_bg);
	 　　}
	 　　}
	 　　};
	 　　public void loadImage(){
	 　　int start = mListView.getFirstVisiblePosition();
	 　　int end =mListView.getLastVisiblePosition();
	 　　if(end >= getCount()){
	 　　end = getCount() -1;
	 　　}
	 　　syncImageLoader.setLoadLimit(start, end);
	 　　syncImageLoader.unlock();
	 　　}
	 　　AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
	 　　@Override
	 　　public void onScrollStateChanged(AbsListView view, int scrollState) {
	 　　switch (scrollState) {
	 　　case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
	 　　DebugUtil.debug("SCROLL_STATE_FLING");
	 　　syncImageLoader.lock();
	 　　break;
	 　　case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
	 　　DebugUtil.debug("SCROLL_STATE_IDLE");
	 　　loadImage();
	 　　//loadImage();
	 　　break;
	 　　case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
	 　　syncImageLoader.lock();
	 　　break;
	 　　default:
	 　　break;
	 　　}
	 　　}
	 　　@Override
	 　　public void onScroll(AbsListView view, int firstVisibleItem,
	 　　int visibleItemCount, int totalItemCount) {
	 　　// TODO Auto-generated method stub
	 　　}
	 　　};
	 　　package cindy.android.test.synclistview;
	 　　Syncimageloader代码 ：
	 　　import java.io.DataInputStream;
	 　　import java.io.File;
	 　　import java.io.FileInputStream;
	 　　import java.io.FileOutputStream;
	 　　import java.io.IOException;
	 　　import java.io.InputStream;
	 　　import java.lang.ref.SoftReference;
	 　　import java.net.URL;
	 　　import java.util.HashMap;
	 　　import android.graphics.drawable.Drawable;
	 　　import android.os.Environment;
	 　　import android.os.Handler;
	 　　public class SyncImageLoader {
	 　　private Object lock = new Object();
	 　　private boolean mAllowLoad = true;
	 　　private boolean firstLoad = true;
	 　　private int mStartLoadLimit = 0;
	 　　private int mStopLoadLimit = 0;
	 　　final Handler handler = new Handler();
	 　　private HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
	 　　public interface OnImageLoadListener {
	 　　public void onImageLoad(Integer t, Drawable drawable);
	 　　public void onError(Integer t);
	 　　}
	 　　public void setLoadLimit(int startLoadLimit,int stopLoadLimit){
	 　　if(startLoadLimit > stopLoadLimit){
	 　　return;
	 　　}
	 　　mStartLoadLimit = startLoadLimit;
	 　　mStopLoadLimit = stopLoadLimit;
	 　　}
	 　　public void restore(){
	 　　mAllowLoad = true;
	 　　firstLoad = true;
	 　　}
	 　　public void lock(){
	 　　mAllowLoad = false;
	 　　firstLoad = false;
	 　　}
	 　　public void unlock(){
	 　　mAllowLoad = true;
	 　　synchronized (lock) {
	 　　lock.notifyAll();
	 　　}
	 　　}
	 　　public void loadImage(Integer t, String imageUrl,
	 　　OnImageLoadListener listener) {
	 　　final OnImageLoadListener mListener = listener;
	 　　final String mImageUrl = imageUrl;
	 　　final Integer mt = t;
	 　　new Thread(new Runn

	*/
//获取图片   Uri.fromFile(temp)
/*	private Bitmap uriOCRb(Uri uri,int siza) {
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
	}*/

/*
1. 读取操作
String path = '/sdcard/foo.txt';
String content = ''; //文件内容字符串
//打开文件
File file = new File(path);
//如果path是传递过来的参数，可以做一个非目录的判断
if (file.isDirectory()){
Toast.makeText(EasyNote.this, '没有指定文本文件！', 1000).show();
}
else{
try {
InputStream instream = new FileInputStream(file);
if (instream != null) {
InputStreamReader inputreader = new InputStreamReader(instream);
BufferedReader buffreader = new BufferedReader(inputreader);
String line;
//分行读取
while (( line = buffreader.readLine()) != null) {
content += line + '\n';
}
instream.close();
} catch (java.io.FileNotFoundException e) {
Toast.makeText(EasyNote.this, '文件不存在', Toast.LENGTH_SHORT).show();
} catch (IOException e) {
e.printStackTrace();
}
}
2. 写入操作
String filePath = '/sdcard/foo2.txt';
String content = '这是将要写入到文本文件的内容';
//如果filePath是传递过来的参数，可以做一个后缀名称判断； 没有指定的文件名没有后缀，则自动保存为.txt格式
if(!filePath.endsWith('.txt') && !filePath.endsWith('.log'))
filePath += '.txt';
//保存文件
File file = new File(filePath);
try {
OutputStream outstream = new FileOutputStream(file);
OutputStreamWriter out = new OutputStreamWriter(outstream);
out.write(content);
out.close();
} catch (java.io.IOException e) {
e.printStackTrace();
}

2
3
4
5
6
7
8
9
10
String Name = File.getName();  //获得文件或文件夹的名称：
String parentPath = File.getParent();  获得文件或文件夹的父目录
String path = File.getAbsoultePath();//绝对路经
String path = File.getPath();//相对路经
File.createNewFile();//建立文件
File.mkDir(); //建立文件夹
File.isDirectory(); //判断是文件或文件夹
File[] files = File.listFiles();  //列出文件夹下的所有文件和文件夹名
File.renameTo(dest);  //修改文件夹和文件名
File.delete();  //删除文件夹或文件

/*
	 public class TestActivity extends Activity {
	 /** Called when the activity is first created. */
	/*Button button1;
	List<String> fileList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
// TODO Auto-generated method stub
					fileList = new ArrayList<String>();
					readFile();
					for(int i=0 ; i<fileList.size(); i++) {
						Log.i("syso", fileList.get(i).toString());
					}
				}
			});
	}*/






/*


package com.baoyz.swipemenulistviewsample;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;

public class SimpleActivity extends Activity {

	private List<ApplicationInfo> mAppList;
	private AppAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		mAppList = getPackageManager().getInstalledApplications(0);

		SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView);
		mAdapter = new AppAdapter();
		listView.setAdapter(mAdapter);

		// step 1. create a MenuCreator 设置滑动菜单
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("Open");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		listView.setMenuCreator(creator);

		// step 2. listener item click event  滑动菜单响应
		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				ApplicationInfo item = mAppList.get(position);
				switch (index) {
				case 0:
					// open
					open(item);
					break;
				case 1:
					// delete
//					delete(item);
					mAppList.remove(position);
					mAdapter.notifyDataSetChanged();
					break;
				}
			}
		});

		// set SwipeListener
		listView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

		// test item long click
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(), position + " long click", 0).show();
				return false;
			}
		});
	}

	private void delete(ApplicationInfo item) {
		// delete app
		try {
			Intent intent = new Intent(Intent.ACTION_DELETE);
			intent.setData(Uri.fromParts("package", item.packageName, null));
			startActivity(intent);
		} catch (Exception e) {
		}
	}

	private void open(ApplicationInfo item) {
		// open app
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(item.packageName);
		List<ResolveInfo> resolveInfoList = getPackageManager()
				.queryIntentActivities(resolveIntent, 0);
		if (resolveInfoList != null && resolveInfoList.size() > 0) {
			ResolveInfo resolveInfo = resolveInfoList.get(0);
			String activityPackageName = resolveInfo.activityInfo.packageName;
			String className = resolveInfo.activityInfo.name;

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName componentName = new ComponentName(
					activityPackageName, className);

			intent.setComponent(componentName);
			startActivity(intent);
		}
	}

	class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mAppList.size();
		}

		@Override
		public ApplicationInfo getItem(int position) {
			return mAppList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_list_app, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			ApplicationInfo item = getItem(position);
			holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
			holder.tv_name.setText(item.loadLabel(getPackageManager()));
			return convertView;
		}

		class ViewHolder {
			ImageView iv_icon;
			TextView tv_name;

			public ViewHolder(View view) {
				iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				tv_name = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(this);
			}
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}

*/

