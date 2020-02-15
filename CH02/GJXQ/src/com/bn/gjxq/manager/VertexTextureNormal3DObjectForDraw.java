package com.bn.gjxq.manager;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
//載入後的物體——攜帶頂點、法向量、紋理訊息
public class VertexTextureNormal3DObjectForDraw 
{
	public FloatBuffer mVertexBuffer; //頂點座標資料緩沖
	public FloatBuffer mTexBuffer; //頂點紋理座標緩沖
	public FloatBuffer   mNormalBuffer;//頂點法向量資料緩沖
	public int vCount = 0; //頂點數
	public AABB3 aabb;//包圍盒
	
      
    public VertexTextureNormal3DObjectForDraw(FloatBuffer mVertexBuffer,FloatBuffer mTexBuffer,FloatBuffer mNormalBuffer,int vCount,AABB3 aabb) 
    {    	
    	this.mVertexBuffer=mVertexBuffer;
		this.mTexBuffer=mTexBuffer;  
		this.mNormalBuffer=mNormalBuffer;
		this.vCount=vCount;
		this.aabb=aabb;
    }

    public void drawSelf(GL10 gl,int texId)
    {      
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//容許使用頂點陣列
		gl.glEnable(GL10.GL_TEXTURE_2D); //開啟紋理
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); //容許使用紋理ST座標緩沖
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);//啟用頂點法向量陣列
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);	//為畫筆指定頂點座標資料  
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);  //為畫筆指定紋理ST座標緩沖		
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);//為畫筆指定頂點法向量資料
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);   //綁定目前紋理
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount); //繪制圖形
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//關閉使用頂點陣列
		gl.glDisableClientState(GL10.GL_TEXTURE_2D); //關閉紋理
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//關閉使用紋理ST座標陣列
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);//關閉頂點法向量陣列
    }
}
