package com.cw.util;
public class IntersectantUtil {	
	/*
	 * 1、透過在螢幕上的觸控位置，計算對應的近平面上座標，
	 * 以便求出AB兩點在攝影機座標系中的座標
	 * 2、將AB兩點在攝影機中座標系中的座標乘以攝影機矩陣的逆矩陣，
	 * 以便求得AB兩點在世界座標系中的座標
	 */
	public static float[] calculateABPosition
	(
		float x,//觸摸螢幕X座標
		float y,//觸摸螢幕Y座標
		float w,// 螢幕寬度
		float h,// 螢幕高度
		float left,//角度left值
		float top,//角度top值
		float near,//角度near值
		float far//角度far值
	)
	{
		//求視口的座標中心在原點時，觸控點的座標
		float x0=x-w/2;
		float y0=h/2-y;		
		//計算對應的near面上的x、y座標
		float xNear=2*x0*left/w;
		float yNear=2*y0*top/h;
		//計算對應的far面上的x、y座標
		float ratio=far/near;
		float xFar=ratio*xNear;
		float yFar=ratio*yNear;
		//攝影機座標系中A的座標
        float ax=xNear;
        float ay=yNear;
        float az=-near;
        //攝影機座標系中B的座標
        float bx=xFar;
        float by=yFar;
        float bz=-far; 
        //透過攝影機座標系中A、B兩點的座標，求世界座標系中A、B兩點的座標
		float[] A = MatrixState.fromPtoPreP(new float[] { ax, ay, az });
		float[] B = MatrixState.fromPtoPreP(new float[] { bx, by, bz });
		return new float[] {//傳回最終的AB兩點座標
			A[0],A[1],A[2],
			B[0],B[1],B[2]
		};
	}
}