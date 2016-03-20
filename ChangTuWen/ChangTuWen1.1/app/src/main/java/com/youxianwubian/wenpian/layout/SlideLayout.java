package com.youxianwubian.wenpian.layout;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.youxianwubian.wenpian.whdui.*;
import com.youxianwubian.wenpian.*;

import com.youxianwubian.wenpian.R;

public class SlideLayout extends RelativeLayout {
	private static String TAG = "SlideMenuLayout";
	
	private Context mContext;
	private Scroller mScroller;    //Android 提供的滑动辅助类
	private int mTouchSlop = 0 ;    //在被判定为滚动之前用户手指可以移动的最大值
	private VelocityTracker mVelocityTracker;    //用于计算手指滑动的速度
	public static final int SNAP_VELOCITY = 100;    //滚动显示和隐藏左侧布局时，手指滑动需要达到的速度：每秒200个像素点
	private int mMaxScrollX = 0;

	private float yDistance;

	private float xDistance;

	private float yLast;

	private float xLast;

	private LinearLayout sli;

	private SwipeMenuListView listView;

	public void setMaxScrollX(int maxScrollX) {
		this.mMaxScrollX = maxScrollX;
	}

	private float mDownX;    //一次按下抬起的动作中，按下时的X坐标，用于和抬起时的X比较，判断移动距离。少于mTouchSlop则判定为原地点击
	private float mLastX;    //记录滑动过程中的X坐标
	
	private boolean isMenuOpen = false;    //菜单界面是否被打开，只有完全打开才为true
	public boolean isMenuOpen() {
		return isMenuOpen;
	}
	private boolean iszsdk=false;

	private View mContent;
	
	public SlideLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init() {
		Log.v(TAG, "init start");
		mScroller = new Scroller(mContext);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		
		//mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(changed){
			mContent = getChildAt(0);
			sli =(LinearLayout) mContent. findViewById(R.id.sli);
		}
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(isMenuOpen)
		{
			return true;//true
		}
         else
		 switch (ev.getAction()) {
		 case MotionEvent.ACTION_DOWN:
		 xDistance = yDistance = 0f;
		 xLast = ev.getX();
		 yLast = ev.getY();
				 mDownX = xLast;
				 mLastX = xLast;
		 break;
		 case MotionEvent.ACTION_MOVE:
		 final float curX = ev.getX();
		 final float curY = ev.getY();

		 xDistance += Math.abs(curX - xLast);
		 yDistance += Math.abs(curY - yLast);
				 int deltaXb=(int)(curX - xLast);
				 if(!(getScrollX()==0&&deltaXb>0))
				 {
					 return false;
				 }
		 xLast = curX;
		 yLast = curY;
		 
				 

		 if (xDistance > yDistance) {
			 if(listView.getisopen())
			 {
				 //listView.closecd();
				 return false;
			 }
			 else
			 {
		 return true;
		 }
		 }
		 }
		 
