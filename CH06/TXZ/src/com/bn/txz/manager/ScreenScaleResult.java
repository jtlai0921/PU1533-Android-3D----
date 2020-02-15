package com.bn.txz.manager;

enum ScreenOrien
{
	HP,  //表示橫屏的列舉值
	SP   //表示垂直的列舉值
}

//縮放計算的結果
public class ScreenScaleResult
{
	public int lucX;//左上角X座標
	public int lucY;//左上角y座標
	public float ratio;//縮放比例
	ScreenOrien so;//橫垂直情況	
	
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