package com.bn.GL20Tail;

import java.util.ArrayList;

import com.bn.cube.game.Constant;
import com.bn.cube.game.MySurfaceView;
import com.bn.cube.view.TextureRectNo;

 

public class GoThread extends Thread
{
	public Tail tail;
	public TailPart currTp; 
	public ArrayList<TailPart> garbage=new ArrayList<TailPart>();
	

	TextureRectNo ball;
	MySurfaceView mv;
	public boolean ballmove=true;
	
	
	public GoThread(Tail tail,TextureRectNo ball,MySurfaceView mv)
	{
		this.tail=tail;
		this.ball=ball;
		this.mv=mv;
	} 
	      
	public void run()
	{
		float ballPO[]={
				Constant.ball_X,
				Constant.ball_Y,
				Constant.ball_Z
			};  
		TailPart tp1=new TailPart
		(
				ballPO,
				ballPO,
			   0.82f
		);
		currTp=tp1;
		while(ballmove)
		{ 
			if(currTp!=null)
			{
				if(mv.ballThread.crashflag)//�Y�G�o�͸I�� �[�J�s��
				{
					float currballPO[]={
							Constant.ball_X,
							Constant.ball_Y,
							Constant.ball_Z
						};   
					TailPart tempTP=new TailPart
							(
									currballPO,
									currballPO,
								    0.82f
							); 
					garbage.add(currTp);//���ª��u�q ��i �o���M��
					
					new AlphaDownThread(currTp,this).start();
					
					
					currTp=tempTP; 
					mv.ballThread.crashflag=false;
				}
				else//�Y�G�S���I���N��s�ثe��m
				{
					for(int i=0;i<garbage.size();i++)
					{
						TailPart temptp=garbage.get(i);
						if(temptp.alpha<0.01)
						{
							garbage.remove(i);
						}
					}
					
					
					if(!mv.ballThread.failGame)
					{
						float tempcurrPO[]={
								Constant.ball_X,
								Constant.ball_Y,
								Constant.ball_Z
							};  
						currTp.endPoint=tempcurrPO;
					}else{
						
						   garbage.clear(); 
							// System.out.println("````````````````````clear the pl");
							   
							  
								   tail.pl.clear();
							 
							  
							   currTp =new TailPart
								(
										new float[]{0,0,-8},
										new float[]{0,0,-8},
									   0.82f
								);
					}
					
				}
				tail.pl.clear(); 
				for(int i=0;i<garbage.size();i++)
				{
					tail.pl.add(garbage.get(i));
				}
			    tail.pl.add(currTp);
				tail.pl.add(currTp); 
	           // if(tail.pl.size()>0)
	            
				 
	            	tail.genVertexData();
	            
				
			}
			
			try 
			{
				Thread.sleep(10);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