		return super.onInterceptTouchEvent(ev);
		
	}
	
	public void setlist(SwipeMenuListView i)
	{
		listView=i;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		createVelocityTracker(event);
		int curScrollX = getScrollX();
		// 检查触摸点是否在滑动布局(内容content)中，如果不是则返回false，即本View不处理该事件
		if(iszsdk)
		{
		if (mContent != null) {
			Rect rect = new Rect();
			mContent.getHitRect(rect);
			if (!rect.contains((int)event.getX() + curScrollX, (int)event.getY())) {
				return false;
			}
		}
		}

		float x = event.getX();    //取得本次event的X坐标
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = x;
			mLastX = x;
//			Log.v(TAG, "(Down)mDownX: " + x);
//			Log.v(TAG, "(Down)mLastX: " + x);
			//Toast.makeText(mContext, "...", 4000).show();
			break;
		case MotionEvent.ACTION_MOVE:
//			Log.v(TAG, "(Move)mLastX: " + x);
			int deltaX = (int)(mLastX - x);
				
			if((curScrollX + deltaX) < -mMaxScrollX) {
				deltaX = -mMaxScrollX - curScrollX;
			}
			if((curScrollX + deltaX) > 0){
				deltaX = -curScrollX;
			}
			
			if (deltaX != 0) {
				scrollBy(deltaX, 0);
			}
			
			mLastX = x;
			
//			Log.v(TAG, "(Move)x: " + x);
			break;
		case MotionEvent.ACTION_UP:
//			Log.v(TAG, "(Up)x: " + x);
//			Log.v(TAG, "(Up)mDownX: " + mDownX);
//			Log.v(TAG, "(Up)curScrollX: " + curScrollX);
			int velocityX = getScrollVelocity();
			int offsetX = (int) (x - mDownX);
			
			//成立表明移动距离已经达到被判断为滑动的最低标准
			//不成立表明不被判断为滑动，则认为是单一的点击，则关闭menu
			if(Math.abs(offsetX) >= mTouchSlop) {
				
				//成立表明手指移动速度达标，则进行自动滑动；
				//不成立表明速度不达标，但仍然需要判断当前SlideLayout的位置
				//如果已经超过一半，则继续自动完成剩下的滑动，如果没有超过一半，则反向滑动
				if(Math.abs(velocityX) >= SNAP_VELOCITY) {
					if(velocityX > 0){
						openMenu();
					} else if(velocityX < 0) {
						closeMenu();
					}
				} else {
					if (curScrollX >= -mMaxScrollX / 2) {
						closeMenu();
					} else {
						openMenu();
					}
				}
			} else {
				if (mContent != null) {
					Rect rect = new Rect();
					mContent.getHitRect(rect);
					if (rect.contains((int)event.getX() + curScrollX, (int)event.getY())) {
						closeMenu();
					}}
				
			}

			recycleVelocityTracker();
			break;
		}
		return true;
	}
	
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}
	
	//获取手指在View上的滑动速度,以每秒钟移动了多少像素值为单位
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		return (int) mVelocityTracker.getXVelocity();
	}
	
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}
	
	//private int curScrollXB;
	//private int curScrollX ;
	//打开Menu布局
	public void openMenu() {
		int curScrollX = getScrollX();
	/*	if(task==null)
		{
			task=new MyTimerTask();
		}
		curScrollX=mMaxScrollX/10;
		timer.schedule(task,0, 10);
		*/
		
		scrollToDestination(-mMaxScrollX - curScrollX);
		isMenuOpen = true;
		iszsdk=true;
	}
/*
	class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            // TODO Auto-generated method stub
			Message msg = new Message();
			msg.what = 0;

			//发送消息
			myHandler.sendMessage(msg);
		}
    }
	private Handler //获取数据，，并显示！
	myHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{

			//如果该消息是本程序所发送的
			if (msg.what == 0)
			{
				curScrollXB=curScrollXB-1;//curScrollXB+mMaxScrollX/100;
				if(getScrollX()>-mMaxScrollX+curScrollX)
				{
					scrollToDestination(-curScrollX);
				}
				else
				{
					scrollToDestination(-mMaxScrollX - getScrollX());
					timer.cancel(); //退出计时器
					timer=null;
					timer = new Timer(true);
					task=null;
					curScrollXB=0;
					isMenuOpen = true;
				}
			}
			
		}
	};
	*/
	/*
	 if(task==null)
	 {
	 task=new MyTimerTask();
	 }

	 timer.schedule(task,0, mtime); //延时1000ms后执行，1000ms执行一次 
	 
	 timer.cancel(); //退出计时器
	 timer=null;
	 timer = new Timer(true);
	 task=null;
	*/
	
	//关闭Menu布局
	public void closeMenu() {
		int curScrollX = getScrollX();
		scrollToDestination(-curScrollX);
		isMenuOpen = false;
		iszsdk=false;
	}
	
	private void scrollToDestination(int x) {
		if (x == 0)
			return;

		mScroller.startScroll(getScrollX(), 0, x, 0, Math.abs(x)*3/2);
		invalidate();
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
		float scale =(float)-l* 1.0f / mMaxScrollX;
		if(-l!=mMaxScrollX)
		{
			iszsdk=false;
		}
		else
		{
			iszsdk=true;
		}
		//ViewHelper.setAlpha(mContent,scale);
		/*float leftScale = 1 - 0.3f * scale;
		float rightScale = 0.8f + scale * 0.2f;

		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, 0.4f + 0.6f * (1 - scale));
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.7f);

		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);*/

		//	ViewHelper.setAlpha(mContent, scale*0.6f+0.4f);
		tmd(sli,1-scale*0.6f);
		

	}

	//透明度
	private void tmd(View re,float s)
	{
		re.setAlpha(s);
		//方式一通过代码的方式定义透明度动画
		/*Animation alphaAnimation=new AlphaAnimation((float)s, s);
		alphaAnimation.setDuration(0);//设置动画持续时间为3秒
		alphaAnimation.setFillAfter(true);//设置动画结束后保持当前的位置（即不返回到动画开始前的位置）
		re.startAnimation(alphaAnimation);*/
	}
}

