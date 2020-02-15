package tstc.fxq.main;
import tstc.fxq.constants.Constant;
import tstc.fxq.utils.PicLoadUtil;
import tstc.fxq.utils.SoundSwitchButton;
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
 *  音效設定界面
 *
 */
public class SoundControlView extends SurfaceView implements SurfaceHolder.Callback{
	MyActivity activity;//activity的參考
	Paint paint;//畫筆參考
	//虛擬按鈕圖片
	Bitmap onBitmap;
	Bitmap offBitmap;
	Bitmap onBitmap2;
	Bitmap offBitmap2;
	//文字的圖片	
	private Bitmap yinyueOnBitmap;
	private Bitmap yinyueOffBitmap;
	private Bitmap yinxiaoOnBitmap;
	private Bitmap yinxiaoOffBitmap;
	//主選單上虛擬按鈕物件參考
	SoundSwitchButton yinyueBtn;
	SoundSwitchButton yinxiaoBtn;	
	//背景圖片
	Bitmap bgBitmap;
	public SoundControlView(MyActivity activity) {
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
		//繪制虛擬按鈕
		yinyueBtn.drawSelf(canvas, paint);
		yinxiaoBtn.drawSelf(canvas, paint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();		
    	switch(event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:
    		//點擊在音樂按鈕上
    		if(yinyueBtn.isActionOnButton(x, y))
    		{
    			yinyueBtn.setOn(!yinyueBtn.isOn());
    			activity.setBackGroundMusicOn(yinyueBtn.isOn());
    		}
    		//點擊在音效按鈕上
    		else if(yinxiaoBtn.isActionOnButton(x, y))
    		{
    			yinxiaoBtn.setOn(!yinxiaoBtn.isOn());
    			activity.setSoundOn(yinxiaoBtn.isOn());
    		}		
    		break;	
    	}
    	//重繪界面
    	repaint();
		return true;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
		//重繪界面
    	repaint();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//建立畫筆
		paint.setAntiAlias(true);//開啟抗鋸齒	
		initBitmap();//起始化點陣圖資源	
		//建立虛擬按鈕物件
		yinyueBtn=new SoundSwitchButton(yinyueOnBitmap,yinyueOffBitmap,onBitmap,offBitmap,180,180,470,180,activity.isBackGroundMusicOn());
		yinxiaoBtn=new SoundSwitchButton(yinxiaoOnBitmap,yinxiaoOffBitmap,onBitmap2,offBitmap2,180,320,470,320,activity.isSoundOn());
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	//載入圖片的方法
	public void initBitmap(){
		onBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.on);
		offBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.off);
		onBitmap2=BitmapFactory.decodeResource(this.getResources(), R.drawable.lvon);
		offBitmap2=BitmapFactory.decodeResource(this.getResources(), R.drawable.lvoff);
		yinyueOnBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.yinyuekai);
		yinyueOffBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.yinyueguan);
		yinxiaoOnBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.yinxiaokai);
		yinxiaoOffBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.yinxiaoguan);	
		bgBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
		
		onBitmap=PicLoadUtil.scaleToFitFullScreen(onBitmap, Constant.wRatio, Constant.hRatio);
		offBitmap=PicLoadUtil.scaleToFitFullScreen(offBitmap, Constant.wRatio, Constant.hRatio);
		onBitmap2=PicLoadUtil.scaleToFitFullScreen(onBitmap2, Constant.wRatio, Constant.hRatio);
		offBitmap2=PicLoadUtil.scaleToFitFullScreen(offBitmap2, Constant.wRatio, Constant.hRatio);
		yinyueOnBitmap=PicLoadUtil.scaleToFitFullScreen(yinyueOnBitmap, Constant.wRatio, Constant.hRatio);		
		yinyueOffBitmap=PicLoadUtil.scaleToFitFullScreen(yinyueOffBitmap, Constant.wRatio, Constant.hRatio);
		yinxiaoOnBitmap=PicLoadUtil.scaleToFitFullScreen(yinxiaoOnBitmap, Constant.wRatio, Constant.hRatio);		
		yinxiaoOffBitmap=PicLoadUtil.scaleToFitFullScreen(yinxiaoOffBitmap, Constant.wRatio, Constant.hRatio);
		bgBitmap=PicLoadUtil.scaleToFitFullScreen(bgBitmap, Constant.wRatio, Constant.hRatio);
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
