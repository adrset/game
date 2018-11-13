#version 120

attribute vec3 position;
attribute vec2 texture_coord;
attribute vec3 normal;
uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

varying vec2 tex_coords;
varying vec3 normals;
varying vec3 frag_pos;

void main(void){
	frag_pos =  vec3(model * vec4(position, 1.0));
	gl_Position = projection * view * model * vec4(position.x, position.y, position.z, 1.0);
	tex_coords = texture_coord;
	normals = normal;
}