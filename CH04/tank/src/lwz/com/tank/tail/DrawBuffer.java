package lwz.com.tank.tail;
 
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import lwz.com.tank.game.MySurfaceView;
import lwz.com.tank.game.OtherSurfaceView;


//���z�T����
public class DrawBuffer 
{	 
    public DrawBuffer(MySurfaceView mv)
    { 
    }  
    
    public DrawBuffer(OtherSurfaceView mv)
    { 
    }  
    public void drawSelf(int texId,GL10 gl,FloatBuffer mVertexBuffer,FloatBuffer mTexCoorBuffer,int vCount)
    {        
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�ҥγ��I�y�а}�C
		//���e�����w���I�y�и��
        gl.glVertexPointer
        (
        		3,				//�C�ӳ��I���y�мƶq��3  xyz 
        		GL10.GL_FLOAT,	//���I�y�ЭȪ����A�� GL_FIXED
        		0, 				//�s���I�y�и�Ƥ��������j
        		mVertexBuffer	//���I�y�и��
        );
        //�}�ү��z
        gl.glEnable(GL10.GL_TEXTURE_2D);   
        //�e�\�ϥί��zST�y�нw�R
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //���e�����w���zST�y�нw�R
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoorBuffer);
        //�j�w�ثe���z
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		
        //ø��ϧ�
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//�H�T���μҦ���R
        		0,
        		vCount 
        );
        
        //�������z
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D); 
    }
}
