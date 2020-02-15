package com.bn.gjxq;

import static com.bn.gjxq.Constant.screenHeight;
import static com.bn.gjxq.Constant.screenWidth;

import com.bn.gjxq.manager.ScreenScaleResult;
import com.bn.gjxq.manager.ScreenScaleUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.bn.gjxq.GJXQActivity.*;

public class MenuView extends SurfaceView implements SurfaceHolder.Callback   //實現生命周期回調接口
{
	static Bitmap[] bitmap;//圖片陣列
	static boolean loadFlag=false;
	
	GJXQActivity activity;//activity的參考
	
	float x;
	float y;
	
	Paint paint;      //畫筆
	ScreenScaleResult ssr;
	public MenuView(GJXQActivity activity)
	{
		super(activity);
		this.activity = activity; 
		ssr=ScreenScaleUtil.calScale(screenWidth, screenHeight);
		loadBitmap();
		this.getHolder().addCallback(this);  //設定生命周期回調接口的實現者
		paint = new Paint();  //建立畫筆
		paint.setAntiAlias(true);  //開啟抗鋸齒
	}
	
	
	
	public void loadBitmap()
	{
		if(loadFlag)
		{
			return;
		}
		loadFlag=true;  
		bitmap=new Bitmap[5];
		//載入圖片
		bitmap[0]=BitmapFactory.decodeResource(this.getResources(),R.drawable.chessmenu);  
		bitmap[1]=BitmapFactory.decodeResource(this.getResources(),R.drawable.game); 
		bitmap[2]=BitmapFactory.decodeResource(this.getResources(),R.drawable.set);
		bitmap[3]=BitmapFactory.decodeResource(this.getResources(),R.drawable.help); 
		bitmap[4]=BitmapFactory.decodeResource(this.getResources(),R.drawable.about); 
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		x=event.getX();
		y=event.getY();
		switch(event.getAction())
		{
		  case MotionEvent.ACTION_DOWN:
			  if(x>=(screenWidthStandard/2-bitmap[1].getWidth()-10+ssr.lucX)*ssr.ratio
				 &&x<=(screenWidthStandard/2-bitmap[1].getWidth()+290+ssr.lucX)*ssr.ratio
				 &&y>=(200+ssr.lucY)*ssr.ratio&&y<=(289+ssr.lucY)*ssr.ratio)
			  {
				  activity.gotoGameView();
			  }
			  else if(x>=(screenWidthStandard/2+10+ssr.lucX)*ssr.ratio
				 &&x<=(screenWidthStandard/2+310+ssr.lucY)*ssr.ratio
				 &&y>=(200+ssr.lucY)*ssr.ratio&&y<=(289+ssr.lucY)*ssr.ratio)
			  {
				  activity.gotoSetView();
			  }
			  else if(x>=(screenWidthStandard/2-bitmap[3].getWidth()-10+ssr.lucX)*ssr.ratio
						 &&x<=(screenWidthStandard/2-bitmap[3].getWidth()+290+ssr.lucY)*ssr.ratio
						 &&y>=(309+ssr.lucY)*ssr.ratio&&y<=(398+ssr.lucY)*ssr.ratio)
			  {
				 activity.gotoHelpView();
			  }
			  else if(x>=(screenWidthStandard/2+10+ssr.lucX)*ssr.ratio&&
					  x<=(screenWidthStandard/2+310+ssr.lucX)*ssr.ratio
					  &&y>=(309+ssr.lucY)*ssr.ratio&&y<=(398+ssr.lucY)*ssr.ratio)
			  {
				  activity.gotoAboutVeiw();
			  }
		   break;
		}
		return true;
	}



	public void onDraw(Canvas canvas){	 
		canvas.save();
		canvas.translate(ssr.lucX,ssr.lucY);
		canvas.scale(ssr.ratio,ssr.ratio); 
		
		//繪制黑填充矩形清背景
		paint.setColor(Color.BLACK);//設定畫筆彩色
		paint.setAlpha(255);
		
		//進行平面貼圖
		canvas.drawBitmap(bitmap[0], 0, 0, paint);
		//進入游戲
		canvas.drawBitmap(bitmap[1], screenWidthStandard/2-bitmap[1].getWidth()-10, 200, paint);
		//音效設定
		canvas.drawBitmap(bitmap[2], screenWidthStandard/2+10, 200, paint);
		//幫助
		canvas.drawBitmap(bitmap[3], screenWidthStandard/2-bitmap[3].getWidth()-10, 309, paint);
		//關於
		canvas.drawBitmap(bitmap[4], screenWidthStandard/2+10, 309, paint);
		canvas.restore();
	}
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{}
	public void surfaceCreated(SurfaceHolder holder) //建立時被呼叫	
	{	
		Canvas canvas=holder.lockCanvas();
		synchronized(holder)
		{
			try
			{
				onDraw(canvas);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if(canvas!=null)
				{
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
    }
	public void surfaceDestroyed(SurfaceHolder arg0)
	{//銷毀時被呼叫
	}
}