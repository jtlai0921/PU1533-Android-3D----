package tstc.fxq.threads;
 
import java.util.ArrayList;

import tstc.fxq.constants.Constant;
import tstc.fxq.constants.Sight;
import tstc.fxq.main.MySurfaceView;
import tstc.fxq.parts.BallKongZhi;
import tstc.fxq.utils.PZJCUtil;
import android.os.Message;
import static tstc.fxq.constants.Constant.*;
//桌球運動的執行緒
public class BallGoThread extends Thread 
{
	//所有桌球的清單
	ArrayList<BallKongZhi> ballAl;
	//執行緒是否繼續工作的標志位
	MySurfaceView mv;
	public boolean flag=true;
	float tempZL;
	float tempZR;
	float tempXU;
	float tempXD;
	float temp=0.2f;//偏移量
	
	//用於記錄移除球的參考
	ArrayList<BallKongZhi> ballForDelete=new ArrayList<BallKongZhi>();
	ArrayList<Integer> ballIndexForDelete=new ArrayList<Integer>();
	
	public BallGoThread(ArrayList<BallKongZhi> ballAl,MySurfaceView mv)
	{
		this.ballAl=ballAl;
		this.mv=mv;
	}
	public void run()
	{
		while(flag)
		{
			//取得球的個數
			int size=ballAl.size();
	
			//判斷每個球是否有進洞
			try
			{
				ballForDelete.clear();
				ballIndexForDelete.clear();
				for(int i=0;i<size;i++)
				{
					BallKongZhi bfc=ballAl.get(i);
					tempZL=BOTTOM_LENGTH/2-bfc.zOffset;//球心到左擋板的距離
					tempZR=bfc.zOffset+BOTTOM_LENGTH/2;//球心到右擋板的距離
					tempXU=bfc.xOffset+BOTTOM_WIDE/2;//球心到上擋板的距離
					tempXD=BOTTOM_WIDE/2-bfc.xOffset;//球心到下擋板的距離					
					
					if(//檢驗球是否進洞
							tempZL<GOT_SCORE_DISTANCE||tempZR<GOT_SCORE_DISTANCE||
							tempXU<GOT_SCORE_DISTANCE||tempXD<GOT_SCORE_DISTANCE
					)
					{
						if(bfc==ballAl.get(0))
						{//母球進洞
							bfc.vx=0;
							bfc.vz=0;
							bfc.yOffset=100f;    							
							MySurfaceView.miniBall=false;
							
							if(!MQJDSYYJBF)
							{
								MQJDSYYJBF=true;

								if(mv.activity.isSoundOn()){//播放球碰撞的音效
									mv.activity.playSound(3, 0, 1);//球進洞的音效
								}
							}							
						}
						else
						{//普通球進洞
							bfc.vx=0;
							bfc.vz=0;
							bfc.yOffset=10f;
							if(mv.activity.isSoundOn()){//播放球碰撞的音效
								mv.activity.playSound(3, 0, 1);//球進洞的音效
							}
							//記錄移除球的參考
							ballForDelete.add(bfc);
							//記錄移除球的索引
							ballIndexForDelete.add(i);
							//進球數加1
							mv.score.addScore(1);
						}
					}					
				}
				
				for(int i=0;i<ballForDelete.size();i++)
				{
					try
					{
						ballAl.remove(ballForDelete.get(i));//移除進洞的球
						MySurfaceView.alBallMiniId.remove(((Integer)ballIndexForDelete.get(i)).intValue());//移除進洞的迷你地圖上的球
						MySurfaceView.alBallTexId.remove(((Integer)ballIndexForDelete.get(i)).intValue());//移除進洞球的紋理
						score++;//得分自加
						if(ballAl.size()==1)
						{
							overFlag=true;//游戲結束標志位設為true
						}
					}catch(Exception ea)
					{
						ea.printStackTrace();
					}
				}
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
			//重設所有球是否停止的標志位，假設所有球都停止
			boolean allStop=true;
			//每個球各自走一步
			for(int i=0;i<ballAl.size();i++)
			{
				BallKongZhi bfc=ballAl.get(i);
				bfc.go(ballAl);			
			}
			//檢驗是否所有球停止
			for(int i=0;i<ballAl.size();i++)
			{
				BallKongZhi bfc=ballAl.get(i);				
				//若此球速度不為0，則將所有球是否停止的標志位置false
				if(bfc.vx!=0||bfc.vz!=0)
				{
					allStop=false;					
				}				
			}
			//若果所有球都停止運動、球桿動畫播放完畢
			if(allStop && (!CUE_DH_FLAG))
			{
				//將連續碰撞次數歸零
				Constant.collisionCount = 0;
				
				//若果白球因進洞而被隱藏，將白球重新重置
				if(ballAl.get(0).yOffset==100)
				{
					//*********************規則開始begin***************************
					//主要是主球落洞的幾個規則
					//若果未與任何球發生碰撞,主球進洞
					if(Constant.IS_FIGHT==false)
					{	
						sendHandlerMessage(Constant.NO_FIGHT_AND_FLOP);
						//防止下面的（未與任何球發生碰撞）Toast再次出現
						Constant.MAINBALL_FLOPFLAG = true;
					}
					//若果主球與其他球發生了碰撞，未擊中目的球，並且主球入洞,9球未入洞
					else if(Constant.IS_FIGHT && Constant.FIRST_FIGHT_ERROR&&
							(ballAl.get(ballAl.size()-1).id==9)&&(Constant.POS_INDEX==1))
					{
		    			//出現主球沒有擊中目的球的Toast			    			
			    		sendHandlerMessage(Constant.MAINBALLFLOP_AND_NOTFIGHTTARGET);
			    		Constant.FIRST_FIGHT_ERROR=false;
			    		
					}//若果只是主球落洞
					else
					{
		    			sendHandlerMessage(Constant.MAINBALL_FLOP);
					}
					//*********************規則end***************************
					

					ballAl.get(0).zOffset=MAIN_BALL_INIT_ZOFFSET;
					if(ballAl.size()>=2)
					{
						//動態搜尋可以的X位置
						for(int ik=0;ik<11;ik++)
						{									
							boolean zjTemp=false;
							ballAl.get(0).xOffset=ik*BALL_R;										
							for(int jk=1;jk<ballAl.size();jk++)
							{
								zjTemp=PZJCUtil.jiSuanPengZhuangSimple(ballAl.get(0), ballAl.get(jk));
								if(zjTemp==true)
								{
									break;
								}
							}										
							if(zjTemp==false)
							{
								break;
							}
							
							zjTemp=false;
							ballAl.get(0).xOffset=-ik*BALL_R;										
							for(int jk=1;jk<ballAl.size();jk++)
							{
								zjTemp=PZJCUtil.jiSuanPengZhuangSimple(ballAl.get(0), ballAl.get(jk));
								if(zjTemp==true)
								{
									break;
								}
							}	
							if(zjTemp==false)
							{
								break;
							}
						}
					}
					ballAl.get(0).yOffset=1;
					MQJDSYYJBF=false;
					mv.cue.setAngle(0);
					//重繪母球
					MySurfaceView.miniBall=true;
				}
				//若果所有球都靜止，且球桿不能顯示，說明擊球完畢後需要播放攝影機運動動畫
				if(Constant.cueFlag == false){
					
					
					//********************規則開始begin********************
					//主球未與任何球發生碰撞begin
					if(Constant.IS_FIGHT == false)
					{
						//在主球沒有進洞的情況下，不會出現Toast
						if(!Constant.MAINBALL_FLOPFLAG)
						{	
			    			sendHandlerMessage(Constant.NO_FIGHT);
			    			
						}
						Constant.MAINBALL_FLOPFLAG = false;
		    			Constant.IS_FIGHT = true;//標志位設為true，防止在次出現toast
		    			//將主球還原到起始位置，不播放攝影機運動的動畫效果
						Constant.recoverWhiteBallNoCam(mv, ballAl);
						
					}else	
					{
						//若果在9球的情況下
						if(Constant.POS_INDEX==1)
						{
							//主球沒有擊中目的球的情況,並且9球沒有入洞
			    			if(Constant.FIRST_FIGHT_ERROR&&(ballAl.get(ballAl.size()-1).id==9))
			    			{	//出現主球沒有擊中目的球的Toast			    			
				    			sendHandlerMessage(Constant.NO_FIGHT_TARGET);
				    			
				    			//將主球還原到起始位置
								Constant.recoverWhiteBallNoCam(mv, ballAl);
								
								Constant.FIRST_FIGHT_ERROR = false;
			    				
			    			}else 
			    			//若果是沒有擊中目的球，並且9球入洞，那麼犯規，則推出程式
			    			if(Constant.FIRST_FIGHT_ERROR&&(ballAl.get(ballAl.size()-1).id!=9))
			    			{
				    			//顯示推出程式的dialog
					    		sendHandlerMessage(Constant.EXIT_SYSTEM_LOSE9);
					    		
					    		Constant.FIRST_FIGHT_ERROR = false;
				    			return;
			    			}
			    			
			    			//在沒有犯規的情況下，9球進洞，則贏得此局的比賽
			    			int tempsize=ballAl.size();
			    			if(ballAl.get(tempsize-1).id != 9)	//若果沒有擊球犯規，最後一個球不是9，則贏得比賽比賽
			    			{
			    				//結束游戲
			    				mv.overGame();
			    				return;
			    			}
						}
						
						//在8球的情況下
						else if(Constant.POS_INDEX == 0)
						{
							//循環所有的球清單，若果存在8號球和白球，則沒有犯規
							
							boolean flag=false; // 8球存在的 標志位
							for(int i=0;i<ballAl.size();i++)
							{
								if(ballAl.get(i).id == 8)
								{
									flag=true;
								}
							}
							//若果不存在8號球，並且球的個數大於2個
							if((flag==false)&&(ballAl.size()>2))
							{
								//顯示失敗的dialog
				    			sendHandlerMessage(Constant.EXIT_SYSTEM_LOSE8);
				    			return;
							}
							
							
							
							//若果第一個球是白球，並且清單中只有一個球的話,則贏得比賽
							if((ballAl.get(0).id==0)&&(ballAl.size()==1))
							{
								//結束游戲
			    				mv.overGame();
				    			return;
							}
						}
						
						
						
						
						
						
						
						//記錄擊球後白球的位置
						MoveCameraThread.xTo = ballAl.get(0).xOffset;
						MoveCameraThread.zTo = ballAl.get(0).zOffset;
						//若果是第一人稱角度，播放攝影機搬移的動畫
						if(mv.currSight == Sight.first){
							//若果目前攝影機執行緒不為空，或是執行緒已經死亡，再開啟搬移攝影機的執行緒
							if(
									MoveCameraThread.currThread == null || 
									!MoveCameraThread.currThread.isAlive()
								)
							{
								//所有球停止後，開啟搬移攝影機的執行緒
								MoveCameraThread.currThread = new MoveCameraThread(mv);	
								MoveCameraThread.currThread.start();
							}
						}					//若果是自由角度，直接顯示球桿
						else{
							//顯示球桿的標志位	
							Constant.cueFlag=true;//繪制球桿標志位
						}
					}
					
			//********************規則end********************
					
					
					
					
					
					
					
					
					//記錄擊球後白球的位置
					MoveCameraThread.xTo = ballAl.get(0).xOffset;
					MoveCameraThread.zTo = ballAl.get(0).zOffset;
					//若果是第一人稱角度，播放攝影機搬移的動畫
					if(mv.currSight == Sight.first){
						//若果目前攝影機執行緒不為空，或是執行緒已經死亡，再開啟搬移攝影機的執行緒
						if(
								MoveCameraThread.currThread == null || 
								!MoveCameraThread.currThread.isAlive()
							)
						{
							//所有球停止後，開啟搬移攝影機的執行緒
							MoveCameraThread.currThread = new MoveCameraThread(mv);	
							MoveCameraThread.currThread.start();
						}
					}
					//若果是自由角度，直接顯示球桿
					else{
						//顯示球桿的標志位	
						Constant.cueFlag=true;//繪制球桿標志位
					}
				}
			}
			//執行緒休眠一定的時間
			try {
				Thread.sleep(Constant.THREAD_SLEEP);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	
	//向LooperThread類別中的handler傳遞訊息的方法
	public void sendHandlerMessage(int message)
	{
		//建立訊息物件
		Message msg=new Message();
		//設定訊息的what值
		msg.what=message;
		//傳送訊息，出現逾時犯規的Toast
		if(mv.activity.hd != null){
			mv.activity.hd.sendMessage(msg);
		}
	}
}