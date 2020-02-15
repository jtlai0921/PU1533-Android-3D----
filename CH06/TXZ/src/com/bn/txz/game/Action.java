package com.bn.txz.game;

//操控動作
public class Action 
{
	ActionType at;				//操控型態
	public float[] data;				//操控資料
	
	public Action(ActionType at,float[] data)
	{
		this.at=at;
		this.data=data;
	}
	
	//要執行的動作
	   public float[][] Robotdata; 
	   //總步驟數
	   public int totalStep;
	public Action(ActionType at)
	{
		this.at=at;
	}
	
	public Action()
	{
		
	}
	 
}
