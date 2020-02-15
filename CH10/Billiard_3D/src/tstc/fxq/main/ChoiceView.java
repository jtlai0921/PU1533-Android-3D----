package tstc.fxq.main;
import tstc.fxq.constants.Constant;
import tstc.fxq.constants.WhatMessage;
import tstc.fxq.utils.PicLoadUtil;
import tstc.fxq.utils.VirtualButton2D;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 * 選項選單界面
 *
 */
public class ChoiceView extends SurfaceView implements SurfaceHolder.Callback{
	MyActivity activity;//activity的參考
	Paint paint;//畫筆參考
	//背景圖片
	Bitmap bgBmp;
	//虛擬按鈕圖片	
	Bitmap ball8Bmp;
	Bitmap ball9Bmp;
	//主選單上虛擬按鈕物件參考
	VirtualButton2D ball8Btn;
	VirtualButton2D ball9Btn;
	//關於按鈕動畫的量
	final float ratioFrom = 0;//x座標的起點
	final float ratioTo = 1;//x座標的終點
	final float ratioSpan = 0.07f;//x座標的步進
	//正在搬移的標志位
	private boolean movingFlag = false;
	public ChoiceView(MyActivity activity) {
		super(activity);
		this.activity=activity;
		//獲得焦點並設定為可觸控
		this.requestFocus();
        this.setFocusableInTouchMode(true);
		getHolder().addCallback(this);//登錄回調接口		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);  	
		//繪制背景
		canvas.drawColor(Color.GRAY);
		canvas.drawBitmap(bgBmp, 0, 0, paint);
		//繪制虛擬按鈕
		ball8Btn.drawSelf(canvas, paint);
		ball9Btn.drawSelf(canvas, paint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {	
		//若果正在搬移，不可觸控
		if(movingFlag){
			return false;
		}
		int x = (int) event.getX();
		int y = (int) event.getY();				
    	switch(event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:    		
    		//點擊在哪個按鈕上開啟哪個按鈕
    		if(ball8Btn.isActionOnButton(x, y)){
    			ball8Btn.pressDown();
    		}
    		else if(ball9Btn.isActionOnButton(x, y)){
    			ball9Btn.pressDown();
    		}
    		break;
    	case MotionEvent.ACTION_UP: 
    		//點擊在哪個按鈕上開啟哪個按鈕
    		if(ball8Btn.isActionOnButton(x, y) && ball8Btn.isDown())
    		{
    			ball8Btn.releaseUp();
    			//8球模式
    			Constant.POS_INDEX = 0;
    			activity.sendMessage(WhatMessage.MODE_VIEW);
    		}
    		else if(ball9Btn.isActionOnButton(x, y) && ball9Btn.isDown())
    		{
    			ball9Btn.releaseUp();
    			//9球模式
    			Constant.POS_INDEX = 1;
    			activity.sendMessage(WhatMessage.MODE_VIEW);
    		}
    		//抬起時關掉所有按鈕
    		ball8Btn.releaseUp();
    		ball9Btn.releaseUp();
    		break;    	
    	}
    	//重繪界面
		repaint();
		return true;
	}	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		//繪制界面
		repaint();
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//建立畫筆
		paint.setAntiAlias(true);//開啟抗鋸齒	
		initBitmap();//起始化點陣圖資源
		//建立虛擬按鈕物件
		ball8Btn=new VirtualButton2D(ball8Bmp,200,220);
		ball9Btn=new VirtualButton2D(ball9Bmp,450,220);
		

		//開啟執行緒，控制按鈕動畫
		new Thread()
		{
			@Override
			public void run()
			{
				//標示不可觸控
				movingFlag = true;
				
				for(float currRatio=ratioFrom; currRatio<=ratioTo; currRatio+=ratioSpan){
					//改變按鈕的位置
					ball8Btn.ratio = currRatio;
					ball9Btn.ratio = currRatio;
					
					//重繪界面
					repaint();
					try{
		            	Thread.sleep(2);//睡眠指定毫秒數
		            }
		            catch(Exception e){
		            	e.printStackTrace();//列印堆堆疊訊息
		            }
				}

				//標示可以觸控
				movingFlag = false;
			}
		}.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	//將圖片載入
	public void initBitmap(){
		ball8Bmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.ball8_btn);
		ball9Bmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.ball9_btn);
		bgBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
		//適應屏
		ball8Bmp=PicLoadUtil.scaleToFitFullScreen(ball8Bmp, Constant.wRatio, Constant.hRatio);
		ball9Bmp=PicLoadUtil.scaleToFitFullScreen(ball9Bmp, Constant.wRatio, Constant.hRatio);
		bgBmp=PicLoadUtil.scaleToFitFullScreen(bgBmp, Constant.wRatio, Constant.hRatio);
	}

	//重新繪制的方法
    public void repaint()
	{
		Canvas canvas=this.getHolder().lockCanvas();
		try
		{
			synchronized(canvas)
			{
				onDraw(canvas);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas!=null)
			{
				this.getHolder().unlockCanvasAndPost(canvas);
			}
		}
	}
}
