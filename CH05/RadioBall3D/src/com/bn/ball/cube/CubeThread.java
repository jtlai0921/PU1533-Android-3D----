package com.bn.ball.cube;
import com.bn.ball.MySurfaceView;

public class CubeThread extends Thread
{
	private MySurfaceView mv;

	public CubeThread(MySurfaceView mv)
	{
		this.mv=mv;
	}
	
	@Override
	public void run()
	{
		int count=0;//重力加速度計數器
		mv.rotate_flag=false;
		for(int i=0;i<Constant.VELOCITY.length;i++)
		{
			mv.loction_offset[i][0]=0;
			mv.loction_offset[i][1]=0;
			mv.loction_offset[i][2]=0;
		}
		while(Constant.THREAD_FLAG)
		{
			//實時更新xyz方向速度
			for(int i=0;i<Constant.VELOCITY.length;i++)
			{
				mv.loction_offset[i][0]+=Constant.VELOCITY[i].x;
				mv.loction_offset[i][1]+=Constant.VELOCITY[i].y-count*Constant.GRAVITY;
				mv.loction_offset[i][2]+=Constant.VELOCITY[i].z;
			}
			//實時更新方塊旋轉角度
			for(int j=0;j<Constant.ANGLE.length;j++)
			{
				mv.angle_offset[j][0]+=Constant.ANGLE[j][0];
				mv.angle_offset[j][1]=Constant.ANGLE[j][1];
				mv.angle_offset[j][2]=Constant.ANGLE[j][2];
				mv.angle_offset[j][3]=Constant.ANGLE[j][3];
			}
			
			try
			{
				sleep(30);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			count++;
			if(count>=30){
				Constant.THREAD_FLAG=false;
				
			}
		}
	}
}
