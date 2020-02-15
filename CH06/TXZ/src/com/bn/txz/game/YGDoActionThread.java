package com.bn.txz.game;

import static com.bn.txz.Constant.YAOGAN_CENTER_X;
import static com.bn.txz.Constant.YAOGAN_CENTER_Y;
import static com.bn.txz.Constant.YAOGAN_R;
import static com.bn.txz.game.TXZGameSurfaceView.vAngle;

import java.util.Queue;

import com.bn.txz.Constant;

public class YGDoActionThread extends Thread{
	public boolean workFlag=true;		//執行緒是否循環工作標志位
    TXZGameSurfaceView gsv;				//游戲View參考
    Queue<Action> aq;
  //搖桿中心圓點的偏移量
    public static float scale_x=0;
    public static float scale_y=0;
    
    public YGDoActionThread(TXZGameSurfaceView gsv)
    {
    	this.gsv=gsv;		//游戲View
    	this.aq=gsv.aqyg;		//動作佇列
    }

	@Override
	public void run() {
		while(workFlag)
		{
			Action ac=null;					//動作參考
    		synchronized(gsv.aqygLock)		//動作佇列鎖
    		{
    			ac=aq.poll();				//從動作佇列中取出一個動作，若佇列中沒有操控動作則取出null
    		}
    		if(ac!=null)
    		{
    			switch(ac.at)
    			{
    			case YAOGAN_MOVE:
     			   float x;
     			   float y;
     			   
     			   if(Math.sqrt((ac.data[0]-Constant.YAOGAN_CENTER_X)*(ac.data[0]-Constant.YAOGAN_CENTER_X)+
                 		   (ac.data[1]-Constant.YAOGAN_CENTER_Y)*(ac.data[1]-Constant.YAOGAN_CENTER_Y))<Constant.YAOGAN_R)
     			   {
     				   x=ac.data[0];
         			   y=ac.data[1];
     			   }
     			   else
     			   {
     				 //目前點座標到搖桿中心點的距離
					   float tempR=(float) Math.sqrt((ac.data[0]-YAOGAN_CENTER_X)*(ac.data[0]-YAOGAN_CENTER_X)+(YAOGAN_CENTER_Y-ac.data[1])*(YAOGAN_CENTER_Y-ac.data[1]));
					   //將目前點座標強制轉為搖桿觸控範圍的邊緣座標
					   x=YAOGAN_CENTER_X+(ac.data[0]-YAOGAN_CENTER_X)*YAOGAN_R/tempR;
					   y=YAOGAN_CENTER_Y+(ac.data[1]-YAOGAN_CENTER_Y)*YAOGAN_R/tempR;
     			   }
	    			 //搖桿中心圓點的x軸偏移量
					scale_x=(x-YAOGAN_CENTER_X)/YAOGAN_R;
					//搖桿中心圓點的y軸偏移量
					scale_y=(YAOGAN_CENTER_Y-y)/YAOGAN_R;
     			   TXZGameSurfaceView.offsetx=scale_x;
     			   TXZGameSurfaceView.offsety=scale_y;
     			   if(y<YAOGAN_CENTER_Y)
   				   	{
   				   		//計算搖桿的偏轉角度
   					   	vAngle=-(float)Math.toDegrees( Math.asin((x-YAOGAN_CENTER_X)/Math.sqrt((x-YAOGAN_CENTER_X)*(x-YAOGAN_CENTER_X)+(y-YAOGAN_CENTER_Y)*(y-YAOGAN_CENTER_Y))));
   				   	}
   					if(y>YAOGAN_CENTER_Y)
   				   	{
   				   		vAngle=-180+(float)Math.toDegrees( Math.asin((x-YAOGAN_CENTER_X)/Math.sqrt((x-YAOGAN_CENTER_X)*(x-YAOGAN_CENTER_X)+(y-YAOGAN_CENTER_Y)*(y-YAOGAN_CENTER_Y))));
   				   	}
	   			break;
    			case ACTION_UP:
     			   TXZGameSurfaceView.offsetx=0;
     			   TXZGameSurfaceView.offsety=0;
     			   TXZGameSurfaceView.vAngle=100;
     		    break;
    			}
    		}
    		try 
    		{
				Thread.sleep(10);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
    
}
