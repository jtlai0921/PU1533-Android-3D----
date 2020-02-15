package tstc.fxq.parts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;

public class MiniLine { 
    
	int mProgram;//自訂著色管執行緒序id
    int muMVPMatrixHandle;//總變換矩陣參考id
    int maPositionHandle; //頂點位置屬性參考id  
    int maColorHandle; //頂點彩色屬性參考id
	
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mColorBuffer;//頂點著色資料緩沖
	
	int vCount;//頂點數量
	public MiniLine(MySurfaceView mv)
	{
		vCount=2;
		//頂點著色資料的起始化================begin============================
        
		float []colors=new float[]{
				1, 1, 1, 1,
				1, 1, 1, 1
		};

        //建立頂點著色資料緩沖
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mColorBuffer = cbb.asFloatBuffer();//轉為Float型緩沖
        mColorBuffer.put(colors);//向緩沖區中放入頂點著色資料
        mColorBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點著色資料的起始化================end============================
        //起始化shader
        intShader(mv);
	}

    //起始化shader
    public void intShader(MySurfaceView mv)
    {
    	//取得自訂著色管執行緒序id 
        mProgram = ShaderManager.getColorShader();
        //取得程式中頂點位置屬性參考id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //取得程式中頂點彩色屬性參考id  
        maColorHandle= GLES20.glGetAttribLocation(mProgram, "aColor");
        //取得程式中總變換矩陣參考id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }
    public void drawSelf(float[] vertices)
    {
		//建立頂點座標資料緩沖
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為Float型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        
        
   	 	//制定使用某套shader程式
   	 	GLES20.glUseProgram(mProgram);
        //將最終變換矩陣傳入shader程式
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);        
        
        //為畫筆指定頂點位置資料    
        GLES20.glVertexAttribPointer        
        (
        		maPositionHandle,   
        		3, 
        		GLES20.GL_FLOAT, 
        		false,
        		3*4, 
        		mVertexBuffer   
        );        
        //為畫筆指定頂點著色資料
        GLES20.glVertexAttribPointer  
        (
       		maColorHandle, 
        		4, 
        		GLES20.GL_FLOAT, 
        		false,
               4*4,   
               mColorBuffer
        );   
        //容許頂點位置資料陣列
        GLES20.glEnableVertexAttribArray(maPositionHandle);  
        GLES20.glEnableVertexAttribArray(maColorHandle);       
        
        //繪制線
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, vCount);//繪制線
    }
}
