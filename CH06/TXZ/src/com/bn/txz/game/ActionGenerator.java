package com.bn.txz.game;

public class ActionGenerator 
{
	static Action acArrayLeft=new Action(ActionType.ROBOT_LEFT);
	static Action acArrayRight=new Action(ActionType.ROBOT_RIGHT);
	static Action acArrayDown=new Action(ActionType.ROBOT_DOWN);
	static Action[] acArrayUp=new Action[3];
	public static Action[] acrobortArray=new Action[33];
	static Action []acArraySalute=new Action[2];
	public static Action[] acArray=new Action[2];
	public static Action[] acselectArray=new Action[2];
	
	//機器人左轉動作
	static 
	{
		acArrayLeft.totalStep=10000;
		acArrayLeft.Robotdata=new float[][]
		{			
			{1,1,0,90,0,1,0}//body的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
		};
	}
	
	//機器人右轉動作
	static 
	{
		acArrayRight.totalStep=10000;
		acArrayRight.Robotdata=new float[][]
		{			
			{1,1,0,-90,0,1,0}//body的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
		};
	}
	
	//機器人向後轉動作
	static 
	{
		acArrayDown.totalStep=20000;
		acArrayDown.Robotdata=new float[][]
		{			
			{1,1,0,180,0,1,0}//body的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
		};
	}
	
	//機器人前進動作
	static 
	{
		acArrayUp[0]=new Action(ActionType.ROBOT_UP);//抬起雙臂動作
		acArrayUp[0].totalStep=20000;
		acArrayUp[0].Robotdata=new float[][]
		{			
			{3,1,0,-90,1,0,0},//左臂的動作  元件編號   動作型態（0-平移 1-旋轉）  起始角度	結束角度   旋轉軸向量xyz
			{5,1,0,-90,1,0,0}//右臂的動作  元件編號   動作型態（0-平移 1-旋轉）  起始角度	結束角度   旋轉軸向量xyz
		};
		
		acArrayUp[1]=new Action(ActionType.ROBOT_UP);//放下雙臂動作
		acArrayUp[1].totalStep=20000;
		acArrayUp[1].Robotdata=new float[][]
		{			
			{3,1,-90,0,1,0,0},//左臂的動作  元件編號   動作型態（0-平移 1-旋轉）  起始角度	結束角度   旋轉軸向量xyz
			{5,1,-90,0,1,0,0}//右臂的動作  元件編號   動作型態（0-平移 1-旋轉）  起始角度	結束角度   旋轉軸向量xyz
		};
		
		acArrayUp[2]=new Action(ActionType.ROBOT_UP);//前進即平搬移作
		acArrayUp[2].totalStep=100000;
		acArrayUp[2].Robotdata=new float[][]
		{			
			{1,0,0,0,0,0,0,0},//body的動作  元件編號  動作型態（0-平移 1-旋轉）起始值XYZ  結束值XYZ
		};
	}
	
