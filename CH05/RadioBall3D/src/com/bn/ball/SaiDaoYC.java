package com.bn.ball;
import static com.bn.ball.Constant.*;

import javax.microedition.khronos.opengles.GL10;

public class SaiDaoYC 
{
	int groundId;
	int textureID;
	SaiDao sd;
	float totalg=Constant.GROUND_LENGTH/Constant.GROUND_L;//�a���`�Ӽ�
	static final float GCD=20;//��@�a������
	float totalgd=Constant.GUANDAO_LENGTH/Constant.GUANDAO_L;//�޽u�`�ӼơA���O�Ӽ�
	static final float GDCD=20;//��@�޽u����
	
	public SaiDaoYC(SaiDao sd,int groundId,int textureID)//�غc��
	{
		this.sd=sd;
		this.groundId=groundId;
		this.textureID=textureID;
	}
	public void drawSelf(GL10 gl)
	{
		//�D�X�ثe�ɹD�s���A�ɹD�s���q0�}�l��
		int dqgdbh= -(int)(BALL_Z/20);
		for(int i=0;i<totalgd;i++)
		{
			boolean flag=false;
			if(i<dqgdbh)
			{
				if(dqgdbh-i<=1)//�ثe�ɹD���e�@���ɹDø��
				{
					flag=true;
				}
			}
			if(i>=dqgdbh)
			{
				if(i-dqgdbh<=4)//�ثe�ɹD�e��4���ɹDø��
				{
					flag=true;
				}
			}
				
			if(flag)
			{
				gl.glPushMatrix();
				gl.glTranslatef(0, 0, -20*i);
				sd.drawSelf(gl);
				gl.glPopMatrix();
			}
		}
	}
}

