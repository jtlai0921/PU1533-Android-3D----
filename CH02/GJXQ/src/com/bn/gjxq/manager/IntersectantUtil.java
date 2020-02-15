package com.bn.gjxq.manager;
import static com.bn.gjxq.Constant.*;
public class IntersectantUtil 
{	
	/*
	 * 1、透過在螢幕上的觸控位置，計算對應的近平面上座標，
	 * 以便求出AB兩點在攝影機座標系中的座標
	 * 2、將AB兩點在攝影機中座標系中的座標乘以攝影機矩陣的逆矩陣，
	 * 以便求得AB兩點在世界座標系中的座標
	 */
	public static Vector3f[] calculateABPosition
	(
		float x, 			//觸摸螢幕X座標
		float y, 			//觸摸螢幕Y座標
		float[] cData		//攝影機9參數
	)
	{
		//求視口的座標中心在原點時，觸控點的座標
		float x0=x-screenWidth/2;
		float y0=screenHeight/2-y;		
		//計算對應的near面上的x、y座標
		float xNear=2*x0*ratio/screenWidth;
		float yNear=2*y0*top/screenHeight;
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
        
        MatrixUtil.setCamera//攝影機9參數
        (
        		cData[0],cData[1],cData[2],
        		cData[3],cData[4],cData[5],
        		cData[6],cData[7],cData[8]
        );
        
        //透過攝影機座標系中A、B兩點的座標，求世界座標系中A、B兩點的座標
		Vector3f start = MatrixUtil.fromGToO
		(
			new Vector3f( ax, ay, az ),
			MatrixUtil.mVMatrix
		);
		Vector3f end = MatrixUtil.fromGToO
		(
			new Vector3f( bx, by, bz ),
			MatrixUtil.mVMatrix
		);
		return new Vector3f[] {//傳回最終的AB兩點座標
			start,
			end
		};
	}

