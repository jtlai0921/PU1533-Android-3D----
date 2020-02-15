package com.bn.box;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import com.bn.zxl.GameView;
public class Box2DUtil {

	//建立木塊
	public static Rec creatRec(World world,float x,float y,float halfweight,float halfHeight,
			boolean isStatic ,GameView gameview,int textureId1
			
	)
	{
		PolygonDef recshap=new PolygonDef();//建立多邊形
		recshap.setAsBox(halfweight, halfHeight);//指定形狀為矩形
		if(isStatic)//是否靜態
		{
			recshap.density=0;
		}
		else
		{
			recshap.density=1.0f;//密度
		}
		recshap.friction=0.3f;//摩擦
		recshap.restitution=0f;//反彈補償
		
		BodyDef bodydef=new BodyDef();//建立bodydef
		Vec2 v=new Vec2(x,y);
		bodydef.position.set(v);//指定中心座標
		
		Body body=world.createBody(bodydef);//在世界中建立body
		body.createShape(recshap);//指定shape
		body.setMassFromShapes();//計算質量
		
		return new Rec(body,gameview,2*halfweight,2*halfHeight,textureId1);//傳回MyRec
	}
	public static ChiLun createChiLun(World world,float x,float y,float radius,float deepth,boolean isStatic,GameView gameview)
	{
		CircleDef ballshapdef=new CircleDef();//建立圓形
		if(isStatic)//是否靜態
		{
			ballshapdef.density=0;//密度0
		}
		else
		{
			ballshapdef.density=1.0f;//密度
		}
		
		ballshapdef.friction=0.1f;//摩擦力
		ballshapdef.restitution=0;//反貪補償
		ballshapdef.radius=radius;//半徑
		
		BodyDef bodydef=new BodyDef();//起始化body
		Vec2 v=new Vec2(x,y);//圓心座標
		bodydef.position.set(v);//加入
		
		Body body=world.createBody(bodydef);//在世界中建立body
		body.createShape(ballshapdef);//指定shape
		
		//設了一下球的初速度
		body.setMassFromShapes();//設定質量
		return new ChiLun(body,radius,deepth,gameview);//傳回MyBall
	}
	//建立木桶
	public static MuTong creatMuTong(World world,float x,float y,float halfWidth,float halfHeight,
			boolean isStatic ,GameView gameview
			
	)
	{
		PolygonDef recshap=new PolygonDef();//建立多邊形
		recshap.setAsBox(halfWidth, halfHeight);//指定形狀為矩形
		if(isStatic)//是否靜態
		{
			recshap.density=0;
		}
		else
		{
			recshap.density=1.0f;//密度
		}
		recshap.friction=0.3f;//摩擦
		recshap.restitution=0f;//反彈補償
		
		BodyDef bodydef=new BodyDef();//建立bodydef
		Vec2 v=new Vec2(x,y);
		bodydef.position.set(v);//指定中心座標
		
		Body body=world.createBody(bodydef);//在世界中建立body
		body.createShape(recshap);//指定shape
		body.setMassFromShapes();//計算質量
		
		return new MuTong(body,halfHeight,gameview);//傳回MyRec
	}
	//建立Rain
	public static Rain creatRain(World world,float x,float y,float halfweight,float halfHeight,
			boolean isStatic ,GameView gameview
			
	)
	{
		PolygonDef recshap=new PolygonDef();//建立多邊形
		recshap.setAsBox(halfweight, halfHeight);//指定形狀為矩形
		if(isStatic)//是否靜態
		{
			recshap.density=0;
		}
		else
		{
			recshap.density=1.0f;//密度
		}
		recshap.friction=0.3f;//摩擦
		recshap.restitution=0f;//反彈補償
		
		BodyDef bodydef=new BodyDef();//建立bodydef
		Vec2 v=new Vec2(x,y);
		bodydef.position.set(v);//指定中心座標
		
		Body body=world.createBody(bodydef);//在世界中建立body
		body.createShape(recshap);//指定shape
		body.setMassFromShapes();//計算質量
		
		return new Rain(body,gameview);//傳回MyRec
	}
	
