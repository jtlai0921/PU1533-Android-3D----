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
 *  ����ɭ�
 *
 */
public class AboutView extends SurfaceView implements SurfaceHolder.Callback{
	MyActivity activity;//activity���Ѧ�
	Paint paint;//�e���Ѧ�
	//�I���Ϥ�
	Bitmap bgBitmap;
	Bitmap bmp;//��r���Ϥ�
	public AboutView(MyActivity activity) {
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
		canvas.drawBitmap(bgBitmap, 0, 0, paint);
		canvas.drawBitmap(bmp, 148*Constant.wRatio, 150*Constant.hRatio, paint);
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
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//�إߵe��
		paint.setAntiAlias(true);//�}�ҧܿ���	
		initBitmap();//�_�l���I�}�ϸ귽
		repaint();//ø��ɭ�
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	//���J�Ϥ�����k
	public void initBitmap(){
		bgBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
		bmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.about);	
		bgBitmap=PicLoadUtil.scaleToFitFullScreen(bgBitmap, Constant.wRatio, Constant.hRatio);
		bmp=PicLoadUtil.scaleToFitFullScreen(bmp, Constant.wRatio, Constant.hRatio);
		
	}	
}
