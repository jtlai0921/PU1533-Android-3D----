package com.bn.txz.game;

//�ޱ��ʧ@
public class Action 
{
	ActionType at;				//�ޱ����A
	public float[] data;				//�ޱ����
	
	public Action(ActionType at,float[] data)
	{
		this.at=at;
		this.data=data;
	}
	
	//�n���檺�ʧ@
	   public float[][] Robotdata; 
	   //�`�B�J��
	   public int totalStep;
	public Action(ActionType at)
	{
		this.at=at;
	}
	
	public Action()
	{
		
	}
	 
}
