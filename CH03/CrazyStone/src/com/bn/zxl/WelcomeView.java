package com.bn.zxl;

import com.bn.util.PicLoadUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import static com.bn.util.Constant.*;

public class WelcomeView extends SurfaceView implements Callback {
	
	MyActivity activity;//activity���Ѧ�
	Paint paint;      //�e��
	int currentAlpha;  //�ثe�����z����
	int sleepSpan=150;      //�ʵe���ɩ�ms
	Bitmap logo;  //logo�Ϥ��Ѧ�
	float currentX;      //�Ϥ���m
	float currentY;
	
	public WelcomeView(MyActivity activity){
		super(activity);
		this.activity=activity;
		this.getHolder().addCallback(this);//�]�w�ͩR�P���^�ձ��f����{��
		paint=new Paint();//�إߵe��
		paint.setAntiAlias(true);//�}�ҧܿ���
		logo=PicLoadUtil.loadBM(getResources(),"androidheli.png" );//���J�Ϥ�
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//ø��¶�R�x�βM�I��
		paint.setColor(Color.BLACK);//�]�w�e���m��
		paint.setAlpha(255);//�]�w���z���׬�255
		canvas.drawRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, paint);
		//�i�業���K��
		if(logo==null)return;
		paint.setAlpha(currentAlpha);
		canvas.drawBitmap(logo, currentX, currentY, paint);	
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		currentX=SCREEN_WIDTH/2-logo.getWidth()/2;
		currentY=SCREEN_HEIGHT/2-logo.getHeight()/2;
		
		new Thread(){
			public void run() {
				
				SurfaceHolder mholder=WelcomeView.this.getHolder();//���o�^�ձ��f
				for(int i=255;i>-10;i-=10){
					
					if(i<0)//�Y�G�ثe���z���פp��s
					{
						i=0;//�N���z���׸m���s
					}
					currentAlpha=i;
					Canvas canvas=mholder.lockCanvas();//���o�e��
					try {
						synchronized (mholder) //�P�B
						{
							onDraw(canvas);//�i��ø��ø��
						}
						Thread.sleep(200);
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						if(canvas!=null)
						{
							mholder.unlockCanvasAndPost(canvas);
						}
					}
					
				}
				if(LOAD_ACTIVITY){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				activity.hd.sendEmptyMessage(1);
			}
		}.start();

	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
