package lwz.com.tank.activity;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import static lwz.com.tank.activity.Constant.*;
//press any key to start��r�����z�x��
public class TextureRect {
	
	private FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
    private FloatBuffer   mTextureBuffer;//���I�ۦ��ƽw�R
    int vCount;
    int texId;
    
    public TextureRect(int texId,float w,float h,float smax,float tmax)
    {
    	this.texId=texId;
    	
    	//���I�y�и�ƪ��_�l��================begin============================
        vCount=6;
        float vertices[]=new float[]
        {
        	-w*UNIT_SIZE,h*UNIT_SIZE,0,
        	-w*UNIT_SIZE,-h*UNIT_SIZE,0,
        	w*UNIT_SIZE,h*UNIT_SIZE,0,
        	
        	-w*UNIT_SIZE,-h*UNIT_SIZE,0,
        	w*UNIT_SIZE,-h*UNIT_SIZE,0,
        	w*UNIT_SIZE,h*UNIT_SIZE,0
        };
		
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରint���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I�y�и�ƪ��_�l��================end============================
        
        //���I���z��ƪ��_�l��================begin============================
        float textures[]=new float[]
        {
        	0,0,
        	0,tmax,
        	smax,0,
        	0,tmax,
        	smax,tmax,
        	smax,0
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
    }

    public void drawSelf(GL10 gl)
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
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        //�j�w�ثe���z
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		
        //ø��ϧ�
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//�H�T���μҦ���R
        		0,
        		vCount
        );
    }
}
