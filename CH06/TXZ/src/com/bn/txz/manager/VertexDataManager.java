package com.bn.txz.manager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.bn.txz.Constant;

import android.content.res.Resources;


//���I��ƺ޲z��
public class VertexDataManager 
{
	public static boolean isLoaded=false;	
	//���鳻�I���M��
	//0--�ж�
	//1--�Y
	//2--����
	//3--�����u�W
	//4--�����u�U
	//5--�k���u�W
	//6--�k���u�U
	//7--��
	//8--�������s�x��
	//9--�p���
	//10--���J�ɭ��I��
	//11--���J�ɭ��i�׫��ܾ��x��
	//12--���J�ɭ���load��r�x��
	//14--Ĺ�ɭ��x��
	//15--��Ĺ�ɭ������s
	//16--���ɭ��I���x��
	//24--�P��
	//25--�p�P��
	//26--�n��c�l
	//27--����ɭ��x��
	//28--�~�h�n��
	//29--���h�n��
	//30--���U�I���x��
	//31--���U���s�x��
	//32--�Ѫ�
	  
	
	
	//����ƶq  
	public static int count=33;					
	//���I�y�и�ƽw�R�ǦC
	public static FloatBuffer[] vertexPositionArray=new FloatBuffer[count];
	//���I���z�y�и�ƽw�R�ǦC	
	public static FloatBuffer[] vertexTextrueArray=new FloatBuffer[count];
	//���I�ۦ��ƪ��w�R�ǦC(�P��)
	public static IntBuffer[] vertexColorArray=new IntBuffer[count];
	//���I�k�V�q�y�и�ƽw�R�ǦC	
	public static FloatBuffer[] vertexNormalArray=new FloatBuffer[count];
	//���I�ƶq�}�C
	public static int[] vCount=new int[count];  
	
	//���J���鳻�I��m�B���z�y�и�ƶi�O����w�R����k
	public static void initVertexData( Resources r)
	{
		if(isLoaded)return;	
		initProgressBackVertexData();
		initLoadProgressVertexData();
		initLoadWordVertexData();
		initButtonVertexData();
		initYaogan1VertexData();
		initYaogan2VertexData();
		initWinButtonVertexData();
		initBigCelestialVertexData();
		initSmallCelestialVertexData();
		initAboutVertexData();
		initSmallBoxVertexData();
		initHelpButtonVertexData();
		LoadUtil.loadFromFileVertexNormalTexture("head_l.obj", r, 16);
		LoadUtil.loadFromFileVertexNormalTexture("body_l.obj", r, 17);
		LoadUtil.loadFromFileVertexNormalTexture("left_top_l.obj", r, 18);
		LoadUtil.loadFromFileVertexNormalTexture("left_bottom_l.obj", r, 19);   
		LoadUtil.loadFromFileVertexNormalTexture("right_top_l.obj", r, 20);
		LoadUtil.loadFromFileVertexNormalTexture("right_bottom_l.obj", r, 21);
		isLoaded=true;
	}
	
	//���J���鳻�I��m�B���z�y�и�ƶi�O����w�R����k
	public static void initVertexData( Resources r,int index)
	{
		isLoaded=false;
		switch(index)
		{
		case 1:
			LoadUtil.loadFromFileVertexNormalTexture("head.obj", r, 1);
			break;
		case 2:
			LoadUtil.loadFromFileVertexNormalTexture("body.obj", r, 2);
			break;
		case 3:
			break;
		case 4:
			LoadUtil.loadFromFileVertexNormalTexture("left_top.obj", r, 3);
			LoadUtil.loadFromFileVertexNormalTexture("left_bottom.obj", r, 4);
			break;
		case 5:
			LoadUtil.loadFromFileVertexNormalTexture("right_top.obj", r, 5);
			LoadUtil.loadFromFileVertexNormalTexture("right_bottom.obj", r, 6);
			break;
		case 6:
			initRoomVertexData();
			initSky();
			initWaterVertexData();
			initWinVertexData();
			break;
		case 7:
		case 8:
			break;
		}
		isLoaded=true;
	}
	
