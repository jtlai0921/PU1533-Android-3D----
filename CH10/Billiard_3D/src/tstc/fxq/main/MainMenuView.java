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
 * �D���ɭ�
 *
 */
public class MainMenuView extends SurfaceView implements SurfaceHolder.Callback{
	MyActivity activity;//activity���Ѧ�
	Paint paint;//�e���Ѧ�
	//�������s�Ϥ�
	Bitmap choiceBmp0;
	Bitmap choiceBmp1;
	Bitmap choiceBmp2;
	Bitmap choiceBmp3;
	//�D���W�������s����Ѧ�
	VirtualButton2D startGameBtn;
	VirtualButton2D soundControlBtn;
	VirtualButton2D helpBtn;
	VirtualButton2D aboutBtn;
	//�I���Ϥ�
	Bitmap bgBmp;
	//������s�ʵe���q
	final float xFrom = 140;//x�y�Ъ��_�I
	final float xTo = 220;//x�y�Ъ����I
	final float xSpan = 4.5f;//x�y�Ъ��B�i
	private boolean movingFlag = false;//���b�h�����ЧӦ�
	public MainMenuView(MyActivity activity) {
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
		startGameBtn.drawSelf(canvas, paint);
		soundControlBtn.drawSelf(canvas, paint);
		helpBtn.drawSelf(canvas, paint);
		aboutBtn.drawSelf(canvas, paint);
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
    		if(startGameBtn.isActionOnButton(x, y)){
    			startGameBtn.pressDown();
    		}
    		else if(soundControlBtn.isActionOnButton(x, y)){
    			soundControlBtn.pressDown();
    		}
    		else if(helpBtn.isActionOnButton(x, y)){
    			helpBtn.pressDown();	
    		}
    		else if(aboutBtn.isActionOnButton(x, y)){
    			aboutBtn.pressDown();	
    		}
    		break;
    	case MotionEvent.ACTION_UP: 
    		//�I���b���ӫ��s�W�}�ҭ��ӫ��s
    		if(startGameBtn.isActionOnButton(x, y) && startGameBtn.isDown())
    		{
    			startGameBtn.releaseUp();
    			activity.sendMessage(WhatMessage.CHOICE_VIEW);
    		}
    		else if(soundControlBtn.isActionOnButton(x, y) && soundControlBtn.isDown())
    		{
    			soundControlBtn.releaseUp();
    			activity.sendMessage(WhatMessage.SOUND_CONTORL_VIEW);
    		}
    		else if(helpBtn.isActionOnButton(x, y) && helpBtn.isDown())
    		{
    			helpBtn.releaseUp();
    			activity.sendMessage(WhatMessage.HELP_VIEW);    			
    		}
    		else if(aboutBtn.isActionOnButton(x, y) && aboutBtn.isDown())
    		{
    			aboutBtn.releaseUp();
    			activity.sendMessage(WhatMessage.ABOUT_VIEW);    			
    		}
    		//��_�������Ҧ����s
    		startGameBtn.releaseUp();
    		soundControlBtn.releaseUp();
    		helpBtn.releaseUp();	
    		aboutBtn.releaseUp();	
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
		startGameBtn=new VirtualButton2D(choiceBmp0,xFrom,160);
		soundControlBtn=new VirtualButton2D(choiceBmp1,xFrom,160+80);
		helpBtn=new VirtualButton2D(choiceBmp2,xFrom,160+2*80);
		aboutBtn=new VirtualButton2D(choiceBmp3,xFrom,160+3*80);
		
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
					startGameBtn.x = currX;
					soundControlBtn.x = 800 - 240 - currX;
					helpBtn.x = currX;
					aboutBtn.x = 800 - 240 - currX;
					
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
		choiceBmp0=BitmapFactory.decodeResource(this.getResources(), R.drawable.choice0);
		choiceBmp1=BitmapFactory.decodeResource(this.getResources(), R.drawable.choice1);
		choiceBmp2=BitmapFactory.decodeResource(this.getResources(), R.drawable.choice2);
		choiceBmp3=BitmapFactory.decodeResource(this.getResources(), R.drawable.choice3);
		bgBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
		//�A����
		choiceBmp0=PicLoadUtil.scaleToFitFullScreen(choiceBmp0, Constant.wRatio, Constant.hRatio);
		choiceBmp1=PicLoadUtil.scaleToFitFullScreen(choiceBmp1, Constant.wRatio, Constant.hRatio);
		choiceBmp2=PicLoadUtil.scaleToFitFullScreen(choiceBmp2, Constant.wRatio, Constant.hRatio);
		choiceBmp3=PicLoadUtil.scaleToFitFullScreen(choiceBmp3, Constant.wRatio, Constant.hRatio);
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
