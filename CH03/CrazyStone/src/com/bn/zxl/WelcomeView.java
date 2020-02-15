package com.bn.zxl;

import com.bn.util.PicLoadUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import static com.bn.util.Constant.*;

public class WelcomeView extends SurfaceView implements Callback {
	
	MyActivity activity;//activity的參考
	Paint paint;      //畫筆
	int currentAlpha;  //目前的不透明值
	int sleepSpan=150;      //動畫的時延ms
	Bitmap logo;  //logo圖片參考
	float currentX;      //圖片位置
	float currentY;
	
	public WelcomeView(MyActivity activity){
		super(activity);
		this.activity=activity;
		this.getHolder().addCallback(this);//設定生命周期回調接口的實現者
		paint=new Paint();//建立畫筆
		paint.setAntiAlias(true);//開啟抗鋸齒
		logo=PicLoadUtil.loadBM(getResources(),"androidheli.png" );//載入圖片
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//繪制黑填充矩形清背景
		paint.setColor(Color.BLACK);//設定畫筆彩色
		paint.setAlpha(255);//設定不透明度為255
		canvas.drawRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, paint);
		//進行平面貼圖
		if(logo==null)return;
		paint.setAlpha(currentAlpha);
		canvas.drawBitmap(logo, currentX, currentY, paint);	
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		currentX=SCREEN_WIDTH/2-logo.getWidth()/2;
		currentY=SCREEN_HEIGHT/2-logo.getHeight()/2;
		
		new Thread(){
			public void run() {
				
				SurfaceHolder mholder=WelcomeView.this.getHolder();//取得回調接口
				for(int i=255;i>-10;i-=10){
					
					if(i<0)//若果目前不透明度小於零
					{
						i=0;//將不透明度置為零
					}
					currentAlpha=i;
					Canvas canvas=mholder.lockCanvas();//取得畫布
					try {
						synchronized (mholder) //同步
						{
							onDraw(canvas);//進行繪制繪制
						}
						Thread.sleep(200);
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						if(canvas!=null)
						{
							mholder.unlockCanvasAndPost(canvas);
						}
					}
					
				}
				if(LOAD_ACTIVITY){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				activity.hd.sendEmptyMessage(1);
			}
		}.start();

	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
