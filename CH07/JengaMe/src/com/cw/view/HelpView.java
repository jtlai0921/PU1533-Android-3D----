package com.cw.view;


import com.cw.game.Constant;
import com.cw.game.JengaMeActivity;
import com.cw.game.R;
import com.cw.game.R.drawable;
import com.cw.util.SYSUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class HelpView extends SurfaceView implements SurfaceHolder.Callback
{
JengaMeActivity activity;
	
	Bitmap beijingtupian;
	Bitmap backpic[]=new Bitmap[5];
	
	Bitmap okbutton;
	Bitmap okbuttondown;
	Bitmap backbutton;
	Bitmap backbuttondown;
	
	
	Bitmap nextbutton;
	Bitmap nextbuttondown;
	
	int currindex=0;
	static boolean  backup=true;
	static boolean  nextup=true;
	static boolean yesflaga=false;
	static boolean yesflagb=false;
	 SurfaceHolder holder;
	 static  float scalX;
	 static  float scalY;
	 
	 public static boolean candraw=true;;
     public HelpView(JengaMeActivity activity)
     {
         super(activity);
         holder = this.getHolder();//取得holder  
         holder.addCallback(this);
		 this.activity=activity;
		 this.getHolder().addCallback(this);//設定生命周期回調接口的實現者	
		 scalX=JengaMeActivity.screenWidth;
		 scalY=JengaMeActivity.screenHeight;
		 initBitmap();//起始化點陣圖
		 candraw=true;
     }

     public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)
     {
     }
     public void surfaceCreated(SurfaceHolder holder)
     {
         new Thread(new MyThread()).start();
     }
     public void surfaceDestroyed(SurfaceHolder holder)
     {
     }
 	public boolean onTouchEvent(MotionEvent e)
 	{
 		int currentNUm=e.getAction();
 		float x=e.getX();
 		float y=e.getY();
 		switch(currentNUm)
 		{
 		case MotionEvent.ACTION_DOWN:
				if(x>30*JengaMeActivity.scaleratiox&&x<(30)*JengaMeActivity.scaleratiox+nextbutton.getWidth()&&y>860*JengaMeActivity.scaleratioY&&y<860*JengaMeActivity.scaleratioY+nextbutton.getHeight())
				{
					nextup=false;
				}
				if(x>(540-30)*JengaMeActivity.scaleratiox-backbutton.getWidth()&&x<(540-30)*JengaMeActivity.scaleratiox&&y>860*JengaMeActivity.scaleratioY&&y<860*JengaMeActivity.scaleratioY+backbutton.getHeight())
				{
					backup=false;
				}
				break;
			case MotionEvent.ACTION_UP:	
	    	    backup=true;
	    	    nextup=true;
	    	    if(x>30*JengaMeActivity.scaleratiox&&x<(30)*JengaMeActivity.scaleratiox+nextbutton.getWidth()&&y>860*JengaMeActivity.scaleratioY&&y<860*JengaMeActivity.scaleratioY+nextbutton.getHeight())
				{
	    			if(currindex<4)
	    			{
	    				currindex++;
	    				beijingtupian=backpic[currindex];
	    			}
				}
			    if(x>(540-30)*JengaMeActivity.scaleratiox-backbutton.getWidth()&&x<(540-30)*JengaMeActivity.scaleratiox&&y>860*JengaMeActivity.scaleratioY&&y<860*JengaMeActivity.scaleratioY+backbutton.getHeight())
				{
			    	if(currindex==0)
			    	{
			    		candraw=false;
						MenuView.cdraw=true;
						activity.hd.sendEmptyMessage(1);
			    	}
			    	else
			    	{
			    		if(currindex>0)
			    		{
			    			currindex--;
			    			beijingtupian=backpic[currindex];
			    		}
			    	}
				}
			break;
 		}
 		return true;
 	}
 	 Bitmap dstbmp;
 	Bitmap recobmp;
 	public void initBitmap()
 	{
 		backpic[0]=SYSUtil.scaleToFit(BitmapFactory.decodeByteArray(Constant.background3,0, Constant.background3.length), JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);	
 		backpic[1]=SYSUtil.scaleToFit(BitmapFactory.decodeByteArray(Constant.background4,0, Constant.background4.length), JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);
 		backpic[2]=SYSUtil.scaleToFit(BitmapFactory.decodeByteArray(Constant.background5,0, Constant.background5.length), JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);	
 		backpic[3]=SYSUtil.scaleToFit(BitmapFactory.decodeByteArray(Constant.background6,0, Constant.background6.length), JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);	
 		backpic[4]=SYSUtil.scaleToFit(BitmapFactory.decodeByteArray(Constant.background7,0, Constant.background7.length), JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);
 		beijingtupian=backpic[0]; 
 		nextbutton=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.nextpage), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);;
 		nextbuttondown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.nextpagedown), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);;
 		backbutton=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.back), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);;
 		backbuttondown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.backdown), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);;
 	}
     class MyThread implements Runnable
     {
         public void run()
         {
             int width = beijingtupian.getWidth();//取得資源點陣圖的寬
             int height = beijingtupian.getHeight();//取得資源點陣圖的高
             float w = scalX/beijingtupian.getWidth();
             float h = scalY/beijingtupian.getHeight();
             Rect rect = new Rect (0,0,width,height); 
             RectF rectf = new RectF(0,0,width*w,height*h); 
         while(candraw)
           {
        		 Canvas canvas = holder.lockCanvas(null);//取得畫布  
                 Paint mPaint = new Paint();
                 canvas.save();
                 canvas.drawBitmap(beijingtupian,rect,rectf,null); 
                 canvas.restore();
                 if(nextup)
                 {
                	 canvas.drawBitmap(nextbutton,30*JengaMeActivity.scaleratiox,860*JengaMeActivity.scaleratioY, mPaint); 
                 }else
                 {
                	 canvas.drawBitmap(nextbuttondown,30*JengaMeActivity.scaleratiox,860*JengaMeActivity.scaleratioY, mPaint); 
                 }
                 
                 if(backup)
                 {
                	 canvas.drawBitmap(backbutton,(540-30)*JengaMeActivity.scaleratiox-backbutton.getWidth(),860*JengaMeActivity.scaleratioY, mPaint); 
                 }else
                 {
                	 canvas.drawBitmap(backbuttondown,(540-30)*JengaMeActivity.scaleratiox-backbutton.getWidth(),860*JengaMeActivity.scaleratioY, mPaint); 
                 }
                 try
                 {
                	holder.unlockCanvasAndPost(canvas);//解鎖畫布，傳送畫好的圖形  
                 }catch(Exception e)
                 {
                 }
          }
         nextbutton.recycle();
         nextbuttondown.recycle();
         backbutton.recycle();
         backbuttondown.recycle();
         backpic[0].recycle();
         backpic[1].recycle();
         backpic[2].recycle();
         backpic[3].recycle();
         backpic[4].recycle();
       }
     }
}
