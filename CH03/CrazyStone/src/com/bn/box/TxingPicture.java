package com.bn.box;

import static com.bn.util.Constant.Height;
import static com.bn.util.Constant.RATIO;
import static com.bn.util.Constant.Width;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


import com.bn.util.Constant;
import com.bn.util.From2DTo3DUtil;
import com.bn.zxl.GameView;

public class TxingPicture 
{
	float width;
	float height;
	private FloatBuffer   mVertexBuffer1;//���I�y�и�ƽw�R
	private FloatBuffer   mVertexBuffer2;//���I�y�и�ƽw�R
	private FloatBuffer   mVertexBuffer3;//���I�y�и�ƽw�R
	private FloatBuffer   mVertexBuffer4;//���I�y�и�ƽw�R
	private FloatBuffer   mVertexBuffer5;//���I�y�и�ƽw�R
	private FloatBuffer   mVertexBuffer6;//���I�y�и�ƽw�R
	private FloatBuffer   mVertexBuffer7;//���I�y�и�ƽw�R
	private FloatBuffer   mVertexBuffer8;//���I�y�и�ƽw�R
    private FloatBuffer   mTextureBuffer;//���I���z��ƽw�R
    int vCount=0;//���I�ƶq    
	public TxingPicture(float width,float height) {
		this.height=height;
		this.width=width;
		 vCount=6;//���I���ƶq    	
	        float [][]vertices=new float[8][18];
	        vertices=vertices(width,height);
	        
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
	        
	        ByteBuffer vbb4 = ByteBuffer.allocateDirect(vertices[0].length*4);
	        vbb4.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
	        mVertexBuffer4 = vbb4.asFloatBuffer();//�ରint���w�R
	        mVertexBuffer4.put(vertices[3]);//�V�w�R�Ϥ���J���I�y�и��
	        mVertexBuffer4.position(0);//�]�w�w�R�ϰ_�l��m
	        
	        ByteBuffer vbb5 = ByteBuffer.allocateDirect(vertices[0].length*4);
	        vbb5.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
	        mVertexBuffer5 = vbb5.asFloatBuffer();//�ରint���w�R
	        mVertexBuffer5.put(vertices[4]);//�V�w�R�Ϥ���J���I�y�и��
	        mVertexBuffer5.position(0);//4�]�w�w�R�ϰ_�l��m
	        
	        ByteBuffer vbb6 = ByteBuffer.allocateDirect(vertices[0].length*4);
	        vbb6.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
	        mVertexBuffer6 = vbb6.asFloatBuffer();//�ରint���w�R
	        mVertexBuffer6.put(vertices[5]);//�V�w�R�Ϥ���J���I�y�и��
	        mVertexBuffer6.position(0);//�]�w�w�R�ϰ_�l��m
	        
	        ByteBuffer vbb7 = ByteBuffer.allocateDirect(vertices[0].length*4);
	        vbb7.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
	        mVertexBuffer7 = vbb7.asFloatBuffer();//�ରint���w�R
	        mVertexBuffer7.put(vertices[6]);//�V�w�R�Ϥ���J���I�y�и��
	        mVertexBuffer7.position(0);//�]�w�w�R�ϰ_�l��m
	        
	        ByteBuffer vbb8 = ByteBuffer.allocateDirect(vertices[0].length*4);
	        vbb8.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
	        mVertexBuffer8 = vbb8.asFloatBuffer();//�ରint���w�R
	        mVertexBuffer8.put(vertices[7]);//�V�w�R�Ϥ���J���I�y�и��
	        mVertexBuffer8.position(0);//�]�w�w�R�ϰ_�l��m
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
	
	public void drawSelf(GL10 gl,float x,float y,float angle,GameView gameview)
    {  
        gl.glPushMatrix();
        float p[]=From2DTo3DUtil.point3D(x,y);
        float z=0;
        gl.glTranslatef(p[0],p[1] , z);
        gl.glRotatef(angle, 0, 0, 1);
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone10);   
//	        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId);   
        //���z===========end==================================================
        //ø��ϧ�
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 
        		0, 
        		vCount
        );

        //�W������ø���
        
