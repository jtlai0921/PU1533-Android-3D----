package lwz.com.tank.game;

import java.util.ArrayList;

public class GameData {
	
	public  float maintank_x=0;//主坦克的起始位置X
	public  float maintank_y=-100;//主坦克的起始位置Y
	public  float maintank_vAngle=0;//主坦克的起始朝向
	public  float maintank_pAngle=0;//主坦克炮管的起始朝向
	public  float mainyaogan_x=0;//搖桿的起始位置X
	public  float mainyaogan_y=0;//搖桿的起始位置Y
	public  float mainfireflag=0;//主坦克發炮標志位
	public  float []maintkwz={maintank_x,maintank_y,maintank_vAngle,mainyaogan_x,mainyaogan_y};//主坦克位置訊息陣列
	public  float []maintkfp={mainfireflag,maintank_pAngle};//主坦克發炮訊息陣列
	
	public  float followtank_x=0;//從坦克的起始位置X
	public  float followtank_y=100;//從坦克的起始位置Y
	public  float followtank_vAngle=0;//從坦克的起始朝向
	public  float followtank_pAngle=0;//從坦克炮管的起始朝向
	public  float followyaogan_x=0;//搖桿的起始位置X
	public  float followyaogan_y=0;//搖桿的起始位置Y
	public  float followfireflag=0;//從坦克發炮標志位
	public  float []followtkwz={followtank_x,followtank_y,followtank_vAngle,followyaogan_x,followyaogan_y};//從坦克位置訊息陣列
	public  float []followtkfp={followfireflag,followtank_pAngle};//從坦克發炮訊息陣列
	
	
	public  float bulletX;//子彈的位置X
	public  float bulletY;//子彈的位置Y
	public  float bulletZ;//子彈的位置Z
	public  float bulletAngle;//子彈的朝向
	public float[] mainbullet=new float[4];//主子彈訊息陣列
	public float[] followbullet=new float[4];//從子彈訊息陣列
	public  ArrayList<float[]> mainbulletAl=new ArrayList<float[]>(); //主子彈清單
	public  ArrayList<float[]> followbulletAl=new ArrayList<float[]>(); //從子彈清單
	//資料讀以及更新的鎖
	public Object dataLock=new Object();
	
	public  ArrayList<float[][]> mainTailAl=new ArrayList<float[][]>(); //尾巴清單
	public  ArrayList<float[][]> followTailAl=new ArrayList<float[][]>(); //尾巴清單
	 

}
