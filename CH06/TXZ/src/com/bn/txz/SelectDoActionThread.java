package com.bn.txz;

import com.bn.txz.game.Action;
import com.bn.txz.game.ActionGenerator;
import com.bn.txz.game.Robot;

public class SelectDoActionThread extends Thread//機器人動畫
{
    public static int currActionIndex=0;
    public static int currStep=0;
    Action currAction;
    Robot robot;
    
    TXZSelectView select;
    public SelectDoActionThread(Robot robot,TXZSelectView select)
    {
    	this.robot=robot;
    	this.select=select;
    }
    
    public void run()
    {
    	try 
		{
			Thread.sleep(50);
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
    		currAction=ActionGenerator.acselectArray[currActionIndex];
        	while(Constant.SELECT_IS_WHILE)
        	{
        		if(currStep>=currAction.totalStep)
        		{
        			currActionIndex=(currActionIndex+1)%ActionGenerator.acselectArray.length;
        			currAction=ActionGenerator.acselectArray[currActionIndex];
        			currStep=0;
        		}        		
        		//修改主資料
        		for(float[] ad:currAction.Robotdata)
        		{
        			//元件索引
        			int partIndex=(int) ad[0];
        			//動作型態
        			int aType=(int)ad[1]; 
        			//目前步驟值
        			
        			if(aType==0)
        			{//平移
        				float xStart=ad[2];
        				float yStart=ad[3];
        				float zStart=ad[4];
        				
        				float xEnd=ad[5];
        				float yEnd=ad[6];
        				float zEnd=ad[7];
        				
        				float currX=xStart+(xEnd-xStart)*currStep/currAction.totalStep;
        				float currY=yStart+(yEnd-yStart)*currStep/currAction.totalStep;
        				float currZ=zStart+(zEnd-zStart)*currStep/currAction.totalStep;
        				
        				robot.bpArraySelect[partIndex].transtate(currX, currY, currZ);//更新子骨骼在父骨骼座標系中的平移
        			}
        			else if(aType==1)
        			{//旋轉
        				float startAngle=ad[2];
        				float endAngle=ad[3];
        				float currAngle=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
        				float x=ad[4];
        				float y=ad[5];
        				float z=ad[6];
        				robot.bpArraySelect[partIndex].rotate(currAngle, x, y, z);//更新子骨骼在父骨骼座標系中的旋轉
        			}														//更新子骨骼在父骨骼座標系中的旋轉的輔助平移
        		}    
        		currStep++;
        		
        		//將主資料覆制進繪制資料
        		synchronized(select.gdDraw.dataLock)
        		{
        			select.gdMain.copyTo(select.gdDraw);
        		}
        		
        		try 
        		{
    				Thread.sleep(20);
    			} catch (InterruptedException e) 
    			{
    				e.printStackTrace();
    			}
    	}
    }
    
}
