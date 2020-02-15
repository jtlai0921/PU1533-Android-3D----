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
 * �ﶵ���ɭ�
 *
 */
public class ChoiceView extends SurfaceView implements SurfaceHolder.Callback{
	MyActivity activity;//activity���Ѧ�
	Paint paint;//�e���Ѧ�
	//�I���Ϥ�
	Bitmap bgBmp;
	//�������s�Ϥ�	
	Bitmap ball8Bmp;
	Bitmap ball9Bmp;
	//�D���W�������s����Ѧ�
	VirtualButton2D ball8Btn;
	VirtualButton2D ball9Btn;
	//������s�ʵe���q
	final float ratioFrom = 0;//x�y�Ъ��_�I
	final float ratioTo = 1;//x�y�Ъ����I
	final float ratioSpan = 0.07f;//x�y�Ъ��B�i
	//���b�h�����ЧӦ�
	private boolean movingFlag = false;
	public ChoiceView(MyActivity activity) {
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
		canvas.drawBitmap(bgBmp, 0, 0, paint);
		//ø��������s
		ball8Btn.drawSelf(canvas, paint);
		ball9Btn.drawSelf(canvas, paint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {	
		//�Y�G���b�h���A���iĲ��
		if(movingFlag){
			return false;
		}
		int x = (int) event.getX();
		int y = (int) event.getY();				
    	switch(event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:    		
    		//�I���b���ӫ��s�W�}�ҭ��ӫ��s
    		if(ball8Btn.isActionOnButton(x, y)){
    			ball8Btn.pressDown();
    		}
    		else if(ball9Btn.isActionOnButton(x, y)){
    			ball9Btn.pressDown();
    		}
    		break;
    	case MotionEvent.ACTION_UP: 
    		//�I���b���ӫ��s�W�}�ҭ��ӫ��s
    		if(ball8Btn.isActionOnButton(x, y) && ball8Btn.isDown())
    		{
    			ball8Btn.releaseUp();
    			//8�y�Ҧ�
    			Constant.POS_INDEX = 0;
    			activity.sendMessage(WhatMessage.MODE_VIEW);
    		}
    		else if(ball9Btn.isActionOnButton(x, y) && ball9Btn.isDown())
    		{
    			ball9Btn.releaseUp();
    			//9�y�Ҧ�
    			Constant.POS_INDEX = 1;
    			activity.sendMessage(WhatMessage.MODE_VIEW);
    		}
    		//��_�������Ҧ����s
    		ball8Btn.releaseUp();
    		ball9Btn.releaseUp();
    		break;    	
    	}
    	//��ø�ɭ�
		repaint();
		return true;
	}	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		//ø��ɭ�
		repaint();
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//�إߵe��
		paint.setAntiAlias(true);//�}�ҧܿ���	
		initBitmap();//�_�l���I�}�ϸ귽
		//�إߵ������s����
		ball8Btn=new VirtualButton2D(ball8Bmp,200,220);
		ball9Btn=new VirtualButton2D(ball9Bmp,450,220);
		

		//�}�Ұ�����A������s�ʵe
		new Thread()
		{
			@Override
			public void run()
			{
				//�Хܤ��iĲ��
				movingFlag = true;
				
				for(float currRatio=ratioFrom; currRatio<=ratioTo; currRatio+=ratioSpan){
					//���ܫ��s����m
					ball8Btn.ratio = currRatio;
					ball9Btn.ratio = currRatio;
					
					//��ø�ɭ�
					repaint();
					try{
		            	Thread.sleep(2);//�ίv���w�@���
		            }
		            catch(Exception e){
		            	e.printStackTrace();//�C�L����|�T��
		            }
				}

				//�Хܥi�HĲ��
				movingFlag = false;
			}
		}.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	//�N�Ϥ����J
	public void initBitmap(){
		ball8Bmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.ball8_btn);
		ball9Bmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.ball9_btn);
		bgBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
		//�A����
		ball8Bmp=PicLoadUtil.scaleToFitFullScreen(ball8Bmp, Constant.wRatio, Constant.hRatio);
		ball9Bmp=PicLoadUtil.scaleToFitFullScreen(ball9Bmp, Constant.wRatio, Constant.hRatio);
		bgBmp=PicLoadUtil.scaleToFitFullScreen(bgBmp, Constant.wRatio, Constant.hRatio);
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
