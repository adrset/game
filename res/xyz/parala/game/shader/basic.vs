#version 150

in vec3 position;
in vec2 texture_coord;
in vec3 normal;
uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

void main(void){
	gl_Position = projection * view * model * vec4(position.x, position.y, position.z, 1.0);
	
}