package com.bn.gjxq.game;
import android.opengl.Matrix;

public class GameData 
{
//////////////////////////////////////////////////////////////////
	public final static int no_qz=-1;//無棋子
	public final static int bche = 0;//白車
	public final static int bma = 1;//白馬
	public final static int bxiang = 2;//白象
	public final static int bhou = 3;//白後
	public final static int bwang = 4;//白王
	public final static int bbing = 5;//白兵
	public final static int hche = 6;//黑車
	public final static int hma = 7;//黑馬
	public final static int hxiang = 8;//黑象
	public final static int hhou = 9;//黑後
	public final static int hwang = 10;//黑王
	public final static int hbing = 11;//黑兵
	///////////////////////////////////////////////////////////////////
	
	//資料讀以及更新的鎖
	public Object dataLock=new Object();
	
    //玩家用黑子還是白子
    public int humanColor=1; //0---黑色  1---白色
    //目前哪個玩家走
	public int currColor=1;//0---黑色  1---白色
	
	//白車--0
	//白馬--1
	//白相--2
	//白後--3
	//白王--4
	//白兵--5
	//黑車--6
	//黑馬--7
	//黑相--8
	//黑後--9
	//黑王--10
	//黑兵--11	
	public int[][] qzwz=//棋子位置
    {
    		{hche,hma,hxiang,hhou,hwang,hxiang,hma,hche},
    		{hbing,hbing,hbing,hbing,hbing,hbing,hbing,hbing},
    		{no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz},
    		{no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz},
    		{no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz},
    		{no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz,no_qz},
    		{bbing,bbing,bbing,bbing,bbing,bbing,bbing,bbing},
    		{bche,bma,bxiang,bhou,bwang,bxiang,bma,bche}
    		
    };
    
    //棋子勾選
    //未勾選---0
    //勾選-----1
    public int[][] qzxz=//棋子勾選
    {
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0}
    };
    
    
    //未勾選---0
    //勾選-----1
    public int[][] gzxz=//棋碟塊勾選
    {
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0},
    		{0,0,0,0,0,0,0,0}
    };
    
    //目前狀態編號
    public int status=0;//0--無棋子勾選  1-有棋子勾選
    
    public float direction = 0;		//方位角，取值範圍0--360
    public float angle =60;			//仰角，取值範圍0--90，90度為平視，0度為俯視
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
		if(angle<=0)
		{
			angle=0;
		}
		else if(angle>=60)
		{
			angle=60;
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
		calculCamare(0,0);
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
	}
	
	//取得指定位置棋子的彩色  0---黑色  1---白色
	public int getColor(int i,int j)
	{
		int result=0;
		int qzbh=qzwz[i][j];
		if(qzbh>=0&&qzbh<=5)//若棋子編號在0--5之間則是白色棋子
		{
			result=1;
		}
		return result;
	}

}