	//�_�l�Ʃж����I���
	public static void initRoomVertexData()
	{
		float UNIT_SIZE=4f;

		//���I�y�и��
    	float vertice[]=new float[]
        {  
    	        2*UNIT_SIZE,2*UNIT_SIZE, -3.464f*UNIT_SIZE, //����
    	        1*UNIT_SIZE,-0*UNIT_SIZE,-1.732f*UNIT_SIZE,
    	        3*UNIT_SIZE,2*UNIT_SIZE,-1.732f*UNIT_SIZE,  
    	          
    	           
    	        1*UNIT_SIZE,-0*UNIT_SIZE,-1.732f*UNIT_SIZE,
    	         2*UNIT_SIZE,-0*UNIT_SIZE,0f*UNIT_SIZE,
    	         3*UNIT_SIZE,2*UNIT_SIZE, -1.732f*UNIT_SIZE,   
    	         
    	           
    	         2*UNIT_SIZE,-0*UNIT_SIZE, 0*UNIT_SIZE,    
    	         4*UNIT_SIZE,2*UNIT_SIZE,0*UNIT_SIZE,
    	         3*UNIT_SIZE,2*UNIT_SIZE, -1.732f*UNIT_SIZE,
    	         
    	         
    	         
    	           
    	           
    	         4*UNIT_SIZE,2*UNIT_SIZE,0*UNIT_SIZE, 
    	         2*UNIT_SIZE,-0*UNIT_SIZE,0*UNIT_SIZE,
    	         3*UNIT_SIZE,  2*UNIT_SIZE, 1.732f*UNIT_SIZE,  
    	           
    	  
    	         2*UNIT_SIZE,  -0*UNIT_SIZE, 0*UNIT_SIZE,   
    	         1*UNIT_SIZE,-0*UNIT_SIZE,1.732f*UNIT_SIZE, 
    	         3*UNIT_SIZE,2*UNIT_SIZE,1.732f*UNIT_SIZE,   
    	          
    	            
    	         1*UNIT_SIZE,-0*UNIT_SIZE,1.732f*UNIT_SIZE,   
    	         2*UNIT_SIZE,2*UNIT_SIZE,3.464f*UNIT_SIZE,
    	         3*UNIT_SIZE,2*UNIT_SIZE,1.732f*UNIT_SIZE,
    	           
    	           
    	           
    	       
    	         2*UNIT_SIZE,2*UNIT_SIZE,3.464f*UNIT_SIZE,
    	         1*UNIT_SIZE,-0*UNIT_SIZE, 1.732f*UNIT_SIZE, 
    	         0*UNIT_SIZE,2*UNIT_SIZE,3.464f*UNIT_SIZE,  
    	          
    	           
    	         1*UNIT_SIZE,-0*UNIT_SIZE,1.732f*UNIT_SIZE,
    	         -1*UNIT_SIZE,-0*UNIT_SIZE,1.732f*UNIT_SIZE,
    	         0*UNIT_SIZE, 2*UNIT_SIZE, 3.464f*UNIT_SIZE,  
    	          
    	           
    	         -1*UNIT_SIZE, -0*UNIT_SIZE, 1.732f*UNIT_SIZE,   
    	         -2*UNIT_SIZE,2*UNIT_SIZE,3.464f*UNIT_SIZE,
    	         0*UNIT_SIZE,2*UNIT_SIZE,3.464f*UNIT_SIZE,
    	         
    	         
    	         
    	           
    	           
    	         -2*UNIT_SIZE,2*UNIT_SIZE,3.464f*UNIT_SIZE,  
    	         -1*UNIT_SIZE,-0*UNIT_SIZE,1.732f*UNIT_SIZE,
    	         -3*UNIT_SIZE,2*UNIT_SIZE, 1.732f*UNIT_SIZE, 
    	           
    	  
    	         -1*UNIT_SIZE,-0*UNIT_SIZE, 1.732f*UNIT_SIZE,  
    	         -2*UNIT_SIZE,-0*UNIT_SIZE,0f*UNIT_SIZE,  
    	         -3*UNIT_SIZE,2*UNIT_SIZE, 1.732f*UNIT_SIZE,   
    	          
    	            
    	         -2*UNIT_SIZE,-0*UNIT_SIZE,0*UNIT_SIZE,   
    	         -4*UNIT_SIZE,2*UNIT_SIZE,0*UNIT_SIZE,
    	         -3*UNIT_SIZE,2*UNIT_SIZE,1.732f*UNIT_SIZE,
    	         
    	         
    	         
    	         
    	         
    	         -4*UNIT_SIZE,2*UNIT_SIZE,0f*UNIT_SIZE,  
    	         -2*UNIT_SIZE,-0*UNIT_SIZE,0*UNIT_SIZE,
    	         -3*UNIT_SIZE,2*UNIT_SIZE, -1.732f*UNIT_SIZE,   
    	           
    	  
    	         -2*UNIT_SIZE,-0*UNIT_SIZE, 0f*UNIT_SIZE,  
    	         -1*UNIT_SIZE,-0*UNIT_SIZE,-1.732f*UNIT_SIZE,  
    	         -3*UNIT_SIZE,2*UNIT_SIZE, -1.732f*UNIT_SIZE,   
    	          
    	            
    	         -1*UNIT_SIZE,-0*UNIT_SIZE,-1.732f*UNIT_SIZE,   
    	         -2*UNIT_SIZE,2*UNIT_SIZE,-3.464f*UNIT_SIZE,
    	         -3*UNIT_SIZE,2*UNIT_SIZE,-1.732f*UNIT_SIZE,
    	         
    	         
    	         
    	         
    	         
    	         -2*UNIT_SIZE,2*UNIT_SIZE,-3.464f*UNIT_SIZE,  
    	         -1*UNIT_SIZE,-0*UNIT_SIZE,-1.732f*UNIT_SIZE,
    	         0*UNIT_SIZE,2*UNIT_SIZE, -3.464f*UNIT_SIZE,  
    	           
    	  
    	         -1*UNIT_SIZE,-0*UNIT_SIZE, -1.732f*UNIT_SIZE,  
    	         1*UNIT_SIZE,-0*UNIT_SIZE,-1.732f*UNIT_SIZE,  
    	         0*UNIT_SIZE,2*UNIT_SIZE, -3.464f*UNIT_SIZE,   
    	          
    	            
    	         1*UNIT_SIZE,-0*UNIT_SIZE,-1.732f*UNIT_SIZE,   
    	         2*UNIT_SIZE,2*UNIT_SIZE,-3.464f*UNIT_SIZE,
    	         0*UNIT_SIZE,2*UNIT_SIZE,-3.464f*UNIT_SIZE,
      };
    	//�ж������I��
    	vCount[0]=54;
    	
    	ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[0]=vbb.asFloatBuffer();
    	vertexPositionArray[0].put(vertice);
    	vertexPositionArray[0].position(0);
    	 
    	
    	//�ж������I���z�y��
    	float Texturecood[]=new float[]
    	{
        		
        		0,0.5f,
        		0.25f,1,
        		0.5f,0.5f,
        		0.25f,1,
        		0.75f,1,
        		0.5f,0.5f,
        		0.75f,1,
        		1,0.5f,
        		0.5f,0.5f,
        		
        		
        		0,0.5f,
        		0.25f,1,
        		0.5f,0.5f,
        		0.25f,1,
        		0.75f,1,
        		0.5f,0.5f,
        		0.75f,1,
        		1,0.5f,
        		0.5f,0.5f,
        		
        		
        		0,0.5f,
        		0.25f,1,
        		0.5f,0.5f,
        		0.25f,1,
        		0.75f,1,
        		0.5f,0.5f,
        		0.75f,1,
        		1,0.5f,
        		0.5f,0.5f,
        		
        		0,0.5f,
        		0.25f,1,
        		0.5f,0.5f,
        		0.25f,1,
        		0.75f,1,
        		0.5f,0.5f,
        		0.75f,1,
        		1,0.5f,
        		0.5f,0.5f,
        		
        		
        		0,0.5f,
        		0.25f,1,
        		0.5f,0.5f,
        		0.25f,1,
        		0.75f,1,
        		0.5f,0.5f,
        		0.75f,1,
        		1,0.5f,
        		0.5f,0.5f,
        		
        		
        		0,0.5f,
        		0.25f,1,
        		0.5f,0.5f,
        		0.25f,1,
        		0.75f,1,
        		0.5f,0.5f,
        		0.75f,1,
        		1,0.5f,
        		0.5f,0.5f,
        		
        		
        		
        		0.5f,0.25f,
        		0.25f,0.5f,
        		0.75f,0.5f,
        		0.5f,0.25f,
        		0.75f,0.5f,
        		1f,0.25f,
        		0.5f,0.25f,
        		1,0.25f,
        		0.75f,0f,
        		
        		
        		0.5f,0.25f,
        		0.75f,0f,
        		0.25f,0f,
        		0.5f,0.25f,
        		0.25f,0f,
        		0f,0.25f,
        		0.5f,0.25f,
        		0f,0.25f,
        		0.25f,0.5f,
    		
    	};
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[0]=cbb.asFloatBuffer();
    	vertexTextrueArray[0].put(Texturecood);
    	vertexTextrueArray[0].position(0);
	}

	
	//�_�l�ƤѪ�
	public static void initSky()
	{
		float UNIT_SIZE=8f;
		//���I�y�и��
    	float vertice[]=new float[]
        {  
    	         0*UNIT_SIZE,2*UNIT_SIZE,0*UNIT_SIZE,//����
      	        -2*UNIT_SIZE,2*UNIT_SIZE,-3.464f*UNIT_SIZE,
      	        2*UNIT_SIZE,2*UNIT_SIZE, -3.464f*UNIT_SIZE,
      	           
      	        0*UNIT_SIZE,2*UNIT_SIZE,0*UNIT_SIZE,
      	        2*UNIT_SIZE,2*UNIT_SIZE,-3.464f*UNIT_SIZE,  
      	        4*UNIT_SIZE,2*UNIT_SIZE, 0*UNIT_SIZE,
      	           
      	        0*UNIT_SIZE,2*UNIT_SIZE,0*UNIT_SIZE,
      	        4*UNIT_SIZE,2*UNIT_SIZE,0*UNIT_SIZE,
      	        2*UNIT_SIZE,2*UNIT_SIZE,3.464f*UNIT_SIZE,
      	         
      	           
      	        0*UNIT_SIZE,2*UNIT_SIZE,0*UNIT_SIZE,
      	        2*UNIT_SIZE,2*UNIT_SIZE,3.464f*UNIT_SIZE,
      	        -2*UNIT_SIZE,2*UNIT_SIZE,3.464f*UNIT_SIZE,
      	          
      	          
      	       0*UNIT_SIZE,2*UNIT_SIZE,0*UNIT_SIZE,  
      	       -2*UNIT_SIZE,2*UNIT_SIZE,3.464f*UNIT_SIZE,
      	       -4*UNIT_SIZE,2*UNIT_SIZE,0*UNIT_SIZE,
      	          
      	           
      	       0*UNIT_SIZE,2*UNIT_SIZE, 0*UNIT_SIZE,
      	       -4*UNIT_SIZE,2*UNIT_SIZE,0*UNIT_SIZE,
      	       -2*UNIT_SIZE,2*UNIT_SIZE, -3.464f*UNIT_SIZE
      };
    	//�ж������I��
    	vCount[32]=18;
    	
    	ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[32]=vbb.asFloatBuffer();
    	vertexPositionArray[32].put(vertice);
    	vertexPositionArray[32].position(0);
    	 
    	
    	//�ж������I���z�y��
    	float Texturecood[]=new float[]
    	{
        		
        		0.5f,0.25f,
        		0.25f,0.5f,
        		0.75f,0.5f,
        		0.5f,0.25f,
        		0.75f,0.5f,
        		1f,0.25f,
        		0.5f,0.25f,
        		1,0.25f,
        		0.75f,0f,
        		
        		
        		0.5f,0.25f,
        		0.75f,0f,
        		0.25f,0f,
        		0.5f,0.25f,
        		0.25f,0f,
        		0f,0.25f,
        		0.5f,0.25f,
        		0f,0.25f,
        		0.25f,0.5f,
    		
    	};
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[32]=cbb.asFloatBuffer();
    	vertexTextrueArray[32].put(Texturecood);
    	vertexTextrueArray[32].position(0);
		
	}
	
