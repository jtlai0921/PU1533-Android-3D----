package com.cw.game;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.cw.util.MatrixState;

import android.opengl.GLES20;

//紋理矩形
public class TextureRect 
{	

	int mProgram;//自訂著色管線著色器程式id  
    int muMVPMatrixHandle;//總變換矩陣參考
    int muMMatrixHandle;//位置、旋轉變換矩陣
    int maPositionHandle; //頂點位置屬性參考  
    int maNormalHandle; //頂點法向量屬性參考  
    int maLightLocationHandle;//光源位置屬性參考  
    int maCameraHandle; //攝影機位置屬性參考 
    int muIsShadow;//是否繪制陰影屬性參考
    int muProjCameraMatrixHandle;//投影、攝影機群組合矩陣參考
    String mVertexShader;//頂點著色器程式碼指令稿    	 
    String mFragmentShader;//片元著色器程式碼指令稿    
	FloatBuffer   mNormalBuffer;//頂點法向量資料緩沖

	int uTexHandle;//外觀紋理屬性參考id
    int maTexCoorHandle; //頂點紋理座標屬性參考id 
    
    
    
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mTexCoorBuffer;//頂點紋理座標資料緩沖
    int vCount=0;   
    float wid;
    float hei;
    float zf;
    public TextureRect(int mProgram,MySurfaceView mv,float width,float heigth,float zOffset)//50,40,0
    {    	
    	this.wid=width;
    	this.hei=heigth;
    	this.zf=zOffset;
    	//起始化頂點座標與著色資料
    	initVertexData();
    	//起始化shader        
    	initShader(mv,mProgram);
    }
    
    //起始化頂點座標與著色資料的方法
    public void initVertexData()
    {
    	//頂點座標資料的起始化
        vCount=6;
        float vertices[]=new float[]
        {
        	-wid,hei,zf,
        	-wid,-hei,zf,
        	wid,-hei,zf,
        	  
        	wid,-hei,zf,
        	wid,hei,zf,
        	-wid,hei,zf
        };
		
        //建立頂點座標資料緩沖
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為Float型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        
        //頂點紋理座標資料的起始化
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
        
        float mNormals[]=new float[]
                {
                	 0,1,0,0,1,0,0,1,0,
                	 0,1,0,0,1,0,0,1,0,
                };

                //建立頂點紋理資料緩沖b
                ByteBuffer mnbb = ByteBuffer.allocateDirect(mNormals.length*4);
                mnbb.order(ByteOrder.nativeOrder());//設定位元組順序
                mNormalBuffer= mnbb.asFloatBuffer();//轉為Float型緩沖
                mNormalBuffer.put(mNormals);//向緩沖區中放入頂點著色資料
                mNormalBuffer.position(0);//設定緩沖區起始位置
    }

    //起始化shader
    public void initShader(MySurfaceView mv,int mProgram)
    {
            this.mProgram=mProgram;
	        maTexCoorHandle=GLES20.glGetAttribLocation(mProgram, "aTexCoor");  
	        uTexHandle=GLES20.glGetUniformLocation(mProgram, "sTexture");
	       //取得程式中頂點位置屬性參考  
	        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
	        //取得程式中頂點彩色屬性參考  
	        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
	        //取得程式中總變換矩陣參考
	        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
	        //取得位置、旋轉變換矩陣參考
	        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix"); 
	        //取得程式中光源位置參考
	        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
	        //取得程式中攝影機位置參考
	        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera");  
	        //取得程式中是否繪制陰影屬性參考
	        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow"); 
	        //取得程式中投影、攝影機群組合矩陣參考
	        muProjCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjCameraMatrix"); 
 
    
    }
    
    public void drawSelf(int texId)
    {        
    	GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
        //將攝影機位置傳入著色器程式   
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
        //將是否繪制陰影屬性傳入著色器程式 
        GLES20.glUniform1i(muIsShadow, 0);       
        //將投影、攝影機群組合矩陣傳入著色器程式
        GLES20.glUniformMatrix4fv(muProjCameraMatrixHandle, 1, false, MatrixState.getViewProjMatrix(), 0); 
        GLES20.glUniform1i(uTexHandle, 0);
         
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //傳輸頂點紋理座標資料
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
         		maNormalHandle, 
         		3,   
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mNormalBuffer
         );
         //容許頂點位置、紋理座標資料陣列
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);
         GLES20.glEnableVertexAttribArray(maNormalHandle);  
         //綁定紋理
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         
         //繪制紋理矩形
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
}