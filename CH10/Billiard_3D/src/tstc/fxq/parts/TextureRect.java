package tstc.fxq.parts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;
//這個主要是歡迎界面 的紋理矩形
public class TextureRect 
{
	int mProgram;//自訂著色管執行緒序id 
    int muMVPMatrixHandle;//總變換矩陣參考id   
    int muMMatrixHandle;//位置、旋轉變換矩陣
    int maCameraHandle; //攝影機位置屬性參考id  
    int maPositionHandle; //頂點位置屬性參考id  
    int maNormalHandle; //頂點法向量屬性參考id  
    int maTexCoorHandle; //頂點紋理座標屬性參考id  
    int maSunLightLocationHandle;//光源位置屬性參考id  
    
    String mVertexShader;//頂點著色器    	 
    String mFragmentShader;//片元著色器
	
    private FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
    private FloatBuffer   mTextureBuffer;//頂點著色資料緩沖
    int vCount;//頂點數量
    int texId;//紋理Id
    
    float width;
    float height;
    float sEnd;
    float tEnd;
    
	public TextureRect(float width,float height,	//紋理矩形的長寬
				float sEnd,float tEnd 		//紋理座標取得器取得的點陣圖右下點座標
				)
	{
		this.width=width;
    	this.height=height;
    	this.sEnd=sEnd;
    	this.tEnd=tEnd;
    	
    	//頂點座標資料的起始化================begin============================
        vCount=6;//每個格子兩個三角形，每個三角形3個頂點        
        float vertices[]=
        {
        		-width/2, height/2,0,
        		-width/2, -height/2,0,
        		width/2, height/2,0,
        		
        		-width/2, -height/2,0,
        		width/2, -height/2,0,
        		width/2, height/2,0
        };
        //建立頂點座標資料緩沖
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為int型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        float textures[]=
        {
        		0f,0f,    0f,tEnd,    sEnd,0f,
        		0f,tEnd,  sEnd,tEnd,  sEnd,0f
        };
        //建立頂點紋理資料緩沖
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTextureBuffer= tbb.asFloatBuffer();//轉為Float型緩沖
        mTextureBuffer.put(textures);//向緩沖區中放入頂點著色資料
        mTextureBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點紋理資料的起始化================end============================  
        
        intShader();
        
	}
    public void intShader()
    {
        this.mProgram=ShaderManager.getTexShader(); 
        //取得程式中頂點位置屬性參考id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //取得程式中頂點紋理座標屬性參考id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //取得程式中總變換矩陣參考id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }
	public void drawSelf(int texId)
	{
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
        //為畫筆指定頂點紋理座標資料
        GLES20.glVertexAttribPointer  
        (
       		maTexCoorHandle, 
        		2, 
        		GLES20.GL_FLOAT, 
        		false,
               2*4,   
               mTextureBuffer
        );   
        //容許頂點位置資料陣列
        GLES20.glEnableVertexAttribArray(maPositionHandle);  
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
        
        //綁定紋理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        
        //繪制紋理矩形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
	}
	
}
