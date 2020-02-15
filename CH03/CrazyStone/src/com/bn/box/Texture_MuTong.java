package com.bn.box;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

import com.bn.util.From2DTo3DUtil;
import com.bn.zxl.GameView;

public class Texture_MuTong 
{
	private FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
    private FloatBuffer mTextureBuffer;//���I���z��ƽw�R

    int vCount=0;//���I�ƶq
    float r;//��W�b�|
    float length;//��W����
    float aspan;//��������
    float lspan;//��������  
    GameView gameview;
    
	public Texture_MuTong(GameView gameview,float radius,float height)
	{
		this.gameview=gameview;
		this.r=From2DTo3DUtil.k2d_3d(radius);
    	this.length=From2DTo3DUtil.k2d_3d(height);
    	this.aspan=15;
    	this.lspan=From2DTo3DUtil.k2d_3d(15);
    	
    	//���o������Ϫ����z�}�C
    	float[] texCoorArray= 
         generateTexCoor
    	(
    			 (int)(360/aspan), //���z�Ϥ������C��
    			 (int)(length/lspan)  //���z�Ϥ��������
    	);
        int tc=0;//���z�}�C�p�ƾ�
        int ts=texCoorArray.length;//���z�}�C����
    	
    	ArrayList<Float> alVertix=new ArrayList<Float>();//�s���I�y�Ъ�ArrayList
    	ArrayList<Float> alTexture=new ArrayList<Float>();//�s�񯾲z�y�Ъ�ArrayList
    	
        for(float tempY=length/2;tempY>-length/2;tempY=tempY-lspan)//������Vlspan���פ@��
        {
        	for(float hAngle=360;hAngle>0;hAngle=hAngle-aspan)//������VangleSpan�פ@��
        	{
        		//���L�����U��@�Ө��׫�p����������I�b�y���W���|��γ��I�y��
        		//�ëغc��Ӹs�զ��|��Ϊ��T����
        		
        		
        		float x1=(float)(r*Math.cos(Math.toRadians(hAngle)));
        		float y1=tempY;
        		float z1=(float)(r*Math.sin(Math.toRadians(hAngle)));
        		
        		float x2=(float)(r*Math.cos(Math.toRadians(hAngle)));
        		float y2=tempY-lspan;
        		float z2=(float)(r*Math.sin(Math.toRadians(hAngle)));
        		
        		float x3=(float)(r*Math.cos(Math.toRadians(hAngle-aspan)));
        		float y3=tempY-lspan;
        		float z3=(float)(r*Math.sin(Math.toRadians(hAngle-aspan)));
        		
        		float x4=(float)(r*Math.cos(Math.toRadians(hAngle-aspan)));
        		float y4=tempY;
        		float z4=(float)(r*Math.sin(Math.toRadians(hAngle-aspan))); 
        		
        		//�غc�Ĥ@�T����
        		alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);        		
        		//�غc�ĤG�T����
        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x3);alVertix.add(y3);alVertix.add(z3); 
        		
        		//�Ĥ@�T����3�ӳ��I��6�ӯ��z�y��
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);        		
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		//�ĤG�T����3�ӳ��I��6�ӯ��z�y��
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);        		
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);       		
        	}
        } 	
        vCount=alVertix.size()/3;//���I���ƶq���y�Эȼƶq��1/3�A�]���@�ӳ��I��3�Ӯy��
    	
        //�NalVertix�����y�Э���s��@��int�}�C��
        float vertices[]=new float[vCount*3];
    	for(int i=0;i<alVertix.size();i++)
    	{
    		vertices[i]=alVertix.get(i);
    	}
        
        //�إ�ø��I��ƽw�R
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରint���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m     
                
        //�إ߯��z�y�нw�R
        float textureCoors[]=new float[alTexture.size()];//���I���z�Ȱ}�C
        for(int i=0;i<alTexture.size();i++) 
        {
        	textureCoors[i]=alTexture.get(i);
        }
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTextureBuffer = tbb.asFloatBuffer();//�ରint���w�R
        mTextureBuffer.put(textureCoors);//�V�w�R�Ϥ���J���I�ۦ���
        mTextureBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
	}
	public void drawSelf(GL10 gl,float angle,float x,float y,int id)
    {
        //�e�\�ϥγ��I�}�C
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, id);
        
        gl.glPushMatrix();
        //ø��ϧ�
        float p[]=From2DTo3DUtil.point3D(x,y);
       
        gl.glTranslatef(p[0], p[1], -From2DTo3DUtil.k2d_3d(15f));
        gl.glRotatef((float) (-angle*180/Math.PI), 0, 0, 1);

        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//�H�T���μҦ���R
        		0, 			 			//�}�l�I�s��
        		vCount					//���I�ƶq
        );        
        gl.glPopMatrix();
    }
	
	public float[] generateTexCoor(int bw,int bh)
    {
    	float[] result=new float[bw*bh*6*2]; 
    	float sizew=1.0f/bw;//�C��
    	float sizeh=1.0f/bh;//���
    	int c=0;
    	for(int i=0;i<bh;i++)
    	{
    		for(int j=0;j<bw;j++)
    		{
    			//�C��C�@�ӯx�ΡA�Ѩ�ӤT���κc���A�@�����I�A12�ӯ��z�y��
    			float s=j*sizew;
    			float t=i*sizeh;
    			
    			result[c++]=s;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t+sizeh;    			
    		}
    	}
    	return result;
    }
}
