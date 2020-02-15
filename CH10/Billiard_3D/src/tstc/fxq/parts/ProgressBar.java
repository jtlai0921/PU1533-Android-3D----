package tstc.fxq.parts;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;

/*
 * 3D中的進度指示器類別
 */
public class ProgressBar 
{	
	int mProgram;//自訂著色管執行緒序id
    int muMVPMatrixHandle;//總變換矩陣參考id
    int maPositionHandle; //頂點位置屬性參考id  
    int maTexCoorHandle; //頂點紋理座標屬性參考id
    int muProgress;			//進度指示器位置座標
    String mVertexShader;//頂點著色器    	 
    String mFragmentShader;//片元著色器
	
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mTexCoorBuffer;//頂點紋理座標資料緩沖 
    int vCount=0;
    
    float x;
    float y;
    float width;
    float height;
    
    float sEnd;
    float tEnd;
    
    /*
     *由於進度指示器相對於螢幕置中，所以2*X+W必須等於1，否則會出現錯誤 
     */
    
    public ProgressBar(
    		MySurfaceView mv,
    		float x,float y,      //假設螢幕的長寬都是1，要繪制的進度指示器左上點座標占螢幕的比例
    		float height, 		//同樣假設螢幕的長寬都是1，要繪制的進度指示器高占螢幕的比例
    							//由於進度指示器相對於螢幕置中，所以2*X+W必須等於1，否則會出現錯誤 ,所以寬度值width由x決定
    		
    		float sEnd,float tEnd //紋理座標取得器取得的點陣圖右下點座標
    		
    )
    {    	
    	this.x = x;
    	this.y = y;
    	
    	this.height = height;
    	this.width = 1f-2*x;
    	
    	this.sEnd = sEnd;
    	this.tEnd = tEnd;
    	

    	//起始化頂點座標與著色資料
    	initVertexData();
    	//起始化shader        
    	intShader(mv);
    }
    
    //起始化頂點座標與著色資料的方法
    public void initVertexData()
    {
    	//頂點座標資料的起始化================begin============================
        vCount=6;
       
        float vertices[]=new float[]
        {
        	-(1-2*1*x),1-2*y, 0,
        	-(1-2*1*x), 1-2*y-2*height, 0,
        	-(1-2*1*x)+2*1*width, 1-2*y, 0,
        	
        	-(1-2*1*x), 1-2*y-2*height, 0,
        	-(1-2*1*x)+2*1*width, 1-2*y-2*height, 0,
        	-(1-2*1*x)+2*1*width, 1-2*y, 0
        };
		
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
        
        //頂點紋理座標資料的起始化================begin============================
        float texCoor[]=new float[]//紋理座標
        {
        		0,0, 0,tEnd, sEnd,0,
        		0,tEnd, sEnd,tEnd, sEnd,0        		
        };        
        //建立頂點紋理座標資料緩沖
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTexCoorBuffer = cbb.asFloatBuffer();//轉為Float型緩沖
        mTexCoorBuffer.put(texCoor);//向緩沖區中放入頂點著色資料
        mTexCoorBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點紋理座標資料的起始化================end============================

    }

    //起始化shader
    public void intShader(MySurfaceView mv)
    {  
        //基於頂點著色器與片元著色器建立程式
        mProgram = ShaderManager.getProgressBarShader();
        //取得程式中頂點位置屬性參考id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //取得程式中頂點紋理座標屬性參考id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //取得程式中總變換矩陣參考id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //進度的參考id
        muProgress= GLES20.glGetUniformLocation(mProgram, "uProgress");
    }
    
    public void drawSelf(int TexId,float currProgress)
    {
    	
    	currProgress = percentageChangeToX(currProgress);
    		
    	 //制定使用某套shader程式
    	 GLES20.glUseProgram(mProgram);         
         //將最終變換矩陣傳入shader程式
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
         //將目前的進度傳入到Shader程式
         GLES20.glUniform1f(muProgress, currProgress);
         
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
                mTexCoorBuffer
         );    
         //容許頂點位置資料陣列
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle); 
         //綁定紋理
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TexId);
         
         //繪制三角形
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
    
    
    //將進度轉化為3D世界中的x座標的方法
    private float percentageChangeToX(float currProgress)
    {
    	return (2*width/100f)*(currProgress-50) ;
    }
    
    
}
