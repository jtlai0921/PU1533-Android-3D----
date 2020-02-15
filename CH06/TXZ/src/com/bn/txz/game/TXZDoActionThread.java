package com.bn.txz.game;

import java.util.Queue;
import com.bn.txz.Constant;
import com.bn.txz.manager.SoundUtil;

import static com.bn.txz.Constant.*;
import static com.bn.txz.game.TXZGameSurfaceView.*;
import static com.bn.txz.game.GameStaticData.UNIT_SIZE;

//執行動作佇列的執行緒
public class TXZDoActionThread extends Thread
{
    public boolean workFlag=true;		//執行緒是否循環工作標志位
    TXZGameSurfaceView gsv;				//游戲View參考
    Queue<Action> aq;					//動作佇列
    Robot robot;
    int i;//控制箱子走動動畫的變數
    int row=0;//機器人所在的行列
	int col=0;
	
    
    public TXZDoActionThread(TXZGameSurfaceView gsv)
    {
    	this.gsv=gsv;		//游戲View
    	this.aq=gsv.aq;		//動作佇列
    	this.robot=gsv.robot;
    }
    
    @Override
    public void run()
    {
    	while(workFlag)
    	{    		
    		Action ac=null;					//動作參考
    		synchronized(gsv.aqLock)		//動作佇列鎖
    		{
    			ac=aq.poll();				//從動作佇列中取出一個動作，若佇列中沒有操控動作則取出null
    		}    		
    		if(ac!=null)					//若操控動作參考不是null，即有動作需要執行
    		{
        		switch(ac.at)				//at為操控動作的型態，根據操控型態執行不同的工作
        		{
        		   case CHANGE_CAMERA:		//改變攝影機動作
        			  synchronized(gsv.gdMain.dataLock) 	//將主資料鎖上
        			  {
        				  //將操控動作攜帶的資料給予值給主資料
        				  gsv.gdMain.calculCamare(ac.data[0], ac.data[1]);//data陣列是搬移的XY距離
        				  synchronized(gsv.gdDraw.dataLock) //將繪制資料鎖上
        				  {
        					  //將主資料給予值給繪制資料
        					  gsv.gdDraw.updateCameraData(gsv.gdMain);
        				  }
        			  }
        		   break;
        		   case ROBOT_LEFT://機器人左轉動作
        			  
        			   RobotTurnLeft();
        			    
        		   break;
        		   case ROBOT_RIGHT://機器人右轉動作
        			   RobotTurnRight();
        		   break;
        		   case ROBOT_DOWN://機器人向後轉動作
        			   RobotTurnDown();
        		   break;
        		   case ROBOT_UP://機器人前進動作
        			   gsv.isInAnimation=true;
        			   synchronized(gsv.gqdMain.gqdataLock)
        			   {
        				   if(currDegree==POSITIVE_MOVETO_Z)//若果是z軸正方向
            			   {
            				   row=robot.m;
            				   col=robot.n;
            				   switch(gsv.gqdMain.MAP[GameData.level-1][row+1][col])//判斷下一步的位置是什麼
            				   {
            				   case 0://遇到水
            				   case 5://遇到擺好的木箱
            					   break;//以上情況不能走，所以什麼都不做
            				   case 3://遇到目的,人走，人的下一個位置改為人在的目的
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=6;
            					   }
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=6;
            					   }
            					   
            					   break;
            				   case 6://若果遇到人在目的點上
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;
            					   }
            					   
            					   break;
            				   case 1://遇到橋   
            				   case 4://遇到人
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;
            					   }
            					   
                    			   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
                    				   RobotGo();
                    				   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
                    				   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;
            					   }
            					   break;
            				   case 2://遇到箱子
            					   //判斷箱子的前面是什麼
            					   if(gsv.gqdMain.MAP[GameData.level-1][row+2][col]==0||
            							   gsv.gqdMain.MAP[GameData.level-1][row+2][col]==5||
            							   gsv.gqdMain.MAP[GameData.level-1][row+2][col]==2)
            					   {}//箱子的前面是橋或擺好的木箱,推不動，不做任何動作
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row+2][col]==3)//箱子前面為目的
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//機器人原來的位置為橋
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row+2][col]=5;//目的地繪制推好的箱子
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//機器人原來的位置為橋
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row+2][col]=5;//目的地繪制推好的箱子
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row+2][col]==1)//箱子前面為橋
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//機器人原來的位置為橋
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row+2][col]=2;//目的地繪制箱子
            							   RobotArmDown();
            							   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//機器人原來的位置為橋
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row+2][col]=2;//目的地繪制箱子
            							   RobotArmDown();
            							   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   break;
            				   }
            			   }
            			   else if(currDegree==POSITIVE_MOVETO_X)//若果是x軸正方向
            			   {
            				   row=robot.m;
            				   col=robot.n;
            				   switch(gsv.gqdMain.MAP[GameData.level-1][row][col+1])//判斷下一步的位置是什麼
            				   {
            				   case 0://遇到水
            				   case 5://遇到擺好的木箱
            					   break;//以上情況不能走，所以什麼都不做
            				   case 3://遇到目的,人走，人的下一個位置改為人在的目的
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=6;
            					   }
            					   
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=6;
            					   }
            					   break;
            				   case 6://若果遇到人在目的點上
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4;
            					   }
            					   break;
            				   case 1://遇到橋  
            				   case 4://遇到人
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4; 
            					   }
            					   
                    			   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
                    				   RobotGo();
                    				   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
                    				   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4;
            					   }
            					   break;
            				   case 2://遇到箱子
            					 //判斷箱子的前面是什麼
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col+2]==0||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+2]==5||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+2]==2)
            					   {}//箱子的前面是橋或擺好的木箱,推不動，不做任何動作
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row][col+2]==3)//箱子前面為目的
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//機器人原來的位置為橋
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+2]=5;//目的地繪制推好的箱子
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
//            							   gsv.gqdMain.cdArray=new CompareDis[gsv.gqdMain.boxCount];
                						   RobotArmDown();
                						   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//機器人原來的位置為目的點
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+2]=5;//目的地繪制推好的箱子
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row][col+2]==1)//箱子前面為橋
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//機器人原來的位置為橋
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+2]=2;//目的地繪制箱子
            							   RobotArmDown();
            							   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//機器人原來的位置為橋
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+2]=2;//目的地繪制箱子
            							   RobotArmDown();
            							   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   break;
            				   }
            			   }
            			   else if(currDegree==NEGATIVE_MOVETO_Z)//若果是z軸負方向
            			   {
            				   row=robot.m;
            				   col=robot.n;
            				   switch(gsv.gqdMain.MAP[GameData.level-1][row-1][col])//判斷下一步的位置是什麼
            				   {
            				   case 0://遇到水
            				   case 5://遇到擺好的木箱
            					   break;//以上情況不能走，所以什麼都不做
            				   case 3://遇到目的,人走，人的下一個位置改為人在的目的
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=6;
            					   }
            					   
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=6;
            					   }
            					   break;
            				   case 6://若果遇到人在目的點上
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;
            					   }
            					   
            					   break;
            				   case 1://遇到橋  
            				   case 4://遇到人
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;
            					   }
            					   
                    			   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
                    				   RobotGo();
                    				   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
                    				   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;
            					   }
            					   break;
            				   case 2://遇到箱子
            					 //判斷箱子的前面是什麼
            					   if(gsv.gqdMain.MAP[GameData.level-1][row-2][col]==0||
            							   gsv.gqdMain.MAP[GameData.level-1][row-2][col]==5||
            							   gsv.gqdMain.MAP[GameData.level-1][row-2][col]==2)
            					   {}//箱子的前面是橋或擺好的木箱,推不動，不做任何動作
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row-2][col]==3)//箱子前面為目的
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//機器人原來的位置為橋
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row-2][col]=5;//目的地繪制推好的箱子
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//機器人原來的位置為橋
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row-2][col]=5;//目的地繪制推好的箱子
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row-2][col]==1)//箱子前面為橋
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//機器人原來的位置為橋
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row-2][col]=2;//目的地繪制箱子
            							   RobotArmDown();
            							   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//機器人原來的位置為橋
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row-2][col]=2;//目的地繪制箱子
            							   RobotArmDown();
            							   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   break;
            				   }
            			   }
            			   else if(currDegree==NEGATIVE_MOVETO_X)//若果是x軸負方向
            			   {
            				   row=robot.m;
            				   col=robot.n;
            				   switch(gsv.gqdMain.MAP[GameData.level-1][row][col-1])//判斷下一步的位置是什麼
            				   {
            				   case 0://遇到水
            				   case 5://遇到擺好的木箱
            					   break;//以上情況不能走，所以什麼都不做
            				   case 3://遇到目的,人走，人的下一個位置改為人在的目的
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=6;
            					   }
            					   
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=6;
            					   }
            					   break;
            				   case 6://若果遇到人在目的點上
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;
            					   }
            					   
            					   break;
            				   case 1://遇到橋  
            				   case 4://遇到人
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;
            					   }
            					   
                    			   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
                    				   RobotGo();
                    				   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
                    				   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;
            					   }
            					   break;
            				   case 2://遇到箱子
            					 //判斷箱子的前面是什麼
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col-2]==0||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-2]==5||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-2]==2)
            					   {}//箱子的前面是橋或擺好的木箱,推不動，不做任何動作
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row][col-2]==3)//箱子前面為目的
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//機器人原來的位置為橋
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-2]=5;//目的地繪制推好的箱子
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//機器人原來的位置為橋
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-2]=5;//目的地繪制推好的箱子
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row][col-2]==1)//箱子前面為橋
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//機器人原來的位置為橋
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-2]=2;//目的地繪制箱子
            							   RobotArmDown();
            							   Salute();
            						   }
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//機器人原來的位置為橋
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;//箱子原來的地方繪制人
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-2]=2;//目的地繪制箱子
            							   RobotArmDown();
            							   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   break;
            				   }
            			   }
        				   synchronized(gsv.gqdDraw.gqdataLock)
        				   {
        					  gsv.gqdDraw.boxCount=gsv.gqdMain.boxCount;
        					   for(int i=0;i<gsv.gqdDraw.MAP[GameData.level-1].length;i++)
        					   {
        						   for(int j=0;j<gsv.gqdDraw.MAP[GameData.level-1][0].length;j++)
        						   {
        							   gsv.gqdDraw.MAP[GameData.level-1][i][j]=gsv.gqdMain.MAP[GameData.level-1][i][j];
        						   }
        					   }
        				   }
        				   gsv.isInAnimation=false;
        			   }
        		   break;
        		   case CONVERT://改變角度動作
        			   gsv.viewFlag=!gsv.viewFlag;
        		   break;
        		}
    		}
    		
    		try 
    		{
				Thread.sleep(10);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
    	}
    }
    

    //機器人胳膊抬起的動畫
    public void RobotArmUp()
    {
    	int currStep=0;
	   Action currAction=ActionGenerator.acArrayUp[0];
	   for(int i=0;i<currAction.totalStep;i++)//抬起雙臂的動作
       {
    		//修改主資料
    		for(float[] ad:currAction.Robotdata)
    		{
    			//元件索引
    			int partIndex=(int) ad[0];
    			//動作型態
    			int aType=(int)ad[1]; 
    			//目前步驟值
    			
    			if(aType==1)//旋轉
    			{
    				float startAngle=ad[2];
    				float endAngle=ad[3];
    				float currAngle=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
    				float x=ad[4];
    				float y=ad[5];
    				float z=ad[6];
    				robot.bpArray[partIndex].rotate(currAngle, x, y, z);//更新子骨骼在父骨骼座標系中的旋轉
    			}  														//更新子骨骼在父骨骼座標系中的旋轉的輔助平移
    		}    
    		currStep++;
    		
    		//將主資料覆制進繪制資料
    		synchronized(gsv.gdDraw.dataLock)     
    		{
    			gsv.gdMain.copyTo(gsv.gdDraw);
    		}
    	}
    }

    //機器人胳膊落下的動畫
    public void RobotArmDown()
    {
    	int currStep=0;
	   Action currAction=ActionGenerator.acArrayUp[1];
	   for(int i=0;i<currAction.totalStep;i++)//抬起雙臂的動作
       {
    		//修改主資料
    		for(float[] ad:currAction.Robotdata)
    		{
    			//元件索引
    			int partIndex=(int) ad[0];
    			//動作型態
    			int aType=(int)ad[1]; 
    			//目前步驟值
    			
    			if(aType==1)//旋轉
    			{
    				float startAngle=ad[2];
    				float endAngle=ad[3];
    				float currAngle=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
    				float x=ad[4];
    				float y=ad[5];
    				float z=ad[6];
    				robot.bpArray[partIndex].rotate(currAngle, x, y, z);//更新子骨骼在父骨骼座標系中的旋轉
    			}  														//更新子骨骼在父骨骼座標系中的旋轉的輔助平移
    		}  
    		currStep++;
    		
    		//將主資料覆制進繪制資料
    		synchronized(gsv.gdDraw.dataLock)     
    		{
    			gsv.gdMain.copyTo(gsv.gdDraw);
    		}
       }
    }
    
    //機器人搬移的動畫
    public void RobotGo()
    {
  	      int currStep=0;
		  Action  currAction=ActionGenerator.acArrayUp[2];
		  if(currStep>=currAction.totalStep)
		  {
			  currStep=0;
		  }
		   for(int i=0;i<currAction.totalStep;i++)//前進動作
	       {
	    		//修改主資料
	    		for(float[] ad:currAction.Robotdata)
	    		{
	    			//元件索引
	    			int partIndex=(int) ad[0];
	    			//動作型態
	    			int aType=(int)ad[1]; 
	    			//目前步驟值
	    			
	    			if(aType==0)//平移
	    			{
	    				float positionx=col*UNIT_SIZE-(int)(gsv.gqdMain.MAP[GameData.level-1][0].length/2)*UNIT_SIZE+1f*UNIT_SIZE;
	    				float positionz=row*UNIT_SIZE-(int)(gsv.gqdMain.MAP[GameData.level-1].length/2)*UNIT_SIZE+0.5f*UNIT_SIZE;
	    				float xStart=ad[2]+positionx;
	    				float yStart=ad[3];
	    				float zStart=ad[4]+positionz;
	    				float xEnd = 0;
	    				float yEnd = 0;
	    				float zEnd = 0;
	    				if(currDegree==POSITIVE_MOVETO_Z)//z軸正方向
	    				{
		    				xEnd=xStart;
		    				yEnd=yStart;
		    				zEnd=zStart+UNIT_SIZE;
	    				}
	    				else if(currDegree==POSITIVE_MOVETO_X)//x正方向
	    				{
		    				xEnd=xStart+UNIT_SIZE;
		    				yEnd=yStart;
		    				zEnd=zStart;
	    				}
	    				else if(currDegree==NEGATIVE_MOVETO_Z)//z負方向
	    				{
		    				xEnd=xStart;
		    				yEnd=yStart;
		    				zEnd=zStart-UNIT_SIZE;
	    				}
	    				else if(currDegree==NEGATIVE_MOVETO_X)//x負方向
	    				{
		    				xEnd=xStart-UNIT_SIZE;  
		    				yEnd=yStart;
		    				zEnd=zStart;
	    				}
	    				
	    				currX=xStart+(xEnd-xStart)*currStep/currAction.totalStep;
	    				currY=yStart+(yEnd-yStart)*currStep/currAction.totalStep;
	    				currZ=zStart+(zEnd-zStart)*currStep/currAction.totalStep;
	    				robot.bpArray[partIndex].transtate(currX, currY, currZ);//更新子骨骼在父骨骼座標系中的平移
	    			}													
	    		}    
	    		
	    		if(GuanQiaData.move_flag)
	    		{
	    			switch((int)currDegree)
					{
						case POSITIVE_MOVETO_Z://若果機器人走向為z軸正方向
							GuanQiaData.zoffset=GuanQiaData.zoffset+1f/currAction.totalStep;
							GuanQiaData.move_row=row+1;
							GuanQiaData.move_col=col;
							break;
						case POSITIVE_MOVETO_X://若果機器人走向為X軸正方向
							GuanQiaData.xoffset=GuanQiaData.xoffset+1f/currAction.totalStep;
							GuanQiaData.move_row=row;
							GuanQiaData.move_col=col+1;
							break;
						case NEGATIVE_MOVETO_Z://若果機器人走向為z軸負方向
							GuanQiaData.zoffset=GuanQiaData.zoffset-1f/currAction.totalStep;
							GuanQiaData.move_row=row-1;
							GuanQiaData.move_col=col;
							break;
						case NEGATIVE_MOVETO_X://若果機器人走向為X軸負方向
							GuanQiaData.xoffset=GuanQiaData.xoffset-1f/currAction.totalStep;
							GuanQiaData.move_row=row;
							GuanQiaData.move_col=col-1;
							break;
					}
	    		}

	    		currStep++;
	    		
	    		//將主資料覆制進繪制資料
	    		synchronized(gsv.gdDraw.dataLock)     
	    		{
	    			gsv.gdMain.copyTo(gsv.gdDraw);
	    			Robot.RobotFlag=false; 
	    		}
	       }  
		   
    }
    
    //機器人左轉動畫
    public void RobotTurnLeft()
    {
    	gsv.isInAnimation=true;
    	   int currStep=0;
    	   currDegreeView=0;
		   Action currAction=ActionGenerator.acArrayLeft;
		  
		   for(int i=0;i<currAction.totalStep;i++)
	    	   {
		    		//修改主資料
		    		for(float[] ad:currAction.Robotdata)
		    		{
		    			//元件索引
		    			int partIndex=(int) ad[0];
		    			//動作型態
		    			int aType=(int)ad[1]; 
		    			//目前步驟值
		    			
		    			if(aType==1)//旋轉
		    			{
		    				float startAngle=ad[2]+currDegree;
		    				float endAngle=ad[3]+currDegree;
		    				currDegreeView=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
		    				float x=ad[4];
		    				float y=ad[5];
		    				float z=ad[6];
		    				robot.bpArray[partIndex].rotate(currDegreeView, x, y, z);//更新子骨骼在父骨骼座標系中的旋轉
		    			}														//更新子骨骼在父骨骼座標系中的旋轉的輔助平移
		    		}    
		    		currStep++;
		    		
		    		//將主資料覆制進繪制資料
		    		synchronized(gsv.gdDraw.dataLock)
		    		{
		    			gsv.gdMain.copyTo(gsv.gdDraw);
		    		}
		    	 }
		    currDegree=(currDegree+90)%360;
		    gsv.isInAnimation=false;
    }

    //機器人右轉動畫
    public void RobotTurnRight()
    {
    	gsv.isInAnimation=true;
    	int currStep=0;
    	currDegreeView=0;
		Action  currAction=ActionGenerator.acArrayRight;
		    for(int i=0;i<currAction.totalStep;i++)
	    	{
	    		//修改主資料
	    		for(float[] ad:currAction.Robotdata)
	    		{
	    			//元件索引
	    			int partIndex=(int) ad[0];
	    			//動作型態
	    			int aType=(int)ad[1]; 
	    			//目前步驟值
	    			
	    			if(aType==1)//旋轉
	    			{
	    				float startAngle=ad[2]+currDegree;
	    				float endAngle=ad[3]+currDegree;
	    				currDegreeView=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
	    				float x=ad[4];
	    				float y=ad[5];
	    				float z=ad[6];
	    				robot.bpArray[partIndex].rotate(currDegreeView, x, y, z);//更新子骨骼在父骨骼座標系中的旋轉
	    			}  														//更新子骨骼在父骨骼座標系中的旋轉的輔助平移
	    		}    
	    		currStep++;
	    		
	    		//將主資料覆制進繪制資料
	    		synchronized(gsv.gdDraw.dataLock)     
	    		{
	    			gsv.gdMain.copyTo(gsv.gdDraw);
	    		}
	    	}
		    if(currDegree-90<0)
		    {
		    	currDegree=(currDegree-90)%360+360;
		    	CURR_DIRECTION=(CURR_DIRECTION+270)%360+360;//修改朝向
		    }
		    else
		    {
		    	currDegree=(currDegree-90)%360;
		    	CURR_DIRECTION=(CURR_DIRECTION+270)%360;//修改朝向
		    }
		    gsv.isInAnimation=false;
    }

    //機器人後傳動畫
    public void RobotTurnDown()
    {
    	gsv.isInAnimation=true;
    	int currStep=0;
        currDegreeView=0;
		Action currAction=ActionGenerator.acArrayDown;
		    for(int i=0;i<currAction.totalStep;i++)
	    	{
	    		//修改主資料
	    		for(float[] ad:currAction.Robotdata)
	    		{
	    			//元件索引
	    			int partIndex=(int) ad[0];
	    			//動作型態
	    			int aType=(int)ad[1]; 
	    			//目前步驟值
	    			
	    			if(aType==1)//旋轉
	    			{
	    				float startAngle=ad[2]+currDegree;
	    				float endAngle=ad[3]+currDegree;
	    				currDegreeView=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
	    				float x=ad[4];
	    				float y=ad[5];
	    				float z=ad[6];
	    				robot.bpArray[partIndex].rotate(currDegreeView, x, y, z);//更新子骨骼在父骨骼座標系中的旋轉
	    			}  														//更新子骨骼在父骨骼座標系中的旋轉的輔助平移
	    		}  
	    		currStep++;
	    		
	    		//將主資料覆制進繪制資料
	    		synchronized(gsv.gdDraw.dataLock)     
	    		{
	    			gsv.gdMain.copyTo(gsv.gdDraw);
	    		}
	    	}
		    currDegree=(currDegree+180)%360;
		    gsv.isInAnimation=false;
    }
    
    //機器人敬禮的動畫
    public void RobotSalute()
    {
    	Constant.IS_DRAW_WIN=false;
    	for(int i=0;i<ActionGenerator.acArraySalute.length;i++)
    	{
        	int currStep=0;
    		Action currAction=ActionGenerator.acArraySalute[i];
    		for(int j=0;j<currAction.totalStep;j++)
    		{
    			//修改主資料
	    		for(float[] ad:currAction.Robotdata)
	    		{
	    			//元件索引
	    			int partIndex=(int) ad[0];
	    			//動作型態
	    			int aType=(int)ad[1]; 
	    			//目前步驟值
	    			if(aType==1)//旋轉
	    			{
	    				float startAngle=ad[2];
	    				float endAngle=ad[3];
	    				float currAngle=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
	    				float x=ad[4];
	    				float y=ad[5];
	    				float z=ad[6];
	    				robot.bpArray[partIndex].rotate(currAngle, x, y, z);//更新子骨骼在父骨骼座標系中的旋轉
	    			}														//更新子骨骼在父骨骼座標系中的旋轉的輔助平移
	    		}    
	    		currStep++;
	    		
	    		//將主資料覆制進繪制資料
	    		synchronized(gsv.gdDraw.dataLock)
	    		{
	    			gsv.gdMain.copyTo(gsv.gdDraw);
	    		}
    		}
    	}
    	Constant.IS_DRAW_WIN=true;
    }

    //推完箱子後轉成正面，然後敬禮，再轉回原來的位置
    public void Salute()
    {
    	gsv.isInAnimation=true;
    	if(currDegree==POSITIVE_MOVETO_Z)//0度
    	{
    		RobotTurn(currDegree,-(gsv.gdDraw.direction%360+185.64499f));
    		RobotSalute();
    		RobotTurn(-(gsv.gdDraw.direction%360+185.64499f),currDegree,gsv.isDrawWinOrLose);
    	}
    	else if(currDegree==POSITIVE_MOVETO_X){//若果是x軸正方向
    		RobotTurn(currDegree,-(gsv.gdDraw.direction%360+185.64499f));
    		RobotSalute();
    		RobotTurn(-(gsv.gdDraw.direction%360+185.64499f),currDegree,gsv.isDrawWinOrLose);
    	}
    	else if(currDegree==NEGATIVE_MOVETO_Z){//若果是z軸負方向
    		RobotTurn(currDegree,-(gsv.gdDraw.direction%360+185.64499f));
    		RobotSalute();
    		RobotTurn(-(gsv.gdDraw.direction%360+185.64499f),currDegree,gsv.isDrawWinOrLose);
    	}
    	else if(currDegree==NEGATIVE_MOVETO_X){//若果是x軸負方向
    		RobotTurn(currDegree,-(gsv.gdDraw.direction%360+185.64499f));
    		RobotSalute();
    		RobotTurn(-(gsv.gdDraw.direction%360+185.64499f),currDegree,gsv.isDrawWinOrLose);
    	}
    }
    
    public void RobotTurn(float curr,float direction)
    {
        Action ac=new Action();
        ac.totalStep=10000;
        ac.Robotdata=new float[][]
		{			
			{1,1,curr,direction,0,1,0}//body的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
		};
        
        int currStep=0;
		    for(int i=0;i<ac.totalStep;i++)
	    	{
	    		//修改主資料
	    		for(float[] ad:ac.Robotdata)
	    		{
	    			//元件索引
	    			int partIndex=(int) ad[0];
	    			//動作型態
	    			int aType=(int)ad[1]; 
	    			//目前步驟值
	    			
	    			if(aType==1)//旋轉
	    			{
	    				float startAngle=ad[2];
	    				float endAngle=ad[3];
	    				float currAngle=startAngle+(endAngle-startAngle)*currStep/ac.totalStep;
	    				float x=ad[4];
	    				float y=ad[5];
	    				float z=ad[6];
	    				robot.bpArray[partIndex].rotate(currAngle, x, y, z);//更新子骨骼在父骨骼座標系中的旋轉
	    			}  														//更新子骨骼在父骨骼座標系中的旋轉的輔助平移
	    		}    
	    		currStep++;
	    		
	    		//將主資料覆制進繪制資料
	    		synchronized(gsv.gdDraw.dataLock)     
	    		{
	    			gsv.gdMain.copyTo(gsv.gdDraw);
	    		}
	    	}
    }
    
    public void RobotTurn(float curr,float direction,boolean isDrawWinOrLose)
    {
        Action ac=new Action();
        ac.totalStep=10000;
        ac.Robotdata=new float[][]
		{			
			{1,1,curr,direction,0,1,0}//body的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
		};
        
        int currStep=0;
		    for(int i=0;i<ac.totalStep;i++)
	    	{
	    		//修改主資料
	    		for(float[] ad:ac.Robotdata)
	    		{
	    			//元件索引
	    			int partIndex=(int) ad[0];
	    			//動作型態
	    			int aType=(int)ad[1]; 
	    			//目前步驟值
	    			
	    			if(aType==1)//旋轉
	    			{
	    				float startAngle=ad[2];
	    				float endAngle=ad[3];
	    				float currAngle=startAngle+(endAngle-startAngle)*currStep/ac.totalStep;
	    				float x=ad[4];
	    				float y=ad[5];
	    				float z=ad[6];
	    				robot.bpArray[partIndex].rotate(currAngle, x, y, z);//更新子骨骼在父骨骼座標系中的旋轉
	    			}  														//更新子骨骼在父骨骼座標系中的旋轉的輔助平移
	    		}    
	    		currStep++;
	    		
	    		//將主資料覆制進繪制資料
	    		synchronized(gsv.gdDraw.dataLock)     
	    		{
	    			gsv.gdMain.copyTo(gsv.gdDraw);
	    			
	    		}
	    		gsv.isDrawWinOrLose=true;
	    		gsv.isInAnimation=false;
	    	}
    }

}
