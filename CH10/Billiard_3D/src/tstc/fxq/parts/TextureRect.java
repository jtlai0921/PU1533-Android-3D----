package tstc.fxq.parts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;
//�o�ӥD�n�O�w��ɭ� �����z�x��
public class TextureRect 
{
	int mProgram;//�ۭq�ۦ�ް������id 
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id   
    int muMMatrixHandle;//��m�B�����ܴ��x�}
    int maCameraHandle; //��v����m�ݩʰѦ�id  
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maNormalHandle; //���I�k�V�q�ݩʰѦ�id  
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id  
    int maSunLightLocationHandle;//������m�ݩʰѦ�id  
    
    String mVertexShader;//���I�ۦ⾹    	 
    String mFragmentShader;//�����ۦ⾹
	
    private FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
    private FloatBuffer   mTextureBuffer;//���I�ۦ��ƽw�R
    int vCount;//���I�ƶq
    int texId;//���zId
    
    float width;
    float height;
    float sEnd;
    float tEnd;
    
	public TextureRect(float width,float height,	//���z�x�Ϊ����e
				float sEnd,float tEnd 		//���z�y�Ш��o�����o���I�}�ϥk�U�I�y��
				)
	{
		this.width=width;
    	this.height=height;
    	this.sEnd=sEnd;
    	this.tEnd=tEnd;
    	
    	//���I�y�и�ƪ��_�l��================begin============================
        vCount=6;//�C�Ӯ�l��ӤT���ΡA�C�ӤT����3�ӳ��I        
        float vertices[]=
        {
        		-width/2, height/2,0,
        		-width/2, -height/2,0,
        		width/2, height/2,0,
        		
        		-width/2, -height/2,0,
        		width/2, -height/2,0,
        		width/2, height/2,0
        };
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରint���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        float textures[]=
        {
        		0f,0f,    0f,tEnd,    sEnd,0f,
        		0f,tEnd,  sEnd,tEnd,  sEnd,0f
        };
        //�إ߳��I���z��ƽw�R
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTextureBuffer= tbb.asFloatBuffer();//�ରFloat���w�R
        mTextureBuffer.put(textures);//�V�w�R�Ϥ���J���I�ۦ���
        mTextureBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I���z��ƪ��_�l��================end============================  
        
        intShader();
        
	}
    public void intShader()
    {
        this.mProgram=ShaderManager.getTexShader(); 
        //���o�{�������I��m�ݩʰѦ�id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //���o�{�������I���z�y���ݩʰѦ�id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
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
        //���e�����w���I���z�y�и��
        GLES20.glVertexAttribPointer  
        (
       		maTexCoorHandle, 
        		2, 
        		GLES20.GL_FLOAT, 
        		false,
               2*4,   
               mTextureBuffer
        );   
        //�e�\���I��m��ư}�C
        GLES20.glEnableVertexAttribArray(maPositionHandle);  
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
        
        //�j�w���z
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        
        //ø��z�x��
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
	}
	
}
