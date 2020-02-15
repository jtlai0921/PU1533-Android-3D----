package tstc.fxq.threads;
 
import java.util.ArrayList;

import tstc.fxq.constants.Constant;
import tstc.fxq.constants.Sight;
import tstc.fxq.main.MySurfaceView;
import tstc.fxq.parts.BallKongZhi;
import tstc.fxq.utils.PZJCUtil;
import android.os.Message;
import static tstc.fxq.constants.Constant.*;
//��y�B�ʪ������
public class BallGoThread extends Thread 
{
	//�Ҧ���y���M��
	ArrayList<BallKongZhi> ballAl;
	//������O�_�~��u�@���ЧӦ�
	MySurfaceView mv;
	public boolean flag=true;
	float tempZL;
	float tempZR;
	float tempXU;
	float tempXD;
	float temp=0.2f;//�����q
	
	//�Ω�O�������y���Ѧ�
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
			//���o�y���Ӽ�
			int size=ballAl.size();
	
			//�P�_�C�Ӳy�O�_���i�}
			try
			{
				ballForDelete.clear();
				ballIndexForDelete.clear();
				for(int i=0;i<size;i++)
				{
					BallKongZhi bfc=ballAl.get(i);
					tempZL=BOTTOM_LENGTH/2-bfc.zOffset;//�y�ߨ쥪�תO���Z��
					tempZR=bfc.zOffset+BOTTOM_LENGTH/2;//�y�ߨ�k�תO���Z��
					tempXU=bfc.xOffset+BOTTOM_WIDE/2;//�y�ߨ�W�תO���Z��
					tempXD=BOTTOM_WIDE/2-bfc.xOffset;//�y�ߨ�U�תO���Z��					
					
					if(//����y�O�_�i�}
							tempZL<GOT_SCORE_DISTANCE||tempZR<GOT_SCORE_DISTANCE||
							tempXU<GOT_SCORE_DISTANCE||tempXD<GOT_SCORE_DISTANCE
					)
					{
						if(bfc==ballAl.get(0))
						{//���y�i�}
							bfc.vx=0;
							bfc.vz=0;
							bfc.yOffset=100f;    							
							MySurfaceView.miniBall=false;
							
							if(!MQJDSYYJBF)
							{
								MQJDSYYJBF=true;

								if(mv.activity.isSoundOn()){//����y�I��������
									mv.activity.playSound(3, 0, 1);//�y�i�}������
								}
							}							
						}
						else
						{//���q�y�i�}
							bfc.vx=0;
							bfc.vz=0;
							bfc.yOffset=10f;
							if(mv.activity.isSoundOn()){//����y�I��������
								mv.activity.playSound(3, 0, 1);//�y�i�}������
							}
							//�O�������y���Ѧ�
							ballForDelete.add(bfc);
							//�O�������y������
							ballIndexForDelete.add(i);
							//�i�y�ƥ[1
							mv.score.addScore(1);
						}
					}					
				}
				
				for(int i=0;i<ballForDelete.size();i++)
				{
					try
					{
						ballAl.remove(ballForDelete.get(i));//�����i�}���y
						MySurfaceView.alBallMiniId.remove(((Integer)ballIndexForDelete.get(i)).intValue());//�����i�}���g�A�a�ϤW���y
						MySurfaceView.alBallTexId.remove(((Integer)ballIndexForDelete.get(i)).intValue());//�����i�}�y�����z
						score++;//�o���ۥ[
						if(ballAl.size()==1)
						{
							overFlag=true;//���������ЧӦ�]��true
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
			
			//���]�Ҧ��y�O�_����ЧӦ�A���]�Ҧ��y������
			boolean allStop=true;
			//�C�Ӳy�U�ۨ��@�B
			for(int i=0;i<ballAl.size();i++)
			{
				BallKongZhi bfc=ballAl.get(i);
				bfc.go(ballAl);			
			}
			//����O�_�Ҧ��y����
			for(int i=0;i<ballAl.size();i++)
			{
				BallKongZhi bfc=ballAl.get(i);				
				//�Y���y�t�פ���0�A�h�N�Ҧ��y�O�_����ЧӦ�mfalse
				if(bfc.vx!=0||bfc.vz!=0)
				{
					allStop=false;					
				}				
			}
			//�Y�G�Ҧ��y������B�ʡB�y��ʵe���񧹲�
			if(allStop && (!CUE_DH_FLAG))
			{
				//�N�s��I�������k�s
				Constant.collisionCount = 0;
				
				//�Y�G�ղy�]�i�}�ӳQ���áA�N�ղy���s���m
				if(ballAl.get(0).yOffset==100)
				{
					//*********************�W�h�}�lbegin***************************
					//�D�n�O�D�y���}���X�ӳW�h
					//�Y�G���P����y�o�͸I��,�D�y�i�}
					if(Constant.IS_FIGHT==false)
					{	
						sendHandlerMessage(Constant.NO_FIGHT_AND_FLOP);
						//����U�����]���P����y�o�͸I���^Toast�A���X�{
						Constant.MAINBALL_FLOPFLAG = true;
					}
					//�Y�G�D�y�P��L�y�o�ͤF�I���A�������ت��y�A�åB�D�y�J�},9�y���J�}
					else if(Constant.IS_FIGHT && Constant.FIRST_FIGHT_ERROR&&
							(ballAl.get(ballAl.size()-1).id==9)&&(Constant.POS_INDEX==1))
					{
		    			//�X�{�D�y�S�������ت��y��Toast			    			
			    		sendHandlerMessage(Constant.MAINBALLFLOP_AND_NOTFIGHTTARGET);
			    		Constant.FIRST_FIGHT_ERROR=false;
			    		
					}//�Y�G�u�O�D�y���}
					else
					{
		    			sendHandlerMessage(Constant.MAINBALL_FLOP);
					}
					//*********************�W�hend***************************
					

					ballAl.get(0).zOffset=MAIN_BALL_INIT_ZOFFSET;
					if(ballAl.size()>=2)
					{
						//�ʺA�j�M�i�H��X��m
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
					//��ø���y
					MySurfaceView.miniBall=true;
				}
				//�Y�G�Ҧ��y���R��A�B�y�줣����ܡA�������y������ݭn������v���B�ʰʵe
				if(Constant.cueFlag == false){
					
					
					//********************�W�h�}�lbegin********************
					//�D�y���P����y�o�͸I��begin
					if(Constant.IS_FIGHT == false)
					{
						//�b�D�y�S���i�}�����p�U�A���|�X�{Toast
						if(!Constant.MAINBALL_FLOPFLAG)
						{	
			    			sendHandlerMessage(Constant.NO_FIGHT);
			    			
						}
						Constant.MAINBALL_FLOPFLAG = false;
		    			Constant.IS_FIGHT = true;//�ЧӦ�]��true�A����b���X�{toast
		    			//�N�D�y�٭��_�l��m�A��������v���B�ʪ��ʵe�ĪG
						Constant.recoverWhiteBallNoCam(mv, ballAl);
						
					}else	
					{
						//�Y�G�b9�y�����p�U
						if(Constant.POS_INDEX==1)
						{
							//�D�y�S�������ت��y�����p,�åB9�y�S���J�}
			    			if(Constant.FIRST_FIGHT_ERROR&&(ballAl.get(ballAl.size()-1).id==9))
			    			{	//�X�{�D�y�S�������ت��y��Toast			    			
				    			sendHandlerMessage(Constant.NO_FIGHT_TARGET);
				    			
				    			//�N�D�y�٭��_�l��m
								Constant.recoverWhiteBallNoCam(mv, ballAl);
								
								Constant.FIRST_FIGHT_ERROR = false;
			    				
			    			}else 
			    			//�Y�G�O�S�������ت��y�A�åB9�y�J�}�A����ǳW�A�h���X�{��
			    			if(Constant.FIRST_FIGHT_ERROR&&(ballAl.get(ballAl.size()-1).id!=9))
			    			{
				    			//��ܱ��X�{����dialog
					    		sendHandlerMessage(Constant.EXIT_SYSTEM_LOSE9);
					    		
					    		Constant.FIRST_FIGHT_ERROR = false;
				    			return;
			    			}
			    			
			    			//�b�S���ǳW�����p�U�A9�y�i�}�A�hĹ�o����������
			    			int tempsize=ballAl.size();
			    			if(ballAl.get(tempsize-1).id != 9)	//�Y�G�S�����y�ǳW�A�̫�@�Ӳy���O9�A�hĹ�o���ɤ���
			    			{
			    				//��������
			    				mv.overGame();
			    				return;
			    			}
						}
						
						//�b8�y�����p�U
						else if(Constant.POS_INDEX == 0)
						{
							//�`���Ҧ����y�M��A�Y�G�s�b8���y�M�ղy�A�h�S���ǳW
							
							boolean flag=false; // 8�y�s�b�� �ЧӦ�
							for(int i=0;i<ballAl.size();i++)
							{
								if(ballAl.get(i).id == 8)
								{
									flag=true;
								}
							}
							//�Y�G���s�b8���y�A�åB�y���ӼƤj��2��
							if((flag==false)&&(ballAl.size()>2))
							{
								//��ܥ��Ѫ�dialog
				    			sendHandlerMessage(Constant.EXIT_SYSTEM_LOSE8);
				    			return;
							}
							
							
							
							//�Y�G�Ĥ@�Ӳy�O�ղy�A�åB�M�椤�u���@�Ӳy����,�hĹ�o����
							if((ballAl.get(0).id==0)&&(ballAl.size()==1))
							{
								//��������
			    				mv.overGame();
				    			return;
							}
						}
						
						
						
						
						
						
						
						//�O�����y��ղy����m
						MoveCameraThread.xTo = ballAl.get(0).xOffset;
						MoveCameraThread.zTo = ballAl.get(0).zOffset;
						//�Y�G�O�Ĥ@�H�٨��סA������v���h�����ʵe
						if(mv.currSight == Sight.first){
							//�Y�G�ثe��v������������šA�άO������w�g���`�A�A�}�ҷh����v���������
							if(
									MoveCameraThread.currThread == null || 
									!MoveCameraThread.currThread.isAlive()
								)
							{
								//�Ҧ��y�����A�}�ҷh����v���������
								MoveCameraThread.currThread = new MoveCameraThread(mv);	
								MoveCameraThread.currThread.start();
							}
						}					//�Y�G�O�ۥѨ��סA������ܲy��
						else{
							//��ܲy�쪺�ЧӦ�	
							Constant.cueFlag=true;//ø��y��ЧӦ�
						}
					}
					
			//********************�W�hend********************
					
					
					
					
					
					
					
					
					//�O�����y��ղy����m
					MoveCameraThread.xTo = ballAl.get(0).xOffset;
					MoveCameraThread.zTo = ballAl.get(0).zOffset;
					//�Y�G�O�Ĥ@�H�٨��סA������v���h�����ʵe
					if(mv.currSight == Sight.first){
						//�Y�G�ثe��v������������šA�άO������w�g���`�A�A�}�ҷh����v���������
						if(
								MoveCameraThread.currThread == null || 
								!MoveCameraThread.currThread.isAlive()
							)
						{
							//�Ҧ��y�����A�}�ҷh����v���������
							MoveCameraThread.currThread = new MoveCameraThread(mv);	
							MoveCameraThread.currThread.start();
						}
					}
					//�Y�G�O�ۥѨ��סA������ܲy��
					else{
						//��ܲy�쪺�ЧӦ�	
						Constant.cueFlag=true;//ø��y��ЧӦ�
					}
				}
			}
			//�������v�@�w���ɶ�
			try {
				Thread.sleep(Constant.THREAD_SLEEP);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	
	//�VLooperThread���O����handler�ǻ��T������k
	public void sendHandlerMessage(int message)
	{
		//�إ߰T������
		Message msg=new Message();
		//�]�w�T����what��
		msg.what=message;
		//�ǰe�T���A�X�{�O�ɥǳW��Toast
		if(mv.activity.hd != null){
			mv.activity.hd.sendMessage(msg);
		}
	}
}