	//建立小球
	public static Ball createBall(World world,float x,float y,float radius,boolean isStatic,int textureId ,GameView gameview)
	{
		CircleDef ballshapdef=new CircleDef();//建立圓形
		if(isStatic)//是否靜態
		{
			ballshapdef.density=0;
		}
		else
		{
			ballshapdef.density=0.8f;//密度
		}
		
		ballshapdef.friction=0.1f;//摩擦力
		ballshapdef.restitution=0;//反貪補償
		ballshapdef.radius=radius;//半徑
		
		BodyDef bodydef=new BodyDef();//起始化body
		Vec2 v=new Vec2(x,y);//圓心座標
		bodydef.position.set(v);//加入
		
		Body body=world.createBody(bodydef);//在世界中建立Body
		body.createShape(ballshapdef);//指定shape
		
		body.setMassFromShapes();//設定質量
		return new Ball(body,gameview,isStatic,textureId,radius);//傳回MyBall
	}
	public static ShiZiJia creatShizijia//建構器
	(
		float x,float y,//中心座標
		float width,//一個扇葉的長寬
		float height,
		boolean isStatic,//是否靜態
		World world,//世界
		GameView gameview
	)
	{
		PolygonDef shape=new PolygonDef();//建立多邊形
		shape.setAsBox(width, height);//多邊形為矩形
		if(isStatic)//是否靜態
		{
			shape.density=0;//密度
		}
		else
		{
			shape.density=1.0f;//密度
		}
		shape.friction=0;//摩擦
		shape.restitution=0f;//反彈補償
		BodyDef bodyDef=new BodyDef();
		bodyDef.position.set(x,y);//指定剛體位置
		Body bodyTemp=world.createBody(bodyDef);//在世界中建立Body
		bodyTemp.createShape(shape);//指定shape

			PolygonDef shape1=new PolygonDef();//建立第二個多邊形
			shape1.setAsBox(height, width);//剛體為矩形
			if(isStatic)//是否為靜態
			{
				shape1.density=0;//密度
			}
			else
			{
				shape1.density=1.0f;//密度
			}
			shape1.friction=0;//摩擦
			shape1.restitution=0f;//反彈補償
			bodyTemp.createShape(shape1);//為風車加入第二個形狀

		bodyTemp.setMassFromShapes();//計算質量
		return new ShiZiJia(bodyTemp,gameview,width*2,height*2,isStatic);
	}
	//建立多邊形物體(彩色)
	public static Txing creatTxing//建構器
	(  
		float x,//x座標
		float y,//y座標
	    float[][][] points,//點序列
        boolean isStatic,//是否為靜止的
        World world,//世界
        GameView gameview,
        int id
    )
	{    
		//建立剛體描述物件   
		BodyDef bodyDef = new BodyDef();   
		//設定位置
		bodyDef.position.set(x, y);  
		//在世界中建立剛體
		Body bodyTemp= world.createBody(bodyDef);
		//建立多邊形描述物件
		PolygonDef shape = new PolygonDef();   
		//設定密度
		if(isStatic)
		{
			shape.density = 0;
		}   
		else
		{
			shape.density = 1f;
		}   
		//設定摩擦系數
		shape.friction = 0.3f;   
		//設定能量損失率（反彈）
		shape.restitution = 0f;   
		float point[][]=points[0];
		for(float[] fa:point)
		{
			shape.addVertex(new Vec2(fa[0],fa[1]));
		}
		//指定剛體形狀
		bodyTemp.createShape(shape);   
		
		PolygonDef shape1 = new PolygonDef();   
		//設定密度
		if(isStatic)
		{
			shape1.density = 0;
		}   
		else
		{
			shape1.density = 1f;
		}   
		//設定摩擦系數
		shape1.friction = 0.3f;   
		//設定能量損失率（反彈）
		shape1.restitution = 0f;   
		float point1[][]=points[1];
		for(float[] fa:point1)
		{
			shape1.addVertex(new Vec2(fa[0],fa[1]));
		}
		//指定剛體形狀
		bodyTemp.createShape(shape1); 
		
		bodyTemp.setMassFromShapes(); 
//		float width=point[2][0]-point[0][0];
//		float height=point[2][1]-point[0][1];
		return new Txing(bodyTemp,gameview);
	}   
}
