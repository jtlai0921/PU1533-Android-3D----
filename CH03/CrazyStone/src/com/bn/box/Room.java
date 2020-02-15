package com.bn.box;

import static com.bn.util.Constant.Height;
import static com.bn.util.Constant.RATIO;
import static com.bn.util.Constant.Width;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import com.bn.zxl.GameView;

public class Room 
{
	float width;
	float height;
	int id;
	private FloatBuffer   mVertexBuffer1;//���I�y�и�ƽw�R
	private FloatBuffer   mVertexBuffer2;//���I�y�и�ƽw�R
	private FloatBuffer   mVertexBuffer3;//���I�y�и�ƽw�R
    private FloatBuffer   mTextureBuffer;//���I���z��ƽw�R
    int vCount=0;//���I�ƶq    
	GameView gameview;
	public Room(GameView gameview,float width,float height,float deep)
	{
		this.height=height;
		this.width=width;
		this.gameview=gameview;
		
		vCount=6;//���I���ƶq    	
        float [][]vertices=new float[3][18];
        vertices=vertices(width,height,deep);
        
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb1 = ByteBuffer.allocateDirect(vertices[0].length*4);
        vbb1.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer1 = vbb1.asFloatBuffer();//�ରint���w�R
        mVertexBuffer1.put(vertices[0]);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer1.position(0);//�]�w�w�R�ϰ_�l��m
        
        ByteBuffer vbb2 = ByteBuffer.allocateDirect(vertices[0].length*4);
        vbb2.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer2 = vbb2.asFloatBuffer();//�ରint���w�R
        mVertexBuffer2.put(vertices[1]);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer2.position(0);//�]�w�w�R�ϰ_�l��m
        
        ByteBuffer vbb3 = ByteBuffer.allocateDirect(vertices[0].length*4);
        vbb3.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer3 = vbb3.asFloatBuffer();//�ରint���w�R
        mVertexBuffer3.put(vertices[2]);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer3.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I�y�и�ƪ��_�l��================end============================
        
        //���I���z��ƪ��_�l��================begin============================
        float textureCoors[]=//���I���zS�BT�y�ЭȰ}�C
	    {
        	0,0,//1
        	0,1,
        	1f,0,
        	1,0,
        	0,1,
        	1,1,
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
        //���I���z��ƪ��_�l��================end============================
	}
	
	public void drawSelf(GL10 gl)
    {  
        gl.glPushMatrix();

        //���I�y��==========begin=============================================
        //�e�\�ϥγ��I�}�C
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//���e�����w���I�y�и��
        gl.glVertexPointer
        (
        		3,				//�C�ӳ��I���y�мƶq��3  xyz 
        		GL10.GL_FLOAT,	//���I�y�ЭȪ����A�� GL_FIXED
        		0, 				//�s���I�y�и�Ƥ��������j
        		mVertexBuffer1	//���I�y�и��
        );
        //���I�y��==========end===============================================
        
        //���z===========begin================================================
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_background);   
//	        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId);   
        //���z===========end==================================================
        //ø��ϧ�
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 
        		0, 
        		vCount
        );

        
        //�}�lø��e����
        gl.glVertexPointer
        (
        		3,				//�C�ӳ��I���y�мƶq��3  xyz 
        		GL10.GL_FLOAT,	//���I�y�ЭȪ����A�� GL_FIXED
        		0, 				//�s���I�y�и�Ƥ��������j
        		mVertexBuffer2	//���I�y�и��
        );
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId);
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 
        		0, 
        		vCount
        );
        
        gl.glVertexPointer
        (
        		3,				//�C�ӳ��I���y�мƶq��3  xyz 
        		GL10.GL_FLOAT,	//���I�y�ЭȪ����A�� GL_FIXED
        		0, 				//�s���I�y�и�Ƥ��������j
        		mVertexBuffer3	//���I�y�и��
        );
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_background);
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 
        		0, 
        		vCount
        );
        
        gl.glPopMatrix();
    }
	
	public static float[][] vertices(float width,float height,float deep)
	{
		float[][] vertices=new float[3][18];
		float halfwidth=width/2;
		float halfheight=height/2;
		vertices[0][0]=(-halfwidth)/(Width/2) *RATIO ;
		vertices[0][1]=(halfheight)/(Height/2) ;
		vertices[0][2]=-deep;
		
		vertices[0][3]=(-halfwidth)/(Width/2)*RATIO  ;
		vertices[0][4]=(-halfheight)/(Height/2) ;
		vertices[0][5]=-deep;
		
		vertices[0][6]=(halfwidth)/(Width/2) *RATIO ;
		vertices[0][7]=halfheight/(Height/2);
		vertices[0][8]=-deep;
		
		vertices[0][9]=(halfwidth)/(Width/2)*RATIO  ;
		vertices[0][10]=halfheight/(Height/2) ;
		vertices[0][11]=-deep;
		
		vertices[0][12]=(-halfwidth)/(Width/2)*RATIO  ;
		vertices[0][13]=(-halfheight)/(Height/2);
		vertices[0][14]=-deep;
		
		vertices[0][15]=(halfwidth)/(Width/2) *RATIO ;
		vertices[0][16]=(-halfheight)/(Height/2) ;
		vertices[0][17]=-deep;
		
		
		vertices[1][0]=(-halfwidth)/(Width/2)*RATIO  ;
		vertices[1][1]=(-halfheight)/(Height/2) ;
		vertices[1][2]=deep;
		
		vertices[1][3]=(-halfwidth)/(Width/2) *RATIO ;
		vertices[1][4]=(-halfheight)/(Height/2) ;
		vertices[1][5]=-deep;
		
		vertices[1][6]=(halfwidth)/(Width/2) *RATIO ;
		vertices[1][7]=-halfheight/(Height/2);
		vertices[1][8]=deep;
		
		vertices[1][9]=(halfwidth)/(Width/2) *RATIO ;
		vertices[1][10]=-halfheight/(Height/2) ;
		vertices[1][11]=deep;
		
		vertices[1][12]=(-halfwidth)/(Width/2)*RATIO  ;
		vertices[1][13]=(-halfheight)/(Height/2) ;
		vertices[1][14]=-deep;
		
		vertices[1][15]=(halfwidth)/(Width/2)*RATIO  ;
		vertices[1][16]=(-halfheight)/(Height/2) ;
		vertices[1][17]=-deep;
		
		
		vertices[2][0]=(halfwidth)/(Width/2)*RATIO  ;
		vertices[2][1]=(-halfheight)/(Height/2) ;
		vertices[2][2]=deep;
		
		vertices[2][3]=(halfwidth)/(Width/2)*RATIO  ;
		vertices[2][4]=(-halfheight)/(Height/2) ;
		vertices[2][5]=-deep;
		
		vertices[2][6]=(halfwidth)/(Width/2)*RATIO  ;
		vertices[2][7]=halfheight/(Height/2);
		vertices[2][8]=deep;
		
		vertices[2][9]=(halfwidth)/(Width/2) *RATIO ;
		vertices[2][10]=halfheight/(Height/2) ;
		vertices[2][11]=deep;
		
		vertices[2][12]=(halfwidth)/(Width/2) *RATIO ;
		vertices[2][13]=(-halfheight)/(Height/2) ;
		vertices[2][14]=-deep;
		
		vertices[2][15]=(halfwidth)/(Width/2)*RATIO  ;
		vertices[2][16]=(halfheight)/(Height/2) ;
		vertices[2][17]=-deep;
		return vertices;
	}
}