	//求拾取射線段與指定物體包圍盒六個面交點的最近點距離
	public static float calObjectDisMin
	(
			Vector3f cameraPosition,	//攝影機在世界座標系中位置
			Vector3f start,				//A點在世界座標系中的位置（其是AB線段的起始點）
			Vector3f end,				//B點在世界座標中的位置（其是AB線段的終點）
			float[] mo,					//物體基本變換矩陣
			AABB3 aabb					//包圍盒
	)
	{
		float disMin=Float.MAX_VALUE;
		
		//將攝影機在世界座標系中的點變換到物體座標系
		Vector3f cameraObject=MatrixUtil.fromGToO(cameraPosition, mo);
		
		//將拾取線段A點變換到物體座標系
		Vector3f startObject=MatrixUtil.fromGToO(start, mo);
		
		//將拾取線段B點變到物體座標系
		Vector3f endObject=MatrixUtil.fromGToO(end, mo);
		
		//計算出射線參數方程式中的d，即end-start
	   	Vector3f dv=endObject.minus(startObject);
	   	
	   	//求與包圍盒xMin面的交點
	   	float t=(aabb.minX-startObject.x)/dv.x;//求出斜率t
		if(t>=0&&t<=1)//若果斜率不在這個範圍是沒有意義的，因為AB是線段
		{
			float y=t*dv.y+startObject.y;//求出xy座標
			float z=t*dv.z+startObject.z;
			
			//若果xy座標在百位和xy最大值與最小值之間
			if(y<=aabb.maxY&&y>=aabb.minY&&z<=aabb.maxZ&&z>=aabb.minZ)
			{
				float x=aabb.minX;//x座標
				float disTemp=Vector3f.disP2(cameraObject, new Vector3f(x,y,z));//求出攝影機與AB和包圍盒交點的最小距離
				if(disTemp<disMin)//若果disTemp小於disMin，則更新disMin
				{
					disMin=disTemp;
				}
			}
		}
		
		//求與包圍盒xMax面的交點
	   	t=(aabb.maxX-startObject.x)/dv.x;//斜率t
		if(t>=0&&t<=1)
		{
			float y=t*dv.y+startObject.y;//yz座標
			float z=t*dv.z+startObject.z;
			
			//若果yz座標在包圍盒最大值與最小值範圍內
			if(y<=aabb.maxY&&y>=aabb.minY&&z<=aabb.maxZ&&z>=aabb.minZ)
			{
				float x=aabb.maxX;//x座標
				float disTemp=Vector3f.disP2(cameraObject, new Vector3f(x,y,z));//求出攝影機與AB和包圍盒交點的最小距離
				if(disTemp<disMin)//若果disTemp小於disMin，則更新disMin
				{
					disMin=disTemp;
				}
			}
		}
		
		//求與包圍盒yMin面的交點
	   	t=(aabb.minY-startObject.y)/dv.y;//斜率t
		if(t>=0&&t<=1)
		{
			float x=t*dv.x+startObject.x;//xz座標
			float z=t*dv.z+startObject.z;
			
			//若果xz座標在包圍盒的最大值與最小值之間
			if(x<=aabb.maxX&&x>=aabb.minX&&z<=aabb.maxZ&&z>=aabb.minZ)
			{
				float y=aabb.minY;//y座標
				float disTemp=Vector3f.disP2(cameraObject, new Vector3f(x,y,z));//求出攝影機與AB和包圍盒交點的最小距離
				if(disTemp<disMin)//若果disTemp小於disMin，則更新disMin
				{
					disMin=disTemp;
				}
			}
		}
		
		//求與包圍盒yMax面的交點
	   	t=(aabb.maxY-startObject.y)/dv.y;//斜率t
		if(t>=0&&t<=1)
		{
			float x=t*dv.x+startObject.x;//xz座標
			float z=t*dv.z+startObject.z;
			
			//若果xz在包圍盒的最大值與最小值之間
			if(x<=aabb.maxX&&x>=aabb.minX&&z<=aabb.maxZ&&z>=aabb.minZ)
			{
				float y=aabb.maxY;//y座標
				float disTemp=Vector3f.disP2(cameraObject, new Vector3f(x,y,z));//求出攝影機與AB和包圍盒交點的最小距離
				if(disTemp<disMin)//若果disTemp小於disMin，則更新disMin
				{
					disMin=disTemp;
				}
			}
		}
		
		//求與包圍盒zMin面的交點
	   	t=(aabb.minZ-startObject.z)/dv.z;//斜率t
		if(t>=0&&t<=1)
		{
			float x=t*dv.x+startObject.x;//xy座標
			float y=t*dv.y+startObject.y;
			
			//若果xy在包圍盒最大值與最小值之間
			if(x<=aabb.maxX&&x>=aabb.minX&&y<=aabb.maxY&&y>=aabb.minY)
			{
				float z=aabb.minZ;//z座標
				float disTemp=Vector3f.disP2(cameraObject, new Vector3f(x,y,z));//求出攝影機與AB和包圍盒交點的最小距離
				if(disTemp<disMin)//若果disTemp小於disMin，則更新disMin
				{
					disMin=disTemp;
				}
			}
		}
		
		//求與包圍盒zMax面的交點
	   	t=(aabb.maxZ-startObject.z)/dv.z;//斜率t
		if(t>=0&&t<=1)
		{
			float x=t*dv.x+startObject.x;//xy座標
			float y=t*dv.y+startObject.y;
			
			//若果xy在包圍盒最大值與最小值之間
			if(x<=aabb.maxX&&x>=aabb.minX&&y<=aabb.maxY&&y>=aabb.minY)
			{
				float z=aabb.maxZ;//z座標
				float disTemp=Vector3f.disP2(cameraObject, new Vector3f(x,y,z));//求出攝影機與AB和包圍盒交點的最小距離
				if(disTemp<disMin)//若果disTemp小於disMin，則更新disMin
				{
					disMin=disTemp;
				}
			}
		}
		
		return disMin;
	}
}