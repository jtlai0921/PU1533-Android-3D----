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
	
	//�����H����ʧ@
	static 
	{
		acArrayLeft.totalStep=10000;
		acArrayLeft.Robotdata=new float[][]
		{			
			{1,1,0,90,0,1,0}//body���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
		};
	}
	
	//�����H�k��ʧ@
	static 
	{
		acArrayRight.totalStep=10000;
		acArrayRight.Robotdata=new float[][]
		{			
			{1,1,0,-90,0,1,0}//body���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
		};
	}
	
	//�����H�V����ʧ@
	static 
	{
		acArrayDown.totalStep=20000;
		acArrayDown.Robotdata=new float[][]
		{			
			{1,1,0,180,0,1,0}//body���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
		};
	}
	
	//�����H�e�i�ʧ@
	static 
	{
		acArrayUp[0]=new Action(ActionType.ROBOT_UP);//��_���u�ʧ@
		acArrayUp[0].totalStep=20000;
		acArrayUp[0].Robotdata=new float[][]
		{			
			{3,1,0,-90,1,0,0},//���u���ʧ@  ����s��   �ʧ@���A�]0-���� 1-����^  �_�l����	��������   ����b�V�qxyz
			{5,1,0,-90,1,0,0}//�k�u���ʧ@  ����s��   �ʧ@���A�]0-���� 1-����^  �_�l����	��������   ����b�V�qxyz
		};
		
		acArrayUp[1]=new Action(ActionType.ROBOT_UP);//��U���u�ʧ@
		acArrayUp[1].totalStep=20000;
		acArrayUp[1].Robotdata=new float[][]
		{			
			{3,1,-90,0,1,0,0},//���u���ʧ@  ����s��   �ʧ@���A�]0-���� 1-����^  �_�l����	��������   ����b�V�qxyz
			{5,1,-90,0,1,0,0}//�k�u���ʧ@  ����s��   �ʧ@���A�]0-���� 1-����^  �_�l����	��������   ����b�V�qxyz
		};
		
		acArrayUp[2]=new Action(ActionType.ROBOT_UP);//�e�i�Y���h���@
		acArrayUp[2].totalStep=100000;
		acArrayUp[2].Robotdata=new float[][]
		{			
			{1,0,0,0,0,0,0,0},//body���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l��XYZ  ������XYZ
		};
	}
	
	static	//�k��W�|,�����s��
	{
		acrobortArray[0]=new Action();
		acrobortArray[0].totalStep=50;
		acrobortArray[0].Robotdata=new float[][]
	    {
				{2,1,0,0,0,1,0} //����  0�X�X0��
	    };
		acrobortArray[1]=new Action();
		acrobortArray[1].totalStep=100;
		acrobortArray[1].Robotdata=new float[][]
	    {//����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
				{3,1,0,-90,0,0,1},	//���u����     z�b
				{5,1,0,90,0,0,1},	//0�X�X90��       z�b  
	    };
		
		acrobortArray[2]=new Action();
		acrobortArray[2].totalStep=100;
		acrobortArray[2].Robotdata=new float[][]
	    {
				{3,1,-90,-180,1,0,0},//����W�|   -90��-180��  		x�b
				{5,1,90,30,0,0,1},	 //90�ר�30��		z�b
				{6,1,0,-70,0,0,1}	 //0��-70��			z�b
	    };
		acrobortArray[3]=new Action();
		acrobortArray[3].totalStep=100;
		acrobortArray[3].Robotdata=new float[][]
		{
				{3,1,-180,-90,0,0,1},//���u����     -180�ר�-90��    z�b
				{4,1,0,0,1,0,0},			//0�ר�0��     x�b
				{5,1,30,90,0,0,1},			//30�ר�90��   z�b
				{6,1,-70,0,0,0,1},			//-70�ר�0��
		};
		acrobortArray[4]=new Action();
		acrobortArray[4].totalStep=100;
		acrobortArray[4].Robotdata=new float[][]
		{
				{3,1,-90,-30,0,0,1},//�k��W�|,�����s��        0��-30��     z�b
				{4,1,0,70,0,0,1},//0��70��   z�b
				{5,1,90,-180,1,0,0},//0�ר�-180��     x�b
				{6,1,0,0,1,0,0}		//0�ר�0��          x�b	
		};
		acrobortArray[5]=new Action();
		acrobortArray[5].totalStep=100;
		acrobortArray[5].Robotdata=new float[][]
		{
				{3,1,-30,-90,1,0,0},	//����W�|�A�k��e��       -30��-90��     x�b
				{4,1,70,0,1,0,0},		//70��0��   	 x�b
				{5,1,-180,-180,1,0,0},		//0��-180��      x�b
				{6,1,0,0,1,0,0}			//0��0��  	 x�b				
		};
		acrobortArray[6]=new Action();
		acrobortArray[6].totalStep=100;
		acrobortArray[6].Robotdata=new float[][]
		{	
				{1,1,0,90,0,1,0},//����90�סA�k��e�s90�סA�������        0��90��     y�b
				{3,1,-90,-90,1,0,0},	//-90��-90��   x�b
				{4,1,0,90,0,0,1},		//0��90��     x�b
				{5,1,-180,-90,1,0,0},	//-180��-90��    x�b
				{6,1,0,0,1,0,0}			//0��0��     x�b
		};		
		acrobortArray[7]=new Action();
		acrobortArray[7].totalStep=100;
		acrobortArray[7].Robotdata=new float[][]
		{//����V�W�s��
				{1,1,90,0,0,1,0},		//0��0��     		y�b
				{3,1,-90,-90,1,0,0},		//0��-90��  		 x�b
				{4,1,90,-110,1,0,0},		//0��-110��   		x�b
				{5,1,-90,-90,1,0,0},		//0��-90��    		x�b
				{6,1,0,-110,1,0,0}		//0��-110��  		 x�b
		};
		acrobortArray[8]=new Action();
		acrobortArray[8].totalStep=100;
		acrobortArray[8].Robotdata=new float[][]
		{//����e���A�V�W�s��
				{3,1,-90,-90,1,0,0},		//0��-90��   		x�b
				{4,1,-110,0,1,0,0},		//-110��0��   		 x�b
				{5,1,-90,-90,1,0,0},		//0��-90��    		x�b
				{6,1,-110,0,1,0,0}		//-110��0��    	x�b
		};	
		acrobortArray[9]=new Action();
		acrobortArray[9].totalStep=100;
		acrobortArray[9].Robotdata=new float[][]
		{//����e��
				{3,1,-90,0,1,0,0},//-90��0��   x�b
				{5,1,-90,0,1,0,0},//-90��0��   x�b
		};	
		acrobortArray[10]=new Action();
		acrobortArray[10].totalStep=100;
		acrobortArray[10].Robotdata=new float[][]
		{
				{3,1,0,-180,1,0,0},//���O���u��_      0��-180��    x�b
				{4,1,0,30,0,0,1}//������u�s���A�i�J�q§���A     0��30��   z�b
		};
		acrobortArray[11]=new Action();
		acrobortArray[11].totalStep=100;
		acrobortArray[11].Robotdata=new float[][]
		{
				{3,1,-180,0,1,0,0},//�q§������U        -180��0��      x�b
				{4,1,30,0,0,0,1}					//30��0��        z�b
		};	
		acrobortArray[12]=new Action();
		acrobortArray[12].totalStep=50;
		acrobortArray[12].Robotdata=new float[][]
		{
				{1,1,0, 90, 0,1,0},  //0��90��     y�b
				{3,1,0,-90,1,0,0},	//0��-90��     x�b
				{5,1,0,-90,1,0,0}	//0��-90��     x�b
		};
		
		acrobortArray[13]=new Action();
		acrobortArray[13].totalStep=200;
		acrobortArray[13].Robotdata=new float[][]
		{
				{1,0,0, 0, 0,11,0,0},//�����H�h��
		};	
		
		
		
		
		
		
		acrobortArray[14]=new Action();
		acrobortArray[14].totalStep=100;
		acrobortArray[14].Robotdata=new float[][]
		{
				{1,1,90,0,0,1,0},//�����H�V�e��
				{3,1,-90,0,1,0,0},
				{5,1,-90,0,1,0,0}
		};
		acrobortArray[15]=new Action();
		acrobortArray[15].totalStep=100;
		acrobortArray[15].Robotdata=new float[][]
		{
				{1,0,11,0,0,11,0,2.5f},//�����H�V�e��
		};
		acrobortArray[16]=new Action();
		acrobortArray[16].totalStep=100;
		acrobortArray[16].Robotdata=new float[][]
		{
				{1,1,0, 90, 0,1,0},//�����H�V�k��
	
		};
		
		acrobortArray[17]=new Action();
		acrobortArray[17].totalStep=100;
		acrobortArray[17].Robotdata=new float[][]
		{
				{1,0,11, 0, 2.5f,22,0,2.5f},//�����H�V�k��
	
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
				{1,0,22, 0, 2.5f,22,0,-0.5f},//�����H�VZ�b��
		};
		acrobortArray[20]=new Action();
		acrobortArray[20].totalStep=100;
		acrobortArray[20].Robotdata=new float[][]
		{
				{1,1,180, 0, 0,1,0},//�����H�V�k��
	
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
				{3,1,0,-180,1,0,0},//���O���u��_
				{4,1,0,30,0,0,1}//������u�s���A�i�J�q§���A
		};
		
		acrobortArray[23]=new Action();
		acrobortArray[23].totalStep=100;
		acrobortArray[23].Robotdata=new float[][]
		{
				{3,1,-180,0,1,0,0},//���O���u��_
				{4,1,30,0,0,0,1}//������u�s���A�i�J�q§���A
		};	
		
		acrobortArray[24]=new Action();
		acrobortArray[24].totalStep=100;
		acrobortArray[24].Robotdata=new float[][]
		{
				{1,1,0,-90,0,1,0},//���O���u��_
				{3,1,0,-90,1,0,0},//���u���ʧ@  ����s��   �ʧ@���A�]0-���� 1-����^  �_�l����	��������   ����b�V�qxyz
				{5,1,0,-90,1,0,0}//�k�u���ʧ@  ����s��   �ʧ@���A�]0-���� 1-����^  �_�l����	��������   ����b�V�qxyz

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
				{1,0,11,0,0,11,0,2.5f},//�����H�V�e��
		};
		acrobortArray[28]=new Action();
		acrobortArray[28].totalStep=100;
		acrobortArray[28].Robotdata=new float[][]
		{
				
				{1,1,0, -90, 0,1,0},//�����H�V�k��
				
		};
		
		acrobortArray[29]=new Action();
		acrobortArray[29].totalStep=100;
		acrobortArray[29].Robotdata=new float[][]
		{
				{1,0,11,0,2.5f,0,0,2.5f},//�����H�V����
	
		};
		acrobortArray[30]=new Action();
		acrobortArray[30].totalStep=100;
		acrobortArray[30].Robotdata=new float[][]
		{
				{1,1,-90, -180, 0,1,0},//�����H�V�k��
	
		};
		acrobortArray[31]=new Action();
		acrobortArray[31].totalStep=100;
		acrobortArray[31].Robotdata=new float[][]
		{
				{1,0,0,0,2.5f,0,0,0},//�����H�VZ�b��
	
		};
		acrobortArray[32]=new Action();
		acrobortArray[32].totalStep=100;
		acrobortArray[32].Robotdata=new float[][]
		{
				{1,1,180, 0, 0,1,0},//�����H�V�k��
	
		};
		
		
		
	}
	
	//�q§���ʧ@
	static
	{
		acArraySalute[0]=new Action();
		acArraySalute[0].totalStep=10000;
		acArraySalute[0].Robotdata=new float[][]
	    {
				{3,1,0,-180,1,0,0},//���O���u��_
				{4,1,0,30,0,0,1}//������u�s���A�i�J�q§���A
	    };
		acArraySalute[1]=new Action();
		acArraySalute[1].totalStep=10000;
		acArraySalute[1].Robotdata=new float[][]
	    {
				{3,1,-180,0,1,0,0},//���O���u��_
				{4,1,30,0,0,0,1}//������u�s���A�^�Э�Ӫ����A
	    };
	}
	
	//���O�����
	static 
	{
		acArray[0]=new Action();
		acArray[1]=new Action();
		
		acArray[0].totalStep=60;
		acArray[0].Robotdata=new float[][]
		{			
			{1,1,-60,60,0,1,0},//body���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
			{3,1,0,-80,1,0,0},//leftTop���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
			{4,1,0,50,0,0,1},//leftBottom���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
			{5,1,0,-80,1,0,0},//rightTop���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
			{6,1,0,-50,0,0,1},//RightBottom���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
		};
		
		acArray[1].totalStep=60;
		acArray[1].Robotdata=new float[][]
		{			
			{1,1,60,-60,0,1,0},//body���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
			{3,1,-80,0,1,0,0},//leftTop���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
			{4,1,50,0,0,0,1},//leftBottom���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
			{5,1,-80,0,1,0,0},//rightTop���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
			{6,1,-50,0,0,0,1},//RightBottom���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
		}; 
	}
	
	//���O����B
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
