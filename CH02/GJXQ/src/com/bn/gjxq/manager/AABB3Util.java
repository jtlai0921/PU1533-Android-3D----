package com.bn.gjxq.manager;

public class AABB3Util 
{
	
	public static AABB3 genAABB3FromVertexPositionData(float[] vertices)
	{
		float minX=Float.MAX_VALUE,minY=Float.MAX_VALUE,minZ=Float.MAX_VALUE;
		float maxX=Float.MIN_VALUE,maxY=Float.MIN_VALUE,maxZ=Float.MIN_VALUE;
		
		for(int i=0;i<vertices.length/3;i++){
			if(vertices[i*3]<minX){
				minX=vertices[i*3];		//�]�򲰳̤p��X��
			}
			if(vertices[i*3]>maxX){
				maxX=vertices[i*3];		//�]�򲰳̤j��X��
			}
			if(vertices[i*3+1]<minY){
				minY=vertices[i*3+1];	//�]�򲰳̤p��Y��
			}
			if(vertices[i*3+1]>maxY){
				maxY=vertices[i*3+1];	//�]�򲰳̤j��Y��
			}
			if(vertices[i*3+2]<minZ){
				minZ=vertices[i*3+2];	//�]�򲰳̤p��Z��
			}
			if(vertices[i*3+2]>maxZ){	//�]�򲰳̤j��Z��
				maxZ=vertices[i*3+2];
			}
		}
		return new AABB3(minX,maxX,minY,maxY,minZ,maxZ);//�N�̦X�A���]�򲰶Ǧ^
	}
}
