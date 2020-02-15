package com.bn.txz.game;

import java.util.Queue;
import com.bn.txz.Constant;
import com.bn.txz.manager.SoundUtil;

import static com.bn.txz.Constant.*;
import static com.bn.txz.game.TXZGameSurfaceView.*;
import static com.bn.txz.game.GameStaticData.UNIT_SIZE;

//����ʧ@��C�������
public class TXZDoActionThread extends Thread
{
    public boolean workFlag=true;		//������O�_�`���u�@�ЧӦ�
    TXZGameSurfaceView gsv;				//����View�Ѧ�
    Queue<Action> aq;					//�ʧ@��C
    Robot robot;
    int i;//����c�l���ʰʵe���ܼ�
    int row=0;//�����H�Ҧb����C
	int col=0;
	
    
    public TXZDoActionThread(TXZGameSurfaceView gsv)
    {
    	this.gsv=gsv;		//����View
    	this.aq=gsv.aq;		//�ʧ@��C
    	this.robot=gsv.robot;
    }
    
    @Override
    public void run()
    {
    	while(workFlag)
    	{    		
    		Action ac=null;					//�ʧ@�Ѧ�
    		synchronized(gsv.aqLock)		//�ʧ@��C��
    		{
    			ac=aq.poll();				//�q�ʧ@��C�����X�@�Ӱʧ@�A�Y��C���S���ޱ��ʧ@�h���Xnull
    		}    		
    		if(ac!=null)					//�Y�ޱ��ʧ@�ѦҤ��Onull�A�Y���ʧ@�ݭn����
    		{
        		switch(ac.at)				//at���ޱ��ʧ@�����A�A�ھھޱ����A���椣�P���u�@
        		{
        		   case CHANGE_CAMERA:		//������v���ʧ@
        			  synchronized(gsv.gdMain.dataLock) 	//�N�D�����W
        			  {
        				  //�N�ޱ��ʧ@��a����Ƶ����ȵ��D���
        				  gsv.gdMain.calculCamare(ac.data[0], ac.data[1]);//data�}�C�O�h����XY�Z��
        				  synchronized(gsv.gdDraw.dataLock) //�Nø������W
        				  {
        					  //�N�D��Ƶ����ȵ�ø����
        					  gsv.gdDraw.updateCameraData(gsv.gdMain);
        				  }
        			  }
        		   break;
        		   case ROBOT_LEFT://�����H����ʧ@
        			  
        			   RobotTurnLeft();
        			    
        		   break;
        		   case ROBOT_RIGHT://�����H�k��ʧ@
        			   RobotTurnRight();
        		   break;
        		   case ROBOT_DOWN://�����H�V����ʧ@
        			   RobotTurnDown();
        		   break;
        		   case ROBOT_UP://�����H�e�i�ʧ@
        			   gsv.isInAnimation=true;
        			   synchronized(gsv.gqdMain.gqdataLock)
        			   {
        				   if(currDegree==POSITIVE_MOVETO_Z)//�Y�G�Oz�b����V
            			   {
            				   row=robot.m;
            				   col=robot.n;
            				   switch(gsv.gqdMain.MAP[GameData.level-1][row+1][col])//�P�_�U�@�B����m�O����
            				   {
            				   case 0://�J���
            				   case 5://�J���\�n����c
            					   break;//�H�W���p���ਫ�A�ҥH���򳣤���
            				   case 3://�J��ت�,�H���A�H���U�@�Ӧ�m�אּ�H�b���ت�
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=6;
            					   }
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=6;
            					   }
            					   
            					   break;
            				   case 6://�Y�G�J��H�b�ت��I�W
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;
            					   }
            					   
            					   break;
            				   case 1://�J���   
            				   case 4://�J��H
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;
            					   }
            					   
                    			   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
                    				   RobotGo();
                    				   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
                    				   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;
            					   }
            					   break;
            				   case 2://�J��c�l
            					   //�P�_�c�l���e���O����
            					   if(gsv.gqdMain.MAP[GameData.level-1][row+2][col]==0||
            							   gsv.gqdMain.MAP[GameData.level-1][row+2][col]==5||
            							   gsv.gqdMain.MAP[GameData.level-1][row+2][col]==2)
            					   {}//�c�l���e���O�����\�n����c,�����ʡA��������ʧ@
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row+2][col]==3)//�c�l�e�����ت�
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//�����H��Ӫ���m����
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row+2][col]=5;//�ت��aø����n���c�l
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//�����H��Ӫ���m����
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row+2][col]=5;//�ت��aø����n���c�l
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row+2][col]==1)//�c�l�e������
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//�����H��Ӫ���m����
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row+2][col]=2;//�ت��aø��c�l
            							   RobotArmDown();
            							   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//�����H��Ӫ���m����
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row+1][col]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row+2][col]=2;//�ت��aø��c�l
            							   RobotArmDown();
            							   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   break;
            				   }
            			   }
            			   else if(currDegree==POSITIVE_MOVETO_X)//�Y�G�Ox�b����V
            			   {
            				   row=robot.m;
            				   col=robot.n;
            				   switch(gsv.gqdMain.MAP[GameData.level-1][row][col+1])//�P�_�U�@�B����m�O����
            				   {
            				   case 0://�J���
            				   case 5://�J���\�n����c
            					   break;//�H�W���p���ਫ�A�ҥH���򳣤���
            				   case 3://�J��ت�,�H���A�H���U�@�Ӧ�m�אּ�H�b���ت�
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=6;
            					   }
            					   
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=6;
            					   }
            					   break;
            				   case 6://�Y�G�J��H�b�ت��I�W
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4;
            					   }
            					   break;
            				   case 1://�J���  
            				   case 4://�J��H
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4; 
            					   }
            					   
                    			   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
                    				   RobotGo();
                    				   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
                    				   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4;
            					   }
            					   break;
            				   case 2://�J��c�l
            					 //�P�_�c�l���e���O����
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col+2]==0||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+2]==5||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+2]==2)
            					   {}//�c�l���e���O�����\�n����c,�����ʡA��������ʧ@
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row][col+2]==3)//�c�l�e�����ت�
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//�����H��Ӫ���m����
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+2]=5;//�ت��aø����n���c�l
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
//            							   gsv.gqdMain.cdArray=new CompareDis[gsv.gqdMain.boxCount];
                						   RobotArmDown();
                						   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//�����H��Ӫ���m���ت��I
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+2]=5;//�ت��aø����n���c�l
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row][col+2]==1)//�c�l�e������
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//�����H��Ӫ���m����
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+2]=2;//�ت��aø��c�l
            							   RobotArmDown();
            							   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//�����H��Ӫ���m����
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+1]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row][col+2]=2;//�ت��aø��c�l
            							   RobotArmDown();
            							   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   break;
            				   }
            			   }
            			   else if(currDegree==NEGATIVE_MOVETO_Z)//�Y�G�Oz�b�t��V
            			   {
            				   row=robot.m;
            				   col=robot.n;
            				   switch(gsv.gqdMain.MAP[GameData.level-1][row-1][col])//�P�_�U�@�B����m�O����
            				   {
            				   case 0://�J���
            				   case 5://�J���\�n����c
            					   break;//�H�W���p���ਫ�A�ҥH���򳣤���
            				   case 3://�J��ت�,�H���A�H���U�@�Ӧ�m�אּ�H�b���ت�
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=6;
            					   }
            					   
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=6;
            					   }
            					   break;
            				   case 6://�Y�G�J��H�b�ت��I�W
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;
            					   }
            					   
            					   break;
            				   case 1://�J���  
            				   case 4://�J��H
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;
            					   }
            					   
                    			   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
                    				   RobotGo();
                    				   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
                    				   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;
            					   }
            					   break;
            				   case 2://�J��c�l
            					 //�P�_�c�l���e���O����
            					   if(gsv.gqdMain.MAP[GameData.level-1][row-2][col]==0||
            							   gsv.gqdMain.MAP[GameData.level-1][row-2][col]==5||
            							   gsv.gqdMain.MAP[GameData.level-1][row-2][col]==2)
            					   {}//�c�l���e���O�����\�n����c,�����ʡA��������ʧ@
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row-2][col]==3)//�c�l�e�����ت�
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//�����H��Ӫ���m����
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row-2][col]=5;//�ت��aø����n���c�l
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//�����H��Ӫ���m����
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row-2][col]=5;//�ت��aø����n���c�l
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row-2][col]==1)//�c�l�e������
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//�����H��Ӫ���m����
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row-2][col]=2;//�ت��aø��c�l
            							   RobotArmDown();
            							   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//�����H��Ӫ���m����
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row-1][col]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row-2][col]=2;//�ت��aø��c�l
            							   RobotArmDown();
            							   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   break;
            				   }
            			   }
            			   else if(currDegree==NEGATIVE_MOVETO_X)//�Y�G�Ox�b�t��V
            			   {
            				   row=robot.m;
            				   col=robot.n;
            				   switch(gsv.gqdMain.MAP[GameData.level-1][row][col-1])//�P�_�U�@�B����m�O����
            				   {
            				   case 0://�J���
            				   case 5://�J���\�n����c
            					   break;//�H�W���p���ਫ�A�ҥH���򳣤���
            				   case 3://�J��ت�,�H���A�H���U�@�Ӧ�m�אּ�H�b���ت�
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=6;
            					   }
            					   
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=6;
            					   }
            					   break;
            				   case 6://�Y�G�J��H�b�ت��I�W
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;
            					   }
            					   
            					   break;
            				   case 1://�J���  
            				   case 4://�J��H
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            					   {
            						   RobotGo();
            						   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;
            						   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;
            					   }
            					   
                    			   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
            					   {
                    				   RobotGo();
                    				   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;
                    				   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;
            					   }
            					   break;
            				   case 2://�J��c�l
            					 //�P�_�c�l���e���O����
            					   if(gsv.gqdMain.MAP[GameData.level-1][row][col-2]==0||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-2]==5||
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-2]==2)
            					   {}//�c�l���e���O�����\�n����c,�����ʡA��������ʧ@
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row][col-2]==3)//�c�l�e�����ت�
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//�����H��Ӫ���m����
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-2]=5;//�ت��aø����n���c�l
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
            						   }
            						   
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//�����H��Ӫ���m����
            							   RobotGo();
            							   if(Constant.IS_YINXIAO)
            							   {
            								   SoundUtil.playSounds(SoundUtil.XUANZHONG, 0, gsv.activity);
            							   }
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-2]=5;//�ت��aø����n���c�l
            							   gsv.gqdMain.boxCount=gsv.gqdMain.boxCount-1;
                						   RobotArmDown();
                						   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   else if(gsv.gqdMain.MAP[GameData.level-1][row][col-2]==1)//�c�l�e������
            					   {
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==4)
            						   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=1;//�����H��Ӫ���m����
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-2]=2;//�ت��aø��c�l
            							   RobotArmDown();
            							   Salute();
            						   }
            						   if(gsv.gqdMain.MAP[GameData.level-1][row][col]==6)
                					   {
            							   RobotArmUp();
            							   GuanQiaData.move_flag=true;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col]=3;//�����H��Ӫ���m����
            							   RobotGo();
            							   GuanQiaData.move_flag=false;
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-1]=4;//�c�l��Ӫ��a��ø��H
            							   gsv.gqdMain.MAP[GameData.level-1][row][col-2]=2;//�ت��aø��c�l
            							   RobotArmDown();
            							   Salute();
                					   }
            						   GuanQiaData.xoffset=0;
            						   GuanQiaData.zoffset=0;
            					   }
            					   break;
            				   }
            			   }
        				   synchronized(gsv.gqdDraw.gqdataLock)
        				   {
        					  gsv.gqdDraw.boxCount=gsv.gqdMain.boxCount;
        					   for(int i=0;i<gsv.gqdDraw.MAP[GameData.level-1].length;i++)
        					   {
        						   for(int j=0;j<gsv.gqdDraw.MAP[GameData.level-1][0].length;j++)
        						   {
        							   gsv.gqdDraw.MAP[GameData.level-1][i][j]=gsv.gqdMain.MAP[GameData.level-1][i][j];
        						   }
        					   }
        				   }
        				   gsv.isInAnimation=false;
        			   }
        		   break;
        		   case CONVERT://���ܨ��װʧ@
        			   gsv.viewFlag=!gsv.viewFlag;
        		   break;
        		}
    		}
    		
    		try 
    		{
				Thread.sleep(10);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
    	}
    }
    

    //�����H�޻K��_���ʵe
    public void RobotArmUp()
    {
    	int currStep=0;
	   Action currAction=ActionGenerator.acArrayUp[0];
	   for(int i=0;i<currAction.totalStep;i++)//��_���u���ʧ@
       {
    		//�ק�D���
    		for(float[] ad:currAction.Robotdata)
    		{
    			//�������
    			int partIndex=(int) ad[0];
    			//�ʧ@���A
    			int aType=(int)ad[1]; 
    			//�ثe�B�J��
    			
    			if(aType==1)//����
    			{
    				float startAngle=ad[2];
    				float endAngle=ad[3];
    				float currAngle=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
    				float x=ad[4];
    				float y=ad[5];
    				float z=ad[6];
    				robot.bpArray[partIndex].rotate(currAngle, x, y, z);//��s�l���f�b�����f�y�Шt��������
    			}  														//��s�l���f�b�����f�y�Шt�������઺���U����
    		}    
    		currStep++;
    		
    		//�N�D����Ш�iø����
    		synchronized(gsv.gdDraw.dataLock)     
    		{
    			gsv.gdMain.copyTo(gsv.gdDraw);
    		}
    	}
    }

    //�����H�޻K���U���ʵe
    public void RobotArmDown()
    {
    	int currStep=0;
	   Action currAction=ActionGenerator.acArrayUp[1];
	   for(int i=0;i<currAction.totalStep;i++)//��_���u���ʧ@
       {
    		//�ק�D���
    		for(float[] ad:currAction.Robotdata)
    		{
    			//�������
    			int partIndex=(int) ad[0];
    			//�ʧ@���A
    			int aType=(int)ad[1]; 
    			//�ثe�B�J��
    			
    			if(aType==1)//����
    			{
    				float startAngle=ad[2];
    				float endAngle=ad[3];
    				float currAngle=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
    				float x=ad[4];
    				float y=ad[5];
    				float z=ad[6];
    				robot.bpArray[partIndex].rotate(currAngle, x, y, z);//��s�l���f�b�����f�y�Шt��������
    			}  														//��s�l���f�b�����f�y�Шt�������઺���U����
    		}  
    		currStep++;
    		
    		//�N�D����Ш�iø����
    		synchronized(gsv.gdDraw.dataLock)     
    		{
    			gsv.gdMain.copyTo(gsv.gdDraw);
    		}
       }
    }
    
    //�����H�h�����ʵe
    public void RobotGo()
    {
  	      int currStep=0;
		  Action  currAction=ActionGenerator.acArrayUp[2];
		  if(currStep>=currAction.totalStep)
		  {
			  currStep=0;
		  }
		   for(int i=0;i<currAction.totalStep;i++)//�e�i�ʧ@
	       {
	    		//�ק�D���
	    		for(float[] ad:currAction.Robotdata)
	    		{
	    			//�������
	    			int partIndex=(int) ad[0];
	    			//�ʧ@���A
	    			int aType=(int)ad[1]; 
	    			//�ثe�B�J��
	    			
	    			if(aType==0)//����
	    			{
	    				float positionx=col*UNIT_SIZE-(int)(gsv.gqdMain.MAP[GameData.level-1][0].length/2)*UNIT_SIZE+1f*UNIT_SIZE;
	    				float positionz=row*UNIT_SIZE-(int)(gsv.gqdMain.MAP[GameData.level-1].length/2)*UNIT_SIZE+0.5f*UNIT_SIZE;
	    				float xStart=ad[2]+positionx;
	    				float yStart=ad[3];
	    				float zStart=ad[4]+positionz;
	    				float xEnd = 0;
	    				float yEnd = 0;
	    				float zEnd = 0;
	    				if(currDegree==POSITIVE_MOVETO_Z)//z�b����V
	    				{
		    				xEnd=xStart;
		    				yEnd=yStart;
		    				zEnd=zStart+UNIT_SIZE;
	    				}
	    				else if(currDegree==POSITIVE_MOVETO_X)//x����V
	    				{
		    				xEnd=xStart+UNIT_SIZE;
		    				yEnd=yStart;
		    				zEnd=zStart;
	    				}
	    				else if(currDegree==NEGATIVE_MOVETO_Z)//z�t��V
	    				{
		    				xEnd=xStart;
		    				yEnd=yStart;
		    				zEnd=zStart-UNIT_SIZE;
	    				}
	    				else if(currDegree==NEGATIVE_MOVETO_X)//x�t��V
	    				{
		    				xEnd=xStart-UNIT_SIZE;  
		    				yEnd=yStart;
		    				zEnd=zStart;
	    				}
	    				
	    				currX=xStart+(xEnd-xStart)*currStep/currAction.totalStep;
	    				currY=yStart+(yEnd-yStart)*currStep/currAction.totalStep;
	    				currZ=zStart+(zEnd-zStart)*currStep/currAction.totalStep;
	    				robot.bpArray[partIndex].transtate(currX, currY, currZ);//��s�l���f�b�����f�y�Шt��������
	    			}													
	    		}    
	    		
	    		if(GuanQiaData.move_flag)
	    		{
	    			switch((int)currDegree)
					{
						case POSITIVE_MOVETO_Z://�Y�G�����H���V��z�b����V
							GuanQiaData.zoffset=GuanQiaData.zoffset+1f/currAction.totalStep;
							GuanQiaData.move_row=row+1;
							GuanQiaData.move_col=col;
							break;
						case POSITIVE_MOVETO_X://�Y�G�����H���V��X�b����V
							GuanQiaData.xoffset=GuanQiaData.xoffset+1f/currAction.totalStep;
							GuanQiaData.move_row=row;
							GuanQiaData.move_col=col+1;
							break;
						case NEGATIVE_MOVETO_Z://�Y�G�����H���V��z�b�t��V
							GuanQiaData.zoffset=GuanQiaData.zoffset-1f/currAction.totalStep;
							GuanQiaData.move_row=row-1;
							GuanQiaData.move_col=col;
							break;
						case NEGATIVE_MOVETO_X://�Y�G�����H���V��X�b�t��V
							GuanQiaData.xoffset=GuanQiaData.xoffset-1f/currAction.totalStep;
							GuanQiaData.move_row=row;
							GuanQiaData.move_col=col-1;
							break;
					}
	    		}

	    		currStep++;
	    		
	    		//�N�D����Ш�iø����
	    		synchronized(gsv.gdDraw.dataLock)     
	    		{
	    			gsv.gdMain.copyTo(gsv.gdDraw);
	    			Robot.RobotFlag=false; 
	    		}
	       }  
		   
    }
    
    //�����H����ʵe
    public void RobotTurnLeft()
    {
    	gsv.isInAnimation=true;
    	   int currStep=0;
    	   currDegreeView=0;
		   Action currAction=ActionGenerator.acArrayLeft;
		  
		   for(int i=0;i<currAction.totalStep;i++)
	    	   {
		    		//�ק�D���
		    		for(float[] ad:currAction.Robotdata)
		    		{
		    			//�������
		    			int partIndex=(int) ad[0];
		    			//�ʧ@���A
		    			int aType=(int)ad[1]; 
		    			//�ثe�B�J��
		    			
		    			if(aType==1)//����
		    			{
		    				float startAngle=ad[2]+currDegree;
		    				float endAngle=ad[3]+currDegree;
		    				currDegreeView=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
		    				float x=ad[4];
		    				float y=ad[5];
		    				float z=ad[6];
		    				robot.bpArray[partIndex].rotate(currDegreeView, x, y, z);//��s�l���f�b�����f�y�Шt��������
		    			}														//��s�l���f�b�����f�y�Шt�������઺���U����
		    		}    
		    		currStep++;
		    		
		    		//�N�D����Ш�iø����
		    		synchronized(gsv.gdDraw.dataLock)
		    		{
		    			gsv.gdMain.copyTo(gsv.gdDraw);
		    		}
		    	 }
		    currDegree=(currDegree+90)%360;
		    gsv.isInAnimation=false;
    }

    //�����H�k��ʵe
    public void RobotTurnRight()
    {
    	gsv.isInAnimation=true;
    	int currStep=0;
    	currDegreeView=0;
		Action  currAction=ActionGenerator.acArrayRight;
		    for(int i=0;i<currAction.totalStep;i++)
	    	{
	    		//�ק�D���
	    		for(float[] ad:currAction.Robotdata)
	    		{
	    			//�������
	    			int partIndex=(int) ad[0];
	    			//�ʧ@���A
	    			int aType=(int)ad[1]; 
	    			//�ثe�B�J��
	    			
	    			if(aType==1)//����
	    			{
	    				float startAngle=ad[2]+currDegree;
	    				float endAngle=ad[3]+currDegree;
	    				currDegreeView=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
	    				float x=ad[4];
	    				float y=ad[5];
	    				float z=ad[6];
	    				robot.bpArray[partIndex].rotate(currDegreeView, x, y, z);//��s�l���f�b�����f�y�Шt��������
	    			}  														//��s�l���f�b�����f�y�Шt�������઺���U����
	    		}    
	    		currStep++;
	    		
	    		//�N�D����Ш�iø����
	    		synchronized(gsv.gdDraw.dataLock)     
	    		{
	    			gsv.gdMain.copyTo(gsv.gdDraw);
	    		}
	    	}
		    if(currDegree-90<0)
		    {
		    	currDegree=(currDegree-90)%360+360;
		    	CURR_DIRECTION=(CURR_DIRECTION+270)%360+360;//�ק�¦V
		    }
		    else
		    {
		    	currDegree=(currDegree-90)%360;
		    	CURR_DIRECTION=(CURR_DIRECTION+270)%360;//�ק�¦V
		    }
		    gsv.isInAnimation=false;
    }

    //�����H��ǰʵe
    public void RobotTurnDown()
    {
    	gsv.isInAnimation=true;
    	int currStep=0;
        currDegreeView=0;
		Action currAction=ActionGenerator.acArrayDown;
		    for(int i=0;i<currAction.totalStep;i++)
	    	{
	    		//�ק�D���
	    		for(float[] ad:currAction.Robotdata)
	    		{
	    			//�������
	    			int partIndex=(int) ad[0];
	    			//�ʧ@���A
	    			int aType=(int)ad[1]; 
	    			//�ثe�B�J��
	    			
	    			if(aType==1)//����
	    			{
	    				float startAngle=ad[2]+currDegree;
	    				float endAngle=ad[3]+currDegree;
	    				currDegreeView=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
	    				float x=ad[4];
	    				float y=ad[5];
	    				float z=ad[6];
	    				robot.bpArray[partIndex].rotate(currDegreeView, x, y, z);//��s�l���f�b�����f�y�Шt��������
	    			}  														//��s�l���f�b�����f�y�Шt�������઺���U����
	    		}  
	    		currStep++;
	    		
	    		//�N�D����Ш�iø����
	    		synchronized(gsv.gdDraw.dataLock)     
	    		{
	    			gsv.gdMain.copyTo(gsv.gdDraw);
	    		}
	    	}
		    currDegree=(currDegree+180)%360;
		    gsv.isInAnimation=false;
    }
    
    //�����H�q§���ʵe
    public void RobotSalute()
    {
    	Constant.IS_DRAW_WIN=false;
    	for(int i=0;i<ActionGenerator.acArraySalute.length;i++)
    	{
        	int currStep=0;
    		Action currAction=ActionGenerator.acArraySalute[i];
    		for(int j=0;j<currAction.totalStep;j++)
    		{
    			//�ק�D���
	    		for(float[] ad:currAction.Robotdata)
	    		{
	    			//�������
	    			int partIndex=(int) ad[0];
	    			//�ʧ@���A
	    			int aType=(int)ad[1]; 
	    			//�ثe�B�J��
	    			if(aType==1)//����
	    			{
	    				float startAngle=ad[2];
	    				float endAngle=ad[3];
	    				float currAngle=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
	    				float x=ad[4];
	    				float y=ad[5];
	    				float z=ad[6];
	    				robot.bpArray[partIndex].rotate(currAngle, x, y, z);//��s�l���f�b�����f�y�Шt��������
	    			}														//��s�l���f�b�����f�y�Шt�������઺���U����
	    		}    
	    		currStep++;
	    		
	    		//�N�D����Ш�iø����
	    		synchronized(gsv.gdDraw.dataLock)
	    		{
	    			gsv.gdMain.copyTo(gsv.gdDraw);
	    		}
    		}
    	}
    	Constant.IS_DRAW_WIN=true;
    }

    //�����c�l���ন�����A�M��q§�A�A��^��Ӫ���m
    public void Salute()
    {
    	gsv.isInAnimation=true;
    	if(currDegree==POSITIVE_MOVETO_Z)//0��
    	{
    		RobotTurn(currDegree,-(gsv.gdDraw.direction%360+185.64499f));
    		RobotSalute();
    		RobotTurn(-(gsv.gdDraw.direction%360+185.64499f),currDegree,gsv.isDrawWinOrLose);
    	}
    	else if(currDegree==POSITIVE_MOVETO_X){//�Y�G�Ox�b����V
    		RobotTurn(currDegree,-(gsv.gdDraw.direction%360+185.64499f));
    		RobotSalute();
    		RobotTurn(-(gsv.gdDraw.direction%360+185.64499f),currDegree,gsv.isDrawWinOrLose);
    	}
    	else if(currDegree==NEGATIVE_MOVETO_Z){//�Y�G�Oz�b�t��V
    		RobotTurn(currDegree,-(gsv.gdDraw.direction%360+185.64499f));
    		RobotSalute();
    		RobotTurn(-(gsv.gdDraw.direction%360+185.64499f),currDegree,gsv.isDrawWinOrLose);
    	}
    	else if(currDegree==NEGATIVE_MOVETO_X){//�Y�G�Ox�b�t��V
    		RobotTurn(currDegree,-(gsv.gdDraw.direction%360+185.64499f));
    		RobotSalute();
    		RobotTurn(-(gsv.gdDraw.direction%360+185.64499f),currDegree,gsv.isDrawWinOrLose);
    	}
    }
    
    public void RobotTurn(float curr,float direction)
    {
        Action ac=new Action();
        ac.totalStep=10000;
        ac.Robotdata=new float[][]
		{			
			{1,1,curr,direction,0,1,0}//body���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
		};
        
        int currStep=0;
		    for(int i=0;i<ac.totalStep;i++)
	    	{
	    		//�ק�D���
	    		for(float[] ad:ac.Robotdata)
	    		{
	    			//�������
	    			int partIndex=(int) ad[0];
	    			//�ʧ@���A
	    			int aType=(int)ad[1]; 
	    			//�ثe�B�J��
	    			
	    			if(aType==1)//����
	    			{
	    				float startAngle=ad[2];
	    				float endAngle=ad[3];
	    				float currAngle=startAngle+(endAngle-startAngle)*currStep/ac.totalStep;
	    				float x=ad[4];
	    				float y=ad[5];
	    				float z=ad[6];
	    				robot.bpArray[partIndex].rotate(currAngle, x, y, z);//��s�l���f�b�����f�y�Шt��������
	    			}  														//��s�l���f�b�����f�y�Шt�������઺���U����
	    		}    
	    		currStep++;
	    		
	    		//�N�D����Ш�iø����
	    		synchronized(gsv.gdDraw.dataLock)     
	    		{
	    			gsv.gdMain.copyTo(gsv.gdDraw);
	    		}
	    	}
    }
    
    public void RobotTurn(float curr,float direction,boolean isDrawWinOrLose)
    {
        Action ac=new Action();
        ac.totalStep=10000;
        ac.Robotdata=new float[][]
		{			
			{1,1,curr,direction,0,1,0}//body���ʧ@  ����s��  �ʧ@���A�]0-���� 1-����^�_�l���׭�  �������׭�   ����b�V�qXYZ
		};
        
        int currStep=0;
		    for(int i=0;i<ac.totalStep;i++)
	    	{
	    		//�ק�D���
	    		for(float[] ad:ac.Robotdata)
	    		{
	    			//�������
	    			int partIndex=(int) ad[0];
	    			//�ʧ@���A
	    			int aType=(int)ad[1]; 
	    			//�ثe�B�J��
	    			
	    			if(aType==1)//����
	    			{
	    				float startAngle=ad[2];
	    				float endAngle=ad[3];
	    				float currAngle=startAngle+(endAngle-startAngle)*currStep/ac.totalStep;
	    				float x=ad[4];
	    				float y=ad[5];
	    				float z=ad[6];
	    				robot.bpArray[partIndex].rotate(currAngle, x, y, z);//��s�l���f�b�����f�y�Шt��������
	    			}  														//��s�l���f�b�����f�y�Шt�������઺���U����
	    		}    
	    		currStep++;
	    		
	    		//�N�D����Ш�iø����
	    		synchronized(gsv.gdDraw.dataLock)     
	    		{
	    			gsv.gdMain.copyTo(gsv.gdDraw);
	    			
	    		}
	    		gsv.isDrawWinOrLose=true;
	    		gsv.isInAnimation=false;
	    	}
    }

}
