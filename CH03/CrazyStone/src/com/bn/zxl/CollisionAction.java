package com.bn.zxl;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import static com.bn.util.Constant.*;

public class CollisionAction 
{
	public static void doAction(GameView gameview,Body body1,Body body2,float x,float y,Vec2 angle,Vec2 velocity)
	{
		if(gameview.rainlist.size()==0)
		{
			return;
		}
		if(gameview.ObjectArray.size()!=0)
		{
			return;
		}
		System.out.println("123y");
		for(int i=0;i<gameview.rainlist.size();i++)
		{
			if(gameview.rainlist.get(i).body==body1||gameview.rainlist.get(i).body==body2)
			{
				if(gameview.BallList.get(0).body==body2&&!Level_Fail_Flag)
				{	
					Change_Thread_Flag=false;
					Level_Fail_Flag=true;
					Load_Finish=false;
				}
				if(gameview.BallList.size()>1&&gameview.BallList.get(1).body==body2&&!Level_Fail_Flag)
				{
					Change_Thread_Flag=false;
					Load_Finish=false;
					Level_Fail_Flag=true;
					
				}
			}
		}
		if(gameview.lastRain.size()>0&&!Level_Fail_Flag)
		{
			if(gameview.lastRain.get(0).body==body1||gameview.lastRain.get(0).body==body2)
			{
				if(CurrentLevel==5)
				{
					CurrentLevel=0;
				}
				else
				{
					CurrentLevel=CurrentLevel+1;
					if (CurrentLevel==getPassNum()+1) 
					{
						setPassNum(gameview.activity.sharedUtil, getPassNum()+1);
					}
				}
				Change_Thread_Flag=false;
				Load_Finish=false;
				Level_Fail_Flag=true;
			}
		}
		
	}
}
