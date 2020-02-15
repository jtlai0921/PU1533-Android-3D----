package com.bn.box;

import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.dynamics.Body;

import com.bn.zxl.GameView;

public abstract class MyBody {
	
	public Body body;
	GameView gameview;
	float x;
	float y;
	float x11;
	float y11;
	float angle;
	//«Øºc¾¹
	public MyBody(Body body, GameView gameview) 
	{
		this.body = body;
		this.gameview=gameview;
	}
	public MyBody(float radius,float height,GameView gameview)
	{
		
	}
	public void drawSelf(GL10 gl)
	{
	}

	
}
