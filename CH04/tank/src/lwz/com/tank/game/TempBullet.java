package lwz.com.tank.game;

import static lwz.com.tank.activity.Constant.*;
import lwz.com.tank.activity.BulletTextureByVertex;
import lwz.com.tank.activity.Constant;
import lwz.com.tank.activity.TextureRect;

public class TempBullet {
	BulletTextureByVertex bullet;
	//�l�u���X����m
	float startX;
	float startY;
	float startZ;
	//�l�u����ɦ�m
	public float cuerrentX;
	public float cuerrentY;	
	public float cuerrentZ;
	//�l�u���X���t��
	float vx;
	float vy;
	float vz;	
	//�l�X�{����V
	 float  startAngle;
	////�l�u����ɤ�V
	float cuerrentAngle=0;
	//�l�u�o�g�᪺�֭p�ɶ�
	float timeLive=0;
	//�Ω�ø���z���ʵe�ĪG�����z�x�θs��
	TextureRect[] trExplo;
	//�z���ʵe�O�_�}�l�Ч�
	 public static boolean tempanmiStart=false;
	//�z���ʵe�د���
	 public static int tempanmiIndex=0;
	//�z����m
	 public static float tempanmiX;
	 public static float tempanmiY;
	 public static float tempanmiZ;
	//�z���ʵe���z�x�Ϊ��ЧӪO����
	float anmiYAngle;
	//MySurfaceView���Ѧ�
	public static int tankeflag1=0;
	public static int tankeflag2=0;
	//�l�u�ЧӦ�
	public int bulletflag=0;
	OtherSurfaceView ov;
	//�P�_�l�u���𪺼ЧӦ�
	public int flag=0;
	//huha )))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))) �����[�J���ڪ��a��
	public int  startMove=-1;
	public float [][]tempData=new float[9][3];
	public float [][]Data=new float[9][3];
	//huha )))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))) �����[�J���ڪ��a��
	public TempBullet(BulletTextureByVertex bullet,float startX,float startY,float startZ,float vx,float vy,float vz,TextureRect[] trExplo,float startAngle,OtherSurfaceView ov,int bulletflag)
	{
		this.trExplo=trExplo;
		this.bullet=bullet;
		this.startX=startX;
		this.startY=startY;
		this.startZ=startZ;
		this.vx=vx;
		this.vy=vy;
		this.vz=vz;
		this.startAngle=startAngle;
		this.ov=ov;	
		this.bulletflag=bulletflag;
		cuerrentAngle=startAngle;
		cuerrentX=startX;
		cuerrentY=startY;
		cuerrentZ=startZ;
	}
	
	public void go()
	{
		timeLive=timeLive+TIME_SPAN;
		cuerrentX=startX+vx*timeLive;
		cuerrentY=startY+vy*timeLive;
		cuerrentZ=startZ;
		
		int rows=lowWallmapdata.length;
		
		for(int i=0;i<rows;i++)
		{
			//18���l�u������          18*Math.cos(cuerrentAngle)���ثe���פU�l�u�bY�b�W������     18*Math.sin(cuerrentAngle)���ثe���פU�l�u�bX�b�W������
			if(cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle)))>lowWallmapdata[i][0]*Wall_UNIT_SIZE&&cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle)))<(lowWallmapdata[i][0]+lowWallmapdata[i][3])*Wall_UNIT_SIZE&&cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle)))<lowWallmapdata[i][1]*Wall_UNIT_SIZE&&cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle)))>(lowWallmapdata[i][1]-lowWallmapdata[i][4])*Wall_UNIT_SIZE)
			{
				flag++;
				if(cuerrentY-vy*TIME_SPAN+(18*Math.cos(Math.toRadians(cuerrentAngle)))<lowWallmapdata[i][1]*Wall_UNIT_SIZE&&cuerrentY-vy*TIME_SPAN+(18*Math.cos(Math.toRadians(cuerrentAngle)))>(lowWallmapdata[i][1]-lowWallmapdata[i][4])*Wall_UNIT_SIZE)
				{
					cuerrentAngle=-startAngle;
					timeLive=0;
					vx=-vx;
					startX=cuerrentX;
					startY=cuerrentY;
					cuerrentZ=startZ;
				}	
				else
				{
					cuerrentAngle=180-startAngle;
					timeLive=0;	
					vy=-vy;
					startX=cuerrentX;
					startY=cuerrentY;
					cuerrentZ=startZ;
				}
			}		
		}
		if(bulletflag==1)
		{
			if(cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle)))>ov.activity.gdFollowDraw.maintkwz[0]-30&&cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle)))<ov.activity.gdFollowDraw.maintkwz[0]+30&&cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle)))>ov.activity.gdFollowDraw.maintkwz[1]-30&&cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle)))<ov.activity.gdFollowDraw.maintkwz[1]+30)
			{
				ov.bulletCount--;
				tempanmiStart=true;	
				ov.activity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//�_��
				ov.activity.soundutil.playEffectsSound(2, 0);
				tempanmiIndex=0;
				tempanmiX=(float) (cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle))));
				tempanmiY=(float) (cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle))));
				tempanmiZ=cuerrentZ;
				ov.tempbulletAl.remove(this);
				tankeflag1++;
				ov.activity.sendMessage(tempanmiStart+"<#>"+tempanmiIndex+"<#>"+tempanmiX+"<#>"+tempanmiY+"<#>"+tempanmiZ+"<#>"+tankeflag1+"<#>");
			}
		}
		if(flag==2)
		{
			if(bulletflag==1)
			{
				ov.bulletCount--;
			}
			tempanmiStart=true;	
			ov.activity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//�_��
			ov.activity.soundutil.playEffectsSound(2, 0);
			tempanmiIndex=0;
			tempanmiX=(float) (cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle))));
			tempanmiY=(float) (cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle))));
			tempanmiZ=cuerrentZ;
			ov.tempbulletAl.remove(this);
			ov.activity.sendMessage(tempanmiStart+"<#>"+tempanmiIndex+"<#>"+tempanmiX+"<#>"+tempanmiY+"<#>"+tempanmiZ+"<#>"+tankeflag1+"<#>");
		}
	}
}
