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
 * �Ҧ�����ɭ�
 *
 */
public class ModeView extends SurfaceView implements SurfaceHolder.Callback{
	MyActivity activity;//activity���Ѧ�
	Paint paint;//�e���Ѧ�
	//�������s�Ϥ�
	Bitmap choiceBmp0;
	Bitmap choiceBmp1;
	Bitmap choiceBmp2;
	//�D���W�������s����Ѧ�
	VirtualButton2D countDownModeBtn;
	VirtualButton2D practiceModeBtn;
	VirtualButton2D highScoreBtn;
	//�I���Ϥ�
	Bitmap bgBmp;
	//������s�ʵe���q
	final float xFrom = 180;//x�y�Ъ��_�I
	final float xTo = 283.5f;//x�y�Ъ����I
	final float xSpan = 5.5f;//x�y�Ъ��B�i
	private boolean movingFlag = false;//���b�h�����ЧӦ�
	public ModeView(MyActivity activity) {
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
		countDownModeBtn.drawSelf(canvas, paint);
		practiceModeBtn.drawSelf(canvas, paint);
		highScoreBtn.drawSelf(canvas, paint);
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
    		//�I���b���ӫ��s�W�}�ҭ��ӫ��s
    		if(countDownModeBtn.isActionOnButton(x, y) && countDownModeBtn.isDown())
    		{
    			countDownModeBtn.releaseUp();
    			//�Хܬ��˭p�ɼҦ�
    			Constant.mode = GameMode.countDown;
    			activity.sendMessage(WhatMessage.GAME_VIEW);
    		}
    		else if(practiceModeBtn.isActionOnButton(x, y) && practiceModeBtn.isDown())
    		{
    			practiceModeBtn.releaseUp();
    			//�Хܬ��m�߼Ҧ�
    			Constant.mode = GameMode.practice;
    			activity.sendMessage(WhatMessage.GAME_VIEW);
    		}
    		else if(highScoreBtn.isActionOnButton(x, y) && highScoreBtn.isDown())
    		{
    			highScoreBtn.releaseUp();
    			activity.sendMessage(WhatMessage.HIGH_SCORE_VIEW);    			
    		}
    		//��_�������Ҧ����s
    		countDownModeBtn.releaseUp();
    		practiceModeBtn.releaseUp();
    		highScoreBtn.releaseUp();	
    		break;    	
    	}
    	//��ø�ɭ�
		repaint();
		return true;
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
		//�إߵ������s����
		countDownModeBtn=new VirtualButton2D(choiceBmp0,xFrom,170);
		practiceModeBtn=new VirtualButton2D(choiceBmp1,xFrom,170+90);
		highScoreBtn=new VirtualButton2D(choiceBmp2,xFrom,170+2*90);
		
		//�}�Ұ�����A������s�ʵe
		new Thread()
		{
			@Override
			public void run()
			{
				//�Хܤ��iĲ��
				movingFlag = true;
				
				for(float currX=xFrom; currX<=xTo; currX+=xSpan){
					//���ܫ��s����m
					countDownModeBtn.x = currX;
					practiceModeBtn.x = 800 - 243 - currX;
					highScoreBtn.x = currX;
					
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
	//�N�Ϥ����J
	public void initBitmap(){
		choiceBmp0=BitmapFactory.decodeResource(this.getResources(), R.drawable.daojishi);
		choiceBmp1=BitmapFactory.decodeResource(this.getResources(), R.drawable.lianxi);
		choiceBmp2=BitmapFactory.decodeResource(this.getResources(), R.drawable.paihang);
		bgBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
		//�A����
		choiceBmp0=PicLoadUtil.scaleToFitFullScreen(choiceBmp0, Constant.wRatio, Constant.hRatio);
		choiceBmp1=PicLoadUtil.scaleToFitFullScreen(choiceBmp1, Constant.wRatio, Constant.hRatio);
		choiceBmp2=PicLoadUtil.scaleToFitFullScreen(choiceBmp2, Constant.wRatio, Constant.hRatio);
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
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}
