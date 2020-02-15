package com.bn.ball.jiemian;

import java.util.Vector;

import com.bn.ball.R;
import com.bn.ball.RadioBallActivity;
import com.bn.ball.util.SQLiteUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
public class HistoryView extends MySFView{
	RadioBallActivity actitity;
	  	Bitmap time;			
	    Bitmap grade;			
	    Bitmap timeh;
	    Bitmap gradeh;
	    Bitmap biankuang;		//�}
	    Bitmap back;			//�Ǧ^
	    Bitmap hengxian;
	    Bitmap maohao;
	    Bitmap number[]=new Bitmap[10];
		boolean timePress=false;
	    boolean gradePress=true;
	    boolean isnoGradePaixu=true;//�O�_���w���ƱƧ�
		int fanggeGeshu=8;//���G�Ӽ�
		int scoreWidth=25;//�Ʀr�e��
		int scoreHeght=20;//�Ʀr������
	    Vector<Vector<String>> vector;//�s�񵲪G�����V�q
	public HistoryView(RadioBallActivity activity){
		this.actitity=activity;
		initBitmap();
		String sql_select="select grade,time from paihangbang  order by grade desc limit 0,8;";
    	vector=SQLiteUtil.query(sql_select);//�q��Ʈw�����X���������
    	fanggeGeshu=vector.size();
	}
	//�N�Ϥ����J
		public void initBitmap()
		{
			time=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.time);
			timeh=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.timeh);
			grade=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.grade); 
			gradeh=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.gradeh);
			biankuang=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.bk);
			back=BitmapFactory.decodeResource(actitity.getResources(), R.drawable.back);
			hengxian=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.hengxian);
			maohao=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.maohao);
			number[0]=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.n0);
			number[1]=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.n1);
			number[2]=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.n2);
			number[3]=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.n3);
			number[4]=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.n4);
			number[5]=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.n5);
			number[6]=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.n6);
			number[7]=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.n7);	
			number[8]=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.n8);
			number[9]=BitmapFactory.decodeResource(actitity.getResources(),R.drawable.n9);
		}
		@Override
		public void onDraw(Canvas canvas) 
		{
			super.onDraw(canvas);
			canvas.save();
			canvas.translate(Constant.sx, Constant.sy);
			canvas.scale(Constant.ratio, Constant.ratio);
			canvas.drawColor(Color.argb(255, 0, 0, 0));//�M���ù����¦�
			if(timePress)
			{
				canvas.drawBitmap(timeh,280,50, null);
			}else
			{
				canvas.drawBitmap(time,280,50, null);
			}
			if(gradePress)
			{
				canvas.drawBitmap(gradeh,660,50, null);
			}else
			{
				canvas.drawBitmap(grade,660,50, null);
			}
			canvas.drawBitmap(biankuang,112,130, null);
			for(int i=0;i<vector.size();i++)//�`��ø��Ʀ�]�����ƩM�����ɶ�
	    	{
				int j=i;
				if(!isnoGradePaixu){
					j=vector.size()-i-1;
				}
				drawRiQi(canvas,vector.get(j).get(1).toString(),//ø��ɶ�
						200,140+73*i);
				drawScoreStr(canvas,vector.get(i).get(0).toString(),
						700,140+73*i);
	    	}
		}
		@Override
		public boolean onTouchEvent(MotionEvent e)
		{
			int x=(int)e.getX();
			int y=(int)e.getY();
			switch(e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
			{
				if(x>Constant.History_Time_Left&&x<Constant.History_Time_Right&&y>Constant.History_Time_Top&&y<Constant.History_Time_Buttom)
				{
					timePress=false;
        			gradePress=true;
        			String sql_select="select grade,time from paihangbang   desc limit 0,8;";
    		    	vector=SQLiteUtil.query(sql_select);//�q��Ʈw�����X���������
    				isnoGradePaixu=false;
				}else if(x>Constant.History_Grade_Left&&x<Constant.History_Grade_Right&&y>Constant.History_Grade_Top&&y<Constant.History_Grade_Buttom)
				{
					timePress=true;
        			gradePress=false;
        			isnoGradePaixu=true;
 				   String sql_select="select grade,time from paihangbang  order by grade desc limit 0,8;";
 		    	   vector=SQLiteUtil.query(sql_select);//�q��Ʈw�����X���������
				}
				break;
			}
			}
			return true;
		}
		
		public void drawScoreStr(Canvas canvas,String s,int width,int height)//ø��Ʀr�r���k
		{
	    	//ø��o��
	    	String scoreStr=s; 
	    	for(int i=0;i<scoreStr.length();i++){//�`��ø��o��
	    		int tempScore=scoreStr.charAt(i)-'0';
	    		canvas.drawBitmap(number[tempScore], width+i*scoreWidth,height, null);
	    		}
		}
		public void drawRiQi(Canvas canvas,String s,int width,int height)//�e�~��
		{
			String ss[]=s.split("-");//���αo��~���
			for(int i=1;i<ss.length;i++){
				if(ss[ss.length-i].length()<2){
					ss[ss.length-i]="0"+ss[ss.length-i];
				}
				drawScoreStr(canvas,ss[ss.length-i],width+scoreWidth*((ss.length-i-1)*3+0),height);//�e�~�Ʀr
				if(i<3){
					canvas.drawBitmap(maohao,width+scoreWidth*((ss.length-i-1)*3-1),height, null);//�e�_��
				}
				else if(i==4){
					canvas.drawBitmap(hengxian,width+scoreWidth*((ss.length-i-1)*3-1),height, null);//�e��u
				}
				
			}
		}  
}
