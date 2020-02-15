package tstc.fxq.threads;

import java.util.ArrayList;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;
import tstc.fxq.parts.BallKongZhi;
import android.os.Message;

public class RegulationTimeThread extends Thread{
	public boolean regTimeFlag=true;
	
	boolean flag = true;	//��45��ĵ��toast�u����@�����ЧӦ�
	
	MySurfaceView mv;
	ArrayList<BallKongZhi> ballAl;//�Ҧ��y���M��
	
	public RegulationTimeThread(MySurfaceView mv,
			ArrayList<BallKongZhi> ballAl	
		)
	{
		this.mv=mv;
		this.ballAl=ballAl;
		//���غc���Q�I�s�A�]�w�ثe�ɶ����A�����y���}�l�ɶ�
		Constant.START_TIME=System.currentTimeMillis();//�����y���}�l�ɶ�
	}

	public void run()
	{
		while(regTimeFlag)
		{
			long currTime=System.currentTimeMillis();//���o�ثe���ɶ�
			
			//45�����y�A�hĵ�ܪ��a
			if(((currTime-Constant.START_TIME)/1000>45)&&flag)
			{
    			//�إ߰T������
    			Message msg=new Message();
    			//�]�w�T����what��
    			msg.what=Constant.REMINDPLAYER;
    			//�ǰe�T���A�X�{�O�ɥǳW��Toast
    			mv.activity.hd.sendMessage(msg);
    			
    			flag = false;
				
			}

			//�Y�G�����y���ɶ��W�L�ɶ����j60��A�h��ǳW�A�X�{Toast
			if((currTime-Constant.START_TIME)/1000>60)
			{
    			//�إ߰T������
    			Message msg=new Message();
    			//�]�w�T����what��
    			msg.what=Constant.OVERTIMETOAST;
    			//�ǰe�T���A�X�{�O�ɥǳW��Toast
    			mv.activity.hd.sendMessage(msg);
    			
    			Constant.START_TIME=System.currentTimeMillis();//�����y���}�l�ɶ�
    			
    			
				//�O���O�ɥǳW��A�ثe�y����m
				MoveCameraThread.xFrom = ballAl.get(0).xOffset;
				MoveCameraThread.zFrom = ballAl.get(0).zOffset;
    			
    			//�N�ղy�٭��_�l����m
    			Constant.recoverWhiteBall(mv, ballAl);
    			
    			flag = true;
    			
			}
			
			try 
			{ 
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{ 
				e.printStackTrace(); 
			}
		}
		
	}
}

