package tstc.fxq.parts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;
import static tstc.fxq.constants.Constant.*;
public class PoolMap {  
	int mProgram;//自訂著色管執行緒序id 
    int muMVPMatrixHandle;//總變換矩陣參考id 
    int maPositionHandle; //頂點位置屬性參考id  
    int maTexCoorHandle; //頂點紋理座標屬性參考id 
    
	private FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
    private FloatBuffer mTexCoorBuffer;//頂點紋理資料緩沖
    int vCount=0;
	public PoolMap(MySurfaceView mv)
	{
		
		float [] vertices=new float[]
        {
			//桌面四邊形
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//1
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//2
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//3
				
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//3
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//4
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//1
				
				//球桌邊框
				//左邊框
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//2
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//1
				-TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//5
				
				-TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//5
				-TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//6
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//2
				//前邊框（下）
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//2
				-TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//6
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//7
				
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//7
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//3
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//2
				
				//右邊框
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//3
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//7
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//8
				
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//8
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//4
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//3
				//後邊框（上）
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//4
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//8
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//1
				
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//8
				-TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//5
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//1
        };
		
		vCount=vertices.length/3;//頂點數量
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為Float型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        
        float[] textureCoors=
       {
    		//桌面
    		0.031f,0.078f,0.031f,0.723f,0.547f,0.723f,
    		0.547f,0.723f,0.547f,0.078f,0.031f,0.078f,
    		//左邊框
    		0.031f,0.723f,0.031f,0.078f,0,0,
    		0,0,0,0.793f,0.031f,0.723f,
    		//前邊框(下)
    		0.031f,0.723f,0,0.793f,0.582f,0.793f,
    		0.582f,0.793f,0.547f,0.723f,0.031f,0.723f,
    		//右邊框
    		0.547f,0.723f,0.582f,0.793f,0.582f,0,
    		0.582f,0,0.547f,0.078f,0.547f,0.723f,
    		//後邊框（上）
    		0.547f,0.078f,0.582f,0,0.031f,0.078f,
    		0.582f,0,0,0,0.031f,0.078f
       };
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTexCoorBuffer = tbb.asFloatBuffer();//轉為int型緩沖
        mTexCoorBuffer.put(textureCoors);//向緩沖區中放入頂點著色資料
        mTexCoorBuffer.position(0);//設定緩沖區起始位置 

        //起始化shader
        intShader(mv);
	}
    //起始化shader
    public void intShader(MySurfaceView mv)
    {
    	//取得自訂著色管執行緒序id 
        mProgram = ShaderManager.getTexShader();
        //取得程式中頂點位置屬性參考id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
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
