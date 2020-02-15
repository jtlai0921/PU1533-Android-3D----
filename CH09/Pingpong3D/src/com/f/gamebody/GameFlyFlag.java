package com.f.gamebody;

import com.f.pingpong.Constant;
import com.f.util.MatrixState;

import android.opengl.GLES20;
import static com.f.pingpong.Constant.*;
import static com.f.pingpong.ShaderManager.*;
//���i���ĪG�����z�x��
public class GameFlyFlag 
{	
	private int mPrograms;//�ۭq�ۦ�޽u�ۦ⾹�{��id
	private int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�
	private int maPositionHandle; //���I��m�ݩʰѦ�  
	private int maTexCoorHandle; //���I���z�y���ݩʰѦ�  
	private int maStartAngleHandle; //���ذ_�l�����ݩʰѦ�
	private int muWidthSpanHandle;//���������`��װѦ�
	private float currStartAngle=0;//�ثe�ت��_�l����0~2PI
    
    public GameFlyFlag(int which)
    {    	
    	//�_�l��shader        
    	initShader(which);
    	//�Ұʤ@�Ӱ�����w�ɴ���
    	new Thread()
    	{
    		public void run()
    		{
    			while(Constant.FLAG_FLY_THREAD)
    			{
    			    currStartAngle+=(float)(Math.PI/16);
        			try 
        			{
    					Thread.sleep(50);
    				} catch (InterruptedException e) 
    				{
    					e.printStackTrace();
    				}
    			}     
    		}    
    	}.start();  
    }
    //�_�l��shader
    public void initShader(int which)
    {
        //����I�ۦ⾹�P�����ۦ⾹�إߵ{��
        mPrograms = (which == 0) ? program[5] : program[9];
        //���o�{�������I��m�ݩʰѦ�  
        maPositionHandle = GLES20.glGetAttribLocation(mPrograms, "aPosition");
        //���o�{�������I���z�y���ݩʰѦ�  
        maTexCoorHandle= GLES20.glGetAttribLocation(mPrograms, "aTexCoor");
        //���o�{�����`�ܴ��x�}�Ѧ�
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mPrograms, "uMVPMatrix");  
        //���o���ذ_�l�����ݩʰѦ�
        maStartAngleHandle=GLES20.glGetUniformLocation(mPrograms, "uStartAngle");  
        //���o���������`��װѦ�
        muWidthSpanHandle=GLES20.glGetUniformLocation(mPrograms, "uWidthSpan");  
    }
    public void drawSelf(int texId)
    {        
    	 //��w�ϥάY�Mshader�{��  
    	 GLES20.glUseProgram(mPrograms); 
         //�N�̲��ܴ��x�}�ǤJshader�{��
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //�N���ذ_�l���׶ǤJshader�{��
         GLES20.glUniform1f(maStartAngleHandle, currStartAngle);
         //�N���������`��׶ǤJshader�{��
         GLES20.glUniform1f(muWidthSpanHandle, WIDTH_SPAN);  
         //�N���I��m��ƶǤJ�ۦ�޽u
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,            
         		3, 
         		GLES20.GL_FLOAT,   
         		false,
                3*4,   
                vertexBuffer[0][10]
         );       
         //�N���I���z�y�и�ƶǤJ�ۦ�޽u
         GLES20.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT, 
         		false,
                2*4,   
                texCoorBuffer[0][4]
         );   
         //�ҥγ��I��m�B���z�y�и��
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
         //�j�w���z
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount[0][2]); 
    }
}
