package tstc.fxq.main;
import tstc.fxq.constants.Constant;
import tstc.fxq.constants.WhatMessage;
import tstc.fxq.utils.PicLoadUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 * 顯示百納科技的動畫界面
 *
 */
public class WelcomeView extends SurfaceView 
implements SurfaceHolder.Callback  //實現生命周期回調接口
{
	MyActivity activity;
	Paint paint;//畫筆
	int currentAlpha=0;//目前的不透明值
	
	int screenWidth=Constant.SCREEN_WIDTH;//螢幕寬度
	int screenHeight=Constant.SCREEN_HEIGHT;//螢幕高度
	int sleepSpan=50;//動畫的時延ms
	
	Bitmap[] logos=new Bitmap[1];//logo圖片陣列
	Bitmap currentLogo;//目前logo圖片參考
	int currentX;
	int currentY;
	
	public WelcomeView(MyActivity activity) {
		super(activity);
		this.activity = activity;
		this.getHolder().addCallback(this);//設定生命周期回調接口的實現者
		paint = new Paint();//建立畫筆
		paint.setAntiAlias(true);//開啟抗鋸齒
		//載入圖片
		logos[0]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dukeb); 
		
		for(int i=0;i<logos.length;i++){
			logos[i]=PicLoadUtil.scaleToFitFullScreen(logos[i], Constant.wRatio, Constant.hRatio);
		}
	}
	public void onDraw(Canvas canvas){
		//繪制黑填充矩形清背景
		paint.setColor(Color.BLACK);//設定畫筆彩色
		paint.setAlpha(255);
		canvas.drawRect(0, 0, screenWidth, screenHeight, paint);
		
		//進行平面貼圖
		if(currentLogo==null)return;
		paint.setAlpha(currentAlpha);		
		canvas.drawBitmap(currentLogo, currentX, currentY, paint);	
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}
	public void surfaceCreated(SurfaceHolder holder) {//建立時被呼叫		
		new Thread()
		{
			public void run()
			{
				for(Bitmap bm:logos)
				{
					currentLogo=bm;
					//計算圖片位置
					currentX=screenWidth/2-bm.getWidth()/2;
					currentY=screenHeight/2-bm.getHeight()/2;
					
					for(int i=255;i>-10;i=i-10)
					{//動態變更圖片的透明度值並不斷重繪	
						currentAlpha=i;
						if(currentAlpha<0)
						{
							currentAlpha=0;
						}
						SurfaceHolder myholder=WelcomeView.this.getHolder();
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
				//動畫播放完畢後，去主選單界面
				activity.sendMessage(WhatMessage.MAIN_MENU_VIEW);
			}
		}.start();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {//銷毀時被呼叫

	}
}