package com.bn.ball.wt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
public class Circle {
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mTexCoorBuffer;//頂點紋理座標資料緩沖
	int YuanId;
    int vCount=0; 
    float yAngle=0;//繞y軸旋轉的角度
    
    public Circle(float scale,float r,int n,int yuanId)
    {
    	this.YuanId=yuanId;
    	//呼叫起始化頂點資料的initVertexData方法
    	initVertexData(scale,r,n);
    }
    public void initVertexData(
    		float scale,	//大小
    		float r,		//半徑
    		int n)		//切分的份數
    {
    	r=r*scale;
		float angdegSpan=360.0f/n;	//頂角的度數
		vCount=3*n;//頂點個數，共有n個三角形，每個三角形都有三個頂點
		
		float[] vertices=new float[vCount*3];//座標資料
		float[] textures=new float[vCount*2];//頂點紋理S、T座標值陣列
		//座標資料起始化
		int count=0;
		int stCount=0;
		for(float angdeg=0;Math.ceil(angdeg)<360;angdeg+=angdegSpan)
		{
			double angrad=Math.toRadians(angdeg);//目前弧度
			double angradNext=Math.toRadians(angdeg+angdegSpan);//下一弧度
			//中心點
			vertices[count++]=0;//頂點座標
			vertices[count++]=0; 
			vertices[count++]=0;
			
			textures[stCount++]=0.5f;//st座標
			textures[stCount++]=0.5f;
			//目前點
			vertices[count++]=(float) (-r*Math.sin(angrad));//頂點座標
			vertices[count++]=(float) (r*Math.cos(angrad));
			vertices[count++]=0;
			
			textures[stCount++]=(float) (0.5f-0.5f*Math.sin(angrad));//st座標
			textures[stCount++]=(float) (0.5f-0.5f*Math.cos(angrad));
			//下一點
			vertices[count++]=(float) (-r*Math.sin(angradNext));//頂點座標
			vertices[count++]=(float) (r*Math.cos(angradNext));
			vertices[count++]=0;
			
			textures[stCount++]=(float) (0.5f-0.5f*Math.sin(angradNext));//st座標
			textures[stCount++]=(float) (0.5f-0.5f*Math.cos(angradNext));
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點座標資料緩沖
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序為本機動作系統順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為float型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        
        //紋理座標資料起始化
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
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, YuanId);
    	
    	gl.glDrawArrays
    	(
    			GL10.GL_TRIANGLES, 
    			0, 
    			vCount
    	);
    }
}
