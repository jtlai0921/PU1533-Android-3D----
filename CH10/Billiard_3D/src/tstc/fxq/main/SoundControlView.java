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
 *  ���ĳ]�w�ɭ�
 *
 */
public class SoundControlView extends SurfaceView implements SurfaceHolder.Callback{
	MyActivity activity;//activity���Ѧ�
	Paint paint;//�e���Ѧ�
	//�������s�Ϥ�
	Bitmap onBitmap;
	Bitmap offBitmap;
	Bitmap onBitmap2;
	Bitmap offBitmap2;
	//��r���Ϥ�	
	private Bitmap yinyueOnBitmap;
	private Bitmap yinyueOffBitmap;
	private Bitmap yinxiaoOnBitmap;
	private Bitmap yinxiaoOffBitmap;
	//�D���W�������s����Ѧ�
	SoundSwitchButton yinyueBtn;
	SoundSwitchButton yinxiaoBtn;	
	//�I���Ϥ�
	Bitmap bgBitmap;
	public SoundControlView(MyActivity activity) {
		super(activity);
		this.activity=activity;
		//��o�J�I�ó]�w���iĲ��
		this.requestFocus();
        this.setFocusableInTouchMode(true);
		getHolder().addCallback(this);//�n���^�ձ��f		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);  	
		//ø��I��
		canvas.drawColor(Color.GRAY);
		canvas.drawBitmap(bgBitmap, 0, 0, paint);
		//ø��������s
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
    		//�I���b���֫��s�W
    		if(yinyueBtn.isActionOnButton(x, y))
    		{
    			yinyueBtn.setOn(!yinyueBtn.isOn());
    			activity.setBackGroundMusicOn(yinyueBtn.isOn());
    		}
    		//�I���b���ī��s�W
    		else if(yinxiaoBtn.isActionOnButton(x, y))
    		{
    			yinxiaoBtn.setOn(!yinxiaoBtn.isOn());
    			activity.setSoundOn(yinxiaoBtn.isOn());
    		}		
    		break;	
    	}
    	//��ø�ɭ�
    	repaint();
		return true;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
		//��ø�ɭ�
    	repaint();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//�إߵe��
		paint.setAntiAlias(true);//�}�ҧܿ���	
		initBitmap();//�_�l���I�}�ϸ귽	
		//�إߵ������s����
		yinyueBtn=new SoundSwitchButton(yinyueOnBitmap,yinyueOffBitmap,onBitmap,offBitmap,180,180,470,180,activity.isBackGroundMusicOn());
		yinxiaoBtn=new SoundSwitchButton(yinxiaoOnBitmap,yinxiaoOffBitmap,onBitmap2,offBitmap2,180,320,470,320,activity.isSoundOn());
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	//���J�Ϥ�����k
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
	//���sø���k
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
