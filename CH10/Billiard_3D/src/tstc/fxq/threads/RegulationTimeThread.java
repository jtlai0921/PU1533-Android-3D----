package tstc.fxq.threads;

import java.util.ArrayList;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;
import tstc.fxq.parts.BallKongZhi;
import android.os.Message;

public class RegulationTimeThread extends Thread{
	public boolean regTimeFlag=true;
	
	boolean flag = true;	//使45秒的警示toast只執行一次的標志位
	
	MySurfaceView mv;
	ArrayList<BallKongZhi> ballAl;//所有球的清單
	
	public RegulationTimeThread(MySurfaceView mv,
			ArrayList<BallKongZhi> ballAl	
		)
	{
		this.mv=mv;
		this.ballAl=ballAl;
		//此建構器被呼叫，設定目前時間為，未擊球的開始時間
		Constant.START_TIME=System.currentTimeMillis();//未擊球的開始時間
	}

	public void run()
	{
		while(regTimeFlag)
		{
			long currTime=System.currentTimeMillis();//取得目前的時間
			
			//45秒未擊球，則警示玩家
			if(((currTime-Constant.START_TIME)/1000>45)&&flag)
			{
    			//建立訊息物件
    			Message msg=new Message();
    			//設定訊息的what值
    			msg.what=Constant.REMINDPLAYER;
    			//傳送訊息，出現逾時犯規的Toast
    			mv.activity.hd.sendMessage(msg);
    			
    			flag = false;
				
			}

			//若果未擊球的時間超過時間間隔60秒，則算犯規，出現Toast
			if((currTime-Constant.START_TIME)/1000>60)
			{
    			//建立訊息物件
    			Message msg=new Message();
    			//設定訊息的what值
    			msg.what=Constant.OVERTIMETOAST;
    			//傳送訊息，出現逾時犯規的Toast
    			mv.activity.hd.sendMessage(msg);
    			
    			Constant.START_TIME=System.currentTimeMillis();//未擊球的開始時間
    			
    			
				//記錄逾時犯規後，目前球的位置
				MoveCameraThread.xFrom = ballAl.get(0).xOffset;
				MoveCameraThread.zFrom = ballAl.get(0).zOffset;
    			
    			//將白球還原到起始的位置
    			Constant.recoverWhiteBall(mv, ballAl);
    			
    			flag = true;
    			
			}
			
			try 
			{ 
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{ 
				e.printStackTrace(); 
			}
		}
		
	}
}

