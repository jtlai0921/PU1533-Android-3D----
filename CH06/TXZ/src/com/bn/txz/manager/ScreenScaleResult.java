package com.bn.txz.manager;

enum ScreenOrien
{
	HP,  //��ܾ�̪��C�|��
	SP   //��ܫ������C�|��
}

//�Y��p�⪺���G
public class ScreenScaleResult
{
	public int lucX;//���W��X�y��
	public int lucY;//���W��y�y��
	public float ratio;//�Y����
	ScreenOrien so;//������p	
	
	public ScreenScaleResult(int lucX,int lucY,float ratio,ScreenOrien so)
	{
		this.lucX=lucX;
		this.lucY=lucY;
		this.ratio=ratio;
		this.so=so;
	}
	
	public String toString()
	{
		return "lucX="+lucX+", lucY="+lucY+", ratio="+ratio+", "+so;
	}
}