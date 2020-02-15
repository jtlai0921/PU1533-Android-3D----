package tstc.fxq.utils;
//3D座標系中法向量的工具類別
public class Vector3Util {
	//計算三角形規格化法向量的方法
	public static float[] calTriangleAverageNormal
	(//三角形三個頂點座標，（要求以逆時針卷冊繞）
			float x0,float y0,float z0,//A
			float x1,float y1,float z1,//B
			float x2,float y2,float z2 //C
	)
	{
		float[] a={x1-x0, y1-y0, z1-z0};//向量AB
		float[] b={x2-x1, y2-y1, z2-z1};//向量BC
		/*
		 * 計算三角形的法向量，
		 * 向量AB、BC做叉乘，
		 * 若ABC三點按逆時針卷冊繞，
		 * 向量AB和BC首尾相連，
		 * 則AB叉乘BC所得法向量的方向符合右手定則
		 */
		float[] c=crossTwoVectors(a,b);
		return normalizeVector(c);//傳回規格化後的法向量
	}
	//計算稜錐側面法向量的方法
	public static float[] calConeNormal
	(//三角形三個頂點座標
			float x0,float y0,float z0,//A，中心點
			float x1,float y1,float z1,//B，底面圓上一點
			float x2,float y2,float z2 //C，頂點
	)
	{
		float[] a={x1-x0, y1-y0, z1-z0};//向量AB
		float[] b={x2-x1, y2-y1, z2-z1};//向量BC
		//先球垂直於平面ABC的向量
		float[] c=crossTwoVectors(a,b);
		//將b和c做叉乘，得出所球向量d
		float[] d=crossTwoVectors(b,c);
		return normalizeVector(d);//傳回規格化後的法向量
	}
	
	//計算球桿側面法向量的方法
	public static float[] calBallArmNormal
	(//三角形三個頂點座標
			float r,float R,float hight,float scale,
			float x1,float y1,float z1,
			float x2,float y2,float z2,
			float angle		//目前的角度值
	)
	{
		
		float[] a={0,1,0};//球桿中心柱的向量
		float[] b={x2-x1,y2-y1,z2-z1};//平行於球桿側面的向量
		
		//先球垂直於平面ABC的向量
		float[] c=crossTwoVectors(b,a);
		//將b和c做叉乘，得出所球向量d
		float[] d=crossTwoVectors(b,c);
		return normalizeVector(d);//傳回規格化後的法向量
	}
	
	
	
	//將一個向量規格化的方法
	public static float[] normalizeVector(float x, float y, float z){
		float mod=module(x,y,z);
		return new float[]{x/mod, y/mod, z/mod};//傳回規格化後的向量
	}
	public static float[] normalizeVector(float [] vec){
		float mod=module(vec);
		return new float[]{vec[0]/mod, vec[1]/mod, vec[2]/mod};//傳回規格化後的向量
	}
	//將一群組向量規格化的方法，陣列中元素個數應是3的倍數
	public static void normalizeAllVectors(float[] allVectors){
		for(int i=0;i<allVectors.length;i+=3){
			float[] result=Vector3Util.normalizeVector(allVectors[i],allVectors[i+1],allVectors[i+2]);
			allVectors[i]=result[0];
			allVectors[i+1]=result[1];
			allVectors[i+2]=result[2];
		}
	}
	//求向量的模的方法
	public static float module(float x, float y, float z){
		return (float) Math.sqrt(x*x+y*y+z*z);
	}
	public static float module(float [] vec){
		return (float) Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]+vec[2]*vec[2]);
	}
	//兩個向量叉乘的方法
	public static float[] crossTwoVectors(float[] a, float[] b)
	{
		float x=a[1]*b[2]-a[2]*b[1];
		float y=a[2]*b[0]-a[0]*b[2];
		float z=a[0]*b[1]-a[1]*b[0];
		return new float[]{x, y, z};//傳回叉乘結果
	}
	//兩個向量點乘的方法
	public static float dotTwoVectors(float[] a, float[] b)
	{
		return a[0]*b[0]+a[1]*b[1]+a[2]*b[2];//傳回點乘結果
	}
	//兩個向量相加的方法
	public static float[] addVector(float[] a, float b[]){
		return new float[]{a[0]+b[0], a[1]+b[1], a[2]+b[2]};
	}
	//求兩個向量夾角的方法
	public static double calVector3Angrad(float[] a, float[] b){
		float[] aNormal=Vector3Util.normalizeVector(a);
		float[] bNormal=Vector3Util.normalizeVector(b);
		return Math.acos(Vector3Util.dotTwoVectors(aNormal, bNormal));
	}
}
