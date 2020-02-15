package com.bn.ball.jiemian;

import com.bn.ball.R;
import com.bn.ball.RadioBallActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;


public class HelpView extends MySFView
{
	RadioBallActivity activity;//Activity的參考 
	Canvas canvas;//畫筆的參考
	Paint paint;//畫筆的參考
	SurfaceHolder holder;//鎖的參考
	Bitmap[] bg_bitmap;					//背景圖
	Bitmap pre_page;					//上一頁按鈕圖片
	Bitmap next_page;					//下一頁按鈕圖片
	Bitmap back;
	
	Bitmap pre_page_press;				//上一頁按鈕圖片
	Bitmap next_page_press;				//下一頁按鈕圖片
	Bitmap back_press;	

	int fullTime=0;//記錄從開始到目前的時間
	long startTime;//開始時間	
	
	private int  next_flag=0;//0表示不搬移,-1表示左移,1表示右移

	private float bg_bitmap_curr_x=0;//目前背景圖片的左上角點的X座標
	private float bg_bitmap_curr_y=10;//目前背景圖片的左上角點的Y座標

	int page_index=0;//目前幫助頁面的索引值
	public boolean flag_go=true;
	
	boolean back_flag=false;
	boolean pre_page_flag=false;
	boolean next_page_flag=false;
	
	boolean isHaveNextFlag=true;
	boolean isHavePreFlag=false;
	
	public HelpView(RadioBallActivity activity) {
		this.activity = activity;
		paint = new Paint();	 //建立畫筆
		bg_bitmap=new Bitmap[4];//建立幫助界面背景圖片陣列物件
		paint.setAntiAlias(true);//開啟抗鋸齒
		initBitmap();			//起始化用到的圖片資源
		startTime=System.currentTimeMillis();
		initThread();
	} 
	public void initThread()
	{
		next_flag=0;//0表示不搬移,-1表示左移,1表示右移

		bg_bitmap_curr_x=0;//目前背景圖片的左上角點的X座標
		bg_bitmap_curr_y=10;//目前背景圖片的左上角點的Y座標
		page_index=0;//目前幫助頁面的索引值
		flag_go=true;
		
		back_flag=false;
		pre_page_flag=false;
		next_page_flag=false;
		
		isHaveNextFlag=true;
		isHavePreFlag=false;		
		
		new Thread()//建立一個執行緒呼叫doDraw方法
		{
			@Override
			public void run()
			{
				while(flag_go)
				{
					//判斷是左移還是右移
					if(next_flag==-1)//左移
					{
							bg_bitmap_curr_x=-Constant.SCREEN_WIDTH;
							next_flag=0;
							page_index++;
							bg_bitmap_curr_x=0;
							if(page_index==bg_bitmap.length-1)
							{
								isHaveNextFlag=false;
							}
					}
					if(next_flag==1)//右移
					{
							bg_bitmap_curr_x=Constant.SCREEN_WIDTH;
							page_index--;
							bg_bitmap_curr_x=0;
							if(page_index==0)
							{
								isHavePreFlag=false;
							}
							next_flag=0;
					}
					try
					{  
						Thread.sleep(10);//執行緒休眠100毫秒
					}
					catch (InterruptedException e)  
					{
					e.printStackTrace();
					}
				}
			}  
		}.start();
	}
	
	//重新定義onDraw方法
	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.save();
		canvas.translate(Constant.sx, Constant.sy);
		canvas.scale(Constant.ratio, Constant.ratio);
		canvas.drawColor(Color.argb(255, 0, 0, 0));//清除螢幕為黑色
		canvas.drawBitmap(bg_bitmap[page_index],bg_bitmap_curr_x,bg_bitmap_curr_y, null);//畫目前背景
		
		
				
