package com.bn.cube.view;

public class WaterThread extends Thread{

	public WaterThread()
	{
		
	}
	@Override 
	public void run()
	{
		while(Constant.water_flag)
		{
			Constant.wx-=0.02f;
			if(Constant.wx<-2f)
			{
				Constant.wx=0f;
			}
			try{
				
				sleep(50);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