	//�_�l�Ƥ���
	public static void initWaterVertexData()
	{
		float UNIT_SIZE=10f;	
		
		float y=0.01f;//��������
		//���I�y�и��
    	float vertice[]=new float[]
    	{
    			-1*UNIT_SIZE,y*UNIT_SIZE,-1*UNIT_SIZE,
    	        -1*UNIT_SIZE,y*UNIT_SIZE,1*UNIT_SIZE,
    	        1*UNIT_SIZE,y*UNIT_SIZE, -1*UNIT_SIZE,
    	           
    	        -1*UNIT_SIZE,y*UNIT_SIZE,1*UNIT_SIZE,
    	        1*UNIT_SIZE,y*UNIT_SIZE,1*UNIT_SIZE,  
    	        1*UNIT_SIZE,y*UNIT_SIZE, -1*UNIT_SIZE
    	}; 
    	vCount[7]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[7]=vbb.asFloatBuffer();
    	vertexPositionArray[7].put(vertice);
    	vertexPositionArray[7].position(0);
    	
    	//���z�}�C
    	float Texturecood[]=new float[]
    	{
    			0,0,
    			0,1,
    			1,0,
    			
    			0,1,
    			1,1,
    			1,0
    	};
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[7]=cbb.asFloatBuffer();
    	vertexTextrueArray[7].put(Texturecood);
    	vertexTextrueArray[7].position(0);
	}

	//�_�l�Ƶ������s���I���
	public static void initButtonVertexData()
	{
		float UNIT_SIZE=0.25f;
		//���I�y�и��
    	float vertice[]=new float[]
    	{
    			-1*UNIT_SIZE,1*UNIT_SIZE,0*UNIT_SIZE,//�f�ɰw
    	        -1*UNIT_SIZE,-1*UNIT_SIZE,0*UNIT_SIZE,
    	        1*UNIT_SIZE, -1*UNIT_SIZE,0*UNIT_SIZE,
    	           
    	        1*UNIT_SIZE,-1*UNIT_SIZE,0*UNIT_SIZE,//���ɰw
    	        1*UNIT_SIZE,1*UNIT_SIZE,0*UNIT_SIZE,
    	        -1*UNIT_SIZE,1*UNIT_SIZE,  0*UNIT_SIZE
    	}; 
    	vCount[8]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[8]=vbb.asFloatBuffer();
    	vertexPositionArray[8].put(vertice);
    	vertexPositionArray[8].position(0);
    	
    	//���z�}�C
    	float Texturecood[]=new float[]
    	{
    			
    			0,0,
				0,1,
				1,1,
				
				1,1,
				1,0,
				0,0
    			
    	};
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[8]=cbb.asFloatBuffer();
    	vertexTextrueArray[8].put(Texturecood);
    	vertexTextrueArray[8].position(0);
	}

