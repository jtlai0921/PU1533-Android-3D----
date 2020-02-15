package com.bn.ball.jiemian;

import com.bn.ball.R;
import com.bn.ball.RadioBallActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
public class SoundView extends MySFView{
	RadioBallActivity actitity;
	  	Bitmap bgmusic;			//背景音樂
	    Bitmap gsound;			//游戲音效
	    Bitmap on;				//開
	    Bitmap off;				//關
	    Bitmap back;			//傳回
	public SoundView(RadioBallActivity activity){
		this.actitity=activity;
		initBitmap();
	}
	//將圖片載入
		public void initBitmap()
		{
			bgmusic=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.bgmusic);                                           
			gsound=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.gsound);                             
			on=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.on);
			off=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.off);
			back=BitmapFactory.decodeResource(actitity.getResources(), R.drawable.back);
		}
		@Override
		public void onDraw(Canvas canvas) 
		{
			super.onDraw(canvas);
			canvas.save();
			canvas.translate(Constant.sx, Constant.sy);
			canvas.scale(Constant.ratio, Constant.ratio);
			canvas.drawColor(Color.argb(255, 0, 0, 0));//清除螢幕為黑色
			canvas.drawBitmap(bgmusic,260,180, null);
			canvas.drawBitmap(gsound,260,400, null);
			
			if(Constant.button1)
			{
				canvas.drawBitmap(on,780,180, null);
			}else
			{
				canvas.drawBitmap(off,780,180, null);
			}
			if(Constant.button2)
			{
				canvas.drawBitmap(on, 780,400, null);
			}else
			{
				canvas.drawBitmap(off,780,400, null);
			}
			canvas.drawBitmap(back,50,580, null);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent e)
		{
			int x=(int)e.getX();
			int y=(int)e.getY();
			switch(e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
			{
				if(x>Constant.Sound_bgMusic_Left&&x<Constant.Sound_bgMusic_Right&&y>Constant.Sound_bgMusic_Top&&y<Constant.Sound_bgMusic_Buttom)
				{
	        		if(Constant.i==0)
	        		{
	        			Constant.button1=false;
	        		}
	        		else
	        		{
	        			Constant.button1=true;
	        		}
				}else if(x>Constant.Sound_gSound_Left&&x<Constant.Sound_gSound_Right&&y>Constant.Sound_gSound_Top&&y<Constant.Sound_gSound_Buttom)
				{
	        		if(Constant.j==0)
	        		{
	        			Constant.button2=false;
	        			Constant.gameMusic_flag=true;
	        			
	        		}
	        		else
	        		{
	        			Constant.button2=true;
	        			Constant.gameMusic_flag=false;
	        		}
				}else if(x>Constant.Sound_Back_Left&&x<Constant.Sound_Back_Right&&y>Constant.Sound_Back_Top&&y<Constant.Sound_Back_Buttom)
				{
					actitity.hd.sendEmptyMessage(1);
				}
				break;
			}
			case MotionEvent.ACTION_UP:
			{
				if(x>Constant.Sound_bgMusic_Left&&x<Constant.Sound_bgMusic_Right&&y>Constant.Sound_bgMusic_Top&&y<Constant.Sound_bgMusic_Buttom)
				{
					Constant.i=(Constant.i+1)%2;
	        		if(actitity.beijingyinyue!=null)
	        	    {
	        			actitity.beijingyinyue.stop();
	        			actitity.beijingyinyue=null;
	        	    }else
	        	    {
	        	    	actitity.playBeiJingYinYue();
	        	    }
				}else if(x>Constant.Sound_gSound_Left&&x<Constant.Sound_gSound_Right&&y>Constant.Sound_gSound_Top&&y<Constant.Sound_gSound_Buttom)
				{
					Constant.j=(Constant.j+1)%2;
					SharedPreferences.Editor editor=actitity.sp.edit();
	        		editor.putBoolean("youxiyinxiao",!Constant.gameMusic_flag);
	        		editor.commit();
				}
				break;
			}
			}
			return true;
		}
}
