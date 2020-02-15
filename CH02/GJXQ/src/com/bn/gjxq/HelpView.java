package com.bn.gjxq;

import static com.bn.gjxq.Constant.screenHeight;
import static com.bn.gjxq.Constant.screenWidth;

import com.bn.gjxq.manager.ScreenScaleResult;
import com.bn.gjxq.manager.ScreenScaleUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class HelpView extends SurfaceView implements SurfaceHolder.Callback{

	GJXQActivity activity;
	Paint paint;
	
	static Bitmap[] bg_bitmap;//背景圖片陣列
	static Bitmap pre_page;			//上一頁按鈕圖片
	static Bitmap next_page;			//下一頁按鈕圖片
	static Bitmap back;				//傳回按鈕圖片
	static boolean loadFlag=false;//是否載入圖片的標志位
	static boolean cycle_flag;
	ScreenScaleResult ssr;
	
	int page_index=0;//目前幫助背景圖片的索引值
	public HelpView(GJXQActivity activity) {
		super(activity);
		this.activity=activity;
		ssr=ScreenScaleUtil.calScale(screenWidth, screenHeight);
		this.loadBitmap();//起始化圖片
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
		bg_bitmap=new Bitmap[6];
		//載入背景圖片
		bg_bitmap[0]=BitmapFactory.decodeResource(this.getResources(),R.drawable.help_menu); 
		bg_bitmap[1]=BitmapFactory.decodeResource(this.getResources(),R.drawable.help_set); 
		bg_bitmap[2]=BitmapFactory.decodeResource(this.getResources(),R.drawable.youxi1); 
		bg_bitmap[3]=BitmapFactory.decodeResource(this.getResources(),R.drawable.youxi2); 
		bg_bitmap[4]=BitmapFactory.decodeResource(this.getResources(),R.drawable.youxi3); 
		bg_bitmap[5]=BitmapFactory.decodeResource(this.getResources(),R.drawable.youxi4); 
		//載入按鈕圖片
		back=BitmapFactory.decodeResource(this.getResources(),R.drawable.help_back); 
		pre_page=BitmapFactory.decodeResource(this.getResources(),R.drawable.prepage);
		next_page=BitmapFactory.decodeResource(this.getResources(),R.drawable.nextpage);
	}	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x=event.getX();
		float y=event.getY();
		switch(event.getAction())
		{
		    case MotionEvent.ACTION_DOWN:
		    	if((x>=(80+ssr.lucX)*ssr.ratio&&x<=(80+97+ssr.lucX)*ssr.ratio&&y>=(450+ssr.lucY)*ssr.ratio
		    			&&y<=(450+85+ssr.lucY)*ssr.ratio)
		    			&&page_index==0)//若果按下的是左下角的按鈕並且為第一頁，則按下的是傳回按鈕
		    	{
		    		cycle_flag=false;
		    		activity.gotoMenuView();
		    	}
		    	if((x>=(80+ssr.lucX)*ssr.ratio&&x<=(80+97+ssr.lucX)*ssr.ratio
		    			&&y>=(450*ssr.lucY)*ssr.ratio&&y<=(450+85+ssr.lucY)*ssr.ratio)
		    			&&page_index!=0)//若果按下的是左下角的按鈕並且不為為第一頁，則按下的是上一頁按鈕
		    	{
		    		page_index--;
		    	}
		    	//若果按下的是右下角的按鈕並且為最後一頁，則按下的是傳回按鈕
		    	if((x>=(786+ssr.lucX)*ssr.ratio&&x<=(786+97+ssr.lucX)*ssr.ratio
		    			&&y>=(450+ssr.lucY)*ssr.ratio&&y<=(450+85+ssr.lucY)*ssr.ratio)
		    			&&page_index==bg_bitmap.length-1)
		    	{
		    		cycle_flag=false;
		    		activity.gotoMenuView();
		    	}
		    	//若果按下的是右下角的按鈕並且不為最後一頁，則按下的是下一頁按鈕
		    	if((x>=(786+ssr.lucX)*ssr.ratio&&x<=(786+97+ssr.lucX)*ssr.ratio
		    			&&y>=(450+ssr.lucY)*ssr.ratio&&y<=(450+85+ssr.lucY)*ssr.ratio)
		    			&&page_index!=bg_bitmap.length-1)
		    	{
		    		page_index++;
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
		
		canvas.drawColor(Color.BLACK);//清除螢幕為黑色
		canvas.drawBitmap(bg_bitmap[page_index],0,0, null);//畫目前背景
		
		if(page_index==0)//第一頁
		{//左傳回，右下一頁
			canvas.drawBitmap(back, 80, 425, paint);//繪制back按鈕
			canvas.drawBitmap(next_page, 786, 450, paint);//繪制下一頁按鈕
		}
		if(page_index>0&&page_index<bg_bitmap.length-1)
		{//目前的頁面索引大於0且小於圖片陣列長度-1，即不是第一頁也不是最後一頁，左上一頁，右下一頁
			canvas.drawBitmap(pre_page,80, 450, paint);//繪制上一頁按鈕
			canvas.drawBitmap(next_page, 786, 450, paint);//繪制下一頁按鈕
		}
		if(page_index==bg_bitmap.length-1)//最後一頁，左上一頁，右傳回
		{
			canvas.drawBitmap(pre_page,80, 450, paint);//繪制下一頁按鈕
			canvas.drawBitmap(back, 786, 425, paint);//繪制back按鈕
		}
		canvas.restore();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread()
		{
			public void run()
			{
				cycle_flag=true;
				while(cycle_flag){
					SurfaceHolder myholder = HelpView.this.getHolder();
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
