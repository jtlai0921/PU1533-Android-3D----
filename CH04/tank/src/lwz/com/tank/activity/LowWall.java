
package lwz.com.tank.activity;

import static lwz.com.tank.activity.Constant.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class LowWall {
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mTextureBuffer;
	int texId;
	int vCount=0;
	float height;
	float x;
	float y;
	float z;
	float width;
	float length;
	
	public LowWall(int texId)
	{
		this.texId = texId;
		vCount=36;
		
	//	final int UNIT_SIZE=Constant.Wall_UNIT_SIZE;		
		int rows=lowWallmapdata.length;
//		int cols=mapdata[0].length;
//        
			ArrayList<Float> alVertex=new ArrayList<Float>();
//        ArrayList<Float> alNormal=new ArrayList<Float>();
			ArrayList<Float> alTexture=new ArrayList<Float>();
			
			
        
        vCount=36*rows;
        for(int i=0;i<rows;i++)
        {
        		float x=lowWallmapdata[i][0]*Wall_UNIT_SIZE;
        		float y=lowWallmapdata[i][1]*Wall_UNIT_SIZE;
        		float z=lowWallmapdata[i][2]*Wall_UNIT_SIZE;
        		float width=lowWallmapdata[i][3]*Wall_UNIT_SIZE;
        		float length=lowWallmapdata[i][4]*Wall_UNIT_SIZE;
        		float height=lowWallmapdata[i][5]*Wall_UNIT_SIZE;
		
        		float x1=x;float y1=y;float z1=z+height;
        		float x2=x;float y2=y-length;float z2=z+height;
        		float x3=x+width;float y3=y;float z3=z+height;
        		float x4=x+width;float y4=y-length;float z4=z+height;
        		float x5=x+width;float y5=y;float z5=z;
        		float x6=x+width;float y6=y-length;float z6=z;
        		float x7=x;float y7=y;float z7=z;
        		float x8=x;float y8=y-length;float z8=z;
        		//前面
        		alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
        		alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);
        		alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
        		
        		alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
        		alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);		
        		alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
        		//右面
        		alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
        		alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
        		alVertex.add(x5);alVertex.add(y5);alVertex.add(z5);
        		
        		alVertex.add(x5);alVertex.add(y5);alVertex.add(z5);
        		alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
        		alVertex.add(x6);alVertex.add(y6);alVertex.add(z6);
        		//後面
        		alVertex.add(x5);alVertex.add(y5);alVertex.add(z5);
        		alVertex.add(x6);alVertex.add(y6);alVertex.add(z6);
        		alVertex.add(x7);alVertex.add(y7);alVertex.add(z7);
        		
        		alVertex.add(x7);alVertex.add(y7);alVertex.add(z7);
        		alVertex.add(x6);alVertex.add(y6);alVertex.add(z6);
        		alVertex.add(x8);alVertex.add(y8);alVertex.add(z8);
        		//左面
        		alVertex.add(x7);alVertex.add(y7);alVertex.add(z7);
        		alVertex.add(x8);alVertex.add(y8);alVertex.add(z8);
        		alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
        		
        		alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
        		alVertex.add(x8);alVertex.add(y8);alVertex.add(z8);
        		alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);
        		//上面
        		alVertex.add(x7);alVertex.add(y7);alVertex.add(z7);
        		alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
        		alVertex.add(x5);alVertex.add(y5);alVertex.add(z5);
        		
        		alVertex.add(x5);alVertex.add(y5);alVertex.add(z5);
        		alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
        		alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
        		//下面
        		alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);
        		alVertex.add(x8);alVertex.add(y8);alVertex.add(z8);
        		alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
        		
        		alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
        		alVertex.add(x8);alVertex.add(y8);alVertex.add(z8);
        		alVertex.add(x6);alVertex.add(y6);alVertex.add(z6);
        		//前面
        		alTexture.add(0f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(0f);
				
				alTexture.add(1f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(1f);
				//右面
				alTexture.add(0f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(0f);
				
				alTexture.add(1f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(1f);
        		//後面
				alTexture.add(0f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(0f);
				
				alTexture.add(1f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(1f);
				//左面
				alTexture.add(0f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(0f);
				
				alTexture.add(1f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(1f);
				//上面
				alTexture.add(0f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(0f);
				
				alTexture.add(1f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(1f);
				//下面
				alTexture.add(0f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(0f);
				
				alTexture.add(1f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(1f);
        }
        vCount=alVertex.size()/3;        
        float vertices[]=new float[alVertex.size()];
        for(int i=0;i<alVertex.size();i++)
        {
        	vertices[i]=alVertex.get(i);
        }		
		
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
		
        float textures[]=new float[alTexture.size()];
        for(int i=0;i<alTexture.size();i++)
        {
        	textures[i]=alTexture.get(i);
        }
      //建立頂點紋理資料緩沖
        //textureCoors.length×4是因為一個float型整數四個位元組
        ByteBuffer cbb = ByteBuffer.allocateDirect(textures.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTextureBuffer = cbb.asFloatBuffer();//轉為int型緩沖
        mTextureBuffer.put(textures);//向緩沖區中放入頂點著色資料
        mTextureBuffer.position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點紋理資料的起始化================end============================
        }  
	
	public void drawSelf(GL10 gl)
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
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
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