	//�_�l�Ƥp����A���B�c�l�B�ت������p����s�զ��A�c�l�P�p������j
	public static void initSmallBoxVertexData()
	{
		float UNIT_SIZE=0.5f;
		//���I�y�и��
    	float vertice[]=new float[]
	      {
    			-1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,//�e
    			1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			-1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,
    			1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			
    			1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,//��
    			-1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,
    			-1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,//��
    			-1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			-1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,
    			-1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,
    			-1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			
    			1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,//�k
    			1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			
    			1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,
    			1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,//��
    			1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			-1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,//��
    			1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,
    			-1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,
    			1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,
    			1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE
	      };
    	
    	vCount[9]=36;
    	ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[9]=vbb.asFloatBuffer();
    	vertexPositionArray[9].put(vertice);
    	vertexPositionArray[9].position(0);
    	
    	//���I�k�V�q��ư_�l��================begin============================
        float normals[]=new float[]
        {
        		0,0,1,
        		0,0,1,
        		0,0,1,
        		
        		0,0,1,
        		0,0,1,
        		0,0,1,
        		
        		0,0,-1,
        		0,0,-1,
        		0,0,-1,
        		
        		0,0,-1,
        		0,0,-1,
        		0,0,-1,
        		
        		-1,0,0,
        		-1,0,0,
        		-1,0,0,
        		
        		-1,0,0,
        		-1,0,0,
        		-1,0,0,
        		
        		1,0,0,
        		1,0,0,
        		1,0,0,
        		
        		1,0,0,
        		1,0,0,
        		1,0,0,
        		
        		0,1,0,
        		0,1,0,
        		0,1,0,
        		
        		0,1,0,
        		0,1,0,
        		0,1,0,
        		
        		0,-1,0,
        		0,-1,0,
        		0,-1,0,
        		
        		0,-1,0,
        		0,-1,0,
        		0,-1,0
        };
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        vertexNormalArray[9] = nbb.asFloatBuffer();//�ରint���w�R
        vertexNormalArray[9].put(normals);//�V�w�R�Ϥ���J���I�ۦ���
        vertexNormalArray[9].position(0);//�]�w�w�R�ϰ_�l��m
        //���I�k�V�q��ư_�l��================end============================ 
    	
    	//�ж������I���z�y��
    	float Texturecood[]=new float[]
	      {
    			0,1,//�e
    			1,0,
    			0,0,
    			
    			0,1,
    			1,1,
    			1,0,
    			
    			0,1,//��
    			1,1,
    			0,0,
    			
    			1,1,
    			1,0,
    			0,0,
    			
    			0,1,//��
    			1,0,
    			0,0,
    			
    			0,1,
    			1,1,
    			1,0,
    			
    			0,1,//�k
    			1,0,
    			0,0,
    			
    			0,1,
    			1,1,
    			1,0,
    			
    			0,1,//��
    			1,0,
    			0,0,
    			
    			0,1,
    			1,1,
    			1,0,
    			
    			0,1,//��
    			1,0,
    			0,0,
    			
    			0,1,
    			1,1,
    			1,0
	      };
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[9]=cbb.asFloatBuffer();
    	vertexTextrueArray[9].put(Texturecood);
    	vertexTextrueArray[9].position(0);
	}
	

	//�_�l�Ƹ��J�i�׫��ܾ��x�γ��I���
	public static void initLoadProgressVertexData()
	{
		float UNIT_SIZE=2f;
		//���I�y�и��
    	float vertice[]=new float[]
    	{
    			-1*UNIT_SIZE,0.1f,0*UNIT_SIZE,//�f�ɰw
    	        -1*UNIT_SIZE,-0.1f,0*UNIT_SIZE,
    	        1*UNIT_SIZE, -0.1f,0*UNIT_SIZE,
    	           
    	        1*UNIT_SIZE,-0.1f,0*UNIT_SIZE,//���ɰw
    	        1*UNIT_SIZE,0.1f,0*UNIT_SIZE,
    	        -1*UNIT_SIZE,0.1f,  0*UNIT_SIZE
    	}; 
    	vCount[11]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[11]=vbb.asFloatBuffer();
    	vertexPositionArray[11].put(vertice);
    	vertexPositionArray[11].position(0);
    	
    	//���z�}�C
    	float Texturecood[]=new float[]
    	{
    			
    			0,0,
				0,1,
				1,1,
				
				1,1,
				1,0,
				0,0
    			
    	};
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[11]=cbb.asFloatBuffer();
    	vertexTextrueArray[11].put(Texturecood);
    	vertexTextrueArray[11].position(0);
	}

	//�_�l�Ƹ��J�I���x�γ��I���
	public static void initLoadWordVertexData()
	{
		float UNIT_SIZE=2f;
		//���I�y�и��
    	float vertice[]=new float[]
    	{
    			-1*UNIT_SIZE,0.1f*UNIT_SIZE,0*UNIT_SIZE,//�f�ɰw
    	        -1*UNIT_SIZE,-0.1f*UNIT_SIZE,0*UNIT_SIZE,
    	        1*UNIT_SIZE, -0.1f*UNIT_SIZE,0*UNIT_SIZE,
    	           
    	        1*UNIT_SIZE,-0.1f*UNIT_SIZE,0*UNIT_SIZE,//���ɰw
    	        1*UNIT_SIZE,0.1f*UNIT_SIZE,0*UNIT_SIZE,
    	        -1*UNIT_SIZE,0.1f*UNIT_SIZE,  0*UNIT_SIZE
    	}; 
    	vCount[12]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[12]=vbb.asFloatBuffer();
    	vertexPositionArray[12].put(vertice);
    	vertexPositionArray[12].position(0);
    	
    	//���z�}�C
    	float Texturecood[]=new float[]
    	{
    			
    			0,0,
				0,1,
				1,1,
				
				1,1,
				1,0,
				0,0
    			
    	};
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[12]=cbb.asFloatBuffer();
    	vertexTextrueArray[12].put(Texturecood);
    	vertexTextrueArray[12].position(0);
	}


