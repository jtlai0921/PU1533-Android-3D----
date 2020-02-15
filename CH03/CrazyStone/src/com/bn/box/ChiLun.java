package com.bn.box;

import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.dynamics.Body;

import com.bn.util.Constant;
import com.bn.util.From2DTo3DUtil;
import com.bn.zxl.GameView;

public class ChiLun extends MyBody{
	
	GameView gameview;
	float radius;
	float deepth;
	ChiLun(Body body,float radius,float deepth,GameView gameview)
	{
		super(body,gameview);
		this.gameview=gameview;
		this.radius=radius;
		this.deepth=deepth;
    }
	
	public void drawSelf(GL10 gl)
    {
        Constant.ChiLun.drawSelf(gl, gameview.textureId_di, From2DTo3DUtil.point3D(this.body.getPosition().x, this.body.getPosition().y), -From2DTo3DUtil.k2d_3d(deepth),body.getAngle());

        //°¼¾À
		Constant.ChiLun_Cebi.drawSelf(gl, this.body.getPosition().x, this.body.getPosition().y,body.getAngle(),gameview.textureId);
		//©³
        Constant.ChiLun.drawSelf(gl, gameview.textureId_di, From2DTo3DUtil.point3D(this.body.getPosition().x, this.body.getPosition().y), -From2DTo3DUtil.k2d_3d(0),body.getAngle());
    }

}

