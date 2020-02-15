package com.bn.box;

import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.dynamics.Body;
import static com.bn.util.Constant.*;
import com.bn.zxl.GameView;

public class Rain extends MyBody
{
	Body body;
	GameView gameview;
	float halfwidth;
	public Rain(Body body, GameView gameview) {
		super(body, gameview);
		this.body=body;
		this.gameview=gameview;
	}
	public void drawSelf(GL10 gl)
	{
		Rain.drawSelf(gl, body, gameview.textureId_stone9,gameview.textureId_stone10);
	}

}
