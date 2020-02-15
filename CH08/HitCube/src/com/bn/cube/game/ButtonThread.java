package com.bn.cube.game;

public class ButtonThread extends Thread{

	MySurfaceView mv;
	int ID;
	boolean flag;
	int sleepTime=200;
	float [][]color1={
			
			{0.97f,0.105f,0.04f,0},
			{0.61f,0.07f,0.027f,0}
	};
	float [][]color2={
			
			{0.234f,0.97f,0.04f,0}, 
			{0.15f,0.57f,0.035f,0}
	};
	float [][]color3={
			
			{0.066f,0.11f,0.95f,0},
			{0.03f,0.055f,0.55f,0}
	};
	float [][]color4={
			
			{0.95f,0.92f,0.035f,0},
			{0.57f,0.55f,0.02f,0}
	};
	public ButtonThread(MySurfaceView myView)
	{
			this.mv=(MySurfaceView)myView;
	}
	@Override 
	public void run()
	{
		int i=0;
		while(Constant.button_flag)
		{
			    if(flag)
			    { 
			    	i++;
			    	i=i%10;
			      float []colorx2=new float[mv.bgQuit.vCount*4];
			      float temp0=color2[0][0]-color2[1][0];
				   float temp1=color2[0][1]-color2[1][1];
				   float temp2=color2[0][2]-color2[1][2];
				      for(int j=0;j<mv.bgQuit.vCount;j++)
				      {
				    	  colorx2[4*j]=color2[0][0]-temp0/10.0f*i;
				    	  colorx2[4*j+1]=color2[0][1]-temp1/10.0f*i;
				    	  colorx2[4*j+2]=color2[0][2]-temp2/10.0f*i;
				    	  colorx2[4*j+3]=0;
				      }
			      mv.bgQuit.mColorBuffer.clear();
			      mv.bgQuit.mColorBuffer.put(colorx2);// 向緩沖區中放入頂點著色資料
			      mv.bgQuit.mColorBuffer.position(0);// 設定緩沖區起始位置
			      float []colorx1=new float[mv.bgQuit.vCount*4];
			      float tempx0=color1[0][0]-color1[1][0];
				   float tempx1=color1[0][1]-color1[1][1];
				   float tempx2=color1[0][2]-color1[1][2];
				      for(int j=0;j<mv.bgQuit.vCount;j++)
				      {
				    	  colorx1[4*j]=color1[0][0]-tempx0/10.0f*i;
				    	  colorx1[4*j+1]=color1[0][1]-tempx1/10.0f*i;
				    	  colorx1[4*j+2]=color1[0][2]-tempx2/10.0f*i;
				    	  colorx1[4*j+3]=0;
				      }
			      mv.bgExit.mColorBuffer.clear();
			      mv.bgExit.mColorBuffer.put(colorx1);// 向緩沖區中放入頂點著色資料
			      mv.bgExit.mColorBuffer.position(0);// 設定緩沖區起始位置
			      
			      mv.winbgQuit.mColorBuffer.clear();
			      mv.winbgQuit.mColorBuffer.put(colorx1);// 向緩沖區中放入頂點著色資料
			      mv.winbgQuit.mColorBuffer.position(0);// 設定緩沖區起始位置
			      
			      float []colorx3=new float[mv.bgQuit.vCount*4];
			      float tempy0=color4[0][0]-color4[1][0];
				   float tempy1=color4[0][1]-color4[1][1];
				   float tempy2=color4[0][2]-color4[1][2];
				      for(int j=0;j<mv.bgQuit.vCount;j++)
				      {
				    	  colorx3[4*j]=color4[0][0]-tempy0/10.0f*i;
				    	  colorx3[4*j+1]=color4[0][1]-tempy1/10.0f*i;
				    	  colorx3[4*j+2]=color4[0][2]-tempy2/10.0f*i;
				    	  colorx3[4*j+3]=0;
				      }
			      mv.bgGoon.mColorBuffer.clear();
			      mv.bgGoon.mColorBuffer.put(colorx3);// 向緩沖區中放入頂點著色資料
			      mv.bgGoon.mColorBuffer.position(0);// 設定緩沖區起始位置
			      
			      mv.bgNext.mColorBuffer.clear();
			      mv.bgNext.mColorBuffer.put(colorx3);// 向緩沖區中放入頂點著色資料
			      mv.bgNext.mColorBuffer.position(0);// 設定緩沖區起始位置
			      if(i==0)
			      {
			    	  flag=false;
			      }
			      sleepTime=200;
			    }
			    if(!flag){
			    if(mv.bgQuit.count<4)
			    {
					   mv.bgQuit.count++;
					   mv.bgGoon.count++; 
					   mv.bgExit.count++; 
					   mv.winbgQuit.count++;
					   mv.bgNext.count++;
			    }else
			    {
					   mv.bgQuit.count=0;
					   mv.bgGoon.count=0;
					   mv.bgExit.count=0;
					   mv.winbgQuit.count=0;
					   mv.bgNext.count=0;
				       flag=true;
			   }	
			    sleepTime=100;
			    }
				try{					
					sleep(sleepTime);
				}catch(Exception e)
				{
					e.printStackTrace();
				}							
		}
	}
}
