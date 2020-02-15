package com.cw.game;
import android.opengl.Matrix;
//物體的AABB包圍盒
public class AABB3
{
	MyVector3f min;
	MyVector3f max;
	//空參建構器
	AABB3(){
		min = new MyVector3f();
		max = new MyVector3f();
		empty();
	}
	//參數為頂點陣列的建構器  
	AABB3(float[] vertices){
		min = new MyVector3f();
		max = new MyVector3f();
		empty();
		//將所有的點加入包圍盒
		for(int i=0; i<vertices.length; i+=3){
			this.add(vertices[i], vertices[i+1], vertices[i+2]);
		}
	}
	//清理AABB
	public void empty(){
		min.x = min.y = min.z = Float.POSITIVE_INFINITY;//將最小點設為最大值
		max.x = max.y = max.z = Float.NEGATIVE_INFINITY;//將最大點設為最小值
	}
	//將單一點加入到AABB中，並在必要的時候延伸AABB以包括每個點
	public void add(MyVector3f p){
		if (p.x < min.x) { min.x = p.x; }
		if (p.x > max.x) { max.x = p.x; }
		if (p.y < min.y) { min.y = p.y; }
		if (p.y > max.y) { max.y = p.y; }
		if (p.z < min.z) { min.z = p.z; }
		if (p.z > max.z) { max.z = p.z; }
	}
	public void add(float x, float y, float z){
		if (x < min.x) { min.x = x; }
		if (x > max.x) { max.x = x; }
		if (y < min.y) { min.y = y; }
		if (y > max.y) { max.y = y; }
		if (z < min.z) { min.z = z; }
		if (z > max.z) { max.z = z; }
	}
	//取得AABB所有頂點座標的方法
	public MyVector3f[] getAllCorners(){
		MyVector3f[] result = new MyVector3f[8];
		for(int i=0; i<8; i++){
			result[i] = getCorner(i);
		}
		return result;
	}
	//取得AABB第i個頂點座標的方法
	public MyVector3f getCorner(int i){		
		if(i<0||i>7){//檢查i是否合法
			return null;
		}
		return new MyVector3f(
				((i & 1) == 0) ? max.x : min.x,
				((i & 2) == 0) ? max.y : min.y, 
				((i & 4) == 0) ? max.z : min.z
				);
	}
	//透過目前仿射變換矩陣求得仿射變換後的AABB包圍盒的方法
	public AABB3 setToTransformedBox(float[] m)
	{
		//取得所有頂點的座標
		MyVector3f[] va = this.getAllCorners();
		//用於存放仿射變換後的頂點陣列
	    float[] transformedCorners=new float[24];
	    //將變換前的AABB包圍盒的8個頂點與仿射變換矩陣m相乘，得到仿射變換後的OBB包圍盒的所有頂點
		float[] tmpResult=new float[4];
	    int count=0;
		for(int i=0;i<va.length;i++){
			float[] point=new float[]{va[i].x,va[i].y,va[i].z,1};//將頂點轉換成齊次座標
			Matrix.multiplyMV(tmpResult, 0, m, 0, point, 0);
			transformedCorners[count++]=tmpResult[0];
			transformedCorners[count++]=tmpResult[1];
			transformedCorners[count++]=tmpResult[2];
		}
		//透過建構器將OBB包圍盒轉換成AABB包圍盒，並傳回
		return new AABB3(transformedCorners);
	}
	public float getXSize(){//取得x方向大小
		return max.x - min.x;
	}
	public float getYSize(){//取得y方向大小
		return max.y - min.y;
	}
	public float getZSize(){//取得z方向大小
		return max.z - min.z;
	}
	public MyVector3f getSize(){//取得對角線向量
		return max.minus(min);
	}
	//取得包圍盒的中心點座標的方法
	public MyVector3f getCenter(){
		return (min.add(max)).multiK(0.5f);
	}
	public float rayIntersect(
			MyVector3f rayStart,//射線起點
			MyVector3f rayDir,//射線長度和方向
			MyVector3f returnNormal//可選的，相交點處法向量
			){
		//若果未相交則傳回這個大數
		final float kNoIntersection = Float.POSITIVE_INFINITY;
		//檢查點在矩形邊界內的情況，並計算到每個面的距離
		boolean inside = true;
		float xt, xn = 0.0f;
		if(rayStart.x<min.x){
			xt = min.x - rayStart.x;
			if(xt>rayDir.x){ return kNoIntersection; }
			xt /= rayDir.x;
			inside = false;
			xn = -1.0f;
		}
		else if(rayStart.x>max.x){
			xt = max.x - rayStart.x;
			if(xt<rayDir.x){ return kNoIntersection; }
			xt /= rayDir.x;
			inside = false;
			xn = 1.0f;
		}
		else{
			xt = -1.0f;
		}
		
		float yt, yn = 0.0f;
		if(rayStart.y<min.y){
			yt = min.y - rayStart.y;
			if(yt>rayDir.y){ return kNoIntersection; }
			yt /= rayDir.y;
			inside = false;
			yn = -1.0f;
		}
		else if(rayStart.y>max.y){
			yt = max.y - rayStart.y;
			if(yt<rayDir.y){ return kNoIntersection; }
			yt /= rayDir.y;
			inside = false;
			yn = 1.0f;
		}
		else{
			yt = -1.0f;
		}
		
		float zt, zn = 0.0f;
		if(rayStart.z<min.z){
			zt = min.z - rayStart.z;
			if(zt>rayDir.z){ return kNoIntersection; }
			zt /= rayDir.z;
			inside = false;
			zn = -1.0f;
		}
		else if(rayStart.z>max.z){
			zt = max.z - rayStart.z;
			if(zt<rayDir.z){ return kNoIntersection; }
			zt /= rayDir.z;
			inside = false;
			zn = 1.0f;
		}
		else{
			zt = -1.0f;
		}
		//是否在矩形邊界框內？
		if(inside){
			if(returnNormal != null){
				returnNormal = rayDir.multiK(-1);
				returnNormal.normalize();
			}
			return 0.0f;
		}
		//選取最遠的平面————發生相交的地方
		int which = 0;
		float t = xt;
		if(yt>t){
			which = 1;
			t=yt;
		}
		if(zt>t){
			which = 2;
			t=zt;
		}
		switch(which){
			case 0://和yz平面相交
			{
				float y=rayStart.y+rayDir.y*t;
				if(y<min.y||y>max.y){return kNoIntersection;}
				float z=rayStart.z+rayDir.z*t;
				if(z<min.z||z>max.z){return kNoIntersection;}
				if(returnNormal != null){
					returnNormal.x = xn;
					returnNormal.y = 0.0f;
					returnNormal.z = 0.0f;
				}				
			}
			break;
			case 1://和xz平面相交
			{
				float x=rayStart.x+rayDir.x*t;
				if(x<min.x||x>max.x){return kNoIntersection;}
				float z=rayStart.z+rayDir.z*t;
				if(z<min.z||z>max.z){return kNoIntersection;}
				if(returnNormal != null){
					returnNormal.x = 0.0f;
					returnNormal.y = yn;
					returnNormal.z = 0.0f;
				}				
			}
			break;
			case 2://和xy平面相交
			{
				float x=rayStart.x+rayDir.x*t;
				if(x<min.x||x>max.x){return kNoIntersection;}
				float y=rayStart.y+rayDir.y*t;
				if(y<min.y||y>max.y){return kNoIntersection;}
				if(returnNormal != null){
					returnNormal.x = 0.0f;
					returnNormal.y = 0.0f;
					returnNormal.z = zn;
				}				
			}
			break;
		}
		return t;//傳回相交點參數值
	}
}
