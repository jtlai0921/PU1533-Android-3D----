package tstc.fxq.parts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;
import static tstc.fxq.constants.Constant.*;
public class PoolMap {  
	int mProgram;//�ۭq�ۦ�ް������id 
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id 
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id 
    
	private FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
    private FloatBuffer mTexCoorBuffer;//���I���z��ƽw�R
    int vCount=0;
	public PoolMap(MySurfaceView mv)
	{
		
		float [] vertices=new float[]
        {
			//�ୱ�|���
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//1
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//2
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//3
				
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//3
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//4
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//1
				
				//�y�����
				//�����
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//2
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//1
				-TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//5
				
				-TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//5
				-TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//6
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//2
				//�e��ء]�U�^
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//2
				-TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//6
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//7
				
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//7
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//3
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//2
				
				//�k���
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//3
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//7
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//8
				
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//8
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//4
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,-TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//3
				//����ء]�W�^
				TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//4
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//8
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//1
				
				TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//8
				-TABLE_UNIT_SIZE*BOTTOM_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*BOTTOM_WIDE*MiniMapScale,0,//5
				-TABLE_UNIT_SIZE*TABLE_AREA_LENGTH*MiniMapScale,TABLE_UNIT_SIZE*TABLE_AREA_WIDTH*MiniMapScale,0,//1
        };
		
		vCount=vertices.length/3;//���I�ƶq
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରFloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
        float[] textureCoors=
       {
    		//�ୱ
    		0.031f,0.078f,0.031f,0.723f,0.547f,0.723f,
    		0.547f,0.723f,0.547f,0.078f,0.031f,0.078f,
    		//�����
    		0.031f,0.723f,0.031f,0.078f,0,0,
    		0,0,0,0.793f,0.031f,0.723f,
    		//�e���(�U)
    		0.031f,0.723f,0,0.793f,0.582f,0.793f,
    		0.582f,0.793f,0.547f,0.723f,0.031f,0.723f,
    		//�k���
    		0.547f,0.723f,0.582f,0.793f,0.582f,0,
    		0.582f,0,0.547f,0.078f,0.547f,0.723f,
    		//����ء]�W�^
    		0.547f,0.078f,0.582f,0,0.031f,0.078f,
    		0.582f,0,0,0,0.031f,0.078f
       };
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTexCoorBuffer = tbb.asFloatBuffer();//�ରint���w�R
        mTexCoorBuffer.put(textureCoors);//�V�w�R�Ϥ���J���I�ۦ���
        mTexCoorBuffer.position(0);//�]�w�w�R�ϰ_�l��m 

        //�_�l��shader
        intShader(mv);
	}
    //�_�l��shader
    public void intShader(MySurfaceView mv)
    {
    	//���o�ۭq�ۦ�ް������id 
        mProgram = ShaderManager.getTexShader();
        //���o�{�������I��m�ݩʰѦ�id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
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
