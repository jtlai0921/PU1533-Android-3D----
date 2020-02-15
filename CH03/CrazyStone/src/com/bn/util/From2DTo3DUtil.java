package com.bn.util;

import static com.bn.util.Constant.*;
public class From2DTo3DUtil 
{
	public static float[] Vertices(float width,float height)
	{
		float halfwidth=width/2;
		float halfheight=height/2;
		float vertices[]=new float[18];
		
		vertices[0]=(-halfwidth)/(Width/2)*RATIO ;
		vertices[1]=(halfheight)/(Height/2);
		vertices[2]=0;
		
		vertices[3]=(-halfwidth)/(Width/2)*RATIO ;
		vertices[4]=(-halfheight)/(Height/2);
		vertices[5]=0;
		
		vertices[6]=(halfwidth)/(Width/2)*RATIO ;
		vertices[7]=halfheight/(Height/2);
		vertices[8]=0;
		
		vertices[9]=(halfwidth)/(Width/2)*RATIO ;
		vertices[10]=halfheight/(Height/2);
		vertices[11]=0;
		
		vertices[12]=(-halfwidth)/(Width/2)*RATIO ;
		vertices[13]=(-halfheight)/(Height/2);
		vertices[14]=0;
		
		vertices[15]=(halfwidth)/(Width/2)*RATIO ;
		vertices[16]=(-halfheight)/(Height/2);
		vertices[17]=0;
		
		return vertices;
	}
	
	public static float[] Vertices(float width,float height,float deep)
	{
		float halfwidth=width/2;
		float halfheight=height/2;
		float vertices[]=new float[18];
		
		vertices[0]=(-halfwidth)/(Width/2)*RATIO ;
		vertices[1]=(halfheight)/(Height/2);
		vertices[2]=deep;
		
		vertices[3]=(-halfwidth)/(Width/2)*RATIO ;
		vertices[4]=(-halfheight)/(Height/2);
		vertices[5]=deep;
		
		vertices[6]=(halfwidth)/(Width/2)*RATIO ;
		vertices[7]=halfheight/(Height/2);
		vertices[8]=deep;
		
		vertices[9]=(halfwidth)/(Width/2)*RATIO ;
		vertices[10]=halfheight/(Height/2);
		vertices[11]=deep;
		
		vertices[12]=(-halfwidth)/(Width/2)*RATIO ;
		vertices[13]=(-halfheight)/(Height/2);
		vertices[14]=deep;
		
		vertices[15]=(halfwidth)/(Width/2)*RATIO ;
		vertices[16]=(-halfheight)/(Height/2);
		vertices[17]=deep;
		
		return vertices;
	}
	
	public static float[] Vertices_heng(float width,float height)
	{
		float halfwidth=width/2;
		float halfheight=width/2;
		float halflength=height/2;
		float vertices[]=new float[18];
		
		vertices[0]=(-halfwidth)/(Width/2)*RATIO ;
		vertices[1]=(halflength)/(Height/2);
		vertices[2]=(halfheight)/(Height/2);
		
		vertices[3]=(-halfwidth)/(Width/2)*RATIO ;
		vertices[4]=(halflength)/(Height/2);
		vertices[5]=(-halfheight)/(Height/2);
		
		vertices[6]=(halfwidth)/(Width/2)*RATIO ;
		vertices[7]=(halflength)/(Height/2);
		vertices[8]=halfheight/(Height/2);
		
		vertices[9]=(halfwidth)/(Width/2)*RATIO ;
		vertices[10]=(halflength)/(Height/2);
		vertices[11]=halfheight/(Height/2);
		
		vertices[12]=(-halfwidth)/(Width/2)*RATIO ;
		vertices[13]=(halflength)/(Height/2);
		vertices[14]=(-halfheight)/(Height/2);
		
		vertices[15]=(halfwidth)/(Width/2)*RATIO ;
		vertices[16]=(halflength)/(Height/2);
		vertices[17]=(-halfheight)/(Height/2);
		
		return vertices;
	}
	
	public static float[] point3D(float position_x,float position_y)
	{
		float[] p=new float[2];
		p[0]=(position_x/(Width/2)-1)*RATIO ;
		p[1]=1-position_y/(Height/2);
		return p;
	}
	
	public static float k2d_3d(float length)
	{
		float k=2/Height;//2d長度轉化為你3D長度的比例
		float f=k*length;
		return f;
		
	}
}
