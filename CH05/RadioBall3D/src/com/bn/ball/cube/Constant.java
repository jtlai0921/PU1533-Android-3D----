package com.bn.ball.cube;


public class Constant {

	//單位尺寸
  	public static final float UNIT_SIZE=0.15f;
  	public static float GRAVITY = 0.015f;
  	public static boolean THREAD_FLAG=false;
	//起始速度陣列
  	public static final Vector3[] VELOCITY=
  	{
  		new Vector3((float)(-1.1*UNIT_SIZE),(float)(0.6*UNIT_SIZE),-0.65f*UNIT_SIZE),new Vector3((float)(0.2*UNIT_SIZE),(float)(2*UNIT_SIZE),-0.65f*UNIT_SIZE),new Vector3(UNIT_SIZE,(float)(1.1*UNIT_SIZE),-0.65f*UNIT_SIZE),
  		new Vector3((float)(-1.4*UNIT_SIZE),(float)(0.5*UNIT_SIZE),-0.65f*UNIT_SIZE),new Vector3((float)(0.3*UNIT_SIZE),(float)(1.7*UNIT_SIZE),-0.65f*UNIT_SIZE),new Vector3((float)(0.6*UNIT_SIZE),(float)(0.2*UNIT_SIZE),-0.65f*UNIT_SIZE),
  		new Vector3(-(float)(0.9*UNIT_SIZE),(float)(0.7*UNIT_SIZE),-0.65f*UNIT_SIZE),new Vector3((float)(0.1*UNIT_SIZE),(float)(0.4*UNIT_SIZE),-0.65f*UNIT_SIZE),new Vector3(-UNIT_SIZE,(float)(0.6*UNIT_SIZE),-0.65f*UNIT_SIZE),
  		
  		new Vector3(-2*UNIT_SIZE,(float)(0.2*UNIT_SIZE),-0.65f*UNIT_SIZE),new Vector3(0,(float)(0.7*UNIT_SIZE),-UNIT_SIZE),new Vector3(2*UNIT_SIZE,(float)(1.5*UNIT_SIZE),-UNIT_SIZE),
  		new Vector3((float)(-0.5*UNIT_SIZE),0,-0.65f*UNIT_SIZE),new Vector3(UNIT_SIZE,UNIT_SIZE,-UNIT_SIZE),new Vector3((float)(0.4*UNIT_SIZE),0,-UNIT_SIZE),
  		new Vector3(-3*UNIT_SIZE,(float)(0.4*UNIT_SIZE),-0.65f*UNIT_SIZE),new Vector3(0,(float)(0.6*UNIT_SIZE),-0.65f*UNIT_SIZE),new Vector3(3*UNIT_SIZE,(float)(0.8*UNIT_SIZE),-UNIT_SIZE),
  		
  		new Vector3((float)(1.5*-UNIT_SIZE),(float)(0.3*UNIT_SIZE),-0.65f*UNIT_SIZE),new Vector3(0,(float)(0.6*UNIT_SIZE),-0.65f*UNIT_SIZE),new Vector3(UNIT_SIZE,(float)(0.7*UNIT_SIZE),-UNIT_SIZE),
  		new Vector3((float)(1.2*-UNIT_SIZE),UNIT_SIZE,-0.65f*UNIT_SIZE),new Vector3(UNIT_SIZE,UNIT_SIZE,-UNIT_SIZE),new Vector3(UNIT_SIZE,0,-UNIT_SIZE),
  		new Vector3((float)(0.8*-UNIT_SIZE),(float)(2*UNIT_SIZE),-0.65f*UNIT_SIZE),new Vector3(0,UNIT_SIZE,-UNIT_SIZE),new Vector3(UNIT_SIZE,(float)(-0.9*UNIT_SIZE),-UNIT_SIZE)
  	};
  	//角度陣列
  	public static final float[][] ANGLE=
  	{
  		{2,0,1,0},{3,1,0,0},{5,1,0,1},{11,0,1,0},{7,1,0,0},{12,0,1,0},{13,1,0,1},{6,0,0,1},{10,1,1,0},
  		{15,1,0,0},{9,0,1,0},{12,0,1,0},{4,1,0,0},{5,0,0,1},{8,1,0,0},{11,0,1,0},{9,1,0,0},{13,0,1,0},
  		{3,1,0,0},{13,0,1,0},{6,1,0,1},{1,1,0,0},{9,0,1,0},{14,0,1,1},{15,0,0,1},{2,0,1,0},{4,0,0,1}
  	};

	
}
