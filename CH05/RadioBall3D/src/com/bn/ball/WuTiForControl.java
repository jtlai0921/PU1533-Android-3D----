package com.bn.ball;

import javax.microedition.khronos.opengles.GL10;

public class WuTiForControl {
	WuTiDrawer wtd;//�إ�ø���`�����O���Ѧ�
	float zOffset;//�����q
	float xOffset;//���ਤ��
	float belongSaidao;//���ݪ��ɹD
	float quanHao;//�O���鸹
	int id;
	public WuTiForControl(WuTiDrawer wtd, float zOffset,float xOffset,int id,float quanhao)
	{
		this.wtd=wtd;
		this.zOffset=zOffset;
		this.xOffset=xOffset;
		this.quanHao=quanhao;
		this.id=id;
		belongSaidao=-zOffset/20.0f;
	}
	
	public void drawSelf(GL10 gl,float currSaiDao)
	{
		if(belongSaidao<currSaiDao-1||belongSaidao>currSaiDao+2)//�T�wø��d��
		{
			return;
		}
		gl.glPushMatrix();
		gl.glTranslatef(xOffset, -1f, zOffset);
		wtd.drawSelf(gl);
		gl.glPopMatrix();
	}
}
