package com.bn.cube.view;

public class ContButtonThread extends Thread{
	ContView cv;
	float [][]color1={
			
			{0.97f,0.105f,0.04f,0},
			{0.61f,0.07f,0.027f,0}
	};
	float [][]color2={
		
		{0.066f,0.11f,0.95f,0},
		{0.03f,0.055f,0.55f,0}
	};
	float [][]color3={
		
			{0.234f,0.97f,0.04f,0}, 
			{0.15f,0.57f,0.035f,0}
	};
	boolean flag;
	int sleepTime=200;
	public ContButtonThread(MyGLSurfaceView mv)
	{
			this.cv=(ContView)mv;
	}
	@Override 
	public void run()
	{
		int i=0;
		while(Constant.contbutton_flag)
		{
			    if(flag)
			    { 
			    	i++;
			    	i=i%10;
			    	float[]levelcolor=new float[cv.bgLevel.vCount*4];
			        float temp0=color2[0][0]-color2[1][0];
				    float temp1=color2[0][1]-color2[1][1];
				    float temp2=color2[0][2]-color2[1][2];
				    for(int j=0;j<cv.bgLevel.vCount;j++)
				    {
				    	  levelcolor[4*j]=color2[0][0]-temp0/10.0f*i;
				    	  levelcolor[4*j+1]=color2[0][1]-temp1/10.0f*i;
				    	  levelcolor[4*j+2]=color2[0][2]-temp2/10.0f*i;
				    	  levelcolor[4*j+3]=0;  
				     }
				     cv.bgLevel.mColorBuffer.clear();
				     cv.bgLevel.mColorBuffer.put(levelcolor);// 向緩沖區中放入頂點著色資料
				     cv.bgLevel.mColorBuffer.position(0);// 設定緩沖區起始位置
			      float []colorx1=new float[cv.bgBack.vCount*4];
			      float tempx0=color3[0][0]-color3[1][0];
				  float tempx1=color3[0][1]-color3[1][1];
				  float tempx2=color3[0][2]-color3[1][2];
				  for(int j=0;j<cv.bgBack.vCount;j++)
				  {
				    	  colorx1[4*j]=color3[0][0]-tempx0/10.0f*i;
				    	  colorx1[4*j+1]=color3[0][1]-tempx1/10.0f*i;
				    	  colorx1[4*j+2]=color3[0][2]-tempx2/10.0f*i;
				    	  colorx1[4*j+3]=0;
				   }
				   cv.bgBack.mColorBuffer.clear();
				   cv.bgBack.mColorBuffer.put(colorx1);// 向緩沖區中放入頂點著色資料
				   cv.bgBack.mColorBuffer.position(0);// 設定緩沖區起始位置
				   cv.bgCont.mColorBuffer.clear();
				   cv.bgCont.mColorBuffer.put(colorx1);// 向緩沖區中放入頂點著色資料
				   cv.bgCont.mColorBuffer.position(0);// 設定緩沖區起始位置
			      float []colorx3=new float[cv.bgBoss.vCount*4];
			      float tempy0=color1[0][0]-color1[1][0];
				  float tempy1=color1[0][1]-color1[1][1];
				  float tempy2=color1[0][2]-color1[1][2];
				  for(int j=0;j<cv.bgBoss.vCount;j++)
				  {
				    	  colorx3[4*j]=color1[0][0]-tempy0/10.0f*i;
				    	  colorx3[4*j+1]=color1[0][1]-tempy1/10.0f*i;
				    	  colorx3[4*j+2]=color1[0][2]-tempy2/10.0f*i;
				    	  colorx3[4*j+3]=0;
				   }
				   cv.bgBoss.mColorBuffer.clear();
				   cv.bgBoss.mColorBuffer.put(colorx3);// 向緩沖區中放入頂點著色資料
				   cv.bgBoss.mColorBuffer.position(0);// 設定緩沖區起始位置
			      if(i==0)
			      {
			    	  flag=false;
			      }
			      sleepTime=200;
			    }
			    if(!flag){
			    	if(cv.bgBack.count<4)
					   {
						   cv.bgBack.count++;
						   cv.bgCont.count++;
						   cv.bgLevel.count++;
						   cv.bgBoss.count++;      
					   }else
					   {
						   cv.bgBack.count=0;
						   cv.bgCont.count=0;
						   cv.bgLevel.count=0;
						   cv.bgBoss.count=0;  
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
