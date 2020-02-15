package tstc.fxq.parts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;
import static tstc.fxq.constants.Constant.*;
public class ZhuoMian {
	int mProgram;//�ۭq�ۦ�ް������id 
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id  
    int muMMatrixHandle;//��m�B�����ܴ��x�}
    int maCameraHandle; //��v����m�ݩʰѦ�id  
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maNormalHandle; //���I�k�V�q�ݩʰѦ�id  
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id  
    int maSunLightLocationHandle;//������m�ݩʰѦ�id 
    
	float small=EDGE_SMALL*0.8f;
	public float mOffsetX;
	public float mOffsetY;
	
	private FloatBuffer mVertexBuffer;//���I�y�и�ƽw�R
	private FloatBuffer mNormalBuffer;//���I�k�V�q��ƽw�R
	private FloatBuffer mTexCoorBuffer;  //���I���z��ƽw�R
    int vCount=0;
    
    public ZhuoMian(MySurfaceView mv, float width,float length)
    {
    	
    	//���I�y�и�ƪ��_�l��================begin============================
        vCount=18;
        float vertices[]=new float[]
        {
        	-(width*TABLE_UNIT_SIZE-small),0,-length*TABLE_UNIT_SIZE,
        	-(width*TABLE_UNIT_SIZE-small),0,length*TABLE_UNIT_SIZE,     
        	(width*TABLE_UNIT_SIZE-small),0,-length*TABLE_UNIT_SIZE,
        	
        	(width*TABLE_UNIT_SIZE-small),0,-length*TABLE_UNIT_SIZE,
        	-(width*TABLE_UNIT_SIZE-small),0,length*TABLE_UNIT_SIZE,     
        	(width*TABLE_UNIT_SIZE-small),0,length*TABLE_UNIT_SIZE,
        	
        	-width*TABLE_UNIT_SIZE,0,-(length*TABLE_UNIT_SIZE-small),
        	-width*TABLE_UNIT_SIZE,0,(length*TABLE_UNIT_SIZE-small),
        	-(width*TABLE_UNIT_SIZE-small),0,-length*TABLE_UNIT_SIZE,
        	
        	-(width*TABLE_UNIT_SIZE-small),0,-length*TABLE_UNIT_SIZE,
        	-width*TABLE_UNIT_SIZE,0,(length*TABLE_UNIT_SIZE-small),
        	-(width*TABLE_UNIT_SIZE-small),0,length*TABLE_UNIT_SIZE,
        	
        	(width*TABLE_UNIT_SIZE-small),0,-length*TABLE_UNIT_SIZE,
        	(width*TABLE_UNIT_SIZE-small),0,length*TABLE_UNIT_SIZE,
        	width*TABLE_UNIT_SIZE,0,-(length*TABLE_UNIT_SIZE-small),
        	
        	width*TABLE_UNIT_SIZE,0,-(length*TABLE_UNIT_SIZE-small),
        	(width*TABLE_UNIT_SIZE-small),0,length*TABLE_UNIT_SIZE,
        	width*TABLE_UNIT_SIZE,0,(length*TABLE_UNIT_SIZE-small)
        };
        for(int i=0; i<vertices.length; i+=3){
    		System.out.print(vertices[i]+", ");
    		System.out.print(vertices[i+1]+", ");
    		System.out.println(vertices[i+2]);
        }
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରFloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I�y�и�ƪ��_�l��================end============================
        
        //���I�k�V�q���
        float normals[]=new float[vertices.length];
        for(int i=0; i<vertices.length; i+=3){
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
        
        float texST[]=new float[]
        {
        	small/(2*width),0,
        	small/(2*width),1,
        	(2*width-small)/(2*width),0,
        	
        	(2*width-small)/(2*width),0,
        	small/(2*width),1,
        	(2*width-small)/(2*width),1,
        	
        	0,small/(2*length),
        	0,(2*length-small)/(2*length),
        	small/(2*width),0,
        	
        	small/(2*width),0,
        	0,(2*length-small)/(2*length),
        	small/(2*width),1,
        	
        	(2*width-small)/(2*width),0,
        	(2*width-small)/(2*width),1,
        	1,small/(2*length),
        	
        	1,small/(2*length),
        	(2*width-small)/(2*width),1,
        	1,(2*length-small)/(2*length)
        };
        ByteBuffer tbb = ByteBuffer.allocateDirect(texST.length*4);
        tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTexCoorBuffer = tbb.asFloatBuffer();//�ରint���w�R
        mTexCoorBuffer.put(texST);//�V�w�R�Ϥ���J���I�ۦ���
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
