package tstc.fxq.main;
import tstc.fxq.constants.Constant;
import tstc.fxq.constants.GameMode;
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
 * 模式選取界面
 *
 */
public class ModeView extends SurfaceView implements SurfaceHolder.Callback{
	MyActivity activity;//activity的參考
	Paint paint;//畫筆參考
	//虛擬按鈕圖片
	Bitmap choiceBmp0;
	Bitmap choiceBmp1;
	Bitmap choiceBmp2;
	//主選單上虛擬按鈕物件參考
	VirtualButton2D countDownModeBtn;
	VirtualButton2D practiceModeBtn;
	VirtualButton2D highScoreBtn;
	//背景圖片
	Bitmap bgBmp;
	//關於按鈕動畫的量
	final float xFrom = 180;//x座標的起點
	final float xTo = 283.5f;//x座標的終點
	final float xSpan = 5.5f;//x座標的步進
	private boolean movingFlag = false;//正在搬移的標志位
	public ModeView(MyActivity activity) {
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
		countDownModeBtn.drawSelf(canvas, paint);
		practiceModeBtn.drawSelf(canvas, paint);
		highScoreBtn.drawSelf(canvas, paint);
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
    		if(countDownModeBtn.isActionOnButton(x, y)){
    			countDownModeBtn.pressDown();
    		}
    		else if(practiceModeBtn.isActionOnButton(x, y)){
    			practiceModeBtn.pressDown();
    		}
    		else if(highScoreBtn.isActionOnButton(x, y)){
    			highScoreBtn.pressDown();	
    		}
    		break;
    	case MotionEvent.ACTION_UP: 
    		//點擊在哪個按鈕上開啟哪個按鈕
    		if(countDownModeBtn.isActionOnButton(x, y) && countDownModeBtn.isDown())
    		{
    			countDownModeBtn.releaseUp();
    			//標示為倒計時模式
    			Constant.mode = GameMode.countDown;
    			activity.sendMessage(WhatMessage.GAME_VIEW);
    		}
    		else if(practiceModeBtn.isActionOnButton(x, y) && practiceModeBtn.isDown())
    		{
    			practiceModeBtn.releaseUp();
    			//標示為練習模式
    			Constant.mode = GameMode.practice;
    			activity.sendMessage(WhatMessage.GAME_VIEW);
    		}
    		else if(highScoreBtn.isActionOnButton(x, y) && highScoreBtn.isDown())
    		{
    			highScoreBtn.releaseUp();
    			activity.sendMessage(WhatMessage.HIGH_SCORE_VIEW);    			
    		}
    		//抬起時關掉所有按鈕
    		countDownModeBtn.releaseUp();
    		practiceModeBtn.releaseUp();
    		highScoreBtn.releaseUp();	
    		break;    	
    	}
    	//重繪界面
		repaint();
		return true;
	}	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//建立畫筆
		paint.setAntiAlias(true);//開啟抗鋸齒	
		initBitmap();//起始化點陣圖資源
		//建立虛擬按鈕物件
		countDownModeBtn=new VirtualButton2D(choiceBmp0,xFrom,170);
		practiceModeBtn=new VirtualButton2D(choiceBmp1,xFrom,170+90);
		highScoreBtn=new VirtualButton2D(choiceBmp2,xFrom,170+2*90);
		
		//開啟執行緒，控制按鈕動畫
		new Thread()
		{
			@Override
			public void run()
			{
				//標示不可觸控
				movingFlag = true;
				
				for(float currX=xFrom; currX<=xTo; currX+=xSpan){
					//改變按鈕的位置
					countDownModeBtn.x = currX;
					practiceModeBtn.x = 800 - 243 - currX;
					highScoreBtn.x = currX;
					
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
	//將圖片載入
	public void initBitmap(){
		choiceBmp0=BitmapFactory.decodeResource(this.getResources(), R.drawable.daojishi);
		choiceBmp1=BitmapFactory.decodeResource(this.getResources(), R.drawable.lianxi);
		choiceBmp2=BitmapFactory.decodeResource(this.getResources(), R.drawable.paihang);
		bgBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
		//適應屏
		choiceBmp0=PicLoadUtil.scaleToFitFullScreen(choiceBmp0, Constant.wRatio, Constant.hRatio);
		choiceBmp1=PicLoadUtil.scaleToFitFullScreen(choiceBmp1, Constant.wRatio, Constant.hRatio);
		choiceBmp2=PicLoadUtil.scaleToFitFullScreen(choiceBmp2, Constant.wRatio, Constant.hRatio);
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
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}
