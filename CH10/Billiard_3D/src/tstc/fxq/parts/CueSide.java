package tstc.fxq.parts;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import tstc.fxq.utils.Vector3Util;
import android.opengl.GLES20;
public class CueSide{  
	int mProgram;//�ۭq�ۦ�ް������id 
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id
    int muMMatrixHandle;//��m�B�����ܴ��x�}
    int maCameraHandle;//��v������m�Ѧ�id
    
    int maSunLightLocationHandle;//������m�ݩʰѦ�id 
    
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id
    int maNormalHandle;//���I�k�V�q�Ѧ�id
    
	public FloatBuffer mVertexBuffer;
	public FloatBuffer mTexCoorBuffer;
	public FloatBuffer mNormalBuffer;//�w�I�k�V�q�w�R
	
	public int vCount=0;
	public static float angleZ=0;
	float yOffset;
	float vy;
	
	//�]���y�쬰�@�Y�j�@�Y�p������C�b�w�q�����O������OrBig���ȥ����j��rSmall���ȡA�_�h�k�V�q���ȷ|�o�Ϳ��~�C
	public CueSide(MySurfaceView mv, float rBig,float rSmall,float hight,float scale,float yOffset)
	{
		this.yOffset=yOffset;
		float FLOAT_SPAN=11.25f;
		float texture[]=generateTexCoor((int)(360/FLOAT_SPAN));
		int c=0;
		int tc=texture.length;
		List<Float>vertexList=new ArrayList<Float>();//���I�y��
		List<Float>textureList=new ArrayList<Float>();//���I���z
		List<Float>normalList=new ArrayList<Float>();//���I�k�V�q
		
		for(float angle=360;angle>0;angle-=FLOAT_SPAN)
		{
			//���I
			float x1=(float)(scale*rSmall*Math.cos(Math.toRadians(angle)));
			float y1=scale*hight;
			float z1=(float)(scale*rSmall*Math.sin(Math.toRadians(angle)));
			
			//���I
			float x2=(float)(scale*rBig*Math.cos(Math.toRadians(angle)));
			float y2=2;
			float z2=(float)(scale*rBig*Math.sin(Math.toRadians(angle)));
			
			//�k�V�q
			float[] aNormal1=Vector3Util.calBallArmNormal(rBig, rSmall, hight, scale, 
					x1,y1,z1,x2,y2,z2,
					angle);
			//�k�V�q
			float[] aNormal2=Vector3Util.calBallArmNormal(rBig, rSmall, hight, scale, 
					x1,y1,z1,x2,y2,z2,
					angle);
			
			//���I
			float x3=(float)(scale*rBig*Math.cos(Math.toRadians(angle-FLOAT_SPAN)));
			float y3=2;
			float z3=(float)(scale*rBig*Math.sin(Math.toRadians(angle-FLOAT_SPAN)));
			
			//���I
			float x4=(float)(scale*rSmall*Math.cos(Math.toRadians(angle-FLOAT_SPAN)));
			float y4=scale*hight;
			float z4=(float)(scale*rSmall*Math.sin(Math.toRadians(angle-FLOAT_SPAN)));
			
			
			//�k�V�q
			float[] aNormal3=Vector3Util.calBallArmNormal(rBig, rSmall, hight, scale, 
					x3,y3,z3,x4,y4,z4,
					angle);
			
			//�k�V�q
			float[] aNormal4=Vector3Util.calBallArmNormal(rBig, rSmall, hight, scale, 
					x3,y3,z3,x4,y4,z4,
					angle);
			
			
			
			//���I
			vertexList.add(x1);vertexList.add(y1);vertexList.add(z1);
			vertexList.add(x2);vertexList.add(y2);vertexList.add(z2);
			vertexList.add(x4);vertexList.add(y4);vertexList.add(z4);
			
			vertexList.add(x4);vertexList.add(y4);vertexList.add(z4);
			vertexList.add(x2);vertexList.add(y2);vertexList.add(z2);
			vertexList.add(x3);vertexList.add(y3);vertexList.add(z3);
			
			//���I�k�V�q
			normalList.add(aNormal1[0]);normalList.add(aNormal1[1]);normalList.add(aNormal1[2]);
			normalList.add(aNormal2[0]);normalList.add(aNormal2[1]);normalList.add(aNormal2[2]);
			normalList.add(aNormal4[0]);normalList.add(aNormal4[1]);normalList.add(aNormal4[2]);
			
			normalList.add(aNormal4[0]);normalList.add(aNormal4[1]);normalList.add(aNormal4[2]);
			normalList.add(aNormal2[0]);normalList.add(aNormal2[1]);normalList.add(aNormal2[2]);
			normalList.add(aNormal3[0]);normalList.add(aNormal3[1]);normalList.add(aNormal3[2]);
			
			
			//���z
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);
			textureList.add(texture[c++%tc]);		
		}
		//���I�y�нw�R
		vCount=vertexList.size()/3;
		float []vertice=new float[vertexList.size()];
		for(int i=0;i<vertexList.size();i++)
		{
			vertice[i]=vertexList.get(i);
		}
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer=vbb.asFloatBuffer();
		mVertexBuffer.put(vertice);
		mVertexBuffer.position(0);
		
		
		//���I���z�w�R
		float[]textures=new float[textureList.size()];
		for(int i=0;i<textureList.size();i++)
		{
			textures[i]=textureList.get(i);
		}
		ByteBuffer tbb=ByteBuffer.allocateDirect(textures.length*4);
		tbb.order(ByteOrder.nativeOrder());
		mTexCoorBuffer=tbb.asFloatBuffer();
		mTexCoorBuffer.put(textures);
		mTexCoorBuffer.position(0);
		
		
        //�إ�ø��I�k�V�q�w�R
		float []normals=new float[normalList.size()];
		for(int i=0;i<normalList.size();i++)
		{
			normals[i]=normalList.get(i);
		}
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mNormalBuffer = nbb.asFloatBuffer();//�ରfloat���w�R
        mNormalBuffer.put(normals);//�V�w�R�Ϥ���J���I�y�и��
        mNormalBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
        //�_�l��shader
        initShader(mv);
	}
    //�_�l��shader
    public void initShader(MySurfaceView mv)
    {
    	//���o�ۭq�ۦ�ް������id 
        mProgram = ShaderManager.getTexLightShader();
        
        //���o�{������v����m�Ѧ�id
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera"); 
        
        //���o�{�������I��m�ݩʰѦ�id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //���o�{�������I���z�ݩʰѦ�id   
        maTexCoorHandle=GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //���I�k�V�q
        maNormalHandle=GLES20.glGetAttribLocation(mProgram, "aNormal");
        
        //���o�{�����`�ܴ��x�}�Ѧ�id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
        //���o��m�B�����ܴ��x�}�Ѧ�id
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        //���o�{����������m�Ѧ�id
        maSunLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocationSun");
        
    }
    public void drawSelf(int texId)
    {
   	 	//��w�ϥάY�Mshader�{��
   	 	GLES20.glUseProgram(mProgram);
        //�N�̲��ܴ��x�}�ǤJshader�{��
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //�N��m�B�����ܴ��x�}�ǤJshader�{��
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        //�N��v����m�ǤJshader�{��   
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
        //�N������m�ǤJshader�{��   
        GLES20.glUniform3fv(maSunLightLocationHandle, 1, MatrixState.lightPositionFB);
        
        //���e�����w���I��m���    
        GLES20.glVertexAttribPointer        
        (
        		maPositionHandle,   
        		3, 
        		GLES20.GL_FLOAT, 
        		false,
        		3*4, 
        		mVertexBuffer   
        );
        //���e�����w���I�g�n�׸��
        GLES20.glVertexAttribPointer  
        (
        		maTexCoorHandle,  
        		2, 
        		GLES20.GL_FLOAT, 
        		false,
               	2*4,   
               	mTexCoorBuffer
        );   
        //���e�����w���I�k�V�q���
        GLES20.glVertexAttribPointer  
        (
        		maNormalHandle, 
        		3, 
        		GLES20.GL_FLOAT, 
        		false,
        		3*4,   
        		mNormalBuffer
        );            
        //�e�\���I��m��ư}�C
        GLES20.glEnableVertexAttribArray(maPositionHandle);  
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);
        GLES20.glEnableVertexAttribArray(maNormalHandle);
        
        //�j�w���z
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);         
        //ø��T����
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
	public float[] generateTexCoor(int share)
	{
		float[]result=new float[share*6*2];
		int c=0;
		float bs=1.0f/share;
		for(int i=0;i<share;i++)
		{
			float s=i*bs;
			result[c++]=s;
			result[c++]=0;
			
			result[c++]=s;
			result[c++]=1;
			
			result[c++]=s+bs;
			result[c++]=0;
			
			result[c++]=s+bs;
			result[c++]=0;
			
			result[c++]=s;
			result[c++]=1;
			
			result[c++]=s+bs;
			result[c++]=1;
			
		}
		return result;	
	}
}


