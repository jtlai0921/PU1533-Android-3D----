package com.bn.ball.cube;

import javax.microedition.khronos.opengles.GL10;

import com.bn.ball.MySurfaceView;


//立方體
public class Cube 
{
	//用於繪制各個面的彩色矩形
	Rectangular cr;
	float UNIT_SIZE=0.3f;
	public Cube(MySurfaceView mv,int wallId)
	{
		//建立用於繪制各個面的彩色矩形
		cr=new Rectangular(mv,wallId);
	}
	
	public void drawSelf(GL10 gl)
	{
		//總繪制思想：透過把一個彩色矩形旋轉移位到立方體每個面的位置
		//繪制立方體的每個面
		
		//保護現場
		gl.glPushMatrix();
		
		//繪制前小面
		gl.glPushMatrix();
		gl.glTranslatef(0, 0, UNIT_SIZE);
		cr.drawSelf(gl);		
		gl.glPopMatrix();
		
		//繪制後小面
		gl.glPushMatrix();		
		gl.glTranslatef(0, 0, -UNIT_SIZE);
		gl.glRotatef(180, 0, 1, 0);
		cr.drawSelf(gl);		
		gl.glPopMatrix();
		
		//繪制上大面
		gl.glPushMatrix();	
		gl.glTranslatef(0,UNIT_SIZE,0);
		gl.glRotatef(-90, 1, 0, 0);
		cr.drawSelf(gl);
		gl.glPopMatrix();
		
		//繪制下大面
		gl.glPushMatrix();	
		gl.glTranslatef(0,-UNIT_SIZE,0);
		gl.glRotatef(90, 1, 0, 0);
		cr.drawSelf(gl);
		gl.glPopMatrix();
		
		//繪制左大面
		gl.glPushMatrix();	
		gl.glTranslatef(UNIT_SIZE,0,0);
		gl.glRotatef(-90, 1, 0, 0);
		gl.glRotatef(90, 0, 1, 0);
		cr.drawSelf(gl);
		gl.glPopMatrix();
		
		//繪制右大面
		gl.glPushMatrix();				
		gl.glTranslatef(-UNIT_SIZE,0,0);
		gl.glRotatef(90, 1, 0, 0);
		gl.glRotatef(-90, 0, 1, 0);
		cr.drawSelf(gl);
		gl.glPopMatrix();
		
		//還原現場
		gl.glPopMatrix();
	}
	

}
