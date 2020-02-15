package tstc.fxq.main;

import tstc.fxq.constants.Constant;
import tstc.fxq.utils.PicLoadUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 *  關於界面
 *
 */
public class AboutView extends SurfaceView implements SurfaceHolder.Callback{
	MyActivity activity;//activity的參考
	Paint paint;//畫筆參考
	//背景圖片
	Bitmap bgBitmap;
	Bitmap bmp;//文字的圖片
	public AboutView(MyActivity activity) {
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
		canvas.drawBitmap(bgBitmap, 0, 0, paint);
		canvas.drawBitmap(bmp, 148*Constant.wRatio, 150*Constant.hRatio, paint);
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
		repaint();//繪制界面
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	//載入圖片的方法
	public void initBitmap(){
		bgBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
		bmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.about);	
		bgBitmap=PicLoadUtil.scaleToFitFullScreen(bgBitmap, Constant.wRatio, Constant.hRatio);
		bmp=PicLoadUtil.scaleToFitFullScreen(bmp, Constant.wRatio, Constant.hRatio);
		
	}	
}
