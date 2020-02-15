precision mediump float;
varying vec2 vTexCoor;
uniform sampler2D sTexture;
void main()
{
	gl_FragColor = texture2D(sTexture,vTexCoor);
}