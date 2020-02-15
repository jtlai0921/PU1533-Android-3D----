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

	static Bitmap[] bitmap;//logo圖片陣列
	static boolean loadFlag=false;//是否載入圖片的標志位
	static boolean cycle_flag;//執行緒循環工作標志位
	static boolean isYinxiao=true;//是否開啟音效的標志位
	static boolean isbsqz=false;//勾選白色棋子的標誌位
	Paint paint;
	GJXQActivity activity;
	ScreenScaleResult ssr;
	
	float x;
	float y;
	public SetView(GJXQActivity activity) {
		super(activity);
		this.activity=activity;
		ssr=ScreenScaleUtil.calScale(screenWidth, screenHeight);
		
		this.loadBitmap();//載入圖片
		
		this.getHolder().addCallback(this);
		paint=new Paint();
		paint.setAntiAlias(true);
	}

	
	//將圖片載入進記憶體的方法
	public void loadBitmap()  
	{
		if(loadFlag)//若載入圖片標志位為true，則直接return
		{
			return;
		}
		loadFlag=true;  
		bitmap=new Bitmap[8];
		//載入圖片
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
			{//點擊黑色棋子
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
			{//點擊白色棋子
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
		
		//繪制黑填充矩形清背景
		paint.setColor(Color.BLACK);//設定畫筆彩色
		paint.setAlpha(255);
		canvas.drawRect(0, 0, screenWidthStandard, screenHeightStandard, paint);
		
		//進行平面貼圖
		canvas.drawBitmap(bitmap[0], 0, 0, paint);

		paint.setTextSize(40);
		canvas.drawText("選取棋子彩色：", 190, 255, paint);
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
		
		
		
		canvas.drawText("      音   效       ：", 190, 380, paint);
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
