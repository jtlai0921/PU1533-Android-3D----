package lwz.com.tank.tail;
 
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import lwz.com.tank.game.MySurfaceView;
import lwz.com.tank.game.OtherSurfaceView;


//紋理三角形
public class DrawBuffer 
{	 
    public DrawBuffer(MySurfaceView mv)
    { 
    }  
    
    public DrawBuffer(OtherSurfaceView mv)
    { 
    }  
    public void drawSelf(int texId,GL10 gl,FloatBuffer mVertexBuffer,FloatBuffer mTexCoorBuffer,int vCount)
    {        
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//啟用頂點座標陣列
		//為畫筆指定頂點座標資料
        gl.glVertexPointer
        (
        		3,				//每個頂點的座標數量為3  xyz 
        		GL10.GL_FLOAT,	//頂點座標值的型態為 GL_FIXED
        		0, 				//連續頂點座標資料之間的間隔
        		mVertexBuffer	//頂點座標資料
        );
        //開啟紋理
        gl.glEnable(GL10.GL_TEXTURE_2D);   
        //容許使用紋理ST座標緩沖
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //為畫筆指定紋理ST座標緩沖
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoorBuffer);
        //綁定目前紋理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		
        //繪制圖形
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//以三角形模式填充
        		0,
        		vCount 
        );
        
        //關閉紋理
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D); 
    }
}
