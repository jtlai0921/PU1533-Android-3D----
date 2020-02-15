package com.f.video;

import static com.f.pingpong.Constant.*;

import java.util.List;
//�v����������
public class PlayVideoThread extends Thread
{
	public boolean wFlag=true;//���񧹲��ЧӦ�
	public PlayVideoThread(){}//�L�ѫغc��
	public long startTime,nowTime,pauseTime;//�}�l�ɶ��A�ثe�ɶ��A�Ȱ��ɶ�
	
	@Override
	public void run()
	{
		startTime = System.nanoTime();//��o�}�l�ɶ�
		FrameData.currIndex = 0;//������޸m��0
		List<FrameData> tempFrameDataList; //�{�ɼ���زM��
		if(IS_HELP)//�Y�G�O�������U�v��
		{
			tempFrameDataList = FrameData.helpFrameDataList;
		}
		else//�Y�G�O����зǤ��ɼv��
		{
			tempFrameDataList = FrameData.playFrameDataList;
		}
		while(wFlag)
		{
			if(!HELP_PAUSE)//�Y�G�S���b�������U�v���ɫ��Ȱ�
			{
				if(!(FrameData.currIndex >= tempFrameDataList.size()-1))//�Y�G�S�������̫�@��
				{
					synchronized(videoLock)//�P�B��
					{
						nowTime = System.nanoTime() - startTime;//��o�ثe�ɶ��W
						//�N�ثe�ɶ��W���e���س�����
						if(tempFrameDataList.get(FrameData.currIndex).timestamp < nowTime)
						{
							FrameData.currIndex++;
						}
						//�Y�G�O�b�������U�v���A�h�b���񧹲���`������
						if(IS_HELP && FrameData.currIndex >= tempFrameDataList.size()-1)
						{
							startTime = System.nanoTime();//�q�s���o�}�l�ɶ�
							FrameData.currIndex = 0;//������ޭ��]
						}
					}
				}
				else
				{
					wFlag=false;//�v�����񧹲�
				}
			}
			try
			{
				Thread.sleep(10);//������ίv10�@��
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();//�C�L�ҥ~�T��
			}
		}
		IS_PLAY_VIDEO = false;//�v�����񧹲�
	}
}