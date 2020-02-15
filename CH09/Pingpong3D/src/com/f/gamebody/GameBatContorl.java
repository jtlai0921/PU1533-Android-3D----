package com.f.gamebody;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import android.opengl.GLES20;

import com.f.gamebody.base.BatNetRect;
import com.f.mainbody.MainBat;
import com.f.pingpong.Constant;
import com.f.util.MatrixState;

import static com.f.pingpong.Constant.*;
public class GameBatContorl {
	
	/** 手在螢幕上抹動的距離 換算成座標系中數值的一個中間值*/
	private final float TOUCH_TO_DISTANCE = 4f/800f;
	
	public final float RACKETS_SIZE_H = 0.202f;
	
	/** 球拍繞z軸旋轉的角度*/
	public float zAngle;
    // 球拍位置 
    public Vector3f position = new Vector3f();
    public Vector3f position_start = new Vector3f();
    public Vector3f position_start_AI = new Vector3f();
    // 球拍搬移距離的最大值
    public Vector2f moveDistanceMax = new Vector2f(); 
    
    // 球拍搬移距離 
    private Vector2f moveDistance = new Vector2f(); 
    
    //擊球時球拍的速度
    public Vector3f speed = new Vector3f();
    
    //計算預測出的碰撞點的位置 --AI端使用
    public Vector3f targetposition = new Vector3f();;
    public Vector3f rotateAngleTotal = new Vector3f();;
    public Vector3f rotateAngleNow = new Vector3f();;

	private BatNetRect racket = null;
	private MainBat racketAi = null;

	private int isAI;
	//碰撞檢驗範圍，當球拍的z值變大，碰撞檢驗範圍變大
	public Vector2f boundary = new Vector2f();; 
	
	public int points;

	//拍子只能擊打一次球
	public volatile boolean isBat = false;
	
	public GameBatContorl(Vector3f position_start,int isAI){//起始的z座標	
		this.isAI = isAI;
		this.position.set(position_start);
		this.boundary.set(RACKETS_LENGTH / 2.0f,RACKETS_WIDTH / 2.0f);
		this.moveDistanceMax.set(1.5f, 0.3f);		
		if(isAI == 1)
		{
			this.position_start_AI.set(position_start);
			this.racketAi = new MainBat();
		}else
		{
			this.position_start.set(position_start);
			this.racket = new BatNetRect(vCount[1][0],vertexBuffer[1][0],texCoorBuffer[1][0] ,normalBuffer[1][0]);
		}
	}
	public void initRackets() {
		this.isBat = false;
	}
	public void initRacketsAI()
	{
		this.position.set(position_start_AI);
	}
	//自己邊拍子滑動 及旋轉  參數是搬移的距離
	public void touchToMove(float moveX,float moveY)
	{
		moveDistance.x += moveX * TOUCH_TO_DISTANCE;
		moveDistance.y += moveY * TOUCH_TO_DISTANCE;//這個y是指沿z軸搬移的多少

		if( moveDistance.x >  moveDistanceMax.x) moveDistance.x =   moveDistanceMax.x;
		if( moveDistance.x < -moveDistanceMax.x) moveDistance.x = - moveDistanceMax.x;
		if( moveDistance.y >  moveDistanceMax.y) moveDistance.y =   moveDistanceMax.y; 
		if( moveDistance.y < -moveDistanceMax.y) moveDistance.y = - moveDistanceMax.y; 
		
		synchronized (Constant.videoLock) {
			position.x = position_start.x+moveDistance.x;
			position.y = position_start.y ;
			position.z = position_start.z+moveDistance.y;
			zAngle = -moveDistance.x / (RACKETS_SIZE_H * 2) * 90;
			if(Math.abs(zAngle) >= 90.0f ){
				zAngle = zAngle > 0.0f ? 90.0f : -90.0f;
			}
		}
		
		float boundaryScale = (position.z - position_start.z) * 0.05f;//0.05縮放比
		boundary.x = RACKETS_LENGTH / 2.0f + boundaryScale;
		boundary.y = RACKETS_WIDTH / 2.0f + boundaryScale;
		
		CAMERAMOVE.x = moveDistance.x * 0.20f;
		CAMERAMOVE.y = moveDistance.y * 0.20f;
	}
	
	public  void calRacketsSpeed(float mx, float my)
	{
		float RACKETS_SPEED_SCALE = 1.5f / 22.0f;
		float rvx = mx * RACKETS_SPEED_SCALE;
		float rvy = my * RACKETS_SPEED_SCALE;
		// -0.7<speed.x<0.7
		rvx = rvx > 0.7f ? 0.7f : rvx;
		rvx = rvx < -0.7f ? -0.7f : rvx;
		// -2.0<speed.z<0
		float transfer;
		if (rvy > 0) {
			transfer = 1.0f;
		} else {
			transfer = -1.0f;
		}
		rvy = rvy > 0.0f ? -rvy : rvy;
		rvy -= 1.7f;
		rvy = rvy < -2.0f ? -2.0f : rvy;
		// speed.y is not used, ball.speed.y != rackets.speed.y
		speed.set(rvx, transfer, rvy);
	}
	
	public void drawSelf(int textureid_racket,Vector3f positionIn){
		
		MatrixState.pushMatrix();
		if(isAI == 1){
			MatrixState.translate(positionIn.x,positionIn.y,positionIn.z);
			MatrixState.rotate(rotateAngleNow.x * 10, 1, 0, 0);
			MatrixState.rotate(rotateAngleNow.y * 10, 0, 1, 0);
			MatrixState.rotate(rotateAngleNow.z, 0, 0, 1);
		}else{
			MatrixState.translate(positionIn.x,positionIn.y,positionIn.z);
		}
		MatrixState.rotate(zAngle, 0, 0, 1);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		if(isAI == 1)
		{
//			MatrixState.rotate(-180, 0, 1, 0);
			MatrixState.rotate(  90, 1, 0, 0);
			MatrixState.scale(0.025f,0.025f,0.025f);
			racketAi.drawSelf(textureid_racket);
		}else
		{
			racket.drawSelf(textureid_racket,0);
		}
		
		GLES20.glDisable(GLES20.GL_BLEND);
		MatrixState.popMatrix();
	}

}
