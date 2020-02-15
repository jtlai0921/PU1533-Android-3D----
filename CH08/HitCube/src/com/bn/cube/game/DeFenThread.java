package com.bn.cube.game;

public class DeFenThread extends Thread{
	
	@Override
	public void run()
	{
		 int count=0;
		while(Constant.deFenThreadFlag)
		{  
			count++;
			if(count<6)
			{
				Constant.denfenAlpha+=0.2;
			}else
			{
				Constant.denfenAlpha=1-0.05f*(count-6);
			}
			if(count>=26)
			{
				Constant.deFenThreadFlag=false;
				Constant.deFenDrawFlag=false;
				Constant.deFenJianFlag=false;
				Constant.deFenJiaFlag=false;
				Constant.denfenAlpha=0;
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
