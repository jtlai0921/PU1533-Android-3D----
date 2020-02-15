package com.bn.txz;

import static com.bn.txz.Constant.*;

import com.bn.txz.manager.ScreenScaleResult;
import com.bn.txz.manager.ScreenScaleUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TXZWelcomeView extends SurfaceView implements SurfaceHolder.Callback   //實現生命周期回調接口
{
	static Bitmap[] logos;//logo圖片陣列
	static boolean loadFlag=false;//是否載入圖片的標志位
	
	TXZActivity activity;	//activity的參考
	
	Bitmap currentLogo;		//目前logo圖片參考
	
	Paint paint;      		//畫筆
	int currentAlpha=0; 	//目前的不透明值
	int sleepSpan=100;       //動畫的時延ms
	float currentX;         //圖片位置
	float currentY;
	ScreenScaleResult ssr;
	public TXZWelcomeView(TXZActivity activity)
	{
		super(activity);
		this.activity = activity; 

		ssr=ScreenScaleUtil.calScale(screenWidth, screenHeight);
		this.loadBitmap();//載入圖片
		
		this.getHolder().addCallback(this);  //設定生命周期回調接口的實現者
		paint = new Paint();  //建立畫筆
		paint.setAntiAlias(true);  //開啟抗鋸齒
	}
	  
	//將圖片載入進記憶體的方法
	public void loadBitmap()  
	{
		if(loadFlag)//若載入圖片標志位為true，則直接return
		{
			return;
		}
		loadFlag=true;  
		logos=new Bitmap[2];
		//載入圖片
		logos[0]=BitmapFactory.decodeResource(this.getResources(),R.drawable.bnkj);  
		logos[1]=BitmapFactory.decodeResource(this.getResources(),R.drawable.welcome);  
	}
	
	//重新定義onDraw方法   
	public void onDraw(Canvas canvas){

		canvas.save();
		canvas.translate(ssr.lucX,ssr.lucY);
		canvas.scale(ssr.ratio,ssr.ratio); 
		
		//繪制黑填充矩形清背景
		paint.setColor(Color.BLACK);//設定畫筆彩色
		paint.setAlpha(255);
		canvas.drawRect(0, 0, screenWidthStandard, screenHeightStandard, paint);
		
		//進行平面貼圖
		if(currentLogo==null)return;
		paint.setAlpha(currentAlpha);	//設定目前不透明度	
		canvas.drawBitmap(currentLogo, currentX, currentY, paint);//繪制目前的logo
		
		canvas.restore();
	}
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{//畫布改變時呼叫
	}
	public void surfaceCreated(SurfaceHolder holder) //建立時被呼叫	
	{	
		new Thread()
		{
			public void run()
			{
				for(int j=0;j<logos.length;j++)//循環每個閃爍的圖片
				{
					Bitmap bm=logos[j];
					currentLogo=bm;  
					//計算圖片位置
					currentX=screenWidthStandard/2-bm.getWidth()/2;
					currentY=screenHeightStandard/2-bm.getHeight()/2;					
					for(int i=255;i>-10;i=i-10)
					{//動態變更圖片的透明度值並不斷重繪	
						currentAlpha=i;
						if(currentAlpha<0)
						{
							currentAlpha=0;
						}
						SurfaceHolder myholder=TXZWelcomeView.this.getHolder();
						Canvas canvas = myholder.lockCanvas();//取得畫布
						try{
							synchronized(myholder){
								onDraw(canvas);//繪制
							}
						}
						catch(Exception e){
							e.printStackTrace();
						}
						finally{
							if(canvas != null){
								myholder.unlockCanvasAndPost(canvas);
							}
						}
						
						try
						{
							if(i==255)
							{//若是新圖片，多等待一會
								Thread.sleep(1000);
							}
							Thread.sleep(sleepSpan);  
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				activity.handler.sendEmptyMessage(Constant.COMMAND_GOTO_MENU_VIEW);//閃爍結束則向Activity傳送訊息
			}
		}.start();
}
	public void surfaceDestroyed(SurfaceHolder arg0)
	{//銷毀時被呼叫
	}
}