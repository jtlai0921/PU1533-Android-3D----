precision mediump float;
uniform int isShadowFrag;//是否繪制陰影
uniform sampler2D sTextureBall;//桌球紋理內容資料
uniform sampler2D sTextureTable;//桌面紋理內容資料
varying vec2 vTextureCoord; //接收從頂點著色器過來的參數
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
varying vec3 vMMPosition;//變換後的位置
void main()                         
{    
   	//將計算出的彩色給此片元
   	vec4 finalColor;

   	if(isShadowFrag==0)
   	{
   		//繪制球本身，紋理從球紋理取樣
	    finalColor=texture2D(sTextureBall, vTextureCoord);
	    gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;//給此片元彩色值
   	}
   	else
   	{   		
   		//位置變換後的x、z座標值
	   	float x = vMMPosition.x;
	   	float z = vMMPosition.z;
	   	//若果位置不在球台內，將片元捨棄
	   	if(   		
	   		//位置不在矩形內
	   		x < -16.08 ||
	   		x > 16.08 ||
	   		z < -10.08 ||
	   		z > 10.08 ||
	   		//位置在四角
	   		x+z < -24.88 ||
	   		z-x < -24.88 ||
	   		z-x > 24.88 ||
	   		x+z > 24.88
	   	)
	   	{
	   		finalColor=texture2D(sTextureTable, vTextureCoord); 
	   		finalColor.a = 0.0;
	   		gl_FragColor = finalColor*ambient*1.6;//給此片元彩色值
	   	}
	   	else
	   	{
	   		//繪制本影
		     finalColor=texture2D(sTextureTable, vTextureCoord); 
		     gl_FragColor = finalColor*ambient*1.4;//給此片元彩色值
	   	}
   	}
}              