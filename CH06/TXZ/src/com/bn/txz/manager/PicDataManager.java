package com.bn.txz.manager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;

//���z�ϸ�ƺ޲z��
public class PicDataManager 
{
	public static boolean isLoaded=false;
	public static boolean idLoadedOther=false;
	public static byte[][] picDataArray=null;
	public static String[] texNameArray=
	{
			"room.jpg",		//�Фl�Ϥ�    				0
			"wall.jpg",		//�s�զ��������z           		1
			"water.png",	//�������z      				2
			"head.jpg",		//�����H�Y���z			3
			"left.jpg",		//�����H��L�������z		4
			"help1.jpg",	//���U1�����z     			5
			"help2.jpg",	//���U2�����z   			6
			"help3.jpg",	//���U3�����z        			7
			"help4.jpg",	//���U4�����z     			8
			"view.png",		//�����ഫ�������s���z	9
			"box.png",		//�c�l���z				10
			"target.jpg",	//�ت��a���z				11
			"load_back.png",//���J�ɭ��I���x�ί��z     	12
			"processbeijing.png",//���J�ɭ��i�׫��ܾ��I�����z  13
			"process.png",	//�i�׫��ܾ�					14
			"load.png",		//���J�ɭ��I���x�ί��z     	15
			"lose.png",		//��ɭ��I�����z  			16
			"win.png",		//Ĺ�ɭ��I�����z  			17
			"retry.jpg",	//�������s���z     			18
			"back.jpg",		//�Ǧ^���z                			19
			"next_guan.jpg",//��U�@�����z			20
			"g1.png",		//���d1���z				21
			"g2.png",		//���d2���z				22
			"g3.png",		//���d3���z				23
			"g4.png",		//���d4���z				24
			"g5.png",		//���d5���z				25
			"g6.png",		//���d6���z				26
			"g7.png",		//���d7���z				27
			"g8.png",		//���d8���z				28
			"g_back.jpg",	//�����ɭ��Ǧ^���s���z	29
			"start.jpg",	//�}�l�������z			30
			"set.jpg",		//�]�w���z				31
			"chance.jpg",	//�������z				32
			"exit.jpg",		//���}���z				33
			"about.jpg",	//���󯾲z				34
			"g9.png",		//���d9���z				35
			"suo.png",		//�꯾�z��				36
			"bgmusic.jpg", 	//�I�����֯��z			37
			"gsound.jpg",	//�������֯��z			38
			"on.jpg",		//���ֶ}					39
			"help.jpg",		//���ɭ����U���z		40
			"help5.jpg",	//���U5�����z			41
			"help6.jpg",	//���U6�����z			42
			"off.jpg",		//������					43
			"startl.jpg",	//�}�l�������z			44
			"setl.jpg",		//�]�w���z				45
			"chancel.jpg",	//�������z				46
			"exitl.jpg",		//���}���z			47
			"aboutl.jpg",	//���󯾲z				48
			"helpl.jpg",		//���ɭ����U���z	49
			"bgmusicl.jpg", 	//�I�����֯��z		50
			"gsoundl.jpg",	//�������֯��z			51
			"onl.jpg",		//���ֶ}					52
			"offl.jpg",		//������					53
			"g_backl.jpg",	//�����ɭ��Ǧ^���s���z	54
			"aboutl.png",	//����ɭ����t�@�ӯ��z	55
			"about.png",	//����ɭ����z			56
			"yaogan1.png",	//�~�h�n��				57
			"yaogan2.png",	//���h�n��				58
			"help7.jpg",	//���U7�����z			59
			"help8.jpg",	//���U8�����z			60
			"help9.jpg",	//���U9�����z			61
			"help10.jpg",	//���U10�����z			62
			"prepage.png",	//�W�@�������s			63
			"nextpage.png"	//�U�@�������s			64
	};
	
	//���J�Ϥ���ƶi�O���骺��k     ���J���J�ɭ��Ψ쪺���z
	public static void loadPicData(Context context)
	{
		if(isLoaded)return;
		
		picDataArray=new byte[texNameArray.length][];   
		
		for(int i=0;i<texNameArray.length;i++)
		{
			if(i==11||i>=12&&i<=15||i>=21&&i<=56)
			{
				picDataArray[i]=loadFromAssets(context,texNameArray[i]);
			}
		}
		picDataArray[3]=loadFromAssets(context,texNameArray[3]);
		picDataArray[4]=loadFromAssets(context,texNameArray[4]);
		
		picDataArray[5]=loadFromAssets(context,texNameArray[5]);//���U
		picDataArray[6]=loadFromAssets(context,texNameArray[6]);
		picDataArray[7]=loadFromAssets(context,texNameArray[7]);
		picDataArray[8]=loadFromAssets(context,texNameArray[8]);
		picDataArray[41]=loadFromAssets(context,texNameArray[41]);
		picDataArray[42]=loadFromAssets(context,texNameArray[42]);
		picDataArray[59]=loadFromAssets(context,texNameArray[59]);
		picDataArray[60]=loadFromAssets(context,texNameArray[60]);
		picDataArray[61]=loadFromAssets(context,texNameArray[61]);
		picDataArray[62]=loadFromAssets(context,texNameArray[62]);
		picDataArray[63]=loadFromAssets(context,texNameArray[63]);
		picDataArray[64]=loadFromAssets(context,texNameArray[64]);
		isLoaded=true;
	}
	
	//���J��L�ɭ��Ψ쪺���z
	public static void loadPicData(Context context,int index)
	{
		idLoadedOther=false;
		switch(index)
		{
		case 1:
			picDataArray[0]=loadFromAssets(context,texNameArray[0]);
			picDataArray[1]=loadFromAssets(context,texNameArray[1]);
			picDataArray[2]=loadFromAssets(context,texNameArray[2]);
	
			
			
			picDataArray[9]=loadFromAssets(context,texNameArray[9]);
			picDataArray[10]=loadFromAssets(context,texNameArray[10]);
			picDataArray[11]=loadFromAssets(context,texNameArray[11]);
			picDataArray[16]=loadFromAssets(context,texNameArray[16]);
			picDataArray[57]=loadFromAssets(context,texNameArray[57]);
			picDataArray[58]=loadFromAssets(context,texNameArray[58]);
			break;
		case 2:  
			picDataArray[17]=loadFromAssets(context,texNameArray[17]);
			picDataArray[18]=loadFromAssets(context,texNameArray[18]);
			picDataArray[19]=loadFromAssets(context,texNameArray[19]);
			picDataArray[20]=loadFromAssets(context,texNameArray[20]);
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			break;
		}
		idLoadedOther=true;
	}
	
	//�qAssets�����J�@�T���z
	public static byte[] loadFromAssets(Context context,String picName)
	{
		byte[] result=null;
		
		try
    	{
    		InputStream in=context.getResources().getAssets().open(picName);
			int ch=0;
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    while((ch=in.read())!=-1)
		    {
		      	baos.write(ch);
		    }      
		    result=baos.toByteArray();
		    baos.close();
		    in.close();    		
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}    	
		
		return result;
	}
}
