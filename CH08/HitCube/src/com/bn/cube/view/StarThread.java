package com.bn.cube.view;

public class StarThread extends Thread{

    Star star;
	public StarThread(Star star)
	{
		this.star=star;
	}
	@Override
	public void run()
	{
		while(Constant.star_flag)
		{
			if(Constant.starY>=1.5)
			{
				Constant.starY=-1;
				star.vCount=(int) (Math.random()*25f+5);
		        float vertices[]=new float[star.vCount*3];
		        for(int i=0;i<star.vCount;i++){ 
		        	//隨機產生每個星星的xyz座標
		        	double tempX=(Math.random()-0.5f);
		        	double tempY=(Math.random()-0.5f);
		        	vertices[3*i]=(float) (star.width*tempX*2);
		        	vertices[3*i+1]=(float) (star.height*tempY*2);
		        	vertices[3*i+2]=0f;
		        	
		        } 
		        star.mVertexBuffer.clear();
		        star.mVertexBuffer.put(vertices);
		        star.mVertexBuffer.position(0);
				star.scale=(float) (Math.random()*9f+1);
			}
			Constant.starY+=0.2;
			
			try{
				sleep(50);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
}
