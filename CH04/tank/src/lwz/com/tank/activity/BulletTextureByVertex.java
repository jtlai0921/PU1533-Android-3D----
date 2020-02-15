
package lwz.com.tank.activity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class BulletTextureByVertex {
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mTextureBuffer;
	int texId;
	int vCount=0;
	
	float x=0;
	float y=0;
	float z=0;
	float r;
	float h;
	float H;
	float tempAngle=15;
	
	public BulletTextureByVertex(float r,float h,float H ,int texId )
	{
		this.r=r;
		this.h=h;
		this.H=H;
		this.texId=texId;
		
		ArrayList<Float> bal=new ArrayList<Float>();//頂點存放清單
		ArrayList<Float> balTexture=new ArrayList<Float>();
		
		for(float circle_degree=0.0f;circle_degree<360.0f;circle_degree+=tempAngle)
		{
			float x0=x;
			float y0=y;
			float z0=z;
			
			float x1=(float) (r*Math.sin(Math.toRadians(circle_degree)));
			float y1=(float) (r*Math.cos(Math.toRadians(circle_degree)));
			float z1=z;
			
			float x2=(float) (r*Math.sin(Math.toRadians(circle_degree+tempAngle)));
			float y2=(float) (r*Math.cos(Math.toRadians(circle_degree+tempAngle)));
			float z2=z;
			
			float x3=(float) (r*Math.sin(Math.toRadians(circle_degree)));
			float y3=(float) (r*Math.cos(Math.toRadians(circle_degree)));
			float z3=z+h;
			
			float x4=(float) (r*Math.sin(Math.toRadians(circle_degree+tempAngle)));
			float y4=(float) (r*Math.cos(Math.toRadians(circle_degree+tempAngle)));
			float z4=z+h;
			
			float x5=x;
			float y5=y;
			float z5=z+H;
			
			bal.add(x5);bal.add(y5);bal.add(z5);
			bal.add(x4);bal.add(y4);bal.add(z4);
			bal.add(x3);bal.add(y3);bal.add(z3);
			
			bal.add(x4);bal.add(y4);bal.add(z4);
			bal.add(x2);bal.add(y2);bal.add(z2);
			bal.add(x3);bal.add(y3);bal.add(z3);
			
			bal.add(x3);bal.add(y3);bal.add(z3);
			bal.add(x2);bal.add(y2);bal.add(z2);
			bal.add(x1);bal.add(y1);bal.add(z1);
			
			bal.add(x0);bal.add(y0);bal.add(z0);
			bal.add(x1);bal.add(y1);bal.add(z1);
			bal.add(x2);bal.add(y2);bal.add(z2);
			
			balTexture.add(0.5f);balTexture.add(0.0f);
			balTexture.add(0.0f);balTexture.add(1.0f);
			balTexture.add(1.0f);balTexture.add(1.0f);
			
			balTexture.add(0.0f);balTexture.add(0.0f);
			balTexture.add(0.0f);balTexture.add(1.0f);
			balTexture.add(1.0f);balTexture.add(0.0f);
			
			balTexture.add(1.0f);balTexture.add(0.0f);
			balTexture.add(0.0f);balTexture.add(1.0f);
			balTexture.add(1.0f);balTexture.add(1.0f);
			
			balTexture.add(0.5f);balTexture.add(0.0f);
			balTexture.add(0.0f);balTexture.add(1.0f);
			balTexture.add(1.0f);balTexture.add(1.0f);
		
			vCount=bal.size()/3;//確定頂點數量
			
			//頂點
			float[] vertices=new float[vCount*3];
			for(int i=0;i<vCount*3;i++)
			{
				vertices[i]=bal.get(i);
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
			
	        float textures[]=new float[balTexture.size()];
	        for(int i=0;i<balTexture.size();i++)
	        {
	        	textures[i]=balTexture.get(i);
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
