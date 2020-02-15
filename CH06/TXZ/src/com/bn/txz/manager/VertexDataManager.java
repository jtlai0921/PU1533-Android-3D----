package com.bn.txz.manager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.bn.txz.Constant;

import android.content.res.Resources;


//頂點資料管理者
public class VertexDataManager 
{
	public static boolean isLoaded=false;	
	//物體頂點的套數
	//0--房間
	//1--頭
	//2--身體
	//3--左手臂上
	//4--左手臂下
	//5--右手臂上
	//6--右手臂下
	//7--水
	//8--虛擬按鈕矩形
	//9--小方塊
	//10--載入界面背景
	//11--載入界面進度指示器矩形
	//12--載入界面中load文字矩形
	//14--贏界面矩形
	//15--輸贏界面中按鈕
	//16--選單界面背景矩形
	//24--星空
	//25--小星空
	//26--軟體箱子
	//27--關於界面矩形
	//28--外層搖桿
	//29--內層搖桿
	//30--幫助背景矩形
	//31--幫助按鈕矩形
	//32--天空
	  
	
	
	//物體數量  
	public static int count=33;					
	//頂點座標資料緩沖序列
	public static FloatBuffer[] vertexPositionArray=new FloatBuffer[count];
	//頂點紋理座標資料緩沖序列	
	public static FloatBuffer[] vertexTextrueArray=new FloatBuffer[count];
	//頂點著色資料的緩沖序列(星空)
	public static IntBuffer[] vertexColorArray=new IntBuffer[count];
	//頂點法向量座標資料緩沖序列	
	public static FloatBuffer[] vertexNormalArray=new FloatBuffer[count];
	//頂點數量陣列
	public static int[] vCount=new int[count];  
	
	//載入物體頂點位置、紋理座標資料進記憶體緩沖的方法
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
	
	//載入物體頂點位置、紋理座標資料進記憶體緩沖的方法
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
	
