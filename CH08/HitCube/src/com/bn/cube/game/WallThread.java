package com.bn.cube.game;

public class WallThread extends Thread{

	Wall wall;
	boolean line;

	public WallThread(Wall wall)
	{
		this.wall=wall;
	}
	@Override
	public void run()
	{
		while(Constant.wall_flag)
		{	
			if(Constant.move_flag)
			{
		   float colors[]=new float[4*wall.vCount];
		   if(line){
			   for(int i=0;i<wall.vCount;i++)
			   {
				   colors[4*i]=1;
				   colors[4*i+1]=0; 
				   colors[4*i+2]=0;
				   colors[4*i+3]=0f;
			   }
			 
			   line=false;

		   }else
		   {
			   for(int i=0;i<wall.vCount;i++)
			   {
				   colors[4*i]=0f;
				   colors[4*i+1]=0f; 
				   colors[4*i+2]=0f;
				   colors[4*i+3]=0f;
			   }
			   line=true;
		   }
			   wall.mColorBuffer.clear();
			   wall.mColorBuffer.put(colors);// 向緩沖區中放入頂點著色資料
			   wall.mColorBuffer.position(0);// 設定緩沖區起始位置
		   
			}
		  
		   try{
			   sleep(600);
		   }catch(Exception e)
		   {
			   e.printStackTrace();
		   }
		
		}
	}
}

