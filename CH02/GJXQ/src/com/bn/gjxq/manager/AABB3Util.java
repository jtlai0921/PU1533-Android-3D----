package com.bn.gjxq.manager;

public class AABB3Util 
{
	
	public static AABB3 genAABB3FromVertexPositionData(float[] vertices)
	{
		float minX=Float.MAX_VALUE,minY=Float.MAX_VALUE,minZ=Float.MAX_VALUE;
		float maxX=Float.MIN_VALUE,maxY=Float.MIN_VALUE,maxZ=Float.MIN_VALUE;
		
		for(int i=0;i<vertices.length/3;i++){
			if(vertices[i*3]<minX){
				minX=vertices[i*3];		//包圍盒最小的X值
			}
			if(vertices[i*3]>maxX){
				maxX=vertices[i*3];		//包圍盒最大的X值
			}
			if(vertices[i*3+1]<minY){
				minY=vertices[i*3+1];	//包圍盒最小的Y值
			}
			if(vertices[i*3+1]>maxY){
				maxY=vertices[i*3+1];	//包圍盒最大的Y值
			}
			if(vertices[i*3+2]<minZ){
				minZ=vertices[i*3+2];	//包圍盒最小的Z值
			}
			if(vertices[i*3+2]>maxZ){	//包圍盒最大的Z值
				maxZ=vertices[i*3+2];
			}
		}
		return new AABB3(minX,maxX,minY,maxY,minZ,maxZ);//將最合適的包圍盒傳回
	}
}
