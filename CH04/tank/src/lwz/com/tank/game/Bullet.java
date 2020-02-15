package lwz.com.tank.game;

import lwz.com.tank.activity.BulletTextureByVertex;
import lwz.com.tank.activity.Constant;
import lwz.com.tank.activity.TextureRect;
import static lwz.com.tank.activity.Constant.*;

public class Bullet {
	//�Ω�ø��l�u���ҫ��Ѧ�
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
		 public static boolean anmiStart=false;
		//�z���ʵe�د���
		 public static int anmiIndex=0;
		//�z����m
		 public static float anmiX;
		 public static float anmiY;
		 public static float anmiZ;
		//�z���ʵe���z�x�Ϊ��ЧӪO����
		float anmiYAngle;
		//MySurfaceView���Ѧ�
		//�Z�J�ͩR�p�ƾ�
		public static int tankeflag1=0;
		public static int tankeflag2=0;
		//�l�u�ЧӦ�
		public int bulletflag=0;
		MySurfaceView mv;
		//�P�_�l�u���𪺼ЧӦ�
		public int flag=0;

		//huha )))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))) �����[�J���ڪ��a��
		public int  startMove=-1;
		public float [][]tempData=new float[9][3];
		public float [][]Data=new float[9][3];
		//huha )))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))) �����[�J���ڪ��a��
		
		public Bullet(BulletTextureByVertex bullet,float startX,float startY,float startZ,float vx,float vy,float vz,TextureRect[] trExplo,float startAngle,MySurfaceView mv,int bulletflag)
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
			this.mv=mv;	
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
				if(cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle)))>mv.activity.gdFollowDraw.followtkwz[0]-30&&cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle)))<mv.activity.gdFollowDraw.followtkwz[0]+30&&cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle)))>mv.activity.gdFollowDraw.followtkwz[1]-30&&cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle)))<mv.activity.gdFollowDraw.followtkwz[1]+30)
				{
					mv.bulletCount--;
					anmiStart=true;	
					mv.activity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//�_��
					mv.activity.soundutil.playEffectsSound(2,0);
					anmiIndex=0;
					anmiX=(float) (cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle))));
					anmiY=(float) (cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle))));
					anmiZ=cuerrentZ;
					mv.bulletAl.remove(this);
					tankeflag2++;
					mv.activity.sendMessage(anmiStart+"<#>"+anmiIndex+"<#>"+anmiX+"<#>"+anmiY+"<#>"+anmiZ+"<#>"+tankeflag2+"<#>");
				}
			}
			if(flag==2)
			{
				if(bulletflag==1)
				{
					mv.bulletCount--;
				}
				anmiStart=true;	
				mv.activity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//�_��
				mv.activity.soundutil.playEffectsSound(2, 0);
				anmiIndex=0;
				anmiX=(float) (cuerrentX+(18*Math.sin(Math.toRadians(cuerrentAngle))));
				anmiY=(float) (cuerrentY+(18*Math.cos(Math.toRadians(cuerrentAngle))));
				anmiZ=cuerrentZ;
				mv.bulletAl.remove(this);
				mv.activity.sendMessage(anmiStart+"<#>"+anmiIndex+"<#>"+anmiX+"<#>"+anmiY+"<#>"+anmiZ+"<#>"+tankeflag2+"<#>");
			}
		}
}
