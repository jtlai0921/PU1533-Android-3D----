package com.bn.cube.view;
 
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;


import android.opengl.GLES20;

//紋理矩形
public class PieceTexture 
{	
	int mProgram;//自訂著色管線著色器程式id
    int muMVPMatrixHandle;//總變換矩陣參考id
    int maPositionHandle; //頂點位置屬性參考id  
    int maTexCoorHandle; //頂點紋理座標屬性參考id 
    int maAlphaHandle;
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mTexCoorBuffer;//頂點紋理座標資料緩沖
	
	float alpha=1f;
    int vCount=0;
    
    public PieceTexture(float width,float height,MyGLSurfaceView mv)
    {    	
    	//起始化頂點座標資料的方法
    	initVertexData(width,height);
    	//起始化著色器程式的方法
    	initShader(mv);
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
    //起始化著色器程式的方法
    public void initShader(MyGLSurfaceView mv)
    {
    	//載入頂點著色器的指令稿內容       
      String  mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_piece.sh", mv.getResources());
        ShaderUtil.checkGlError("==ss==");   
        //載入片元著色器的指令稿內容
      String  mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_piece.sh", mv.getResources());  
        //基於頂點著色器與片元著色器建立程式
        ShaderUtil.checkGlError("==ss==");      
        mProgram =ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //取得程式中頂點紋理座標屬性參考id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        maAlphaHandle=GLES20.glGetAttribLocation(mProgram, "aAlpha");
        //取得程式中總變換矩陣參考id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }

    //weisu重新定義 
    public void drawSelf(int texId)
    { 
    	 //制定使用某套shader程式
    	 GLES20.glUseProgram(mProgram);        
    	 //起始化變換矩陣
         //將最終變換矩陣傳入shader程式
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //傳入頂點位置資料
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //傳入頂點紋理座標資料
         GLES20.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );
         
        // GLES20.glUniform1f(maAlphaHandle,1);
         
         //容許頂點位置資料陣列
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
         GLES20.glEnableVertexAttribArray(maAlphaHandle);  
         
         
         //綁定紋理
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         
         //繪制紋理矩形
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
    
    
    //weisu重新定義 
    public void drawSelf(int texId,FloatBuffer mAlphaBuffer)
    { 
    	 //制定使用某套shader程式
    	 GLES20.glUseProgram(mProgram);        
    	 //起始化變換矩陣
         //將最終變換矩陣傳入shader程式
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //傳入頂點位置資料
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //傳入頂點紋理座標資料
         GLES20.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );
         GLES20.glVertexAttribPointer  
         (
        		maAlphaHandle, 
         		1, 
         		GLES20.GL_FLOAT, 
         		false,
                1*4,   
                mAlphaBuffer
         ); 
         GLES20.glUniform1f(maAlphaHandle,alpha);
         
         //容許頂點位置資料陣列
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
         GLES20.glEnableVertexAttribArray(maAlphaHandle);  
         //綁定紋理
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         
         //繪制紋理矩形
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
}