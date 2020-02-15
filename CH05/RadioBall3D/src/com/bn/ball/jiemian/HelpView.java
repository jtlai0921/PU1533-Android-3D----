package com.bn.ball.jiemian;

import com.bn.ball.R;
import com.bn.ball.RadioBallActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;


public class HelpView extends MySFView
{
	RadioBallActivity activity;//Activity���Ѧ� 
	Canvas canvas;//�e�����Ѧ�
	Paint paint;//�e�����Ѧ�
	SurfaceHolder holder;//�ꪺ�Ѧ�
	Bitmap[] bg_bitmap;					//�I����
	Bitmap pre_page;					//�W�@�����s�Ϥ�
	Bitmap next_page;					//�U�@�����s�Ϥ�
	Bitmap back;
	
	Bitmap pre_page_press;				//�W�@�����s�Ϥ�
	Bitmap next_page_press;				//�U�@�����s�Ϥ�
	Bitmap back_press;	

	int fullTime=0;//�O���q�}�l��ثe���ɶ�
	long startTime;//�}�l�ɶ�	
	
	private int  next_flag=0;//0��ܤ��h��,-1��ܥ���,1��ܥk��

	private float bg_bitmap_curr_x=0;//�ثe�I���Ϥ������W���I��X�y��
	private float bg_bitmap_curr_y=10;//�ثe�I���Ϥ������W���I��Y�y��

	int page_index=0;//�ثe���U���������ޭ�
	public boolean flag_go=true;
	
	boolean back_flag=false;
	boolean pre_page_flag=false;
	boolean next_page_flag=false;
	
	boolean isHaveNextFlag=true;
	boolean isHavePreFlag=false;
	
	public HelpView(RadioBallActivity activity) {
		this.activity = activity;
		paint = new Paint();	 //�إߵe��
		bg_bitmap=new Bitmap[4];//�إ����U�ɭ��I���Ϥ��}�C����
		paint.setAntiAlias(true);//�}�ҧܿ���
		initBitmap();			//�_�l�ƥΨ쪺�Ϥ��귽
		startTime=System.currentTimeMillis();
		initThread();
	} 
	public void initThread()
	{
		next_flag=0;//0��ܤ��h��,-1��ܥ���,1��ܥk��

		bg_bitmap_curr_x=0;//�ثe�I���Ϥ������W���I��X�y��
		bg_bitmap_curr_y=10;//�ثe�I���Ϥ������W���I��Y�y��
		page_index=0;//�ثe���U���������ޭ�
		flag_go=true;
		
		back_flag=false;
		pre_page_flag=false;
		next_page_flag=false;
		
		isHaveNextFlag=true;
		isHavePreFlag=false;		
		
		new Thread()//�إߤ@�Ӱ�����I�sdoDraw��k
		{
			@Override
			public void run()
			{
				while(flag_go)
				{
					//�P�_�O�����٬O�k��
					if(next_flag==-1)//����
					{
							bg_bitmap_curr_x=-Constant.SCREEN_WIDTH;
							next_flag=0;
							page_index++;
							bg_bitmap_curr_x=0;
							if(page_index==bg_bitmap.length-1)
							{
								isHaveNextFlag=false;
							}
					}
					if(next_flag==1)//�k��
					{
							bg_bitmap_curr_x=Constant.SCREEN_WIDTH;
							page_index--;
							bg_bitmap_curr_x=0;
							if(page_index==0)
							{
								isHavePreFlag=false;
							}
							next_flag=0;
					}
					try
					{  
						Thread.sleep(10);//�������v100�@��
					}
					catch (InterruptedException e)  
					{
					e.printStackTrace();
					}
				}
			}  
		}.start();
	}
	
	//���s�w�qonDraw��k
	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.save();
		canvas.translate(Constant.sx, Constant.sy);
		canvas.scale(Constant.ratio, Constant.ratio);
		canvas.drawColor(Color.argb(255, 0, 0, 0));//�M���ù����¦�
		canvas.drawBitmap(bg_bitmap[page_index],bg_bitmap_curr_x,bg_bitmap_curr_y, null);//�e�ثe�I��
		
		
				
