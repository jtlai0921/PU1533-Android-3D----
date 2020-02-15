package com.bn.cube.view;

public class SoundButtonThread extends Thread{

	SoundView sv;
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
	public SoundButtonThread(SoundView sv)
	{
			this.sv=sv;
	}
	@Override 
	public void run()
	{
		int i=0;
		while(Constant.soundbutton_flag)
		{
			    if(flag)
			    { 
			    	i++;
			    	i=i%10;
			      float []colorx2=new float[sv.bgBack.vCount*4];
			      float temp0=color2[0][0]-color2[1][0];
				   float temp1=color2[0][1]-color2[1][1];
				   float temp2=color2[0][2]-color2[1][2];
				      for(int j=0;j<sv.bgBack.vCount;j++)
				      {
				    	  colorx2[4*j]=color2[0][0]-temp0/10.0f*i;
				    	  colorx2[4*j+1]=color2[0][1]-temp1/10.0f*i;
				    	  colorx2[4*j+2]=color2[0][2]-temp2/10.0f*i;
				    	  colorx2[4*j+3]=0;
				      }
				  sv.bgBack.mColorBuffer.clear();
				  sv.bgBack.mColorBuffer.put(colorx2);// 向緩沖區中放入頂點著色資料
				  sv.bgBack.mColorBuffer.position(0);// 設定緩沖區起始位置
			      float []colorx1=new float[sv.bgBack.vCount*4];
			      float tempx0=color4[0][0]-color4[1][0];
				   float tempx1=color4[0][1]-color4[1][1];
				   float tempx2=color4[0][2]-color4[1][2];
				      for(int j=0;j<sv.bgBack.vCount;j++)
				      {
				    	  colorx1[4*j]=color4[0][0]-tempx0/10.0f*i;
				    	  colorx1[4*j+1]=color4[0][1]-tempx1/10.0f*i;
				    	  colorx1[4*j+2]=color4[0][2]-tempx2/10.0f*i;
				    	  colorx1[4*j+3]=0;
				      }
			      sv.bgMusic.mColorBuffer.clear();
			      sv.bgMusic.mColorBuffer.put(colorx1);// 向緩沖區中放入頂點著色資料
			      sv.bgMusic.mColorBuffer.position(0);// 設定緩沖區起始位置
			      
			      sv.bgSound.mColorBuffer.clear();
			      sv.bgSound.mColorBuffer.put(colorx1);// 向緩沖區中放入頂點著色資料
			      sv.bgSound.mColorBuffer.position(0);// 設定緩沖區起始位置
			      
			      float []colorx3=new float[sv.bgBack.vCount*4];
			      float tempy0=color5[0][0]-color5[1][0];
				   float tempy1=color5[0][1]-color5[1][1];
				   float tempy2=color5[0][2]-color5[1][2];
				      for(int j=0;j<sv.bgBack.vCount;j++)
				      {
				    	  colorx3[4*j]=color5[0][0]-tempy0/10.0f*i;
				    	  colorx3[4*j+1]=color5[0][1]-tempy1/10.0f*i;
				    	  colorx3[4*j+2]=color5[0][2]-tempy2/10.0f*i;
				    	  colorx3[4*j+3]=0;
				      }
			      sv.bgMusicButton.mColorBuffer.clear();
			      sv.bgMusicButton.mColorBuffer.put(colorx3);// 向緩沖區中放入頂點著色資料
			      sv.bgMusicButton.mColorBuffer.position(0);// 設定緩沖區起始位置
			      
			      sv.bgSoundButton.mColorBuffer.clear();
			      sv.bgSoundButton.mColorBuffer.put(colorx3);
			      sv.bgSoundButton.mColorBuffer.position(0);
			      
			      if(i==0)
			      {
			    	  flag=false;
			      }
			      sleepTime=200;
			    }
			    if(!flag){
			    if(sv.bgBack.count<4)
			    {
					   sv.bgBack.count++;
					   sv.bgMusic.count++;
					   sv.bgSound.count++;
					   sv.bgMusicButton.count++;
					   sv.bgSoundButton.count++;

			    }else
			    {
					   sv.bgBack.count=0;
					   sv.bgMusic.count=0;
					   sv.bgSound.count=0;
					   sv.bgMusicButton.count=0;
					   sv.bgSoundButton.count=0;
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
