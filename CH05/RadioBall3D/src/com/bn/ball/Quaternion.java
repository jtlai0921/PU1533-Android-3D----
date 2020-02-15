package com.bn.ball;
/*
 * 代表四元數的類別
 */
public class Quaternion {
	//四元數的四個分量值
	float w, x, y, z;
	//單位四元數
	public static Quaternion getIdentityQuaternion()
	{
		return  new Quaternion(1f, 0f, 0f, 0f);
	}

	public Quaternion() {	}
	public Quaternion(float w, float x, float y, float z) 
	{
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	//建構繞指定軸旋轉的四元數的方法
	public void setToRotateAboutAxis(Vector3f axis, float theta){
		//旋轉軸必須規格化
		axis.normalize();
		//計算半角和sin值
		float thetaOver2 = theta/2f;
		float sinThetaOver2 = (float) Math.sin(thetaOver2);
		//給予值
		w = (float) Math.cos(thetaOver2);
		x = axis.x * sinThetaOver2;
		y = axis.y * sinThetaOver2;
		z = axis.z * sinThetaOver2;
	}
	//分析旋轉角的方法
	public float getRotationAngle(){
		//計算半角，w = cos(theta/2)
		float thetaOver2 = (float) Math.acos(w);
		//傳回旋轉角
		return thetaOver2 * 2f;
	}
	//分析旋轉軸的方法
	public Vector3f getRotationAxis(){
		//計算sin^2(theta/2), 記住w = cos(theta/2),  sin^2(x) + cos^2(x) = 1
		float sinThetaOver2Sq = 1.0f - w*w;
		//注意確保數值精度
		if(sinThetaOver2Sq <= 0.0f){
			//單位四元數或不精確的數值，只要傳回有效的向量即可
			return new Vector3f(1.0f, 0f, 0f);
		}
		//計算1/sin(theta/2)
		float oneOversinThetaOver2 = (float) (1.0f/Math.sqrt(sinThetaOver2Sq));
		//傳回旋轉軸
		return new Vector3f(
				x * oneOversinThetaOver2,
				y * oneOversinThetaOver2,
				z * oneOversinThetaOver2
				);
	}
	/*
	 * 四元數叉乘運算，用以匯總多次旋轉，
	 * 乘的順序是從左向右，
	 * 這和四元數叉乘的“標准”定義相反
	 */
	public Quaternion cross(Quaternion a){
		Quaternion result = new Quaternion();
		
		result.w = w*a.w - x*a.x - y*a.y - z*a.z;
		result.x = w*a.x + x*a.w + z*a.y - y*a.z;
		result.y = w*a.y + y*a.w + x*a.z - z*a.x;
		result.z = w*a.z + z*a.w + y*a.x - x*a.y;
		
		return result;
	}
}
