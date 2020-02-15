package com.cw.view;
import com.cw.game.Constant;
import com.cw.game.JengaMeActivity;
import com.cw.game.R;
import com.cw.game.R.drawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
public class SplashScreenView 
extends SurfaceView 
implements SurfaceHolder.Callback   //��{�ͩR�P���^�ձ��f
{
	JengaMeActivity activity;//activity���Ѧ�
	Paint paint;      //�e��
	int currentAlpha=0;  //�ثe�����z����
	int sleepSpan=150;      //�ʵe���ɩ�ms
	Bitmap[] logos=new Bitmap[1];//logo�Ϥ��}�C
	Bitmap currentLogo;  //�ثelogo�Ϥ��Ѧ�
	float currentX;      //�Ϥ���m
	float currentY;
	public SplashScreenView(JengaMeActivity activity)
	{
		super(activity);
		this.activity = activity; 
		this.getHolder().addCallback(this);  //�]�w�ͩR�P���^�ձ��f����{��
		paint = new Paint();  //�إߵe��
		paint.setAntiAlias(true);  //�}�ҧܿ���
		//���J�Ϥ�
		logos[0]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.szombie0);
		//logos[1]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.vision);
	}
	public void onDraw(Canvas canvas)
	{	
		//ø��¶�R�x�βM�I��
		paint.setColor(Color.BLACK);//�]�w�e���m��
		paint.setAlpha(255);//�]�w���z���׬�255
		canvas.drawRect(0, 0, 540, 960, paint);
		//�i�業���K��
		if(currentLogo==null)return;
		paint.setAlpha(currentAlpha);		
		canvas.drawBitmap(currentLogo, currentX, currentY, paint);	
	}
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{
	}
	public void surfaceCreated(SurfaceHolder holder) //�إ߮ɳQ�I�s	
	{	 
		Constant.background0=InPutStreamTobyte.readStream(this,R.drawable.background0);
		Constant.background1=InPutStreamTobyte.readStream(this,R.drawable.background1);
		Constant.background2=InPutStreamTobyte.readStream(this,R.drawable.background2);
		Constant.background3=InPutStreamTobyte.readStream(this,R.drawable.background3);
		Constant.background4=InPutStreamTobyte.readStream(this,R.drawable.background4);
		Constant.background5=InPutStreamTobyte.readStream(this,R.drawable.background5);
		Constant.background6=InPutStreamTobyte.readStream(this,R.drawable.background6);
		Constant.background7=InPutStreamTobyte.readStream(this,R.drawable.background7);
		Constant.background8=InPutStreamTobyte.readStream(this,R.drawable.background8);
		Constant.recordback=InPutStreamTobyte.readStream(this,R.drawable.recordback);
		new Thread()
		{
			public void run()
			{
				for(Bitmap bm:logos)
				{
					currentLogo=bm;//�ثe�Ϥ����Ѧ�
					currentX=540/2-bm.getWidth()/2;//�Ϥ���m
					currentY=960/2-bm.getHeight()/2;
					for(int i=255;i>-10;i=i-10)
					{//�ʺA�ܧ�Ϥ����z���׭Ȩä��_��ø	
						currentAlpha=i;
						if(currentAlpha<0)//�Y�G�ثe���z���פp��s
						{
							currentAlpha=0;//�N���z���׸m���s
						}
						SurfaceHolder myholder=SplashScreenView.this.getHolder();//���o�^�ձ��f
						Canvas canvas = myholder.lockCanvas();//���o�e��
						try{
							synchronized(myholder)//�P�B
							{
								onDraw(canvas);//�i��ø��ø��
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							if(canvas!= null)//�Y�G�ثe�e��������
							{
								myholder.unlockCanvasAndPost(canvas);//����e��
							}
						}
						try
						{
							if(i==255)//�Y�O�s�Ϥ��A�h���ݤ@�|
							{
								Thread.sleep(1000);
							}
							Thread.sleep(sleepSpan);
						}
						catch(Exception e)//�ߥX�ҥ~
						{
							e.printStackTrace();
						}
					}
				}
				activity.hd.sendEmptyMessage(1);//�ǰe�T���A�i�J��D���ɭ�
			}
		}.start();
	}
	public void surfaceDestroyed(SurfaceHolder arg0)
	{//�P���ɳQ�I�s
	}
}
