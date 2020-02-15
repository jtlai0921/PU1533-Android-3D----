package com.bn.ball;

import com.bn.ball.util.SQLiteUtil;

public class BoLiPZThread extends Thread{
	WuTiForControl zfcTemp;
	public BoLiPZThread(WuTiForControl zfcTemp)
	{
		this.zfcTemp=zfcTemp;
	}
	public void run()
	{
//		Constant.vk=1;
		while(Constant.bolipzFlag)
		{
			if(Constant.vk>0)
			{ 
				if(Constant.BALL_Z>zfcTemp.zOffset+0.5)
				{
					Constant.vk=1.1f*Constant.vk;
					if(Constant.vk>0.8f){
						 Constant.vk=0.8f;
					}
					
				}else{ 
					Constant.bolipzFlag=false;
					Constant.gameOver=true;
				}
			}else if(Constant.vz<0)
			{
				Constant.vk=0.95f*Constant.vk;
				if(Math.abs(Constant.vk)<0.01f){  
					Constant.vk=-Constant.vk;
				}
			}
			if(Constant.BALL_Z>zfcTemp.zOffset&&Constant.BALL_Z<zfcTemp.zOffset+0.5&&Math.abs(Constant.vz*Constant.vk)<0.02)
			{
				SQLiteUtil.insertTime(Constant.sumBoardScore+10000  );
				Constant.loseFlag=true;
				Constant.vz=0;
				Constant.flag=false;
				Constant.bolipzFlag=false;
				
				System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"+Constant.gameOver);
			}
			try
			{
				sleep(50);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
}
