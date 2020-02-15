package com.bn.cube.game;
public class CubeThread extends Thread{

	Cube cube[];
	float [][]color;
	public CubeThread(Cube cube[],float[][] color)
	{
		this.cube=cube;
		this.color=color;
	}
	@Override
	public void run()
	{
		int i=2;
		while(Constant.cube_flag)
		{	
		  if(Constant.move_flag){
			i++;
			if(i==9)
			{
				i=3;
			}
		  for(int k=0;k<Constant.cube_length;k++){
		   float colors[]=new float[6*4];
		   for(int j=0;j<6;j++){
			   colors[4*j]=color[k][0]/10*i;
			   colors[4*j+1]=color[k][1]/10*i;
			   colors[4*j+2]=color[k][2]/10*i;
			   colors[4*j+3]=0f;
		   }
		   cube[k].mColorBuffer.clear();
		   cube[k].mColorBuffer.put(colors);// �V�w�R�Ϥ���J���I�ۦ���
		   cube[k].mColorBuffer.position(0);// �]�w�w�R�ϰ_�l��m
		  }
		  }
		   try{
			   sleep(200);
		   }catch(Exception e)
		   {
			   e.printStackTrace();
		   }
			
		}
	}
}
