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
	  	Bitmap background;			//�I�� 
	    Bitmap button_play;			//�i�J�������s�Ϥ�
	    Bitmap button_soundset;		//���ĳ]�w���s�Ϥ�
	    Bitmap button_help;			//�������U���s�Ϥ�
	    Bitmap button_history;		//����
	    Bitmap button_exit;			//���}�������s�Ϥ�
	public MenuView(RadioBallActivity activity){
		this.actitity=activity;
		initBitmap();
	}
	//�N�Ϥ����J
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
			canvas.drawColor(Color.argb(255, 0, 0, 0));//�M���ù����¦�
			canvas.drawBitmap(background,344,0, null);//�e�I��
			canvas.drawBitmap(button_play,20,100, null);//�}�l���s
			canvas.drawBitmap(button_soundset,20,210, null);//�]�w���s
			canvas.drawBitmap(button_help,20,320, null);//���U���s
			canvas.drawBitmap(button_history, 20,430, null);//���v���s
			canvas.drawBitmap(button_exit,20,540, null);//���}���s
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
				{//�}�l���s
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