		if(next_flag==-1)
		{
			canvas.drawBitmap(bg_bitmap[page_index+1],0,60, null);//�e�U�@�T�I��
		}
		if(next_flag==1)
		{
			canvas.drawBitmap(bg_bitmap[page_index-1],0,60, null);//�e�U�@�T�I��
		}
		if(isHaveNextFlag==false)
		{
			canvas.drawBitmap(back, 810, 600, paint);//ø��back���s
		}
		if(page_index>0)//�ثe���������ޤj��0
		{
			canvas.drawBitmap(pre_page,44, 600, paint);//ø��W�@�����s
		}
		if(!isHavePreFlag)
		{
			canvas.drawBitmap(back,44, 600, null);//ø��back���s
		}
		if(page_index<bg_bitmap.length-1)//�ثe�������ޭȤp�����U�Ϥ��}�C-1
		{
			canvas.drawBitmap(next_page, 810, 600, paint);//ø��U�@�����s
		}
	}
	//���s�w�qĲ�N�ƥ��k
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		int x=(int)e.getX();//���oĲ���I��XY�y��
		int y=(int)e.getY();
		switch(e.getAction())
		{
		case MotionEvent.ACTION_DOWN://��_�ƥ�
			if(next_flag==0&&x>Constant.BZ_LB_Left&&x<Constant.BZ_LB_Right&&y>Constant.BZ_LB_Top&&y<Constant.BZ_LB_Buttom)
			{
				if(!isHavePreFlag)
				{
					back_flag=true;
				}
				else
				{
					pre_page_flag=true;
				}
			}
			else if(next_flag==0&&x>Constant.BZ_RB_Left&&x<Constant.BZ_RB_Right&&y>Constant.BZ_RB_Top&&y<Constant.BZ_RB_Buttom)
			{
				if(!isHaveNextFlag)
				{
					back_flag=true;
				}
				else
				{
					next_page_flag=true;
				}
			}
			else if(page_index==0&&x>Constant.BZ_RB_Left&x<Constant.BZ_RB_Right&&y>Constant.BZ_RB_Top&&y<Constant.BZ_RB_Buttom)
			{//�Ǧ^���s
    			back_flag=true;
			}else if(page_index==bg_bitmap.length-1&&x>Constant.BZ_LB_Left&&x<Constant.BZ_LB_Right&&y>Constant.BZ_LB_Top&&y<Constant.BZ_LB_Buttom)
			{//�Ǧ^���s
    			back_flag=true;
			}
			break;
		case MotionEvent.ACTION_UP://��_�ƥ� 
			pre_page_flag=false;
			next_page_flag=false;
			back_flag=false;
			if(next_flag==0&&x>Constant.BZ_LB_Left&&x<Constant.BZ_LB_Right&&y>Constant.BZ_LB_Top&&y<Constant.BZ_LB_Buttom)
			{
				if(!isHavePreFlag)
				{
					//�Ǧ^��D���
					flag_go=false;
					activity.hd.sendEmptyMessage(1);
				}
				else
				{
					isHavePreFlag=true;
					isHaveNextFlag=true;
					//�k��   
					next_flag=1;
				}
			}
			else if(next_flag==0&&x>Constant.BZ_RB_Left&&x<Constant.BZ_RB_Right&&y>Constant.BZ_RB_Top&&y<Constant.BZ_RB_Buttom)
			{
				if(!isHaveNextFlag)
				{
					//�Ǧ^�D���
					flag_go=false;
					activity.hd.sendEmptyMessage(1);
				}
				else
				{
					isHaveNextFlag=true;
					isHavePreFlag=true;
					//����
					next_flag=-1;
				}
				
			}
			break;
		}
		return true;
	}
	//�_�l�ƹϤ�����k
	public void initBitmap()
	{
		bg_bitmap[0] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.jiafen);//���U�ɭ��I���Ϥ�
		bg_bitmap[1] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.jianfen);                
		bg_bitmap[2] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.shengli);
		bg_bitmap[3] = BitmapFactory.decodeResource(activity.getResources(), R.drawable.shibai);
		 pre_page=BitmapFactory.decodeResource(activity.getResources(), R.drawable.pre);
		 next_page=BitmapFactory.decodeResource(activity.getResources(), R.drawable.next);
		 back=BitmapFactory.decodeResource(activity.getResources(), R.drawable.back);                   
	}

}
