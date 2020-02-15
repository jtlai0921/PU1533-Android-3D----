package com.bn.cube.game;

public class ShaLouThread extends Thread{

	MySurfaceView mv;
	public ShaLouThread(MySurfaceView mv)
	{
		this.mv=mv;
	}
	@Override
	public void run()
	{
		
		int sleepTime=100;
		while(Constant.shalouFlag)
		{
			if(Constant.shalouAngle>=-180)
			{
				Constant.shalouAngle-=1;
				sleepTime=20;
			}else{
				sleepTime=200;
				Constant.shalouKaiId=1.0f;
				if(Constant.shalouCount<=20)
				{
					Constant.shalouCount++;
				}else{
					Constant.shalouFlag=false;
					Constant.threadTime=15;
					mv.shalouId=mv.shaloupuId;
					Constant.shalouAngle=0.0f;
					Constant.shalouCount=0;
					Constant.shalouKaiId=0;
				}
				
			}
			try{
				
				sleep(sleepTime);
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
