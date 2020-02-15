package com.bn.ball;
import static com.bn.ball.Constant.*;

import javax.microedition.khronos.opengles.GL10;

public class SaiDaoYC 
{
	int groundId;
	int textureID;
	SaiDao sd;
	float totalg=Constant.GROUND_LENGTH/Constant.GROUND_L;//地面總個數
	static final float GCD=20;//單一地面長度
	float totalgd=Constant.GUANDAO_LENGTH/Constant.GUANDAO_L;//管線總個數，單位是個數
	static final float GDCD=20;//單一管線長度
	
	public SaiDaoYC(SaiDao sd,int groundId,int textureID)//建構器
	{
		this.sd=sd;
		this.groundId=groundId;
		this.textureID=textureID;
	}
	public void drawSelf(GL10 gl)
	{
		//求出目前賽道編號，賽道編號從0開始數
		int dqgdbh= -(int)(BALL_Z/20);
		for(int i=0;i<totalgd;i++)
		{
			boolean flag=false;
			if(i<dqgdbh)
			{
				if(dqgdbh-i<=1)//目前賽道的前一個賽道繪制
				{
					flag=true;
				}
			}
			if(i>=dqgdbh)
			{
				if(i-dqgdbh<=4)//目前賽道前面4個賽道繪制
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

