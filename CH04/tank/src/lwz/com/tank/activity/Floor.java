package lwz.com.tank.activity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Floor {
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mTextureBuffer;
	int texId;
	int vCount=0;
	
	float xOffset;
	float yOffset;
	float width;
	float length;
	float height;
	
	public Floor(float xOffset,float yOffset,float width,float length,float height,int texId)
	{
		this.xOffset=xOffset;
		this.yOffset=yOffset;
		this.width=width;
		this.length=length;
		this.height=height;
		this.texId=texId;
		
		vCount=6;
		//final int UNIT_SIZE=65;
		final int UNIT_SIZE=200;
		float vertices[]=new float[]
			{
				xOffset*UNIT_SIZE,yOffset*UNIT_SIZE,0,
				xOffset*UNIT_SIZE,(yOffset-length)*UNIT_SIZE,0,
				(xOffset+width)*UNIT_SIZE,yOffset*UNIT_SIZE,0,
				
				(xOffset+width)*UNIT_SIZE,yOffset*UNIT_SIZE,0,
				xOffset*UNIT_SIZE,(yOffset-length)*UNIT_SIZE,0,
				(xOffset+width)*UNIT_SIZE,(yOffset-length)*UNIT_SIZE,0,
				
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
        
        float textureCoors[]=new float[]
        	{
        		0,0,
        		0,1,
        		1,0,
        		
        		1,0,
        		0,1,
        		1,1
        	};
      //�إ߳��I���z��ƽw�R
        //textureCoors.length��4�O�]���@��float����ƥ|�Ӧ줸��
        ByteBuffer cbb = ByteBuffer.allocateDirect(textureCoors.length*4);
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTextureBuffer = cbb.asFloatBuffer();//�ରint���w�R
        mTextureBuffer.put(textureCoors);//�V�w�R�Ϥ���J���I�ۦ���
        mTextureBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I���z��ƪ��_�l��======
	}
	public void drawSelf(GL10 gl)
	{
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�ҥγ��I�y�и�ƽw�R
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
        
        //�������z
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
	}
}
