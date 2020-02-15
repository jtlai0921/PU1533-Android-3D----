package com.bn.ball;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

import static com.bn.ball.Constant.*;


//�Ω�ø��y
public class Ball {   
	private FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
    private FloatBuffer mTextureBuffer;//���I���z��ƽw�R

    int vCount=0;//���I�ƶq
    float scale;//�ؤo
    int textureID;
    
    public Ball(float scale,int textureID) 
    {
    	this.textureID=textureID;
    	this.scale=scale;    	
    	
    	//���o������Ϫ����z�}�C
    	float[] texCoorArray= 
         generateTexCoor
    	 (
    			 (int)(360/ANGLE_SPAN), //���z�Ϥ������C��
    			 (int)(180/ANGLE_SPAN)  //���z�Ϥ��������
    	 );
    	
    	ArrayList<Float> alVertix=new ArrayList<Float>();//�s���I�y�Ъ�ArrayList
    	
        for(float vAngle=90;vAngle>-90;vAngle=vAngle-ANGLE_SPAN)//������VangleSpan�פ@��
        {
        	for(float hAngle=360;hAngle>0;hAngle=hAngle-ANGLE_SPAN)//������VangleSpan�פ@��
        	{
        		//���L�����U��@�Ө��׫�p����������I�b�y���W���|��γ��I�y��
        		//�ëغc��Ӹs�զ��|��Ϊ��T����
        		
        		//0-----1
        		//|  \  |
        		//3-----2
        		
        		double r=scale*UNIT_SIZE;
        		//�p�|��Ϊ���0�ӳ��I  
        		float x0=(float)(r*Math.cos(Math.toRadians(vAngle))*Math.cos(Math.toRadians(hAngle)));
        		float z0=(float)(r*Math.cos(Math.toRadians(vAngle))*Math.sin(Math.toRadians(hAngle)));
        		float y0=(float)(r*Math.sin(Math.toRadians(vAngle)));
        		
        		//�p�|��Ϊ���1�ӳ��I        		
        		float x1=(float)(r*Math.cos(Math.toRadians(vAngle))*Math.cos(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float z1=(float)(r*Math.cos(Math.toRadians(vAngle))*Math.sin(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float y1=(float)(r*Math.sin(Math.toRadians(vAngle)));
        		
        		//�p�|��Ϊ���2�ӳ��I        		
        		float x2=(float)(r*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN))*Math.cos(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float z2=(float)(r*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN))*Math.sin(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float y2=(float)(r*Math.sin(Math.toRadians(vAngle-ANGLE_SPAN)));
        		
        		//�p�|��Ϊ���3�ӳ��I        		
        		float x3=(float)(r*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN))*Math.cos(Math.toRadians(hAngle)));
        		float z3=(float)(r*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN))*Math.sin(Math.toRadians(hAngle)));
        		float y3=(float)(r*Math.sin(Math.toRadians(vAngle-ANGLE_SPAN)));
        		
        		//�غc�Ĥ@�T����0-2-1
        		alVertix.add(x0);alVertix.add(y0);alVertix.add(z0);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);          		
        		      		
        		//�غc�ĤG�T����0-3-2        		
        		alVertix.add(x0);alVertix.add(y0);alVertix.add(z0);
        		alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);        		
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2); 
        	}
        } 	
        
        vCount=alVertix.size()/3;//���I���ƶq���y�Эȼƶq��1/3�A�]���@�ӳ��I��3�Ӯy��
    	
        //�NalVertix�����y�Э���s��@��float�}�C��
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
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoorArray.length*4);
        tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTextureBuffer = tbb.asFloatBuffer();//�ରint���w�R
        mTextureBuffer.put(texCoorArray);//�V�w�R�Ϥ���J���I�ۦ���
        mTextureBuffer.position(0);//�]�w�w�R�ϰ_�l��m
    }

    public void drawSelf(GL10 gl)
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
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
        //ø��ϧ�
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//�H�T���μҦ���R
        		0, 			 			//�}�l�I�s��
        		vCount					//���I�ƶq
        );        
    }
    
    //�۰ʤ������z���ͯ��z�}�C����k
    public float[] generateTexCoor(int bw,int bh)
    {
    	//0-----1
		//|  \  |
		//3-----2    	
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
    			
    			result[c++]=s+sizew;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			
    			result[c++]=s;
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
