package com.bn.ball;

public class DaoJuThread extends Thread{

	@Override
	public void run()
	{
		while(Constant.daojuZFlag)
		{
			Constant.change+=0.1;
			if(Constant.change>2)
			{
				Constant.daojuZFlag=false;
				Constant.daojuFlag=false;
				Constant.change=0;
				Constant.daoJuZ=0;
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
