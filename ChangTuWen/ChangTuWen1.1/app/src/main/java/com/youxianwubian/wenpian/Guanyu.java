package com.youxianwubian.wenpian;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youxianwubian.wenpian.layout.ImgListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/1/30.
 */
public class Guanyu extends  ActivityBase
{
    private ImgListView sListView;
    private List<String> sNewsList;
    private Context sContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.guanyu);
        sContext = this;
        init();
    }
    private void init()
    {
        sNewsList = new ArrayList<String>();
        sListView = (ImgListView) findViewById(R.id.xListView);
        NewsAdapter  sNewsAdapter = new NewsAdapter();
        sListView.setAdapter(sNewsAdapter);
        geneItems();


        sListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //toast(""+position);
                if(position==1){
                    //更新

                }
                if(position==3){
                    //加qq群
                    joinQQGroup("JNgKqKKT0E_mOrtvTKpaRyeYuRvYu1rV");
                }
                if(position==4){
                    ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    //clip.getText(); // 粘贴
                    clip.setText("1425255972"); // 复制
                    toast("qq号码已复制到粘贴板！");
                }
                //微博
                //http://weibo.com/5034086518
                if(position==5){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://weibo.com/5034086518"));
                    startActivity(intent);

                }
                //百度贴吧
                if(position==6){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tieba.baidu.com/f?kw=%E6%9C%89%E9%99%90%E6%97%A0%E8%BE%B9"));
                    startActivity(intent);

                }
            }
        });

        ImageButton   guanyu_fhtp = (ImageButton) findViewById(R.id.guanyu_fhtp);
        guanyu_fhtp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //返回
                Guanyu.this.finish();
            }
        });
        TextView   guanyu_fh = (TextView) findViewById(R.id.guanyu_fh);
        guanyu_fh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //返回
                Guanyu.this.finish();
            }
        });

    }
    private void geneItems() {
//        for (int i = 0; i != 10; ++i) {
//            sNewsList.add(""+i);
//        }
        sNewsList.add("版本：v1.1");
        sNewsList.add("作者：有限无边");
        sNewsList.add("qq群:542228575");
        sNewsList.add("qq:1425255972");
        sNewsList.add("微博：aide有限无边");
        sNewsList.add("贴吧：有限无边吧");

    }
    private class NewsAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public NewsAdapter() {
            mInflater = LayoutInflater.from(sContext);
        }

        @Override
        public int getCount() {
            return sNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return sNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder h = null;
            if (convertView == null) {
                h = new Holder();
                convertView = mInflater.inflate(R.layout.list_item, null);
                h.content = (TextView) convertView.findViewById(R.id.tv_content);
                convertView.setTag(h);
            } else {
                h = (Holder) convertView.getTag();
            }
            h.content.setText(sNewsList.get(position));
            return convertView;
        }

        private class Holder {
            public TextView content;
        }
    }
    /****************
     *
     * 发起添加群流程。群号：陕铁院app内部交流群(542228575) 的 key 为： JNgKqKKT0E_mOrtvTKpaRyeYuRvYu1rV
     * 调用 joinQQGroup(JNgKqKKT0E_mOrtvTKpaRyeYuRvYu1rV) 即可发起手Q客户端申请加群 陕铁院app内部交流群(542228575)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }
}
