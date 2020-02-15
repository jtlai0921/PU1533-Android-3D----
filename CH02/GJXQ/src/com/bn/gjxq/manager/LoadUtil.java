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
	//�qobj�ɮפ����J��a���I�B���z�T����3D����
    public static void loadFromFileVertexTexture(String fname, Resources r,int no)
    {
    	//��l���I�y�вM��--�����qobj�ɮפ����J
    	ArrayList<Float> alv=new ArrayList<Float>();
    	
    	//���I�s�ո˭����޲M��--�ھڭ����T���q�ɮפ����J
    	ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();
    	
    	//���G���I�y�вM��--�����s��´�n
    	ArrayList<Float> alvResult=new ArrayList<Float>();   	
    	
    	//��l���z�y�вM��
    	ArrayList<Float> alt=new ArrayList<Float>(); 
    	
    	//���z�y�е��G�M��
    	ArrayList<Float> altResult=new ArrayList<Float>();  
    	
    	try
    	{
    		InputStream in=r.getAssets().open(fname);
    		InputStreamReader isr=new InputStreamReader(in);
    		BufferedReader br=new BufferedReader(isr);
    		String temps=null;
    		
    		//�����ɮסA�ھڦ櫬�A�����P���椣�P���B�z�޿�
		    while((temps=br.readLine())!=null) 
		    {
		    	//�ΪŮ���Φ椤���U�Ӹs�զ�����
		    	String[] tempsa=temps.split("[ ]+");
		      	if(tempsa[0].trim().equals("v"))//���欰���I�y��
		      	{
		      	    //�Y�����I�y�Ц�h���R�X�����I��XYZ�y�зs�W���l���I�y�вM�椤
		      		alv.add(Float.parseFloat(tempsa[1]));
		      		alv.add(Float.parseFloat(tempsa[2]));
		      		alv.add(Float.parseFloat(tempsa[3]));
		      	}
		      	else if(tempsa[0].trim().equals("vt"))//���欰���z�y�Ц�
		      	{
		      		//�Y�����z�y�Ц�h���RST�y�Шå[�J�i��l���z�y�вM�椤
		      		float s=Float.parseFloat(tempsa[1]);
		      		float t=Float.parseFloat(tempsa[2]);
		      		
		      		float sF=s;
		      		float tF=1.0f-t;
		      		
		      		alt.add(sF);		  
		      		alt.add(tF); 		          		        		
		      	}
		      	else if(tempsa[0].trim().equals("f"))  //���欰�T���έ�   
		      	{
		      		/*
		      		 *�Y���T���έ���h�ھ� �s�զ��������I�����ޱq��l���I�y�вM�椤
		      		 *���R���������I�y�Эȷs�W�쵲�G���I�y�вM�椤�A�P�ɮھڤT��
		      		 *���I���y�Эp��X�������k�V�q�÷s�W�쥭���e�U�ӯ��޹������I
		      		 *���k�V�q���X�s�զ���Map��
		      		*/
		      		
		      		int[] index=new int[3];//�T�ӳ��I���ޭȪ��}�C
		      		
		      		//�p���0�ӳ��I�����ޡA�è��o�����I��XYZ�T�Ӯy��	      		
		      		index[0]=Integer.parseInt(tempsa[1].split("/")[0])-1;
		      		float x0=alv.get(3*index[0]);  
		      		float y0=alv.get(3*index[0]+1);
		      		float z0=alv.get(3*index[0]+2);
		      		alvResult.add(x0);
		      		alvResult.add(y0);
		      		alvResult.add(z0);		
		      		
		      	    //�p���1�ӳ��I�����ޡA�è��o�����I��XYZ�T�Ӯy��	  
		      		index[1]=Integer.parseInt(tempsa[2].split("/")[0])-1;
		      		float x1=alv.get(3*index[1]);
		      		float y1=alv.get(3*index[1]+1);
		      		float z1=alv.get(3*index[1]+2);
		      		alvResult.add(x1);
		      		alvResult.add(y1);
		      		alvResult.add(z1);
		      		
		      	    //�p���2�ӳ��I�����ޡA�è��o�����I��XYZ�T�Ӯy��	
		      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
		      		float x2=alv.get(3*index[2]);
		      		float y2=alv.get(3*index[2]+1);
		      		float z2=alv.get(3*index[2]+2);
		      		alvResult.add(x2);
		      		alvResult.add(y2); 
		      		alvResult.add(z2);	
		      		
		      		//�O�����������I����
		      		alFaceIndex.add(index[0]);
		      		alFaceIndex.add(index[1]);
		      		alFaceIndex.add(index[2]);		      		
		      		
		      		//�N���z�y�иs��´�쵲�G���z�y�вM�椤
		      		//��0�ӳ��I�����z�y��
		      		int indexTex=Integer.parseInt(tempsa[1].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	    //��1�ӳ��I�����z�y��
		      		indexTex=Integer.parseInt(tempsa[2].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	    //��2�ӳ��I�����z�y��
		      		indexTex=Integer.parseInt(tempsa[3].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	}		      		
		    } 
		    
		    //���ͳ��I�}�C
		    int size=alvResult.size();
		    float[] vXYZ=new float[size];
		    for(int i=0;i<size;i++)
		    {
		    	vXYZ[i]=alvResult.get(i);
		    }
		    
		    //���ͯ��z�}�C
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
	    	   	
	    	//�Ѥl���]�� �䤤vXYZ�O���I�}�C
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
    
	//�qobj�ɮפ����J��a���I�T��������A�æ۰ʭp��C�ӳ��I�������k�V�q
    public static void loadFromFileVertexNormalTexture(String fname, Resources r,int no)
    {
    	//��l���I�y�вM��--�����qobj�ɮפ����J
    	ArrayList<Float> alv=new ArrayList<Float>();
    	//���I�s�ո˭����޲M��--�ھڭ����T���q�ɮפ����J
    	ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();
    	//���G���I�y�вM��--�����s��´�n
    	ArrayList<Float> alvResult=new ArrayList<Float>();    	
    	//�����e�U�ӯ��޹������I���k�V�q���XMap
    	//��HashMap��key���I�����ޡA value���I�Ҧb���U�ӭ����k�V�q�����X
    	HashMap<Integer,HashSet<Normal>> hmn=new HashMap<Integer,HashSet<Normal>>();    	
    	//��l���z�y�вM��
    	ArrayList<Float> alt=new ArrayList<Float>();  
    	//���z�y�е��G�M��
    	ArrayList<Float> altResult=new ArrayList<Float>();  
    	
    	try
    	{
    		InputStream in=r.getAssets().open(fname);
    		InputStreamReader isr=new InputStreamReader(in);
    		BufferedReader br=new BufferedReader(isr);
    		String temps=null;
    		
    		//�����ɮסA�ھڦ櫬�A�����P���椣�P���B�z�޿�
		    while((temps=br.readLine())!=null) 
		    {
		    	//�ΪŮ���Φ椤���U�Ӹs�զ�����
		    	String[] tempsa=temps.split("[ ]+");
		      	if(tempsa[0].trim().equals("v"))
		      	{//���欰���I�y��
		      	    //�Y�����I�y�Ц�h���R�X�����I��XYZ�y�зs�W���l���I�y�вM�椤
		      		alv.add(Float.parseFloat(tempsa[1]));
		      		alv.add(Float.parseFloat(tempsa[2]));
		      		alv.add(Float.parseFloat(tempsa[3]));
		      	}
		      	else if(tempsa[0].trim().equals("vt"))
		      	{//���欰���z�y�Ц�
		      		//�Y�����z�y�Ц�h���RST�y�Шå[�J�i��l���z�y�вM�椤		      		
		      		float s=Float.parseFloat(tempsa[1]);
		      		float t=Float.parseFloat(tempsa[2]);
		      		
		      		float sF=s;
		      		float tF=1.0f-t;
		      		
		      		alt.add(sF);		  
		      		alt.add(tF); 	
		      	}
		      	else if(tempsa[0].trim().equals("f")) 
		      	{//���欰�T���έ�
		      		/*
		      		 *�Y���T���έ���h�ھ� �s�զ��������I�����ޱq��l���I�y�вM�椤
		      		 *���R���������I�y�Эȷs�W�쵲�G���I�y�вM�椤�A�P�ɮھڤT��
		      		 *���I���y�Эp��X�������k�V�q�÷s�W�쥭���e�U�ӯ��޹������I
		      		 *���k�V�q���X�s�զ���Map��
		      		*/
		      		
		      		int[] index=new int[3];//�T�ӳ��I���ޭȪ��}�C
		      		
		      		//�p���0�ӳ��I�����ޡA�è��o�����I��XYZ�T�Ӯy��	      		
		      		index[0]=Integer.parseInt(tempsa[1].split("/")[0])-1;
		      		float x0=alv.get(3*index[0]);
		      		float y0=alv.get(3*index[0]+1);
		      		float z0=alv.get(3*index[0]+2);
		      		alvResult.add(x0);
		      		alvResult.add(y0);
		      		alvResult.add(z0);		
		      		
		      	    //�p���1�ӳ��I�����ޡA�è��o�����I��XYZ�T�Ӯy��	  
		      		index[1]=Integer.parseInt(tempsa[2].split("/")[0])-1;
		      		float x1=alv.get(3*index[1]);
		      		float y1=alv.get(3*index[1]+1);
		      		float z1=alv.get(3*index[1]+2);
		      		alvResult.add(x1);
		      		alvResult.add(y1);
		      		alvResult.add(z1);
		      		
		      	    //�p���2�ӳ��I�����ޡA�è��o�����I��XYZ�T�Ӯy��	
		      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
		      		float x2=alv.get(3*index[2]);
		      		float y2=alv.get(3*index[2]+1);
		      		float z2=alv.get(3*index[2]+2);
		      		alvResult.add(x2);
		      		alvResult.add(y2); 
		      		alvResult.add(z2);	
		      		
		      		//�O�����������I����
		      		alFaceIndex.add(index[0]);
		      		alFaceIndex.add(index[1]);
		      		alFaceIndex.add(index[2]);
		      		
		      		//�z�L�T���έ������V�q0-1�A0-2�D�e�n�o�즹�����k�V�q
		      	    //�D0���I��1���I���V�q
		      		float vxa=x1-x0;
		      		float vya=y1-y0;
		      		float vza=z1-z0;
		      	    //�D0���I��2���I���V�q
		      		float vxb=x2-x0;
		      		float vyb=y2-y0;
		      		float vzb=z2-z0;
		      	    //�z�L�D��ӦV�q���e�n�p��k�V�q
		      		float[] vNormal=VectorUtil.getCrossProduct
					      			(
					      					vxa,vya,vza,vxb,vyb,vzb
					      			);
		      	    
		      		for(int tempInxex:index)
		      		{//�O���C�ӯ����I���k�V�q�쥭���e�U�ӯ��޹������I���k�V�q���X�s�զ���Map��
		      			//���o�ثe���޹����I���k�V�q���X
		      			HashSet<Normal> hsn=hmn.get(tempInxex);
		      			if(hsn==null)
		      			{//�Y���X���s�b�h�إ�
		      				hsn=new HashSet<Normal>();
		      			}
		      			//�N���I���k�V�q�s�W�춰�X��
		      			//�ѩ�Normal���O���s�w�q�Fequals��k�A�]���P�˪��k�V�q���|���_�X�{�b���I
		      			//�������k�V�q���X��
		      			hsn.add(new Normal(vNormal[0],vNormal[1],vNormal[2]));
		      			//�N���X��iHsahMap��
		      			hmn.put(tempInxex, hsn);
		      		}
		      		
		      		//�N���z�y�иs��´�쵲�G���z�y�вM�椤
		      		//��0�ӳ��I�����z�y��
		      		int indexTex=Integer.parseInt(tempsa[1].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	    //��1�ӳ��I�����z�y��
		      		indexTex=Integer.parseInt(tempsa[2].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	    //��2�ӳ��I�����z�y��
		      		indexTex=Integer.parseInt(tempsa[3].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	}		      		
		    } 
		    
		    //���ͳ��I�}�C
		    int size=alvResult.size();
		    float[] vXYZ=new float[size];
		    for(int i=0;i<size;i++)
		    {
		    	vXYZ[i]=alvResult.get(i);
		    }
		    
		    //���ͪk�V�q�}�C
		    float[] nXYZ=new float[alFaceIndex.size()*3];
		    int c=0;
		    for(Integer i:alFaceIndex)
		    {
		    	//�ھڥثe�I�����ޱqMap�����X�@�Ӫk�V�q�����X
		    	HashSet<Normal> hsn=hmn.get(i);
		    	//�D�X�����k�V�q
		    	float[] tn=Normal.getAverage(hsn);	
		    	//�N�p��X�������k�V�q�s���k�V�q�}�C��
		    	nXYZ[c++]=tn[0];
		    	nXYZ[c++]=tn[1];
		    	nXYZ[c++]=tn[2];
		    }
		    
		    //���ͯ��z�}�C
		    size=altResult.size();
		    float[] tST=new float[size];
		    for(int i=0;i<size;i++)
		    {
		    	tST[i]=altResult.get(i);
		    }
		    
		    //�p��X���I�ƶq
            VertexDataManager.vCount[no]=vXYZ.length/3;
	    	//���ͳ��I�y�нw�R
	    	ByteBuffer vbb=ByteBuffer.allocateDirect(vXYZ.length*4);
	    	vbb.order(ByteOrder.nativeOrder());
	    	VertexDataManager.vertexPositionArray[no]=vbb.asFloatBuffer();
	    	VertexDataManager.vertexPositionArray[no].put(vXYZ);
	    	VertexDataManager.vertexPositionArray[no].position(0);
	    	   	
	    	//3D���骺�]�� �䤤vXYZ�O���I�}�C
	    	VertexDataManager.AABBData[no]=AABB3Util.genAABB3FromVertexPositionData(vXYZ);
	    	
	    	//���ͳ��I���z�y�нw�R
	    	ByteBuffer cbb=ByteBuffer.allocateDirect(tST.length*4);
	    	cbb.order(ByteOrder.nativeOrder());
	    	VertexDataManager.vertexTextrueArray[no]=cbb.asFloatBuffer();
	    	VertexDataManager.vertexTextrueArray[no].put(tST);
	    	VertexDataManager.vertexTextrueArray[no].position(0);
	    	
	    	//���ͳ��I�k�V�q�w�R
	        ByteBuffer vbn = ByteBuffer.allocateDirect(nXYZ.length*4);
	        vbn.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
	        VertexDataManager.vertexNormalArray[no] = vbn.asFloatBuffer();//�ରFloat���w�R
	        VertexDataManager.vertexNormalArray[no].put(nXYZ);//�V�w�R�Ϥ���J���I�y�и��
	        VertexDataManager.vertexNormalArray[no].position(0);//�]�w�w�R�ϰ_�l��m 
    	}
    	catch(Exception e)
    	{
    		Log.d("load error", "load error");
    		e.printStackTrace();
    	}    
    }
    
}