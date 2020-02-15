package com.bn.box;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import org.jbox2d.dynamics.Body;
import com.bn.util.From2DTo3DUtil;
import com.bn.zxl.GameView;

public class Ball extends MyBody{
	private FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
    private FloatBuffer   mTextureBuffer;//���I���z��ƽw�R
    int vCount=0;//���I�ƶq    
	float scale;//�y�b�|
	boolean isStatic ;
	int id;
	public Ball(Body body,GameView gameview,boolean isStatic,int id,float radius)//�غc��
	{
		super(body,gameview);
		this.body=body;
		this.id=id;
		this.isStatic=isStatic;
		scale=From2DTo3DUtil.k2d_3d(radius);//�y�b�|
    	final float angleSpan=11.25f;//�N�y�i�������������
    	//���o������Ϫ����z�}�C
    	float[] texCoorArray= 
         generateTexCoor
    	 (
    			 (int)(360/angleSpan), //���z�Ϥ������C��
    			 (int)(180/angleSpan)  //���z�Ϥ��������
    	);
        int tc=0;//���z�}�C�p�ƾ�
        int ts=texCoorArray.length;//���z�}�C����
    	ArrayList<Float> alVertix=new ArrayList<Float>();//�s���I�y�Ъ�ArrayList
    	ArrayList<Float> alTexture=new ArrayList<Float>();//�s�񯾲z�y�Ъ�ArrayList
        for(float vAngle=90;vAngle>-90;vAngle=vAngle-angleSpan)//������VangleSpan�פ@��
        {
        	for(float hAngle=360;hAngle>0;hAngle=hAngle-angleSpan)//������VangleSpan�פ@��
        	{
        		//���L�����U��@�Ө��׫�p����������I�b�y���W���|��γ��I�y��
        		//�ëغc��Ӹs�զ��|��Ϊ��T����
        		//V1�j���
        		double xozLength=scale*Math.cos(Math.toRadians(vAngle));
        		float x1=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
        		float z1=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
        		float y1=(float)(scale*Math.sin(Math.toRadians(vAngle)));
        		
        		xozLength=scale *Math.cos(Math.toRadians(vAngle-angleSpan));
        		float x2=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
        		float z2=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
        		float y2=(float)(scale *Math.sin(Math.toRadians(vAngle-angleSpan)));
        		
        		xozLength=scale *Math.cos(Math.toRadians(vAngle-angleSpan));
        		float x3=(float)(xozLength*Math.cos(Math.toRadians(hAngle-angleSpan)));
        		float z3=(float)(xozLength*Math.sin(Math.toRadians(hAngle-angleSpan)));
        		float y3=(float)(scale *Math.sin(Math.toRadians(vAngle-angleSpan)));
        		
        		xozLength=scale *Math.cos(Math.toRadians(vAngle));
        		float x4=(float)(xozLength*Math.cos(Math.toRadians(hAngle-angleSpan)));
        		float z4=(float)(xozLength*Math.sin(Math.toRadians(hAngle-angleSpan)));
        		float y4=(float)(scale *Math.sin(Math.toRadians(vAngle)));  
        		
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
        //�إ߳��I�k�V�q��ƽw�R
        ByteBuffer nbb = ByteBuffer.allocateDirect(vertices.length*4);
        nbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
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
	public void drawSelf(GL10 gl)
    {
        gl.glPushMatrix();
    	float p[]=From2DTo3DUtil.point3D(body.getPosition().x,body.getPosition().y);
    	gl.glTranslatef(p[0],p[1] , 0);
        gl.glRotatef((float) (-body.getAngle()*180/Math.PI), 0, 0, 1);
        
        int textureId;
        if(isStatic)
        {
        	textureId=gameview.textureId_di;
        }
        else
        {
        	
        	if(id==1)
        	{
        		textureId=gameview.textureId_stone10;
        	}
        	else if(id==-1)
        	{
        		textureId=gameview.textureId_zhadan;
        	}
        	else
        	{
        		textureId=gameview.textureId_cebi;
        	}
        	
        }
        
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
	
//�۰ʤ������z���ͯ��z�}�C����k
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

