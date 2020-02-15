package com.bn.cube.game;

import com.bn.cube.view.HitCubeActivity;

import android.content.Context;

public class BallThread extends Thread{

	float ball_R;
	HitCubeActivity activity;		
	public boolean crashflag=false; 
	public boolean failGame=false; 
	public static Object ob=new Object();
	ReflectUtil reflect=new ReflectUtil();
	MySurfaceView mv;
	public BallThread(MySurfaceView mv,Context context)
	{
		this.activity=(HitCubeActivity)context;
		this.mv=mv;
	}
	@Override
	public void run()
	{
		while(Constant.ball_flag)
		{  
			if(Constant.move_flag)
			{
				failGame=false;
			  float tempX=Constant.ball_X;
			  float tempY=Constant.ball_Y;
			  float tempZ=Constant.ball_Z;
			  
			  tempX+=0.1f*Constant.i;
			  tempY+=0.1f*Constant.j;
			  tempZ-=0.1f*Constant.k;				  
			  if(tempZ>=-4.7f&&tempZ<-4.6)  //¼²À»¾×ªO
			  {
				  if(Constant.dangBanBigFlag)
				  {
					  reflect.set(new Vector3(Constant.i,Constant.j,Constant.k), new Vector3(0,0,-1));
						 Vector3 vec=reflect.getReflect();
						 Constant.i=(float) (vec.x+(Math.random()-0.5f)*0.4f);
						 Constant.j=(float) (vec.y+(Math.random()-0.5f)*0.4f); 
						 Constant.k=-Constant.k;	
						 Constant.peng_flag=true;
						 Constant.baiTiaoDrawFlag=true;
						 Constant.baiTiaoAngleId=0;
						 Constant.lightX=tempX;
						 Constant.lightY=tempY;
						 Constant.lightZ=tempZ;
							if(activity.yinXiao){
								activity.playSound(1, 0);
							}
							
							 crashflag=true;
				  }else 
					  if(Constant.ball_X*92.6f+500>=Constant.curr_X*92.6f+350&&Constant.ball_X*92.6f+500<=Constant.curr_X*92.6f+650&&
						 Constant.ball_Y*92.6f+360>=Constant.curr_Y*92.6f+260&&Constant.ball_Y*92.6f+360<=Constant.curr_Y*92.6f+460 
						 ) 
				 {
					 reflect.set(new Vector3(Constant.i,Constant.j,Constant.k), new Vector3(0,0,-1));
					 Vector3 vec=reflect.getReflect();
					 Constant.i=(float) (vec.x+(Math.random()-0.5f)*0.4f);
					 Constant.j=(float) (vec.y+(Math.random()-0.5f)*0.4f); 
					 Constant.k=-Constant.k;	
					 Constant.peng_flag=true;
					 Constant.baiTiaoDrawFlag=true;
					 Constant.baiTiaoAngleId=0;
					 Constant.lightX=tempX;
					 Constant.lightY=tempY;
					 Constant.lightZ=tempZ;
						if(activity.yinXiao){
							activity.playSound(1, 0);
						}
						
						 crashflag=true;
				 }
			  }else if(tempZ<-19.2)
			  {
				    reflect.set(new Vector3(Constant.i,Constant.j,Constant.k), new Vector3(0,0,1));
					 Vector3 vec=reflect.getReflect();
					 Constant.i=(float) vec.x;
					 Constant.j=(float) vec.y;
					 Constant.k=-Constant.k;
					 Constant.peng_flag=true;
					 Constant.baiTiaoDrawFlag=true;
					 Constant.baiTiaoAngleId=0;
					 Constant.lightX=tempX;
					 Constant.lightY=tempY;
					 Constant.lightZ=tempZ;
						if(activity.yinXiao){
							activity.playSound(1, 0);
						}
						 crashflag=true;
			  }
			  if(tempZ>0.2)
			  {
				  
				  failGame=true;
				  tempX=0;
				  tempY=0;
				  tempZ=-12f;
				  Constant.i=0;
				  Constant.j=0;
				  Constant.k=-0.5f;		
				  if(Constant.lifeNum<0)
				  {
					  Constant.lifeNum=0;
				  }else{
					  Constant.lifeNum--;
				  }
				  
				  if(Constant.lifeNum<0)
				  {
					  if(activity.yinXiao)
					  {
						  activity.playSound(5, 0);
					  }
					  
					  Constant.lifeNum=0;
					  activity.hd.sendEmptyMessage(4);
				  }		
				  Constant.pieces_flag=true;
			       try{ 
						sleep(1000);
					}catch(Exception e)
					{
						e.printStackTrace();
					}
			       Constant.counter_flag=true;
			       new CounterThread().start();			   
			       try{
						sleep(4000);
					}catch(Exception e)
					{
						e.printStackTrace();
					}
			  }
			if(tempX>4.3f)
			{
				if((tempY*tempY+(tempX-4.3)*(tempX-4.3))>6.28f)
				{	
					reflect.set(new Vector3(Constant.i,Constant.j,Constant.k), new Vector3(4.3f-tempX,0-tempY,0));
					 Vector3 vec=reflect.getReflect();
					 Constant.i=(float) vec.x;
					 Constant.j=(float) vec.y;
					 tempX-=0.1f;
					 Constant.peng_flag=true;
					 Constant.baiTiaoDrawFlag=true;
					 Constant.lightX=tempX+0.5f;
					 Constant.lightY=tempY;
					 Constant.lightZ=tempZ;
						if(activity.yinXiao){
							activity.playSound(1, 0);
						}
						 crashflag=true;
				}
 
			}else if(tempX<-4.3f)
			{
				if((tempY*tempY+(tempX+4.3)*(tempX+4.3))>6.28f)
				{
					reflect.set(new Vector3(Constant.i,Constant.j,Constant.k), new Vector3(-4.3f-tempX,0-tempY,0));
					 Vector3 vec=reflect.getReflect();
					 Constant.i=(float) vec.x;
					 Constant.j=(float) vec.y;
					 tempX+=0.1f;
					 Constant.peng_flag=true;
					 Constant.baiTiaoDrawFlag=true;
					 Constant.lightX=tempX-0.5f;
					 Constant.lightY=tempY;
					 Constant.lightZ=tempZ;
						if(activity.yinXiao){
							activity.playSound(1, 0);
						}
						 crashflag=true;
				}
			}else if(tempY>2.58f)
			  {
				  	 reflect.set(new Vector3(Constant.i,Constant.j,Constant.k), new Vector3(0,-1,0));
					 Vector3 vec=reflect.getReflect();
					 Constant.i=(float) vec.x;
					 Constant.j=(float) vec.y;
					 tempY-=0.1f; 
					 Constant.peng_flag=true;
					 Constant.baiTiaoDrawFlag=true;
					 Constant.baiTiaoAngleId=1;
					 Constant.lightX=tempX;
					 Constant.lightY=tempY+0.5f;
					 Constant.lightZ=tempZ;
						if(activity.yinXiao){
							activity.playSound(1, 0);
						}
						 crashflag=true;
			  }else if(tempY<-2.58f) 
			  {
				  	 reflect.set(new Vector3(Constant.i,Constant.j,Constant.k), new Vector3(0,1,0));
					 Vector3 vec=reflect.getReflect();
					 Constant.i=(float) vec.x;
					 Constant.j=(float) vec.y;
					 tempY+=0.1f;
					 Constant.peng_flag=true;
					 Constant.baiTiaoDrawFlag=true;
					 Constant.baiTiaoAngleId=1;
					 Constant.lightX=tempX;
					 Constant.lightY=tempY-0.5f;
					 Constant.lightZ=tempZ;
						if(activity.yinXiao){
							activity.playSound(1, 0);
						}
						 crashflag=true;
			  }
		       Constant.ball_X=tempX;
		       Constant.ball_Y=tempY;
		       Constant.ball_Z=tempZ; 
		       CubeHit.cubeAABB(Constant.i,Constant.j, Constant.k,activity);
		       
		       if(Constant.second<=0.2)
		       {
		    	   Constant.second=0;
		       }else
		       {
			       Constant.second-=0.015f;
		       }
		       Constant.totalTime+=0.015f;
			}
			
		       try{
					sleep(Constant.threadTime);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
		}
		
	}
}
