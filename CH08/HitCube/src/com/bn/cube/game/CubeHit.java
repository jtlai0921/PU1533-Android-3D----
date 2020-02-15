package com.bn.cube.game;

import com.bn.cube.view.HitCubeActivity;

public class CubeHit {
  float [][]cubeVertix;
  static ReflectUtil reflect=new ReflectUtil();
  static int count=0;
  public static void cubeAABB(float i,float j,float k,HitCubeActivity activity)
  {
	  for(float []cubes:CubeInfo.cubes)
	  {
		  if(cubes[4]==1)
		  {
			  continue;
		  }
		 float []dis=new float[10];
		 float x1=cubes[0]-Constant.cube_Width;
		 float y1=cubes[1]+Constant.cube_Height;
		 float z1=cubes[2]+Constant.cube_Depth;
		 
		 float x2=cubes[0]+Constant.cube_Width;
		 float y2=cubes[1]+Constant.cube_Height;
		 float z2=cubes[2]+Constant.cube_Depth;
		 
		 float x3=cubes[0]+Constant.cube_Width;
		 float y3=cubes[1]-Constant.cube_Height;
		 float z3=cubes[2]+Constant.cube_Depth;
		 
		 float x4=cubes[0]-Constant.cube_Width;
		 float y4=cubes[1]-Constant.cube_Height;
		 float z4=cubes[2]+Constant.cube_Depth;
		 
		 float x5=cubes[0];
		 float y5=cubes[1];
		 float z5=cubes[2]+Constant.cube_Depth;
		 
		 float x6=cubes[0]-Constant.cube_Width;
		 float y6=cubes[1]+Constant.cube_Height;
		 float z6=cubes[2]-Constant.cube_Depth;
		 
		 float x7=cubes[0]+Constant.cube_Width;
		 float y7=cubes[1]+Constant.cube_Height;
		 float z7=cubes[2]-Constant.cube_Depth;
		 
		 float x8=cubes[0]+Constant.cube_Width;
		 float y8=cubes[1]-Constant.cube_Height;
		 float z8=cubes[2]-Constant.cube_Depth;
		 
		 float x9=cubes[0]-Constant.cube_Width;
		 float y9=cubes[1]-Constant.cube_Height;
		 float z9=cubes[2]-Constant.cube_Depth;
		 
		
		 
		 float x10=cubes[0];
		 float y10=cubes[1];
		 float z10=cubes[2]-Constant.cube_Depth;
		 
		 
		 dis[0]=(x1-Constant.ball_X)*(x1-Constant.ball_X)+(y1-Constant.ball_Y)*(y1-Constant.ball_Y)+(z1-Constant.ball_Z)*(z1-Constant.ball_Z);
		 dis[1]=(x2-Constant.ball_X)*(x2-Constant.ball_X)+(y2-Constant.ball_Y)*(y2-Constant.ball_Y)+(z2-Constant.ball_Z)*(z2-Constant.ball_Z);
		 dis[2]=(x3-Constant.ball_X)*(x3-Constant.ball_X)+(y3-Constant.ball_Y)*(y3-Constant.ball_Y)+(z3-Constant.ball_Z)*(z3-Constant.ball_Z);
		 dis[3]=(x4-Constant.ball_X)*(x4-Constant.ball_X)+(y4-Constant.ball_Y)*(y4-Constant.ball_Y)+(z4-Constant.ball_Z)*(z4-Constant.ball_Z);
		 dis[4]=(x5-Constant.ball_X)*(x5-Constant.ball_X)+(y5-Constant.ball_Y)*(y5-Constant.ball_Y)+(z5-Constant.ball_Z)*(z5-Constant.ball_Z);
		 dis[5]=(x6-Constant.ball_X)*(x6-Constant.ball_X)+(y6-Constant.ball_Y)*(y6-Constant.ball_Y)+(z6-Constant.ball_Z)*(z6-Constant.ball_Z);
		 dis[6]=(x7-Constant.ball_X)*(x7-Constant.ball_X)+(y7-Constant.ball_Y)*(y7-Constant.ball_Y)+(z7-Constant.ball_Z)*(z7-Constant.ball_Z);
		 dis[7]=(x8-Constant.ball_X)*(x8-Constant.ball_X)+(y8-Constant.ball_Y)*(y8-Constant.ball_Y)+(z8-Constant.ball_Z)*(z8-Constant.ball_Z);
		 dis[8]=(x9-Constant.ball_X)*(x9-Constant.ball_X)+(y9-Constant.ball_Y)*(y9-Constant.ball_Y)+(z9-Constant.ball_Z)*(z9-Constant.ball_Z);
		 dis[9]=(x10-Constant.ball_X)*(x10-Constant.ball_X)+(y10-Constant.ball_Y)*(y10-Constant.ball_Y)+(z10-Constant.ball_Z)*(z10-Constant.ball_Z);
		 float minDis=dis[0];
		 for(int m=0;m<dis.length;m++){
			 
			 if(dis[m]<minDis)
			 {
				 minDis=dis[m];
				 count=m+1;
			 }	 
		 } 
		if(minDis<0.15) 
		{ 
			Constant.cubePieceDrawFlag=true;
			Constant.sx=cubes[0];
			Constant.sy=cubes[1];
			Constant.sz=cubes[2];
			
			if(activity.yinXiao){
				activity.playSound(2, 0);
			}
			
			cubes[4]=1;
			Constant.sumBoardScore+=100;
			Constant.scoreNumberbase++;
			Constant.threadTime=15;
			Constant.dangBanBigFlag=false;
			if(cubes[5]!=-1f)
			{
				if(Constant.scoreNumberbase<CubeInfo.cubeNum&&activity.yinXiao)
				{
					activity.playSound(3,0);
				}				
				Constant.daojuX=cubes[0];
				Constant.daojuY=cubes[1];
				Constant.daojuZ=cubes[2];
				Constant.daojuId=(int)cubes[5];
				if(cubes[5]==0)
				{
					Constant.dangBanBigFlag=true;
				}else if(cubes[5]==1)
				{
					Constant.threadTime=8;
				}else if(cubes[5]==2)
				{
					Constant.threadTime=50;
				}else if(cubes[5]==3)
				{
					Constant.deFenDrawFlag=true;
					Constant.deFenJianFlag=true;
					if(Constant.sumBoardScore>200)
					{
						Constant.sumBoardScore-=200;
						
					}else{
						Constant.sumBoardScore=0;
					}
					
				}else if(cubes[5]==4)
				{
					Constant.deFenDrawFlag=true;
					Constant.deFenJiaFlag=true;
					Constant.sumBoardScore+=500;
				}
				Constant.drawdaojuFlag=true;
				Constant.daojuThreadFlag=true;
				new DaoJuThread().start();
				
			}
			if(count<=5)
			{
				reflect.set(new Vector3(i,j,k), new Vector3(0,0,1));
				 Vector3 vec=reflect.getReflect();
				 Constant.i=(float) vec.x;
				 Constant.j=(float) vec.y;
				 Constant.k=-Constant.k;
				 Constant.ball_Z+=0.2f;
				 break;
			}else{
				 reflect.set(new Vector3(i,j,k), new Vector3(0,0,-1));
				 Vector3 vec=reflect.getReflect();
				 Constant.i=(float) vec.x;
				 Constant.j=(float) vec.y;
				 Constant.k=-Constant.k;
				 Constant.ball_Z-=0.2f;
				 break;
			}
			
			
		}
	  }
  }
  
}
