uniform mat4 uMVPMatrix;
attribute vec3 aPosition;
attribute vec2 aTexCoor;
varying vec3 vPosition;
varying vec2 vTexCoor;
void main()
{
	gl_Position = uMVPMatrix*vec4(aPosition,1);
	vTexCoor = aTexCoor;
}
