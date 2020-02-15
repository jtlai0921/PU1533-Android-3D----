package tstc.fxq.threads;

import tstc.fxq.constants.Constant;
import tstc.fxq.constants.Sight;
import tstc.fxq.main.MySurfaceView;
import static tstc.fxq.constants.Constant.*;
public class ThreadKey extends Thread{

	MySurfaceView mv;
	public boolean stateFlag=true;
	int tempCount=0;//自動變速計數器
	//表示按鈕狀態的常數
	public static final int leftRotate = 0;
	public static final int rightRotate = 1;
	public static final int nearer = 2;
	public static final int farther = 3;
	public ThreadKey(MySurfaceView mv)
	{
		this.mv=mv;
	}
	
	public void run()
	{
		while(stateFlag)
		{
			//按下左轉按鈕
			if(MySurfaceView.state==leftRotate)
			{
				tempCount++;
				if(tempCount>15)
				{
					mv.cue.setAngle(mv.cue.getAngle()+8*CUE_ROTATE_DEGREE_SPAN);//設定球桿轉動角度
				}
				else
				{
					mv.cue.setAngle(mv.cue.getAngle()+CUE_ROTATE_DEGREE_SPAN);//設定球桿轉動角度
				}
				//改變球桿旋轉角度後，設定攝影機的位置
				mv.setCameraPostion(MySurfaceView.ballAl.get(0).xOffset, MySurfaceView.ballAl.get(0).zOffset);
			}
			//按下右轉按鈕
			else if(MySurfaceView.state==rightRotate)
			{
				tempCount++;				
				if(tempCount>15)
				{
					mv.cue.setAngle(mv.cue.getAngle()-8*CUE_ROTATE_DEGREE_SPAN);//設定球桿轉動角度
				}
				else
				{
					mv.cue.setAngle(mv.cue.getAngle()-CUE_ROTATE_DEGREE_SPAN);//設定球桿轉動角度
				}
				//改變球桿旋轉角度後，設定攝影機的位置
				mv.setCameraPostion(MySurfaceView.ballAl.get(0).xOffset, MySurfaceView.ballAl.get(0).zOffset);
			}
			//按下拉近按鈕
			else if(MySurfaceView.state==nearer)
			{
				//若為自由角度，目前按自由角度的量拉近
				if(mv.currSight == Sight.first){
					mv.currSightDis -= Constant.FIRST_SIGHT_DIS_SPAN;
					//限制角度拉近不得超過一定限度
					if(mv.currSightDis < Constant.FIRST_SIGHT_DIS_MIN){
						mv.currSightDis = Constant.FIRST_SIGHT_DIS_MIN;
					}
				}
				//若為自由角度，目前按自由角度的量拉近
				else if(mv.currSight == Sight.free){
					mv.currSightDis -= Constant.FREE_SIGHT_DIS_SPAN;
					//限制角度拉遠不得超過一定限度
					if(mv.currSightDis < Constant.FREE_SIGHT_DIS_MIN){
						mv.currSightDis = Constant.FREE_SIGHT_DIS_MIN;
					}
				}
				//距離改變後，設定攝影機的位置
				mv.setCameraPostion(MySurfaceView.ballAl.get(0).xOffset, MySurfaceView.ballAl.get(0).zOffset);
			}
			//按下拉遠按鈕
			else if(MySurfaceView.state==farther)
			{
				//若為自由角度，目前按自由角度的量拉遠
				if(mv.currSight == Sight.first){
					mv.currSightDis += Constant.FIRST_SIGHT_DIS_SPAN;
					//限制角度拉遠不得超過一定限度
					if(mv.currSightDis > Constant.FIRST_SIGHT_DIS_MAX){
						mv.currSightDis = Constant.FIRST_SIGHT_DIS_MAX;
					}
				}
				//若為自由角度，目前按自由角度的量拉遠
				else if(mv.currSight == Sight.free){
					mv.currSightDis += Constant.FREE_SIGHT_DIS_SPAN;
					//限制角度拉遠不得超過一定限度
					if(mv.currSightDis > Constant.FREE_SIGHT_DIS_MAX){
						mv.currSightDis = Constant.FREE_SIGHT_DIS_MAX;
					}
				}	
				//距離改變後，設定攝影機的位置
				mv.setCameraPostion(MySurfaceView.ballAl.get(0).xOffset, MySurfaceView.ballAl.get(0).zOffset);
			}
			else{
				tempCount=0;				
			}
			try 
			{ 
				Thread.sleep(40);
			} 
			catch (InterruptedException e) 
			{ 
				e.printStackTrace(); 
			}
		}
		
	}
}

