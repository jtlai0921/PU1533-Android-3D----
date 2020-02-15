package com.bn.cube.view;
public class LineThread extends Thread{

	ButtonLine[] buttonLine;
	boolean line;

	public LineThread(ButtonLine buttonLine[] )
	{
		this.buttonLine=buttonLine;
	}
	@Override
	public void run()
	{
		while(Constant.line_frag)
		{	
		   float colors[]=new float[4*buttonLine[0].vCount];
		   if(line){
			   for(int i=0;i<buttonLine[0].vCount;i++)
			   {
				   colors[4*i]=0;
				   colors[4*i+1]=0; 
				   colors[4*i+2]=1;
				   colors[4*i+3]=0f;
			   }
			 
			   line=false;

		   }else
		   {
			   for(int i=0;i<buttonLine[0].vCount;i++)
			   {
				   colors[4*i]=0.8f;
				   colors[4*i+1]=0.8f; 
				   colors[4*i+2]=0.8f;
				   colors[4*i+3]=0f;
			   }
			   line=true;
		   }
		   for(int i=0;i<buttonLine.length;i++)
		   {
			   buttonLine[i].mColorBuffer.clear();
			   buttonLine[i].mColorBuffer.put(colors);// �V�w�R�Ϥ���J���I�ۦ���
			   buttonLine[i].mColorBuffer.position(0);// �]�w�w�R�ϰ_�l��m
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
