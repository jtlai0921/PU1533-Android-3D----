package tstc.fxq.main;
import tstc.fxq.constants.Constant;
import tstc.fxq.utils.DBUtil;
import tstc.fxq.utils.PicLoadUtil;
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
 * 積分榜界面
 *
 */
public class HighScoreView extends SurfaceView implements SurfaceHolder.Callback{
	MyActivity activity;//activity的參考
	Paint paint;//畫筆參考
	DrawThread drawThread;//繪制執行緒參考		
	Bitmap bgBitmap;//背景圖片	
	Bitmap bmp;//文字的圖片
	Bitmap defenBitmap;
	Bitmap riqiBitmap;
	Bitmap[] numberBitmaps;//數字圖片	
	Bitmap gangBitmap;//符號"-"對應的圖片
	String queryResultStr;//查詢資料庫的結果
	String[] splitResultStrs;//將查詢結果切分後的陣列
	private int numberWidth;//數字圖片的寬度
	private int numberHeight;//數字圖片的寬度
	private int posFrom=-1;//查詢的開始位置
	private int length=5;//查詢的最大記錄個數
	int downY=0;//按下和抬起的y座標
	int upY=0;
	public HighScoreView(MyActivity activity) {
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
		canvas.drawBitmap(bgBitmap, 0, 0, paint);
		//繪制文字+Constant.X_OFFSET,  +Constant.Y_OFFSET
		canvas.drawBitmap(bmp, 95*Constant.wRatio, 148*Constant.hRatio, paint);
		canvas.drawBitmap(riqiBitmap, 230*Constant.wRatio, 180*Constant.hRatio, paint);
		canvas.drawBitmap(defenBitmap, 470*Constant.wRatio, 180*Constant.hRatio, paint);
		//繪制得分和時間
		float x;
		float y;
		for(int i=0;i<splitResultStrs.length;i++)
		{
			if(i%2==0)//計算得分的位置
			{
				x=570;				
			}
			else//計算時間的位置
			{
				x=410;
			}
			y=180+(numberHeight+10)*(i/2+1);
			//繪制字串
			drawDateBitmap(splitResultStrs[i], x*Constant.wRatio, y*Constant.hRatio, canvas,paint);
		}
	}
	//畫日期圖片的方法
	public void drawDateBitmap(String numberStr,float endX,float endY,Canvas canvas,Paint paint)
	{
		for(int i=0;i<numberStr.length();i++)
		{
			char c=numberStr.charAt(i);
			if(c=='-')
			{
				canvas.drawBitmap(gangBitmap,(endX-numberWidth*(numberStr.length()-i))*Constant.wRatio, endY*Constant.hRatio, paint);
			}
			else
			{
				canvas.drawBitmap
				(
						numberBitmaps[c-'0'], 
						(endX-numberWidth*(numberStr.length()-i))*Constant.wRatio, 
						endY*Constant.hRatio, 
						paint
				);
			}			
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int y = (int) event.getY();		
    	switch(event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:
    		downY=y;
    		break;
    	case MotionEvent.ACTION_UP:
    		upY=y;    		
        	if(Math.abs(downY-upY)<20)//在域值範圍內，不翻頁
        	{
        		return true;
        	}
        	else if(downY<upY)//往下抹
        	{	
        		//若果抹到最前頁，不可再抹
        		if(this.posFrom-this.length>=-1)
        		{
        			this.posFrom-=this.length;        			
        		}
        	}
        	else//往上抹
        	{	
        		//若果抹到最後頁，不可再抹
        		if(this.posFrom+this.length<DBUtil.getRowCount(Constant.POS_INDEX)-1)
        		{
        			this.posFrom+=this.length;        			
        		}
        	}
        	queryResultStr=DBUtil.query(Constant.POS_INDEX, posFrom, length);//得到資料庫中的資料
			splitResultStrs=queryResultStr.split("/", 0);//用"/"切分，且捨掉空字串
    		break;    	
    	}
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
		createAllThreads();//建立所有執行緒
		initBitmap();//起始化點陣圖資源	
		numberWidth=numberBitmaps[0].getWidth()+3;//得到數字圖片的寬度
		numberHeight = numberBitmaps[0].getHeight();
		posFrom=-1;//查詢的開始位置置-1			
		queryResultStr = DBUtil.query(Constant.POS_INDEX, posFrom, length);//得到資料庫中的資料
		splitResultStrs = queryResultStr.split("/", 0);//用"/"切分，且捨掉空字串		
		//使圖片自適應屏
		changeBmpToFitScreen();
		startAllThreads();//開啟所有執行緒
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		  boolean retry = true;
	        stopAllThreads();
	        while (retry) {
	            try {
	            	drawThread.join();
	                retry = false;
	            } 
	            catch (InterruptedException e) {e.printStackTrace();}//不斷地循環，直到刷框執行緒結束
	        }
	}
	//將圖片載入
	public void initBitmap(){
		bmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.bmp);	
		bgBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
		numberBitmaps=new Bitmap[]{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number0),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number1),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number2),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number3),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number4),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number5),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number6),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number7),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number8),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number9),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number0),
		};
		gangBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.gang);
		defenBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.defen);
		riqiBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.riqi);
		
	}
	void changeBmpToFitScreen(){
		//適應屏
		bmp=PicLoadUtil.scaleToFitFullScreen(bmp, Constant.wRatio, Constant.hRatio);
		gangBitmap=PicLoadUtil.scaleToFitFullScreen(gangBitmap, Constant.wRatio, Constant.hRatio);
		riqiBitmap=PicLoadUtil.scaleToFitFullScreen(riqiBitmap, Constant.wRatio, Constant.hRatio);
		defenBitmap=PicLoadUtil.scaleToFitFullScreen(defenBitmap, Constant.wRatio, Constant.hRatio);				
		for(int i=0;i<numberBitmaps.length;i++){
			numberBitmaps[i]=PicLoadUtil.scaleToFitFullScreen(numberBitmaps[i], Constant.wRatio, Constant.hRatio);
		}
		bgBitmap=PicLoadUtil.scaleToFitFullScreen(bgBitmap, Constant.wRatio, Constant.hRatio);
	}
	void createAllThreads()
	{
		drawThread=new DrawThread(this);//建立繪制執行緒		
	}
	void startAllThreads()
	{
		drawThread.setFlag(true);     
		drawThread.start();
	}
	void stopAllThreads()
	{
		drawThread.setFlag(false);       
	}
	private class DrawThread extends Thread{
		private boolean flag = true;	
		private int sleepSpan = 100;
		HighScoreView fatherView;
		SurfaceHolder surfaceHolder;
		public DrawThread(HighScoreView fatherView){
			this.fatherView = fatherView;
			this.surfaceHolder = fatherView.getHolder();
		}
		public void run(){
			Canvas c;
	        while (this.flag) {
	            c = null;
	            try {
	            	// 鎖定整個畫布，在記憶體要求比較高的情況下，建議參數不要為null
	                c = this.surfaceHolder.lockCanvas(null);
	                synchronized (this.surfaceHolder) {
	                	fatherView.onDraw(c);//繪制
	                }
	            } finally {
	                if (c != null) {
	                	//並釋放鎖
	                    this.surfaceHolder.unlockCanvasAndPost(c);
	                }
	            }
	            try{
	            	Thread.sleep(sleepSpan);//睡眠指定毫秒數
	            }
	            catch(Exception e){
	            	e.printStackTrace();//列印堆堆疊訊息
	            }
	        }
		}
		public void setFlag(boolean flag) {
			this.flag = flag;
		}
	}
}
