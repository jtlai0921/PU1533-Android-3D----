package com.bn.ball;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
//���J�᪺����X�X����a���I�T���A�m���H��
public class LoadedObjectVertexNormal 
{
	private FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	private FloatBuffer   mNormalBuffer;//���I�k�V�q��ƽw�R
    int vCount=0; 
      
    public LoadedObjectVertexNormal(float[] vertices,float[] normals) 
    {
    	//���I�y�и�ƪ��_�l��================begin============================
        vCount=vertices.length/3;    
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
        
        ByteBuffer vbn = ByteBuffer.allocateDirect(normals.length*4);
        vbn.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mNormalBuffer = vbn.asFloatBuffer();//�ରFloat���w�R
        mNormalBuffer.put(normals);//�V�w�R�Ϥ���J���I�y�и��
        mNormalBuffer.position(0);//�]�w�w�R�ϰ_�l��m 
    }

    public void drawSelf(GL10 gl)
    {        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�ҥγ��I�y�а}�C
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);//�ҥγ��I�k�V�q�}�C

        
		//���e�����w���I�y�и��
        gl.glVertexPointer
        (
        		3,				//�C�ӳ��I���y�мƶq��3  xyz 
        		GL10.GL_FLOAT,	//���I�y�ЭȪ����A�� GL_FIXED
        		0, 				//�s���I�y�и�Ƥ��������j
        		mVertexBuffer	//���I�y�и��
        ); 
        
        //���e�����w���I�k�V�q���
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
		
        //ø��ϧ�
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//�H�T���μҦ���R
        		0, 			 			//�}�l�I�s��
        		vCount					//���I���ƶq
        );        
        
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);//�T�γ��I�k�V�q�}�C
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//�ҥγ��I�y�а}�C
    }
}
