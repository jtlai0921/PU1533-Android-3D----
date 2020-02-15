package tstc.fxq.utils;

import tstc.fxq.main.MySurfaceView;

/*
 * �Ω�޲z�ۦ⾹�����O
 * 
 * ���I
 */
public class ShaderManager {
	
	//shader�ɦW�}�C
	public static String[] vertexShaders = {"vertex_color.sh","vertex_shadow.sh","vertex_tex_board.sh","vertex_tex_btn.sh","vertex_tex_light.sh","vertex_tex.sh","vertex_progress.sh"};
	public static String[] fragShaders = {"frag_color.sh","frag_shadow.sh","frag_tex_board.sh","frag_tex_btn.sh","frag_tex_light.sh","frag_tex.sh","frag_progress.sh"};
	
	//shader���{���X�}�C
	public static String[] vertexCodes = new String[vertexShaders.length];
	public static String[] fragCodes = new String[fragShaders.length];
	
	//�ۭq�ۦ�ް������id�}�C
	public static int[] programs = new int[vertexShaders.length];
	
	//�q�ɮפ����Jshader���{���X����k�]�i�H�u�W�{���I�s�^
	public static void loadShadersFromFile(MySurfaceView mv){
		//���J���I�ۦ⾹�����O�Z���e
		for(int i=0; i<vertexShaders.length; i++){
			vertexCodes[i] = ShaderUtil.loadFromAssetsFile(vertexShaders[i], mv.getResources());
			ShaderUtil.checkGlError("==ss==");   
		}
		
        //���J�����ۦ⾹�����O�Z���e
		for(int i=0; i<fragShaders.length; i++){
			fragCodes[i] = ShaderUtil.loadFromAssetsFile(fragShaders[i], mv.getResources());
			ShaderUtil.checkGlError("==ss=="); 
		}
	}
	
	//�sĶshader���{���X����k�]����u�W�{���I�s�^
	public static void compileShaders(){
		//����I�ۦ⾹�P�����ۦ⾹�إߵ{��
		for(int i=0; i<programs.length; i++){
			programs[i] = ShaderUtil.createProgram(vertexCodes[i], fragCodes[i]);
		}
	}
	
	//*********************************���J�ɭ���shader begin************************************************
	//���J���J�ɭ���shader
	public static void loadWelcomeSurfaceShaderFromFile(MySurfaceView mv)
	{
		vertexCodes[5] = ShaderUtil.loadFromAssetsFile(vertexShaders[5], mv.getResources());
		ShaderUtil.checkGlError("==ss=="); 
		
		fragCodes[5] = ShaderUtil.loadFromAssetsFile(fragShaders[5], mv.getResources());
		ShaderUtil.checkGlError("==ss=="); 
	}
	
	//�sĶ���J�ɭ���shader
	public static void compileWelcomeSurfaceShader()
	{
		programs[5] = ShaderUtil.createProgram(vertexCodes[5], fragCodes[5]);
	}
	//*********************************���J�ɭ���shader end************************************************
	
	
	//*******************************�i�׫��ܾ��ɭ���shader begin***********************************
	//���J�i�׫��ܾ��ɭ���shader
	//���J���J�ɭ���shader
	public static void loadProgressBarShaderFromFile(MySurfaceView mv)
	{
		vertexCodes[6] = ShaderUtil.loadFromAssetsFile(vertexShaders[6], mv.getResources());
		ShaderUtil.checkGlError("==ss=="); 
		
		fragCodes[6] = ShaderUtil.loadFromAssetsFile(fragShaders[6], mv.getResources());
		ShaderUtil.checkGlError("==ss=="); 
	}
	
	//�sĶ���J�ɭ���shader
	public static void compileProgressBarShader()
	{
		programs[6] = ShaderUtil.createProgram(vertexCodes[6], fragCodes[6]);
	}
	
	//*******************************�i�׫��ܾ��ɭ���shader end***********************************
	
	
	
	
	//���o�U�ۦ⾹id����k
	public static int getColorShader(){
		return programs[0];
	}
	public static int getShadowShader(){
		return programs[1];
	}
	public static int getTexBoardShader(){
		return programs[2];
	}
	public static int getTexBtnShader(){
		return programs[3];
	}
	public static int getTexLightShader(){
		return programs[4];
	}
	public static int getTexShader(){
		return programs[5];
	}
	public static int getProgressBarShader(){
		return programs[6];
	}
}
