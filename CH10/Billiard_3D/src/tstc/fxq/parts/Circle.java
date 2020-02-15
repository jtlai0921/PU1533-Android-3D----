package tstc.fxq.parts;
import java.nio.ByteBuffer;
import static tstc.fxq.constants.Constant.*;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;
public class Circle {  
	int mProgram;//�ۭq�ۦ�ް������id 
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id 
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maNormalHandle; //���I�k�V�q�ݩʰѦ�id  
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id 
    
	FloatBuffer mVertexBuffer;
	private FloatBuffer mNormalBuffer;//���I�k�V�q��ƽw�R
	FloatBuffer mTexCoorBuffer;	
	int vCount;
	public Circle(MySurfaceView mv, float scaleX,float scaleZ)
	{	
		float SPAN=18;
		ArrayList<Float> alVertex=new ArrayList<Float>();
		for(int i=0;i<360/SPAN;i++)
		{
			float x1=0.0f;float y1=0.0f;float z1=0.0f;//�y�жb���I
			//�ھڳ�쨤�׭p��ĤG���I�y��
			float x2=(float)Math.cos(Math.toRadians(i*SPAN))*scaleX*TABLE_UNIT_SIZE;
			float y2=0.0f;
			float z2=(float)Math.sin(Math.toRadians(i*SPAN))*scaleZ*TABLE_UNIT_SIZE;
			//�p��ĤT���I�y��
			float x3=(float)Math.cos(Math.toRadians((i+1)*SPAN))*scaleX*TABLE_UNIT_SIZE;
			float y3=0.0f;
			float z3=(float)Math.sin(Math.toRadians((i+1)*SPAN))*scaleZ*TABLE_UNIT_SIZE;
			//���f�ɰw��V�\���I�A�s�զ��T����
			alVertex.add(x1);
			alVertex.add(y1);
			alVertex.add(z1);	
			alVertex.add(x3);
			alVertex.add(y3);
			alVertex.add(z3);		
			alVertex.add(x2);
			alVertex.add(y2);
			alVertex.add(z2);		
		}
		vCount=alVertex.size()/3;
		float[] verteices=new float[vCount*3];
		for(int i=0;i<vCount*3;i++)
		{
			verteices[i]=alVertex.get(i);
		}	
		ByteBuffer vbb=ByteBuffer.allocateDirect(verteices.length*4);//�N���I�y�Щ�J��ƽw�R
		vbb.order(ByteOrder.nativeOrder());	//�]�w�줸�ն���
		mVertexBuffer=vbb.asFloatBuffer();//�ରfloat���w�R
		mVertexBuffer.put(verteices);//�V�w�R�Ϥ���J���I�y�и��
		mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m 
		//���I�k�V�q���
        float normals[]=new float[verteices.length];
        for(int i=0; i<normals.length; i+=3){
        	normals[i] = 0;
        	normals[i+1] = 1;
        	normals[i+2] = 0;
        }
        //�إ�ø��I�k�V�q�w�R
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mNormalBuffer = nbb.asFloatBuffer();//�ରfloat���w�R
        mNormalBuffer.put(normals);//�V�w�R�Ϥ���J���I�y�и��
        mNormalBuffer.position(0);//�]�w�w�R�ϰ_�l��m  
        //���z���
		float[] textureCoors=new float[vCount*2];//���t���z�y��
		for(int i=0;i<vCount/3;i++)
		{
			textureCoors[i*6]=0;
			textureCoors[i*6+1]=0;
			
			textureCoors[i*6+2]=0;
			textureCoors[i*6+3]=1;
			
			textureCoors[i*6+4]=1;
			textureCoors[i*6+5]=0;
		}
		
		ByteBuffer tbb=ByteBuffer.allocateDirect(textureCoors.length*4);//�N���z�y�Юy�Щ�J��ƽw�R
		tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
		mTexCoorBuffer=tbb.asFloatBuffer();//�ରfloat���w�R
		mTexCoorBuffer.put(textureCoors);//�V�w�R�Ϥ���J���I�y�и��
		mTexCoorBuffer.position(0);//�]�w�w�R�ϰ_�l��m

        //�_�l��shader
        intShader(mv);
	}
    //�_�l��shader
    public void intShader(MySurfaceView mv)
    {
    	//���o�ۭq�ۦ�ް������id 
        mProgram = ShaderManager.getTexLightShader();
        //���o�{�������I��m�ݩʰѦ�id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition"); 
        //���o�{�������I�k�V�q�ݩʰѦ�id  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //���o�{�������I�g�n���ݩʰѦ�id   
        maTexCoorHandle=GLES20.glGetAttribLocation(mProgram, "aTexCoor"); 
        //���o�{�����`�ܴ��x�}�Ѧ�id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
    }
    public void drawSelf(int texId)
    {
   	 	//��w�ϥάY�Mshader�{��
   	 	GLES20.glUseProgram(mProgram);
        //�N�̲��ܴ��x�}�ǤJshader�{��
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        
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
        //���e�����w���I���z�y�и��
        GLES20.glVertexAttribPointer  
        (
        		maTexCoorHandle,  
        		2, 
        		GLES20.GL_FLOAT, 
        		false,
               	2*4,   
               	mTexCoorBuffer
        );
        //�e�\���I��m��ư}�C
        GLES20.glEnableVertexAttribArray(maPositionHandle);  
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);          
        //�j�w���z
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);         
        //ø��T����
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
}