/*
package com.noter.layout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class SlideLayout extends RelativeLayout {
	private static String TAG = "SlideMenuLayout";
	
	private Context mContext;
	private Scroller mScroller;    //Android 提供的滑动辅助类
	private int mTouchSlop = 0 ;    //在被判定为滚动之前用户手指可以移动的最大值
	private VelocityTracker mVelocityTracker;    //用于计算手指滑动的速度
	public static final int SNAP_VELOCITY = 200;    //滚动显示和隐藏左侧布局时，手指滑动需要达到的速度：每秒200个像素点
	private int mMaxScrollX = 0;    //最大滚动距离，等于menu的宽度
	public void setMaxScrollX(int maxScrollX) {
		this.mMaxScrollX = maxScrollX;
	}

	private float mDownX;    //一次按下抬起的动作中，按下时的X坐标，用于和抬起时的X比较，判断移动距离。少于mTouchSlop则判定为原地点击
	private float mLastX;    //记录滑动过程中的X坐标
	
	private boolean isMenuOpen = false;    //菜单界面是否被打开，只有完全打开才为true
	public boolean isMenuOpen() {
		return isMenuOpen;
	}

	private View mContent;
	
	public SlideLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init() {
		Log.v(TAG, "init start");
		mScroller = new Scroller(mContext);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(changed){
			mContent = getChildAt(0);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		createVelocityTracker(event);
		int curScrollX = getScrollX();
		// 检查触摸点是否在滑动布局(内容content)中，如果不是则返回false，即本View不处理该事件
		if (mContent != null) {
			Rect rect = new Rect();
			mContent.getHitRect(rect);
			if (!rect.contains((int)event.getX() + curScrollX, (int)event.getY())) {
				return false;
			}
		}

		float x = event.getX();    //取得本次event的X坐标
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = x;
			mLastX = x;
//			Log.v(TAG, "(Down)mDownX: " + x);
//			Log.v(TAG, "(Down)mLastX: " + x);
			break;
		case MotionEvent.ACTION_MOVE:
//			Log.v(TAG, "(Move)mLastX: " + x);
			int deltaX = (int)(mLastX - x);
			if((curScrollX + deltaX) < -mMaxScrollX) {
				deltaX = -mMaxScrollX - curScrollX;
			}
			if((curScrollX + deltaX) > 0){
				deltaX = -curScrollX;
			}
			
			if (deltaX != 0) {
				scrollBy(deltaX, 0);
			}
			mLastX = x;
//			Log.v(TAG, "(Move)x: " + x);
			break;
		case MotionEvent.ACTION_UP:
//			Log.v(TAG, "(Up)x: " + x);
//			Log.v(TAG, "(Up)mDownX: " + mDownX);
//			Log.v(TAG, "(Up)curScrollX: " + curScrollX);
			int velocityX = getScrollVelocity();
			int offsetX = (int) (x - mDownX);
			
			//成立表明移动距离已经达到被判断为滑动的最低标准
			//不成立表明不被判断为滑动，则认为是单一的点击，则关闭menu
			if(Math.abs(offsetX) >= mTouchSlop) {
				
				//成立表明手指移动速度达标，则进行自动滑动；
				//不成立表明速度不达标，但仍然需要判断当前SlideLayout的位置
				//如果已经超过一半，则继续自动完成剩下的滑动，如果没有超过一半，则反向滑动
				if(Math.abs(velocityX) >= SNAP_VELOCITY) {
					if(velocityX > 0){
						openMenu();
					} else if(velocityX < 0) {
						closeMenu();
					}
				} else {
					if (curScrollX >= -mMaxScrollX / 2) {
						closeMenu();
					} else {
						openMenu();
					}
				}
			} else {
				closeMenu();
			}

			recycleVelocityTracker();
			break;
		}
		return true;
	}
	
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}
	
	//获取手指在View上的滑动速度,以每秒钟移动了多少像素值为单位
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		return (int) mVelocityTracker.getXVelocity();
	}
	
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}
	
	//打开Menu布局
	public void openMenu() {
		int curScrollX = getScrollX();
		scrollToDestination(-mMaxScrollX - curScrollX);
		isMenuOpen = true;
	}
	
	//关闭Menu布局
	public void closeMenu() {
		int curScrollX = getScrollX();
		scrollToDestination(-curScrollX);
		isMenuOpen = false;
	}
	
	private void scrollToDestination(int x) {
		if (x == 0)
			return;

		mScroller.startScroll(getScrollX(), 0, x, 0, Math.abs(x));
		invalidate();
	}
}
*/
