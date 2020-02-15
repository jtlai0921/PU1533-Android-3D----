package com.bn.txz.manager;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;
import com.bn.txz.Constant;

//�u�����I�P���z�y�Ъ�����
public class VertexTexture3DObjectForDraw
{
	public FloatBuffer mVertexBuffer; //���I�y�и�ƽw�R
	public FloatBuffer mTexBuffer; //���I���z�y�нw�R
	public int vCount = 0; //���I��
	
	//vertices���I  texCoor���zST�y��
	public VertexTexture3DObjectForDraw(FloatBuffer mVertexBuffer,FloatBuffer mTexBuffer,int vCount) 
	{
		this.mVertexBuffer=mVertexBuffer;
		this.mTexBuffer=mTexBuffer;  
		this.vCount=vCount;
	}

	public void drawSelf(GL10 gl, int texId) {

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�e�\�ϥγ��I�}�C
		gl.glEnable(GL10.GL_TEXTURE_2D); //�}�ү��z
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); //�e�\�ϥί��zST�y�нw�R
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);	//���e�����w���I�y�и��  
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);  //���e�����w���zST�y�нw�R
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);   //�j�w�ثe���z
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount); //ø��ϧ�
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//�����ϥγ��I�}�C
		gl.glDisableClientState(GL10.GL_TEXTURE_2D); //�������z
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�����ϥί��zST�y�нw�R
	}
	
	int xOffset;//x�����q
    int zOffset;//z�����q
    float scale;//�P�P�ؤo
    IntBuffer mColorBuffer;//���I�ۦ���
	public VertexTexture3DObjectForDraw(int xOffset,int zOffset,float scale,
			FloatBuffer mVertexBuffer,IntBuffer mColorBuffer,int vCount)
	{
		this.xOffset=xOffset;
		this.zOffset=zOffset;
		this.scale=scale;
		this.mVertexBuffer=mVertexBuffer;
		this.mColorBuffer=mColorBuffer;
		this.vCount=vCount;
	}
	public void drawSelf(GL10 gl)
    {        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�ҥγ��I�y�а}�C
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);//�ҥγ��I�m��}�C
        
        gl.glPointSize(scale);//�]�w�P�P�ؤo
        gl.glTranslatef(xOffset*Constant.UNIT_SIZE, 0, 0);//x�V����
        gl.glTranslatef(0, 0, zOffset*Constant.UNIT_SIZE);//y�V����
        
		//���e�����w���I�y�и��
        gl.glVertexPointer
        (
        		3,				//�C�ӳ��I���y�мƶq��3  xyz 
        		GL10.GL_FLOAT,	//���I�y�ЭȪ����A�� GL_FIXED
        		0, 				//�s���I�y�и�Ƥ��������j
        		mVertexBuffer	//���I�y�и��
        );        
        
        //���e�����w���I�ۦ���
        gl.glColorPointer
        (
        		4, 				//�]�w�m�⪺�s�զ������A������4�XRGBA
        		GL10.GL_FIXED, 	//���I�m��Ȫ����A�� GL_FIXED
        		0, 				//�s���I�ۦ��Ƥ��������j
        		mColorBuffer	//���I�ۦ���
        );
		
        //ø�� �I
        gl.glDrawArrays
        (
        		GL10.GL_POINTS, 		//�H�I�Ҧ���R
        		0, 			 			//�}�l�I�s��
        		vCount					//���I���ƶq
        );
        
        gl.glPointSize(1);//�٭칳���ؤo
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//�����ϥγ��I�}�C
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);//�����ϥαm��}�C
    }
}
