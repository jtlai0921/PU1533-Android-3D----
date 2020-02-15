package com.bn.ball;

public class BallYThread extends Thread{

	@Override
	public void run()
	{
		int count=0;
		while(Constant.ballYFlag)
		{
			if(count<=30)
			{
				Constant.ballVY-=0.006*count;
				Constant.BALL_Y+=Constant.ballVY*count;
				if(Constant.vk>0||Constant.BALL_Y>Constant.boLi_Height)
				{
					Constant.BALL_Z-=0.2f;
				}
				
			}else
			{
				Constant.ballVY+=0.006*(count-30);
				Constant.BALL_Y-=Constant.ballVY*count;
				if(Constant.vk>0||Constant.BALL_Y>Constant.boLi_Height)
				{
					Constant.BALL_Z-=0.2f;
				}
			}		
			count++;
			if(Constant.BALL_Y<-1.2f)
			{
				Constant.BALL_Y=-1.2f;
				Constant.ballYFlag=false;
				Constant.ballVY=0.18f;
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
