package com.youxianwubian.wenpian.layout;



import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.youxianwubian.wenpian.*;

public class GridViewFaceAdapter extends BaseAdapter
{
	// 定义Context
	private Context	mContext;
	// 定义整型数组 即图片源
	private int[] mImageIds;

	public GridViewFaceAdapter(Context c)
	{
		mContext = c;
		mImageIds = new int[]{
			R.mipmap.f001,R.mipmap.f002,R.mipmap.f003,R.mipmap.f004,R.mipmap.f005,R.mipmap.f006,
			R.mipmap.f007,R.mipmap.f008,R.mipmap.f009,R.mipmap.f010,R.mipmap.f011,R.mipmap.f012,
			R.mipmap.f013,R.mipmap.f014,R.mipmap.f015,R.mipmap.f016,R.mipmap.f017,R.mipmap.f018,
			R.mipmap.f019,R.mipmap.f020,R.mipmap.f021,R.mipmap.f022,R.mipmap.f023,R.mipmap.f024,
			R.mipmap.f025,R.mipmap.f026,R.mipmap.f027,R.mipmap.f028,R.mipmap.f029,R.mipmap.f030,
			R.mipmap.f031,R.mipmap.f032,R.mipmap.f033,R.mipmap.f034,R.mipmap.f035,R.mipmap.f036,
			R.mipmap.f037,R.mipmap.f038,R.mipmap.f039,R.mipmap.f040,R.mipmap.f041,R.mipmap.f042,
			R.mipmap.f043,R.mipmap.f044,R.mipmap.f045,R.mipmap.f046,R.mipmap.f047,R.mipmap.f048,
			R.mipmap.f049,R.mipmap.f050,R.mipmap.f051,R.mipmap.f052,R.mipmap.f053,R.mipmap.f054,
			R.mipmap.f055,R.mipmap.f056,R.mipmap.f057,R.mipmap.f058,R.mipmap.f059,R.mipmap.f060,
			R.mipmap.f061,R.mipmap.f062,R.mipmap.f063,R.mipmap.f064,R.mipmap.f065,R.mipmap.f067,
			R.mipmap.f068,R.mipmap.f069,R.mipmap.f070,R.mipmap.f071,R.mipmap.f072,R.mipmap.f073,
			R.mipmap.f074,R.mipmap.f075,R.mipmap.f076,R.mipmap.f077,R.mipmap.f078,R.mipmap.f079,
			R.mipmap.f080,R.mipmap.f081,R.mipmap.f082,R.mipmap.f083,R.mipmap.f084,R.mipmap.f085,
			R.mipmap.f086,R.mipmap.f087,R.mipmap.f088,R.mipmap.f089,R.mipmap.f090,R.mipmap.f091,
			R.mipmap.f092,R.mipmap.f093,R.mipmap.f094,R.mipmap.f095,R.mipmap.f096,R.mipmap.f097,
			R.mipmap.f098,R.mipmap.f099,R.mipmap.f100,R.mipmap.f101,R.mipmap.f103,R.mipmap.f104,
			R.mipmap.f105,
				R.mipmap.dit,R.mipmap.diu,R.mipmap.div,R.mipmap.diw,R.mipmap.dix,R.mipmap.diy,R.mipmap.diz,
				R.mipmap.dja,R.mipmap.djb,R.mipmap.djb,R.mipmap.djc,R.mipmap.djd,R.mipmap.dje,R.mipmap.djf,
				R.mipmap.djg,R.mipmap.djh,R.mipmap.dji,R.mipmap.djj,R.mipmap.djk,R.mipmap.djl,R.mipmap.djm,

				R.mipmap.wb_001,R.mipmap.wb_002,R.mipmap.wb_003,R.mipmap.wb_004,R.mipmap.wb_005,R.mipmap.wb_006
				,R.mipmap.wb_007,R.mipmap.wb_008,R.mipmap.wb_009,R.mipmap.wb_010,R.mipmap.wb_011,R.mipmap.wb_012
				,R.mipmap.wb_013,R.mipmap.wb_014,R.mipmap.wb_015,R.mipmap.wb_016,R.mipmap.wb_017,R.mipmap.wb_018
				,R.mipmap.wb_019,R.mipmap.wb_020,R.mipmap.wb_021,R.mipmap.wb_022,R.mipmap.wb_023,R.mipmap.wb_024
				,R.mipmap.wb_025,R.mipmap.wb_026,R.mipmap.wb_027,R.mipmap.wb_028,R.mipmap.wb_029,R.mipmap.wb_030
				,R.mipmap.wb_031,R.mipmap.wb_032,R.mipmap.wb_033,R.mipmap.wb_034,R.mipmap.wb_035,R.mipmap.wb_036
				,R.mipmap.wb_037,R.mipmap.wb_038,R.mipmap.wb_039,R.mipmap.wb_040,R.mipmap.wb_041,R.mipmap.wb_042
				,R.mipmap.wb_043,R.mipmap.wb_044,R.mipmap.wb_045,R.mipmap.wb_046,R.mipmap.wb_047,R.mipmap.wb_048
				,R.mipmap.wb_049,R.mipmap.wb_050,R.mipmap.wb_051,R.mipmap.wb_052
				,R.mipmap.tb_001,R.mipmap.tb_002,R.mipmap.tb_003,R.mipmap.tb_004,R.mipmap.tb_005,R.mipmap.tb_006,R.mipmap.tb_007,R.mipmap.tb_008
				,R.mipmap.tb_009,R.mipmap.tb_010,R.mipmap.tb_011,R.mipmap.tb_012,R.mipmap.tb_013,R.mipmap.tb_013,R.mipmap.tb_015,R.mipmap.tb_016
				,R.mipmap.tb_017,R.mipmap.tb_018,R.mipmap.tb_019,R.mipmap.tb_020,R.mipmap.tb_021,R.mipmap.tb_022,R.mipmap.tb_023,R.mipmap.tb_024
				,R.mipmap.tb_025,R.mipmap.tb_026,R.mipmap.tb_027,R.mipmap.tb_028,R.mipmap.tb_029,R.mipmap.tb_030,R.mipmap.tb_031,R.mipmap.tb_032
				,R.mipmap.tb_033,R.mipmap.tb_034
		};
	}

	// 获取图片的个数
	public int getCount()
	{
		return mImageIds.length;
	}

	// 获取图片在库中的位置
	public Object getItem(int position)
	{
		return position;
	}


	// 获取图片ID
	public long getItemId(int position)
	{
		return mImageIds[position];
	}


	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		if (convertView == null)
		{
			imageView = new ImageView(mContext);
			// 设置图片n×n显示
			imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
			// 设置显示比例类型
			imageView.setScaleType(ImageView.ScaleType.CENTER);
		}
		else
		{
			imageView = (ImageView) convertView;
		}
		
		imageView.setImageResource(mImageIds[position]);
		imageView.setTag("-b%"+position+"%b-");
		/*
		^b~   ~b^
		if(position < 65)
			imageView.setTag("["+position+"]");
		else if(position < 100)
			imageView.setTag("["+(position+1)+"]");
		else
			imageView.setTag("["+(position+2)+"]");*/

		return imageView;
	}

}
