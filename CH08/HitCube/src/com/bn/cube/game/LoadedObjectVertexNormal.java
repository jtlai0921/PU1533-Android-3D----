package com.bn.cube.game;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;

import android.opengl.GLES20;

//載入後的物體——攜帶頂點訊息，自動計算面平均法向量
public class LoadedObjectVertexNormal
{	
	int mProgram;//自訂著色管線著色器程式id  
    int muMVPMatrixHandle;//總變換矩陣參考
    int muMMatrixHandle;//位置、旋轉變換矩陣
    int maPositionHandle; //頂點位置屬性參考  
    int maNormalHandle; //頂點法向量屬性參考  
    int maLightLocationHandle;//光源位置屬性參考  
    int maCameraHandle; //攝影機位置屬性參考 
    int muColorIdHandle;
    int muDaoJuAHandle;
    int muDaoJuALevelHandle;
    String mVertexShader;//頂點著色器程式碼指令稿    	 
    String mFragmentShader;//片元著色器程式碼指令稿    
	
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖  
	FloatBuffer   mNormalBuffer;//頂點法向量資料緩沖
    int vCount=0;  
    
    public LoadedObjectVertexNormal(MySurfaceView mv,float[] vertices,float[] normals)
    {    	
    	//起始化頂點座標與著色資料
    	initVertexData(vertices,normals);
    	//起始化shader        
    	initShader(mv);
    }
    
    //起始化頂點座標與著色資料的方法
    public void initVertexData(float[] vertices,float[] normals)
    {
    	//頂點座標資料的起始化================begin============================
    	vCount=vertices.length/3;   
		
        //建立頂點座標資料緩沖
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為Float型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點座標資料的起始化================end============================
        
        //頂點法向量資料的起始化================begin============================  
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mNormalBuffer = cbb.asFloatBuffer();//轉為Float型緩沖
        mNormalBuffer.put(normals);//向緩沖區中放入頂點法向量資料
        mNormalBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點著色資料的起始化================end============================
    }

    //起始化shader
    public void initShader(MySurfaceView mv)
    {
    	//載入頂點著色器的指令稿內容
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_daoju.sh", mv.getResources());
        //載入片元著色器的指令稿內容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_daoju.sh", mv.getResources());  
        //基於頂點著色器與片元著色器建立程式
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
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
        muColorIdHandle=GLES20.glGetUniformLocation(mProgram, "uColorId");
        muDaoJuAHandle=GLES20.glGetUniformLocation(mProgram, "uDaoJuA");
        muDaoJuALevelHandle=GLES20.glGetUniformLocation(mProgram, "uDaoJuALevel");
    }
    
    public void drawSelf()
    {        
    	 //制定使用某套著色器程式
    	 GLES20.glUseProgram(mProgram);
         //將最終變換矩陣傳入著色器程式
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //將位置、旋轉變換矩陣傳入著色器程式
         GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);   
         GLES20.glUniform1f(muColorIdHandle, Constant.daojuId);
         GLES20.glUniform1f(muDaoJuAHandle, Constant.daojuACount);
         GLES20.glUniform1f(muDaoJuALevelHandle, Constant.daojuAlevel);
         //將光源位置傳入著色器程式   
         GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
         //將攝影機位置傳入著色器程式   
         GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
         // 將頂點位置資料傳入著色管線
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //將頂點法向量資料傳入著色管線
         GLES20.glVertexAttribPointer  
         (
        		maNormalHandle, 
         		3,   
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mNormalBuffer
         );   
         //啟用頂點位置、法向量資料
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maNormalHandle);  
         //繪制載入的物體
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
}
