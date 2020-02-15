package tstc.fxq.parts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;
import static tstc.fxq.constants.Constant.*;
public class ZhuoZiDiTable {  
	int mProgram;//�ۭq�ۦ�ް������id 
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id  
    int muMMatrixHandle;//��m�B�����ܴ��x�}
    int maCameraHandle; //��v����m�ݩʰѦ�id  
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maNormalHandle; //���I�k�V�q�ݩʰѦ�id  
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id  
    int maSunLightLocationHandle;//������m�ݩʰѦ�id  
    
    private FloatBuffer mVertexBuffer;//���I�y�и�ƽw�R
	private FloatBuffer mNormalBuffer;//���I�k�V�q��ƽw�R
	private FloatBuffer mTexCoorBuffer;  //���I���z��ƽw�R
	public float mOffsetX;
	public float mOffsetY;
	float scale;
	float l=EDGE;
	float k=EDGE_BIG;
	int vCount;
	public ZhuoZiDiTable(MySurfaceView mv, float scale,float length,float width)
	{
		this.scale=scale;
		vCount=84;
		float[] verteices=
		{
				//�W��
				-(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				-(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				
				(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				-(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				
				-(TABLE_UNIT_SIZE*length),TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				-(TABLE_UNIT_SIZE*length),TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				-(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				
				-(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				-(TABLE_UNIT_SIZE*length),TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				-(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				
				(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				
				TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				
				//�U��
				-(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				-(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				
				(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				-(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				
				-(TABLE_UNIT_SIZE*length),-TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),//1
				-(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,//4
				-(TABLE_UNIT_SIZE*length),-TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),//2
				
				-(TABLE_UNIT_SIZE*length),-TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),//2
				-(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,//4
				-(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,//3
				
				(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,//1
				TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),//4
				(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,//2

				(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,//2
				TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),//4
				TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),//3
				
				//�e��
				-(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				-(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				
				(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				-(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				
				//�᭱
				-(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				-(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				
				(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				-(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				
				//����
				-TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				-TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				-TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				
				-TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				-TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				-TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				
				//�k��
				TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				
				TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				
				//���W����
				-(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				-(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				-TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				
				-TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				-(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				-TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				
				//���U����
				-TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				-TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				-(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				
				-(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				-TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				-(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				
				//�k�U����
				(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				
				TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,TABLE_UNIT_SIZE*width,
				TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,(TABLE_UNIT_SIZE*width-(l-k)),
				
				//�k�W����
				TABLE_UNIT_SIZE*length,TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				
				(TABLE_UNIT_SIZE*length-(l-k)),TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width,
				TABLE_UNIT_SIZE*length,-TABLE_UNIT_HIGHT*scale,-(TABLE_UNIT_SIZE*width-(l-k)),
				(TABLE_UNIT_SIZE*length-(l-k)),-TABLE_UNIT_HIGHT*scale,-TABLE_UNIT_SIZE*width
		};		
		ByteBuffer vbb=ByteBuffer.allocateDirect(verteices.length*4);//�إ߳��I�y�и�ƽw�R
		vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
		mVertexBuffer=vbb.asFloatBuffer();//�ରfloat���w�R
		mVertexBuffer.put(verteices);//�V�w�R�Ϥ���J���I�y�и��
		mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
		//���I�k�V�q���
        float normals[]=new float[verteices.length];
        for(int i=0; i<verteices.length; i+=3){
        	normals[i] = 0;
        	normals[i+1] = -1;
        	normals[i+2] = 0;
        }
        //�إ�ø��I�k�V�q�w�R
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mNormalBuffer = nbb.asFloatBuffer();//�ରfloat���w�R
        mNormalBuffer.put(normals);//�V�w�R�Ϥ���J���I�y�и��
        mNormalBuffer.position(0);//�]�w�w�R�ϰ_�l��m  
        
        //���z�y�и��
		float[] textureCoors=new float[vCount*2];//�إ߯��z�}�C
		for(int i=0;i<vCount/6;i++)
		{
			textureCoors[i*12]=0;
			textureCoors[(i*12)+1]=0;
			
			textureCoors[(i*12)+2]=0;
			textureCoors[(i*12)+3]=1;
			
			textureCoors[(i*12)+4]=1;
			textureCoors[(i*12)+5]=0;
			
			textureCoors[(i*12)+6]=1;
			textureCoors[(i*12)+7]=0;
			
			textureCoors[(i*12)+8]=0;
			textureCoors[(i*12)+9]=1;
			
			textureCoors[(i*12)+10]=1;
			textureCoors[(i*12)+11]=1;		

		}				
		ByteBuffer tbb=ByteBuffer.allocateDirect(textureCoors.length*4);//�إ߳��I�y�и�ƽw�R
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
        //���o�{�������I�g�n���ݩʰѦ�id   
        maTexCoorHandle=GLES20.glGetAttribLocation(mProgram, "aTexCoor");  
        //���o�{�������I�k�V�q�ݩʰѦ�id  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //���o�{�����`�ܴ��x�}�Ѧ�id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");   
        //���o�{������v����m�Ѧ�id
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera"); 
        //���o�{����������m�Ѧ�id
        maSunLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocationSun"); 
        //���o��m�B�����ܴ��x�}�Ѧ�id
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");  
    }
    public void drawSelf(int texId)
    {
		MatrixState.rotate(mOffsetX, 1, 0, 0);
		MatrixState.rotate(mOffsetY, 0, 1, 0);
		
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
}