	 //�_�l�Ƴ��I�y�и�ƪ���k
    public static void initWinVertexData()
    {
    	//���I�y�и�ƪ��_�l��================begin============================
    	vCount[14]=6;
        float vertices[]=new float[]
        {
        		-1f,0.5f,0,
            	-1f,-0.5f,0,
            	1f,-0.5f,0,
            	
            	1f,-0.5f,0,
            	1f,0.5f,0,
            	-1f,0.5f,0
        };
		
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        vertexPositionArray[14] = vbb.asFloatBuffer();//�ରFloat���w�R
        vertexPositionArray[14].put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        vertexPositionArray[14].position(0);//�]�w�w�R�ϰ_�l��m
        
        float texCoor[]=new float[]//���I�m��Ȱ}�C�A�C�ӳ��I4�Ӧ�m��RGBA
        {
        		0,0, 0,1, 1,1,
        		1,1, 1,0, 0,0
        };        
        //�إ߳��I���z�y�и�ƽw�R
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        vertexTextrueArray[14] = cbb.asFloatBuffer();//�ରFloat���w�R
        vertexTextrueArray[14].put(texCoor);//�V�w�R�Ϥ���J���I�ۦ���
        vertexTextrueArray[14].position(0);//�]�w�w�R�ϰ_�l��m
    }
    
  //�_�l�Ƴ��I�y�и�ƪ���k
    public static void initWinButtonVertexData()
    {
    	//���I�y�и�ƪ��_�l��================begin============================
    	vCount[15]=6;
        float vertices[]=new float[]
        {
        		-0.4f,0.14f,0,
            	-0.4f,-0.14f,0,
            	0.4f,-0.14f,0,
            	
            	0.4f,-0.14f,0,
            	0.4f,0.14f,0,
            	-0.4f,0.14f,0
        };
		
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        vertexPositionArray[15] = vbb.asFloatBuffer();//�ରFloat���w�R
        vertexPositionArray[15].put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        vertexPositionArray[15].position(0);//�]�w�w�R�ϰ_�l��m
        
        float texCoor[]=new float[]//���I�m��Ȱ}�C�A�C�ӳ��I4�Ӧ�m��RGBA
        {
        		0,0, 0,1, 1,1,
        		1,1, 1,0, 0,0
        };        
        //�إ߳��I���z�y�и�ƽw�R
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        vertexTextrueArray[15] = cbb.asFloatBuffer();//�ରFloat���w�R
        vertexTextrueArray[15].put(texCoor);//�V�w�R�Ϥ���J���I�ۦ���
        vertexTextrueArray[15].position(0);//�]�w�w�R�ϰ_�l��m
    }
    
  //�_�l�Ƹ��J�I���x�γ��I���
	public static void initProgressBackVertexData()
	{
		float UNIT_SIZE=1.8f;
		//���I�y�и��
    	float vertice[]=new float[]
    	{
    			-1*UNIT_SIZE,0.57f*UNIT_SIZE,0*UNIT_SIZE,//�f�ɰw
    	        -1*UNIT_SIZE,-0.57f*UNIT_SIZE,0*UNIT_SIZE,
    	        1*UNIT_SIZE, -0.57f*UNIT_SIZE,0*UNIT_SIZE,
    	           
    	        1*UNIT_SIZE,-0.57f*UNIT_SIZE,0*UNIT_SIZE,//���ɰw
    	        1*UNIT_SIZE,0.57f*UNIT_SIZE,0*UNIT_SIZE,
    	        -1*UNIT_SIZE,0.57f*UNIT_SIZE,  0*UNIT_SIZE
    	}; 
    	vCount[22]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[22]=vbb.asFloatBuffer();
    	vertexPositionArray[22].put(vertice);
    	vertexPositionArray[22].position(0);
    	
    	//���z�}�C
    	float Texturecood[]=new float[]
    	{
    			
    			0,0,
				0,1,
				1,1,
				
				1,1,
				1,0,
				0,0
    			
    	};
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[22]=cbb.asFloatBuffer();
    	vertexTextrueArray[22].put(Texturecood);
    	vertexTextrueArray[22].position(0);
	}
	
