package lwz.com.tank.game;

import java.util.ArrayList;

public class GameData {
	
	public  float maintank_x=0;//�D�Z�J���_�l��mX
	public  float maintank_y=-100;//�D�Z�J���_�l��mY
	public  float maintank_vAngle=0;//�D�Z�J���_�l�¦V
	public  float maintank_pAngle=0;//�D�Z�J���ު��_�l�¦V
	public  float mainyaogan_x=0;//�n�쪺�_�l��mX
	public  float mainyaogan_y=0;//�n�쪺�_�l��mY
	public  float mainfireflag=0;//�D�Z�J�o���ЧӦ�
	public  float []maintkwz={maintank_x,maintank_y,maintank_vAngle,mainyaogan_x,mainyaogan_y};//�D�Z�J��m�T���}�C
	public  float []maintkfp={mainfireflag,maintank_pAngle};//�D�Z�J�o���T���}�C
	
	public  float followtank_x=0;//�q�Z�J���_�l��mX
	public  float followtank_y=100;//�q�Z�J���_�l��mY
	public  float followtank_vAngle=0;//�q�Z�J���_�l�¦V
	public  float followtank_pAngle=0;//�q�Z�J���ު��_�l�¦V
	public  float followyaogan_x=0;//�n�쪺�_�l��mX
	public  float followyaogan_y=0;//�n�쪺�_�l��mY
	public  float followfireflag=0;//�q�Z�J�o���ЧӦ�
	public  float []followtkwz={followtank_x,followtank_y,followtank_vAngle,followyaogan_x,followyaogan_y};//�q�Z�J��m�T���}�C
	public  float []followtkfp={followfireflag,followtank_pAngle};//�q�Z�J�o���T���}�C
	
	
	public  float bulletX;//�l�u����mX
	public  float bulletY;//�l�u����mY
	public  float bulletZ;//�l�u����mZ
	public  float bulletAngle;//�l�u���¦V
	public float[] mainbullet=new float[4];//�D�l�u�T���}�C
	public float[] followbullet=new float[4];//�q�l�u�T���}�C
	public  ArrayList<float[]> mainbulletAl=new ArrayList<float[]>(); //�D�l�u�M��
	public  ArrayList<float[]> followbulletAl=new ArrayList<float[]>(); //�q�l�u�M��
	//���Ū�H�Χ�s����
	public Object dataLock=new Object();
	
	public  ArrayList<float[][]> mainTailAl=new ArrayList<float[][]>(); //���ڲM��
	public  ArrayList<float[][]> followTailAl=new ArrayList<float[][]>(); //���ڲM��
	 

}
