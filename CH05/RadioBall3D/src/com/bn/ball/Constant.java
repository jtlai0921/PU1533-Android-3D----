package com.bn.ball;

public class Constant 
{	
	
	public static final float MAX_S_QHC=1.0f;//青花瓷最大S紋理座標
	public static final float MAX_T_QHC=0.746f;//青花瓷最大T紋理座標
	   
	public static final float MAX_S_GHXP=1.0f;//國畫小品最大S紋理座標
	public static final float MAX_T_GHXP=1.0f;//國畫小品最大T紋理座標
	
	public static final float UNIT_SIZE=0.8f;//球單位尺寸
	public static final float BALL_SCALE=0.35f;//球縮放值
	public static final float BALL_R=UNIT_SIZE*BALL_SCALE;//球的半徑
	public static float H=16;
	public static float angleSpan=20; //管線的切分角度
	public static float BALL_Z=-1.5f;//球的起始Z座標
	public static float BALL_X=0;	//球的起始X座標
	public static float BALL_Y=-1.2f; //球的起始Y座標
	public static float GUANDAO_L=20;//每個管線的長度
	public static final float GUANDAO_LENGTH=2000;//管線總長度
	public static float GROUND_L=20;//每個地面的長度
	public static final float GROUND_LENGTH=2000;
	public static final float DISTANCE_CAMERA_BALL=2.0f;//攝影機距離帆船的平面距離	
	public static final float MOVE_SPAN=0.2f;//攝影機每次搬移的位移		
	public static float cUpX=0;
	public static float cUpY=0;
	public static float cUpZ=0;	
	public static final float ANGLE_SPAN=5;//將球進行單位切分的角度	
	public static float angleCurr;//目前桌球的旋轉角度
	public static float currAxisX;//目前桌球旋轉軸向量的X分量
	public static float currAxisY;//目前桌球旋轉軸向量的Y分量（因為球在桌面上運動旋轉軸平行於桌面，因此沒有Y分量）
	public static float currAxisZ;	//目前桌球旋轉軸向量的Z分量
	public static float vx=0f;
	public static float vz=-0.2f;	//小球在Z軸的速度
	public static float TIME_SPAN=1.5f;//球搬移每一步的類比時間間隔（此值越小類比效果越真實，但計算量增大，因此要合理設定此值大小）
	public static boolean flag=false;
	
	public static int num=0;
	
	public static boolean daojuFlag=false;
	
	public static float yuanZhu_Length=0.5f;//圓柱的長長寬的一半
	public static float yuanZhu_Width=0.5f;
	public static float yuanZhu_Height=0.6f;
	
	public static float liFangTi_Length=0.8f;//立方體的長長寬的一半
	public static float liFangTi_Width=0.8f;
	public static float liFangTi_Height=0.8f;
	
	public static float container_Length=1.0f;//長方體的長長寬的一半
	public static float container_Width=1.5f;
	public static float container_Height=1.0f;
	
	public static float cube_Length=1.5f;//立方體的長長寬的一半
	public static float cube_Width=1.5f;
	public static float cube_Height=1.5f;
		
	public static float boLi_Length=2.4f;//玻璃的長長寬的一半
	public static float boLi_Height=0.9f;
	
	public static boolean ballYFlag=false;
	public static boolean bolipzFlag=false;
	public static boolean loseFlag=true;
	public static boolean winFlag=false;
	public static boolean daojuZFlag=false;
	
	public static float ballVY=0.18f;
	
	public static final float V_THRESHOLD=0.032f;//速度設定值，小於此設定值的速度認為為0
	public static final float V_MC=0.98f;
	public static float upValue=-0.02f;
	public static float downValue=-0.04f;
	
	public static float pengCount=1.2f;
	public static float vk=1;
	public static int sumBoardScore=0;
	public static int sumLoadScore=0;
	public static int sumEfectScore=0;
	public static float daoJuZ=-5.5f;
	public static float change=0;
	
	public static boolean gameOver=true;
}
