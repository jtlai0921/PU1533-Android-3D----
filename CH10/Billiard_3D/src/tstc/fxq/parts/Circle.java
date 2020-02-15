package tstc.fxq.parts;
import java.nio.ByteBuffer;
import static tstc.fxq.constants.Constant.*;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;
public class Circle {  
	int mProgram;//自訂著色管執行緒序id 
    int muMVPMatrixHandle;//總變換矩陣參考id 
    int maPositionHandle; //頂點位置屬性參考id  
    int maNormalHandle; //頂點法向量屬性參考id  
    int maTexCoorHandle; //頂點紋理座標屬性參考id 
    
	FloatBuffer mVertexBuffer;
	private FloatBuffer mNormalBuffer;//頂點法向量資料緩沖
	FloatBuffer mTexCoorBuffer;	
	int vCount;
	public Circle(MySurfaceView mv, float scaleX,float scaleZ)
	{	
		float SPAN=18;
		ArrayList<Float> alVertex=new ArrayList<Float>();
		for(int i=0;i<360/SPAN;i++)
		{
			float x1=0.0f;float y1=0.0f;float z1=0.0f;//座標軸原點
			//根據單位角度計算第二個點座標
			float x2=(float)Math.cos(Math.toRadians(i*SPAN))*scaleX*TABLE_UNIT_SIZE;
			float y2=0.0f;
			float z2=(float)Math.sin(Math.toRadians(i*SPAN))*scaleZ*TABLE_UNIT_SIZE;
			//計算第三個點座標
			float x3=(float)Math.cos(Math.toRadians((i+1)*SPAN))*scaleX*TABLE_UNIT_SIZE;
			float y3=0.0f;
			float z3=(float)Math.sin(Math.toRadians((i+1)*SPAN))*scaleZ*TABLE_UNIT_SIZE;
			//按逆時針方向擺放頂點，群組成三角形
			alVertex.add(x1);
			alVertex.add(y1);
			alVertex.add(z1);	
			alVertex.add(x3);
			alVertex.add(y3);
			alVertex.add(z3);		
			alVertex.add(x2);
			alVertex.add(y2);
			alVertex.add(z2);		
		}
		vCount=alVertex.size()/3;
		float[] verteices=new float[vCount*3];
		for(int i=0;i<vCount*3;i++)
		{
			verteices[i]=alVertex.get(i);
		}	
		ByteBuffer vbb=ByteBuffer.allocateDirect(verteices.length*4);//將頂點座標放入資料緩沖
		vbb.order(ByteOrder.nativeOrder());	//設定位元組順序
		mVertexBuffer=vbb.asFloatBuffer();//轉為float型緩沖
		mVertexBuffer.put(verteices);//向緩沖區中放入頂點座標資料
		mVertexBuffer.position(0);//設定緩沖區起始位置 
		//頂點法向量資料
        float normals[]=new float[verteices.length];
        for(int i=0; i<normals.length; i+=3){
        	normals[i] = 0;
        	normals[i+1] = 1;
        	normals[i+2] = 0;
        }
        //建立繪制頂點法向量緩沖
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mNormalBuffer = nbb.asFloatBuffer();//轉為float型緩沖
        mNormalBuffer.put(normals);//向緩沖區中放入頂點座標資料
        mNormalBuffer.position(0);//設定緩沖區起始位置  
        //紋理資料
		float[] textureCoors=new float[vCount*2];//分配紋理座標
		for(int i=0;i<vCount/3;i++)
		{
			textureCoors[i*6]=0;
			textureCoors[i*6+1]=0;
			
			textureCoors[i*6+2]=0;
			textureCoors[i*6+3]=1;
			
			textureCoors[i*6+4]=1;
			textureCoors[i*6+5]=0;
		}
		
		ByteBuffer tbb=ByteBuffer.allocateDirect(textureCoors.length*4);//將紋理座標座標放入資料緩沖
		tbb.order(ByteOrder.nativeOrder());//設定位元組順序
		mTexCoorBuffer=tbb.asFloatBuffer();//轉為float型緩沖
		mTexCoorBuffer.put(textureCoors);//向緩沖區中放入頂點座標資料
		mTexCoorBuffer.position(0);//設定緩沖區起始位置

        //起始化shader
        intShader(mv);
	}
    //起始化shader
    public void intShader(MySurfaceView mv)
    {
    	//取得自訂著色管執行緒序id 
        mProgram = ShaderManager.getTexLightShader();
        //取得程式中頂點位置屬性參考id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition"); 
        //取得程式中頂點法向量屬性參考id  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //取得程式中頂點經緯度屬性參考id   
        maTexCoorHandle=GLES20.glGetAttribLocation(mProgram, "aTexCoor"); 
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
        
        //為畫筆指定頂點法向量資料
        GLES20.glVertexAttribPointer  
        (
        		maNormalHandle, 
        		3, 
        		GLES20.GL_FLOAT, 
        		false,
        		3*4,   
        		mNormalBuffer
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
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);         
        //繪制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
}
