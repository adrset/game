#version 150

in vec2 tex_coords;

out vec4 out_Color;

uniform float time;
uniform sampler2D ourTexture;

void main(void){
	
	//out_Color = vec4(sin(time), cos(time), cos(time*2), 1.0);
	out_Color = texture(ourTexture, tex_coords);

}