package com.bn.cube.game;

public class CubeInfo{

	public static MySurfaceView mv;
	public static float [][]cubes;
	public static Cube[] cube;
	public static float[][] colors;
	public static int cubeNum=0;
	public CubeInfo(MySurfaceView mv)
	{
		CubeInfo.mv=mv;
	}
	//²Ä¤@Ãö
	
	public static void initCube()
	{
		
		if(Constant.level==1)
		{
			cubeNum=9;
			Constant.cube_length=4;
			cube=new Cube[Constant.cube_length];
			colors=new float[Constant.cube_length][4];
			colors[0]=new float[]{1.0f,1.0f,0f,0f};
			colors[1]=new float[]{0f,1.0f,0f,0f};
			colors[2]=new float[]{1.0f,0f,0f,0f};
			colors[3]=new float[]{0f,0f,1f,0f};
			cube[0]=new Cube(mv,colors[0]);
	        cube[1]=new Cube(mv,colors[1]);
	        cube[2]=new Cube(mv,colors[2]);
	        cube[3]=new Cube(mv,colors[3]);
	        cubes=new float[][]{
	        		
	        		{0,0,-15f,0,0,0},
	        		{1,0.5f,-15f,1,0,-1},
	        		{2f, 1f, -15f,2,0,1},
	        		{1f, -0.5f, -15f,1,0,2},
	        		{2f, -1f, -15f,2,0,3},
	        		{-1f, 0.5f, -15f,1,0,4},
	        		{-2f, 1f, -15f,2,0,-1},
	        		{-1f, -0.5f, -15f,1,0,0},
	        		{-2f, -1f, -15f,2,0,3}

	        };	       
		}
		if(Constant.level==2)
		{
			cubeNum=29;
			Constant.cube_length=4;
			cube=new Cube[Constant.cube_length];
			colors=new float[Constant.cube_length][4];
			colors[0]=new float[]{1.0f,1.0f,0f,0f};
			colors[1]=new float[]{0f,1.0f,0f,0f};
			colors[2]=new float[]{1.0f,0f,0f,0f};
			colors[3]=new float[]{0f,0f,1f,0f};
			cube[0]=new Cube(mv,colors[0]);
	        cube[1]=new Cube(mv,colors[1]);  
	        cube[2]=new Cube(mv,colors[2]);
	        cube[3]=new Cube(mv,colors[3]);
	        cubes=new float[][]{	        		
	        	{0,0,-15f,1,0,-1},{1,0,-15f,3,0,2},{2,0,-15f,1,0,-1},{3,0,-15f,1,0,0},
	        	{-1,0,-15f,3,0,0},{-2,0,-15f,1,0,-1},{-3,0,-15f,1,0,0},
	        	{0,0.5f,-15f,1,0,3},{1,0.5f,-15f,1,0,-1},{2,0.5f,-15f,1,0,3},{-1,0.5f,-15f,1,0,-1},{-2,0.5f,-15f,1,0,-1},
	        	{0,-0.5f,-15f,1,0,1},{1,-0.5f,-15f,1,0,-1},{2,-0.5f,-15f,1,0,2},{-1,-0.5f,-15f,1,0,-1},{-2,-0.5f,-15f,1,0,4},
	        	{0,-1,-15f,2,0,0},{1,-1,-15f,1,0,2},{-1,-1,-15f,1,0,4},{0,1,-15f,1,0,-1},{1,1,-15f,1,0,4},{-1,1,-15f,1,0,-1},
	        	{0,1.5f,-15f,1,0,0},{0,-1.5f,-15f,1,0,-1},
	        	{3,1.5f,-15f,0,0,1},{3,-1.5f,-15f,0,0,-1},{-3,-1.5f,-15f,0,0,2},{-3,1.5f,-15f,0,0,-1},
	        			
	        };	       
		}
		if(Constant.level==3)
		{
			cubeNum=35;
			Constant.cube_length=4;
			cube=new Cube[Constant.cube_length];
			colors=new float[Constant.cube_length][4];
			colors[0]=new float[]{1.0f,1.0f,0f,0f};
			colors[1]=new float[]{0f,1.0f,0f,0f};
			colors[2]=new float[]{1.0f,0f,0f,0f};
			colors[3]=new float[]{0f,0f,1f,0f};
			cube[0]=new Cube(mv,colors[0]);
	        cube[1]=new Cube(mv,colors[1]);
	        cube[2]=new Cube(mv,colors[2]);
	        cube[3]=new Cube(mv,colors[3]);
	        cubes=new float[][]{	
	        	{0,1.5f,-15,0,0,1},{1,1f,-15,0,0,-1},{2,0.5f,-15,0,0,-1},{3,0,-15,0,0,0},{1,-1f,-15,0,0,-1},{2,-0.5f,-15,0,0,2},
	        	{0,-1.5f,-15,0,0,-1},{-1,1f,-15,0,0,4},{-2,0.5f,-15,0,0,2},{-3,0,-15,0,0,3},{-1,-1f,-15,0,0,-1},{-2,-0.5f,-15,0,0,0},
	        	{0,1f,-15,1,0,1},{1,0.5f,-15,1,0,-1},{2,0,-15,1,0,-1},{1,-0.5f,-15,1,0,0},
	        	{0,-1f,-15,1,0,2},{-1,0.5f,-15,1,0,-1},{-2,0,-15,1,0,-1},{-1,-0.5f,-15,1,0,3},
	        	{0,0.5f,-15,2,0,-1},{0,-0.5f,-15,2,0,-1},{1,0f,-15,2,0,-1},{-1,0f,-15,2,0,4},
	        	{0,0,-15,3,0,0}, {0,2f,-15,0,0,-1}, {0,2.5f,-15,0,0,-1},{0,-2f,-15,0,0,2}, {0,-2.5f,-15,0,0,-1},
	        	{4,0,-15,0,0,2},{5,0,-15,0,0,3},{-4,0,-15,0,0,-1},{-5,0,-15,0,0,-1},{-6,0,-15,0,0,-1},{6,0,-15,0,0,0}
	        };	       
		}
		if(Constant.level==4)
		{
			cubeNum=36;
			Constant.cube_length=4;
			cube=new Cube[Constant.cube_length];
			colors=new float[Constant.cube_length][4];
			colors[0]=new float[]{1.0f,1.0f,0f,0f};
			colors[1]=new float[]{0f,1.0f,0f,0f};
			colors[2]=new float[]{1.0f,0f,0f,0f};
			colors[3]=new float[]{0f,0f,1f,0f}; 
			cube[0]=new Cube(mv,colors[0]);
	        cube[1]=new Cube(mv,colors[1]);
	        cube[2]=new Cube(mv,colors[2]);
	        cube[3]=new Cube(mv,colors[3]);
	        cubes=new float[][]{	
	        	{-2.5f,1.25f,-15f,1,0,-1},{-1.5f,1.25f,-15f,1,0,-1},{-0.5f,1.25f,-15f,1,0,1},{0.5f,1.25f,-15f,1,0,3},{1.5f,1.25f,-15f,1,0,-1},{2.5f,1.25f,-15f,1,0,-1},
	        	{-2.5f,0.75f,-15f,1,0,-1},{-1.5f,0.75f,-15f,0,0,1},{-0.5f,0.75f,-15f,0,0,2},{0.5f,0.75f,-15f,0,0,-1},{1.5f,0.75f,-15f,2,0,3},{2.5f,0.75f,-15f,1,0,4},
	        	{-2.5f,0.25f,-15f,1,0,1},{-1.5f,0.25f,-15f,2,0,-1},{-0.5f,0.25f,-15f,0,0,-1},{0.5f,0.25f,-15f,2,0,2},{1.5f,0.25f,-15f,2,0,4},{2.5f,0.25f,-15f,1,0,-1},
	        	{-2.5f,-0.25f,-15f,1,0,-1},{-1.5f,-0.25f,-15f,2,0,2},{-0.5f,-0.25f,-15f,2,0,3},{0.5f,-0.25f,-15f,0,0,-1},{1.5f,-0.25f,-15f,2,0,-1},{2.5f,-0.25f,-15f,1,0,1},
	        	{-2.5f,-0.75f,-15f,1,0,2},{-1.5f,-0.75f,-15f,2,0,-1},{-0.5f,-0.75f,-15f,0,0,3},{0.5f,-0.75f,-15f,0,0,1},{1.5f,-0.75f,-15f,0,0,-1},{2.5f,-0.75f,-15f,1,0,2},
	        	{-2.5f,-1.25f,-15f,1,0,3},{-1.5f,-1.25f,-15f,1,0,-1},{-0.5f,-1.25f,-15f,1,0,2},{0.5f,-1.25f,-15f,1,0,4},{1.5f,-1.25f,-15f,1,0,-1},{2.5f,-1.25f,-15f,1,0,3}
	        	
	        };	       
		}
		if(Constant.level==5)
		{
			cubeNum=24;
			Constant.cube_length=4;
			cube=new Cube[Constant.cube_length];
			colors=new float[Constant.cube_length][4];
			colors[0]=new float[]{1.0f,1.0f,0f,0f};
			colors[1]=new float[]{0f,1.0f,0f,0f};
			colors[2]=new float[]{1.0f,0f,0f,0f};
			colors[3]=new float[]{0f,0f,1f,0f};
			cube[0]=new Cube(mv,colors[0]);
	        cube[1]=new Cube(mv,colors[1]);
	        cube[2]=new Cube(mv,colors[2]);
	        cube[3]=new Cube(mv,colors[3]);
	        cubes=new float[][]{	
	        	{-1.5f,1.25f,-17,0,0,2},{-2.5f,0.75f,-17,0,0,-1},{-1.5f,0.75f,-17,0,0,-1},{-1.5f,0.75f,-16.5f,0,0,-1},{-0.5f,0.75f,-17,0,0,1},{-1.5f,0.25f,-17,0,0,0},
	        	{1.5f,1.25f,-14,0,0,4},{2.5f,0.75f,-14,0,0,-1},{1.5f,0.75f,-14,0,0,-1},{1.5f,0.75f,-13.5f,0,0,2},{0.5f,0.75f,-14,0,0,-1},{1.5f,0.25f,-14,0,0,3},
	        	{1.5f,-1.25f,-11,1,0,-1},{2.5f,-0.75f,-11,1,0,4},{1.5f,-0.75f,-11,1,0,-1},{1.5f,-0.75f,-10.5f,1,0,3},{0.5f,-0.75f,-11,1,0,-1},{1.5f,-0.25f,-11,1,0,2},
	        	{-1.5f,-1.25f,-9,1,0,-1},{-2.5f,-0.75f,-9,1,0,2},{-1.5f,-0.75f,-9,1,0,0},{-1.5f,-0.75f,-8.5f,1,0,-1},{-0.5f,-0.75f,-9,1,0,0},{-1.5f,-0.25f,-9,1,0,-1},
	        };	       
		}
		if(Constant.level==6)
		{
			cubeNum=16;
			Constant.cube_length=4;
			cube=new Cube[Constant.cube_length];
			colors=new float[Constant.cube_length][4];
			colors[0]=new float[]{1.0f,1.0f,0f,0f};
			colors[1]=new float[]{0f,1.0f,0f,0f};
			colors[2]=new float[]{1.0f,0f,0f,0f};
			colors[3]=new float[]{0f,0f,1f,0f};
			cube[0]=new Cube(mv,colors[0]);
	        cube[1]=new Cube(mv,colors[1]);
	        cube[2]=new Cube(mv,colors[2]);
	        cube[3]=new Cube(mv,colors[3]);
	        cubes=new float[][]{	        		
	        		{0,0,-15f,3,0,3},
	        		{2.8f,0f,-15f,1,0,-1},
	        		{0,-2.5f,-15f,1,0,1},
	        		{-2.8f,0f,-15f,1,0,-1}, 
	        		{1.75f,1.25f,-15f,0,0,2},
	        		{1.75f,-1.25f,-15f,0,0,4},
	        		{-1.75f,-1.25f,-15f,0,0,-1},
	        		{-1.75f,1.25f,-15f,0,0,-1},
	        		{2.5f,0.625f,-15f,0,0,0},
	        		{-2.5f,0.625f,-15f,0,0,-1},
	        		{2.5f,-0.625f,-15f,0,0,0},
	        		{-2.5f,-0.625f,-15f,0,0,2},
	        		{1.2f,-1.88f,-15,0,0,-1},
	        		{-1.2f,-1.88f,-15,0,0,3},
	        		{0.6f,0.625f,-15f,0,0,4},
	        		{-0.6f,0.625f,-15f,0,0,0},
	        };	       
		}
		if(Constant.level==7)
		{
			cubeNum=54;
			Constant.cube_length=4;
			cube=new Cube[Constant.cube_length];
			colors=new float[Constant.cube_length][4];
			colors[0]=new float[]{1.0f,1.0f,0f,0f};
			colors[1]=new float[]{0f,1.0f,0f,0f};
			colors[2]=new float[]{1.0f,0f,0f,0f};
			colors[3]=new float[]{0f,0f,1f,0f};
			cube[0]=new Cube(mv,colors[0]);
	        cube[1]=new Cube(mv,colors[1]);
	        cube[2]=new Cube(mv,colors[2]);
	        cube[3]=new Cube(mv,colors[3]);
	        cubes=new float[][]{	        		
	        	{1,0.5f,-15,0,0,0},
	        	{1,0,-15,0,0,-1},{2,0,-15,0,0,-1},
	        	{1,-0.5f,-15,0,0,0},{2,-0.5f,-15,0,0,2},{3,-0.5f,-15,0,0,-1},
	        	{1,-1f,-15,0,0,-1},{2,-1f,-15,0,0,3},{3,-1f,-15,0,0,-1},{4,-1f,-15,0,0,0},
	        	{-1,0.5f,-15,0,0,-1},
	        	{-1,0,-15,0,0,-1},{-2,0,-15,0,0,2},
	        	{-1,-0.5f,-15,0,0,-1},{-2,-0.5f,-15,0,0,0},{-3,-0.5f,-15,0,0,1},
	        	{-1,-1f,-15,0,0,3},{-2,-1f,-15,0,0,-1},{-3,-1f,-15,0,0,2},{-4,-1f,-15,0,0,-1},
	        	{0,-1f,-15,0,0,-1},{0,-0.5f,-15,0,0,1},{0,0f,-15,0,0,-1},{0,0.5f,-15,0,0,3},{0,1f,-15,0,0,0},
	        	
	        	{1,0,-14,1,0,-1},
	        	{1,-0.5f,-14,1,0,2},{2,-0.5f,-14,1,0,-1},
	        	{1,-1f,-14,1,0,-1},{2,-1f,-14,1,0,3},{3,-1f,-14,1,0,1},
	        	{-1,0,-14,1,0,-1},
	        	{-1,-0.5f,-14,1,0,4},{-2,-0.5f,-14,1,0,-1},
	        	{-1,-1f,-14,1,0,3},{-2,-1f,-14,1,0,-1},{-3,-1f,-14,1,0,2},
	        	{0,-1f,-14,1,0,-1},{0,-0.5f,-14,1,0,0},{0,0f,-14,1,0,4},{0,0.5f,-14,1,0,-1},
	        	
	        	{1,-0.5f,-13,0,0,1},
	        	{1,-1f,-13,0,0,0},{2,-1f,-13,0,0,-1},
	        	{-1,-0.5f,-13,0,0,4},
	        	{-1,-1f,-13,0,0,2},{-2,-1f,-13,0,0,1},
	        	{0,-1f,-13,0,0,-1},{0,-0.5f,-13,0,0,2},{0,0f,-13,0,0,-1},
	        	
	        	{1,-1f,-12,1,0,0},
	        	{-1,-1f,-12,1,0,2},
	        	{0,-1f,-12,1,0,3},{0,-0.5f,-12,1,0,4},
	        };	       
		}
	}
		
	
}
