package com.bn.ball.wt;

import javax.microedition.khronos.opengles.GL10;

import com.bn.ball.WuTiDrawer;

public class YuanZhu extends WuTiDrawer{
	Circle bottomCircle;//����
	Circle topCircle;//����
	CircleSide circleSide;
	
	float xAngle=0;//¶x�b���઺����
    float yAngle=0;//¶y�b���઺����
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
		
		topCircle=new Circle(scale,r,n,topYuanId);	//�إ߳����ꪫ��
		bottomCircle=new Circle(scale,r,n,bottomYuanId);  //�إߩ����ꪫ��
		circleSide=new CircleSide(scale,r,h,n,circleSideId); //�إ߰����L����W����
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
