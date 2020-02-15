package com.bn.txz.game;

import android.opengl.Matrix;

public class GameData 
{
	public  boolean loseFlag=true;
	public  boolean winFlag=false;
	public boolean gameFinish=false;
	public static int level=1;
	
	//資料讀以及更新的鎖
	public Object dataLock=new Object();
	
	//箱子在x,y軸的位移
	public static float offsetx=0;
	public static float offsetz=0;
    
    public float direction = -182.18f;		//方位角，取值範圍0--360
    public float angle =52f;			//仰角，取值範圍0--90，90度為平視，0度為俯視
    public float distance = 12f; 	//攝影機目的點距離
    public final float TOUCH_SCALE_FACTOR = 180.0f/400;	//角度縮放比例
	//攝影機的位置
    public float cx=0; 
    public float cy=0; 
    public float cz=0; 

	//觀察點的位置
    public float tx=0; 
    public float ty=0; 
    public float tz=0; 
    
    //up向量
    public float ux=0; 
    public float uy=0; 
    public float uz=0; 
	
	public void calculCamare(float dx,float dy) //計算攝影機的位置與旋轉角度
	{
		direction += dx * TOUCH_SCALE_FACTOR;	//方位角
		angle+=dy * TOUCH_SCALE_FACTOR;			//方向角
		if(angle<=6.53f)
		{
			angle=6.53f;
		}
		else if(angle>=52)
		{
			angle=52;
		}
		//虛擬向量
	   	 double angleHD=Math.toRadians(direction);
	   	 float[] cup={-(float) Math.sin(angleHD),0,(float) Math.cos(angleHD),1};
	   	 float[] cLocation={0,distance,0,1};
	   	 
	   	 //計算轉軸向量    	 
	   	 float[] zhou={-cup[2],0,cup[0]};   
	   	
	   	 //計算攝影機參數
	   	 float[] helpM=new float[16];
	   	 Matrix.setIdentityM(helpM, 0);
	   	 Matrix.rotateM(helpM, 0, angle, zhou[0],zhou[1],zhou[2]);
	   	 float[] cupr=new float[4];
	   	 float[] cLocationr=new float[4];
	   	 Matrix.multiplyMV(cupr, 0, helpM, 0, cup, 0);
	   	 Matrix.multiplyMV(cLocationr, 0, helpM, 0, cLocation, 0);
	   	 
	   	 cx=cLocationr[0];//攝影機位置
	   	 cy=cLocationr[1];
	   	 cz=cLocationr[2];
	   	 tx=0f;ty=0f;tz=0f;//觀察點位置
	   	 ux=cupr[0];uy=cupr[1];uz=cupr[2];//up向量
	}
	
	{
		calculCamare(-7.7f,-2.5f);
	}
	
	public void updateCameraData(GameData gdIn)//將傳輸進來的游戲資料給予值給攝影機的位置
	{
		this.cx=gdIn.cx;//攝影機位置
		this.cy=gdIn.cy;
		this.cz=gdIn.cz;
		this.tx=gdIn.tx;//觀察點位置
		this.ty=gdIn.tx;
		this.tz=gdIn.tx;
		this.ux=gdIn.ux;//up向量
		this.uy=gdIn.uy;
		this.uz=gdIn.uz;
		this.direction=gdIn.direction;
	}
	
	
	public BodyPartData[] dataArray=new BodyPartData[7];
	
	public GameData()
	{
		for(int i=0;i<dataArray.length;i++)
		{
			dataArray[i]=new BodyPartData();
		}
	}
	
	public void copyTo(GameData gd)
	{
		for(int i=0;i<dataArray.length;i++)
		{
			this.dataArray[i].copyTo(gd.dataArray[i]);
		}
	}

}
