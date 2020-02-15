package tstc.fxq.utils;

import tstc.fxq.main.MySurfaceView;

/*
 * 用於管理著色器的類別
 * 
 * 頂點
 */
public class ShaderManager {
	
	//shader檔名陣列
	public static String[] vertexShaders = {"vertex_color.sh","vertex_shadow.sh","vertex_tex_board.sh","vertex_tex_btn.sh","vertex_tex_light.sh","vertex_tex.sh","vertex_progress.sh"};
	public static String[] fragShaders = {"frag_color.sh","frag_shadow.sh","frag_tex_board.sh","frag_tex_btn.sh","frag_tex_light.sh","frag_tex.sh","frag_progress.sh"};
	
	//shader源程式碼陣列
	public static String[] vertexCodes = new String[vertexShaders.length];
	public static String[] fragCodes = new String[fragShaders.length];
	
	//自訂著色管執行緒序id陣列
	public static int[] programs = new int[vertexShaders.length];
	
	//從檔案中載入shader源程式碼的方法（可以線上程中呼叫）
	public static void loadShadersFromFile(MySurfaceView mv){
		//載入頂點著色器的指令稿內容
		for(int i=0; i<vertexShaders.length; i++){
			vertexCodes[i] = ShaderUtil.loadFromAssetsFile(vertexShaders[i], mv.getResources());
			ShaderUtil.checkGlError("==ss==");   
		}
		
        //載入片元著色器的指令稿內容
		for(int i=0; i<fragShaders.length; i++){
			fragCodes[i] = ShaderUtil.loadFromAssetsFile(fragShaders[i], mv.getResources());
			ShaderUtil.checkGlError("==ss=="); 
		}
	}
	
	//編譯shader源程式碼的方法（不能線上程中呼叫）
	public static void compileShaders(){
		//基於頂點著色器與片元著色器建立程式
		for(int i=0; i<programs.length; i++){
			programs[i] = ShaderUtil.createProgram(vertexCodes[i], fragCodes[i]);
		}
	}
	
	//*********************************載入界面的shader begin************************************************
	//載入載入界面的shader
	public static void loadWelcomeSurfaceShaderFromFile(MySurfaceView mv)
	{
		vertexCodes[5] = ShaderUtil.loadFromAssetsFile(vertexShaders[5], mv.getResources());
		ShaderUtil.checkGlError("==ss=="); 
		
		fragCodes[5] = ShaderUtil.loadFromAssetsFile(fragShaders[5], mv.getResources());
		ShaderUtil.checkGlError("==ss=="); 
	}
	
	//編譯載入界面的shader
	public static void compileWelcomeSurfaceShader()
	{
		programs[5] = ShaderUtil.createProgram(vertexCodes[5], fragCodes[5]);
	}
	//*********************************載入界面的shader end************************************************
	
	
	//*******************************進度指示器界面的shader begin***********************************
	//載入進度指示器界面的shader
	//載入載入界面的shader
	public static void loadProgressBarShaderFromFile(MySurfaceView mv)
	{
		vertexCodes[6] = ShaderUtil.loadFromAssetsFile(vertexShaders[6], mv.getResources());
		ShaderUtil.checkGlError("==ss=="); 
		
		fragCodes[6] = ShaderUtil.loadFromAssetsFile(fragShaders[6], mv.getResources());
		ShaderUtil.checkGlError("==ss=="); 
	}
	
	//編譯載入界面的shader
	public static void compileProgressBarShader()
	{
		programs[6] = ShaderUtil.createProgram(vertexCodes[6], fragCodes[6]);
	}
	
	//*******************************進度指示器界面的shader end***********************************
	
	
	
	
	//取得各著色器id的方法
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
