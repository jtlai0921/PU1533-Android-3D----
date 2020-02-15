package com.f.gamebody;

import com.f.pingpong.Constant;
import com.f.util.MatrixState;

import android.opengl.GLES20;
import static com.f.pingpong.Constant.*;
import static com.f.pingpong.ShaderManager.*;
//有波浪效果的紋理矩形
public class GameFlyFlag 
{	
	private int mPrograms;//自訂著色管線著色器程式id
	private int muMVPMatrixHandle;//總變換矩陣參考
	private int maPositionHandle; //頂點位置屬性參考  
	private int maTexCoorHandle; //頂點紋理座標屬性參考  
	private int maStartAngleHandle; //本框起始角度屬性參考
	private int muWidthSpanHandle;//水平長度總跨度參考
	private float currStartAngle=0;//目前框的起始角度0~2PI
    
    public GameFlyFlag(int which)
    {    	
    	//起始化shader        
    	initShader(which);
    	//啟動一個執行緒定時換框
    	new Thread()
    	{
    		public void run()
    		{
    			while(Constant.FLAG_FLY_THREAD)
    			{
    			    currStartAngle+=(float)(Math.PI/16);
        			try 
        			{
    					Thread.sleep(50);
    				} catch (InterruptedException e) 
    				{
    					e.printStackTrace();
    				}
    			}     
    		}    
    	}.start();  
    }
    //起始化shader
    public void initShader(int which)
    {
        //基於頂點著色器與片元著色器建立程式
        mPrograms = (which == 0) ? program[5] : program[9];
        //取得程式中頂點位置屬性參考  
        maPositionHandle = GLES20.glGetAttribLocation(mPrograms, "aPosition");
        //取得程式中頂點紋理座標屬性參考  
        maTexCoorHandle= GLES20.glGetAttribLocation(mPrograms, "aTexCoor");
        //取得程式中總變換矩陣參考
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mPrograms, "uMVPMatrix");  
        //取得本框起始角度屬性參考
        maStartAngleHandle=GLES20.glGetUniformLocation(mPrograms, "uStartAngle");  
        //取得水平長度總跨度參考
        muWidthSpanHandle=GLES20.glGetUniformLocation(mPrograms, "uWidthSpan");  
    }
    public void drawSelf(int texId)
    {        
    	 //制定使用某套shader程式  
    	 GLES20.glUseProgram(mPrograms); 
         //將最終變換矩陣傳入shader程式
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //將本框起始角度傳入shader程式
         GLES20.glUniform1f(maStartAngleHandle, currStartAngle);
         //將水平長度總跨度傳入shader程式
         GLES20.glUniform1f(muWidthSpanHandle, WIDTH_SPAN);  
         //將頂點位置資料傳入著色管線
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,            
         		3, 
         		GLES20.GL_FLOAT,   
         		false,
                3*4,   
                vertexBuffer[0][10]
         );       
         //將頂點紋理座標資料傳入著色管線
         GLES20.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT, 
         		false,
                2*4,   
                texCoorBuffer[0][4]
         );   
         //啟用頂點位置、紋理座標資料
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
         //綁定紋理
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount[0][2]); 
    }
}
