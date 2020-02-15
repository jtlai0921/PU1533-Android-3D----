package com.f.util;

import static com.f.pingpong.Constant.*;

import javax.vecmath.Vector3f;

public class CalculateUtil 
{
	//計算對面落點的位置  和 反彈速度
	public static Vector3f[] getPosAndSpeB(Vector3f position,Vector3f speed)
	{
		Vector3f[] result = new Vector3f[2];
		Vector3f positionB = null;
		Vector3f speedB = null;
		//y方向上 就是豎直上拋  起始速度speed.y  起始高度是  球台半高+半徑
		
		//到最高點的時間
		float t1 = Math.abs(speed.y/A.y);
		//到最高點的距離
		float s1 = Math.abs(speed.y*speed.y/(2*A.y));
		//最高點到桌面的距離
		float s2 = s1+position.y-(TABLE_Y + BALL_R);
		//最高點到桌面的時間
		float t2 = (float) Math.sqrt(Math.abs(2*s2/A.y));
		//球到對面桌子上(B點)的時間
		float t = t1+t2;
		//B點 x z座標
		float px = position.x+speed.x*t;
		float pz = position.z+speed.z*t;
		positionB = new Vector3f(px,TABLE_Y + BALL_R,pz);
		
		//計算反彈後一瞬間的速度  x z上的加速度暫時為 0 
		float vx = speed.x + A.x * t;
		float vy = Math.abs((speed.y + A.y * t)*BALL_ENERGY_LOST);
		float vz = speed.z + A.z * t;
		speedB = new Vector3f(vx,vy,vz);
		
		result[0]=positionB;
		result[1]=speedB;
		return result;
	}
	
	//根據球在對面落點位置和速度    計算撞點位置和碰撞前速度
	public static Vector3f[] getPosAndSpeC(Vector3f positionB,Vector3f speedB)
	{
		Vector3f[] result = new Vector3f[2];
		Vector3f positionC = null;
		Vector3f speedC = null;
		
		//撞點 與 對面落點的z軸上的距離
		float s = Math.abs(AI_Z-positionB.z);//暫時設定球台最遠邊為    碰撞面
		//落點到撞點的時間
		float t = Math.abs(s/speedB.z);
		
		float px = positionB.x+speedB.x*t;
		float py = positionB.y+(speedB.y*t+0.5f*A.y*t*t);//這個地方 大概有0.01的誤差  百分之1到2
		float pz = AI_Z;
		positionC = new Vector3f(px,py,pz);
		
		float vx = speedB.x;
		float vy = speedB.y+A.y*t;
		float vz = speedB.z;
		speedC = new Vector3f(vx,vy,vz);

		result[0] = positionC;
		result[1] = speedC;
		return result;
	}
	
	//在中心點範圍內 隨機產生一個目的打擊點    隨機的區域暫定為矩形 ratio為一個比例  值越大 矩形範圍越大
	//TABLE_X_MIN<center.x<TABLE_X_MAX
	//TABLE_Z_MAX/3<center.z<TABLE_Z_MAX
	public static Vector3f randomTarget(Vector3f center,float ratio)
	{
		Vector3f result = new Vector3f();
		float maxl = TABLE_WIDHT * ratio;
		float maxw = TABLE_LENGTH * ratio / 3.0f;
		float x1 = (float) (maxl * (Math.random()-0.5f));
		float z1 = (float) (maxw * (Math.random()-0.5f));
		result.x = center.x + x1;
		result.y = center.y ;
		result.z = center.z + z1;
		return result;
	}
//	//根據落點計算射出速度 vy < 0
//	public static Vector3f getOutSpeedDown0(Vector3f target,Vector3f position_out,float vy)
//	{
//		//s = (vt*vt - v0*v0) / 2*g
//		float s  = position_out.y - TABLE_Y;
//		float vt = (float) Math.sqrt(2 * s * A.y + vy * vy);
//		float t  = Math.abs((vt - vy) / A.y);
//		
//		float vx = (target.x - position_out.x )/ t;
//		float vz = (target.z - position_out.z )/ t;
//		
//		return new Vector3f(vx,vy,vz);
//	}
	//根據落點計算射出速度 vy > 0
	public static Vector3f getOutSpeedUp(Vector3f target,Vector3f position_out,float vy)
	{
		Vector3f result = new Vector3f();
		//上拋時間
		float t1 = Math.abs(vy / A.y);
		//上拋距離
		//float s1 = Math.abs(vy * vy / (2 * A.y));//v*v = 2 * g *h
		float s1 = vy * t1 + 0.5f * A.y * t1 *t1 ;//s = vt + 0.5f * g * t * t
		//下落時間
		float t2 = (float) Math.sqrt(Math.abs(2 * (s1 + position_out.y) / A.y));
		//總時間
		float t = t1+t2;
		
		float vx = (target.x - position_out.x )/ t;
		float vz = (target.z - position_out.z )/ t;

		result.x = vx;
		result.y = vy;
		result.z = vz;
		
		return result;
	}
	
