#version 150

in vec3 position;
in vec2 texture_coord;
in vec3 normal;
uniform float deaf;

void main(void){
	gl_Position = vec4(position.x, position.y, position.z, 1.0);
	
}