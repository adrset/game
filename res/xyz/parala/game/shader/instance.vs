#version 130

in vec3 position; //0
in vec2 texture_coord;
in vec3 normal;
// 3,4 not used but reserved
in mat4 model;
in vec3 color;
uniform mat4 projection;
uniform mat4 view;

out vec3 box_color;
out vec2 tex_coords;
out vec3 normals;
out vec3 frag_pos;
varying float instanceId;

void main(void){
	frag_pos =  vec3(model * vec4(position, 1.0));
	gl_Position = projection * view * model * vec4(position.x, position.y, position.z, 1.0);
	tex_coords = texture_coord;
	normals = normal;
	instanceId = gl_InstanceID;
	box_color = color;
}