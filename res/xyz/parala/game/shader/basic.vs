#version 150

in vec3 position;
in vec2 texture_coord;
in vec3 normal;
uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

out vec2 tex_coords;
out vec3 normals;
out vec3 frag_pos;

void main(void){
	frag_pos =  vec3(model * vec4(position, 1.0));
	gl_Position = projection * view * model * vec4(position.x, position.y, position.z, 1.0);
	tex_coords = texture_coord;
	normals = normal;
}