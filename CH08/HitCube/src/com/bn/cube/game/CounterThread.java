package com.bn.cube.game;

public class CounterThread extends Thread
{
	@Override
	public void run()
	{
		Constant.pictureId=3;
		while(Constant.counter_flag)
		{
			try{
				sleep(1000);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			if(Constant.pictureId==0)
			{
				Constant.counter_flag=false; 				
			}else
			{
				Constant.pictureId--;
			}			
		}
	}
}
