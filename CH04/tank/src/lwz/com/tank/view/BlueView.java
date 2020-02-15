package lwz.com.tank.view;

import lwz.com.tank.activity.Constant;
import lwz.com.tank.activity.MainActivity;
import lwz.com.tank.util.PicLoadUtil;
import static lwz.com.tank.activity.Constant.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BlueView extends SurfaceView implements SurfaceHolder.Callback
{
    //�ŧiactivity
	MainActivity activity;
	//�ŧiholder
	SurfaceHolder holder;
	//�ŧiBitmap
	Bitmap background;
	Bitmap jionup;
	Bitmap jiondown;
	Bitmap creatup;
	Bitmap creatdown;
	Bitmap discovery;
	Bitmap pair;
	//�ɭ�ø��ЧӦ�
	public static boolean cdraw = true;
	//�[�J�����ЧӦ�
	static boolean jion = false;
	//�إߴ������s�ЧӦ�
	static boolean creat = false;
	//�O�_�}���Ū޼ЧӦ�
	public static boolean blue = false;
	//�Ϥ��Y���
	public static float ratio_width=1;
	public static float ratio_height=1;
	//ø���Y���
	static  float scalX;
	static  float scalY;
	public BlueView(MainActivity activity) {
		super(activity);
		holder = this.getHolder();
		holder.addCallback(this);
		this.activity=activity;
		this.getHolder().addCallback(this);
		scalX=MainActivity.screenWidth;
		scalY=MainActivity.screenHeight;
		initBitmap();
		cdraw = true;
	}
	//Ĳ���ƥ�
	public boolean onTouchEvent(MotionEvent e)
	{
		int currentNum = e.getAction();
		float x = e.getX();
		float y = e.getY();
		switch(currentNum)
		{
			case MotionEvent.ACTION_DOWN:
				if(x>(160+ssr.lucX)*ssr.ratio&&x<(160+ssr.lucX)*ssr.ratio+creatup.getWidth()&&y>300*1&&y<300*1+creatup.getHeight())
				{
					jion=true;
				}
				if(x>(160+ssr.lucX)*ssr.ratio&&x<(160+ssr.lucX)*ssr.ratio+creatup.getWidth()&&y>180*1&&y<180*1+creatup.getHeight())
				{
					creat=true;
				}
				break;
			case MotionEvent.ACTION_UP:
				jion = false;
				creat = false;
				if(x>(560+ssr.lucX)*ssr.ratio&&x<(560+ssr.lucX)*ssr.ratio+creatup.getWidth()&&y>(180+ssr.lucY)*ssr.ratio&&y<(180+ssr.lucY)*ssr.ratio+creatup.getHeight())
				{
					activity.hd.sendEmptyMessage(5);
				}
				if(x>(160+ssr.lucX)*ssr.ratio&&x<(160+ssr.lucX)*ssr.ratio+creatup.getWidth()&&y>(180+ssr.lucY)*ssr.ratio&&y<(180+ssr.lucY)*ssr.ratio+creatup.getHeight())
				{
					cdraw=false;
					activity.hd.sendEmptyMessage(9);
				}
				if(x>(160+ssr.lucX)*ssr.ratio&&x<(160+ssr.lucX)*ssr.ratio+creatup.getWidth()&&y>(300+ssr.lucY)*ssr.ratio&&y<(300+ssr.lucY)*ssr.ratio+creatup.getHeight())
				{
					cdraw=false;
					activity.hd.sendEmptyMessage(8);
					activity.sendMessage("1"+"<#>");
				}
				if(x>(560+ssr.lucX)*ssr.ratio&&x<(560+ssr.lucX)*ssr.ratio+creatup.getWidth()&&y>(300+ssr.lucY)*ssr.ratio&&y<(300+ssr.lucY)*ssr.ratio+creatup.getHeight())
				{
					activity.hd.sendEmptyMessage(4);
				}
				break;
		}
		return true;
	}
	//�_�l��Bitmap��k
	private void initBitmap() {
		background=PicLoadUtil.loadBM(getResources(), "background.png");
		jionup=PicLoadUtil.loadBM(getResources(), "jionup.png");
		jiondown=PicLoadUtil.loadBM(getResources(), "jiondown.png");
		creatup=PicLoadUtil.loadBM(getResources(), "creatup.png");
		creatdown=PicLoadUtil.loadBM(getResources(), "creatdown.png");
		discovery=PicLoadUtil.loadBM(getResources(), "discovery.png");
		pair=PicLoadUtil.loadBM(getResources(), "pair.png");
	}
	//ø������
	class MyThread implements Runnable
	{
		public void run() {
			while(cdraw)
			{
				Paint mpaint = new Paint();
				Canvas canvas = holder.lockCanvas(null);
				canvas.translate(Constant.ssr.lucX, Constant.ssr.lucY);
				canvas.scale(Constant.ssr.ratio, Constant.ssr.ratio); //�I�sscale��k�N�ثe�e���Y���S�w�����
				canvas.drawBitmap(background, 0,0,null);
				canvas.save();
				canvas.restore();
				canvas.drawBitmap(discovery,560*1,180*1, mpaint); 
				canvas.drawBitmap(pair,560*1,300*1, mpaint); 
				if(!jion)
				{
					canvas.drawBitmap(jionup,160*1,300*1, mpaint); 
				}else
				{
					canvas.drawBitmap(jiondown,160*1,300*1, mpaint); 
				}if(!creat)
				{
					canvas.drawBitmap(creatup,160*1,180*1, mpaint); 
				}else
				{
					canvas.drawBitmap(creatdown,160*1,180*1, mpaint); 
				}
				try
				{
					holder.unlockCanvasAndPost(canvas);
				}catch(Exception e)
				{
					
				}
			}
			background.recycle();
			creatup.recycle();
			jionup.recycle();
			jiondown.recycle();
			creatup.recycle();
			creatdown.recycle();
			discovery.recycle();
			pair.recycle();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(new MyThread()).start();
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
}
