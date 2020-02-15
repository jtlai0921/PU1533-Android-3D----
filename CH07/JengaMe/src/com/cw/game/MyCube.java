package com.cw.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.cw.util.MatrixState;
import com.cw.util.SYSUtil;

import android.opengl.GLES20;

public class MyCube {
	
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
	 
	float yAngle=0;
	boolean onTopLayer=false;
	float halfSize;//立方體的半邊長
	RigidBody body;//對應的剛體物件
	MySurfaceView mv;
	AABB3 preBox;//仿射變換之前的包圍盒
    Point2PointConstraint p2p;  
     boolean isPicked=false;
    int vCount; 
    float[] m = new float[16];//仿射變換的矩陣  
    private  int specular=1;
    CollisionShape colShape;
    float wx,wy,wz;
    int layer=0;
    int index=-1;
    float mMass=0;
    boolean checked=false;
    public static int countD=0;
    int upflag=0;
    boolean touchable=true;
    static boolean turnnable=false;
    DiscreteDynamicsWorld dynamicsWorld;
    
    Transform trans;
	  float angleY=90;
	  float delateX=0;
	  float delateY=0;
	  float delateZ=0;
	public  static int flag=0;
	boolean upthere=false;

	public MyCube(MySurfaceView mv,float halfSize, CollisionShape colShape,
			DiscreteDynamicsWorld dynamicsWorld,float mass,float cx,float cy,float cz,float yAngle,int mProgram,int layer)
	{
		wx=cx;
		wy=cy;
		wz=cz;
		mMass=mass;
		  if(isPicked)
	        {
	        	specular=2;
	        }else
	        {
	        	specular=1;
	        }
		  this.dynamicsWorld=dynamicsWorld;
		initVertexData(halfSize);
		this.yAngle=yAngle;
		boolean isDynamic = (mMass != 0f);//物體是否可以運動
		Vector3f localInertia = new Vector3f(0, 0, 0);//慣性向量
		if(isDynamic) //若果物體可以運動
		{
			colShape.calculateLocalInertia(mMass, localInertia);//計算慣性
		}
		
		Transform startTransform = new Transform();//建立剛體的起始變換物件
	 
		startTransform.setIdentity();//變換起始化
		startTransform.origin.set(new Vector3f(cx, cy, cz));//設定起始的位置
		//建立剛體的運動狀態物件
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
		//建立剛體訊息物件
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo
									(mMass, myMotionState, colShape, localInertia);
		body = new RigidBody(rbInfo);//建立剛體
		body.setRestitution(0.00f);//設定反彈系數
		body.setFriction(0.3f);//設定摩擦系數
		dynamicsWorld.addRigidBody(body);//將剛體加入進實體世界
		this.mv=mv;	//儲存MySurfaceView參考
		this.mProgram=mProgram;//儲存著色器程式參考
		this.halfSize=halfSize;	//儲存半長
	}
	public void initVertexData(final float UNIT_SIZE){
    	//頂點座標資料的起始化================begin============================
        vCount=36;
        float vertices[]=new float[]
        {
        		3*UNIT_SIZE,UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	
            	3*UNIT_SIZE,UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,//ding面
            	
            	3*UNIT_SIZE,-UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,-UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,-UNIT_SIZE/2,-UNIT_SIZE,
            	
            	3*UNIT_SIZE,-UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,-UNIT_SIZE/2,-UNIT_SIZE,
            	3*UNIT_SIZE,-UNIT_SIZE/2,-UNIT_SIZE,//di面
            	
            	
            	3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,-1*UNIT_SIZE/2,UNIT_SIZE,
            	
            	
            	3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,-1*UNIT_SIZE/2,UNIT_SIZE,
            	3*UNIT_SIZE,-UNIT_SIZE/2,UNIT_SIZE,//前面
            	
            	
            	-3*UNIT_SIZE,UNIT_SIZE/2,-UNIT_SIZE,
                3*UNIT_SIZE,UNIT_SIZE/2,-UNIT_SIZE,
                3*UNIT_SIZE,-UNIT_SIZE/2,-UNIT_SIZE,
                	
            	-3*UNIT_SIZE,UNIT_SIZE/2,-UNIT_SIZE,
            	3*UNIT_SIZE,-UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,-UNIT_SIZE/2,-UNIT_SIZE,//hou面
            	
            	
            	-3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,1*UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,-1*UNIT_SIZE/2,UNIT_SIZE,
            	
            	-3*UNIT_SIZE,1*UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,-1*UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,-1*UNIT_SIZE/2,UNIT_SIZE,//左面
            	
            
            	3*UNIT_SIZE,1*UNIT_SIZE/2,-UNIT_SIZE,
            	3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	3*UNIT_SIZE,-1*UNIT_SIZE/2,UNIT_SIZE,
            	
            	3*UNIT_SIZE,1*UNIT_SIZE/2,-UNIT_SIZE,
            	3*UNIT_SIZE,-1*UNIT_SIZE/2,UNIT_SIZE,
            	3*UNIT_SIZE,-1*UNIT_SIZE/2,-UNIT_SIZE,//右面
        };
    	preBox = new AABB3(vertices);
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mVertexBuffer = vbb.asFloatBuffer();//轉為int型緩沖
        mVertexBuffer.put(vertices);//向緩沖區中放入頂點座標資料
        mVertexBuffer.position(0);//設定緩沖區起始位置
        float textures[]=new float[]
        {
        		1,0,2/5.0f, 0, 2/5.0f,1/3.0f,
        		1,0, 2/5.0f,1/3.0f, 1,1/3.0f,
            	
        		1,0,2/5.0f, 0, 2/5.0f,1/3.0f,
        		1,0,2/5.0f,1/3.0f, 1,1/3.0f, 
            	
            	1,1/3.0f, 2/5.0f,1/3.0f, 2/5.0f,1,
            	1,1/3.0f, 2/5.0f,1, 1,1,
            	
            	1,1/3.0f, 2/5.0f,1/3.0f, 2/5.0f,1,
            	1,1/3.0f, 2/5.0f,1, 1,1,
            	
            	2/5.0f,0,0,0,2/5.0f,1/3.0f,
            	2/5.0f,1/3.0f,0,1/3.0f,0,0,
            	
            	2/5.0f,1/3.0f,2/5.0f,0,0,0,
            	2/5.0f,1/3.0f,0,0,0,1/3.0f,
        };
        //建立頂點紋理資料緩沖
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//設定位元組順序
        mTextureBuffer= tbb.asFloatBuffer();//轉為Float型緩沖
        mTextureBuffer.put(textures);//向緩沖區中放入頂點著色資料
        mTextureBuffer.position(0);//設定緩沖區起始位置
	
        //起始化法向量陣列
        float mNormals[]=new float[]
                {
                		0,1,0,0,1,0,0,1,0,
                		0,1,0,0,1,0,0,1,0,
                		
                		
                		0,-1,0,0,-1,0,0,-1,0,
                		0,-1,0,0,-1,0,0,-1,0,
                		
                		
        				0,0,1,0,0,1,0,0,1,
                		0,0,1,0,0,1,0,0,1,
                		
                		0,0,-1,0,0,-1,0,0,-1,
                		0,0,-1,0,0,-1,0,0,-1,
                		
                		1,0,0,1,0,0,1,0,0,
                		1,0,0,1,0,0,1,0,0,
                		
                		-1,0,0,-1,0,0,-1,0,0,
                		-1,0,0,-1,0,0,-1,0,0,
                };
                //建立頂點紋理資料緩沖
                ByteBuffer mNbb = ByteBuffer.allocateDirect(mNormals.length*4);
                mNbb.order(ByteOrder.nativeOrder());//設定位元組順序
                mNormalBuffer= mNbb.asFloatBuffer();//轉為Float型緩沖
                mNormalBuffer.put(mNormals);//向緩沖區中放入頂點著色資料
                mNormalBuffer.position(0);
	}
	int spencularlight;
	  public void initShader(MySurfaceView mv,int mProgram)
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
	 int effectcount=0;
	public void drawSelf(MySurfaceView mv,int[] texIda,int isShadow)
	{
		initShader(mv, mProgram);//紋理矩形起始化著色器
		int texId;
		if(isPicked)
		{
			texId=texIda[0];//物體運動時的紋理id
		}else
		{
		 texId=texIda[1];
		}
		
		if(!body.isActive()){texId=texIda[1];}//物體靜止時的紋理id
		MatrixState.pushMatrix();
		trans = body.getMotionState().getWorldTransform(new Transform());
		Quat4f ro=trans.getRotation(new Quat4f());
		if(trans.origin.y-this.wy<-0.3)//若果木塊y方向偏移超過0.2 那麼就計數器加1
		{
			countD++;
		}

		if(upthere&&!mv.isMoveFlag||!turnnable&&MySurfaceView.checkedIndex==this.index&&(this.trans.origin.x>3||this.trans.origin.x<-3||this.trans.origin.z>3||this.trans.origin.z<-3))
		{
			this.body.destroy();
			if(this.p2p!=null)
			{
				this.removePickedConstraint();
			}
			countD=0;
			upflag=1;
			MyCube mc=mv.cubegroup.get(MySurfaceView.index3);//獲得最頂端的最後一個被置換的木塊
			if(mc.yAngle==90)
			{
				this.yAngle=0;
			}
			else
			{
				this.yAngle=90;
			}
			delateY=(MySurfaceView.maxLayer+4)/2;
			switch(flag%3)
			{
			case 0:
				
				trans.origin.set(0,delateY,0);
				mv.tpx=0;
				mv.tpy=delateY;
				mv.tpz=0;
				break;
			case 1: 
				if(mc.yAngle==90)
				{
					trans.origin.set(0,delateY,-1);
					mv.tpx=0;
					mv.tpy=delateY;
					mv.tpz=-1;
				}else
				{
					trans.origin.set(-1,delateY,0);
					mv.tpx=-1;
					mv.tpy=delateY;
					mv.tpz=0;
				}
				break;
			case 2: 
				if(mc.yAngle==0)
				{
					trans.origin.set(1,delateY,0);
					mv.tpx=1;
					mv.tpy=delateY;
					mv.tpz=0;
				}else
				{
					trans.origin.set(0,delateY,1);
					mv.tpx=0;
					mv.tpy=delateY;	
					mv.tpz=1;
				}
			
				break;
			}
			if(MySurfaceView.changeSelectState)//當物體被拉至上方時 相機的動作 
			{
        		    mv.cx= Constant.TARGET_X+mv.Radius*(float)Math.sin(-mv.xAngle);//,   //人眼位置的XEYE_X
					mv.cy= delateY;//,//人眼位置的YRadius*(float)Math.cos(xAngle)/3
					mv.cz=  Constant.TARGET_Z+mv.Radius*(float)Math.cos(-mv.xAngle);//,  //人眼位置的z
					mv.tx= Constant.TARGET_X;//, 	//人眼光看的點X
				    mv.ty= delateY;//, //人眼光看的點Y
					mv.tz= Constant.TARGET_Z;//,//人眼光看的點Z
					mv.dx=0;
       			    mv.dy=1;
       			    mv.dz=0;
			}
			MySurfaceView.changeSelectState=false;//設定勾選狀態不可變
		  //MatrixState.translate(trans.origin.x,trans.origin.y,trans.origin.z);
		}
		if(turnnable&&this.isPicked)//當游戲失敗時解除關節束縛
		{
			this.isPicked=false;
			this.removePickedConstraint();
		}
		//下面的一行程式碼不僅是物體受引擎影響的變換 還有手動控制的變換 
		if((ro.x!=0||ro.y!=0||ro.z!=0)&&turnnable)
		{
			float[] fa=SYSUtil.fromSYStoAXYZ(ro);
			if(!Float.isInfinite(fa[0])&&!Float.isInfinite(fa[1])&&!Float.isInfinite(fa[2])&&
					!Float.isNaN(fa[0])&&!Float.isNaN(fa[1])&&!Float.isNaN(fa[2])){
				MatrixState.rotate(fa[0],fa[1],fa[2],fa[3]);
			}
    	}
		MatrixState.translate(trans.origin.x,trans.origin.y,trans.origin.z);
		if(upflag==1)
		{
			upthere=true;
		}
		MatrixState.rotate(yAngle, 0, 1, 0);
		if(this.trans.origin.y>=(MySurfaceView.maxLayer-2.5)/2-0.2&&this.trans.origin.y<=(MySurfaceView.maxLayer)/2+0.2)
		{
			//若果升至頂端的物體的位置到達指定位置
			this.isPicked=false;
		}
		if(this.trans.origin.y>=(MySurfaceView.maxLayer)/2-0.2&&this.trans.origin.y<=(MySurfaceView.maxLayer)/2+0.2)
		{
			effectcount++;
			MySurfaceView.changeSelectState=true;
			this.isPicked=false;
			this.upflag=0;
			if(mv.playmusic&&this.trans.origin.y>=(MySurfaceView.maxLayer)/2+0.1)
			{
				mv.activity.soundutil.playEffectsSound(1, 1);
			}
		}
		if(this.trans.origin.y>=(MySurfaceView.maxLayer)/2-0.2&&this.trans.origin.y<=(MySurfaceView.maxLayer+4)/2-0.0001)
		{
			if(this.p2p!=null)
			{
				this.removePickedConstraint();
			}
			this.touchable=false;
			MySurfaceView.changeSelectState=true;
		}

		copyM();
   	 	GLES20.glUseProgram(mProgram);
   	    GLES20.glUniform1f(spencularlight,specular);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
        GLES20.glUniform1i(muIsShadow, isShadow);
        GLES20.glUniformMatrix4fv(muProjCameraMatrixHandle, 1, false, MatrixState.getViewProjMatrix(), 0);   
        GLES20.glEnableVertexAttribArray(maPositionHandle);  
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
        GLES20.glEnableVertexAttribArray(maNormalHandle);  
        GLES20.glUniform1i(uTexHandle, 0);
        
        GLES20.glVertexAttribPointer
        (
        		maPositionHandle,
        		3,
        		GLES20.GL_FLOAT, 
        		false,
                3*4,
                mVertexBuffer
        ); 
        GLES20.glVertexAttribPointer  
        (  
       		    maTexCoorHandle,  
        		2, 
        		GLES20.GL_FLOAT, 
        		false,
               2*4,
               mTextureBuffer
        );
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLES20.glVertexAttribPointer  
        (
        		maNormalHandle, 
        		3,   
        		GLES20.GL_FLOAT, 
        		false,
               3*4,   
               mNormalBuffer
        );
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
        MatrixState.popMatrix();//還原現場
	}
	 public AABB3 getCurrBox(){
	    	return preBox.setToTransformedBox(m);//取得變換後的包圍盒
	  }
	  public void copyM(){
	    	for(int i=0;i<16;i++){
	    		m[i]=MatrixState.getMMatrix()[i];
	    	}}
	  public void addPickedConstraint(){
	    p2p = new Point2PointConstraint(body, new Vector3f(0,0,0));
	   	mv.dynamicsWorld.addConstraint(p2p, true);
	    }
	    public void removePickedConstraint(){
	    	if(p2p!=null){
	    		mv.dynamicsWorld.removeConstraint(p2p);
	    	}
	    }
}
