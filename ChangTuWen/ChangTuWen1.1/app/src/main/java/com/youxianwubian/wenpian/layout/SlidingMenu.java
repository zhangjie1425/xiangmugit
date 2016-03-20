package com.youxianwubian.wenpian.layout;

	import android.content.*;
	import android.content.res.*;
	import android.util.*;
	import android.view.*;
	import android.widget.*;
	

public class SlidingMenu extends ScrollView
	{
		/**
		 * 屏幕宽度
		 */
		private int mScreenWidth;
		/**
		 * dp
		 */
		private int mMenuRightPadding;
		/**
		 * 菜单的宽度
		 */
		private int mMenuWidth;
		private int mHalfMenuWidth;

		private boolean isOpen;

		private boolean once;

		private ViewGroup mMenu;
		private ViewGroup mContent;

		private float lastY;

	private float lastX;

	private float xLast;

	private float yLast;

	private float yDistance;

	private float xDistance;

		public SlidingMenu(Context context, AttributeSet attrs)
		{
			this(context, attrs, 0);

		}

		public SlidingMenu(Context context, AttributeSet attrs, int defStyle)
		{
			super(context, attrs, defStyle);
		/*	mScreenWidth = ScreenUtils.getScreenWidth(context);

			TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
																	 R.styleable.SlidingMenu, defStyle, 0);
			int n = a.getIndexCount();
			for (int i = 0; i < n; i++)
			{
				int attr = a.getIndex(i);
				switch (attr)
				{
					case R.styleable.SlidingMenu_rightPadding:
						// 默认50
						mMenuRightPadding = a.getDimensionPixelSize(attr,
																	(int) TypedValue.applyDimension(
																		TypedValue.COMPLEX_UNIT_DIP, 50f,
																		getResources().getDisplayMetrics()));// 默认为10DP
						break;
				}
			}
			a.recycle();*/
		}

		public SlidingMenu(Context context)
		{
			this(context, null, 0);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
		{
			/**
			 * 显示的设置一个宽度
			 */
		/*	if (!once)
			{
				LinearLayout wrapper = (LinearLayout) getChildAt(0);
				mMenu = (ViewGroup) wrapper.getChildAt(0);
				mContent = (ViewGroup) wrapper.getChildAt(1);

				mMenuWidth = mScreenWidth - mMenuRightPadding;
				mHalfMenuWidth = mMenuWidth / 2;
				mMenu.getLayoutParams().width = mMenuWidth;
				mContent.getLayoutParams().width = mScreenWidth;

			}*/
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		}

		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b)
		{
			super.onLayout(changed, l, t, r, b);
			/*if (changed)
			{
				// 将菜单隐藏
				this.scrollTo(mMenuWidth, 0);
				once = true;
			}*/
		}
	public boolean onGenericMotionEvent(android.view.MotionEvent event) {
		
		return false;
	}
		 @Override
		 public boolean onInterceptTouchEvent(MotionEvent ev) {
			 switch (ev.getAction()) {
				 case MotionEvent.ACTION_DOWN:
					 xDistance = yDistance = 0f;
					 xLast = ev.getX();
					 yLast = ev.getY();
					 break;
				 case MotionEvent.ACTION_MOVE:
					 final float curX = ev.getX();
					 final float curY = ev.getY();

					 xDistance += Math.abs(curX - xLast);
					 yDistance += Math.abs(curY - yLast);
					 xLast = curX;
					 yLast = curY;

					 if (xDistance < yDistance) {
						 return false;
					 }
			 }

			 return super.onInterceptTouchEvent(ev);
			
		 }
	 
		 /*
		 public boolean onInterceptTouchEvent(MotionEvent ev) {
		 return false;
		 }
/*	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
		}*/
	/*	@Override
		public boolean onTouchEvent(MotionEvent ev)
		{
		/*	int action = ev.getAction();
			boolean bo=true;
			boolean issd=false;
			switch (action)
			{
				case MotionEvent.ACTION_DOWN:

					lastX = ev.getX();
					lastY=ev.getY();
					//this.scrollTo((int)ev.getX(), 0);
					//issd=false;
					break;
				case MotionEvent.ACTION_MOVE:
					//this.scrollTo((int)ev.getX(), 0);
					if(!issd)
					{
						float distanceX = Math.abs( ev.getX() - lastX);
						float distanceY = Math.abs(ev.getY()-lastY);

						if(distanceX>distanceY &&distanceX>5){
							bo = true;
							issd=true;
						}else{
							if(distanceX<distanceY&&distanceY>5)
								bo = false;
							issd=true;
						}
					}
					break;

					// Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
				case MotionEvent.ACTION_UP:
					int scrollX = getScrollX();

					if(isOpen)
					{
						if (scrollX > mHalfMenuWidth/2)
						{

							this.smoothScrollTo(mMenuWidth, 0);
							isOpen = false;
						} 
						else
						{
							this.smoothScrollTo(0, 0);
							isOpen = true;
						}

						/*	if (scrollX > mHalfMenuWidth)
						 {
						 this.smoothScrollTo(mMenuWidth, 0);
						 isOpen = false;
						 } else
						 {
						 this.smoothScrollTo(0, 0);
						 isOpen = true;
						 }*/
			/*		}
					else
					{
						if (scrollX >mHalfMenuWidth*3/2)
						{

							this.smoothScrollTo(mMenuWidth, 0);
							isOpen = false;
						} 
						else
						{
							this.smoothScrollTo(0, 0);
							isOpen = true;
						}
					}
					issd=false;
					return true;
			}
			//
			if(bo)
			{
				return super.onTouchEvent(ev);
			}
			else
			{
				return true;
			}*/
			/*return false;
		}

		/**
		 * 打开菜单
		 */
		public void openMenu()
		{
			if (isOpen)
				return;
			this.smoothScrollTo(0, 0);
			isOpen = true;
		}

		/**
		 * 关闭菜单
		 */
		public void closeMenu()
		{
			if (isOpen)
			{
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			}
		}

		/**
		 * 切换菜单状态
		 */
		public void toggle()
		{
			if (isOpen)
			{
				closeMenu();
			} else
			{
				openMenu();
			}
		}
		public void toggleb()
		{
			if (isOpen)
			{
				closeMenu();
			}
		}
		/*@Override
		protected void onScrollChanged(int l, int t, int oldl, int oldt)
		{
			super.onScrollChanged(l, t, oldl, oldt);
			float scale = l * 1.0f / mMenuWidth;
			float leftScale = 1 - 0.3f * scale;
			float rightScale = 0.8f + scale * 0.2f;

			ViewHelper.setScaleX(mMenu, leftScale);
			ViewHelper.setScaleY(mMenu, leftScale);
			ViewHelper.setAlpha(mMenu, 0.4f + 0.6f * (1 - scale));
			ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.7f);

			ViewHelper.setPivotX(mContent, 0);
			ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
			ViewHelper.setScaleX(mContent, rightScale);
			ViewHelper.setScaleY(mContent, rightScale);

			//	ViewHelper.setAlpha(mContent, scale*0.6f+0.4f);

		}*/

		/*
		 @Override)
		 public boolean onInterceptTouchEvent(MotionEvent ev)
		 {
		 return super.onInterceptTouchEvent(ev)
		 && mGestureDetector.onTouchEvent(ev);
		 }*/
		//设置侧滑动作条件

		/*	public boolean onInterceptTouchEvent(MotionEvent ev) {

		 boolean issd=false;
		 boolean result=false;
		 switch (ev.getAction()) {
		 case MotionEvent.ACTION_DOWN:

		 lastX = ev.getX();
		 lastY = ev.getY();
		 issd=false;
		 break;
		 case MotionEvent.ACTION_MOVE:
		 if(!issd)
		 {
		 float distanceX = Math.abs( ev.getX() - lastX);
		 float distanceY = Math.abs(ev.getY()-lastY);

		 if(distanceX>distanceY &&distanceX>10){
		 result = true;
		 issd=true;
		 }else{
		 if(distanceX<distanceY&&distanceY>10)
		 result = false;
		 issd=true;
		 }
		 }
		 break;
		 case MotionEvent.ACTION_UP:
		 issd=false;
		 //result=false;
		 break;

		 default:
		 break;
		 }

		 return result;
		 }*/
	}


