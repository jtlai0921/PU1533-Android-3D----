package com.f.gamebody;

import com.f.gamebody.base.GameFloorWallRect;
import com.f.util.MatrixState;

import static com.f.pingpong.Constant.*;

public class GameRoom {

	private GameFloorWallRect floor = null;
	private GameFloorWallRect wall = null;
	public GameRoom(){
		floor = new GameFloorWallRect(vCount[1][6],vertexBuffer[1][6],texCoorBuffer[1][3], normalBuffer[1][2]);
		wall = new GameFloorWallRect(vCount[1][7],vertexBuffer[1][7],texCoorBuffer[1][3], normalBuffer[1][2]);
	}
	public void drawSelf(int texFloor,int texShadow,int texWall)
	{
        // 繪制天空盒後面
        MatrixState.pushMatrix();
        MatrixState.translate(0, 0, -7);
        MatrixState.rotate(90, 1, 0, 0);
        wall.drawSelf(texWall,texShadow);
        MatrixState.popMatrix();              
		
        //繪制天空盒下面
	    MatrixState.pushMatrix();
	    MatrixState.translate(0, -0.76f*UNIT_SIZE, 0);
	    floor.drawSelf(texFloor,texShadow);
	    MatrixState.popMatrix(); 
	}
}
