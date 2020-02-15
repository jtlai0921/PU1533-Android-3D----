package com.bn.ball;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
//���J�᪺����X�X��a���I�B�k�V�q�B���z�T��
public class LoadedObjectVertexNormalTexture 
{
	private FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	private FloatBuffer   mNormalBuffer;//���I�k�V�q��ƽw�R
	private FloatBuffer   mTexBuffer;//���I���z��ƽw�R
    int vCount=0;  
    int texId;
      
    public LoadedObjectVertexNormalTexture(float[] vertices,float[] normals,float texCoors[],int texId) 
    {
    	this.texId=texId;
    	
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
        
        //�k�V�q�T���_�l��
        ByteBuffer vbn = ByteBuffer.allocateDirect(normals.length*4);
        vbn.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mNormalBuffer = vbn.asFloatBuffer();//�ରFloat���w�R
        mNormalBuffer.put(normals);//�V�w�R�Ϥ���J���I�y�и��
        mNormalBuffer.position(0);//�]�w�w�R�ϰ_�l��m 
        
        //���z�y�нw�R�_�l��
        ByteBuffer vbt = ByteBuffer.allocateDirect(texCoors.length*4);
        vbt.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTexBuffer = vbt.asFloatBuffer();//�ରFloat���w�R
        mTexBuffer.put(texCoors);//�V�w�R�Ϥ���J���I�y�и��
        mTexBuffer.position(0);//�]�w�w�R�ϰ_�l��m 
    }

    public void drawSelf(GL10 gl)
    {        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�ҥγ��I�y�а}�C
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);//�ҥγ��I�k�V�q�}�C     
        gl.glEnable(GL10.GL_TEXTURE_2D);    //�}�ү��z      
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�e�\�ϥί��zST�y�нw�R

        
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
        
        //���e�����w���zST�y�нw�R
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
        //�j�w�ثe���z
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		
        //ø��ϧ�
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//�H�T���μҦ���R
        		0, 			 			//�}�l�I�s��
        		vCount					//���I���ƶq
        );        
        
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);//�T�γ��I�k�V�q�}�C
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//�ҥγ��I�y�а}�C
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�e�\�ϥί��zST�y�нw�R
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }
}
