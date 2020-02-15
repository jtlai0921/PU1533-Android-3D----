package com.bn.ball.wt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

//紋理矩形
public class TextureRect 
{	
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mTexCoorBuffer;//頂點紋理座標資料緩沖
    int vCount=0;
   // int texId;
    public TextureRect(float width,float height)
    {    	
    	//起始化頂點座標資料的方法
    	initVertexData(width,height);
    }
    
    //起始化頂點座標資料的方法
    public void initVertexData(float width,float height)
    {
    	//頂點座標資料的起始化================begin============================
        vCount=6;
        float vertices[]=new float[]
        {
        	-width,height,0,
        	-width,-height,0,
        	width,-height,0,
        	
        	width,-height,0,
        	width,height,0,
        	-width,height,0
        };
		
        //建立頂點座標資料緩沖
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為Float型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        
        float texCoor[]=new float[]//頂點彩色值陣列，每個頂點4個色彩值RGBA
        {
        		0,0, 0,1, 1,1,
        		1,1, 1,0, 0,0
        };        
        //建立頂點紋理座標資料緩沖
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTexCoorBuffer = cbb.asFloatBuffer();//轉為Float型緩沖
        mTexCoorBuffer.put(texCoor);//向緩沖區中放入頂點著色資料
        mTexCoorBuffer.position(0);//設定緩沖區起始位置
    }
    
    public void drawSelf(GL10 gl,int texId)
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
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
    	
    	gl.glDrawArrays
    	(
    			GL10.GL_TRIANGLES, 
    			0, 
    			vCount
    	);
    }
}
