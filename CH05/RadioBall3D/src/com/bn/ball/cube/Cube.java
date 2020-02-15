package com.bn.ball.cube;

import javax.microedition.khronos.opengles.GL10;

import com.bn.ball.MySurfaceView;


//�ߤ���
public class Cube 
{
	//�Ω�ø��U�ӭ����m��x��
	Rectangular cr;
	float UNIT_SIZE=0.3f;
	public Cube(MySurfaceView mv,int wallId)
	{
		//�إߥΩ�ø��U�ӭ����m��x��
		cr=new Rectangular(mv,wallId);
	}
	
	public void drawSelf(GL10 gl)
	{
		//�`ø���Q�G�z�L��@�ӱm��x�α��ಾ���ߤ���C�ӭ�����m
		//ø��ߤ��骺�C�ӭ�
		
		//�O�@�{��
		gl.glPushMatrix();
		
		//ø��e�p��
		gl.glPushMatrix();
		gl.glTranslatef(0, 0, UNIT_SIZE);
		cr.drawSelf(gl);		
		gl.glPopMatrix();
		
		//ø���p��
		gl.glPushMatrix();		
		gl.glTranslatef(0, 0, -UNIT_SIZE);
		gl.glRotatef(180, 0, 1, 0);
		cr.drawSelf(gl);		
		gl.glPopMatrix();
		
		//ø��W�j��
		gl.glPushMatrix();	
		gl.glTranslatef(0,UNIT_SIZE,0);
		gl.glRotatef(-90, 1, 0, 0);
		cr.drawSelf(gl);
		gl.glPopMatrix();
		
		//ø��U�j��
		gl.glPushMatrix();	
		gl.glTranslatef(0,-UNIT_SIZE,0);
		gl.glRotatef(90, 1, 0, 0);
		cr.drawSelf(gl);
		gl.glPopMatrix();
		
		//ø��j��
		gl.glPushMatrix();	
		gl.glTranslatef(UNIT_SIZE,0,0);
		gl.glRotatef(-90, 1, 0, 0);
		gl.glRotatef(90, 0, 1, 0);
		cr.drawSelf(gl);
		gl.glPopMatrix();
		
		//ø��k�j��
		gl.glPushMatrix();				
		gl.glTranslatef(-UNIT_SIZE,0,0);
		gl.glRotatef(90, 1, 0, 0);
		gl.glRotatef(-90, 0, 1, 0);
		cr.drawSelf(gl);
		gl.glPopMatrix();
		
		//�٭�{��
		gl.glPopMatrix();
	}
	

}