	static	//右手上舉,左手彎曲
	{
		acrobortArray[0]=new Action();
		acrobortArray[0].totalStep=50;
		acrobortArray[0].Robotdata=new float[][]
	    {
				{2,1,0,0,0,1,0} //旋轉  0——0度
	    };
		acrobortArray[1]=new Action();
		acrobortArray[1].totalStep=100;
		acrobortArray[1].Robotdata=new float[][]
	    {//元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
				{3,1,0,-90,0,0,1},	//手臂平行     z軸
				{5,1,0,90,0,0,1},	//0——90度       z軸  
	    };
		
		acrobortArray[2]=new Action();
		acrobortArray[2].totalStep=100;
		acrobortArray[2].Robotdata=new float[][]
	    {
				{3,1,-90,-180,1,0,0},//左手上舉   -90到-180度  		x軸
				{5,1,90,30,0,0,1},	 //90度到30度		z軸
				{6,1,0,-70,0,0,1}	 //0到-70度			z軸
	    };
		acrobortArray[3]=new Action();
		acrobortArray[3].totalStep=100;
		acrobortArray[3].Robotdata=new float[][]
		{
				{3,1,-180,-90,0,0,1},//手臂平行     -180度到-90度    z軸
				{4,1,0,0,1,0,0},			//0度到0度     x軸
				{5,1,30,90,0,0,1},			//30度到90度   z軸
				{6,1,-70,0,0,0,1},			//-70度到0度
		};
		acrobortArray[4]=new Action();
		acrobortArray[4].totalStep=100;
		acrobortArray[4].Robotdata=new float[][]
		{
				{3,1,-90,-30,0,0,1},//右手上舉,左手彎曲        0到-30度     z軸
				{4,1,0,70,0,0,1},//0到70度   z軸
				{5,1,90,-180,1,0,0},//0度到-180度     x軸
				{6,1,0,0,1,0,0}		//0度到0度          x軸	
		};
		acrobortArray[5]=new Action();
		acrobortArray[5].totalStep=100;
		acrobortArray[5].Robotdata=new float[][]
		{
				{3,1,-30,-90,1,0,0},	//左手上舉，右手前伸       -30到-90度     x軸
				{4,1,70,0,1,0,0},		//70到0度   	 x軸
				{5,1,-180,-180,1,0,0},		//0到-180度      x軸
				{6,1,0,0,1,0,0}			//0到0度  	 x軸				
		};
		acrobortArray[6]=new Action();
		acrobortArray[6].totalStep=100;
		acrobortArray[6].Robotdata=new float[][]
		{	
				{1,1,0,90,0,1,0},//旋轉90度，右手前彎90度，左手伸直        0到90度     y軸
				{3,1,-90,-90,1,0,0},	//-90到-90度   x軸
				{4,1,0,90,0,0,1},		//0到90度     x軸
				{5,1,-180,-90,1,0,0},	//-180到-90度    x軸
				{6,1,0,0,1,0,0}			//0到0度     x軸
		};		
		acrobortArray[7]=new Action();
		acrobortArray[7].totalStep=100;
		acrobortArray[7].Robotdata=new float[][]
		{//雙手向上彎曲
				{1,1,90,0,0,1,0},		//0到0度     		y軸
				{3,1,-90,-90,1,0,0},		//0到-90度  		 x軸
				{4,1,90,-110,1,0,0},		//0到-110度   		x軸
				{5,1,-90,-90,1,0,0},		//0到-90度    		x軸
				{6,1,0,-110,1,0,0}		//0到-110度  		 x軸
		};
		acrobortArray[8]=new Action();
		acrobortArray[8].totalStep=100;
		acrobortArray[8].Robotdata=new float[][]
		{//雙手前伸，向上彎曲
				{3,1,-90,-90,1,0,0},		//0到-90度   		x軸
				{4,1,-110,0,1,0,0},		//-110到0度   		 x軸
				{5,1,-90,-90,1,0,0},		//0到-90度    		x軸
				{6,1,-110,0,1,0,0}		//-110到0度    	x軸
		};	
		acrobortArray[9]=new Action();
		acrobortArray[9].totalStep=100;
		acrobortArray[9].Robotdata=new float[][]
		{//雙手前伸
				{3,1,-90,0,1,0,0},//-90到0度   x軸
				{5,1,-90,0,1,0,0},//-90到0度   x軸
		};	
		acrobortArray[10]=new Action();
		acrobortArray[10].totalStep=100;
		acrobortArray[10].Robotdata=new float[][]
		{
				{3,1,0,-180,1,0,0},//先是左臂抬起      0到-180度    x軸
				{4,1,0,30,0,0,1}//之後手臂彎曲，進入敬禮狀態     0到30度   z軸
		};
		acrobortArray[11]=new Action();
		acrobortArray[11].totalStep=100;
		acrobortArray[11].Robotdata=new float[][]
		{
				{3,1,-180,0,1,0,0},//敬禮之後手放下        -180到0度      x軸
				{4,1,30,0,0,0,1}					//30到0度        z軸
		};	
		acrobortArray[12]=new Action();
		acrobortArray[12].totalStep=50;
		acrobortArray[12].Robotdata=new float[][]
		{
				{1,1,0, 90, 0,1,0},  //0到90度     y軸
				{3,1,0,-90,1,0,0},	//0到-90度     x軸
				{5,1,0,-90,1,0,0}	//0到-90度     x軸
		};
		
		acrobortArray[13]=new Action();
		acrobortArray[13].totalStep=200;
		acrobortArray[13].Robotdata=new float[][]
		{
				{1,0,0, 0, 0,11,0,0},//機器人搬移
		};	
		
		
		
		
		
		
		acrobortArray[14]=new Action();
		acrobortArray[14].totalStep=100;
		acrobortArray[14].Robotdata=new float[][]
		{
				{1,1,90,0,0,1,0},//機器人向前轉
				{3,1,-90,0,1,0,0},
				{5,1,-90,0,1,0,0}
		};
		acrobortArray[15]=new Action();
		acrobortArray[15].totalStep=100;
		acrobortArray[15].Robotdata=new float[][]
		{
				{1,0,11,0,0,11,0,2.5f},//機器人向前走
		};
		acrobortArray[16]=new Action();
		acrobortArray[16].totalStep=100;
		acrobortArray[16].Robotdata=new float[][]
		{
				{1,1,0, 90, 0,1,0},//機器人向右轉
	
		};
		
		acrobortArray[17]=new Action();
		acrobortArray[17].totalStep=100;
		acrobortArray[17].Robotdata=new float[][]
		{
				{1,0,11, 0, 2.5f,22,0,2.5f},//機器人向右走
	
		};
		acrobortArray[18]=new Action();
		acrobortArray[18].totalStep=100;
		acrobortArray[18].Robotdata=new float[][]
		{
				{1,1,90,180,0,1,0},
		};
		acrobortArray[19]=new Action();
		acrobortArray[19].totalStep=100;
		acrobortArray[19].Robotdata=new float[][]
		{
				{1,0,22, 0, 2.5f,22,0,-0.5f},//機器人向Z軸走
		};
		acrobortArray[20]=new Action();
		acrobortArray[20].totalStep=100;
		acrobortArray[20].Robotdata=new float[][]
		{
				{1,1,180, 0, 0,1,0},//機器人向右轉
	
		};
		
		acrobortArray[21]=new Action();
		acrobortArray[21].totalStep=50;
		acrobortArray[21].Robotdata=new float[][]
	    {
				{2,1,0,0,0,1,0}
	    };
		
		acrobortArray[22]=new Action();
		acrobortArray[22].totalStep=100;
		acrobortArray[22].Robotdata=new float[][]
		{
				{3,1,0,-180,1,0,0},//先是左臂抬起
				{4,1,0,30,0,0,1}//之後手臂彎曲，進入敬禮狀態
		};
		
		acrobortArray[23]=new Action();
		acrobortArray[23].totalStep=100;
		acrobortArray[23].Robotdata=new float[][]
		{
				{3,1,-180,0,1,0,0},//先是左臂抬起
				{4,1,30,0,0,0,1}//之後手臂彎曲，進入敬禮狀態
		};	
		
		acrobortArray[24]=new Action();
		acrobortArray[24].totalStep=100;
		acrobortArray[24].Robotdata=new float[][]
		{
				{1,1,0,-90,0,1,0},//先是左臂抬起
				{3,1,0,-90,1,0,0},//左臂的動作  元件編號   動作型態（0-平移 1-旋轉）  起始角度	結束角度   旋轉軸向量xyz
				{5,1,0,-90,1,0,0}//右臂的動作  元件編號   動作型態（0-平移 1-旋轉）  起始角度	結束角度   旋轉軸向量xyz

		};
		
		acrobortArray[25]=new Action();
		acrobortArray[25].totalStep=253;
		acrobortArray[25].Robotdata=new float[][]
		{
				{1,0,22, 0, 0,11.5f,0,0},
		};
		acrobortArray[26]=new Action();
		acrobortArray[26].totalStep=100;
		acrobortArray[26].Robotdata=new float[][]
		{
				{1,1,-90,0,0,1,0},
				{2,1,-90,0,0,1,0},
				{3,1,-90,0,1,0,0},
				{5,1,-90,0,1,0,0}
		};
		acrobortArray[27]=new Action();
		acrobortArray[27].totalStep=100;
		acrobortArray[27].Robotdata=new float[][]
		{				
				{1,0,11,0,0,11,0,2.5f},//機器人向前走
		};
		acrobortArray[28]=new Action();
		acrobortArray[28].totalStep=100;
		acrobortArray[28].Robotdata=new float[][]
		{
				
				{1,1,0, -90, 0,1,0},//機器人向右轉
				
		};
		
		acrobortArray[29]=new Action();
		acrobortArray[29].totalStep=100;
		acrobortArray[29].Robotdata=new float[][]
		{
				{1,0,11,0,2.5f,0,0,2.5f},//機器人向左走
	
		};
		acrobortArray[30]=new Action();
		acrobortArray[30].totalStep=100;
		acrobortArray[30].Robotdata=new float[][]
		{
				{1,1,-90, -180, 0,1,0},//機器人向右轉
	
		};
		acrobortArray[31]=new Action();
		acrobortArray[31].totalStep=100;
		acrobortArray[31].Robotdata=new float[][]
		{
				{1,0,0,0,2.5f,0,0,0},//機器人向Z軸走
	
		};
		acrobortArray[32]=new Action();
		acrobortArray[32].totalStep=100;
		acrobortArray[32].Robotdata=new float[][]
		{
				{1,1,180, 0, 0,1,0},//機器人向右轉
	
		};
		
		
		
	}
	