	//向量vec1與vec2的角平分線
	public static Vector3f getBisector(Vector3f vecIn,Vector3f vecOut)
	{
		Vector3f bisector = new Vector3f();
		Vector3f tempOut = new Vector3f(vecOut.x,vecOut.y,vecOut.z);
		Vector3f tempIn = new Vector3f(vecIn.x,vecIn.y,vecIn.z);
		tempOut.normalize();
		tempIn.normalize();
		tempIn.x = -tempIn.x;
		tempIn.y = -tempIn.y;
		tempIn.z = -tempIn.z;
		bisector.x = tempIn.x + tempOut.x;
		bisector.y = tempIn.y + tempOut.y;
		bisector.z = tempIn.z + tempOut.z;
		bisector.normalize();
		return bisector;
	}

	//與x,y,z軸的夾角,即球拍應該繞x,y,z旋轉的角度
	public static Vector3f getAngle(Vector3f bisector){
		
		//三個座標軸向量
		Vector3f xaxle = new Vector3f(1,0,0); 
		Vector3f yaxle = new Vector3f(0,1,0); 
		Vector3f zaxle = new Vector3f(0,0,1);
		//bisector分別在x-o-y,y-o-z,z-o-x平面上的投影
		Vector3f xoy = new Vector3f(bisector.x,bisector.y,0); 
		Vector3f yoz = new Vector3f(0,bisector.y,bisector.z); 
		Vector3f xoz = new Vector3f(bisector.x,0,bisector.z);
		xoy.normalize();
		yoz.normalize();
		xoz.normalize();
		//投影與座標軸的夾角
		float xAngle = (float) Math.acos(yaxle.dot(yoz));
		float yAngle = (float) Math.acos(zaxle.dot(xoz));
		float zAngle = (float) Math.acos(xaxle.dot(xoy));
		return new Vector3f(xAngle, yAngle, zAngle);
	}

	//計算球是否撞網	//positionC預測的球與智慧排的撞擊點,target上面程式隨機的落點
	public static boolean isCollNet(Vector3f position_out,Vector3f target,float vy)
	{
		//上拋時間
		float t1 = Math.abs(vy / A.y);
		//上拋距離
		//float s1 = Math.abs(vy * vy / (2 * A.y));//v*v = 2 * g *h
		float s1 = vy * t1 + 0.5f * A.y * t1 * t1 ;//s = vt + 0.5f * g * t * t
		//下落時間
		float t2 = (float) Math.sqrt(Math.abs(2 * (s1 + position_out.y) / A.y));
		//總時間
		float t = t1+t2;
		//z方向速度
		float vz = (target.z - position_out.z )/ t;
		//從擊球點到網的時間
		float tz = Math.abs( position_out.z / vz );
		//在tz時間球y方向的位移
		float s2 = vy * tz + 0.5f * A.y * tz * tz;
		//在tz時間後球的y值
		float s3 = position_out.y + s2;
		if(s3 > NET_Y + BALL_R){
			return false;
		}else{
			return true;
		}
	}
}