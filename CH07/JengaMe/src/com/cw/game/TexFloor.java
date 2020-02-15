package com.cw.game;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.cw.util.MatrixState;

import javax.vecmath.Vector3f;

public class TexFloor {
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
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖  
	FloatBuffer   mNormalBuffer;//頂點法向量資料緩沖
	private FloatBuffer   mTextureBuffer;//頂點著色資料緩沖
	int uTexHandle;//外觀紋理屬性參考id
    int maTexCoorHandle; //頂點紋理座標屬性參考id 
    int vCount;
    float yOffset;
    
    public TexFloor(int mProgram,final float UNIT_SIZE, float yOffset,CollisionShape groundShape,DiscreteDynamicsWorld dynamicsWorld)
    {
    	this.mProgram=mProgram;
    	this.yOffset=yOffset;
		//建立剛體的起始變換物件
		Transform groundTransform = new Transform();
		groundTransform.setIdentity();
		groundTransform.origin.set(new Vector3f(0.f, yOffset, 0.f));		
		Vector3f localInertia = new Vector3f(0, 0, 0);//慣性		
		//建立剛體的運動狀態物件
		DefaultMotionState myMotionState = new DefaultMotionState(groundTransform);
		//建立剛體訊息物件
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(0, myMotionState, groundShape, localInertia);
		//建立剛體
		RigidBody body = new RigidBody(rbInfo);
		//設定反彈系數
		body.setRestitution(0.4f);
		//設定摩擦系數
		body.setFriction(0.8f);
		//將剛體加入進實體世界
		dynamicsWorld.addRigidBody(body);
		initVertexData(UNIT_SIZE);
		initShader(mProgram);
    }
    public TexFloor(int mProgram,final float UNIT_SIZE)
    {
    	this.mProgram=mProgram;
		initVertexData(UNIT_SIZE);
		initShader(mProgram);
    }
    public void initVertexData(final float UNIT_SIZE){
    	//頂點座標資料的起始化================begin============================
        vCount=6;
        float vertices[]=new float[]
        {        	
        	1*UNIT_SIZE,yOffset,1*UNIT_SIZE,
        	-1*UNIT_SIZE,yOffset,-1*UNIT_SIZE,
        	-1*UNIT_SIZE,yOffset,1*UNIT_SIZE,
        	
        	1*UNIT_SIZE,yOffset,1*UNIT_SIZE,
        	1*UNIT_SIZE,yOffset,-1*UNIT_SIZE,
        	-1*UNIT_SIZE,yOffset,-1*UNIT_SIZE,        	
        };
		
        //建立頂點座標資料緩沖
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為int型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點座標資料的起始化================end============================
        //頂點紋理資料的起始化================begin============================
        float textures[]=new float[]
        {
        	UNIT_SIZE/2,UNIT_SIZE/2,  0,0,  0,UNIT_SIZE/2,
        	UNIT_SIZE/2,UNIT_SIZE/2,  UNIT_SIZE/2,0,  0,0
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
        //頂點法向量資料
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
    public void initShader(int mProgram)
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
         GLES20.glVertexAttribPointer  
         (
         		maNormalHandle, 
         		3,   
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mNormalBuffer
         );
         //容許頂點位置資料陣列
         GLES20.glEnableVertexAttribArray(maPositionHandle);
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);
         GLES20.glEnableVertexAttribArray(maNormalHandle);  
         //綁定紋理
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         //繪制三角形
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }
}
