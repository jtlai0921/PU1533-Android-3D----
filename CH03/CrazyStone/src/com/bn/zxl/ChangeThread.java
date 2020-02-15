package com.bn.zxl;
import static com.bn.util.Constant.*;
public class ChangeThread extends Thread
{
	GameView gameview;
	public ChangeThread(GameView gameview)
	{
		this.gameview=gameview;
	}
	
	@Override
	public void run()
	{
		while(DRAW_THREAD_FLAG)
		{
			if(Load_Finish)
			{
				if(Array_Null_Flag)
				{
					if(gameview.ObjectArray.size()==0)
					{
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Change_Thread_Flag=true;
						Array_Null_Flag=false;
						
					}
				}
				if(Change_Thread_Flag&&!RESTART)
				{
					System.out.println("2349482938");
					if(Cloud_Position>860)
					{
						Change_Thread_Flag=false;
					}
					else
					{
						Cloud_Position=Cloud_Position+10;
					}
				}
			}
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
}