	//敬禮的動作
	static
	{
		acArraySalute[0]=new Action();
		acArraySalute[0].totalStep=10000;
		acArraySalute[0].Robotdata=new float[][]
	    {
				{3,1,0,-180,1,0,0},//先是左臂抬起
				{4,1,0,30,0,0,1}//之後手臂彎曲，進入敬禮狀態
	    };
		acArraySalute[1]=new Action();
		acArraySalute[1].totalStep=10000;
		acArraySalute[1].Robotdata=new float[][]
	    {
				{3,1,-180,0,1,0,0},//先是左臂抬起
				{4,1,30,0,0,0,1}//之後手臂彎曲，回覆原來的狀態
	    };
	}
	
	//類別似拍手
	static 
	{
		acArray[0]=new Action();
		acArray[1]=new Action();
		
		acArray[0].totalStep=60;
		acArray[0].Robotdata=new float[][]
		{			
			{1,1,-60,60,0,1,0},//body的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
			{3,1,0,-80,1,0,0},//leftTop的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
			{4,1,0,50,0,0,1},//leftBottom的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
			{5,1,0,-80,1,0,0},//rightTop的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
			{6,1,0,-50,0,0,1},//RightBottom的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
		};
		
		acArray[1].totalStep=60;
		acArray[1].Robotdata=new float[][]
		{			
			{1,1,60,-60,0,1,0},//body的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
			{3,1,-80,0,1,0,0},//leftTop的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
			{4,1,50,0,0,0,1},//leftBottom的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
			{5,1,-80,0,1,0,0},//rightTop的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
			{6,1,-50,0,0,0,1},//RightBottom的動作  元件編號  動作型態（0-平移 1-旋轉）起始角度值  結束角度值   旋轉軸向量XYZ
		}; 
	}
	
	//類別似踏步
	static 
	{
	acselectArray[0]=new Action();
	acselectArray[1]=new Action();

	acselectArray[0].totalStep=60;
	acselectArray[0].Robotdata=new float[][]
	{ 
	{3,1,-30,30,1,0,0},
	{5,1,30,-30,1,0,0},
	};

	acselectArray[1].totalStep=60;
	acselectArray[1].Robotdata=new float[][]
	{ 
	{3,1,30,-30,1,0,0},
	{5,1,-30,30,1,0,0},
	}; 
	} 

}
