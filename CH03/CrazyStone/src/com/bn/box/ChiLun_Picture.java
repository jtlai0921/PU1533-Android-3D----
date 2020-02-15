package com.bn.box;

import javax.microedition.khronos.opengles.GL10;
import com.bn.util.Constant;
import com.bn.util.From2DTo3DUtil;
import com.bn.zxl.GameView;

public class ChiLun_Picture
{
	
	GameView gameview;
	float deepth;
	public ChiLun_Picture(float deepth,GameView gameview)
	{
		this.gameview=gameview;
		this.deepth=deepth;
    }
	
	public void drawSelf(float x,float y,GL10 gl)
    {
        Constant.ChiLun.drawSelf(gl, gameview.textureId_di, From2DTo3DUtil.point3D(x, y), -From2DTo3DUtil.k2d_3d(deepth),0);

        //°¼¾À
		Constant.ChiLun_Cebi.drawSelf(gl, x, y,0,gameview.textureId);
		//©³
        Constant.ChiLun.drawSelf(gl, gameview.textureId_di, From2DTo3DUtil.point3D(x, y), -From2DTo3DUtil.k2d_3d(0),0);
    }

}

