package tstc.fxq.utils;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;

/*
 * �˭p�����O
 * 
 * �ѩ�3D��������O�n�۾A���ù��A
 * �b��������ɷh����m����K�A
 * �]���ɶ����O�����C�Ӧ�m�����@�ӻ���O
 */
public class Timer {
	MySurfaceView mv;
	private final int totalSecond=12*60;//�`���
	private int leftSecond = totalSecond;//�ɶ�
	private Number3D minuteNum;//����Ʀr
	private Number3D secondNum;//����Ʀr
	private Board breakBoard;//���j��
	public Timer
	(
			MySurfaceView mv, 
			float x, float y,//�Ʀr�����W�I�y��
			float numberWidth, float numberHeight,//�C�ӼƦr�����e
    		float sEnd, float tEnd, //�Ʀr�Ϥ��k�U����s�Bt��
			int numberTexId,
			int breakTexId
	){
		this.mv = mv;
		minuteNum = new Number3D(
				mv, 
				x, y, 
				numberWidth, numberHeight, 
				sEnd, tEnd, 
				numberTexId
				);
		secondNum = new Number3D(
				mv, 
				x + 3*numberWidth, y, 
				numberWidth, numberHeight, 
				sEnd, tEnd, 
				numberTexId
				);
		breakBoard = new Board(
				mv, 
				x + 2*numberWidth, y, 
				numberWidth, numberHeight, 
				0, 0, 
				sEnd, tEnd, 
				breakTexId, 
				0
				);
		//�ھڳѧE��ơA�p��ó]�w�ثe����ƩM���
		setTime();
	}
	
	//��֮ɶ�����k
	public void subtractTime(int second)
	{
		if(this.leftSecond>0)
		{
			this.leftSecond -= second;
			//�ھڳѧE��ơA�p��ó]�w�ثe����ƩM���
			setTime();
		}
		else//�Y�G�ɶ���0�A��������
		{
			//��ܮɶ���������͵���
			mv.bgt.sendHandlerMessage(Constant.TIME_UP);
			//��������
			mv.overGame();
		}
	}
	//�ھڳѧE��ơA�p��ó]�w�ثe����ƩM��ƪ���k
	private void setTime(){		
		//���o����M���骺��k
		int second = this.leftSecond%60;
		int minute = this.leftSecond/60;	
		String secondStr = second + "";
		String minuteStr = minute + "";
		if(second<10){//�T�O�ܤ֦����
			secondStr="0" + second;
		}

		if(minute<10){//�T�O�ܤ֦����
			minuteStr="0" + minute;
		}
		//�]�w����M����
		secondNum.setNumber(secondStr);
		minuteNum.setNumber(minuteStr);
	}
	
	//���o�ѧE�ɶ�����k
	public int getLeftSecond() {
		return leftSecond;
	}
	
	//ø��ɶ�����k
	public void drawSelf()
	{
		//ø�����M����
		secondNum.drawSelf();
		minuteNum.drawSelf();
		//ø����j��
		breakBoard.drawSelf();
	}
}
