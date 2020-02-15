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
	
	int mProgram;//�ۭq�ۦ�޽u�ۦ⾹�{��id  
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�
    int muMMatrixHandle;//��m�B�����ܴ��x�}
    int maPositionHandle; //���I��m�ݩʰѦ�  
    int maNormalHandle; //���I�k�V�q�ݩʰѦ�  
    int maLightLocationHandle;//������m�ݩʰѦ�  
    int maCameraHandle; //��v����m�ݩʰѦ� 
    int muIsShadow;//�O�_ø��v�ݩʰѦ�
    int muProjCameraMatrixHandle;//��v�B��v���s�զX�x�}�Ѧ�
    String mVertexShader;//���I�ۦ⾹�{���X���O�Z    	 
    String mFragmentShader;//�����ۦ⾹�{���X���O�Z    
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R  
	FloatBuffer   mNormalBuffer;//���I�k�V�q��ƽw�R
	private FloatBuffer   mTextureBuffer;//���I�ۦ��ƽw�R
	int uTexHandle;//�~�[���z�ݩʰѦ�id
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id  
	 
	float yAngle=0;
	boolean onTopLayer=false;
	float halfSize;//�ߤ��骺�b���
	RigidBody body;//���������骫��
	MySurfaceView mv;
	AABB3 preBox;//��g�ܴ����e���]��
    Point2PointConstraint p2p;  
     boolean isPicked=false;
    int vCount; 
    float[] m = new float[16];//��g�ܴ����x�}  
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
		boolean isDynamic = (mMass != 0f);//����O�_�i�H�B��
		Vector3f localInertia = new Vector3f(0, 0, 0);//�D�ʦV�q
		if(isDynamic) //�Y�G����i�H�B��
		{
			colShape.calculateLocalInertia(mMass, localInertia);//�p��D��
		}
		
		Transform startTransform = new Transform();//�إ߭��骺�_�l�ܴ�����
	 
		startTransform.setIdentity();//�ܴ��_�l��
		startTransform.origin.set(new Vector3f(cx, cy, cz));//�]�w�_�l����m
		//�إ߭��骺�B�ʪ��A����
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
		//�إ߭���T������
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo
									(mMass, myMotionState, colShape, localInertia);
		body = new RigidBody(rbInfo);//�إ߭���
		body.setRestitution(0.00f);//�]�w�ϼu�t��
		body.setFriction(0.3f);//�]�w�����t��
		dynamicsWorld.addRigidBody(body);//�N����[�J�i����@��
		this.mv=mv;	//�x�sMySurfaceView�Ѧ�
		this.mProgram=mProgram;//�x�s�ۦ⾹�{���Ѧ�
		this.halfSize=halfSize;	//�x�s�b��
	}
	public void initVertexData(final float UNIT_SIZE){
    	//���I�y�и�ƪ��_�l��================begin============================
        vCount=36;
        float vertices[]=new float[]
        {
        		3*UNIT_SIZE,UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	
            	3*UNIT_SIZE,UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,//ding��
            	
            	3*UNIT_SIZE,-UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,-UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,-UNIT_SIZE/2,-UNIT_SIZE,
            	
            	3*UNIT_SIZE,-UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,-UNIT_SIZE/2,-UNIT_SIZE,
            	3*UNIT_SIZE,-UNIT_SIZE/2,-UNIT_SIZE,//di��
            	
            	
            	3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,-1*UNIT_SIZE/2,UNIT_SIZE,
            	
            	
            	3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,-1*UNIT_SIZE/2,UNIT_SIZE,
            	3*UNIT_SIZE,-UNIT_SIZE/2,UNIT_SIZE,//�e��
            	
            	
            	-3*UNIT_SIZE,UNIT_SIZE/2,-UNIT_SIZE,
                3*UNIT_SIZE,UNIT_SIZE/2,-UNIT_SIZE,
                3*UNIT_SIZE,-UNIT_SIZE/2,-UNIT_SIZE,
                	
            	-3*UNIT_SIZE,UNIT_SIZE/2,-UNIT_SIZE,
            	3*UNIT_SIZE,-UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,-UNIT_SIZE/2,-UNIT_SIZE,//hou��
            	
            	
            	-3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	-3*UNIT_SIZE,1*UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,-1*UNIT_SIZE/2,UNIT_SIZE,
            	
            	-3*UNIT_SIZE,1*UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,-1*UNIT_SIZE/2,-UNIT_SIZE,
            	-3*UNIT_SIZE,-1*UNIT_SIZE/2,UNIT_SIZE,//����
            	
            
            	3*UNIT_SIZE,1*UNIT_SIZE/2,-UNIT_SIZE,
            	3*UNIT_SIZE,UNIT_SIZE/2,UNIT_SIZE,
            	3*UNIT_SIZE,-1*UNIT_SIZE/2,UNIT_SIZE,
            	
            	3*UNIT_SIZE,1*UNIT_SIZE/2,-UNIT_SIZE,
            	3*UNIT_SIZE,-1*UNIT_SIZE/2,UNIT_SIZE,
            	3*UNIT_SIZE,-1*UNIT_SIZE/2,-UNIT_SIZE,//�k��
        };
    	preBox = new AABB3(vertices);
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରint���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
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
        //�إ߳��I���z��ƽw�R
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTextureBuffer= tbb.asFloatBuffer();//�ରFloat���w�R
        mTextureBuffer.put(textures);//�V�w�R�Ϥ���J���I�ۦ���
        mTextureBuffer.position(0);//�]�w�w�R�ϰ_�l��m
	
        //�_�l�ƪk�V�q�}�C
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
                //�إ߳��I���z��ƽw�R
                ByteBuffer mNbb = ByteBuffer.allocateDirect(mNormals.length*4);
                mNbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
                mNormalBuffer= mNbb.asFloatBuffer();//�ରFloat���w�R
                mNormalBuffer.put(mNormals);//�V�w�R�Ϥ���J���I�ۦ���
                mNormalBuffer.position(0);
	}
	int spencularlight;
	  public void initShader(MySurfaceView mv,int mProgram)
	    {
	    	this.mProgram=mProgram;
	        maTexCoorHandle=GLES20.glGetAttribLocation(mProgram, "aTexCoor");  
	        uTexHandle=GLES20.glGetUniformLocation(mProgram, "sTexture");
	        //���o�{�������I��m�ݩʰѦ�  
	        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
	        //���o�{�������I�m���ݩʰѦ�  
	        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
	        //���o�{�����`�ܴ��x�}�Ѧ�
	        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
	        //���o��m�B�����ܴ��x�}�Ѧ�
	        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix"); 
	        //���o�{����������m�Ѧ�
	        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
	        //���o�{������v����m�Ѧ�
	        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera");  
	        //���o�{�����O�_ø��v�ݩʰѦ�
	        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow"); 
	        //���o�{������v�B��v���s�զX�x�}�Ѧ�
	        muProjCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjCameraMatrix"); 
	    
	    }
	 int effectcount=0;
	public void drawSelf(MySurfaceView mv,int[] texIda,int isShadow)
	{
		initShader(mv, mProgram);//���z�x�ΰ_�l�Ƶۦ⾹
		int texId;
		if(isPicked)
		{
			texId=texIda[0];//����B�ʮɪ����zid
		}else
		{
		 texId=texIda[1];
		}
		
		if(!body.isActive()){texId=texIda[1];}//�����R��ɪ����zid
		MatrixState.pushMatrix();
		trans = body.getMotionState().getWorldTransform(new Transform());
		Quat4f ro=trans.getRotation(new Quat4f());
		if(trans.origin.y-this.wy<-0.3)//�Y�G���y��V�����W�L0.2 ����N�p�ƾ��[1
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
			MyCube mc=mv.cubegroup.get(MySurfaceView.index3);//��o�̳��ݪ��̫�@�ӳQ�m�������
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
			if(MySurfaceView.changeSelectState)//����Q�ԦܤW��� �۾����ʧ@ 
			{
        		    mv.cx= Constant.TARGET_X+mv.Radius*(float)Math.sin(-mv.xAngle);//,   //�H����m��XEYE_X
					mv.cy= delateY;//,//�H����m��YRadius*(float)Math.cos(xAngle)/3
					mv.cz=  Constant.TARGET_Z+mv.Radius*(float)Math.cos(-mv.xAngle);//,  //�H����m��z
					mv.tx= Constant.TARGET_X;//, 	//�H�����ݪ��IX
				    mv.ty= delateY;//, //�H�����ݪ��IY
					mv.tz= Constant.TARGET_Z;//,//�H�����ݪ��IZ
					mv.dx=0;
       			    mv.dy=1;
       			    mv.dz=0;
			}
			MySurfaceView.changeSelectState=false;//�]�w�Ŀ窱�A���i��
		  //MatrixState.translate(trans.origin.x,trans.origin.y,trans.origin.z);
		}
		if(turnnable&&this.isPicked)//��������ѮɸѰ����`����
		{
			this.isPicked=false;
			this.removePickedConstraint();
		}
		//�U�����@��{���X���ȬO����������v�T���ܴ� �٦���ʱ���ܴ� 
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
			//�Y�G�ɦܳ��ݪ����骺��m��F���w��m
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
        MatrixState.popMatrix();//�٭�{��
	}
	 public AABB3 getCurrBox(){
	    	return preBox.setToTransformedBox(m);//���o�ܴ��᪺�]��
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
