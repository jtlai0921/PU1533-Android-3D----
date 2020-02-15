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
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class MenuView extends SurfaceView implements SurfaceHolder.Callback
{
	JengaMeActivity activity;
	Bitmap beijingtupian;
	Bitmap quitgame;
	Bitmap quitgamedown;
	Bitmap playgame;
	Bitmap playdown;
	Bitmap record;
	Bitmap recorddown;
	Bitmap options;
	Bitmap optionsdown;
	Bitmap help;
	Bitmap helpdown;
	static boolean  palyup=true;;
	static boolean recordup=true;
	static boolean helpup=true;
	static boolean quitup=true;
	static boolean optioup=true;
	
	 SurfaceHolder holder;
	 public static  float scalX;
	 public static  float scalY;
	 
	 public static boolean cdraw=true;
     public MenuView(JengaMeActivity activity)
     {
         super(activity);
         holder = this.getHolder();//取得holder  
         holder.addCallback(this);
		this.activity=activity;
		this.getHolder().addCallback(this);//設定生命周期回調接口的實現者	
		scalX=JengaMeActivity.screenWidth;
		scalY=JengaMeActivity.screenHeight;
		initBitmap();//起始化點陣圖
     }

 	//設定監聽
 	public boolean onTouchEvent(MotionEvent e)
 	{
 		int currentNUm=e.getAction();
 		float x=e.getX();
 		float y=e.getY();		
 		switch(currentNUm)
 		{
 			case MotionEvent.ACTION_DOWN:
 				//開始游戲
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+playgame.getWidth()&&y>160*JengaMeActivity.scaleratioY&&y<160*JengaMeActivity.scaleratioY+playgame.getHeight())
 				{
 					palyup=false;
 				}
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+playgame.getWidth()&&y>280*JengaMeActivity.scaleratioY&&y<280+playgame.getHeight()*JengaMeActivity.scaleratioY)
 	 			{
 					recordup=false;
 				}
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+quitgame.getWidth()&&y>640*JengaMeActivity.scaleratioY&&y<640*JengaMeActivity.scaleratioY+quitgame.getHeight())
 	 			{
 					quitup=false;
 				}
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+options.getWidth()&&y>520*JengaMeActivity.scaleratioY&&y<520*JengaMeActivity.scaleratioY+options.getHeight())
  				{
 					helpup=false;
 				}
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+options.getWidth()&&y>400*JengaMeActivity.scaleratioY&&y<400*JengaMeActivity.scaleratioY+options.getHeight())
 	 			{
 					optioup=false;
 				}
 				break;
 			case MotionEvent.ACTION_UP:	
 				palyup=true;
		    	quitup=true;
		    	optioup=true;
		    	recordup=true;
		    	helpup=true;
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+playgame.getWidth()&&y>160*JengaMeActivity.scaleratioY&&y<160*JengaMeActivity.scaleratioY+playgame.getHeight())
 				{
 					cdraw=false;
 					 activity.hd.sendEmptyMessage(0);
 					//Toast.makeText(activity, "游戲載入中····", Toast.LENGTH_SHORT).show();
 				}
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+playgame.getWidth()&&y>280*JengaMeActivity.scaleratioY&&y<280+playgame.getHeight()*JengaMeActivity.scaleratioY)
 				{
 					cdraw=false;
					activity.hd.sendEmptyMessage(4);
 				}
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+options.getWidth()&&y>400*JengaMeActivity.scaleratioY&&y<400*JengaMeActivity.scaleratioY+options.getHeight())
 				{
 					cdraw=false;
  			        activity.hd.sendEmptyMessage(2);
 				}
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+quitgame.getWidth()&&y>640*JengaMeActivity.scaleratioY&&y<640*JengaMeActivity.scaleratioY+quitgame.getHeight())
 				{
 					System.exit(0);
 				}
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+options.getWidth()&&y>520*JengaMeActivity.scaleratioY&&y<520*JengaMeActivity.scaleratioY+options.getHeight())
 				{
 					cdraw=false;
					activity.hd.sendEmptyMessage(5);
 				}
 			break;
 		}
 		return true;
 	}
 	Bitmap dstbmp;
 	public void initBitmap()
 	{
 		//beijingtupian=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.background0), JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);	
 		
 		beijingtupian=SYSUtil.scaleToFit(BitmapFactory.decodeByteArray(Constant.background0,0, Constant.background0.length), JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);	
 		
 		playgame=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.play),JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);	
 		playdown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.playdown),JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);	;	
 		options=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.options),JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);	
 		optionsdown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.optionsdown),JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);	
 		quitgame=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.quit),JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);	
 		quitgamedown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.quitdown),JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);;	
 		record=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.record),JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);	
 		recorddown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.recorddown),JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);
 		helpdown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.helpdown),JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);
 		help=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.help),JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);;
 	}
     //內定類別的內定類別  繪圖執行緒 也就是第二執行緒（a secondary thread can render in to the screen）
     class MyThread implements Runnable
     {
         public void run()
         {
        	 //Matrix matrix=new Matrix();
             int width = beijingtupian.getWidth();//取得資源點陣圖的寬
             int height = beijingtupian.getHeight();//取得資源點陣圖的高
             float w = scalX/beijingtupian.getWidth();
             float h = scalY/beijingtupian.getHeight();
             Rect rect = new Rect (0,0,width,height); 
             RectF rectf = new RectF(0,0,width*w,height*h); 
             
             
        	while(cdraw)
        	 {
        		 Canvas canvas = holder.lockCanvas(null);//取得畫布  
        		 canvas.drawBitmap(beijingtupian,rect,rectf,null); 
                 Paint mPaint = new Paint();
                	 canvas.save();
                   //  canvas.drawBitmap(dstbmp,0,0, mPaint); //在螢幕上畫出點陣圖
                     canvas.restore();
                     if(palyup)
                     {
                   	  canvas.drawBitmap(playgame,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,160*JengaMeActivity.scaleratioY, mPaint); 
                    }else
                    {
                   	  canvas.drawBitmap(playdown,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,160*JengaMeActivity.scaleratioY, mPaint); 
                    }
                     if(recordup)
                     {
                    	  canvas.drawBitmap(record,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,280*JengaMeActivity.scaleratioY, mPaint); 
                     }else
                     {
                    	  canvas.drawBitmap(recorddown,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,280*JengaMeActivity.scaleratioY, mPaint); 
                     }
                     if(MenuView.optioup)
                     {
                    	 canvas.drawBitmap(options,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,400*JengaMeActivity.scaleratioY, mPaint); 
                     }else
                     {
                    	 canvas.drawBitmap(optionsdown,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,400*JengaMeActivity.scaleratioY, mPaint); 
                     } 
                     if(helpup)
                     {
                    	 canvas.drawBitmap(help,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,520*JengaMeActivity.scaleratioY, mPaint); 
                     }else
                     {
                    	 canvas.drawBitmap(helpdown,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,520*JengaMeActivity.scaleratioY, mPaint); 
                     }
                     
                     if(MenuView.quitup)
                     {
                    	 canvas.drawBitmap(quitgame,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,640*JengaMeActivity.scaleratioY, mPaint); 
                     }else
                     {
                    	 canvas.drawBitmap(quitgamedown,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,640*JengaMeActivity.scaleratioY, mPaint); 
                     }
                     try
                     {
                    	holder.unlockCanvasAndPost(canvas);//解鎖畫布，傳送畫好的圖形  
                     }catch(Exception e)
                     {
                    	 
                     }
            }
        	 beijingtupian.recycle();
        	 playgame.recycle();
        	 playdown.recycle();
        	 record.recycle();
        	 recorddown.recycle();
        	 options.recycle();
        	 optionsdown.recycle();
        	 help.recycle();
        	 helpdown.recycle();
        	 quitgame.recycle();
        	 quitgamedown.recycle();
         }
     }
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}
	public void surfaceCreated(SurfaceHolder holder) {
		 new Thread(new MyThread()).start();
		
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
}