	//�_�l�ƬP�ų��I
	public static void initBigCelestialVertexData()
	{
		int count=60;
		//���I�y�и�ƪ��_�l��================begin=======================================       
        float vertice[]=new float[count*3];
        for(int i=0;i<count;i++)
        {
        	//�H�����ͨC�ӬP�P��xyz�y��
        	double angleTempJD=Math.PI*2*Math.random();
        	double angleTempWD=Math.PI/2*Math.random();
        	vertice[i*3]=(float)(Constant.UNIT_SIZE*Math.cos(angleTempWD)*Math.sin(angleTempJD));
        	vertice[i*3+1]=(float)(Constant.UNIT_SIZE*Math.sin(angleTempWD));
        	vertice[i*3+2]=(float)(Constant.UNIT_SIZE*Math.cos(angleTempWD)*Math.cos(angleTempJD));
        }
        
        vCount[24]=3*count;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[24]=vbb.asFloatBuffer();
    	vertexPositionArray[24].put(vertice);
    	vertexPositionArray[24].position(0);
    	
    	//���I�ۦ��ƪ��_�l��================begin============================
        final int one = 65535;
        int[] colors=new int[count*4];//���I�m��Ȱ}�C�A�C�ӳ��I4�Ӧ�m��RGBA
        for(int i=0;i<count;i++)
        {
        	colors[i*4]=one;
        	colors[i*4+1]=one;
        	colors[i*4+2]=one;
        	colors[i*4+3]=0;
        }
        ByteBuffer cbb=ByteBuffer.allocateDirect(colors.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexColorArray[24]=cbb.asIntBuffer();
    	vertexColorArray[24].put(colors);
    	vertexColorArray[24].position(0);
	}
	//�_�l�ƬP�ų��I
	public static void initSmallCelestialVertexData()
	{
		int count=250;
		//���I�y�и�ƪ��_�l��================begin=======================================       
        float vertice[]=new float[count*3];
        for(int i=0;i<count;i++)
        {
        	//�H�����ͨC�ӬP�P��xyz�y��
        	double angleTempJD=Math.PI*2*Math.random();
        	double angleTempWD=Math.PI/2*Math.random();
        	vertice[i*3]=(float)(Constant.UNIT_SIZE*Math.cos(angleTempWD)*Math.sin(angleTempJD));
        	vertice[i*3+1]=(float)(Constant.UNIT_SIZE*Math.sin(angleTempWD));
        	vertice[i*3+2]=(float)(Constant.UNIT_SIZE*Math.cos(angleTempWD)*Math.cos(angleTempJD));
        }
        
        vCount[25]=3*count;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[25]=vbb.asFloatBuffer();
    	vertexPositionArray[25].put(vertice);
    	vertexPositionArray[25].position(0);
    	
    	//���I�ۦ��ƪ��_�l��================begin============================
        final int one = 65535;
        int[] colors=new int[count*4];//���I�m��Ȱ}�C�A�C�ӳ��I4�Ӧ�m��RGBA
        for(int i=0;i<count;i++)
        {
        	colors[i*4]=one;
        	colors[i*4+1]=one;
        	colors[i*4+2]=one;
        	colors[i*4+3]=0;
        }
        ByteBuffer cbb=ByteBuffer.allocateDirect(colors.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexColorArray[25]=cbb.asIntBuffer();
    	vertexColorArray[25].put(colors);
    	vertexColorArray[25].position(0);
	}

	//�_�l�Ƴn��c�l���I
    public static void initSoftBoxVertexData(double Angle)
    {
    	final float Y_MAX=1f;
        final float Y_MIN=0f;
        final int FD=4;
        final float hw=0.5f;
        final float s=1;
    	
    	//���I�y�и�ƪ��_�l��================begin============================
        vCount[26]=FD*4*6+2*6;//FD�O�N������������   6�O�C�ӭ����ӳ��I   5�O������ݨ���5�ӭ�

        float vertices[]=new float[vCount[26]*3];
        float texCoor[]=new float[vCount[26]*2];
        float yStart=Y_MIN;
        float ySpan=(Y_MAX-Y_MIN)/FD;//�o�O�C�@��y���t��
        float allySpan=Y_MAX-Y_MIN;
        int vCount=0;
        int tCount=0;
        float ySpant=s/FD;//���z����
        for(int i=0;i<FD;i++)
        {
        	double currAngle=i*ySpan/allySpan*Angle; 
        	float x1=(float)((-hw)*Math.cos(currAngle)-hw*Math.sin(currAngle));
        	float y1=yStart+i*ySpan;
        	float z1=(float)((-hw)*Math.sin(currAngle)+hw*Math.cos(currAngle));
        	
        	float x2=(float)(hw*Math.cos(currAngle)-hw*Math.sin(currAngle));
        	float y2=yStart+i*ySpan;
        	float z2=(float)(hw*Math.sin(currAngle)+hw*Math.cos(currAngle));
        	
        	float x3=(float)(hw*Math.cos(currAngle)-(-hw)*Math.sin(currAngle));
        	float y3=yStart+i*ySpan;
        	float z3=(float)(hw*Math.sin(currAngle)+(-hw)*Math.cos(currAngle));
        	
        	float x4=(float)((-hw)*Math.cos(currAngle)-(-hw)*Math.sin(currAngle));
        	float y4=yStart+i*ySpan;
        	float z4=(float)((-hw)*Math.sin(currAngle)+(-hw)*Math.cos(currAngle));
        	
        	currAngle=(i+1)*ySpan/allySpan*Angle; 
        	float x5=(float)((-hw)*Math.cos(currAngle)-hw*Math.sin(currAngle));
        	float y5=yStart+(i+1)*ySpan;
        	float z5=(float)((-hw)*Math.sin(currAngle)+hw*Math.cos(currAngle));
        	
        	float x6=(float)(hw*Math.cos(currAngle)-hw*Math.sin(currAngle));
        	float y6=yStart+(i+1)*ySpan;
        	float z6=(float)(hw*Math.sin(currAngle)+hw*Math.cos(currAngle));
        	
        	float x7=(float)(hw*Math.cos(currAngle)-(-hw)*Math.sin(currAngle));
        	float y7=yStart+(i+1)*ySpan;
        	float z7=(float)(hw*Math.sin(currAngle)+(-hw)*Math.cos(currAngle));
        	
        	float x8=(float)((-hw)*Math.cos(currAngle)-(-hw)*Math.sin(currAngle));
        	float y8=yStart+(i+1)*ySpan;
        	float z8=(float)((-hw)*Math.sin(currAngle)+(-hw)*Math.cos(currAngle));
        	if(i==0)
        	{
        		vertices[vCount++]=x3;vertices[vCount++]=y3;vertices[vCount++]=z3;
            	vertices[vCount++]=x1;vertices[vCount++]=y1;vertices[vCount++]=z1;
            	vertices[vCount++]=x4;vertices[vCount++]=y4;vertices[vCount++]=z4;
            	
            	vertices[vCount++]=x3;vertices[vCount++]=y3;vertices[vCount++]=z3;
            	vertices[vCount++]=x2;vertices[vCount++]=y2;vertices[vCount++]=z2;
            	vertices[vCount++]=x1;vertices[vCount++]=y1;vertices[vCount++]=z1;
            	
            	texCoor[tCount++]=1;texCoor[tCount++]=1;
            	texCoor[tCount++]=0;texCoor[tCount++]=0;
            	texCoor[tCount++]=0;texCoor[tCount++]=1;
            	
            	texCoor[tCount++]=1;texCoor[tCount++]=1;
            	texCoor[tCount++]=1;texCoor[tCount++]=0;
            	texCoor[tCount++]=0;texCoor[tCount++]=0;
        	}
        	
        	vertices[vCount++]=x2;vertices[vCount++]=y2;vertices[vCount++]=z2;
        	vertices[vCount++]=x5;vertices[vCount++]=y5;vertices[vCount++]=z5;
        	vertices[vCount++]=x1;vertices[vCount++]=y1;vertices[vCount++]=z1;
        	
        	vertices[vCount++]=x2;vertices[vCount++]=y2;vertices[vCount++]=z2;
        	vertices[vCount++]=x6;vertices[vCount++]=y6;vertices[vCount++]=z6;
        	vertices[vCount++]=x5;vertices[vCount++]=y5;vertices[vCount++]=z5;
        	
        	vertices[vCount++]=x3;vertices[vCount++]=y3;vertices[vCount++]=z3;
        	vertices[vCount++]=x6;vertices[vCount++]=y6;vertices[vCount++]=z6;
        	vertices[vCount++]=x2;vertices[vCount++]=y2;vertices[vCount++]=z2;
        	
        	vertices[vCount++]=x3;vertices[vCount++]=y3;vertices[vCount++]=z3;
        	vertices[vCount++]=x7;vertices[vCount++]=y7;vertices[vCount++]=z7;
        	vertices[vCount++]=x6;vertices[vCount++]=y6;vertices[vCount++]=z6;
        	
        	vertices[vCount++]=x4;vertices[vCount++]=y4;vertices[vCount++]=z4;
        	vertices[vCount++]=x7;vertices[vCount++]=y7;vertices[vCount++]=z7;
        	vertices[vCount++]=x3;vertices[vCount++]=y3;vertices[vCount++]=z3;
        	
        	vertices[vCount++]=x4;vertices[vCount++]=y4;vertices[vCount++]=z4;
        	vertices[vCount++]=x8;vertices[vCount++]=y8;vertices[vCount++]=z8;
        	vertices[vCount++]=x7;vertices[vCount++]=y7;vertices[vCount++]=z7;
        	
        	vertices[vCount++]=x1;vertices[vCount++]=y1;vertices[vCount++]=z1;
        	vertices[vCount++]=x8;vertices[vCount++]=y8;vertices[vCount++]=z8;
        	vertices[vCount++]=x4;vertices[vCount++]=y4;vertices[vCount++]=z4;
        	
        	vertices[vCount++]=x1;vertices[vCount++]=y1;vertices[vCount++]=z1;
        	vertices[vCount++]=x5;vertices[vCount++]=y5;vertices[vCount++]=z5;
        	vertices[vCount++]=x8;vertices[vCount++]=y8;vertices[vCount++]=z8;
        	
        	texCoor[tCount++]=1;texCoor[tCount++]=1-i*ySpant;
        	texCoor[tCount++]=0;texCoor[tCount++]=1-(i+1)*ySpant;
        	texCoor[tCount++]=0;texCoor[tCount++]=1-i*ySpant;
        	
        	texCoor[tCount++]=1;texCoor[tCount++]=1-i*ySpant;
        	texCoor[tCount++]=1;texCoor[tCount++]=1-(i+1)*ySpant;
        	texCoor[tCount++]=0;texCoor[tCount++]=1-(i+1)*ySpant;
        	
        	texCoor[tCount++]=1;texCoor[tCount++]=1-i*ySpant;
        	texCoor[tCount++]=0;texCoor[tCount++]=1-(i+1)*ySpant;
        	texCoor[tCount++]=0;texCoor[tCount++]=1-i*ySpant;
        	
        	texCoor[tCount++]=1;texCoor[tCount++]=1-i*ySpant;
        	texCoor[tCount++]=1;texCoor[tCount++]=1-(i+1)*ySpant;
        	texCoor[tCount++]=0;texCoor[tCount++]=1-(i+1)*ySpant;
        	
        	texCoor[tCount++]=1;texCoor[tCount++]=1-i*ySpant;
        	texCoor[tCount++]=0;texCoor[tCount++]=1-(i+1)*ySpant;
        	texCoor[tCount++]=0;texCoor[tCount++]=1-i*ySpant;
        	
        	texCoor[tCount++]=1;texCoor[tCount++]=1-i*ySpant;
        	texCoor[tCount++]=1;texCoor[tCount++]=1-(i+1)*ySpant;
        	texCoor[tCount++]=0;texCoor[tCount++]=1-(i+1)*ySpant;
        	
        	texCoor[tCount++]=1;texCoor[tCount++]=1-i*ySpant;
        	texCoor[tCount++]=0;texCoor[tCount++]=1-(i+1)*ySpant;
        	texCoor[tCount++]=0;texCoor[tCount++]=1-i*ySpant;
        	
        	texCoor[tCount++]=1;texCoor[tCount++]=1-i*ySpant;
        	texCoor[tCount++]=1;texCoor[tCount++]=1-(i+1)*ySpant;
        	texCoor[tCount++]=0;texCoor[tCount++]=1-(i+1)*ySpant;
        	if(i==(FD-1))
        	{
        		vertices[vCount++]=x6;vertices[vCount++]=y6;vertices[vCount++]=z6;
            	vertices[vCount++]=x8;vertices[vCount++]=y8;vertices[vCount++]=z8;
            	vertices[vCount++]=x5;vertices[vCount++]=y5;vertices[vCount++]=z5;
            	
            	vertices[vCount++]=x6;vertices[vCount++]=y6;vertices[vCount++]=z6;
            	vertices[vCount++]=x7;vertices[vCount++]=y7;vertices[vCount++]=z7;
            	vertices[vCount++]=x8;vertices[vCount++]=y8;vertices[vCount++]=z8;
            	
        		texCoor[tCount++]=1;texCoor[tCount++]=1;
            	texCoor[tCount++]=0;texCoor[tCount++]=0;
            	texCoor[tCount++]=0;texCoor[tCount++]=1;
            	
            	texCoor[tCount++]=1;texCoor[tCount++]=1;
            	texCoor[tCount++]=1;texCoor[tCount++]=0;
            	texCoor[tCount++]=0;texCoor[tCount++]=0;
        	}
        }
		
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        vertexPositionArray[26] = vbb.asFloatBuffer();//�ରFloat���w�R
        vertexPositionArray[26].put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        vertexPositionArray[26].position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I�y�и�ƪ��_�l��================end============================
        
        //���I���z�y�и�ƪ��_�l��================begin============================       
        //�إ߳��I���z�y�и�ƽw�R
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        vertexTextrueArray[26] = cbb.asFloatBuffer();//�ରFloat���w�R
        vertexTextrueArray[26].put(texCoor);//�V�w�R�Ϥ���J���I�ۦ���
        vertexTextrueArray[26].position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I���z�y�и�ƪ��_�l��================end============================

    }
    
    //�_�l�Ƹ��J�I���x�γ��I���
	public static void initAboutVertexData()
	{
		float UNIT_SIZE=1.5f;
		//���I�y�и��
    	float vertice[]=new float[]
    	{
    			-0.8f*UNIT_SIZE,0.2f*UNIT_SIZE,0*UNIT_SIZE,//�f�ɰw
    	        -0.8f*UNIT_SIZE,-0.2f*UNIT_SIZE,0*UNIT_SIZE,
    	        0.8f*UNIT_SIZE, -0.2f*UNIT_SIZE,0*UNIT_SIZE,
    	           
    	        0.8f*UNIT_SIZE,-0.2f*UNIT_SIZE,0*UNIT_SIZE,//���ɰw
    	        0.8f*UNIT_SIZE,0.2f*UNIT_SIZE,0*UNIT_SIZE,
    	        -0.8f*UNIT_SIZE,0.2f*UNIT_SIZE,  0*UNIT_SIZE
    	}; 
    	vCount[27]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[27]=vbb.asFloatBuffer();
    	vertexPositionArray[27].put(vertice);
    	vertexPositionArray[27].position(0);
    	
    	//���z�}�C
    	float Texturecood[]=new float[]
    	{
    			
    			0,0,
				0,1,
				1,1,
				
				1,1,
				1,0,
				0,0
    			
    	};
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[27]=cbb.asFloatBuffer();
    	vertexTextrueArray[27].put(Texturecood);
    	vertexTextrueArray[27].position(0);
	}
	
	//�_�l�Ʒn��~�h�����I
    public static void initYaogan1VertexData()
    {
    	float UNIT_SIZE=Constant.YAOGAN_UNIT_SIZE;
		//���I�y�и��
    	float vertice[]=new float[]
    	{
    			1*UNIT_SIZE,-1*UNIT_SIZE,0*UNIT_SIZE,//�f�ɰw
    	        -1*UNIT_SIZE,1*UNIT_SIZE,0*UNIT_SIZE,
    	        -1*UNIT_SIZE, -1*UNIT_SIZE,0*UNIT_SIZE,
    	           
    	        1*UNIT_SIZE,-1*UNIT_SIZE,0*UNIT_SIZE,
    	        1*UNIT_SIZE,1*UNIT_SIZE,0*UNIT_SIZE,
    	        -1*UNIT_SIZE,1*UNIT_SIZE,0*UNIT_SIZE
    	}; 
    	vCount[28]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[28]=vbb.asFloatBuffer();
    	vertexPositionArray[28].put(vertice);
    	vertexPositionArray[28].position(0);
    	
    	//���z�}�C
    	float Texturecood[]=new float[]
    	{
    			
    			1,1,
				0,0,
				0,1,
				
				1,1,
				1,0,
				0,0
    			
    	};
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[28]=cbb.asFloatBuffer();
    	vertexTextrueArray[28].put(Texturecood);
    	vertexTextrueArray[28].position(0);
    }
    
    //�_�l�Ʒn�줺�h�����I
    public static void initYaogan2VertexData()
    {
    	float UNIT_SIZE=Constant.YAOGAN_UNIT_SIZE/2;
		//���I�y�и��
    	float vertice[]=new float[]
    	{
    			1*UNIT_SIZE,-1*UNIT_SIZE,0*UNIT_SIZE,//�f�ɰw
    	        -1*UNIT_SIZE,1*UNIT_SIZE,0*UNIT_SIZE,
    	        -1*UNIT_SIZE, -1*UNIT_SIZE,0*UNIT_SIZE,
    	           
    	        1*UNIT_SIZE,-1*UNIT_SIZE,0*UNIT_SIZE,
    	        1*UNIT_SIZE,1*UNIT_SIZE,0*UNIT_SIZE,
    	        -1*UNIT_SIZE,1*UNIT_SIZE,0*UNIT_SIZE
    	}; 
    	vCount[29]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[29]=vbb.asFloatBuffer();
    	vertexPositionArray[29].put(vertice);
    	vertexPositionArray[29].position(0);
    	
    	//���z�}�C
    	float Texturecood[]=new float[]
    	{
    			
    			1,1,
				0,0,
				0,1,
				
				1,1,
				1,0,
				0,0
    			
    	};
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[29]=cbb.asFloatBuffer();
    	vertexTextrueArray[29].put(Texturecood);
    	vertexTextrueArray[29].position(0);
    }

//    //�_�l�����U�I���x�Ϊ����I
//    public static void initHelpBackVertexData()
//    {
//    	float UNIT_SIZE=1.8f;
//		//���I�y�и��
//    	float vertice[]=new float[]
//    	{
//    			-1*UNIT_SIZE,0.57f*UNIT_SIZE,0*UNIT_SIZE,//�f�ɰw
//    	        -1*UNIT_SIZE,-0.57f*UNIT_SIZE,0*UNIT_SIZE,
//    	        1*UNIT_SIZE, -0.57f*UNIT_SIZE,0*UNIT_SIZE,
//    	           
//    	        1*UNIT_SIZE,-0.57f*UNIT_SIZE,0*UNIT_SIZE,//���ɰw
//    	        1*UNIT_SIZE,0.57f*UNIT_SIZE,0*UNIT_SIZE,
//    	        -1*UNIT_SIZE,0.57f*UNIT_SIZE,  0*UNIT_SIZE
//    	};  
//    	vCount[30]=6;
//        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
//    	vbb.order(ByteOrder.nativeOrder());
//    	vertexPositionArray[30]=vbb.asFloatBuffer();
//    	vertexPositionArray[30].put(vertice);
//    	vertexPositionArray[30].position(0);
//    	
//    	//���z�}�C
//    	float Texturecood[]=new float[]
//    	{
//    			
//    			0,0,
//				0,1,
//				1,1,
//				
//				1,1,
//				1,0,
//				0,0
//    			
//    	};
//    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
//    	cbb.order(ByteOrder.nativeOrder());
//    	vertexTextrueArray[30]=cbb.asFloatBuffer();
//    	vertexTextrueArray[30].put(Texturecood);
//    	vertexTextrueArray[30].position(0);
//    }
    //�_�l�����U���s�����I
    public static void initHelpButtonVertexData()
    {
    	float UNIT_SIZE=0.35f;
		//���I�y�и��
    	float vertice[]=new float[]
    	{
    			-1f*UNIT_SIZE,-0.5f*UNIT_SIZE,0*UNIT_SIZE,//�f�ɰw
    	        1f*UNIT_SIZE,0.5f*UNIT_SIZE,0*UNIT_SIZE,
    	        -1f*UNIT_SIZE, 0.5f*UNIT_SIZE,0*UNIT_SIZE,
    	           
    	        -1f*UNIT_SIZE,-0.5f*UNIT_SIZE,0*UNIT_SIZE,//���ɰw
    	        1f*UNIT_SIZE,-0.5f*UNIT_SIZE,0*UNIT_SIZE,
    	        1f*UNIT_SIZE,0.5f*UNIT_SIZE,0*UNIT_SIZE
    	}; 
    	vCount[31]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[31]=vbb.asFloatBuffer();
    	vertexPositionArray[31].put(vertice);
    	vertexPositionArray[31].position(0);
    	
    	//���z�}�C
    	float Texturecood[]=new float[]
    	{
    			
    			0,1,
				1,0,
				0,0,
				
				0,1,
				1,1,
				1,0
    			
    	};
    	ByteBuffer cbb=ByteBuffer.allocateDirect(Texturecood.length*4);
    	cbb.order(ByteOrder.nativeOrder());
    	vertexTextrueArray[31]=cbb.asFloatBuffer();
    	vertexTextrueArray[31].put(Texturecood);
    	vertexTextrueArray[31].position(0);
    }
}
