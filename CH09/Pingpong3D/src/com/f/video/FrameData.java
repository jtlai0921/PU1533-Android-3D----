package com.f.video;

import java.io.Serializable;
import java.util.*;

import javax.vecmath.Vector3f;

public class FrameData implements Serializable
{
	private static final long serialVersionUID = 530561501682643649L;
	
	public static List<FrameData> playFrameDataList=new ArrayList<FrameData>();
	
	public static List<FrameData> helpFrameDataList=new ArrayList<FrameData>();
	
    public static int currIndex;
    
    public float cx,cy;
    public Vector3f positionPlayer=new Vector3f();
    public Vector3f positionAI=new Vector3f();
    public Vector3f positionBall=new Vector3f();
    public int task[];
    public float zAngle;
	public float zAngleAI;
	public List<Vector3f> lList = new ArrayList<Vector3f>();//位置(Location)清單
    public boolean is_paddle = false;
	
    public int pointsplayer;
    public int pointAi;
    public float tx,ty;//TouchPoint
    public boolean isTaskDone=false;
    public boolean ishsootman;//誰發球
    public long timestamp;
    public FrameData(){}
    
    public static FrameData LoadData
    (
    	float cxIn,float cyIn,
    	Vector3f positionPlayerIn,
    	Vector3f positionAIIn,
    	Vector3f positionBallIn,
    	int[] taskIn,
    	float zAngleIn,
    	float zAngleInAI,
    	List<Vector3f> lListIn,
    	boolean is_paddleIn,
    	int pointsplayerIn,
    	int pointsAiIn,
    	float txIn,
    	float tyIn,
    	long timeIn,
    	boolean isshootmanIn
    )
    {
    	FrameData result=new FrameData();
    	
    	result.cx=cxIn;
    	result.cy=cyIn;
    	copyVector3f(result.positionPlayer,positionPlayerIn);
    	copyVector3f(result.positionAI,positionAIIn);
    	copyVector3f(result.positionBall,positionBallIn);
    	
    	result.task=new int[taskIn.length];
    	for(int i=0;i<taskIn.length;i++)
    	{
    		result.task[i]=taskIn[i];
    	}
    	
    	result.zAngle = zAngleIn;
    	result.zAngleAI = zAngleInAI;
    	
    	copyVecList(result.lList,lListIn);
    	result.is_paddle = is_paddleIn;
    	
    	result.pointAi = pointsAiIn;
    	result.pointsplayer = pointsplayerIn;
    	
    	result.tx = txIn;
    	result.ty = tyIn;
    	
    	result.timestamp = timeIn;
    	result.ishsootman = isshootmanIn;
    	return result;
    }
    
    public static void copyVector3f(Vector3f target,Vector3f source)
    {
    	target.set(source.x, source.y,source.z);
    }
    
    public static void copyVecList(List<Vector3f> target,List<Vector3f> source)
    {
    	for(int i=0;i<source.size();i++)
    	{
    		Vector3f temp = source.get(i);
    		target.add(new Vector3f(temp.x, temp.y, temp.z));
    	}
    }
}