package com.bn.GL20Tail;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Tail 
{
	final float w=0.1f;
	FloatBuffer   mVertexBufferG;//���I�y�и�ƽw�R
	FloatBuffer   mTexCoorBufferG;//���I���z�y�и�ƽw�R
	FloatBuffer   mAlphaBufferG;//���I�z���׸�ƽw�R
	
	int vCount=0;   
	DrawBuffer db;
	public List<TailPart> pl=new ArrayList<TailPart>();
	Object lock=new Object();
	
	public Tail(DrawBuffer db)
	{
		this.db=db;
	}
	
	public float[][] getPnArray(TailPart tp)
	{
		float[][] result=null;
		//��l�x�Υ|���I�y��
		float[] p0={-w,0,0,1};
		float[] p1={w,0,0,1};
		float[] p2={w,1,0,1};
		float[] p3={-w,1,0,1};
		float[] sourceV={0,1,0,1};
		
		float[] targetV=new float[3];
		targetV[0]=tp.endPoint[0]-tp.startPoint[0];
		targetV[1]=tp.endPoint[1]-tp.startPoint[1];
		targetV[2]=tp.endPoint[2]-tp.startPoint[2];
		tp.selfVector=VectorUtil.vectorNormal(targetV);
		
		//�p��V�q������
		float angle=(float)Math.toDegrees(VectorUtil.angle(sourceV, targetV));
		tp.angle=angle;
		
		//�p��V�q���e�n
		float[] normal=VectorUtil.getCrossProduct
		(
			sourceV[0], sourceV[1], sourceV[2], 
			targetV[0], targetV[1], targetV[2]
		);
		normal=VectorUtil.vectorNormal(normal);
		tp.normal=normal;
		
		//�p�⦹�u�q�_�l�I�P�ت��I���Z��
		float dis=VectorUtil.mould(targetV);
		tp.length=dis;
		float[] mc=new float[16];
		Matrix.setIdentityM(mc, 0);
		Matrix.translateM(mc, 0, tp.startPoint[0], tp.startPoint[1], tp.startPoint[2]);
		Matrix.rotateM(mc, 0, angle, normal[0], normal[1], normal[2]);
		Matrix.scaleM(mc, 0, 1, dis, 1);
		
		float[] p0a=new float[4];
		float[] p1a=new float[4];
		float[] p2a=new float[4];
		float[] p3a=new float[4];
		
		Matrix.multiplyMV(p0a,0, mc,0, p0, 0);
		Matrix.multiplyMV(p1a,0, mc,0, p1, 0);
		Matrix.multiplyMV(p2a,0, mc,0, p2, 0);
		Matrix.multiplyMV(p3a,0, mc,0, p3, 0);
		
		result=new float[][]
		{
			p0a,p1a,p2a,p3a
        };
		
		return result;
	}
	
	//�_�l�Ƴ��I�y�лP�ۦ��ƪ���ksynchronized
     public void genVertexData()
    {
    	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
    	FloatBuffer   mTexCoorBuffer;//���I���z�y�и�ƽw�R    	
    	FloatBuffer   mAlphaBuffer;//���I�z���׸�ƽw�R
    	
    	vCount=pl.size()*6;
    	float vertices[]=new float[vCount*3];
    	float texCoor[]=new float[vCount*2];
    	float alphaData[]=new float[vCount];
    	int count=0;
    	float[][] pArrayBefore=null;
    	float[][] pArrayAfter=null;
    	float[][] pArray=null;
    	for(int i=0;i<pl.size();i++)
    	{
    		//�C�ӯx�γ��I���G�p�U
    		// 3-----2
    		// | \   |
    		// |   \ |
    		// 0-----1
    		// ���U¶�Ҧ�0-1-3�A1-2-3
    		//�C�ӯx�Ϊ�0���I���W�@�ӯx�Ϊ�3���I
    		//�C�ӯx�Ϊ�1���I���W�@�ӯx�Ϊ�2���I
    		//�C�ӯx�Ϊ�2���I���U�@�ӯx�Ϊ�1���I
    		//�C�ӯx�Ϊ�3���I���U�@�ӯx�Ϊ�0���I
    		//�Ĥ@�ӯx�ΨS���W�a�A�̫�@�ӯx�ΨS���U�a
    		
    		float[] p0c,p1c,p2c,p3c;//�ثe��
			float[] p0n=null;
    		float[] p1n=null;
			@SuppressWarnings("unused")
			float[] p2n;
			@SuppressWarnings("unused")
			float[] p3n; 
    		@SuppressWarnings("unused")
			float[] p0p=null,p1p=null,p2p=null,p3p=null;//�W�@��
    		float[] p0a=null;//���G
    		float[] p1a=null;
    		float[] p2a=null;
    		float[] p3a=null;
    		
    		//�Y�G�O�Ĥ@�q�M�e������
    		if(i==0)
    		{
    			pArray=getPnArray(pl.get(i));
        		
    		}
    		if(i<pl.size()-1)
    		{
    			pArrayAfter=getPnArray(pl.get(i+1));
    		}
    		
    		p0c=pArray[0];
    		p1c=pArray[1];
    		p2c=pArray[2];
    		p3c=pArray[3];
    		//if(pArray!=null)
    		{
    			p0n=pArrayAfter[0];
    		p1n=pArrayAfter[1];
    		p2n=pArrayAfter[2];
    		p3n=pArrayAfter[3];	
    		}
    	
    		
    		if(i!=0)
    		{
    			p0p=pArrayBefore[0];
    			p1p=pArrayBefore[1];
    			p2p=pArrayBefore[2];
    			p3p=pArrayBefore[3];
    		}
    		
    		if(i==0)
    		{
    			p0a=p0c;
        		p1a=p1c;
        		p2a=new float[]
        		{
        			(p2c[0]+p1n[0])/2.0f,
        			(p2c[1]+p1n[1])/2.0f,
        			(p2c[2]+p1n[2])/2.0f,
        		};
        		p3a=new float[]
        		{
        			(p3c[0]+p0n[0])/2.0f,
        			(p3c[1]+p0n[1])/2.0f,
        			(p3c[2]+p0n[2])/2.0f,
            	};
    		}
    		else if(i==pl.size()-1)
    		{
    			p0a=new float[]
        		{
        			(p0c[0]+p3p[0])/2.0f,
        			(p0c[1]+p3p[1])/2.0f,
        			(p0c[2]+p3p[2])/2.0f,
        		};
    			
    			p1a=new float[]
        		{
        			(p1c[0]+p2p[0])/2.0f,
        			(p1c[1]+p2p[1])/2.0f,
        			(p1c[2]+p2p[2])/2.0f,
        		};
    			
    			p2a=p2c;
        		p3a=p3c;
    		}
    		else
    		{
    			p0a=new float[]
        		{
        			(p0c[0]+p3p[0])/2.0f,
        			(p0c[1]+p3p[1])/2.0f,
        			(p0c[2]+p3p[2])/2.0f,
        		};
    			
    			p1a=new float[]
        		{
        			(p1c[0]+p2p[0])/2.0f,
        			(p1c[1]+p2p[1])/2.0f,
        			(p1c[2]+p2p[2])/2.0f,
        		};
        		p2a=new float[]
        		{
        			(p2c[0]+p1n[0])/2.0f,
        			(p2c[1]+p1n[1])/2.0f,
        			(p2c[2]+p1n[2])/2.0f,
        		};
        		p3a=new float[]
        		{
        			(p3c[0]+p0n[0])/2.0f,
        			(p3c[1]+p0n[1])/2.0f,
        			(p3c[2]+p0n[2])/2.0f,
            	};    			
    		}
    		
    		vertices[count*18+0]=p0a[0];
    		vertices[count*18+1]=p0a[1];
    		vertices[count*18+2]=p0a[2];
    		
    		vertices[count*18+3]=p1a[0];
    		vertices[count*18+4]=p1a[1];
    		vertices[count*18+5]=p1a[2];
    		
    		vertices[count*18+6]=p3a[0];
    		vertices[count*18+7]=p3a[1];
    		vertices[count*18+8]=p3a[2];
    		
    		vertices[count*18+9]=p1a[0];
    		vertices[count*18+10]=p1a[1];
    		vertices[count*18+11]=p1a[2];
    		
    		vertices[count*18+12]=p2a[0];
    		vertices[count*18+13]=p2a[1];
    		vertices[count*18+14]=p2a[2];
    		
    		vertices[count*18+15]=p3a[0];
    		vertices[count*18+16]=p3a[1];
    		vertices[count*18+17]=p3a[2];
    		
    		
    		texCoor[count*12+0]=0;
    		texCoor[count*12+1]=1;
    		texCoor[count*12+2]=1;
    		texCoor[count*12+3]=1;
    		texCoor[count*12+4]=0;
    		texCoor[count*12+5]=0;
    		
    		texCoor[count*12+6]=1;
    		texCoor[count*12+7]=1;
    		texCoor[count*12+8]=1;
    		texCoor[count*12+9]=0;
    		texCoor[count*12+10]=0;
    		texCoor[count*12+11]=0;
    		
    		
    		 
    			float alphaTemp=pl.get(i).alpha;
        		alphaData[count*6+0]=alphaTemp;
        		alphaData[count*6+1]=alphaTemp;
        		alphaData[count*6+2]=alphaTemp;
        		alphaData[count*6+3]=alphaTemp;
        		alphaData[count*6+4]=alphaTemp;
        		alphaData[count*6+5]=alphaTemp;
    		 
    		
    	
    		
    		count++;
    		
    		pArrayBefore=pArray;
    		pArray=pArrayAfter;
    	}
    	
    	//���I�y�и�ƪ��_�l��================begin===============================
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରFloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I�y�и�ƪ��_�l��================end=================================
        
        //���I���z�y�и�ƪ��_�l��================begin============================ 
        //�إ߳��I���z�y�и�ƽw�R
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTexCoorBuffer = cbb.asFloatBuffer();//�ରFloat���w�R
        mTexCoorBuffer.put(texCoor);//�V�w�R�Ϥ���J���I�ۦ���
        mTexCoorBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I���z�y�и�ƪ��_�l��================end============================
        
        
        ByteBuffer abb = ByteBuffer.allocateDirect(alphaData.length*4);
        abb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mAlphaBuffer = abb.asFloatBuffer();//�ରFloat���w�R
        mAlphaBuffer.put(alphaData);//�V�w�R�Ϥ���J���I�ۦ���
        mAlphaBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
        synchronized(lock)
        {
        	mVertexBufferG=mVertexBuffer;//���I�y�и�ƽw�R
        	mTexCoorBufferG=mTexCoorBuffer;//���I���z�y�и�ƽw�R
        	mAlphaBufferG=mAlphaBuffer;
        }
    }    
	
	public void drawSelf(int texId)
	{       
		FloatBuffer   mVertexBuffer=null;//���I�y�и�ƽw�R
    	FloatBuffer   mTexCoorBuffer=null;//���I���z�y�и�ƽw�R
    	FloatBuffer   mAlphaBuffer=null;//���I�z���׸�ƽw�R
        synchronized(lock)
        {
        	mVertexBuffer=mVertexBufferG;//���I�y�и�ƽw�R
        	mTexCoorBuffer=mTexCoorBufferG;//���I���z�y�и�ƽw�R
        	mAlphaBuffer=mAlphaBufferG;
        }
        if(mVertexBuffer!=null&&mTexCoorBufferG!=null)
        {
        	GLES20.glEnable(GLES20.GL_BLEND);
        	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        	db.drawSelf(texId, mVertexBuffer, mTexCoorBuffer,mAlphaBuffer, vCount);
        	GLES20.glDisable(GLES20.GL_BLEND);
        }
	}
}
