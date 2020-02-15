package com.bn.zxl;
import static com.bn.util.Constant.*;

import com.bn.box.Box2DUtil;
public class PhysicsThread extends Thread {
	GameView gv;
	
	public PhysicsThread(GameView gv){
		this.gv=gv;
	}
	
	@Override
	public void run() {
		while (DRAW_THREAD_FLAG) {
			gv.world.step(TIME_STEP, ITERATIONS);
			
			if(RESTART)//注意，未控制完全
			{
				Cloud_Position=100;
				Cloud_Current_Position=100;
				Array_Null_Flag=true;
				Level_Fail_Flag=false;
				LastLevel=CurrentLevel;
					for(int i=0;i<ObjectList[CurrentLevel].length;i++)
			        {
			        	gv.ObjectArray.add(ObjectList[CurrentLevel][i]);
			        }
				
				for(int i=0;i<gv.reclist.size();i++)
				{
					gv.world.destroyBody(gv.reclist.get(i).body);
				}
				gv.reclist.clear();
				for(int i=0;i<gv.rainlist.size();i++)
				{
					gv.world.destroyBody(gv.rainlist.get(i).body);
				}
				gv.rainlist.clear();
				for(int i=0;i<gv.lastRain.size();i++)
				{
					gv.world.destroyBody(gv.lastRain.get(i).body);
				}
				gv.lastRain.clear();
				for(int i=0;i<gv.BallList.size();i++)
				{
					gv.world.destroyBody(gv.BallList.get(i).body);
				}
				gv.BallList.clear();
				
				Level_Data.loadGameData(gv, CurrentLevel);
				
				RESTART=false;
			}
			
			
			if(Add_Flag&&gv.ObjectArray.size()!=0&&Touch_Flag)
			{
				if(gv.ObjectArray.get(gv.ObjectArray.size()-1)=="Rec"&&gv.ObjectArray.size()!=0)
				{
					gv.reclist.add(
							Box2DUtil.creatRec(gv.world, BoxPosition_x, BoxPosition_y, 54f, 38f, false, gv,1)
					);
				}
				else if(gv.ObjectArray.get(gv.ObjectArray.size()-1)=="MuTong"&&gv.ObjectArray.size()!=0)
				{
					gv.reclist.add(
			        		Box2DUtil.creatMuTong(gv.world, BoxPosition_x, BoxPosition_y, 27f, 38f, false, gv)
					);
				}
				else if(gv.ObjectArray.get(gv.ObjectArray.size()-1)=="ChiLun"&&gv.ObjectArray.size()!=0)
				{
					gv.reclist.add(
			        		Box2DUtil.createChiLun(gv.world, BoxPosition_x, BoxPosition_y, 40f,30, false, gv)
					);
				}
				Add_Flag=false;
				Touch_Flag=false;
				Load_Finish=true;
				
				gv.ObjectArray.remove(gv.ObjectArray.size()-1);
			}
			if(Change_Thread_Flag)
			{
				if((Cloud_Position-Cloud_Current_Position)>30)
				{
			        gv.rainlist.add(Box2DUtil.creatTxing(Cloud_Position, 80, new float[][][]{
				    		{{0,0},{24,0},{24,8},{0,8}},
				    		{{8,8},{16,8},{16,24},{8,24}}
				    		}, false, gv.world, gv, 0)
				        );
					gv.rainlist.add(
							Box2DUtil.createBall(gv.world, Cloud_Position+15, 50, 10, false, -1, gv)
					);
					Cloud_Current_Position=Cloud_Position;
				}
			}
			if(!Change_Thread_Flag&&Cloud_Current_Position>100)
			{
				gv.lastRain.add(
						Box2DUtil.creatRain(gv.world, 940, 100, 10, 10, false, gv)
				);
				Cloud_Current_Position=70;
			}
			for(int i=0;i<gv.BallList.size();i++)
			{
				if(gv.BallList.get(i).body.getPosition().x<0||gv.BallList.get(i).body.getPosition().x>960||gv.BallList.get(i).body.getPosition().y>540)
				{
					Change_Thread_Flag=false;
					Load_Finish=false;
					gv.ObjectArray.clear();
					Level_Fail_Flag=true;
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
