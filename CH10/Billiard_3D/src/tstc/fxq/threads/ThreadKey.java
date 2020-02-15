package tstc.fxq.threads;

import tstc.fxq.constants.Constant;
import tstc.fxq.constants.Sight;
import tstc.fxq.main.MySurfaceView;
import static tstc.fxq.constants.Constant.*;
public class ThreadKey extends Thread{

	MySurfaceView mv;
	public boolean stateFlag=true;
	int tempCount=0;//�۰��ܳt�p�ƾ�
	//��ܫ��s���A���`��
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
			//���U������s
			if(MySurfaceView.state==leftRotate)
			{
				tempCount++;
				if(tempCount>15)
				{
					mv.cue.setAngle(mv.cue.getAngle()+8*CUE_ROTATE_DEGREE_SPAN);//�]�w�y����ʨ���
				}
				else
				{
					mv.cue.setAngle(mv.cue.getAngle()+CUE_ROTATE_DEGREE_SPAN);//�]�w�y����ʨ���
				}
				//���ܲy����ਤ�׫�A�]�w��v������m
				mv.setCameraPostion(MySurfaceView.ballAl.get(0).xOffset, MySurfaceView.ballAl.get(0).zOffset);
			}
			//���U�k����s
			else if(MySurfaceView.state==rightRotate)
			{
				tempCount++;				
				if(tempCount>15)
				{
					mv.cue.setAngle(mv.cue.getAngle()-8*CUE_ROTATE_DEGREE_SPAN);//�]�w�y����ʨ���
				}
				else
				{
					mv.cue.setAngle(mv.cue.getAngle()-CUE_ROTATE_DEGREE_SPAN);//�]�w�y����ʨ���
				}
				//���ܲy����ਤ�׫�A�]�w��v������m
				mv.setCameraPostion(MySurfaceView.ballAl.get(0).xOffset, MySurfaceView.ballAl.get(0).zOffset);
			}
			//���U�Ԫ���s
			else if(MySurfaceView.state==nearer)
			{
				//�Y���ۥѨ��סA�ثe���ۥѨ��ת��q�Ԫ�
				if(mv.currSight == Sight.first){
					mv.currSightDis -= Constant.FIRST_SIGHT_DIS_SPAN;
					//����שԪ񤣱o�W�L�@�w����
					if(mv.currSightDis < Constant.FIRST_SIGHT_DIS_MIN){
						mv.currSightDis = Constant.FIRST_SIGHT_DIS_MIN;
					}
				}
				//�Y���ۥѨ��סA�ثe���ۥѨ��ת��q�Ԫ�
				else if(mv.currSight == Sight.free){
					mv.currSightDis -= Constant.FREE_SIGHT_DIS_SPAN;
					//����שԻ����o�W�L�@�w����
					if(mv.currSightDis < Constant.FREE_SIGHT_DIS_MIN){
						mv.currSightDis = Constant.FREE_SIGHT_DIS_MIN;
					}
				}
				//�Z�����ܫ�A�]�w��v������m
				mv.setCameraPostion(MySurfaceView.ballAl.get(0).xOffset, MySurfaceView.ballAl.get(0).zOffset);
			}
			//���U�Ի����s
			else if(MySurfaceView.state==farther)
			{
				//�Y���ۥѨ��סA�ثe���ۥѨ��ת��q�Ի�
				if(mv.currSight == Sight.first){
					mv.currSightDis += Constant.FIRST_SIGHT_DIS_SPAN;
					//����שԻ����o�W�L�@�w����
					if(mv.currSightDis > Constant.FIRST_SIGHT_DIS_MAX){
						mv.currSightDis = Constant.FIRST_SIGHT_DIS_MAX;
					}
				}
				//�Y���ۥѨ��סA�ثe���ۥѨ��ת��q�Ի�
				else if(mv.currSight == Sight.free){
					mv.currSightDis += Constant.FREE_SIGHT_DIS_SPAN;
					//����שԻ����o�W�L�@�w����
					if(mv.currSightDis > Constant.FREE_SIGHT_DIS_MAX){
						mv.currSightDis = Constant.FREE_SIGHT_DIS_MAX;
					}
				}	
				//�Z�����ܫ�A�]�w��v������m
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

