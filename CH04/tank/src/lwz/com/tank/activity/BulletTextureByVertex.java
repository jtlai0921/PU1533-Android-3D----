
package lwz.com.tank.activity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class BulletTextureByVertex {
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mTextureBuffer;
	int texId;
	int vCount=0;
	
	float x=0;
	float y=0;
	float z=0;
	float r;
	float h;
	float H;
	float tempAngle=15;
	
	public BulletTextureByVertex(float r,float h,float H ,int texId )
	{
		this.r=r;
		this.h=h;
		this.H=H;
		this.texId=texId;
		
		ArrayList<Float> bal=new ArrayList<Float>();//���I�s��M��
		ArrayList<Float> balTexture=new ArrayList<Float>();
		
		for(float circle_degree=0.0f;circle_degree<360.0f;circle_degree+=tempAngle)
		{
			float x0=x;
			float y0=y;
			float z0=z;
			
			float x1=(float) (r*Math.sin(Math.toRadians(circle_degree)));
			float y1=(float) (r*Math.cos(Math.toRadians(circle_degree)));
			float z1=z;
			
			float x2=(float) (r*Math.sin(Math.toRadians(circle_degree+tempAngle)));
			float y2=(float) (r*Math.cos(Math.toRadians(circle_degree+tempAngle)));
			float z2=z;
			
			float x3=(float) (r*Math.sin(Math.toRadians(circle_degree)));
			float y3=(float) (r*Math.cos(Math.toRadians(circle_degree)));
			float z3=z+h;
			
			float x4=(float) (r*Math.sin(Math.toRadians(circle_degree+tempAngle)));
			float y4=(float) (r*Math.cos(Math.toRadians(circle_degree+tempAngle)));
			float z4=z+h;
			
			float x5=x;
			float y5=y;
			float z5=z+H;
			
			bal.add(x5);bal.add(y5);bal.add(z5);
			bal.add(x4);bal.add(y4);bal.add(z4);
			bal.add(x3);bal.add(y3);bal.add(z3);
			
			bal.add(x4);bal.add(y4);bal.add(z4);
			bal.add(x2);bal.add(y2);bal.add(z2);
			bal.add(x3);bal.add(y3);bal.add(z3);
			
			bal.add(x3);bal.add(y3);bal.add(z3);
			bal.add(x2);bal.add(y2);bal.add(z2);
			bal.add(x1);bal.add(y1);bal.add(z1);
			
			bal.add(x0);bal.add(y0);bal.add(z0);
			bal.add(x1);bal.add(y1);bal.add(z1);
			bal.add(x2);bal.add(y2);bal.add(z2);
			
			balTexture.add(0.5f);balTexture.add(0.0f);
			balTexture.add(0.0f);balTexture.add(1.0f);
			balTexture.add(1.0f);balTexture.add(1.0f);
			
			balTexture.add(0.0f);balTexture.add(0.0f);
			balTexture.add(0.0f);balTexture.add(1.0f);
			balTexture.add(1.0f);balTexture.add(0.0f);
			
			balTexture.add(1.0f);balTexture.add(0.0f);
			balTexture.add(0.0f);balTexture.add(1.0f);
			balTexture.add(1.0f);balTexture.add(1.0f);
			
			balTexture.add(0.5f);balTexture.add(0.0f);
			balTexture.add(0.0f);balTexture.add(1.0f);
			balTexture.add(1.0f);balTexture.add(1.0f);
		
			vCount=bal.size()/3;//�T�w���I�ƶq
			
			//���I
			float[] vertices=new float[vCount*3];
			for(int i=0;i<vCount*3;i++)
			{
				vertices[i]=bal.get(i);
			}
			
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
			
	        float textures[]=new float[balTexture.size()];
	        for(int i=0;i<balTexture.size();i++)
	        {
	        	textures[i]=balTexture.get(i);
	        }
	      //�إ߳��I���z��ƽw�R
	        //textureCoors.length��4�O�]���@��float����ƥ|�Ӧ줸��
	        ByteBuffer cbb = ByteBuffer.allocateDirect(textures.length*4);
	        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
	        mTextureBuffer = cbb.asFloatBuffer();//�ରint���w�R
	        mTextureBuffer.put(textures);//�V�w�R�Ϥ���J���I�ۦ���
	        mTextureBuffer.position(0);//�]�w�w�R�ϰ_�l��m
	        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
	        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
	        //���I���z��ƪ��_�l��================end============================
		}
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
        
        //�������z
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D); 
	}
	
}
