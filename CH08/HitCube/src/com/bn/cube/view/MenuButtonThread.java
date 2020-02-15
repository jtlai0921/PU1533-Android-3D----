package com.bn.cube.view;

public class MenuButtonThread extends Thread{

	MenuView mv;
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
	float [][]color5={
			{0f,0.95f,0.95f,0},
			{0.06f,0.53f,0.53f,0}
	};
	public MenuButtonThread(MyGLSurfaceView myView)
	{
			this.mv=(MenuView)myView;
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
			      float []colorx2=new float[mv.bgNew.vCount*4];
			      float temp0=color2[0][0]-color2[1][0];
				   float temp1=color2[0][1]-color2[1][1];
				   float temp2=color2[0][2]-color2[1][2];
				      for(int j=0;j<mv.bgNew.vCount;j++)
				      {
				    	  colorx2[4*j]=color2[0][0]-temp0/10.0f*i;
				    	  colorx2[4*j+1]=color2[0][1]-temp1/10.0f*i;
				    	  colorx2[4*j+2]=color2[0][2]-temp2/10.0f*i;
				    	  colorx2[4*j+3]=0;
				      }
			      mv.bgNew.mColorBuffer.clear();
			      mv.bgNew.mColorBuffer.put(colorx2);// 向緩沖區中放入頂點著色資料
			      mv.bgNew.mColorBuffer.position(0);// 設定緩沖區起始位置
			      float []colorx1=new float[mv.bgNew.vCount*4];
			      float tempx0=color1[0][0]-color1[1][0];
				   float tempx1=color1[0][1]-color1[1][1];
				   float tempx2=color1[0][2]-color1[1][2];
				      for(int j=0;j<mv.bgNew.vCount;j++)
				      {
				    	  colorx1[4*j]=color1[0][0]-tempx0/10.0f*i;
				    	  colorx1[4*j+1]=color1[0][1]-tempx1/10.0f*i;
				    	  colorx1[4*j+2]=color1[0][2]-tempx2/10.0f*i;
				    	  colorx1[4*j+3]=0;
				      }
			      mv.bgExit.mColorBuffer.clear();
			      mv.bgExit.mColorBuffer.put(colorx1);// 向緩沖區中放入頂點著色資料
			      mv.bgExit.mColorBuffer.position(0);// 設定緩沖區起始位置
			      
			      float []colorx3=new float[mv.bgNew.vCount*4];
			      float tempy0=color4[0][0]-color4[1][0];
				   float tempy1=color4[0][1]-color4[1][1];
				   float tempy2=color4[0][2]-color4[1][2];
				      for(int j=0;j<mv.bgNew.vCount;j++)
				      {
				    	  colorx3[4*j]=color4[0][0]-tempy0/10.0f*i;
				    	  colorx3[4*j+1]=color4[0][1]-tempy1/10.0f*i;
				    	  colorx3[4*j+2]=color4[0][2]-tempy2/10.0f*i;
				    	  colorx3[4*j+3]=0;
				      }
			      mv.bgCont.mColorBuffer.clear();
			      mv.bgCont.mColorBuffer.put(colorx3);// 向緩沖區中放入頂點著色資料
			      mv.bgCont.mColorBuffer.position(0);// 設定緩沖區起始位置
			      
			      float []colorx=new float[mv.bgNew.vCount*4];
			      float tempn0=color5[0][0]-color5[1][0];
				   float tempn1=color5[0][1]-color5[1][1];
				   float tempn2=color5[0][2]-color5[1][2];
				      for(int j=0;j<mv.bgNew.vCount;j++)
				      {
				    	  colorx[4*j]=color5[0][0]-tempn0/10.0f*i;
				    	  colorx[4*j+1]=color5[0][1]-tempn1/10.0f*i;
				    	  colorx[4*j+2]=color5[0][2]-tempn2/10.0f*i;
				    	  colorx[4*j+3]=0;
				      }
			      mv.bgSet.mColorBuffer.clear();
			      mv.bgSet.mColorBuffer.put(colorx);// 向緩沖區中放入頂點著色資料
			      mv.bgSet.mColorBuffer.position(0);// 設定緩沖區起始位置
			      
			      mv.bgHelp.mColorBuffer.clear();
			      mv.bgHelp.mColorBuffer.put(colorx);// 向緩沖區中放入頂點著色資料
			      mv.bgHelp.mColorBuffer.position(0);// 設定緩沖區起始位置
			      
			      if(i==0)
			      {
			    	  flag=false;
			      }
			      sleepTime=200;
			    }
			    if(!flag){
			    if(mv.bgNew.count<4)
			    {
					   mv.bgNew.count++;
					   mv.bgCont.count++; 
					   mv.bgExit.count++;  
					   mv.bgSet.count++;
			    }else
			    {
					   mv.bgNew.count=0;
					   mv.bgCont.count=0;
					   mv.bgExit.count=0;
					   mv.bgSet.count=0;
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