        //�}�lø��e����
        gl.glVertexPointer
        (
        		3,				//�C�ӳ��I���y�мƶq��3  xyz 
        		GL10.GL_FLOAT,	//���I�y�ЭȪ����A�� GL_FIXED
        		0, 				//�s���I�y�и�Ƥ��������j
        		mVertexBuffer2	//���I�y�и��
        );
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone1);
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone1);
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
        		mVertexBuffer4	//���I�y�и��
        );
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone1);
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
        		mVertexBuffer6	//���I�y�и��
        );
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone10);
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
        		mVertexBuffer7	//���I�y�и��
        );
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone1);
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
        		mVertexBuffer8	//���I�y�и��
        );
        gl.glBindTexture(GL10.GL_TEXTURE_2D, gameview.textureId_stone1);
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 
        		0, 
        		vCount
        );
        gl.glPopMatrix();
        
    }
	
	public static float[][] vertices(float width,float height)
	{
		float[][] vertices=new float[8][18];

		vertices[0][0]=0 ;
		vertices[0][1]=0 ;
		vertices[0][2]=Constant.Z_R/3;
		
		vertices[0][3]=0 ;
		vertices[0][4]=(-height)/(Height/2) ;
		vertices[0][5]=Constant.Z_R/3;
		
		vertices[0][6]=(width)/(Width/2) *RATIO ;
		vertices[0][7]=0;
		vertices[0][8]=Constant.Z_R/3;
		
		vertices[0][9]=(width)/(Width/2)*RATIO  ;
		vertices[0][10]=0 ;
		vertices[0][11]=Constant.Z_R/3;
		
		vertices[0][12]=0 ;
		vertices[0][13]=(-height)/(Height/2);
		vertices[0][14]=Constant.Z_R/3;
		
		vertices[0][15]=(width)/(Width/2) *RATIO ;
		vertices[0][16]=(-height)/(Height/2) ;
		vertices[0][17]=Constant.Z_R/3;
		
		
		vertices[1][0]=0 ;
		vertices[1][1]=0 ;
		vertices[1][2]=-Constant.Z_R/3;
		
		vertices[1][3]=0 ;
		vertices[1][4]=0 ;
		vertices[1][5]=Constant.Z_R/3;
		
		vertices[1][6]=(width)/(Width/2) *RATIO ;
		vertices[1][7]=0;
		vertices[1][8]=-Constant.Z_R/3;
		
		vertices[1][9]=(width)/(Width/2) *RATIO ;
		vertices[1][10]=0 ;
		vertices[1][11]=-Constant.Z_R/3;
		
		vertices[1][12]=0  ;
		vertices[1][13]=0 ;
		vertices[1][14]=Constant.Z_R/3;
		
		vertices[1][15]=(width)/(Width/2)*RATIO  ;
		vertices[1][16]=0 ;
		vertices[1][17]=Constant.Z_R/3;
		
		
		vertices[2][0]=(width)/(Width/2)*RATIO  ;
		vertices[2][1]=(-height)/(Height/2) ;
		vertices[2][2]=Constant.Z_R/3;
		
		vertices[2][3]=(width)/(Width/2)*RATIO  ;
		vertices[2][4]=(-height)/(Height/2) ;
		vertices[2][5]=-Constant.Z_R/3;
		
		vertices[2][6]=(width)/(Width/2)*RATIO  ;
		vertices[2][7]=0;
		vertices[2][8]=Constant.Z_R/3;
		
		vertices[2][9]=(width)/(Width/2) *RATIO ;
		vertices[2][10]=0 ;
		vertices[2][11]=Constant.Z_R/3;
		
		vertices[2][12]=(width)/(Width/2) *RATIO ;
		vertices[2][13]=(-height)/(Height/2) ;
		vertices[2][14]=-Constant.Z_R/3;
		
		vertices[2][15]=(width)/(Width/2)*RATIO  ;
		vertices[2][16]=0 ;
		vertices[2][17]=-Constant.Z_R/3;
		
		
		vertices[3][0]=0  ;
		vertices[3][1]=0 ;
		vertices[3][2]=Constant.Z_R/3;
		
		vertices[3][3]=0 ;
		vertices[3][4]=0 ;
		vertices[3][5]=-Constant.Z_R/3;
		
		vertices[3][6]=0 ;
		vertices[3][7]=(-height)/(Height/2);
		vertices[3][8]=Constant.Z_R/3;
		
		vertices[3][9]=0  ;
		vertices[3][10]=(-height)/(Height/2) ;
		vertices[3][11]=Constant.Z_R/3;
		
		vertices[3][12]=0 ;
		vertices[3][13]=0;
		vertices[3][14]=-Constant.Z_R/3;
		
		vertices[3][15]=0 ;
		vertices[3][16]=(-height)/(Height/2) ;
		vertices[3][17]=-Constant.Z_R/3;
		
		
		vertices[4][0]=0  ;
		vertices[4][1]=(-height)/(Height/2) ;
		vertices[4][2]=Constant.Z_R/3;
		
		vertices[4][3]=0 ;
		vertices[4][4]=(-height)/(Height/2) ;
		vertices[4][5]=-Constant.Z_R/3;
		
		vertices[4][6]=(width)/(Width/2) *RATIO ;
		vertices[4][7]=-height/(Height/2);
		vertices[4][8]=Constant.Z_R/3;
		
		vertices[4][9]=(width)/(Width/2) *RATIO ;
		vertices[4][10]=-height/(Height/2) ;
		vertices[4][11]=Constant.Z_R/3;
		
		vertices[4][12]=0 ;
		vertices[4][13]=(-height)/(Height/2) ;
		vertices[4][14]=-Constant.Z_R/3;
		
		vertices[4][15]=(width)/(Width/2)*RATIO  ;
		vertices[4][16]=(-height)/(Height/2) ;
		vertices[4][17]=-Constant.Z_R/3;
		
		
		vertices[5][0]=(width/3)/(Width/2)*RATIO  ;
		vertices[5][1]=0 ;
		vertices[5][2]=Constant.Z_R/3;
		
		vertices[5][3]=(width/3)/(Width/2)*RATIO  ;
		vertices[5][4]=(-width)/(Height/2) ;
		vertices[5][5]=Constant.Z_R/3;
		
		vertices[5][6]=(width*2/3)/(Width/2)*RATIO  ;
		vertices[5][7]=0;
		vertices[5][8]=Constant.Z_R/3;
		
		vertices[5][9]=(width*2/3)/(Width/2) *RATIO ;
		vertices[5][10]=0 ;
		vertices[5][11]=Constant.Z_R/3;
		
		vertices[5][12]=(width/3)/(Width/2) *RATIO ;
		vertices[5][13]=(-width)/(Height/2) ;
		vertices[5][14]=Constant.Z_R/3;
		
		vertices[5][15]=(width*2/3)/(Width/2)*RATIO  ;
		vertices[5][16]=(-width)/(Height/2) ;
		vertices[5][17]=Constant.Z_R/3;
		
		
		vertices[6][0]=(width/3)/(Width/2)*RATIO  ;
		vertices[6][1]=0 ;
		vertices[6][2]=-Constant.Z_R/3;
		
		vertices[6][3]=(width/3)/(Width/2)*RATIO  ;
		vertices[6][4]=(-width)/(Height/2) ;
		vertices[6][5]=-Constant.Z_R/3;
		
		vertices[6][6]=(width/3)/(Width/2)*RATIO  ;
		vertices[6][7]=0;
		vertices[6][8]=Constant.Z_R/3;
		
		vertices[6][9]=(width/3)/(Width/2) *RATIO ;
		vertices[6][10]=0 ;
		vertices[6][11]=Constant.Z_R/3;
		
		vertices[6][12]=(width/3)/(Width/2) *RATIO ;
		vertices[6][13]=(-width)/(Height/2) ;
		vertices[6][14]=-Constant.Z_R/3;
		
		vertices[6][15]=(width/3)/(Width/2)*RATIO  ;
		vertices[6][16]=(-width)/(Height/2) ;
		vertices[6][17]=Constant.Z_R/3;
		
		vertices[7][0]=(width*2/3)/(Width/2)*RATIO  ;
		vertices[7][1]=0 ;
		vertices[7][2]=Constant.Z_R/3;
		
		vertices[7][3]=(width*2/3)/(Width/2)*RATIO  ;
		vertices[7][4]=(-width)/(Height/2) ;
		vertices[7][5]=Constant.Z_R/3;
		
		vertices[7][6]=(width*2/3)/(Width/2)*RATIO  ;
		vertices[7][7]=0;
		vertices[7][8]=-Constant.Z_R/3;
		
		vertices[7][9]=(width*2/3)/(Width/2) *RATIO ;
		vertices[7][10]=0 ;
		vertices[7][11]=-Constant.Z_R/3;
		
		vertices[7][12]=(width*2/3)/(Width/2) *RATIO ;
		vertices[7][13]=(-width)/(Height/2) ;
		vertices[7][14]=Constant.Z_R/3;
		
		vertices[7][15]=(width*2/3)/(Width/2)*RATIO  ;
		vertices[7][16]=(-width)/(Height/2) ;
		vertices[7][17]=-Constant.Z_R/3;
		
		return vertices;
	}

}
