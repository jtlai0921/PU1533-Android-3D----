package com.bn.ball.jiemian;
import com.bn.ball.R;
import com.bn.ball.RadioBallActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
public class MenuView extends MySFView{
	RadioBallActivity actitity;
	  	Bitmap background;			//背景 
	    Bitmap button_play;			//進入游戲按鈕圖片
	    Bitmap button_soundset;		//音效設定按鈕圖片
	    Bitmap button_help;			//游戲幫助按鈕圖片
	    Bitmap button_history;		//游戲
	    Bitmap button_exit;			//離開游戲按鈕圖片
	public MenuView(RadioBallActivity activity){
		this.actitity=activity;
		initBitmap();
	}
	//將圖片載入
		public void initBitmap()
		{
			background=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.bg);                         
			button_play=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.start);                             
			button_soundset=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.set);
			button_help=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.help);
			button_history=BitmapFactory.decodeResource(actitity.getResources(), R.drawable.history);
			button_exit=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.exit);
			
		}
		@Override
		public void onDraw(Canvas canvas) 
		{
			super.onDraw(canvas);
			canvas.save();
			canvas.translate(Constant.sx, Constant.sy);
			canvas.scale(Constant.ratio, Constant.ratio);
			canvas.drawColor(Color.argb(255, 0, 0, 0));//清除螢幕為黑色
			canvas.drawBitmap(background,344,0, null);//畫背景
			canvas.drawBitmap(button_play,20,100, null);//開始按鈕
			canvas.drawBitmap(button_soundset,20,210, null);//設定按鈕
			canvas.drawBitmap(button_help,20,320, null);//幫助按鈕
			canvas.drawBitmap(button_history, 20,430, null);//歷史按鈕
			canvas.drawBitmap(button_exit,20,540, null);//離開按鈕
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent e)
		{
			int x=(int)e.getX();
			int y=(int)e.getY();
			switch(e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				if(x>Constant.Menu_Start_Left&&x<Constant.Menu_Start_Right&&y>Constant.Menu_Start_Top&&y<Constant.Menu_Start_Bottom)
				{//開始按鈕
					actitity.hd.sendEmptyMessage(5);
				}else if(x>Constant.Menu_Set_Left&&x<Constant.Menu_Set_Right&&y>Constant.Menu_Set_Top&&y<Constant.Menu_Set_Bottom)
				{
					actitity.hd.sendEmptyMessage(2);
				}else if(x>Constant.Menu_Help_Left&&x<Constant.Menu_Help_Right&&y>Constant.Menu_Help_Top&&y<Constant.Menu_Help_Bottom)
				{
					actitity.hd.sendEmptyMessage(4);
				}else if(x>Constant.Menu_History_Left&&x<Constant.Menu_History_Right&&y>Constant.Menu_History_Top&&y<Constant.Menu_History_Bottom)
				{
					actitity.hd.sendEmptyMessage(3);
				}else if(x>Constant.Menu_Exit_Left&&x<Constant.Menu_Exit_Right&&y>Constant.Menu_Exit_Top&&y<Constant.Menu_Exit_Bottom)
				{
					System.exit(0);
				}
				break;
			}
			return true;
		}
}
