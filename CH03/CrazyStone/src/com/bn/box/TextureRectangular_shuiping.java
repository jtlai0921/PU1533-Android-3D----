package com.bn.box;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import com.bn.util.From2DTo3DUtil;

public class TextureRectangular_shuiping 
{
	private FloatBuffer mVertexBuffer;//���I�y�и�ƽw�R
    private FloatBuffer mTextureBuffer;//���I���z��ƽw�R
    int vCount;
    
    public TextureRectangular_shuiping(float width,float height)
    {
    	float vertices[]=From2DTo3DUtil.Vertices_heng(width,height);
    	this.vCount=vertices.length/3;
    	
    	ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//�إ߳��I��ƽw�R
    	vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
    	mVertexBuffer = vbb.asFloatBuffer();//�ରint���w�R
    	mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
        float textureCoors[]=
    	{
    		0,0,
    		0,1,
    		1,0,
    		1,0,
    		0,1,
    		1,1
    	};
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length*4);//�إ߳��I���z��ƽw�R
        tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTextureBuffer= tbb.asFloatBuffer();//�ରFloat���w�R
        mTextureBuffer.put(textureCoors);//�V�w�R�Ϥ���J���I�ۦ���
        mTextureBuffer.position(0);//�]�w�w�R�ϰ_�l��m
    }
    
    public void drawSelf(GL10 gl,float angle,int textureId,float[] point)
    {
    	gl.glPushMatrix();	
    	gl.glTranslatef(point[0], point[1], -0.05f);
        gl.glRotatef((float) (-angle*180/Math.PI), 0, 0, 1);

    	
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�ҥγ��I�y�а}�C
    	
    	gl.glVertexPointer
        (
        		3,				//�C�ӳ��I���y�мƶq��3  xyz 
        		GL10.GL_FLOAT,	//���I�y�ЭȪ����A�� GL_FIXED
        		0, 				//�s���I�y�и�Ƥ��������j
        		mVertexBuffer	//���I�y�и��
        );
    	
    	//�}�ү��z
        gl.glEnable(GL10.GL_TEXTURE_2D);   
        //�e�\�ϥί��z�}�C
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //���e�����w���zuv�y�и��
        gl.glTexCoordPointer
        (
        		2, 					//�C�ӳ��I��ӯ��z�y�и�� S�BT
        		GL10.GL_FLOAT, 		//��ƫ��A
        		0, 					//�s�򯾲z�y�и�Ƥ��������j
        		mTextureBuffer		//���z�y�и��
        );
        
        //���e���j�w���w�W��ID���z		
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);   
        
        //ø��ϧ�
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 
        		0, 
        		vCount
        );
        gl.glPopMatrix();
    }
}
