package com.bn.box;

import javax.microedition.khronos.opengles.GL10;
import com.bn.util.Constant;
import com.bn.util.From2DTo3DUtil;
import com.bn.zxl.GameView;

public class MuTong_Picture
{
	GameView gameview;
	public MuTong_Picture(GameView gameview) {
		this.gameview=gameview;
	}
	public void drawSelf(GL10 gl,float x,float y)
	{
		Constant.Mutong_cebi.drawSelf(gl, 0,x, y,gameview.textureId_mutong);
		Constant.MuTong_di_1.drawSelf(gl,0, gameview.textureId_di, From2DTo3DUtil.point3D(x,y));

		Constant.MuTong_di.drawSelf(gl,0, gameview.textureId_di, From2DTo3DUtil.point3D(x,y));
	}
	
}
