package com.bn.box;

import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.dynamics.Body;

import com.bn.util.Constant;
import com.bn.zxl.GameView;

public final class Txing extends MyBody
{

	public Txing(Body body, GameView gameview) {
		super(body, gameview);
	}
	public void drawSelf(GL10 gl)
    {
		Constant.tXing.drawSelf(gl, body.getPosition().x, body.getPosition().y, (float) (-body.getAngle()*180/Math.PI), gameview);
    }
}
