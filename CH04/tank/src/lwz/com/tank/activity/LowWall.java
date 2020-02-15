
package lwz.com.tank.activity;

import static lwz.com.tank.activity.Constant.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class LowWall {
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mTextureBuffer;
	int texId;
	int vCount=0;
	float height;
	float x;
	float y;
	float z;
	float width;
	float length;
	
	public LowWall(int texId)
	{
		this.texId = texId;
		vCount=36;
		
	//	final int UNIT_SIZE=Constant.Wall_UNIT_SIZE;		
		int rows=lowWallmapdata.length;
//		int cols=mapdata[0].length;
//        
			ArrayList<Float> alVertex=new ArrayList<Float>();
//        ArrayList<Float> alNormal=new ArrayList<Float>();
			ArrayList<Float> alTexture=new ArrayList<Float>();
			
			
        
        vCount=36*rows;
        for(int i=0;i<rows;i++)
        {
        		float x=lowWallmapdata[i][0]*Wall_UNIT_SIZE;
        		float y=lowWallmapdata[i][1]*Wall_UNIT_SIZE;
        		float z=lowWallmapdata[i][2]*Wall_UNIT_SIZE;
        		float width=lowWallmapdata[i][3]*Wall_UNIT_SIZE;
        		float length=lowWallmapdata[i][4]*Wall_UNIT_SIZE;
        		float height=lowWallmapdata[i][5]*Wall_UNIT_SIZE;
		
        		float x1=x;float y1=y;float z1=z+height;
        		float x2=x;float y2=y-length;float z2=z+height;
        		float x3=x+width;float y3=y;float z3=z+height;
        		float x4=x+width;float y4=y-length;float z4=z+height;
        		float x5=x+width;float y5=y;float z5=z;
        		float x6=x+width;float y6=y-length;float z6=z;
        		float x7=x;float y7=y;float z7=z;
        		float x8=x;float y8=y-length;float z8=z;
        		//�e��
        		alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
        		alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);
        		alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
        		
        		alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
        		alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);		
        		alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
        		//�k��
        		alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
        		alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
        		alVertex.add(x5);alVertex.add(y5);alVertex.add(z5);
        		
        		alVertex.add(x5);alVertex.add(y5);alVertex.add(z5);
        		alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
        		alVertex.add(x6);alVertex.add(y6);alVertex.add(z6);
        		//�᭱
        		alVertex.add(x5);alVertex.add(y5);alVertex.add(z5);
        		alVertex.add(x6);alVertex.add(y6);alVertex.add(z6);
        		alVertex.add(x7);alVertex.add(y7);alVertex.add(z7);
        		
        		alVertex.add(x7);alVertex.add(y7);alVertex.add(z7);
        		alVertex.add(x6);alVertex.add(y6);alVertex.add(z6);
        		alVertex.add(x8);alVertex.add(y8);alVertex.add(z8);
        		//����
        		alVertex.add(x7);alVertex.add(y7);alVertex.add(z7);
        		alVertex.add(x8);alVertex.add(y8);alVertex.add(z8);
        		alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
        		
        		alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
        		alVertex.add(x8);alVertex.add(y8);alVertex.add(z8);
        		alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);
        		//�W��
        		alVertex.add(x7);alVertex.add(y7);alVertex.add(z7);
        		alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
        		alVertex.add(x5);alVertex.add(y5);alVertex.add(z5);
        		
        		alVertex.add(x5);alVertex.add(y5);alVertex.add(z5);
        		alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
        		alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
        		//�U��
        		alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);
        		alVertex.add(x8);alVertex.add(y8);alVertex.add(z8);
        		alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
        		
        		alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
        		alVertex.add(x8);alVertex.add(y8);alVertex.add(z8);
        		alVertex.add(x6);alVertex.add(y6);alVertex.add(z6);
        		//�e��
        		alTexture.add(0f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(0f);
				
				alTexture.add(1f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(1f);
				//�k��
				alTexture.add(0f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(0f);
				
				alTexture.add(1f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(1f);
        		//�᭱
				alTexture.add(0f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(0f);
				
				alTexture.add(1f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(1f);
				//����
				alTexture.add(0f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(0f);
				
				alTexture.add(1f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(1f);
				//�W��
				alTexture.add(0f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(0f);
				
				alTexture.add(1f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(1f);
				//�U��
				alTexture.add(0f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(0f);
				
				alTexture.add(1f);alTexture.add(0f);
				alTexture.add(0f);alTexture.add(1f);
				alTexture.add(1f);alTexture.add(1f);
        }
        vCount=alVertex.size()/3;        
        float vertices[]=new float[alVertex.size()];
        for(int i=0;i<alVertex.size();i++)
        {
        	vertices[i]=alVertex.get(i);
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
		
        float textures[]=new float[alTexture.size()];
        for(int i=0;i<alTexture.size();i++)
        {
        	textures[i]=alTexture.get(i);
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
