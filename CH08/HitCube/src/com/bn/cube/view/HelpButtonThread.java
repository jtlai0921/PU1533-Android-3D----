package com.bn.cube.view;

public class HelpButtonThread extends Thread{

	HelpView mv;
	boolean flag;
	int sleepTime=200;

	float [][]color1={
			{0f,0.95f,0.95f,0},
			{0.06f,0.53f,0.53f,0}
	};
	public HelpButtonThread(MyGLSurfaceView myView)
	{
			this.mv=(HelpView)myView;
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
			      float []colorx2=new float[mv.bgButton.vCount*4];
			      float temp0=color1[0][0]-color1[1][0];
				   float temp1=color1[0][1]-color1[1][1];
				   float temp2=color1[0][2]-color1[1][2];
				      for(int j=0;j<mv.bgButton.vCount;j++)
				      {
				    	  colorx2[4*j]=color1[0][0]-temp0/10.0f*i;
				    	  colorx2[4*j+1]=color1[0][1]-temp1/10.0f*i;
				    	  colorx2[4*j+2]=color1[0][2]-temp2/10.0f*i;
				    	  colorx2[4*j+3]=0;
				      }
			      mv.bgButton.mColorBuffer.clear();
			      mv.bgButton.mColorBuffer.put(colorx2);// 向緩沖區中放入頂點著色資料
			      mv.bgButton.mColorBuffer.position(0);// 設定緩沖區起始位置
			      
			      if(i==0)
			      {
			    	  flag=false;
			      }
			      sleepTime=200;
			    }
			    if(!flag){
			    if(mv.bgButton.count<4)
			    {
					   mv.bgButton.count++;
			    }else
			    {
					   mv.bgButton.count=0;

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
