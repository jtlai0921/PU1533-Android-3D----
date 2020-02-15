package com.cw.game;

import com.cw.util.ShaderUtil;

import android.content.res.Resources;

public class ShaderManager
{
	final static int shaderCount=1;
	final static String[][] shaderName=
	{
		{"vertex_shadow.sh","frag_shadow.sh"}
	};
	static String[]mVertexShader=new String[shaderCount];
	static String[]mFragmentShader=new String[shaderCount];
	static int[] program=new int[shaderCount];
	
	public static void loadCodeFromFile(Resources r)
	{
		for(int i=0;i<shaderCount;i++)
		{
			//載入頂點著色器的指令稿內容       
	        mVertexShader[i]=ShaderUtil.loadFromAssetsFile(shaderName[i][0],r);
	        //載入片元著色器的指令稿內容 
	        mFragmentShader[i]=ShaderUtil.loadFromAssetsFile(shaderName[i][1], r);
		}	
	}
	
	//編譯3D物體的shader
	public static void compileShader()
	{
		for(int i=0;i<shaderCount;i++)
		{
			program[i]=ShaderUtil.createProgram(mVertexShader[i], mFragmentShader[i]);
			System.out.println("mProgram "+program[i]);
		}
	}
	//這裡傳回的是紋理的shader程式
	public static int getTextureShaderProgram()
	{
		return program[0];
	}
}
