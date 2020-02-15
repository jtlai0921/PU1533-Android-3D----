package tstc.fxq.main;
import tstc.fxq.constants.Constant;
import tstc.fxq.constants.WhatMessage;
import tstc.fxq.utils.PicLoadUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 * ��ܦʯǬ�ު��ʵe�ɭ�
 *
 */
public class WelcomeView extends SurfaceView 
implements SurfaceHolder.Callback  //��{�ͩR�P���^�ձ��f
{
	MyActivity activity;
	Paint paint;//�e��
	int currentAlpha=0;//�ثe�����z����
	
	int screenWidth=Constant.SCREEN_WIDTH;//�ù��e��
	int screenHeight=Constant.SCREEN_HEIGHT;//�ù�����
	int sleepSpan=50;//�ʵe���ɩ�ms
	
	Bitmap[] logos=new Bitmap[1];//logo�Ϥ��}�C
	Bitmap currentLogo;//�ثelogo�Ϥ��Ѧ�
	int currentX;
	int currentY;
	
	public WelcomeView(MyActivity activity) {
		super(activity);
		this.activity = activity;
		this.getHolder().addCallback(this);//�]�w�ͩR�P���^�ձ��f����{��
		paint = new Paint();//�إߵe��
		paint.setAntiAlias(true);//�}�ҧܿ���
		//���J�Ϥ�
		logos[0]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dukeb); 
		
		for(int i=0;i<logos.length;i++){
			logos[i]=PicLoadUtil.scaleToFitFullScreen(logos[i], Constant.wRatio, Constant.hRatio);
		}
	}
	public void onDraw(Canvas canvas){
		//ø��¶�R�x�βM�I��
		paint.setColor(Color.BLACK);//�]�w�e���m��
		paint.setAlpha(255);
		canvas.drawRect(0, 0, screenWidth, screenHeight, paint);
		
		//�i�業���K��
		if(currentLogo==null)return;
		paint.setAlpha(currentAlpha);		
		canvas.drawBitmap(currentLogo, currentX, currentY, paint);	
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}
	public void surfaceCreated(SurfaceHolder holder) {//�إ߮ɳQ�I�s		
		new Thread()
		{
			public void run()
			{
				for(Bitmap bm:logos)
				{
					currentLogo=bm;
					//�p��Ϥ���m
					currentX=screenWidth/2-bm.getWidth()/2;
					currentY=screenHeight/2-bm.getHeight()/2;
					
					for(int i=255;i>-10;i=i-10)
					{//�ʺA�ܧ�Ϥ����z���׭Ȩä��_��ø	
						currentAlpha=i;
						if(currentAlpha<0)
						{
							currentAlpha=0;
						}
						SurfaceHolder myholder=WelcomeView.this.getHolder();
						Canvas canvas = myholder.lockCanvas();//���o�e��
						try{
							synchronized(myholder){
								onDraw(canvas);//ø��
							}
						}
						catch(Exception e){
							e.printStackTrace();
						}
						finally{
							if(canvas != null){
								myholder.unlockCanvasAndPost(canvas);
							}
						}						
						try
						{
							if(i==255)
							{//�Y�O�s�Ϥ��A�h���ݤ@�|
								Thread.sleep(1000);
							}
							Thread.sleep(sleepSpan);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				//�ʵe���񧹲���A�h�D���ɭ�
				activity.sendMessage(WhatMessage.MAIN_MENU_VIEW);
			}
		}.start();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {//�P���ɳQ�I�s

	}
}