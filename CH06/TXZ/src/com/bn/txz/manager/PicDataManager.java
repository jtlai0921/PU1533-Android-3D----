package com.bn.txz.manager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;

//紋理圖資料管理者
public class PicDataManager 
{
	public static boolean isLoaded=false;
	public static boolean idLoadedOther=false;
	public static byte[][] picDataArray=null;
	public static String[] texNameArray=
	{
			"room.jpg",		//房子圖片    				0
			"wall.jpg",		//群組成橋的紋理           		1
			"water.png",	//水的紋理      				2
			"head.jpg",		//機器人頭紋理			3
			"left.jpg",		//機器人其他部分紋理		4
			"help1.jpg",	//幫助1的紋理     			5
			"help2.jpg",	//幫助2的紋理   			6
			"help3.jpg",	//幫助3的紋理        			7
			"help4.jpg",	//幫助4的紋理     			8
			"view.png",		//角度轉換虛擬按鈕紋理	9
			"box.png",		//箱子紋理				10
			"target.jpg",	//目的地紋理				11
			"load_back.png",//載入界面背景矩形紋理     	12
			"processbeijing.png",//載入界面進度指示器背景紋理  13
			"process.png",	//進度指示器					14
			"load.png",		//載入界面背景矩形紋理     	15
			"lose.png",		//輸界面背景紋理  			16
			"win.png",		//贏界面背景紋理  			17
			"retry.jpg",	//重玩按鈕紋理     			18
			"back.jpg",		//傳回紋理                			19
			"next_guan.jpg",//到下一關紋理			20
			"g1.png",		//關卡1紋理				21
			"g2.png",		//關卡2紋理				22
			"g3.png",		//關卡3紋理				23
			"g4.png",		//關卡4紋理				24
			"g5.png",		//關卡5紋理				25
			"g6.png",		//關卡6紋理				26
			"g7.png",		//關卡7紋理				27
			"g8.png",		//關卡8紋理				28
			"g_back.jpg",	//選關界面傳回按鈕紋理	29
			"start.jpg",	//開始游戲紋理			30
			"set.jpg",		//設定紋理				31
			"chance.jpg",	//選關紋理				32
			"exit.jpg",		//離開紋理				33
			"about.jpg",	//關於紋理				34
			"g9.png",		//關卡9紋理				35
			"suo.png",		//鎖紋理圖				36
			"bgmusic.jpg", 	//背景音樂紋理			37
			"gsound.jpg",	//游戲音樂紋理			38
			"on.jpg",		//音樂開					39
			"help.jpg",		//選單界面幫助紋理		40
			"help5.jpg",	//幫助5的紋理			41
			"help6.jpg",	//幫助6的紋理			42
			"off.jpg",		//音樂關					43
			"startl.jpg",	//開始游戲紋理			44
			"setl.jpg",		//設定紋理				45
			"chancel.jpg",	//選關紋理				46
			"exitl.jpg",		//離開紋理			47
			"aboutl.jpg",	//關於紋理				48
			"helpl.jpg",		//選單界面幫助紋理	49
			"bgmusicl.jpg", 	//背景音樂紋理		50
			"gsoundl.jpg",	//游戲音樂紋理			51
			"onl.jpg",		//音樂開					52
			"offl.jpg",		//音樂關					53
			"g_backl.jpg",	//選關界面傳回按鈕紋理	54
			"aboutl.png",	//關於界面的另一個紋理	55
			"about.png",	//關於界面紋理			56
			"yaogan1.png",	//外層搖桿				57
			"yaogan2.png",	//內層搖桿				58
			"help7.jpg",	//幫助7的紋理			59
			"help8.jpg",	//幫助8的紋理			60
			"help9.jpg",	//幫助9的紋理			61
			"help10.jpg",	//幫助10的紋理			62
			"prepage.png",	//上一頁的按鈕			63
			"nextpage.png"	//下一頁的按鈕			64
	};
	
	//載入圖片資料進記憶體的方法     載入載入界面用到的紋理
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
		
		picDataArray[5]=loadFromAssets(context,texNameArray[5]);//幫助
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
	
	//載入其他界面用到的紋理
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
	
	//從Assets中載入一幅紋理
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
