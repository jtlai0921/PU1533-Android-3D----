package com.f.pingpong;

import android.content.res.Resources;

import com.f.util.ShaderUtil;

public class ShaderManager 
{
	public static int shadercount = 8;
	public static String[] vertexshader = new String[shadercount];
	public static String[] fragmentshader = new String[shadercount];
	public static int[] program=new int[12];
	
	public static void loadShaderString(Resources res)
	{
		//MainBall
		vertexshader[0] = ShaderUtil.loadFromAssetsFile("vertex_mainball_shadow.sh",res);
		fragmentshader[0] = ShaderUtil.loadFromAssetsFile("frag_mainball_shadow.sh",res);
		
		//BatAiObj,MainBat
		vertexshader[1] = ShaderUtil.loadFromAssetsFile("vertex_light.sh",res);
		fragmentshader[1] = ShaderUtil.loadFromAssetsFile("frag_light.sh",res);
		
		//GameFloorWallRect,MainFloor
		vertexshader[2] = ShaderUtil.loadFromAssetsFile("vertex_light.sh",res);
		fragmentshader[2] = ShaderUtil.loadFromAssetsFile("frag_floor.sh",res);
		
		//spring,MainMenuRect
		vertexshader[3] = ShaderUtil.loadFromAssetsFile("vertex_spring.sh",res);
		fragmentshader[3] = ShaderUtil.loadFromAssetsFile("frag_spring.sh",res);
		
		//maintable
		vertexshader[4] = ShaderUtil.loadFromAssetsFile("vertex_maintable_shadow.sh",res);
		fragmentshader[4] = ShaderUtil.loadFromAssetsFile("frag_maintable_shadow.sh",res);
		
		//flyflag
		vertexshader[5] = ShaderUtil.loadFromAssetsFile("vertex_flyflag.sh",res);
		fragmentshader[5] = ShaderUtil.loadFromAssetsFile("frag_flyflag.sh",res);
		
		//BatNetRect,GameTableCube
		vertexshader[6] = ShaderUtil.loadFromAssetsFile("vertex_gametable_shadow.sh",res);
		fragmentshader[6] = ShaderUtil.loadFromAssetsFile("frag_gametable_shadow.sh",res);
		
		//GameBall
		vertexshader[7] = ShaderUtil.loadFromAssetsFile("vertex_gameball_shadow.sh",res);
		fragmentshader[7] = ShaderUtil.loadFromAssetsFile("frag_gameball_shadow.sh",res);
	}
	
	//½sÄ¶shader
	public static void compileMainShaderReal()
	{
		program[0] = ShaderUtil.createProgram(vertexshader[0], fragmentshader[0]);//MainBall
		program[1] = ShaderUtil.createProgram(vertexshader[1], fragmentshader[1]);//MainBat
		program[2] = ShaderUtil.createProgram(vertexshader[2], fragmentshader[2]);//MainFloor
		program[3] = ShaderUtil.createProgram(vertexshader[3], fragmentshader[3]);//MainMenuRect
		program[4] = ShaderUtil.createProgram(vertexshader[4], fragmentshader[4]);//maintables
		program[5] = ShaderUtil.createProgram(vertexshader[5], fragmentshader[5]);//flyflag
	}
	
	//½sÄ¶shader
	public static void scompileGameShaderReal()
	{
		program[1] = ShaderUtil.createProgram(vertexshader[1], fragmentshader[1]);//MainBat
		program[7] = ShaderUtil.createProgram(vertexshader[2], fragmentshader[2]);//GameFloorWallRect
		program[8] = ShaderUtil.createProgram(vertexshader[3], fragmentshader[3]);//spring
		program[9] = ShaderUtil.createProgram(vertexshader[5], fragmentshader[5]);//flyflag
		program[10] = ShaderUtil.createProgram(vertexshader[6], fragmentshader[6]);//BatNetRect,GameTableCube
		program[11] = ShaderUtil.createProgram(vertexshader[7], fragmentshader[7]);//GameBall
	}
}
