package com.bn.ball.jiemian;


import com.bn.ball.R;
import com.bn.ball.RadioBallActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback   //��{�ͩR�P���^�ձ��f
{
	static Bitmap[] logos;//logo�Ϥ��}�C
	static boolean loadFlag=false;//�O�_���J�Ϥ����ЧӦ�
	
	RadioBallActivity activity;	//activity���Ѧ�
	
	Bitmap currentLogo;		//�ثelogo�Ϥ��Ѧ�
	
	Paint paint;      		//�e��
	int currentAlpha=0; 	//�ثe�����z����
	int sleepSpan=50;       //�ʵe���ɩ�ms
	float currentX;         //�Ϥ���m
	float currentY;
	public WelcomeView(RadioBallActivity activity)
	{
		super(activity);
		this.activity = activity; 

		this.loadBitmap();//���J�Ϥ�
		
		this.getHolder().addCallback(this);  //�]�w�ͩR�P���^�ձ��f����{��
		paint = new Paint();  //�إߵe��
		paint.setAntiAlias(true);  //�}�ҧܿ���
	}
	  
	//�N�Ϥ����J�i�O���骺��k
	public void loadBitmap()  
	{
		
		if(loadFlag)//�Y���J�Ϥ��ЧӦ쬰true�A�h����return
		{
			return;
		}
		loadFlag=true;  
		logos=new Bitmap[2];
		//���J�Ϥ�
		logos[0]=BitmapFactory.decodeResource(this.getResources(),R.drawable.bnkj);  
		logos[1]=BitmapFactory.decodeResource(this.getResources(),R.drawable.hlzd);  
	}
	
	//���s�w�qonDraw��k   
	public void onDraw(Canvas canvas){	
		
		canvas.save();
		canvas.translate(Constant.sx, Constant.sy);
		canvas.scale(Constant.ratio, Constant.ratio);
		//ø��¶�R�x�βM�I��
		paint.setColor(Color.BLACK);//�]�w�e���m��
		paint.setAlpha(255);
		canvas.drawRect(0, 0,1024, 720, paint);
		
		//�i�業���K��
		if(currentLogo==null)return;
		paint.setAlpha(currentAlpha);	//�]�w�ثe���z����	
		canvas.drawBitmap(currentLogo, 12, 220, paint);//ø��ثe��logo	
	}
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{//�e�����ܮɩI�s
	}
	public void surfaceCreated(SurfaceHolder holder) //�إ߮ɳQ�I�s	
	{	
		new Thread()
		{
			public void run()
			{
				for(int j=0;j<logos.length;j++)//�`���C�Ӱ{�{���Ϥ�
				{
					Bitmap bm=logos[j];
					currentLogo=bm;  
					//�p��Ϥ���m
					currentX=Constant.SCREEN_WIDTH/2-bm.getWidth()/2;
					currentY=Constant.SCREEN_HEIGHT/2-bm.getHeight()/2;					
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
				activity.hd.sendEmptyMessage(1);//�{�{�����h�VActivity�ǰe�T��
			}
		}.start();
}
	public void surfaceDestroyed(SurfaceHolder arg0)
	{//�P���ɳQ�I�s
	}
}