package com.bn.txz.game;

import android.opengl.Matrix;

public class GameData 
{
	public  boolean loseFlag=true;
	public  boolean winFlag=false;
	public boolean gameFinish=false;
	public static int level=1;
	
	//���Ū�H�Χ�s����
	public Object dataLock=new Object();
	
	//�c�l�bx,y�b���첾
	public static float offsetx=0;
	public static float offsetz=0;
    
    public float direction = -182.18f;		//��쨤�A���Ƚd��0--360
    public float angle =52f;			//�����A���Ƚd��0--90�A90�׬������A0�׬�����
    public float distance = 12f; 	//��v���ت��I�Z��
    public final float TOUCH_SCALE_FACTOR = 180.0f/400;	//�����Y����
	//��v������m
    public float cx=0; 
    public float cy=0; 
    public float cz=0; 

	//�[���I����m
    public float tx=0; 
    public float ty=0; 
    public float tz=0; 
    
    //up�V�q
    public float ux=0; 
    public float uy=0; 
    public float uz=0; 
	
	public void calculCamare(float dx,float dy) //�p����v������m�P���ਤ��
	{
		direction += dx * TOUCH_SCALE_FACTOR;	//��쨤
		angle+=dy * TOUCH_SCALE_FACTOR;			//��V��
		if(angle<=6.53f)
		{
			angle=6.53f;
		}
		else if(angle>=52)
		{
			angle=52;
		}
		//�����V�q
	   	 double angleHD=Math.toRadians(direction);
	   	 float[] cup={-(float) Math.sin(angleHD),0,(float) Math.cos(angleHD),1};
	   	 float[] cLocation={0,distance,0,1};
	   	 
	   	 //�p����b�V�q    	 
	   	 float[] zhou={-cup[2],0,cup[0]};   
	   	
	   	 //�p����v���Ѽ�
	   	 float[] helpM=new float[16];
	   	 Matrix.setIdentityM(helpM, 0);
	   	 Matrix.rotateM(helpM, 0, angle, zhou[0],zhou[1],zhou[2]);
	   	 float[] cupr=new float[4];
	   	 float[] cLocationr=new float[4];
	   	 Matrix.multiplyMV(cupr, 0, helpM, 0, cup, 0);
	   	 Matrix.multiplyMV(cLocationr, 0, helpM, 0, cLocation, 0);
	   	 
	   	 cx=cLocationr[0];//��v����m
	   	 cy=cLocationr[1];
	   	 cz=cLocationr[2];
	   	 tx=0f;ty=0f;tz=0f;//�[���I��m
	   	 ux=cupr[0];uy=cupr[1];uz=cupr[2];//up�V�q
	}
	
	{
		calculCamare(-7.7f,-2.5f);
	}
	
	public void updateCameraData(GameData gdIn)//�N�ǿ�i�Ӫ�������Ƶ����ȵ���v������m
	{
		this.cx=gdIn.cx;//��v����m
		this.cy=gdIn.cy;
		this.cz=gdIn.cz;
		this.tx=gdIn.tx;//�[���I��m
		this.ty=gdIn.tx;
		this.tz=gdIn.tx;
		this.ux=gdIn.ux;//up�V�q
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
