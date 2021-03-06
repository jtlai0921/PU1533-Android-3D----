package com.bn.gjxq.manager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import android.content.res.Resources;
import android.util.Log;
import java.util.*;

public class LoadUtil 
{	
	//從obj檔案中載入攜帶頂點、紋理訊息的3D物體
    public static void loadFromFileVertexTexture(String fname, Resources r,int no)
    {
    	//原始頂點座標清單--直接從obj檔案中載入
    	ArrayList<Float> alv=new ArrayList<Float>();
    	
    	//頂點群組裝面索引清單--根據面的訊息從檔案中載入
    	ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();
    	
    	//結果頂點座標清單--按面群組織好
    	ArrayList<Float> alvResult=new ArrayList<Float>();   	
    	
    	//原始紋理座標清單
    	ArrayList<Float> alt=new ArrayList<Float>(); 
    	
    	//紋理座標結果清單
    	ArrayList<Float> altResult=new ArrayList<Float>();  
    	
    	try
    	{
    		InputStream in=r.getAssets().open(fname);
    		InputStreamReader isr=new InputStreamReader(in);
    		BufferedReader br=new BufferedReader(isr);
    		String temps=null;
    		
    		//掃面檔案，根據行型態的不同執行不同的處理邏輯
		    while((temps=br.readLine())!=null) 
		    {
		    	//用空格分割行中的各個群組成部分
		    	String[] tempsa=temps.split("[ ]+");
		      	if(tempsa[0].trim().equals("v"))//此行為頂點座標
		      	{
		      	    //若為頂點座標行則分析出此頂點的XYZ座標新增到原始頂點座標清單中
		      		alv.add(Float.parseFloat(tempsa[1]));
		      		alv.add(Float.parseFloat(tempsa[2]));
		      		alv.add(Float.parseFloat(tempsa[3]));
		      	}
		      	else if(tempsa[0].trim().equals("vt"))//此行為紋理座標行
		      	{
		      		//若為紋理座標行則分析ST座標並加入進原始紋理座標清單中
		      		float s=Float.parseFloat(tempsa[1]);
		      		float t=Float.parseFloat(tempsa[2]);
		      		
		      		float sF=s;
		      		float tF=1.0f-t;
		      		
		      		alt.add(sF);		  
		      		alt.add(tF); 		          		        		
		      	}
		      	else if(tempsa[0].trim().equals("f"))  //此行為三角形面   
		      	{
		      		/*
		      		 *若為三角形面行則根據 群組成面的頂點的索引從原始頂點座標清單中
		      		 *分析對應的頂點座標值新增到結果頂點座標清單中，同時根據三個
		      		 *頂點的座標計算出此面的法向量並新增到平均前各個索引對應的點
		      		 *的法向量集合群組成的Map中
		      		*/
		      		
		      		int[] index=new int[3];//三個頂點索引值的陣列
		      		
		      		//計算第0個頂點的索引，並取得此頂點的XYZ三個座標	      		
		      		index[0]=Integer.parseInt(tempsa[1].split("/")[0])-1;
		      		float x0=alv.get(3*index[0]);  
		      		float y0=alv.get(3*index[0]+1);
		      		float z0=alv.get(3*index[0]+2);
		      		alvResult.add(x0);
		      		alvResult.add(y0);
		      		alvResult.add(z0);		
		      		
		      	    //計算第1個頂點的索引，並取得此頂點的XYZ三個座標	  
		      		index[1]=Integer.parseInt(tempsa[2].split("/")[0])-1;
		      		float x1=alv.get(3*index[1]);
		      		float y1=alv.get(3*index[1]+1);
		      		float z1=alv.get(3*index[1]+2);
		      		alvResult.add(x1);
		      		alvResult.add(y1);
		      		alvResult.add(z1);
		      		
		      	    //計算第2個頂點的索引，並取得此頂點的XYZ三個座標	
		      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
		      		float x2=alv.get(3*index[2]);
		      		float y2=alv.get(3*index[2]+1);
		      		float z2=alv.get(3*index[2]+2);
		      		alvResult.add(x2);
		      		alvResult.add(y2); 
		      		alvResult.add(z2);	
		      		
		      		//記錄此面的頂點索引
		      		alFaceIndex.add(index[0]);
		      		alFaceIndex.add(index[1]);
		      		alFaceIndex.add(index[2]);		      		
		      		
		      		//將紋理座標群組織到結果紋理座標清單中
		      		//第0個頂點的紋理座標
		      		int indexTex=Integer.parseInt(tempsa[1].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	    //第1個頂點的紋理座標
		      		indexTex=Integer.parseInt(tempsa[2].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	    //第2個頂點的紋理座標
		      		indexTex=Integer.parseInt(tempsa[3].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	}		      		
		    } 
		    
		    //產生頂點陣列
		    int size=alvResult.size();
		    float[] vXYZ=new float[size];
		    for(int i=0;i<size;i++)
		    {
		    	vXYZ[i]=alvResult.get(i);
		    }
		    
		    //產生紋理陣列
		    size=altResult.size();
		    float[] tST=new float[size];
		    for(int i=0;i<size;i++)
		    {
		    	tST[i]=altResult.get(i);
		    }
		    
		    VertexDataManager.vCount[no]=vXYZ.length/3;
	    	
	    	ByteBuffer vbb=ByteBuffer.allocateDirect(vXYZ.length*4);
	    	vbb.order(ByteOrder.nativeOrder());
	    	VertexDataManager.vertexPositionArray[no]=vbb.asFloatBuffer();
	    	VertexDataManager.vertexPositionArray[no].put(vXYZ);
	    	VertexDataManager.vertexPositionArray[no].position(0);
	    	   	
	    	//棋子的包圍盒 其中vXYZ是頂點陣列
	    	VertexDataManager.AABBData[no]=AABB3Util.genAABB3FromVertexPositionData(vXYZ);
	    	
	    	ByteBuffer cbb=ByteBuffer.allocateDirect(tST.length*4);
	    	cbb.order(ByteOrder.nativeOrder());
	    	VertexDataManager.vertexTextrueArray[no]=cbb.asFloatBuffer();
	    	VertexDataManager.vertexTextrueArray[no].put(tST);
	    	VertexDataManager.vertexTextrueArray[no].position(0);
    	}
    	catch(Exception e)
    	{
    		Log.d("load error", "load error");
    		e.printStackTrace();
    	} 
    }
    
	//從obj檔案中載入攜帶頂點訊息的物體，並自動計算每個頂點的平均法向量
    public static void loadFromFileVertexNormalTexture(String fname, Resources r,int no)
    {
    	//原始頂點座標清單--直接從obj檔案中載入
    	ArrayList<Float> alv=new ArrayList<Float>();
    	//頂點群組裝面索引清單--根據面的訊息從檔案中載入
    	ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();
    	//結果頂點座標清單--按面群組織好
    	ArrayList<Float> alvResult=new ArrayList<Float>();    	
    	//平均前各個索引對應的點的法向量集合Map
    	//此HashMap的key為點的索引， value為點所在的各個面的法向量的集合
    	HashMap<Integer,HashSet<Normal>> hmn=new HashMap<Integer,HashSet<Normal>>();    	
    	//原始紋理座標清單
    	ArrayList<Float> alt=new ArrayList<Float>();  
    	//紋理座標結果清單
    	ArrayList<Float> altResult=new ArrayList<Float>();  
    	
    	try
    	{
    		InputStream in=r.getAssets().open(fname);
    		InputStreamReader isr=new InputStreamReader(in);
    		BufferedReader br=new BufferedReader(isr);
    		String temps=null;
    		
    		//掃面檔案，根據行型態的不同執行不同的處理邏輯
		    while((temps=br.readLine())!=null) 
		    {
		    	//用空格分割行中的各個群組成部分
		    	String[] tempsa=temps.split("[ ]+");
		      	if(tempsa[0].trim().equals("v"))
		      	{//此行為頂點座標
		      	    //若為頂點座標行則分析出此頂點的XYZ座標新增到原始頂點座標清單中
		      		alv.add(Float.parseFloat(tempsa[1]));
		      		alv.add(Float.parseFloat(tempsa[2]));
		      		alv.add(Float.parseFloat(tempsa[3]));
		      	}
		      	else if(tempsa[0].trim().equals("vt"))
		      	{//此行為紋理座標行
		      		//若為紋理座標行則分析ST座標並加入進原始紋理座標清單中		      		
		      		float s=Float.parseFloat(tempsa[1]);
		      		float t=Float.parseFloat(tempsa[2]);
		      		
		      		float sF=s;
		      		float tF=1.0f-t;
		      		
		      		alt.add(sF);		  
		      		alt.add(tF); 	
		      	}
		      	else if(tempsa[0].trim().equals("f")) 
		      	{//此行為三角形面
		      		/*
		      		 *若為三角形面行則根據 群組成面的頂點的索引從原始頂點座標清單中
		      		 *分析對應的頂點座標值新增到結果頂點座標清單中，同時根據三個
		      		 *頂點的座標計算出此面的法向量並新增到平均前各個索引對應的點
		      		 *的法向量集合群組成的Map中
		      		*/
		      		
		      		int[] index=new int[3];//三個頂點索引值的陣列
		      		
		      		//計算第0個頂點的索引，並取得此頂點的XYZ三個座標	      		
		      		index[0]=Integer.parseInt(tempsa[1].split("/")[0])-1;
		      		float x0=alv.get(3*index[0]);
		      		float y0=alv.get(3*index[0]+1);
		      		float z0=alv.get(3*index[0]+2);
		      		alvResult.add(x0);
		      		alvResult.add(y0);
		      		alvResult.add(z0);		
		      		
		      	    //計算第1個頂點的索引，並取得此頂點的XYZ三個座標	  
		      		index[1]=Integer.parseInt(tempsa[2].split("/")[0])-1;
		      		float x1=alv.get(3*index[1]);
		      		float y1=alv.get(3*index[1]+1);
		      		float z1=alv.get(3*index[1]+2);
		      		alvResult.add(x1);
		      		alvResult.add(y1);
		      		alvResult.add(z1);
		      		
		      	    //計算第2個頂點的索引，並取得此頂點的XYZ三個座標	
		      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
		      		float x2=alv.get(3*index[2]);
		      		float y2=alv.get(3*index[2]+1);
		      		float z2=alv.get(3*index[2]+2);
		      		alvResult.add(x2);
		      		alvResult.add(y2); 
		      		alvResult.add(z2);	
		      		
		      		//記錄此面的頂點索引
		      		alFaceIndex.add(index[0]);
		      		alFaceIndex.add(index[1]);
		      		alFaceIndex.add(index[2]);
		      		
		      		//透過三角形面兩個邊向量0-1，0-2求叉積得到此面的法向量
		      	    //求0號點到1號點的向量
		      		float vxa=x1-x0;
		      		float vya=y1-y0;
		      		float vza=z1-z0;
		      	    //求0號點到2號點的向量
		      		float vxb=x2-x0;
		      		float vyb=y2-y0;
		      		float vzb=z2-z0;
		      	    //透過求兩個向量的叉積計算法向量
		      		float[] vNormal=VectorUtil.getCrossProduct
					      			(
					      					vxa,vya,vza,vxb,vyb,vzb
					      			);
		      	    
		      		for(int tempInxex:index)
		      		{//記錄每個索引點的法向量到平均前各個索引對應的點的法向量集合群組成的Map中
		      			//取得目前索引對應點的法向量集合
		      			HashSet<Normal> hsn=hmn.get(tempInxex);
		      			if(hsn==null)
		      			{//若集合不存在則建立
		      				hsn=new HashSet<Normal>();
		      			}
		      			//將此點的法向量新增到集合中
		      			//由於Normal類別重新定義了equals方法，因此同樣的法向量不會重復出現在此點
		      			//對應的法向量集合中
		      			hsn.add(new Normal(vNormal[0],vNormal[1],vNormal[2]));
		      			//將集合放進HsahMap中
		      			hmn.put(tempInxex, hsn);
		      		}
		      		
		      		//將紋理座標群組織到結果紋理座標清單中
		      		//第0個頂點的紋理座標
		      		int indexTex=Integer.parseInt(tempsa[1].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	    //第1個頂點的紋理座標
		      		indexTex=Integer.parseInt(tempsa[2].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	    //第2個頂點的紋理座標
		      		indexTex=Integer.parseInt(tempsa[3].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	}		      		
		    } 
		    
		    //產生頂點陣列
		    int size=alvResult.size();
		    float[] vXYZ=new float[size];
		    for(int i=0;i<size;i++)
		    {
		    	vXYZ[i]=alvResult.get(i);
		    }
		    
		    //產生法向量陣列
		    float[] nXYZ=new float[alFaceIndex.size()*3];
		    int c=0;
		    for(Integer i:alFaceIndex)
		    {
		    	//根據目前點的索引從Map中取出一個法向量的集合
		    	HashSet<Normal> hsn=hmn.get(i);
		    	//求出平均法向量
		    	float[] tn=Normal.getAverage(hsn);	
		    	//將計算出的平均法向量存放到法向量陣列中
		    	nXYZ[c++]=tn[0];
		    	nXYZ[c++]=tn[1];
		    	nXYZ[c++]=tn[2];
		    }
		    
		    //產生紋理陣列
		    size=altResult.size();
		    float[] tST=new float[size];
		    for(int i=0;i<size;i++)
		    {
		    	tST[i]=altResult.get(i);
		    }
		    
		    //計算出頂點數量
            VertexDataManager.vCount[no]=vXYZ.length/3;
	    	//產生頂點座標緩沖
	    	ByteBuffer vbb=ByteBuffer.allocateDirect(vXYZ.length*4);
	    	vbb.order(ByteOrder.nativeOrder());
	    	VertexDataManager.vertexPositionArray[no]=vbb.asFloatBuffer();
	    	VertexDataManager.vertexPositionArray[no].put(vXYZ);
	    	VertexDataManager.vertexPositionArray[no].position(0);
	    	   	
	    	//3D物體的包圍盒 其中vXYZ是頂點陣列
	    	VertexDataManager.AABBData[no]=AABB3Util.genAABB3FromVertexPositionData(vXYZ);
	    	
	    	//產生頂點紋理座標緩沖
	    	ByteBuffer cbb=ByteBuffer.allocateDirect(tST.length*4);
	    	cbb.order(ByteOrder.nativeOrder());
	    	VertexDataManager.vertexTextrueArray[no]=cbb.asFloatBuffer();
	    	VertexDataManager.vertexTextrueArray[no].put(tST);
	    	VertexDataManager.vertexTextrueArray[no].position(0);
	    	
	    	//產生頂點法向量緩沖
	        ByteBuffer vbn = ByteBuffer.allocateDirect(nXYZ.length*4);
	        vbn.order(ByteOrder.nativeOrder());//設定位元組順序
	        VertexDataManager.vertexNormalArray[no] = vbn.asFloatBuffer();//轉為Float型緩沖
	        VertexDataManager.vertexNormalArray[no].put(nXYZ);//向緩沖區中放入頂點座標資料
	        VertexDataManager.vertexNormalArray[no].position(0);//設定緩沖區起始位置 
    	}
    	catch(Exception e)
    	{
    		Log.d("load error", "load error");
    		e.printStackTrace();
    	}    
    }
    
}
