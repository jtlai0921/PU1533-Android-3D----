package com.bn.gjxq.manager;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.res.Resources;


//���I��ƺ޲z��
public class VertexDataManager 
{
	public static boolean isLoaded=false;	
	//���鳻�I���M��
	//0--�ж�
	//1--�ѺФ��
	//2--�� 
	//3--��
	//4--��
	//5--��
	//6--��
	//7--�L
	//8--�p�Ѻ�
	//9--�p�ѺФ������
	//10--�p�ѺФ��Ѥl
	//11--�I�������é���ܤp�ѺЪ��p�b�Y
	
	//����ƶq  
	public static int count=12;					
	//���I�y�и�ƽw�R�ǦC
	public static FloatBuffer[] vertexPositionArray=new FloatBuffer[count];
	//���I���z�y�и�ƽw�R�ǦC	
	public static FloatBuffer[] vertexTextrueArray=new FloatBuffer[count];
	//���I�k�V�q�y�и�ƽw�R�ǦC	
	public static FloatBuffer[] vertexNormalArray=new FloatBuffer[count];
	//���I�ƶq�}�C
	public static int[] vCount=new int[count];  
	//�]�򲰤��ѼƸs��
	public static AABB3[] AABBData=new AABB3[count];
	
	//���J���鳻�I��m�B���z�y�и�ƶi�O����w�R����k
	public static void initVertexData( Resources r)
	{
		if(isLoaded)return;		
		initRoomVertexData();
		initBlockVertexData();
		initSmallBoardVertexData();
		initSmallBardBlockVertexData();
		initSmallChessData();
		initJianTouVertexData();
		LoadUtil.loadFromFileVertexNormalTexture("che.obj", r, 2);
		LoadUtil.loadFromFileVertexNormalTexture("ma.obj", r, 3);
		LoadUtil.loadFromFileVertexNormalTexture("xiang.obj", r, 4);
		LoadUtil.loadFromFileVertexNormalTexture("hou.obj", r, 5);
		LoadUtil.loadFromFileVertexNormalTexture("wang.obj", r, 6);
		LoadUtil.loadFromFileVertexNormalTexture("bing.obj", r, 7);
		isLoaded=true;
	}
	
	//�_�l�Ʃж����I���
	public static void initRoomVertexData()
	{
		float UNIT_SIZE=2.0f;

		//���I�y�и��
    	float vertice[]=new float[]
        {  
    			-4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    			4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE, 
    	        -4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,
    	           
    	        -4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    	        4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    	        4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,                 
    	           
    	           -4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,
    	           4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE,
    	           -4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE,
    	         
    	           
    	           -4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,
    	           4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,
    	           4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE, 
    	          
    	          
    	           4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,  
    	           4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,
    	           4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE,
    	          
    	           
    	           4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE, 
    	           4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE, 
    	           4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,           
    	          
    	           
    	           4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    	           -4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,
    	           4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,          
    	          
    	           
    	           4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    	           -4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    	           -4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,           
    	          
    	           
    	           -4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,     
    	           -4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE,
    	           -4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,
    	           
    	           
    	           -4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    	           -4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,
    	           -4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE,          
    	           
    	  
    	           -4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE, 
    	           4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,  
    	           -4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,          
    	          
    	            
    	           -4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE,
    	           4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE,
    	           4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE
    	         
      };
    	//�ж������I��
    	vCount[0]=36;
    	
    	ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[0]=vbb.asFloatBuffer();
    	vertexPositionArray[0].put(vertice);
    	vertexPositionArray[0].position(0);
    	 
    	//�ж����]��
    	AABBData[0]=AABB3Util.genAABB3FromVertexPositionData(vertice);
    	
    	//�ж������I���z�y��
    	float Texturecood[]=new float[]
    	{
    		0,0.68f,
    		1,1,
    		0,1,
    		0,0.68f,
    		1,0.68f,
    		1,1,
    		
    		
    		0,0.68f,
    		1,1,
    		0,1,
    		0,0.68f,
    		1,0.68f,
    		1,1,
    		
    		
    		0,0.68f,
    		1,1,
    		0,1,
    		0,0.68f,
    		1,0.68f,
    		1,1,
    		
    		
    		0,0.68f,
    		1,1,
    		0,1,
    		0,0.68f,
    		1,0.68f,
    		1,1,
    		
    		
    		0,0.68f,
    		1,1,
    		0,1,
    			
    		0,0.68f,
    		1,0.68f,
    		1,1,
    		
    		
    		0,0,
    		1,0.6f,
    		0,0.6f,
    		0,0,
    		1,0,
    		1,0.6f
    		
    	};
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[0]=cbb.asFloatBuffer();
    	vertexTextrueArray[0].put(Texturecood);
    	vertexTextrueArray[0].position(0);
	}

