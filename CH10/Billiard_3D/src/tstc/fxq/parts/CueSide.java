package tstc.fxq.parts;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import tstc.fxq.utils.Vector3Util;
import android.opengl.GLES20;
public class CueSide{  
	int mProgram;//自訂著色管執行緒序id 
    int muMVPMatrixHandle;//總變換矩陣參考id
    int muMMatrixHandle;//位置、旋轉變換矩陣
    int maCameraHandle;//攝影機的位置參考id
    
    int maSunLightLocationHandle;//光源位置屬性參考id 
    
    int maPositionHandle; //頂點位置屬性參考id  
    int maTexCoorHandle; //頂點紋理座標屬性參考id
    int maNormalHandle;//頂點法向量參考id
    
	public FloatBuffer mVertexBuffer;
	public FloatBuffer mTexCoorBuffer;
	public FloatBuffer mNormalBuffer;//定點法向量緩沖
	
	public int vCount=0;
	public static float angleZ=0;
	float yOffset;
	float vy;
	
	//因為球桿為一頭大一頭小的物體。在定義該類別的物件是rBig的值必須大於rSmall的值，否則法向量的值會發生錯誤。
	public CueSide(MySurfaceView mv, float rBig,float rSmall,float hight,float scale,float yOffset)
	{
		this.yOffset=yOffset;
		float FLOAT_SPAN=11.25f;
		float texture[]=generateTexCoor((int)(360/FLOAT_SPAN));
		int c=0;
		int tc=texture.length;
		List<Float>vertexList=new ArrayList<Float>();//頂點座標
		List<Float>textureList=new ArrayList<Float>();//頂點紋理
		List<Float>normalList=new ArrayList<Float>();//頂點法向量
		
		for(float angle=360;angle>0;angle-=FLOAT_SPAN)
		{
			//頂點
			float x1=(float)(scale*rSmall*Math.cos(Math.toRadians(angle)));
			float y1=scale*hight;
			float z1=(float)(scale*rSmall*Math.sin(Math.toRadians(angle)));
			
			//頂點
			float x2=(float)(scale*rBig*Math.cos(Math.toRadians(angle)));
			float y2=2;
			float z2=(float)(scale*rBig*Math.sin(Math.toRadians(angle)));
			
			//法向量
			float[] aNormal1=Vector3Util.calBallArmNormal(rBig, rSmall, hight, scale, 
					x1,y1,z1,x2,y2,z2,
					angle);
			//法向量
			float[] aNormal2=Vector3Util.calBallArmNormal(rBig, rSmall, hight, scale, 
					x1,y1,z1,x2,y2,z2,
					angle);
			
			//頂點
			float x3=(float)(scale*rBig*Math.cos(Math.toRadians(angle-FLOAT_SPAN)));
			float y3=2;
			float z3=(float)(scale*rBig*Math.sin(Math.toRadians(angle-FLOAT_SPAN)));
			
			//頂點
			float x4=(float)(scale*rSmall*Math.cos(Math.toRadians(angle-FLOAT_SPAN)));
			float y4=scale*hight;
			float z4=(float)(scale*rSmall*Math.sin(Math.toRadians(angle-FLOAT_SPAN)));
			
			
			//法向量
			float[] aNormal3=Vector3Util.calBallArmNormal(rBig, rSmall, hight, scale, 
					x3,y3,z3,x4,y4,z4,
					angle);
			
			//法向量
			float[] aNormal4=Vector3Util.calBallArmNormal(rBig, rSmall, hight, scale, 
					x3,y3,z3,x4,y4,z4,
					angle);
			
			
			
			//頂點
			vertexList.add(x1);vertexList.add(y1);vertexList.add(z1);
			vertexList.add(x2);vertexList.add(y2);vertexList.add(z2);
			vertexList.add(x4);vertexList.add(y4);vertexList.add(z4);
			
			vertexList.add(x4);vertexList.add(y4);vertexList.add(z4);
			vertexList.add(x2);vertexList.add(y2);vertexList.add(z2);
			vertexList.add(x3);vertexList.add(y3);vertexList.add(z3);
			
			//頂點法向量
			normalList.add(aNormal1[0]);normalList.add(aNormal1[1]);normalList.add(aNormal1[2]);
			normalList.add(aNormal2[0]);normalList.add(aNormal2[1]);normalList.add(aNormal2[2]);
			normalList.add(aNormal4[0]);normalList.add(aNormal4[1]);normalList.add(aNormal4[2]);
			
			normalList.add(aNormal4[0]);normalList.add(aNormal4[1]);normalList.add(aNormal4[2]);
			normalList.add(aNormal2[0]);normalList.add(aNormal2[1]);normalList.add(aNormal2[2]);
			normalList.add(aNormal3[0]);normalList.add(aNormal3[1]);normalList.add(aNormal3[2]);
			
			
			//紋理
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);		
		}
		//頂點座標緩沖
		vCount=vertexList.size()/3;
		float []vertice=new float[vertexList.size()];
		for(int i=0;i<vertexList.size();i++)
		{
			vertice[i]=vertexList.get(i);
		}
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer=vbb.asFloatBuffer();
		mVertexBuffer.put(vertice);
		mVertexBuffer.position(0);
		
		
		//頂點紋理緩沖
		float[]textures=new float[textureList.size()];
		for(int i=0;i<textureList.size();i++)
		{
			textures[i]=textureList.get(i);
		}
		ByteBuffer tbb=ByteBuffer.allocateDirect(textures.length*4);
		tbb.order(ByteOrder.nativeOrder());
		mTexCoorBuffer=tbb.asFloatBuffer();
		mTexCoorBuffer.put(textures);
		mTexCoorBuffer.position(0);
		
		
        //建立繪制頂點法向量緩沖
		float []normals=new float[normalList.size()];
		for(int i=0;i<normalList.size();i++)
		{
			normals[i]=normalList.get(i);
		}
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mNormalBuffer = nbb.asFloatBuffer();//轉為float型緩沖
        mNormalBuffer.put(normals);//向緩沖區中放入頂點座標資料
        mNormalBuffer.position(0);//設定緩沖區起始位置
        
