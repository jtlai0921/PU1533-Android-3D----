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
			//���J���I�ۦ⾹�����O�Z���e       
	        mVertexShader[i]=ShaderUtil.loadFromAssetsFile(shaderName[i][0],r);
	        //���J�����ۦ⾹�����O�Z���e 
	        mFragmentShader[i]=ShaderUtil.loadFromAssetsFile(shaderName[i][1], r);
		}	
	}
	
	//�sĶ3D���骺shader
	public static void compileShader()
	{
		for(int i=0;i<shaderCount;i++)
		{
			program[i]=ShaderUtil.createProgram(mVertexShader[i], mFragmentShader[i]);
			System.out.println("mProgram "+program[i]);
		}
	}
	//�o�̶Ǧ^���O���z��shader�{��
	public static int getTextureShaderProgram()
	{
		return program[0];
	}
}
