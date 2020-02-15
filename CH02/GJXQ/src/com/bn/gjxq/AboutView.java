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
	static Bitmap[] bitmap;//圖片陣列
	static boolean loadFlag=false;//是否載入圖片的標志位
	GJXQActivity activity;
	Paint paint;
	ScreenScaleResult ssr;
	public AboutView(GJXQActivity activity) {
		super(activity);
		this.activity=activity;  
		ssr=ScreenScaleUtil.calScale(screenWidth, screenHeight);
		this.loadBitmap();//載入圖片
		this.getHolder().addCallback(this);
		paint=new Paint();//新增畫筆
		paint.setAntiAlias(true);//設定抗鋸齒
	}

	//將圖片載入進記憶體的方法
	public void loadBitmap()  
	{
		if(loadFlag)//若載入圖片標志位為true，則直接return
		{
			return;
		}
		loadFlag=true;  
		bitmap=new Bitmap[4];
		//載入圖片
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
		
		//繪制黑填充矩形清背景
		paint.setColor(Color.BLACK);//設定畫筆彩色
		paint.setAlpha(255);
		canvas.drawRect(0, 0, screenWidthStandard, screenHeightStandard, paint);
		//進行平面貼圖
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
