package lwz.com.tank.tail;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import lwz.com.tank.game.Bullet;
import lwz.com.tank.game.MySurfaceView;
import lwz.com.tank.game.OtherSurfaceView;
import lwz.com.tank.game.TempBullet;


import android.opengl.Matrix;

public class Tail 
{
	final float w=2f;
	FloatBuffer   mVertexBufferG;//���I�y�и�ƽw�R
	FloatBuffer   mTexCoorBufferG;//���I���z�y�и�ƽw�R
	int vCount=0;   
	DrawBuffer db;
	List<TailPart> pl=new ArrayList<TailPart>();
	Object lock=new Object();
	float TempData[][]=new float[9][3];
	
	TailPart tp1;
	TailPart tp2;
	TailPart tp3;
	TailPart tp4;
	TailPart tp5;
	TailPart tp6;
	TailPart tp7;
	TailPart tp8;
	
	public Tail(DrawBuffer db,float TempData[][])
	{
		this.db=db;
		this.TempData=TempData;
		if(TempData!=null)
		{
			tp1=new TailPart(TempData[0],TempData[1]);
			tp2=new TailPart(TempData[1],TempData[2]);
			tp3=new TailPart(TempData[2],TempData[3]);
			tp4=new TailPart(TempData[3],TempData[4]);
			tp5=new TailPart(TempData[4],TempData[5]);
			tp6=new TailPart(TempData[5],TempData[6]);
			tp7=new TailPart(TempData[6],TempData[7]);
			tp8=new TailPart(TempData[7],TempData[8]); 
			
			tp1.startPoint=TempData[0];
			tp1.endPoint=TempData[1];
			tp2.startPoint=TempData[1];
			tp2.endPoint=TempData[2]; 
			tp3.startPoint=TempData[2];
			tp3.endPoint=TempData[3]; 
			tp4.startPoint=TempData[3];
			tp4.endPoint=TempData[4];
			tp5.startPoint=TempData[4];
			tp5.endPoint=TempData[5];
			tp6.startPoint=TempData[5];
			tp6.endPoint=TempData[6];
			tp7.startPoint=TempData[6];
			tp7.endPoint=TempData[7];
			tp8.startPoint=TempData[7];
			tp8.endPoint=TempData[8]; 
		}
		pl.add(tp1);  
		pl.add(tp2); 
		pl.add(tp3);
		pl.add(tp4); 
		pl.add(tp5);
		pl.add(tp6); 
		pl.add(tp7);
		pl.add(tp8);
		
		if(pl.size()>0)
		{
			genVertexData(); 
		}
		
	}
	public synchronized float[][] getPnArray(TailPart tp)
	{
		float[][] result=null;
		
		float[] p0={-w,0,1,1};
		float[] p1={w,0,0,1};
		float[] p2={w,1,0,1};
		float[] p3={-w,1,0,1};
		float[] sourceV={0,1,0,1};
		
		float[] targetV=new float[3];
		targetV[0]=tp.endPoint[0]-tp.startPoint[0];
		targetV[1]=tp.endPoint[1]-tp.startPoint[1];
		targetV[2]=tp.endPoint[2]-tp.startPoint[2];
		
		//�p��V�q������
		float angle=(float)Math.toDegrees(VectorUtil.angle(sourceV, targetV));
		
		//�p��V�q���e�n
		float[] normal=VectorUtil.getCrossProduct
		(
			sourceV[0], sourceV[1], sourceV[2],
			targetV[0], targetV[1], targetV[2]
		);
		normal=VectorUtil.vectorNormal(normal);
		
		//�p�⦹�u�q�_�l�I�P�ت��I���Z��
		float dis=VectorUtil.mould(targetV);
		
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
	
	//�_�l�Ƴ��I�y�лP�ۦ��ƪ���k
    public void genVertexData()
    {
    	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
    	FloatBuffer   mTexCoorBuffer;//���I���z�y�и�ƽw�R    	
    	
    	vCount=pl.size()*6;
    	float vertices[]=new float[vCount*3];
    	float texCoor[]=new float[vCount*2];
    	int count=0;
    	float[][] pArrayBefore=null;
    	float[][] pArrayAfter=null;
    	float[][] pArray=null;
    	
    	
    	for(int i=0;i<pl.size();i++)
    	{
    		float[] p0c,p1c,p2c,p3c;//�ثe��
    		@SuppressWarnings("unused")
			float[] p0n= null,p1n = null,p2n= null,p3n= null;//�U�@��
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
    		
    			p0n=pArrayAfter[0];
        		p1n=pArrayAfter[1];
        		p2n=pArrayAfter[2];
        		p3n=pArrayAfter[3];
    		
    		
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
    		
    		count++;
    		
    		pArrayBefore=pArray;
    		pArray=pArrayAfter;
    	}
    	
    	//���I�y�и�ƪ��_�l��================begin============================
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରFloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I�y�и�ƪ��_�l��================end============================
        
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
        
        synchronized(lock)
        {
        	mVertexBufferG=mVertexBuffer;//���I�y�и�ƽw�R
        	mTexCoorBufferG=mTexCoorBuffer;//���I���z�y�и�ƽw�R
        }
    }    
	
	public void drawSelf(int texId,GL10 gl)
	{       
		FloatBuffer   mVertexBuffer=null;//���I�y�и�ƽw�R
    	FloatBuffer   mTexCoorBuffer=null;//���I���z�y�и�ƽw�R
        synchronized(lock)
        {
        	mVertexBuffer=mVertexBufferG;//���I�y�и�ƽw�R
        	mTexCoorBuffer=mTexCoorBufferG;//���I���z�y�и�ƽw�R
        }
        if(mVertexBuffer!=null&&mTexCoorBuffer!=null)
        {
        	 db.drawSelf(texId,gl, mVertexBuffer, mTexCoorBuffer, vCount);
        }
       
	}
}
