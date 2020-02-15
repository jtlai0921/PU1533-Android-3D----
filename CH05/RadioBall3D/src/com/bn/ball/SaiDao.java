package com.bn.ball;

import javax.microedition.khronos.opengles.GL10;

public class SaiDao 
{
	GuanDao gd;
	Ground ground;
	
	int groundId;//地面紋理id
	int textureID;//管線id
	int height;
	
	public SaiDao(MySurfaceView mv,int groundId,int textureID,int height)
	{
		this.groundId=groundId;
		this.textureID=textureID;
		
		gd=new GuanDao(mv,3);
		ground=new Ground(textureID);
	}
	public void drawSelf(GL10 gl)
	{
		gl.glPushMatrix();
        gl.glTranslatef(0,-1.5f,0);
        ground.drawSelf(gl);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(0,0,0);
        gd.drawSelf(gl);
        gl.glPopMatrix();
	}
}

