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
		
        //�}�ҲV�X
        GLES20.glEnable(GLES20.GL_BLEND);
        //�]�w�V�X�Ѽ�
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		surface.drawSelf(surface_texid,1);
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        
		surface.drawSelf(surface_texid,0);
		
		// �}�ҲV�X
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
		//ø��y��
		MatrixState.pushMatrix();
		MatrixState.translate(0, 0.18f, 0f);
		net.drawSelf(net_texid,0);
		//�������v
		MatrixState.rotate(10, 1, 0, 0);
		net.drawSelf(net_texid,1);
		MatrixState.popMatrix();
		// �����V�X
		GLES20.glDisable(GLES20.GL_BLEND);
		
		//��[
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
