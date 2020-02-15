package com.bn.cube.game;

public class LoseButtonThread extends Thread{

	LoseView lv;
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
	public LoseButtonThread(LoseView lv)
	{
			this.lv=lv;
	}
	@Override 
	public void run()
	{
		int i=0;
		while(Constant.losebutton_frag)
		{
			    if(flag)  
			    { 
			    	i++;
			    	i=i%10;
			      float []colorx2=new float[lv.bgQuit.vCount*4];
			      float temp0=color1[0][0]-color1[1][0];
				   float temp1=color1[0][1]-color1[1][1];
				   float temp2=color1[0][2]-color1[1][2];
				      for(int j=0;j<lv.bgQuit.vCount;j++)
				      {
				    	  colorx2[4*j]=color2[0][0]-temp0/10.0f*i;
				    	  colorx2[4*j+1]=color2[0][1]-temp1/10.0f*i;
				    	  colorx2[4*j+2]=color2[0][2]-temp2/10.0f*i;
				    	  colorx2[4*j+3]=0;
				      }
			      lv.bgQuit.mColorBuffer.clear();
			      lv.bgQuit.mColorBuffer.put(colorx2);// 向緩沖區中放入頂點著色資料
			      lv.bgQuit.mColorBuffer.position(0);// 設定緩沖區起始位置
			      if(i==0)
			      {
			    	  flag=false;
			      }
			      sleepTime=200;
			    }
			    if(!flag){
			    if(lv.bgQuit.count<4)
			    {
					   lv.bgQuit.count++;
			    }else
			    {
					   lv.bgQuit.count=0;
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
