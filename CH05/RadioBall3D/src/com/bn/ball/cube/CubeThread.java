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
		int count=0;//���O�[�t�׭p�ƾ�
		mv.rotate_flag=false;
		for(int i=0;i<Constant.VELOCITY.length;i++)
		{
			mv.loction_offset[i][0]=0;
			mv.loction_offset[i][1]=0;
			mv.loction_offset[i][2]=0;
		}
		while(Constant.THREAD_FLAG)
		{
			//��ɧ�sxyz��V�t��
			for(int i=0;i<Constant.VELOCITY.length;i++)
			{
				mv.loction_offset[i][0]+=Constant.VELOCITY[i].x;
				mv.loction_offset[i][1]+=Constant.VELOCITY[i].y-count*Constant.GRAVITY;
				mv.loction_offset[i][2]+=Constant.VELOCITY[i].z;
			}
			//��ɧ�s������ਤ��
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
