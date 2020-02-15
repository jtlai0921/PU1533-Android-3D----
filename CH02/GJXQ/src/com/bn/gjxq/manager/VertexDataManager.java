package com.bn.gjxq.manager;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.res.Resources;


//頂點資料管理者
public class VertexDataManager 
{
	public static boolean isLoaded=false;	
	//物體頂點的套數
	//0--房間
	//1--棋碟方塊
	//2--車 
	//3--馬
	//4--相
	//5--後
	//6--王
	//7--兵
	//8--小棋碟
	//9--小棋碟中的方塊
	//10--小棋碟中棋子
	//11--點擊後隱藏或顯示小棋碟的小箭頭
	
	//物體數量  
	public static int count=12;					
	//頂點座標資料緩沖序列
	public static FloatBuffer[] vertexPositionArray=new FloatBuffer[count];
	//頂點紋理座標資料緩沖序列	
	public static FloatBuffer[] vertexTextrueArray=new FloatBuffer[count];
	//頂點法向量座標資料緩沖序列	
	public static FloatBuffer[] vertexNormalArray=new FloatBuffer[count];
	//頂點數量陣列
	public static int[] vCount=new int[count];  
	//包圍盒六參數群組
	public static AABB3[] AABBData=new AABB3[count];
	
	//載入物體頂點位置、紋理座標資料進記憶體緩沖的方法
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
	
	//起始化房間頂點資料
	public static void initRoomVertexData()
	{
		float UNIT_SIZE=2.0f;

		//頂點座標資料
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
    	//房間的頂點數
    	vCount[0]=36;
    	
    	ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[0]=vbb.asFloatBuffer();
    	vertexPositionArray[0].put(vertice);
    	vertexPositionArray[0].position(0);
    	 
    	//房間的包圍盒
    	AABBData[0]=AABB3Util.genAABB3FromVertexPositionData(vertice);
    	
    	//房間的頂點紋理座標
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

    //起始化棋碟方塊頂點資料
	public static void initBlockVertexData()
	{
		float UNIT_SIZE=0.15f;

		//棋碟方塊的頂點座標資料
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
    	//棋碟方塊的頂點數
    	vCount[1]=30;
    	
    	ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
    	vbb.order(ByteOrder.nativeOrder());
    	vertexPositionArray[1]=vbb.asFloatBuffer();
    	vertexPositionArray[1].put(vertice);
    	vertexPositionArray[1].position(0);
    	
    	//棋碟方塊的包圍盒
    	AABBData[1]=AABB3Util.genAABB3FromVertexPositionData(vertice);   	
    	
    	//棋碟方塊頂點紋理座標
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

	//起始化小棋碟頂點資料
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
			vCount[8]=6;//圖形頂點數量
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點座標資料緩沖
			vbb.order(ByteOrder.nativeOrder());//設定位元組順序
			vertexPositionArray[8]=vbb.asFloatBuffer();//轉為Float型緩沖
			vertexPositionArray[8].put(vertices);//向緩沖中放入頂點座標資料
			vertexPositionArray[8].position(0);//設定緩沖起始位置
			
			float []tex= new float[]{//頂點紋理S、T座標值陣列
				0,0,
				0,1,
				1,1,
				
				1,1,
				1,0,
				0,0
		
			};
			ByteBuffer tbb=ByteBuffer.allocateDirect(tex.length*4);//建立頂點紋理資料緩沖
			tbb.order(ByteOrder.nativeOrder());//設定位元組順序
			vertexTextrueArray[8]=tbb.asFloatBuffer();//換為Float型緩沖
			vertexTextrueArray[8].put(tex);//向緩沖中放入頂點紋理資料
			vertexTextrueArray[8].position(0);//設定起始位置
			
	}

	//起始化小棋碟中方塊頂點資料
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
			vCount[9]=6;//圖形頂點數量
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點座標資料緩沖
			vbb.order(ByteOrder.nativeOrder());//設定位元組順序
			vertexPositionArray[9]=vbb.asFloatBuffer();//轉為Float型緩沖
			vertexPositionArray[9].put(vertices);//向緩沖中放入頂點座標資料
			vertexPositionArray[9].position(0);//設定緩沖起始位置
			
			float []tex= new float[]{//頂點紋理S、T座標值陣列
				0,0,
				0,1,
				1,1,
				
				1,1,
				1,0,
				0,0
		
			};
			ByteBuffer tbb=ByteBuffer.allocateDirect(tex.length*4);//建立頂點紋理資料緩沖
			tbb.order(ByteOrder.nativeOrder());//設定位元組順序
			vertexTextrueArray[9]=tbb.asFloatBuffer();//換為Float型緩沖
			vertexTextrueArray[9].put(tex);//向緩沖中放入頂點紋理資料
			vertexTextrueArray[9].position(0);//設定起始位置
	}
	
	//起始化小棋碟中棋子的頂點資料
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
			vCount[10]=6;//圖形頂點數量
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點座標資料緩沖
			vbb.order(ByteOrder.nativeOrder());//設定位元組順序
			vertexPositionArray[10]=vbb.asFloatBuffer();//轉為Float型緩沖
			vertexPositionArray[10].put(vertices);//向緩沖中放入頂點座標資料
			vertexPositionArray[10].position(0);//設定緩沖起始位置
			
			float []tex= new float[]{//頂點紋理S、T座標值陣列
				0,0,
				0,1,
				1,1,
				
				1,1,
				1,0,
				0,0
		
			};
			ByteBuffer tbb=ByteBuffer.allocateDirect(tex.length*4);//建立頂點紋理資料緩沖
			tbb.order(ByteOrder.nativeOrder());//設定位元組順序
			vertexTextrueArray[10]=tbb.asFloatBuffer();//換為Float型緩沖
			vertexTextrueArray[10].put(tex);//向緩沖中放入頂點紋理資料
			vertexTextrueArray[10].position(0);//設定起始位置
	}
	//點擊後隱藏或顯示小棋碟的小箭頭的頂點資料
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
			vCount[11]=6;//圖形頂點數量
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//建立頂點座標資料緩沖
			vbb.order(ByteOrder.nativeOrder());//設定位元組順序
			vertexPositionArray[11]=vbb.asFloatBuffer();//轉為Float型緩沖
			vertexPositionArray[11].put(vertices);//向緩沖中放入頂點座標資料
			vertexPositionArray[11].position(0);//設定緩沖起始位置
			
			float []tex= new float[]{//頂點紋理S、T座標值陣列
				0,0,
				0,1,
				1,1,
				
				1,1,
				1,0,
				0,0
		
			};
			ByteBuffer tbb=ByteBuffer.allocateDirect(tex.length*4);//建立頂點紋理資料緩沖
			tbb.order(ByteOrder.nativeOrder());//設定位元組順序
			vertexTextrueArray[11]=tbb.asFloatBuffer();//換為Float型緩沖
			vertexTextrueArray[11].put(tex);//向緩沖中放入頂點紋理資料
			vertexTextrueArray[11].position(0);//設定起始位置
			
	}
}