    //�_�l�ƴѺФ�����I���
	public static void initBlockVertexData()
	{
		float UNIT_SIZE=0.15f;

		//�ѺФ�������I�y�и��
    	float vertice[]=new float[]
       {  
    			-4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    			 -4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,
    			4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE, 
    	       
    	           
    	        -4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    	        4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,    
    	        4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    	                     
    	           
    	        -4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,
    	        -4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE,
    	        4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE,
    	        
    	         
    	           
    	        -4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,
    	        4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE, 
    	        4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,
    	       
    	          
    	          
    	        4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,  
    	        4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE,
    	        4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,
    	        
    	          
    	           
    	        4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE, 
    	        4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,  
    	        4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE, 
    	                
    	        4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    	        4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,      
    	        -4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,
    	           
    	        4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    	        -4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,    
    	        -4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    	               
    	        -4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    	        -4*UNIT_SIZE,0*UNIT_SIZE,-4*UNIT_SIZE,
    	        -4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE,
    	        
    	        -4*UNIT_SIZE,4*UNIT_SIZE,-4*UNIT_SIZE,
    	        -4*UNIT_SIZE,0*UNIT_SIZE,4*UNIT_SIZE, 
    	        -4*UNIT_SIZE,4*UNIT_SIZE,4*UNIT_SIZE,
    	         
      };
    	//�ѺФ�������I��
    	vCount[1]=30;
    	
    	ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[1]=vbb.asFloatBuffer();
    	vertexPositionArray[1].put(vertice);
    	vertexPositionArray[1].position(0);
    	
    	//�ѺФ�����]��
    	AABBData[1]=AABB3Util.genAABB3FromVertexPositionData(vertice);   	
    	
    	//�ѺФ�����I���z�y��
    	float Texturecood[]=new float[]
    	{
    		0,0,
    		0,0.5f,
    		1,0.5f,
    		0,0,
    		1,0.5f,
    		1,0,
    		
    		0,0.5f,
    		0,1,
    		1,1,
    		0,0.5f,
    		1,1,
    		1,0.5f,
    		
    		0,0.5f,
    		0,1,
    		1,1,
    		0,0.5f,
    		1,1,
    		1,0.5f,
    		
    		0,0.5f,
    		0,1,
    		1,1,
    		0,0.5f,
    		1,1,
    		1,0.5f,
    		
    		0,0.5f,
    		0,1,
    		1,1,
    		0,0.5f,
    		1,1,
    		1,0.5f,
    	};
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[1]=cbb.asFloatBuffer();
    	vertexTextrueArray[1].put(Texturecood);
    	vertexTextrueArray[1].position(0);
	}

	//�_�l�Ƥp�Ѻг��I���
	public static void initSmallBoardVertexData()
	{
		float []vertices = new float[]{
				-0.55f,0.55f,0,
				-0.55f,-0.55f,0,
				0.55f,-0.55f,0,
				
				0.55f,-0.55f,0,
				0.55f,0.55f,0,
				-0.55f,0.55f,0,
			};
			vCount[8]=6;//�ϧγ��I�ƶq
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//�إ߳��I�y�и�ƽw�R
			vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
			vertexPositionArray[8]=vbb.asFloatBuffer();//�ରFloat���w�R
			vertexPositionArray[8].put(vertices);//�V�w�R����J���I�y�и��
			vertexPositionArray[8].position(0);//�]�w�w�R�_�l��m
			
			float []tex= new float[]{//���I���zS�BT�y�ЭȰ}�C
				0,0,
				0,1,
				1,1,
				
				1,1,
				1,0,
				0,0
		
			};
			ByteBuffer tbb=ByteBuffer.allocateDirect(tex.length*4);//�إ߳��I���z��ƽw�R
			tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
			vertexTextrueArray[8]=tbb.asFloatBuffer();//����Float���w�R
			vertexTextrueArray[8].put(tex);//�V�w�R����J���I���z���
			vertexTextrueArray[8].position(0);//�]�w�_�l��m
			
	}

