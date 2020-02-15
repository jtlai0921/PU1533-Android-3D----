package tstc.fxq.parts;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;
import static tstc.fxq.constants.Constant.*;
//用於繪制的球
public class BallDingDian {	
	int mProgram;//自訂著色管執行緒序id
    int muMVPMatrixHandle;//總變換矩陣參考id
    int muMMatrixHandle;//位置、旋轉變換矩陣參考id
    int muCameraMatrixHandle;//攝影機矩陣參考id
    int muProjMatrixHandle;//投影矩陣參考id
    int maPositionHandle; //頂點位置屬性參考id  
    int maTexCoorHandle; //頂點紋理座標屬性參考id  
    int maNormalHandle; //頂點法向量屬性參考id  
    int maLightLocationHandle;//光源位置屬性參考id  
    int maCameraHandle; //攝影機位置屬性參考id 
    int muIsShadow;//是否繪制陰影屬性參考id  
    int muIsShadowFrag;//是否繪制陰影屬性參考id 
    int muBallTexHandle;//桌球紋理屬性參考id 
    int muTableTexHandle;//用於繪制陰影的桌面紋理屬性參考id 
	
	FloatBuffer   mVertexBuffer;//頂點座標資料緩沖
	FloatBuffer   mTexCoorBuffer;//頂點紋理座標資料緩沖
	FloatBuffer   mNormalBuffer;//頂點法向量資料緩沖
    int vCount=0;//頂點數量
    float scale;//尺寸
    float xOffset;
    float zOffset;
    public BallDingDian(MySurfaceView mv, float scale) 
    {
    	this.scale=scale;    	
    	
    	//取得切分整圖的紋理陣列
    	float[] texCoorArray= 
         generateTexCoor
    	 (
    			 (int)(360/ANGLE_SPAN), //紋理圖切分的列數
    			 (int)(180/ANGLE_SPAN)  //紋理圖切分的行數
    	);
        int tc=0;//紋理陣列計數器
        int ts=texCoorArray.length;//紋理陣列長度
    	
    	ArrayList<Float> alVertix=new ArrayList<Float>();//存放頂點座標的ArrayList
    	ArrayList<Float> alTexture=new ArrayList<Float>();//存放紋理座標的ArrayList
    	
        for(float vAngle=90;vAngle>-90;vAngle=vAngle-ANGLE_SPAN)//垂直方向angleSpan度一份
        {
        	for(float hAngle=360;hAngle>0;hAngle=hAngle-ANGLE_SPAN)//水平方向angleSpan度一份
        	{
        		//直印水平各到一個角度後計算對應的此點在球面上的四邊形頂點座標
        		//並建構兩個群組成四邊形的三角形
        		
        		double xozLength=scale*UNIT_SIZE*Math.cos(Math.toRadians(vAngle));
        		float x1=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
        		float z1=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
        		float y1=(float)(scale*UNIT_SIZE*Math.sin(Math.toRadians(vAngle)));
        		
        		xozLength=scale*UNIT_SIZE*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN));
        		float x2=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
        		float z2=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
        		float y2=(float)(scale*UNIT_SIZE*Math.sin(Math.toRadians(vAngle-ANGLE_SPAN)));
        		
        		xozLength=scale*UNIT_SIZE*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN));
        		float x3=(float)(xozLength*Math.cos(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float z3=(float)(xozLength*Math.sin(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float y3=(float)(scale*UNIT_SIZE*Math.sin(Math.toRadians(vAngle-ANGLE_SPAN)));
        		
        		xozLength=scale*UNIT_SIZE*Math.cos(Math.toRadians(vAngle));
        		float x4=(float)(xozLength*Math.cos(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float z4=(float)(xozLength*Math.sin(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float y4=(float)(scale*UNIT_SIZE*Math.sin(Math.toRadians(vAngle)));   
        		
        		//建構第一三角形
        		alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);        		
        		//建構第二三角形
        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x3);alVertix.add(y3);alVertix.add(z3); 
        		
        		//第一三角形3個頂點的6個紋理座標
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);        		
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		//第二三角形3個頂點的6個紋理座標
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);        		
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);       		
        	}
        } 	
        
        vCount=alVertix.size()/3;//頂點的數量為座標值數量的1/3，因為一個頂點有3個座標
    	
        //將alVertix中的座標值轉存到一個float陣列中
        float vertices[]=new float[vCount*3];
    	for(int i=0;i<alVertix.size();i++)
    	{
    		vertices[i]=alVertix.get(i);
    	}
        
        //建立繪制頂點資料緩沖
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為float型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置     
        
        //建立繪制頂點法向量緩沖
        ByteBuffer nbb = ByteBuffer.allocateDirect(vertices.length*4);
        nbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mNormalBuffer = nbb.asFloatBuffer();//轉為float型緩沖
        mNormalBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mNormalBuffer.position(0);//設定緩沖區起始位置     
       
                
        //建立紋理座標緩沖
        float textureCoors[]=new float[alTexture.size()];//頂點紋理值陣列
        for(int i=0;i<alTexture.size();i++) 
        {
        	textureCoors[i]=alTexture.get(i);
        }
        
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
        mProgram = ShaderManager.getShadowShader();
        //取得程式中頂點位置屬性參考id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //取得程式中頂點紋理座標屬性參考id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //取得程式中總變換矩陣參考id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //取得位置、旋轉變換矩陣參考id
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");  
        //取得程式中頂點法向量屬性參考id  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //取得程式中光源位置參考id
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //取得程式中攝影機位置參考id
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera"); 
        //取得程式中是否繪制陰影屬性參考id
        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow"); 
        muIsShadowFrag=GLES20.glGetUniformLocation(mProgram, "isShadowFrag"); 
        //取得程式中攝影機矩陣參考id
        muCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMCameraMatrix"); 
        //取得程式中投影矩陣參考id
        muProjMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjMatrix");  
        //取得桌球紋理屬性參考id 
        muBallTexHandle=GLES20.glGetUniformLocation(mProgram, "sTextureBall"); 
        //用於繪制陰影的桌面紋理屬性參考id 
        muTableTexHandle=GLES20.glGetUniformLocation(mProgram, "sTextureTable"); 
    }
    
    public void drawSelf(int ballTexId,int tableTexId,int isShadow)//0-no shadow 1-with shadow
    { 
    	 //制定使用某套shader程式
    	 GLES20.glUseProgram(mProgram); 
         //將最終變換矩陣傳入shader程式
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //將位置、旋轉變換矩陣傳入shader程式
         GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);  
         //將光源位置傳入shader程式   
         GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
         //將攝影機位置傳入shader程式   
         GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
         //將是否繪制陰影屬性傳入shader程式 
         GLES20.glUniform1i(muIsShadow, isShadow);
         GLES20.glUniform1i(muIsShadowFrag, isShadow);         
         //將攝影機矩陣傳入shader程式
         GLES20.glUniformMatrix4fv(muCameraMatrixHandle, 1, false, MatrixState.getCaMatrix(), 0); 
         //將投影矩陣傳入shader程式
         GLES20.glUniformMatrix4fv(muProjMatrixHandle, 1, false, MatrixState.getProjMatrix(), 0); 
         
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
         
         //容許頂點位置、紋理座標、法向量資料陣列
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
         GLES20.glEnableVertexAttribArray(maNormalHandle);  
         
         //綁定紋理
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ballTexId);    
         GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tableTexId);            
         GLES20.glUniform1i(muBallTexHandle, 0);
         GLES20.glUniform1i(muTableTexHandle, 1);  
         //繪制紋理矩形
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);          
         
    }
    //自動切分紋理產生紋理陣列的方法
    public float[] generateTexCoor(int bw,int bh)
    {
    	float[] result=new float[bw*bh*6*2]; 
    	float sizew=1.0f/bw;//列數
    	float sizeh=1.0f/bh;//行數
    	int c=0;
    	for(int i=0;i<bh;i++)
    	{
    		for(int j=0;j<bw;j++)
    		{
    			//每行列一個矩形，由兩個三角形構成，共六個點，12個紋理座標
    			float s=j*sizew;
    			float t=i*sizeh;
    			
    			result[c++]=s;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t+sizeh;    			
    		}
    	}
    	return result;
    }
    
    public void angle()
    {
    	MatrixState.rotate(xOffset, 1, 0, 0);//繞X軸轉動
    	MatrixState.rotate(zOffset, 0, 0, 1);
    }
    
}

