package com.cw.view;
import com.cw.game.Constant;
import com.cw.game.JengaMeActivity;
import com.cw.game.R;
import com.cw.game.R.drawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
public class SplashScreenView 
extends SurfaceView 
implements SurfaceHolder.Callback   //實現生命周期回調接口
{
	JengaMeActivity activity;//activity的參考
	Paint paint;      //畫筆
	int currentAlpha=0;  //目前的不透明值
	int sleepSpan=150;      //動畫的時延ms
	Bitmap[] logos=new Bitmap[1];//logo圖片陣列
	Bitmap currentLogo;  //目前logo圖片參考
	float currentX;      //圖片位置
	float currentY;
	public SplashScreenView(JengaMeActivity activity)
	{
		super(activity);
		this.activity = activity; 
		this.getHolder().addCallback(this);  //設定生命周期回調接口的實現者
		paint = new Paint();  //建立畫筆
		paint.setAntiAlias(true);  //開啟抗鋸齒
		//載入圖片
		logos[0]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.szombie0);
		//logos[1]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.vision);
	}
	public void onDraw(Canvas canvas)
	{	
		//繪制黑填充矩形清背景
		paint.setColor(Color.BLACK);//設定畫筆彩色
		paint.setAlpha(255);//設定不透明度為255
		canvas.drawRect(0, 0, 540, 960, paint);
		//進行平面貼圖
		if(currentLogo==null)return;
		paint.setAlpha(currentAlpha);		
		canvas.drawBitmap(currentLogo, currentX, currentY, paint);	
	}
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{
	}
	public void surfaceCreated(SurfaceHolder holder) //建立時被呼叫	
	{	 
		Constant.background0=InPutStreamTobyte.readStream(this,R.drawable.background0);
		Constant.background1=InPutStreamTobyte.readStream(this,R.drawable.background1);
		Constant.background2=InPutStreamTobyte.readStream(this,R.drawable.background2);
		Constant.background3=InPutStreamTobyte.readStream(this,R.drawable.background3);
		Constant.background4=InPutStreamTobyte.readStream(this,R.drawable.background4);
		Constant.background5=InPutStreamTobyte.readStream(this,R.drawable.background5);
		Constant.background6=InPutStreamTobyte.readStream(this,R.drawable.background6);
		Constant.background7=InPutStreamTobyte.readStream(this,R.drawable.background7);
		Constant.background8=InPutStreamTobyte.readStream(this,R.drawable.background8);
		Constant.recordback=InPutStreamTobyte.readStream(this,R.drawable.recordback);
		new Thread()
		{
			public void run()
			{
				for(Bitmap bm:logos)
				{
					currentLogo=bm;//目前圖片的參考
					currentX=540/2-bm.getWidth()/2;//圖片位置
					currentY=960/2-bm.getHeight()/2;
					for(int i=255;i>-10;i=i-10)
					{//動態變更圖片的透明度值並不斷重繪	
						currentAlpha=i;
						if(currentAlpha<0)//若果目前不透明度小於零
						{
							currentAlpha=0;//將不透明度置為零
						}
						SurfaceHolder myholder=SplashScreenView.this.getHolder();//取得回調接口
						Canvas canvas = myholder.lockCanvas();//取得畫布
						try{
							synchronized(myholder)//同步
							{
								onDraw(canvas);//進行繪制繪制
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							if(canvas!= null)//若果目前畫布不為空
							{
								myholder.unlockCanvasAndPost(canvas);//解鎖畫布
							}
						}
						try
						{
							if(i==255)//若是新圖片，多等待一會
							{
								Thread.sleep(1000);
							}
							Thread.sleep(sleepSpan);
						}
						catch(Exception e)//拋出例外
						{
							e.printStackTrace();
						}
					}
				}
				activity.hd.sendEmptyMessage(1);//傳送訊息，進入到主選單界面
			}
		}.start();
	}
	public void surfaceDestroyed(SurfaceHolder arg0)
	{//銷毀時被呼叫
	}
}
