package com.bn.ball.cube;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.bn.ball.MySurfaceView;
public class Rectangular 
{
	
	private FloatBuffer mVertexBuffer;//頂點座標資料緩沖
	private FloatBuffer mTextureBuffer;
    int vCount=0;	
    float UNIT_SIZE=0.3f;
    int wallId;
    public Rectangular(MySurfaceView mv,int wallId)
    {
    	//起始化頂點座標與著色資料
    	initVertexData();
    	this.wallId=wallId;
    	
    }
    
  //起始化頂點座標與著色資料的方法
    public void initVertexData()
    {
    	//頂點座標資料的起始化================begin============================
        float vertices[]=new float[]
	    {
        		-UNIT_SIZE,UNIT_SIZE,0,
				-UNIT_SIZE,-UNIT_SIZE,0,
				UNIT_SIZE,-UNIT_SIZE,0,
				
				-UNIT_SIZE,UNIT_SIZE,0,
				UNIT_SIZE,-UNIT_SIZE,0,
				UNIT_SIZE,UNIT_SIZE,0
	     };
        
        vCount=vertices.length/3;
		
        //建立頂點座標資料緩沖
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為Float型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        
        //起始化彩色陣列
        float textureCoors[]=new float[]
    	        {
    	            0,0,
    	            0,1,
    	            1,1,
    	                               				
    	            0,0,
    	            1,1,
    	            1,0,              		
    	          };
    	        ByteBuffer cbb = ByteBuffer.allocateDirect(textureCoors.length*4);
    	        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
    	        mTextureBuffer = cbb.asFloatBuffer();//轉為int型緩沖
    	        mTextureBuffer.put(textureCoors);//向緩沖區中放入頂點著色資料
    	        mTextureBuffer.position(0);//設定緩沖區起始位置
    }
    
    public void drawSelf(GL10 gl)
    {        
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//啟用頂點座標陣列
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        
		//為畫筆指定頂點座標資料
        gl.glVertexPointer
        (
        		3,				
        		GL10.GL_FLOAT,
        		0, 				
        		mVertexBuffer	
        );    
        //開啟紋理
        gl.glEnable(GL10.GL_TEXTURE_2D);   
        //容許使用紋理ST座標緩沖
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //為畫筆指定紋理ST座標緩沖
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        //綁定目前紋理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, wallId);		
        //繪制圖形
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		
        		0, 			 			
        		vCount				
        );
    }
}
