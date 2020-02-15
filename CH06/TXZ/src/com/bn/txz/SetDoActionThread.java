package com.bn.txz;

import com.bn.txz.game.Action;
import com.bn.txz.game.ActionGenerator;
import com.bn.txz.game.Robot;

public class SetDoActionThread extends Thread//�����H�ʵe
{
    public static int currActionIndex=0;
    public static int currStep=0;
    Action currAction;
    Robot robot;
    
    TXZSetView set;
    public SetDoActionThread(Robot robot,TXZSetView set)
    {
    	this.robot=robot;
    	this.set=set;
    	Constant.status=1;
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
		
		if(Constant.status==1)
    	{
    		currAction=ActionGenerator.acArray[currActionIndex];
        	while(Constant.SET_IS_WHILE)
        	{
        		if(currStep>=currAction.totalStep)
        		{
        			currActionIndex=(currActionIndex+1)%ActionGenerator.acArray.length;
        			currAction=ActionGenerator.acArray[currActionIndex];
        			currStep=0;
        		}        		
        		//�ק�D���
        		for(float[] ad:currAction.Robotdata)
        		{
        			//�������
        			int partIndex=(int) ad[0];
        			//�ʧ@���A
        			int aType=(int)ad[1]; 
        			//�ثe�B�J��
        			
        			if(aType==0)
        			{//����
        				float xStart=ad[2];
        				float yStart=ad[3];
        				float zStart=ad[4];
        				
        				float xEnd=ad[5];
        				float yEnd=ad[6];
        				float zEnd=ad[7];
        				
        				float currX=xStart+(xEnd-xStart)*currStep/currAction.totalStep;
        				float currY=yStart+(yEnd-yStart)*currStep/currAction.totalStep;
        				float currZ=zStart+(zEnd-zStart)*currStep/currAction.totalStep;
        				
        				robot.bpArraySet[partIndex].transtate(currX, currY, currZ);//��s�l���f�b�����f�y�Шt��������
        			}
        			else if(aType==1)
        			{//����
        				float startAngle=ad[2];
        				float endAngle=ad[3];
        				float currAngle=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
        				float x=ad[4];
        				float y=ad[5];
        				float z=ad[6];
        				robot.bpArraySet[partIndex].rotate(currAngle, x, y, z);//��s�l���f�b�����f�y�Шt��������
        			}														//��s�l���f�b�����f�y�Шt�������઺���U����
        		}    
        		currStep++;
        		
        		//�N�D����Ш�iø����
        		synchronized(set.gdDraw.dataLock)
        		{
        			set.gdMain.copyTo(set.gdDraw);
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
    
}