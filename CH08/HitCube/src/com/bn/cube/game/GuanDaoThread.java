package com.bn.cube.game;

public class GuanDaoThread extends Thread{
	
	@Override
	public void run()
	{
		int i=-1;
		while(Constant.guan_flag)
		{
			if(Constant.move_flag){
			i++;
			if(i>20)
			{
				i=0;
			}
			System.out.print(Constant.currZL);
			Constant.currZL=i;
			}
			try{
				
				sleep(60);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}

}
