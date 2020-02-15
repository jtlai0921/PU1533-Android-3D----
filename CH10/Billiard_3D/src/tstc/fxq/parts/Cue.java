package tstc.fxq.parts;

import tstc.fxq.constants.Constant;
import tstc.fxq.constants.Sight;
import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import static tstc.fxq.constants.Constant.*;
/*
 * 球桿，由兩個圓與一個一頭大一頭小的圓柱群組成
 */

public class Cue {
	
	MySurfaceView mv;//MySurfaceView類別的參考
	float size;//球桿大小的比例
	//球桿抬起的角度
	private float angdegElevation = 4;
	
	//使球桿繞世界座標系的y軸旋轉時，對應的球桿的物體座標系的軸
	private float axisX = (float) Math.cos(Math.toRadians(angdegElevation));
	private float axisY = (float) Math.sin(Math.toRadians(angdegElevation));
	
	//球桿的旋轉角度
	float angle;
	
	//獲得球桿旋轉角度的方法
	public float getAngle() {
		//若為第一人稱角度，傳回攝影機的旋轉角度
		if(mv.currSight == Sight.first){
			return mv.angdegAzimuth;
		}
		//若為自由角度，傳回球桿的旋轉角度
		else{
			return angle;
		}
	}
	//設定球桿旋轉角度的方法
	public void setAngle(float angle) {
		//若為第一人稱角度，將球桿旋轉角度，轉換成攝影機的方位角
		if(mv.currSight == Sight.first){			
			mv.angdegAzimuth = angle;
		}
		//若為自由角度，設定球桿的旋轉角
		else{
			this.angle = angle;
		}
	}
	
	CueSide cueSide;//中間的圓柱
	Circle circleBig;//球桿上的圓球
	Circle circleSmall;//球桿上的圓球
	//攝影機方位角的鎖定值
	private float angdegAzimuthLock;
	//鎖定目前框攝影機方位角的方法
	public void lockAngdegAzimuth(float angdegAzimuth){
		angdegAzimuthLock = angdegAzimuth;
	}
	public Cue(MySurfaceView mv,float size)
	{
		this.mv=mv;
		this.size=size;
		cueSide=new CueSide(mv, 0.04f*size,0.08f*size,5f*size,3*size,0);//建立球桿物件
		circleBig=new Circle(mv,0.24f*size*size/Constant.TABLE_UNIT_SIZE, 0.24f*size*size/Constant.TABLE_UNIT_SIZE);
		circleSmall = new Circle(mv,0.12f*size*size/Constant.TABLE_UNIT_SIZE, 0.12f*size*size/Constant.TABLE_UNIT_SIZE);
	}
	public void drawSelf(BallKongZhi mainBall, int texId1,int texId2, int texId3)
	{
		MatrixState.pushMatrix(); 
		/*
		 * 將球桿搬移到白球處
		 * 
		 * 在設計的時候，球的起始位置和桌面差90度
		 * 故繪制球時整體沿y軸旋轉了-90度
		 * 座標也要對應轉換成旋轉後的
		 */
		MatrixState.translate(-mainBall.zOffset, BALL_Y, mainBall.xOffset);
		MatrixState.rotate(90-angdegElevation, 0, 0, 1);
		//若為第一人稱角度，按攝影機的角度旋轉
		if(mv.currSight == Sight.first){
			MatrixState.rotate(angdegAzimuthLock, axisX, axisY, 0);
		}
		//若為自由角度，按球桿的角度旋轉
		else{
			MatrixState.rotate(angle, axisX, axisY, 0);
		}
		MatrixState.translate(0, CUE_Y_ADJUST, 0);
		
        MatrixState.pushMatrix();     
        MatrixState.rotate(180, 0, 1, 0);
		cueSide.drawSelf(texId1);//繪制球桿
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();
		MatrixState.translate(0, 15*size, 0);
		circleBig.drawSelf(texId2);//繪制圓片
		MatrixState.popMatrix();
		
		MatrixState.pushMatrix();
		MatrixState.translate(0, 2*size, 0);
		MatrixState.rotate(180, 1, 0, 0);
		circleSmall.drawSelf(texId3);//繪制圓片
		MatrixState.popMatrix();
		
		MatrixState.popMatrix(); 
	}

}
