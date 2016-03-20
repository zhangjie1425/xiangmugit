package com.youxianwubian.wenpian.layout;

import android.app.*;
import android.content.*;
import android.view.*;
import android.view.ViewGroup.*;
import android.view.animation.*;
import android.widget.*;
import com.youxianwubian.wenpian.*;

	/**
	 * @Function: 自定义对话框
	 * @Date: 2013-10-28
	 * @Time: 下午12:37:43
	 * @author Tom.Cai
	 */
public class Zidydhk extends Dialog {
	//	private EditText editText;
		//private Button positiveButton, negativeButton;
//	private TextView title;

	private View mView;
/*
	private int la;

	private int buqd;

	private int buqx;
*/
		public Zidydhk(Context context,int la) {
			super(context,R.style.ActionSheetDialogStyle);
			//模糊
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
								 WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			mView = LayoutInflater.from(getContext()).inflate(la, null);
			//	title = (TextView) mView.findViewById(R.id.title);
			//		editText = (EditText) mView.findViewById(R.id.number);
			//positiveButton = (Button) mView.findViewById(buqd);
			//negativeButton = (Button) mView.findViewById(buqx);


			super.setContentView(mView);
		}

		/*private void setCustomDialog() {
		
		}
		*/
		//返回所创建的View
		public View mView()
		{
			
			return mView;
		}

/*		public View getEditText(){
			return editText;
		}
*/
		@Override
		public void setContentView(int layoutResID) {
		}

		@Override
		public void setContentView(View view, LayoutParams params) {
		}

		@Override
		public void setContentView(View view) {
		}

		/**
		 * 确定键监听器
		 * @param listener
		 */ 
	/*	public void setOnPositiveListener(View.OnClickListener listener){ 
			positiveButton.setOnClickListener(listener); 
		} */
		/**
		 * 取消键监听器
		 * @param listener
		 */ 
		/*public void setOnNegativeListener(View.OnClickListener listener){ 
			negativeButton.setOnClickListener(listener); 
		}*/
	}
