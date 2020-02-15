package tstc.fxq.main;
import tstc.fxq.constants.Constant;
import tstc.fxq.utils.PicLoadUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
/**
 * 
 *  幫助界面
 *
 */
public class HelpView extends SurfaceView implements SurfaceHolder.Callback{
	MyActivity activity;//activity的參考
	Paint paint;//畫筆參考
	//所有圖片的id
	private final int[] bmpIds = 
	{
			R.drawable.help0, R.drawable.help1, R.drawable.help2,
			R.drawable.help3, R.drawable.help4, R.drawable.help5, 
			R.drawable.help6,
	};
	Bitmap[] helpBmps = new Bitmap[bmpIds.length];
	Bitmap pointerBmp;
	int currIndex;//目前幫助圖片在陣列中的索引
	float currY;//目前圖片的y位置
	float bmpHeight;//原圖片的高度
	float pointerBmpWidth;//指標圖片的高度
	float pointerX = 700f;
	float pointerY = 5f;
	//關於觸摸的變數
	private boolean movingFlag = false;//正在搬移的標志位
	float mPreviousY;
	float dy; 	
	int fullTime=0;//記錄從開始到目前的時間
	long startTime;//開始時間
	DrawThread drawThread;//繪制執行緒參考	
	
	//有關標示的變數
	private boolean touchScreenFlag = false;//是否正在觸控屏的標志位
	private Bitmap[] markBmps = new Bitmap[5];//標示圖片陣列，共有5種圖片
	private final int[] markBmpIds = {R.drawable.mark0, R.drawable.mark1, R.drawable.mark2, R.drawable.mark3, R.drawable.mark4,};
	//按鈕圖片的id
	private final int BTN_ID = 0;
	private final int BAR_ID = 1;
	private final int ICON_ID = 2;
	private final int TIME_ID = 3;
	private final int MAP_ID = 4;
	//存放標示訊息的3D陣列，第二維指哪一頁，第3D三個量的含意：要繪制的圖的索引，圖的x座標，y座標
	private final int[][][] markPos = 
	{
		{//第1頁
			//不用標示
		},
		{//第2頁
			{
				BTN_ID, 162, 421 //球桿方向調整按鈕--左				
			},
			{
				BTN_ID, 259, 421 //球桿方向調整按鈕--右		
			},
			{
				BAR_ID, 21, 272 //球桿方向調整按鈕--右		
			},
		},
		{//第3頁
			{
				BTN_ID, 86, 422 //縮放按鈕--左
			},
			{
				BTN_ID, 121, 422 //縮放按鈕--右
			},
			{
				BTN_ID, 20, 422 //擊球按鈕
			},
		},
		{//第4頁
			{
				BTN_ID, 325, 422 //M按鈕
			},
			{
				MAP_ID, 320, 205 //迷你圖
			},
		},
		{//第5頁
			{
				BTN_ID, 396, 422 //角度切換按鈕
			},
		},
		{//第6頁
			{
				ICON_ID, 23, 206 //進球圖示
			},
		},
		{//第7頁
			{
				TIME_ID, 199, 211 //倒計時
			},
		},
	};
	//顯示toast的handler
	public Handler hd=new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// 呼叫父類別處理
			super.handleMessage(msg);
			Toast.makeText(activity, "上下抹動螢幕翻頁", Toast.LENGTH_LONG).show();
		}
	};
   
	public HelpView(MyActivity activity) {
		super(activity);
		this.activity=activity;
		//獲得焦點並設定為可觸控
		this.requestFocus();
        this.setFocusableInTouchMode(true);
		getHolder().addCallback(this);//登錄回調接口
		startTime=System.currentTimeMillis();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas); 
		canvas.drawColor(Color.GRAY);
		//繪制背景
		canvas.drawBitmap(helpBmps[currIndex], 0, currY*Constant.hRatio, paint);
		if(currIndex < bmpIds.length-1){
			canvas.drawBitmap(helpBmps[currIndex+1], 0, (currY+bmpHeight)*Constant.hRatio, paint);			
		}
		if(currIndex > 0){
			canvas.drawBitmap(helpBmps[currIndex-1], 0, (currY-bmpHeight)*Constant.hRatio, paint);
		}
		
		//繪制幫助界面閃動的圖示
		long currentTime=System.currentTimeMillis();//記錄目前時間
		fullTime=(int) ((currentTime-startTime));//記錄總時間	
		//將1秒分成兩份，在0.7秒內繪制，0.3秒內不繪制
		if((fullTime/100)%10 < 7) {
			//繪制翻頁指示圖示
			drawPointer(canvas, paint);
			//若果沒有搬移，再繪制標示
			if(!(movingFlag || touchScreenFlag)){
				drawMarks(canvas, paint);
			}
		}
	}
	
	//繪制翻頁指示針的方法
	public void drawPointer(Canvas canvas, Paint paint){
		paint.setAlpha(200);
		canvas.drawBitmap(pointerBmp, pointerX*Constant.wRatio, pointerY*Constant.hRatio, paint);
		paint.setAlpha(255);
	}
	
	//繪制標示按鈕的方法
	public void drawMarks(Canvas canvas, Paint paint){
		for(int i=0; i<markPos[currIndex].length; i++){
			canvas.drawBitmap(
					markBmps[markPos[currIndex][i][0]], 
					markPos[currIndex][i][1] * Constant.wRatio, 
					markPos[currIndex][i][2] * Constant.hRatio, 
					paint
					);
		}		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//若果正在搬移，不可觸控
		if(movingFlag){
			touchScreenFlag = false;
			return false;
		}
		float x = event.getX();
		float y = event.getY();
    	switch(event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:
    		touchScreenFlag = true;
    		
    		if(x>pointerX*Constant.wRatio && x<pointerX*Constant.wRatio + pointerBmp.getWidth() &&
    				(y>pointerY*Constant.hRatio && y<pointerY*Constant.hRatio + pointerBmp.getHeight())
    		){
    			hd.sendEmptyMessage(1);
    		}
    		mPreviousY = y;//記錄觸控位置
    		break;
    	case MotionEvent.ACTION_MOVE:
        	dy = y - mPreviousY;//計算觸控筆Y位移	
        	//若果向前翻頁
        	if(dy > 0){
        		currY = dy;
        	}
        	//若果向後翻頁
        	else {
            	currY = dy;
        	}
    		break;
    	case MotionEvent.ACTION_UP: 
        	touchScreenFlag = false;
        	
    		dy = y - mPreviousY;//計算觸控筆Y位移
    		//開啟執行緒，控制搬移動畫
    		new Thread()
    		{
    			private final int timeSpan = 2;
    			@Override
    			public void run()
    			{
    				//標示不可觸控
    				movingFlag = true;    				
    				final float SPAN = 40;
    				float yFrom = currY;
    				float ySpan = 0;
    				float yTo = 0;
    				int totalSteps = 0;
    	    		
    	    		//若為往前翻頁
    	    		if(dy >0){
    	    			//可以翻
    	    			if(Math.abs(dy) > Constant.SCREEN_HEIGHT/2.0f && currIndex > 0){
    	    				ySpan = SPAN;
    	    				yTo = Constant.screenHeightTest;
    	    				totalSteps = (int) (Math.abs(yFrom-yTo)/Math.abs(ySpan));  
    	    			}
    	    			//不可以翻
    	    			else{
    	    				ySpan = -SPAN;
    	    				yTo = 0;
    	    				totalSteps = (int) (Math.abs(yFrom-yTo)/Math.abs(ySpan));  
    	    			}
    	    		}
    	    		//若為向後翻頁
    	    		else if(dy <0){
    	    			//可以翻
    	    			if(Math.abs(dy) > Constant.SCREEN_HEIGHT/2.0f && currIndex < bmpIds.length-1){
    	    				ySpan = -SPAN;
    	    				yTo = -Constant.screenHeightTest;
    	    				totalSteps = (int) (Math.abs(yFrom-yTo)/Math.abs(ySpan));  
    	    			}
    	    			//不可以翻
    	    			else{
    	    				ySpan = SPAN;
    	    				yTo = 0;
    	    				totalSteps = (int) (Math.abs(yFrom-yTo)/Math.abs(ySpan));  
    	    			}    	    		
    	    		}
    				for(int i=0; i<totalSteps+1; i++){
    					//改變按鈕的位置
    					currY = yFrom + i*ySpan;
    					//重繪界面
    					repaint();
    					try{
    		            	Thread.sleep(timeSpan);//睡眠指定毫秒數
    		            }
    		            catch(Exception e){
    		            	e.printStackTrace();//列印堆堆疊訊息
    		            }
    				}
    				
    	    		//若為往前翻頁，且大於設定值
    	    		if(dy >0 && Math.abs(dy) > Constant.SCREEN_HEIGHT/2.0f){
    	    			if(currIndex > 0){
    	        			currIndex -= 1;
    	    			}
    	    		}
    	    		//若為向後翻頁，且大於設定值
    	    		else if(dy <0 && Math.abs(dy) > Constant.SCREEN_HEIGHT/2.0f){
    	    			if(currIndex < bmpIds.length-1){
    	        			currIndex += 1;                      
    	    			}
    	    		}
    	    		currY = 0;
    				//標示可以觸控
    				movingFlag = false;
    			}
    		}.start();
    		break;    	
    	}
		return true;
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
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//建立畫筆
		paint.setAntiAlias(true);//開啟抗鋸齒
		initBitmap();//起始化點陣圖資源
		currIndex = 0;
		drawThread=new DrawThread(this);//建立繪制執行緒'
		drawThread.start();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		drawThread.setFlag(false);
	}
	//載入圖片的方法
	public void initBitmap(){
		for(int i=0 ;i<helpBmps.length; i++){
			helpBmps[i]=BitmapFactory.decodeResource(this.getResources(), bmpIds[i]);			
		}
		pointerBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.pointer);		
		for(int i=0 ;i<markBmps.length; i++){
			markBmps[i]=BitmapFactory.decodeResource(this.getResources(), markBmpIds[i]);			
		}
		
		//原圖片高度
		bmpHeight = helpBmps[0].getHeight();
		pointerBmpWidth = pointerBmp.getWidth();
		//適應屏
		for(int i=0 ;i<helpBmps.length; i++){
			helpBmps[i]=PicLoadUtil.scaleToFitFullScreen(helpBmps[i], Constant.wRatio, Constant.hRatio);
		}
		pointerBmp=PicLoadUtil.scaleToFitFullScreen(pointerBmp, Constant.wRatio, Constant.hRatio);
		for(int i=0 ;i<markBmps.length; i++){
			markBmps[i]=PicLoadUtil.scaleToFitFullScreen(markBmps[i], Constant.wRatio, Constant.hRatio);
		}
	}	
}

class DrawThread extends Thread{
	private boolean flag = true;	
	private int sleepSpan = 20;
	HelpView fatherView;
	SurfaceHolder surfaceHolder;
	public DrawThread(HelpView helpView){
		this.fatherView = helpView;
		this.surfaceHolder = helpView.getHolder();
	}
	public void run(){
        while (this.flag)
        {
            fatherView.repaint();
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
