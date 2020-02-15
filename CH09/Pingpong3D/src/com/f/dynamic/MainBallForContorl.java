package com.f.dynamic;

import static com.f.pingpong.Constant.*;

import com.f.mainbody.MainBall;

public class MainBallForContorl extends Thread
{
	MainBall mb = null;
	public MainBallForContorl(MainBall mb)
	{
		this.mb=mb;
	}
	
	public void run()
	{
		while(mb.isMove)
		{
			mb.timeLive.y += mb.time_unit;
			mb.speed.y = mb.speed_start.y + MA.y * mb.timeLive.y;
			float ty = mb.speed_start.y * mb.timeLive.y + 0.5f * MA.y * mb.timeLive.y * mb.timeLive.y;
			mb.position.y = mb.positoin_start.y + ty;
			
			if(mb.position.y <= 2.05)
			{
				mb.positoin_start.y = 2.05f;
				mb.speed_start.y = (-mb.speed.y) * 0.999f;
				mb.timeLive.y = 0.0f;
			}
			try 
			{
				Thread.sleep(20);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