	//�_�l�Ƥp�ѺФ�������I���
	public static void initSmallBardBlockVertexData()
	{
		float []vertices = new float[]{
				-0.055f,0.055f,0,
				-0.055f,-0.055f,0,
				0.055f,-0.055f,0,
				
				0.055f,-0.055f,0,
				0.055f,0.055f,0,
				-0.055f,0.055f,0,
			};
			vCount[9]=6;//�ϧγ��I�ƶq
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//�إ߳��I�y�и�ƽw�R
			vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
			vertexPositionArray[9]=vbb.asFloatBuffer();//�ରFloat���w�R
			vertexPositionArray[9].put(vertices);//�V�w�R����J���I�y�и��
			vertexPositionArray[9].position(0);//�]�w�w�R�_�l��m
			
			float []tex= new float[]{//���I���zS�BT�y�ЭȰ}�C
				0,0,
				0,1,
				1,1,
				
				1,1,
				1,0,
				0,0
		
			};
			ByteBuffer tbb=ByteBuffer.allocateDirect(tex.length*4);//�إ߳��I���z��ƽw�R
			tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
			vertexTextrueArray[9]=tbb.asFloatBuffer();//����Float���w�R
			vertexTextrueArray[9].put(tex);//�V�w�R����J���I���z���
			vertexTextrueArray[9].position(0);//�]�w�_�l��m
	}
	
	//�_�l�Ƥp�ѺФ��Ѥl�����I���
	public static void initSmallChessData()
	{
		float []vertices = new float[]{
				-0.055f,0.055f,0,
				-0.055f,-0.055f,0,
				0.055f,-0.055f,0,
				
				0.055f,-0.055f,0,
				0.055f,0.055f,0,
				-0.055f,0.055f,0,
			};
			vCount[10]=6;//�ϧγ��I�ƶq
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//�إ߳��I�y�и�ƽw�R
			vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
			vertexPositionArray[10]=vbb.asFloatBuffer();//�ରFloat���w�R
			vertexPositionArray[10].put(vertices);//�V�w�R����J���I�y�и��
			vertexPositionArray[10].position(0);//�]�w�w�R�_�l��m
			
			float []tex= new float[]{//���I���zS�BT�y�ЭȰ}�C
				0,0,
				0,1,
				1,1,
				
				1,1,
				1,0,
				0,0
		
			};
			ByteBuffer tbb=ByteBuffer.allocateDirect(tex.length*4);//�إ߳��I���z��ƽw�R
			tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
			vertexTextrueArray[10]=tbb.asFloatBuffer();//����Float���w�R
			vertexTextrueArray[10].put(tex);//�V�w�R����J���I���z���
			vertexTextrueArray[10].position(0);//�]�w�_�l��m
	}
	//�I�������é���ܤp�ѺЪ��p�b�Y�����I���
	public static void initJianTouVertexData()
	{
		float []vertices = new float[]{
				-0.55f,0.05f,0,
				-0.55f,-0.05f,0,
				0.55f,-0.05f,0,
				
				0.55f,-0.05f,0,
				0.55f,0.05f,0,
				-0.55f,0.05f,0,
			};
			vCount[11]=6;//�ϧγ��I�ƶq
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//�إ߳��I�y�и�ƽw�R
			vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
			vertexPositionArray[11]=vbb.asFloatBuffer();//�ରFloat���w�R
			vertexPositionArray[11].put(vertices);//�V�w�R����J���I�y�и��
			vertexPositionArray[11].position(0);//�]�w�w�R�_�l��m
			
			float []tex= new float[]{//���I���zS�BT�y�ЭȰ}�C
				0,0,
				0,1,
				1,1,
				
				1,1,
				1,0,
				0,0
		
			};
			ByteBuffer tbb=ByteBuffer.allocateDirect(tex.length*4);//�إ߳��I���z��ƽw�R
			tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
			vertexTextrueArray[11]=tbb.asFloatBuffer();//����Float���w�R
			vertexTextrueArray[11].put(tex);//�V�w�R����J���I���z���
			vertexTextrueArray[11].position(0);//�]�w�_�l��m
			
	}
}
