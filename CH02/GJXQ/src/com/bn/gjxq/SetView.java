package com.bn.gjxq;

import static com.bn.gjxq.Constant.screenHeight;
import static com.bn.gjxq.Constant.screenWidth;

import com.bn.gjxq.R;
import com.bn.gjxq.manager.ScreenScaleResult;
import com.bn.gjxq.manager.ScreenScaleUtil;
import static com.bn.gjxq.GJXQActivity.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SetView extends SurfaceView implements SurfaceHolder.Callback{

	static Bitmap[] bitmap;//logo�Ϥ��}�C
	static boolean loadFlag=false;//�O�_���J�Ϥ����ЧӦ�
	static boolean cycle_flag;//������`���u�@�ЧӦ�
	static boolean isYinxiao=true;//�O�_�}�ҭ��Ī��ЧӦ�
	static boolean isbsqz=false;//�Ŀ�զ�Ѥl���лx��
	Paint paint;
	GJXQActivity activity;
	ScreenScaleResult ssr;
	
	float x;
	float y;
	public SetView(GJXQActivity activity) {
		super(activity);
		this.activity=activity;
		ssr=ScreenScaleUtil.calScale(screenWidth, screenHeight);
		
		this.loadBitmap();//���J�Ϥ�
		
		this.getHolder().addCallback(this);
		paint=new Paint();
		paint.setAntiAlias(true);
	}

	
	//�N�Ϥ����J�i�O���骺��k
	public void loadBitmap()  
	{
		if(loadFlag)//�Y���J�Ϥ��ЧӦ쬰true�A�h����return
		{
			return;
		}
		loadFlag=true;  
		bitmap=new Bitmap[8];
		//���J�Ϥ�
		bitmap[0]=BitmapFactory.decodeResource(this.getResources(),R.drawable.chessmenu); 
		bitmap[1]=BitmapFactory.decodeResource(this.getResources(),R.drawable.no);
		bitmap[2]=BitmapFactory.decodeResource(this.getResources(),R.drawable.yes);
		bitmap[3]=BitmapFactory.decodeResource(this.getResources(),R.drawable.bsqz);
		bitmap[4]=BitmapFactory.decodeResource(this.getResources(),R.drawable.hsqz);
		bitmap[5]=BitmapFactory.decodeResource(this.getResources(),R.drawable.xzbsqz);
		bitmap[6]=BitmapFactory.decodeResource(this.getResources(),R.drawable.xzhsqz);
		bitmap[7]=BitmapFactory.decodeResource(this.getResources(),R.drawable.back);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		x=event.getX();
		y=event.getY();
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			if(x>=(490+ssr.lucX)*ssr.ratio&&x<=(490+225+ssr.lucX)*ssr.ratio
					&&y>=(180+ssr.lucY)*ssr.ratio&&y<=(180+59+ssr.lucY)*ssr.ratio)
			{//�I���¦�Ѥl
				isbsqz=!isbsqz;
				if(isbsqz)
				{
					Constant.SELECTED_COLOR=1;
				}
				else
				{
					Constant.SELECTED_COLOR=0;
				}
			}
			else if(x>=(490+ssr.lucX)*ssr.ratio&&x<=(490+225+ssr.lucX)*ssr.ratio
					&&y>=(250+ssr.lucY)*ssr.ratio&&y<=(250+59+ssr.lucY)*ssr.ratio)
			{//�I���զ�Ѥl
				isbsqz=!isbsqz;
				if(isbsqz)
				{
					Constant.SELECTED_COLOR=1;
				}
				else
				{
					Constant.SELECTED_COLOR=0;
				}
			}
			else if(x>=(520+ssr.lucX)*ssr.ratio&&x<=(520+136+ssr.lucX)*ssr.ratio
					&&y>=(300+ssr.lucY)*ssr.ratio&&y<=(300+139+ssr.lucY)*ssr.ratio)
			{
				isYinxiao=!isYinxiao;
				Constant.IS_YINXIAO=isYinxiao;
			}
			else if(x>=(750+ssr.lucX)*ssr.ratio&&x<=(750+125+ssr.lucX)*ssr.ratio
					&&y>=(400+ssr.lucY)*ssr.ratio&&y<=(400+59+ssr.lucY)*ssr.ratio)
			{
				activity.gotoMenuView();
			}
			break;
		}
		return true;
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

		paint.setTextSize(40);
		canvas.drawText("����Ѥl�m��G", 190, 255, paint);
		if(isbsqz)
		{
			canvas.drawBitmap(bitmap[5], 490, 250, paint);
			canvas.drawBitmap(bitmap[4], 490, 180, paint);
		}
		else
		{
			canvas.drawBitmap(bitmap[3], 490, 250, paint);
			canvas.drawBitmap(bitmap[6], 490, 180, paint);
		}
		
		
		
		canvas.drawText("      ��   ��       �G", 190, 380, paint);
		if(isYinxiao){
			canvas.drawBitmap(bitmap[2], 520, 300, paint);
		}else{
			canvas.drawBitmap(bitmap[1], 520, 300, paint);
		}
		
		canvas.drawBitmap(bitmap[7], 750, 400, paint);
		
		canvas.restore();
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {}
	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		new Thread()
		{
			public void run()
			{
				cycle_flag=true;
				while(cycle_flag){
					SurfaceHolder myholder = SetView.this.getHolder();
					Canvas canvas = myholder.lockCanvas();
					try {
						synchronized (myholder) {
							onDraw(canvas);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (canvas != null) {
							myholder.unlockCanvasAndPost(canvas);
						}
					}
				}
			}
		}.start();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		cycle_flag=false;
	}
}