	//起始化房間頂點資料
	public static void initRoomVertexData()
	{
		float UNIT_SIZE=4f;

		//頂點座標資料
    	float vertice[]=new float[]
        {  
    	        2*UNIT_SIZE,2*UNIT_SIZE, -3.464f*UNIT_SIZE, //側面
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
    	//房間的頂點數
    	vCount[0]=54;
    	
    	ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[0]=vbb.asFloatBuffer();
    	vertexPositionArray[0].put(vertice);
    	vertexPositionArray[0].position(0);
    	 
    	
    	//房間的頂點紋理座標
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

	
	//起始化天空
	public static void initSky()
	{
		float UNIT_SIZE=8f;
		//頂點座標資料
    	float vertice[]=new float[]
        {  
    	         0*UNIT_SIZE,2*UNIT_SIZE,0*UNIT_SIZE,//頂面
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
    	//房間的頂點數
    	vCount[32]=18;
    	
    	ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[32]=vbb.asFloatBuffer();
    	vertexPositionArray[32].put(vertice);
    	vertexPositionArray[32].position(0);
    	 
    	
    	//房間的頂點紋理座標
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
	
	//起始化水面
	public static void initWaterVertexData()
	{
		float UNIT_SIZE=10f;	
		
		float y=0.01f;//水面高度
		//頂點座標資料
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
    	
    	//紋理陣列
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

	//起始化虛擬按鈕頂點資料
	public static void initButtonVertexData()
	{
		float UNIT_SIZE=0.25f;
		//頂點座標資料
    	float vertice[]=new float[]
    	{
    			-1*UNIT_SIZE,1*UNIT_SIZE,0*UNIT_SIZE,//逆時針
    	        -1*UNIT_SIZE,-1*UNIT_SIZE,0*UNIT_SIZE,
    	        1*UNIT_SIZE, -1*UNIT_SIZE,0*UNIT_SIZE,
    	           
    	        1*UNIT_SIZE,-1*UNIT_SIZE,0*UNIT_SIZE,//順時針
    	        1*UNIT_SIZE,1*UNIT_SIZE,0*UNIT_SIZE,
    	        -1*UNIT_SIZE,1*UNIT_SIZE,  0*UNIT_SIZE
    	}; 
    	vCount[8]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[8]=vbb.asFloatBuffer();
    	vertexPositionArray[8].put(vertice);
    	vertexPositionArray[8].position(0);
    	
    	//紋理陣列
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

	//起始化小方塊，橋、箱子、目的都有小方塊群組成，箱子與小方塊等大
	public static void initSmallBoxVertexData()
	{
		float UNIT_SIZE=0.5f;
		//頂點座標資料
    	float vertice[]=new float[]
	      {
    			-1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,//前
    			1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			-1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,
    			1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			
    			1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,//後
    			-1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,
    			-1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,//左
    			-1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			-1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,
    			-1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,
    			-1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			
    			1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,//右
    			1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			
    			1*UNIT_SIZE,0*UNIT_SIZE,1*UNIT_SIZE,
    			1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,//頂
    			1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			-1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,1*UNIT_SIZE,
    			1*UNIT_SIZE,2*UNIT_SIZE,-1*UNIT_SIZE,
    			
    			-1*UNIT_SIZE,0*UNIT_SIZE,-1*UNIT_SIZE,//底
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
    	
    	//頂點法向量資料起始化================begin============================
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
        nbb.order(ByteOrder.nativeOrder());//設定位元組順序
        vertexNormalArray[9] = nbb.asFloatBuffer();//轉為int型緩沖
        vertexNormalArray[9].put(normals);//向緩沖區中放入頂點著色資料
        vertexNormalArray[9].position(0);//設定緩沖區起始位置
        //頂點法向量資料起始化================end============================ 
    	
    	//房間的頂點紋理座標
    	float Texturecood[]=new float[]
	      {
    			0,1,//前
    			1,0,
    			0,0,
    			
    			0,1,
    			1,1,
    			1,0,
    			
    			0,1,//後
    			1,1,
    			0,0,
    			
    			1,1,
    			1,0,
    			0,0,
    			
    			0,1,//左
    			1,0,
    			0,0,
    			
    			0,1,
    			1,1,
    			1,0,
    			
    			0,1,//右
    			1,0,
    			0,0,
    			
    			0,1,
    			1,1,
    			1,0,
    			
    			0,1,//頂
    			1,0,
    			0,0,
    			
    			0,1,
    			1,1,
    			1,0,
    			
    			0,1,//底
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
	

	//起始化載入進度指示器矩形頂點資料
	public static void initLoadProgressVertexData()
	{
		float UNIT_SIZE=2f;
		//頂點座標資料
    	float vertice[]=new float[]
    	{
    			-1*UNIT_SIZE,0.1f,0*UNIT_SIZE,//逆時針
    	        -1*UNIT_SIZE,-0.1f,0*UNIT_SIZE,
    	        1*UNIT_SIZE, -0.1f,0*UNIT_SIZE,
    	           
    	        1*UNIT_SIZE,-0.1f,0*UNIT_SIZE,//順時針
    	        1*UNIT_SIZE,0.1f,0*UNIT_SIZE,
    	        -1*UNIT_SIZE,0.1f,  0*UNIT_SIZE
    	}; 
    	vCount[11]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[11]=vbb.asFloatBuffer();
    	vertexPositionArray[11].put(vertice);
    	vertexPositionArray[11].position(0);
    	
    	//紋理陣列
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

	//起始化載入背景矩形頂點資料
	public static void initLoadWordVertexData()
	{
		float UNIT_SIZE=2f;
		//頂點座標資料
    	float vertice[]=new float[]
    	{
    			-1*UNIT_SIZE,0.1f*UNIT_SIZE,0*UNIT_SIZE,//逆時針
    	        -1*UNIT_SIZE,-0.1f*UNIT_SIZE,0*UNIT_SIZE,
    	        1*UNIT_SIZE, -0.1f*UNIT_SIZE,0*UNIT_SIZE,
    	           
    	        1*UNIT_SIZE,-0.1f*UNIT_SIZE,0*UNIT_SIZE,//順時針
    	        1*UNIT_SIZE,0.1f*UNIT_SIZE,0*UNIT_SIZE,
    	        -1*UNIT_SIZE,0.1f*UNIT_SIZE,  0*UNIT_SIZE
    	}; 
    	vCount[12]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[12]=vbb.asFloatBuffer();
    	vertexPositionArray[12].put(vertice);
    	vertexPositionArray[12].position(0);
    	
    	//紋理陣列
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


	 //起始化頂點座標資料的方法
    public static void initWinVertexData()
    {
    	//頂點座標資料的起始化================begin============================
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
		
        //建立頂點座標資料緩沖
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        vertexPositionArray[14] = vbb.asFloatBuffer();//轉為Float型緩沖
        vertexPositionArray[14].put(vertices);//向緩沖區中放入頂點座標資料
        vertexPositionArray[14].position(0);//設定緩沖區起始位置
        
        float texCoor[]=new float[]//頂點彩色值陣列，每個頂點4個色彩值RGBA
        {
        		0,0, 0,1, 1,1,
        		1,1, 1,0, 0,0
        };        
        //建立頂點紋理座標資料緩沖
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        vertexTextrueArray[14] = cbb.asFloatBuffer();//轉為Float型緩沖
        vertexTextrueArray[14].put(texCoor);//向緩沖區中放入頂點著色資料
        vertexTextrueArray[14].position(0);//設定緩沖區起始位置
    }
    
  //起始化頂點座標資料的方法
    public static void initWinButtonVertexData()
    {
    	//頂點座標資料的起始化================begin============================
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
		
        //建立頂點座標資料緩沖
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        vertexPositionArray[15] = vbb.asFloatBuffer();//轉為Float型緩沖
        vertexPositionArray[15].put(vertices);//向緩沖區中放入頂點座標資料
        vertexPositionArray[15].position(0);//設定緩沖區起始位置
        
        float texCoor[]=new float[]//頂點彩色值陣列，每個頂點4個色彩值RGBA
        {
        		0,0, 0,1, 1,1,
        		1,1, 1,0, 0,0
        };        
        //建立頂點紋理座標資料緩沖
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        vertexTextrueArray[15] = cbb.asFloatBuffer();//轉為Float型緩沖
        vertexTextrueArray[15].put(texCoor);//向緩沖區中放入頂點著色資料
        vertexTextrueArray[15].position(0);//設定緩沖區起始位置
    }
    
  //起始化載入背景矩形頂點資料
	public static void initProgressBackVertexData()
	{
		float UNIT_SIZE=1.8f;
		//頂點座標資料
    	float vertice[]=new float[]
    	{
    			-1*UNIT_SIZE,0.57f*UNIT_SIZE,0*UNIT_SIZE,//逆時針
    	        -1*UNIT_SIZE,-0.57f*UNIT_SIZE,0*UNIT_SIZE,
    	        1*UNIT_SIZE, -0.57f*UNIT_SIZE,0*UNIT_SIZE,
    	           
    	        1*UNIT_SIZE,-0.57f*UNIT_SIZE,0*UNIT_SIZE,//順時針
    	        1*UNIT_SIZE,0.57f*UNIT_SIZE,0*UNIT_SIZE,
    	        -1*UNIT_SIZE,0.57f*UNIT_SIZE,  0*UNIT_SIZE
    	}; 
    	vCount[22]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[22]=vbb.asFloatBuffer();
    	vertexPositionArray[22].put(vertice);
    	vertexPositionArray[22].position(0);
    	
    	//紋理陣列
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
	
	//起始化星空頂點
	public static void initBigCelestialVertexData()
	{
		int count=60;
		//頂點座標資料的起始化================begin=======================================       
        float vertice[]=new float[count*3];
        for(int i=0;i<count;i++)
        {
        	//隨機產生每個星星的xyz座標
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
    	
    	//頂點著色資料的起始化================begin============================
        final int one = 65535;
        int[] colors=new int[count*4];//頂點彩色值陣列，每個頂點4個色彩值RGBA
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
	//起始化星空頂點
	public static void initSmallCelestialVertexData()
	{
		int count=250;
		//頂點座標資料的起始化================begin=======================================       
        float vertice[]=new float[count*3];
        for(int i=0;i<count;i++)
        {
        	//隨機產生每個星星的xyz座標
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
    	
    	//頂點著色資料的起始化================begin============================
        final int one = 65535;
        int[] colors=new int[count*4];//頂點彩色值陣列，每個頂點4個色彩值RGBA
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

	//起始化軟體箱子頂點
    public static void initSoftBoxVertexData(double Angle)
    {
    	final float Y_MAX=1f;
        final float Y_MIN=0f;
        final int FD=4;
        final float hw=0.5f;
        final float s=1;
    	
    	//頂點座標資料的起始化================begin============================
        vCount[26]=FD*4*6+2*6;//FD是將高分成的分數   6是每個面六個頂點   5是長方體看見的5個面

        float vertices[]=new float[vCount[26]*3];
        float texCoor[]=new float[vCount[26]*2];
        float yStart=Y_MIN;
        float ySpan=(Y_MAX-Y_MIN)/FD;//這是每一份y的差值
        float allySpan=Y_MAX-Y_MIN;
        int vCount=0;
        int tCount=0;
        float ySpant=s/FD;//紋理分份
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
		
        //建立頂點座標資料緩沖
        //vertices.length*4是因為一個整數四個位元組
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設定位元組順序
        vertexPositionArray[26] = vbb.asFloatBuffer();//轉為Float型緩沖
        vertexPositionArray[26].put(vertices);//向緩沖區中放入頂點座標資料
        vertexPositionArray[26].position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點座標資料的起始化================end============================
        
        //頂點紋理座標資料的起始化================begin============================       
        //建立頂點紋理座標資料緩沖
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//設定位元組順序
        vertexTextrueArray[26] = cbb.asFloatBuffer();//轉為Float型緩沖
        vertexTextrueArray[26].put(texCoor);//向緩沖區中放入頂點著色資料
        vertexTextrueArray[26].position(0);//設定緩沖區起始位置
        //特別提示：由於不同平台位元組順序不同資料單元不是位元組的一定要經由ByteBuffer
        //轉換，關鍵是要透過ByteOrder設定nativeOrder()，否則有可能會出問題
        //頂點紋理座標資料的起始化================end============================

    }
    
    //起始化載入背景矩形頂點資料
	public static void initAboutVertexData()
	{
		float UNIT_SIZE=1.5f;
		//頂點座標資料
    	float vertice[]=new float[]
    	{
    			-0.8f*UNIT_SIZE,0.2f*UNIT_SIZE,0*UNIT_SIZE,//逆時針
    	        -0.8f*UNIT_SIZE,-0.2f*UNIT_SIZE,0*UNIT_SIZE,
    	        0.8f*UNIT_SIZE, -0.2f*UNIT_SIZE,0*UNIT_SIZE,
    	           
    	        0.8f*UNIT_SIZE,-0.2f*UNIT_SIZE,0*UNIT_SIZE,//順時針
    	        0.8f*UNIT_SIZE,0.2f*UNIT_SIZE,0*UNIT_SIZE,
    	        -0.8f*UNIT_SIZE,0.2f*UNIT_SIZE,  0*UNIT_SIZE
    	}; 
    	vCount[27]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[27]=vbb.asFloatBuffer();
    	vertexPositionArray[27].put(vertice);
    	vertexPositionArray[27].position(0);
    	
    	//紋理陣列
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
	
	//起始化搖桿外層的頂點
    public static void initYaogan1VertexData()
    {
    	float UNIT_SIZE=Constant.YAOGAN_UNIT_SIZE;
		//頂點座標資料
    	float vertice[]=new float[]
    	{
    			1*UNIT_SIZE,-1*UNIT_SIZE,0*UNIT_SIZE,//逆時針
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
    	
    	//紋理陣列
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
    
    //起始化搖桿內層的頂點
    public static void initYaogan2VertexData()
    {
    	float UNIT_SIZE=Constant.YAOGAN_UNIT_SIZE/2;
		//頂點座標資料
    	float vertice[]=new float[]
    	{
    			1*UNIT_SIZE,-1*UNIT_SIZE,0*UNIT_SIZE,//逆時針
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
    	
    	//紋理陣列
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

//    //起始化幫助背景矩形的頂點
//    public static void initHelpBackVertexData()
//    {
//    	float UNIT_SIZE=1.8f;
//		//頂點座標資料
//    	float vertice[]=new float[]
//    	{
//    			-1*UNIT_SIZE,0.57f*UNIT_SIZE,0*UNIT_SIZE,//逆時針
//    	        -1*UNIT_SIZE,-0.57f*UNIT_SIZE,0*UNIT_SIZE,
//    	        1*UNIT_SIZE, -0.57f*UNIT_SIZE,0*UNIT_SIZE,
//    	           
//    	        1*UNIT_SIZE,-0.57f*UNIT_SIZE,0*UNIT_SIZE,//順時針
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
//    	//紋理陣列
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
    //起始化幫助按鈕的頂點
    public static void initHelpButtonVertexData()
    {
    	float UNIT_SIZE=0.35f;
		//頂點座標資料
    	float vertice[]=new float[]
    	{
    			-1f*UNIT_SIZE,-0.5f*UNIT_SIZE,0*UNIT_SIZE,//逆時針
    	        1f*UNIT_SIZE,0.5f*UNIT_SIZE,0*UNIT_SIZE,
    	        -1f*UNIT_SIZE, 0.5f*UNIT_SIZE,0*UNIT_SIZE,
    	           
    	        -1f*UNIT_SIZE,-0.5f*UNIT_SIZE,0*UNIT_SIZE,//順時針
    	        1f*UNIT_SIZE,-0.5f*UNIT_SIZE,0*UNIT_SIZE,
    	        1f*UNIT_SIZE,0.5f*UNIT_SIZE,0*UNIT_SIZE
    	}; 
    	vCount[31]=6;
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[31]=vbb.asFloatBuffer();
    	vertexPositionArray[31].put(vertice);
    	vertexPositionArray[31].position(0);
    	
    	//紋理陣列
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
