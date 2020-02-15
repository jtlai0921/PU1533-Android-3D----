package com.f.gamebody;

import android.opengl.GLES20;

import com.f.gamebody.base.BatNetRect;
import com.f.gamebody.base.Spring;
import com.f.gamebody.base.GameTableCube;
import com.f.util.MatrixState;

import static com.f.pingpong.Constant.*;
public class GameTable 
{
	private GameTableCube surface = null;
	private Spring spring = null;
	private BatNetRect net = null;
	
	public GameTable()
	{
		surface = new GameTableCube();
		net = new BatNetRect(vCount[1][1],vertexBuffer[1][1],texCoorBuffer[1][0] ,normalBuffer[1][0]);
		spring = new Spring();
	}
	public void drawSelf(int surface_texid,int net_texid ,int spring_texid)
	{
		MatrixState.pushMatrix();
		
        //開啟混合
        GLES20.glEnable(GLES20.GL_BLEND);
        //設定混合參數
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		surface.drawSelf(surface_texid,1);
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        
		surface.drawSelf(surface_texid,0);
		
		// 開啟混合
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
		//繪制球網
		MatrixState.pushMatrix();
		MatrixState.translate(0, 0.18f, 0f);
		net.drawSelf(net_texid,0);
		//網的陰影
		MatrixState.rotate(10, 1, 0, 0);
		net.drawSelf(net_texid,1);
		MatrixState.popMatrix();
		// 關閉混合
		GLES20.glDisable(GLES20.GL_BLEND);
		
		//支架
		MatrixState.pushMatrix();
		MatrixState.translate(-0.5f * UNIT_SIZE, -2.4f, 0f);//x,y,z
		MatrixState.rotate(90, 0, 0, 1);
		spring.drawSelf(spring_texid);
		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate( 0.5f * UNIT_SIZE, -2.4f, 0f);
		MatrixState.rotate(90, 0, 0, 1);
		spring.drawSelf(spring_texid);
		MatrixState.popMatrix();
		
		MatrixState.popMatrix();
	}
}
