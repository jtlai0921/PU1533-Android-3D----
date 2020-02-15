package com.bn.ball.wt;

import javax.microedition.khronos.opengles.GL10;

import com.bn.ball.WuTiDrawer;

public class YuanZhu extends WuTiDrawer{
	Circle bottomCircle;//底圓
	Circle topCircle;//頂圓
	CircleSide circleSide;
	
	float xAngle=0;//繞x軸旋轉的角度
    float yAngle=0;//繞y軸旋轉的角度
	float h;
	float scale;
	int topYuanId;
	int bottomYuanId;
	int circleSideId;
	
	public YuanZhu(float scale,float r, float h,int n,
			int topYuanId, int bottomYuanId, int circleSideId)
	{
		
		this.h=h;
		this.scale=scale;
		this.topYuanId=topYuanId;
		this.bottomYuanId=bottomYuanId;
		this.circleSideId=circleSideId;
		
		topCircle=new Circle(scale,r,n,topYuanId);	//建立頂面圓物件
		bottomCircle=new Circle(scale,r,n,bottomYuanId);  //建立底面圓物件
		circleSide=new CircleSide(scale,r,h,n,circleSideId); //建立側面無頂圓柱物件
	}
	@Override
	public void drawSelf(GL10 gl)
	{
		gl.glPushMatrix();
		gl.glTranslatef(-0.02f, h/2*scale, 0);
		gl.glRotatef(-90, 1, 0, 0);
		topCircle.drawSelf(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(-0.02f, -h/2*scale, 0);
		gl.glRotatef(90, 1, 0, 0);
		gl.glRotatef(180, 0, 0, 1);
		bottomCircle.drawSelf(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(-0.02f, -h/2*scale, 0);
		circleSide.drawSelf(gl);
		gl.glPopMatrix();
	}
	
}
