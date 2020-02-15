package com.bn.ball;

import javax.microedition.khronos.opengles.GL10;

public class WuTiForControl {
	WuTiDrawer wtd;//建立繪制總根類別的參考
	float zOffset;//平移量
	float xOffset;//旋轉角度
	float belongSaidao;//所屬的賽道
	float quanHao;//記錄圈號
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
		if(belongSaidao<currSaiDao-1||belongSaidao>currSaiDao+2)//確定繪制範圍
		{
			return;
		}
		gl.glPushMatrix();
		gl.glTranslatef(xOffset, -1f, zOffset);
		wtd.drawSelf(gl);
		gl.glPopMatrix();
	}
}
