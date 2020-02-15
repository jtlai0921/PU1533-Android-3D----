package com.bn.gjxq.game;

import java.util.Queue;

import android.os.Bundle;
import android.os.Message;

import com.bn.gjxq.Constant;
import com.bn.gjxq.SoundUtil;
import com.bn.gjxq.manager.ChessRuleUtil;
import com.bn.gjxq.manager.Finish;
import com.bn.gjxq.manager.IntersectantUtil;
import com.bn.gjxq.manager.MatrixUtil;
import com.bn.gjxq.manager.RobotAutoUtil;
import com.bn.gjxq.manager.Vector3f;

//執行動作佇列的執行緒
public class DoActionThread extends Thread
{
    public boolean workFlag=true;		//執行緒是否循環工作標志位
    GameSurfaceView gsv;				//游戲View參考
    Queue<Action> aq;					//動作佇列
    
    public DoActionThread(GameSurfaceView gsv)
    {
    	this.gsv=gsv;		//游戲View
    	this.aq=gsv.aq;		//動作佇列
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
        		   case SELECT_3D:			//拾取動作
        			   synchronized(gsv.gdMain.dataLock) //鎖上主資料鎖
        			   {
        				   //首先檢驗有沒有勾選的棋子
        				   int xzI=-1;
        				   int xzJ=-1;
        				   float disMin=Float.MAX_VALUE;//距離的最小值
        				   
        				   //將拾取動作的資料傳給工具方法計算出AB兩點在世界座標系中的位置
        				   Vector3f[] AB=IntersectantUtil.calculateABPosition
							(
								ac.data[0], //操控動作中的資料，（按下觸控點的xy座標）
								ac.data[1], 
								new float[]
								{
									gsv.gdMain.cx,//攝影機位置
									gsv.gdMain.cy,
									gsv.gdMain.cz,
									gsv.gdMain.tx,//觀察點位置
									gsv.gdMain.ty,
									gsv.gdMain.tz,
									gsv.gdMain.ux,//up向量
									gsv.gdMain.uy,
									gsv.gdMain.uz
								}
							);
        				   
        				   //循環棋碟上的64方塊
        				   for(int i=0;i<8;i++)
        				   {
        						for(int j=0;j<8;j++)
        						{
        							int qzbh=gsv.gdMain.qzwz[i][j];//棋子編號
        							int qzxzzt=gsv.gdMain.qzxz[i][j];//棋子勾選狀態
        							if(qzbh==-1)continue;//若棋子編號是-1（棋子型態中沒有-1型態）
        							//棋子在X與Z軸上的偏移量
        							float xOffset=GameStaticData.XOffsetQZ[i][j];
        							float zOffset=GameStaticData.ZOffsetQZ[i][j];
        							int hbqkTemp=(qzbh>5)?1:0;//判斷棋子的型態，小於5為白子  
        							MatrixUtil.setInitStack();//起始化物體基本變換矩陣currMatrix
        							
        							//若棋子勾選狀態為1（勾選）則沿Y軸上移0.6個單位長度，沒勾選則Y方向不搬移
        							MatrixUtil.translate(xOffset, 0.6f+((qzxzzt==1)?0.6f:0.0f), zOffset);
        							//根據hbqkTemp棋子型態繞著Y軸旋轉特殊的角度
        							MatrixUtil.rotate(GameStaticData.ANGLE[hbqkTemp], 0, 1, 0);
        							
        							//求出在物體座標系中攝影機與拾取射線和棋子包圍盒六個面交點的最短距離
        							float disTemp=IntersectantUtil.calObjectDisMin
        							(
        								new Vector3f(gsv.gdMain.cx,gsv.gdMain.cy,gsv.gdMain.cz),//攝影機位置
        								AB[0], //A點
        								AB[1], //B點
        								MatrixUtil.currMatrix,//物體目前變換矩陣 
        								gsv.qizi[qzbh%6].aabb//棋子的包圍盒
        							);
        							
        							//若求出的距離disTemp小於距離的最小值disMin
        							if(disTemp<disMin)
        							{
        								disMin=disTemp;
        								xzI=i;//將有效的位置給予值給勾選的IJ
        		        				xzJ=j;
        							}
        						}
        				   }
        				   
        				   //有棋子勾選（若勾選的位置是有效的）
        				   if(xzI!=-1&&xzJ!=-1)
        				   {        					  
        					   for(int i=0;i<8;i++)
            				   {
            						for(int j=0;j<8;j++)
            						{
        								if(i==xzI&&j==xzJ&&gsv.gdMain.qzxz[i][j]==0)//若果勾選位置的勾選狀態是未勾選狀態
        								{		
        									if(gsv.gdMain.status==0)
        									{//若目前沒有勾選棋子，則檢查勾選棋子是否為本方彩色
        									 //若符合條件，則將拾取到的棋子設定為勾選的棋子
        									 //並設定標志位
        										if(gsv.gdMain.currColor==gsv.gdMain.getColor(i, j))
        										{//勾選棋子為本方彩色
        											gsv.gdMain.qzxz[i][j]=1;//值棋子勾選為1（勾選）
            										gsv.gdMain.status=1;//改變目前狀態編號為有棋子勾選
            										//播放棋子勾選音效
            										if(Constant.IS_YINXIAO)
            										{
            											SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            										}
        										}
        										else
        										{
        											sendMsgForToast("請勾選己方彩色的棋子！");
        										}
        									}
        									else
        									{//若目前已經有勾選棋子則要看這次勾選的棋子能否被吃掉，
        									   //若能吃掉，則執行走子
        									   int toI=i;//要落子的位置
         									   int toJ=j;
         									   int fromI=0;//起始棋子位置
         									   int fromJ=0;
         									   for(int a=0;a<8;a++)
         	                				   {
         	                						for(int b=0;b<8;b++)//循環棋碟
         	                						{
         	                						   //若棋子勾選為1（勾選狀態），則更新起始棋子的位置
         	                						   if(gsv.gdMain.qzxz[a][b]==1)
         	                						   {
         	                							   fromI=a;
                     									   fromJ=b;
         	                						   }
         	                						}
         	                				   }
         									   boolean canFlag=ChessRuleUtil.chessRule//呼叫走棋規則類別，判斷是否能夠走子
         									   (
         										   gsv.gdMain.qzwz, //棋子位置
         										   new int[]{fromI,fromJ}, //起始棋子位置
         										   new int[]{toI,toJ}//落子位置
         									   );
         									   if(canFlag)
           									   {//若能走子執行走子（吃子）
         										   //將出發位置的棋子勾選狀態設定為0，表示未勾選
           										   gsv.gdMain.qzxz[fromI][fromJ]=0;
           										   //將目前棋子落在勾選的目的位置
           										   gsv.gdMain.qzwz[toI][toJ]=gsv.gdMain.qzwz[fromI][fromJ];
           										   //將棋子位置置為無棋子
           										   gsv.gdMain.qzwz[fromI][fromJ]=GameData.no_qz;
           										   gsv.gdMain.status=0;//置目前狀態編號為0（無棋子勾選）
           										   
           										   //播放吃子音效
           										   if(Constant.IS_YINXIAO)
           										   {
           											   SoundUtil.playSounds(SoundUtil.CHIZI, 0, gsv.activity);
           										   }
           										   
           										   //換下棋方
        										   gsv.gdMain.currColor=(gsv.gdMain.currColor+1)%2;
        										   //讀取是否有輸贏
        										   Finish result=ChessRuleUtil.isFinish(gsv.gdMain.qzwz);
        										   switch(result)
        										   {
        										   case BLACK_WIN://黑方贏了，跳躍到結束界面（判斷是人贏還是機器贏）
      										    	 this.workFlag=false;
      										    	 String tempshow;
      										    	 if(this.gsv.gdMain.humanColor==0)//若果人是黑方
      										    	 {
      										    		 tempshow="恭喜你，你贏了！";
      										    		 sendMsgForDialog(tempshow);
      										    	 }
      										    	 else if(this.gsv.gdMain.humanColor==1)//若果人是白方
      										    	 {
      										    		 tempshow="不好意思，你輸了！";
      										    		 sendMsgForDialog(tempshow);
      										    	 }
      										      break;
      										      case WHITE_WIN://白方贏了，跳躍到結束界面（判斷是人贏還是機器贏）
      										    	 this.workFlag=false;
      										    	 if(this.gsv.gdMain.humanColor==0)//若果人是黑方
      										    	 {
      										    		 tempshow="不好意思，你輸了！";
      										    		 sendMsgForDialog(tempshow);
      										    	 }
      										    	 else if(this.gsv.gdMain.humanColor==1)//若果人是白方
      										    	 {
      										    		 tempshow="恭喜你，你贏了！";
      										    		 sendMsgForDialog(tempshow);
      										    	 }
          										  break;
      										      case NO_FINISH://沒有結束，則機器走子
      										         this.jqzz();
      										      break;          										  
        										   }
           									   }
           									   else
           									   {//若不能走子則更換勾選棋子
	           										if(gsv.gdMain.currColor==gsv.gdMain.getColor(i, j))//勾選棋子為本方彩色
	        										{
	           											gsv.gdMain.qzxz[i][j]=1;//該位置的棋子處於勾選狀態
	         										    gsv.gdMain.status=1;//置目前狀態編號為1（有棋子勾選）
	         										    //播放棋子勾選的音效
	         										    if(Constant.IS_YINXIAO)
	         										    {
	         										    	SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
	         										    }
	        										}
	        										else//勾選棋子不是本方彩色
	        										{
	        											sendMsgForToast("此處違反規則，不可走！");
	        										}
           									   }
        									}
        								}
        								else if(i==xzI&&j==xzJ&&gsv.gdMain.qzxz[i][j]==1)//若果該勾選位置的棋子勾選狀態為勾選狀態則
        								{	//更新主資料中該棋子位置的勾選狀態為未勾選狀態
        									gsv.gdMain.qzxz[i][j]=0;
        									gsv.gdMain.status=0;//置目前狀態編號為0（無棋子勾選）
        								}
        							}
            				   }
        					   
        					   //循環棋碟上所有的位置,將除此輪勾選的棋子位置設定為未勾選
        					   for(int i=0;i<8;i++)
            				   {
            						for(int j=0;j<8;j++)
            						{
            						   if(!(i==xzI&&j==xzJ))//若果目前位置不是勾選位置的I、J
            						   {
            							   gsv.gdMain.qzxz[i][j]=0;//將該棋子位置的勾選狀態為未勾選狀態
            						   }
            						}
            				   }
        				   }
        				   
        				   //若沒有勾選的棋子，再看有沒有勾選的格子
        				   if(xzI==-1||xzJ==-1)
        				   {
            				   xzI=-1;
            				   xzJ=-1;
            				   disMin=Float.MAX_VALUE;
            				   
            				   for(int i=0;i<8;i++)
            				   {
            						for(int j=0;j<8;j++)//循環棋碟上所有的位置
            						{     
            							//棋碟方塊的X與Z軸的偏移量  
            							float xOffset=GameStaticData.XOffset[i][j];
            							float zOffset=GameStaticData.ZOffset[i][j];
            							
            							//起始化物體基本變換矩陣currMatrix
            							MatrixUtil.setInitStack();
            							MatrixUtil.translate(xOffset, 0, zOffset);
            							
            							//求出物體座標系中攝影機與棋碟方塊包圍盒和AB兩點射線交點的最小距離
            							float disTemp=IntersectantUtil.calObjectDisMin
            							(
            								new Vector3f(gsv.gdMain.cx,gsv.gdMain.cy,gsv.gdMain.cz),//攝影機座標
            								AB[0], //A點
            								AB[1], //B點
            								MatrixUtil.currMatrix, //物體目前變換矩陣
            								gsv.block.aabb//棋碟方塊的包圍盒
            							);
            							
            							if(disTemp<disMin)
            							{//若果disTemp小於disMin則更新disMin並更新勾選IJ為目前循環到的i、j
            								disMin=disTemp;
            								xzI=i;
            		        				xzJ=j;
            							}
            						}
            				   }
            				   
            				   if(xzI!=-1&&xzJ!=-1)//棋碟方塊的勾選位置是有效的（沒有勾選棋子但是有勾選的格子）
            				   {
            					   for(int i=0;i<8;i++)
                				   {
                						for(int j=0;j<8;j++)
                						{
                						   if(i!=xzI||j!=xzJ)//若果循環到的位置不是棋碟方塊勾選的位置
                						   {				 //則更新主資料中格子勾選狀態為0（不勾選狀態）
                							   gsv.gdMain.gzxz[i][j]=0;
                						   }
                						   else		//若果循環到的位置是棋碟方塊勾選的位置
                						   {		
                							   if(gsv.gdMain.qzwz[i][j]==-1)//若果勾選的格子位置上沒有棋子
                							   {
                								   if(gsv.gdMain.status==1)//目前狀態編號為1（有棋子勾選）
                								   {//若勾選格子時有勾選的棋子則呼叫規則看能否走棋                									   
                									   int toI=i;//更新要去的棋子位置
                									   int toJ=j;
                									   int fromI=0;//更新起始點的棋子位置
                									   int fromJ=0;
                									   for(int a=0;a<8;a++)
                	                				   {
                	                						for(int b=0;b<8;b++)//循環棋碟，找到棋子的目前位置
                	                						{
                	                						   if(gsv.gdMain.qzxz[a][b]==1)//棋子為勾選狀態
                	                						   {
                	                							   fromI=a;//更新起始點的棋子位置
                            									   fromJ=b;
                	                						   }
                	                						}
                	                				   }
                									   
                									   //呼叫走棋規則類別，判斷是否能夠走子
                									   boolean canFlag=ChessRuleUtil.chessRule
                									   (
                										   gsv.gdMain.qzwz, //棋子位置
                										   new int[]{fromI,fromJ}, //起始棋子位置
                										   new int[]{toI,toJ}//落子位子
                									   );
                									   if(canFlag)//若能走棋則走棋（走子到容許的空白位置）
                									   {
                										   gsv.gdMain.qzxz[fromI][fromJ]=0;//將棋子勾選置為0（即該處無棋子）
                										   //將目前棋子落在勾選的目的位置
                										   gsv.gdMain.qzwz[toI][toJ]=gsv.gdMain.qzwz[fromI][fromJ];
                										   //將起始的棋子位置置為無棋子
                										   gsv.gdMain.qzwz[fromI][fromJ]=GameData.no_qz;
                										   gsv.gdMain.status=0;//置目前狀態編號為無棋子勾選
                										   
                										   //播放普通落子的音效
                										   if(Constant.IS_YINXIAO)
                										   {
                											   SoundUtil.playSounds(SoundUtil.PUTONGLUOZI, 0, gsv.activity);
                										   }
                										   
                										   //換下棋方
                										   gsv.gdMain.currColor=(gsv.gdMain.currColor+1)%2;
                										   //讀取是否有輸贏
                										   Finish result=ChessRuleUtil.isFinish(gsv.gdMain.qzwz);
                										   switch(result)
                										   {
                										   case BLACK_WIN://黑方贏了，跳躍到結束界面（判斷是人贏還是機器贏）
              										    	 this.workFlag=false;
              										    	 String tempshow;
              										    	 if(this.gsv.gdMain.humanColor==0)//若果人是黑方
              										    	 {
              										    		 tempshow="恭喜你，你贏了！";
              										    		 sendMsgForDialog(tempshow);
              										    	 }
              										    	 else if(this.gsv.gdMain.humanColor==1)//若果人是白方
              										    	 {
              										    		 tempshow="不好意思，你輸了！";
              										    		 sendMsgForDialog(tempshow);
              										    	 }
              										      break;
              										      case WHITE_WIN://白方贏了，跳躍到結束界面（判斷是人贏還是機器贏）
              										    	 this.workFlag=false;
              										    	 if(this.gsv.gdMain.humanColor==0)//若果人是黑方
              										    	 {
              										    		 tempshow="不好意思，你輸了！";
              										    		 sendMsgForDialog(tempshow);
              										    	 }
              										    	 else if(this.gsv.gdMain.humanColor==1)//若果人是白方
              										    	 {
              										    		 tempshow="恭喜你，你贏了！";
              										    		 sendMsgForDialog(tempshow);
              										    	 }
                  										  break;
              										      case NO_FINISH://沒有結束，則機器走子
              										         this.jqzz();
              										      break;            	        										  
                										   }
                									   }
                									   else//若不能走棋則顯示提示訊息
                									   {
                										   gsv.gdMain.gzxz[i][j]=1;//格子勾選置為1（勾選狀態）                    									   
                    									   sendMsgForToast("此處違反規則，不可走！");
                									   }
                								   }
                								   else
                								   {                									   
                									   sendMsgForToast("請先勾選棋子！");
                								   }
                							   }
                						   }
                						}
                				   }
            				   }
        				   }
        				   else//若有勾選的棋子則取消所有格子勾選
        				   {
        					   for(int i=0;i<8;i++)
            				   {
            						for(int j=0;j<8;j++)
            						{
            							//更新主資料中格子勾選為0（即該格子沒有勾選）
            							gsv.gdMain.gzxz[i][j]=0;
            						}
            				   }
        				   }
        				   //將新的主資料更新到繪制資料中
        				   synchronized(gsv.gdDraw.dataLock)//鎖上繪制資料鎖
    					   {
    						   for(int i=0;i<8;i++)
            				   {
            						for(int j=0;j<8;j++)
            						{
            							//將主資料copy進繪制資料
            							gsv.gdDraw.qzwz[i][j]=gsv.gdMain.qzwz[i][j];//棋子位置
            							gsv.gdDraw.qzxz[i][j]=gsv.gdMain.qzxz[i][j];//棋子勾選
            							gsv.gdDraw.gzxz[i][j]=gsv.gdMain.gzxz[i][j];//格子勾選
            						}
            				   }
    					   }
        			   }
        		   break;
        		}
    		}
    		
    		try 
    		{
				Thread.sleep(25);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
    	}
    }
    
    //顯示Toast方法
    public void sendMsgForToast(String msgStr)
    {
    	Bundle bundle=new Bundle();
	    bundle.putString("msg",msgStr);
	    Message msg=new Message();
	    msg.what=Constant.COMMAND_TOAST_MSG;
	    msg.setData(bundle);
	    gsv.activity.handler.sendMessage(msg);
    }
    //顯示dialog方法
    public void sendMsgForDialog(String msgStr)
    {
    	Bundle bundle=new Bundle();
	    bundle.putString("msg",msgStr);
	    Message msg=new Message();
	    msg.what=Constant.COMMAND_DIALOG_MSG;
	    msg.setData(bundle);
	    gsv.activity.handler.sendMessage(msg);
    }
    
    //機器根據目前情況走子
    public void jqzz()
    {    	
    	new Thread()
    	{
    		public void run()
    		{
    			try 
    			{
					Thread.sleep(2000);
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
    			synchronized(gsv.gdMain.dataLock)
    			{
    				int[][] qzwzTemp=RobotAutoUtil.autoGo
        	    	(
        	    		gsv.gdMain.qzwz,//目前棋碟情況
        	    		gsv.gdMain.humanColor//人的彩色
        	    	);
    				
    				switch(RobotAutoUtil.preAction)
    				{
    					case PTZZ:
    						if(Constant.IS_YINXIAO)
    						{
    							SoundUtil.playSounds(SoundUtil.PUTONGLUOZI, 0, gsv.activity);
    						}
    					break;
    					case CZ:
    						if(Constant.IS_YINXIAO)
    						{
    							SoundUtil.playSounds(SoundUtil.CHIZI, 0, gsv.activity);
    						}
    					break;
    				}
    				
    				for(int i=0;i<8;i++)
      				{
      				     for(int j=0;j<8;j++)
      					 {
      							//將主資料copy進繪制資料
      							gsv.gdMain.qzwz[i][j]=qzwzTemp[i][j];//棋子位置
      					 }
      				}
    				
    				//換下棋方
					gsv.gdMain.currColor=(gsv.gdMain.currColor+1)%2;
					//讀取是否有輸贏
					Finish result=ChessRuleUtil.isFinish(gsv.gdMain.qzwz);
					switch(result)
				    {
				    case BLACK_WIN://黑方贏了，跳躍到結束界面（判斷是人贏還是機器贏）
				    	 workFlag=false;
				    	 String tempshow;
				    	 if(gsv.gdMain.humanColor==0)//若果人是黑方
				    	 {
				    		 tempshow="恭喜你，你贏了！";
				    		 sendMsgForDialog(tempshow);
				    	 }
				    	 else if(gsv.gdMain.humanColor==1)//若果人是白方
				    	 {
				    		 tempshow="不好意思，你輸了！";
				    		 sendMsgForDialog(tempshow);
				    	 }
				      break;
				      case WHITE_WIN://白方贏了，跳躍到結束界面（判斷是人贏還是機器贏）
				    	 workFlag=false;
				    	 if(gsv.gdMain.humanColor==0)//若果人是黑方
				    	 {
				    		 tempshow="不好意思，你輸了！";
				    		 sendMsgForDialog(tempshow);
				    	 }
				    	 else if(gsv.gdMain.humanColor==1)//若果人是白方
				    	 {
				    		 tempshow="恭喜你，你贏了！";
				    		 sendMsgForDialog(tempshow);
				    	 }
					  break; 
				      case NO_FINISH://沒有結束，則機器走子
				    	 //將新的主資料更新到繪制資料中
	       				 synchronized(gsv.gdDraw.dataLock)//鎖上繪制資料鎖
	   					 {
	   						 for(int i=0;i<8;i++)
	           				 {
	           				     for(int j=0;j<8;j++)
	           					 {
	           							//將主資料copy進繪制資料
	           							gsv.gdDraw.qzwz[i][j]=gsv.gdMain.qzwz[i][j];//棋子位置
	           							gsv.gdDraw.qzxz[i][j]=gsv.gdMain.qzxz[i][j];//棋子勾選
	           							gsv.gdDraw.gzxz[i][j]=gsv.gdMain.gzxz[i][j];//格子勾選
	           					 }
	           				 }
	   					 }
					  break;   
				    }
    			}
    		}
    	}.start();
    }
}
