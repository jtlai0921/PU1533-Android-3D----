package com.bn.gjxq.manager;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

//只有頂點與紋理座標的物體
public class VertexTexture3DObjectForDraw
{
	public FloatBuffer mVertexBuffer; //頂點座標資料緩沖
	public FloatBuffer mTexBuffer; //頂點紋理座標緩沖
	public int vCount = 0; //頂點數
	public AABB3 aabb;//包圍盒
	
	//vertices頂點  texCoor紋理ST座標
	public VertexTexture3DObjectForDraw(FloatBuffer mVertexBuffer,FloatBuffer mTexBuffer,int vCount,AABB3 aabb) 
	{
		this.mVertexBuffer=mVertexBuffer;
		this.mTexBuffer=mTexBuffer;  
		this.vCount=vCount;
		this.aabb=aabb;
	}

	public void drawSelf(GL10 gl, int texId) {

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//容許使用頂點陣列
		gl.glEnable(GL10.GL_TEXTURE_2D); //開啟紋理
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); //容許使用紋理ST座標緩沖
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);	//為畫筆指定頂點座標資料  
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);  //為畫筆指定紋理ST座標緩沖
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);   //綁定目前紋理
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount); //繪制圖形
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//關閉使用頂點陣列
		gl.glDisableClientState(GL10.GL_TEXTURE_2D); //關閉紋理
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//關閉使用紋理ST座標緩沖
	}
}
