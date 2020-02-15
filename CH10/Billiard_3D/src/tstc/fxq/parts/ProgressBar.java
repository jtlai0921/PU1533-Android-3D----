package tstc.fxq.parts;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;

/*
 * 3D�����i�׫��ܾ����O
 */
public class ProgressBar 
{	
	int mProgram;//�ۭq�ۦ�ް������id
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id
    int muProgress;			//�i�׫��ܾ���m�y��
    String mVertexShader;//���I�ۦ⾹    	 
    String mFragmentShader;//�����ۦ⾹
	
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	FloatBuffer   mTexCoorBuffer;//���I���z�y�и�ƽw�R 
    int vCount=0;
    
    float x;
    float y;
    float width;
    float height;
    
    float sEnd;
    float tEnd;
    
    /*
     *�ѩ�i�׫��ܾ��۹��ù��m���A�ҥH2*X+W��������1�A�_�h�|�X�{���~ 
     */
    
    public ProgressBar(
    		MySurfaceView mv,
    		float x,float y,      //���]�ù������e���O1�A�nø��i�׫��ܾ����W�I�y�Хe�ù������
    		float height, 		//�P�˰��]�ù������e���O1�A�nø��i�׫��ܾ����e�ù������
    							//�ѩ�i�׫��ܾ��۹��ù��m���A�ҥH2*X+W��������1�A�_�h�|�X�{���~ ,�ҥH�e�׭�width��x�M�w
    		
    		float sEnd,float tEnd //���z�y�Ш��o�����o���I�}�ϥk�U�I�y��
    		
    )
    {    	
    	this.x = x;
    	this.y = y;
    	
    	this.height = height;
    	this.width = 1f-2*x;
    	
    	this.sEnd = sEnd;
    	this.tEnd = tEnd;
    	

    	//�_�l�Ƴ��I�y�лP�ۦ���
    	initVertexData();
    	//�_�l��shader        
    	intShader(mv);
    }
    
    //�_�l�Ƴ��I�y�лP�ۦ��ƪ���k
    public void initVertexData()
    {
    	//���I�y�и�ƪ��_�l��================begin============================
        vCount=6;
       
        float vertices[]=new float[]
        {
        	-(1-2*1*x),1-2*y, 0,
        	-(1-2*1*x), 1-2*y-2*height, 0,
        	-(1-2*1*x)+2*1*width, 1-2*y, 0,
        	
        	-(1-2*1*x), 1-2*y-2*height, 0,
        	-(1-2*1*x)+2*1*width, 1-2*y-2*height, 0,
        	-(1-2*1*x)+2*1*width, 1-2*y, 0
        };
		
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
        
        //���I���z�y�и�ƪ��_�l��================begin============================
        float texCoor[]=new float[]//���z�y��
        {
        		0,0, 0,tEnd, sEnd,0,
        		0,tEnd, sEnd,tEnd, sEnd,0        		
        };        
        //�إ߳��I���z�y�и�ƽw�R
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTexCoorBuffer = cbb.asFloatBuffer();//�ରFloat���w�R
        mTexCoorBuffer.put(texCoor);//�V�w�R�Ϥ���J���I�ۦ���
        mTexCoorBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I���z�y�и�ƪ��_�l��================end============================

    }

    //�_�l��shader
    public void intShader(MySurfaceView mv)
    {  
        //����I�ۦ⾹�P�����ۦ⾹�إߵ{��
        mProgram = ShaderManager.getProgressBarShader();
        //���o�{�������I��m�ݩʰѦ�id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //���o�{�������I���z�y���ݩʰѦ�id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //���o�{�����`�ܴ��x�}�Ѧ�id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //�i�ת��Ѧ�id
        muProgress= GLES20.glGetUniformLocation(mProgram, "uProgress");
    }
    
    public void drawSelf(int TexId,float currProgress)
    {
    	
    	currProgress = percentageChangeToX(currProgress);
    		
    	 //��w�ϥάY�Mshader�{��
    	 GLES20.glUseProgram(mProgram);         
         //�N�̲��ܴ��x�}�ǤJshader�{��
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
         //�N�ثe���i�׶ǤJ��Shader�{��
         GLES20.glUniform1f(muProgress, currProgress);
         
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
                mTexCoorBuffer
         );    
         //�e�\���I��m��ư}�C
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle); 
         //�j�w���z
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TexId);
         
         //ø��T����
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
    
    
    //�N�i����Ƭ�3D�@�ɤ���x�y�Ъ���k
    private float percentageChangeToX(float currProgress)
    {
    	return (2*width/100f)*(currProgress-50) ;
    }
    
    
}
