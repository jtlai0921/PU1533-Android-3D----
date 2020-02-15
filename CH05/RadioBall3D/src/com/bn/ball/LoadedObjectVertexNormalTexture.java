package com.bn.ball;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
//載入後的物體——攜帶頂點、法向量、紋理訊息
public class LoadedObjectVertexNormalTexture 
{
	private FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	private FloatBuffer   mNormalBuffer;//頂點法向量資料緩沖
	private FloatBuffer   mTexBuffer;//頂點紋理資料緩沖
    int vCount=0;  
    int texId;
      
    public LoadedObjectVertexNormalTexture(float[] vertices,float[] normals,float texCoors[],int texId) 
    {
    	this.texId=texId;
    	
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
        
        //法向量訊息起始化
        ByteBuffer vbn = ByteBuffer.allocateDirect(normals.length*4);
        vbn.order(ByteOrder.nativeOrder());//設定位元組順序
        mNormalBuffer = vbn.asFloatBuffer();//轉為Float型緩沖
        mNormalBuffer.put(normals);//向緩沖區中放入頂點座標資料
        mNormalBuffer.position(0);//設定緩沖區起始位置 
        
        //紋理座標緩沖起始化
        ByteBuffer vbt = ByteBuffer.allocateDirect(texCoors.length*4);
        vbt.order(ByteOrder.nativeOrder());//設定位元組順序
        mTexBuffer = vbt.asFloatBuffer();//轉為Float型緩沖
        mTexBuffer.put(texCoors);//向緩沖區中放入頂點座標資料
        mTexBuffer.position(0);//設定緩沖區起始位置 
    }

    public void drawSelf(GL10 gl)
    {        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//啟用頂點座標陣列
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);//啟用頂點法向量陣列     
        gl.glEnable(GL10.GL_TEXTURE_2D);    //開啟紋理      
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//容許使用紋理ST座標緩沖

        
		//為畫筆指定頂點座標資料
        gl.glVertexPointer
        (
        		3,				//每個頂點的座標數量為3  xyz 
        		GL10.GL_FLOAT,	//頂點座標值的型態為 GL_FIXED
        		0, 				//連續頂點座標資料之間的間隔
        		mVertexBuffer	//頂點座標資料
        ); 
        
        //為畫筆指定頂點法向量資料
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
        
        //為畫筆指定紋理ST座標緩沖
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
        //綁定目前紋理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		
        //繪制圖形
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//以三角形模式填充
        		0, 			 			//開始點編號
        		vCount					//頂點的數量
        );        
        
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);//禁用頂點法向量陣列
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//啟用頂點座標陣列
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//容許使用紋理ST座標緩沖
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }
}
