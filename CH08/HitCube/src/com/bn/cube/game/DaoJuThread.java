package com.bn.cube.game;

public class DaoJuThread extends Thread{
	
	@Override
	public void run()
	{
		int dCount=0;
		while(Constant.daojuThreadFlag)
		{
			
			Constant.daojuY-=0.05f;
			Constant.daojuACount++;
			dCount++;
			if(Constant.daojuACount<=5)
			{
				Constant.daojuAlevel=0;
			}else{
				Constant.daojuAlevel=1;
			}
			if(dCount>40)
			{
				Constant.daojuX=0;
				Constant.daojuY=0;
				Constant.daojuZ=0;
				Constant.drawdaojuFlag=false;
				Constant.daojuThreadFlag=false;
				Constant.daojuAlevel=0;
				Constant.daojuACount=0;
			}
			try{
				sleep(80);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
