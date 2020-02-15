package com.bn.box;

import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.dynamics.Body;

import com.bn.util.Constant;
import com.bn.util.From2DTo3DUtil;
import com.bn.zxl.GameView;

public class MuTong extends MyBody
{
	Body body;
	GameView gameview;
	float halfHeight;
	public MuTong(Body body, float halfHeight,GameView gameview) {
		super(body, gameview);
		this.body=body;
		this.halfHeight=halfHeight;
		this.gameview=gameview;
	}
	public void drawSelf(GL10 gl)
	{
		Constant.Mutong_cebi.drawSelf(gl, body.getAngle(),body.getPosition().x, body.getPosition().y,gameview.textureId_mutong);
		Constant.MuTong_di_1.drawSelf(gl,body.getAngle(), gameview.textureId_di, From2DTo3DUtil.point3D(body.getPosition().x,body.getPosition().y));
		Constant.MuTong_di.drawSelf(gl,body.getAngle(), gameview.textureId_di, From2DTo3DUtil.point3D(body.getPosition().x,body.getPosition().y));
	}
	
}
