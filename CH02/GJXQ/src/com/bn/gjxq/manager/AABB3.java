package com.bn.gjxq.manager;

public class AABB3 
{
	//AABB�]�򲰤��ӭ����̤p��
	float minX;
	float maxX;
	float minY;
	float maxY;
	float minZ;
	float maxZ;
	
	public AABB3(float minX,float maxX,float minY,float maxY,float minZ,float maxZ)
	{
		this.minX=minX;
		this.maxX=maxX;
		this.minY=minY;
		this.maxY=maxY;
		this.minZ=minZ;
		this.maxZ=maxZ;
	}
}