        //起始化shader
        initShader(mv);
	}
    //起始化shader
    public void initShader(MySurfaceView mv)
    {
    	//取得自訂著色管執行緒序id 
        mProgram = ShaderManager.getTexLightShader();
        
        //取得程式中攝影機位置參考id
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera"); 
        
        //取得程式中頂點位置屬性參考id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //取得程式中頂點紋理屬性參考id   
        maTexCoorHandle=GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //頂點法向量
        maNormalHandle=GLES20.glGetAttribLocation(mProgram, "aNormal");
        
        //取得程式中總變換矩陣參考id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
        //取得位置、旋轉變換矩陣參考id
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        //取得程式中光源位置參考id
        maSunLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocationSun");
        
    }
    public void drawSelf(int texId)
    {
   	 	//制定使用某套shader程式
   	 	GLES20.glUseProgram(mProgram);
        //將最終變換矩陣傳入shader程式
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //將位置、旋轉變換矩陣傳入shader程式
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        //將攝影機位置傳入shader程式   
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
        //將光源位置傳入shader程式   
        GLES20.glUniform3fv(maSunLightLocationHandle, 1, MatrixState.lightPositionFB);
        
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
        //為畫筆指定頂點經緯度資料
        GLES20.glVertexAttribPointer  
        (
        		maTexCoorHandle,  
        		2, 
        		GLES20.GL_FLOAT, 
        		false,
               	2*4,   
               	mTexCoorBuffer
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
	public float[] generateTexCoor(int share)
	{
		float[]result=new float[share*6*2];
		int c=0;
		float bs=1.0f/share;
		for(int i=0;i<share;i++)
		{
			float s=i*bs;
			result[c++]=s;
			result[c++]=0;
			
			result[c++]=s;
			result[c++]=1;
			
			result[c++]=s+bs;
			result[c++]=0;
			
			result[c++]=s+bs;
			result[c++]=0;
			
			result[c++]=s;
			result[c++]=1;
			
			result[c++]=s+bs;
			result[c++]=1;
			
		}
		return result;	
	}
}


