package com.bn.ball.wt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class CircleSide {
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mTexCoorBuffer;//頂點紋理座標資料緩沖
	FloatBuffer   mNormalBuffer;//頂點法向量資料緩沖
	int circleSideId;
    int vCount=0; 
    
    public CircleSide(float scale,float r,float h,int n, int circleSideId)
    {    	
    	this.circleSideId=circleSideId;
    	//呼叫起始化頂點資料的initVertexData方法
    	initVertexData(scale,r,h,n);
    }
    
    public void initVertexData(
    		float scale,	//大小
    		float r,		//半徑
    		float h,		//高度
    		int n			//切分的份數
    	)
    {
    	r=scale*r;
    	h=scale*h;
    	
		float angdegSpan=360.0f/n;
		vCount=3*n*4;//頂點個數，共有3*n*4個三角形，每個三角形都有三個頂點
		//座標資料起始化
		float[] vertices=new float[vCount*3];
		float[] textures=new float[vCount*2];//頂點紋理S、T座標值陣列
		//座標資料起始化
		int count=0;
		int stCount=0;
		for(float angdeg=0;Math.ceil(angdeg)<360;angdeg+=angdegSpan)//側面
		{
			double angrad=Math.toRadians(angdeg);//目前弧度
			double angradNext=Math.toRadians(angdeg+angdegSpan);//下一弧度
			//底圓目前點---0
			vertices[count++]=(float) (-r*Math.sin(angrad));
			vertices[count++]=0;
			vertices[count++]=(float) (-r*Math.cos(angrad));
			
			textures[stCount++]=(float) (angrad/(2*Math.PI));//st座標
			textures[stCount++]=1;
			//頂圓下一點---3
			vertices[count++]=(float) (-r*Math.sin(angradNext));
			vertices[count++]=h;
			vertices[count++]=(float) (-r*Math.cos(angradNext));
			
			textures[stCount++]=(float) (angradNext/(2*Math.PI));//st座標
			textures[stCount++]=0;
			//頂圓目前點---2
			vertices[count++]=(float) (-r*Math.sin(angrad));
			vertices[count++]=h;
			vertices[count++]=(float) (-r*Math.cos(angrad));
			
			textures[stCount++]=(float) (angrad/(2*Math.PI));//st座標
			textures[stCount++]=0;
			
			//底圓目前點---0
			vertices[count++]=(float) (-r*Math.sin(angrad));
			vertices[count++]=0;
			vertices[count++]=(float) (-r*Math.cos(angrad));
			
			textures[stCount++]=(float) (angrad/(2*Math.PI));//st座標
			textures[stCount++]=1;
			//底圓下一點---1
			vertices[count++]=(float) (-r*Math.sin(angradNext));
			vertices[count++]=0;
			vertices[count++]=(float) (-r*Math.cos(angradNext));
			
			textures[stCount++]=(float) (angradNext/(2*Math.PI));//st座標
			textures[stCount++]=1;
			//頂圓下一點---3
			vertices[count++]=(float) (-r*Math.sin(angradNext));
			vertices[count++]=h;
			vertices[count++]=(float) (-r*Math.cos(angradNext));
			
			textures[stCount++]=(float) (angradNext/(2*Math.PI));//st座標
			textures[stCount++]=0;
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點座標資料緩沖
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序為本機動作系統順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為float型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        
        //st座標資料起始化
        ByteBuffer cbb = ByteBuffer.allocateDirect(textures.length*4);//建立頂點紋理資料緩沖
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序為本機動作系統順序
        mTexCoorBuffer = cbb.asFloatBuffer();//轉為float型緩沖
        mTexCoorBuffer.put(textures);//向緩沖區中放入頂點紋理資料
        mTexCoorBuffer.position(0);//設定緩沖區起始位置
    }
    public void drawSelf(GL10 gl)
    {
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    	gl.glVertexPointer
    	(
    			3, 
    			GL10.GL_FLOAT, 
    			0, 
    			mVertexBuffer
    	);
    	gl.glEnable(GL10.GL_TEXTURE_2D);
    	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    	gl.glTexCoordPointer
    	(
    			2, 
    			GL10.GL_FLOAT, 
    			0, 
    			mTexCoorBuffer
    	);
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, circleSideId);
    	
    	gl.glDrawArrays
    	(
    			GL10.GL_TRIANGLES, 
    			0, 
    			vCount
    	);
    }
}
