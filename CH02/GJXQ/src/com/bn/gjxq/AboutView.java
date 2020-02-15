package com.bn.gjxq;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.bn.gjxq.R;
import com.bn.gjxq.manager.ScreenScaleResult;
import com.bn.gjxq.manager.ScreenScaleUtil;
import static com.bn.gjxq.Constant.*;
import static com.bn.gjxq.GJXQActivity.*;

public class AboutView extends SurfaceView implements SurfaceHolder.Callback
{
	static Bitmap[] bitmap;//�Ϥ��}�C
	static boolean loadFlag=false;//�O�_���J�Ϥ����ЧӦ�
	GJXQActivity activity;
	Paint paint;
	ScreenScaleResult ssr;
	public AboutView(GJXQActivity activity) {
		super(activity);
		this.activity=activity;  
		ssr=ScreenScaleUtil.calScale(screenWidth, screenHeight);
		this.loadBitmap();//���J�Ϥ�
		this.getHolder().addCallback(this);
		paint=new Paint();//�s�W�e��
		paint.setAntiAlias(true);//�]�w�ܿ���
	}

	//�N�Ϥ����J�i�O���骺��k
	public void loadBitmap()  
	{
		if(loadFlag)//�Y���J�Ϥ��ЧӦ쬰true�A�h����return
		{
			return;
		}
		loadFlag=true;  
		bitmap=new Bitmap[4];
		//���J�Ϥ�
		bitmap[0]=BitmapFactory.decodeResource(this.getResources(),R.drawable.chessmenu); 
		bitmap[1]=BitmapFactory.decodeResource(this.getResources(),R.drawable.about_title);
		bitmap[2]=BitmapFactory.decodeResource(this.getResources(),R.drawable.about_write);
		bitmap[3]=BitmapFactory.decodeResource(this.getResources(),R.drawable.about_write_yy);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.save();
		canvas.translate(ssr.lucX,ssr.lucY);
		canvas.scale(ssr.ratio,ssr.ratio); 
		
		//ø��¶�R�x�βM�I��
		paint.setColor(Color.BLACK);//�]�w�e���m��
		paint.setAlpha(255);
		canvas.drawRect(0, 0, screenWidthStandard, screenHeightStandard, paint);
		//�i�業���K��
		canvas.drawBitmap(bitmap[0], 0, 0, paint);
		canvas.drawBitmap(bitmap[1], 280, 180, paint);
		canvas.drawBitmap(bitmap[2], 180, 320, paint);
		canvas.drawBitmap(bitmap[3], 160, 400, paint);
		
		canvas.restore();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas canvas = holder.lockCanvas();
		try {
			synchronized (holder) {
				onDraw(canvas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {}
}
