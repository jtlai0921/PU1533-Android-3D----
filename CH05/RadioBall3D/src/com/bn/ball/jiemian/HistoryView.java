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
	    Bitmap biankuang;		//開
	    Bitmap back;			//傳回
	    Bitmap hengxian;
	    Bitmap maohao;
	    Bitmap number[]=new Bitmap[10];
		boolean timePress=false;
	    boolean gradePress=true;
	    boolean isnoGradePaixu=true;//是否為安分數排序
		int fanggeGeshu=8;//結果個數
		int scoreWidth=25;//數字寬度
		int scoreHeght=20;//數字的高度
	    Vector<Vector<String>> vector;//存放結果集的向量
	public HistoryView(RadioBallActivity activity){
		this.actitity=activity;
		initBitmap();
		String sql_select="select grade,time from paihangbang  order by grade desc limit 0,8;";
    	vector=SQLiteUtil.query(sql_select);//從資料庫中取出對應的資料
    	fanggeGeshu=vector.size();
	}
	//將圖片載入
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
			canvas.drawColor(Color.argb(255, 0, 0, 0));//清除螢幕為黑色
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
			for(int i=0;i<vector.size();i++)//循環繪制排行榜的分數和對應時間
	    	{
				int j=i;
				if(!isnoGradePaixu){
					j=vector.size()-i-1;
				}
				drawRiQi(canvas,vector.get(j).get(1).toString(),//繪制時間
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
    		    	vector=SQLiteUtil.query(sql_select);//從資料庫中取出對應的資料
    				isnoGradePaixu=false;
				}else if(x>Constant.History_Grade_Left&&x<Constant.History_Grade_Right&&y>Constant.History_Grade_Top&&y<Constant.History_Grade_Buttom)
				{
					timePress=true;
        			gradePress=false;
        			isnoGradePaixu=true;
 				   String sql_select="select grade,time from paihangbang  order by grade desc limit 0,8;";
 		    	   vector=SQLiteUtil.query(sql_select);//從資料庫中取出對應的資料
				}
				break;
			}
			}
			return true;
		}
		
		public void drawScoreStr(Canvas canvas,String s,int width,int height)//繪制數字字串方法
		{
	    	//繪制得分
	    	String scoreStr=s; 
	    	for(int i=0;i<scoreStr.length();i++){//循環繪制得分
	    		int tempScore=scoreStr.charAt(i)-'0';
	    		canvas.drawBitmap(number[tempScore], width+i*scoreWidth,height, null);
	    		}
		}
		public void drawRiQi(Canvas canvas,String s,int width,int height)//畫年月
		{
			String ss[]=s.split("-");//切割得到年月日
			for(int i=1;i<ss.length;i++){
				if(ss[ss.length-i].length()<2){
					ss[ss.length-i]="0"+ss[ss.length-i];
				}
				drawScoreStr(canvas,ss[ss.length-i],width+scoreWidth*((ss.length-i-1)*3+0),height);//畫年數字
				if(i<3){
					canvas.drawBitmap(maohao,width+scoreWidth*((ss.length-i-1)*3-1),height, null);//畫冒號
				}
				else if(i==4){
					canvas.drawBitmap(hengxian,width+scoreWidth*((ss.length-i-1)*3-1),height, null);//畫橫線
				}
				
			}
		}  
}