		if(next_flag==-1)
		{
			canvas.drawBitmap(bg_bitmap[page_index+1],0,60, null);//畫下一幅背景
		}
		if(next_flag==1)
		{
			canvas.drawBitmap(bg_bitmap[page_index-1],0,60, null);//畫下一幅背景
		}
		if(isHaveNextFlag==false)
		{
			canvas.drawBitmap(back, 810, 600, paint);//繪制back按鈕
		}
		if(page_index>0)//目前的頁面索引大於0
		{
			canvas.drawBitmap(pre_page,44, 600, paint);//繪制上一頁按鈕
		}
		if(!isHavePreFlag)
		{
			canvas.drawBitmap(back,44, 600, null);//繪制back按鈕
		}
		if(page_index<bg_bitmap.length-1)//目前頁面索引值小於幫助圖片陣列-1
		{
			canvas.drawBitmap(next_page, 810, 600, paint);//繪制下一頁按鈕
		}
	}
	//重新定義觸摸事件方法
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		int x=(int)e.getX();//取得觸控點的XY座標
		int y=(int)e.getY();
		switch(e.getAction())
		{
		case MotionEvent.ACTION_DOWN://抬起事件
			if(next_flag==0&&x>Constant.BZ_LB_Left&&x<Constant.BZ_LB_Right&&y>Constant.BZ_LB_Top&&y<Constant.BZ_LB_Buttom)
			{
				if(!isHavePreFlag)
				{
					back_flag=true;
				}
				else
				{
					pre_page_flag=true;
				}
			}
			else if(next_flag==0&&x>Constant.BZ_RB_Left&&x<Constant.BZ_RB_Right&&y>Constant.BZ_RB_Top&&y<Constant.BZ_RB_Buttom)
			{
				if(!isHaveNextFlag)
				{
					back_flag=true;
				}
				else
				{
					next_page_flag=true;
				}
			}
			else if(page_index==0&&x>Constant.BZ_RB_Left&x<Constant.BZ_RB_Right&&y>Constant.BZ_RB_Top&&y<Constant.BZ_RB_Buttom)
			{//傳回按鈕
    			back_flag=true;
			}else if(page_index==bg_bitmap.length-1&&x>Constant.BZ_LB_Left&&x<Constant.BZ_LB_Right&&y>Constant.BZ_LB_Top&&y<Constant.BZ_LB_Buttom)
			{//傳回按鈕
    			back_flag=true;
			}
			break;
		case MotionEvent.ACTION_UP://抬起事件 
			pre_page_flag=false;
			next_page_flag=false;
			back_flag=false;
			if(next_flag==0&&x>Constant.BZ_LB_Left&&x<Constant.BZ_LB_Right&&y>Constant.BZ_LB_Top&&y<Constant.BZ_LB_Buttom)
			{
				if(!isHavePreFlag)
				{
					//傳回到主選單
					flag_go=false;
					activity.hd.sendEmptyMessage(1);
				}
				else
				{
					isHavePreFlag=true;
					isHaveNextFlag=true;
					//右移   
					next_flag=1;
				}
			}
			else if(next_flag==0&&x>Constant.BZ_RB_Left&&x<Constant.BZ_RB_Right&&y>Constant.BZ_RB_Top&&y<Constant.BZ_RB_Buttom)
			{
				if(!isHaveNextFlag)
				{
					//傳回主選單
					flag_go=false;
					activity.hd.sendEmptyMessage(1);
				}
				else
				{
					isHaveNextFlag=true;
					isHavePreFlag=true;
					//左移
					next_flag=-1;
				}
				
			}
			break;
		}
		return true;
	}
	//起始化圖片的方法
	public void initBitmap()
	{
		bg_bitmap[0] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.jiafen);//幫助界面背景圖片
		bg_bitmap[1] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.jianfen);                
		bg_bitmap[2] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.shengli);
		bg_bitmap[3] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.shibai);
		 pre_page=BitmapFactory.decodeResource(activity.getResources(), R.drawable.pre);
		 next_page=BitmapFactory.decodeResource(activity.getResources(), R.drawable.next);
		 back=BitmapFactory.decodeResource(activity.getResources(), R.drawable.back);                   
	}

}
