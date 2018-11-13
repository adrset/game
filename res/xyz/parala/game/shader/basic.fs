#version 120

struct Material {
    sampler2D diffuse;
    sampler2D specular;
    float shininess;
}; 

struct PointLight {
    vec3 position;
    
    float constant;
    float linear;
    float quadratic;
	
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

varying vec2 tex_coords;
varying vec3 normals;
varying vec3 frag_pos;

varying vec4 out_Color;

uniform vec3 viewPos;
uniform vec3 sp_color;
uniform float blinn;
uniform Material material;
uniform PointLight pointLight;

vec3 CalcPointLight2(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir, vec3 colour)
{
    vec3 lightDir = normalize(light.position - fragPos);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
 
   	vec3 reflectDir = reflect(-lightDir, normal);
	float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);	
	
    // attenuation
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));    
    // combine results
    vec3 ambient = light.ambient * colour;
    vec3 diffuse = light.diffuse * diff * colour;
    vec3 specular = light.specular * spec * colour; // specular here
    ambient *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;
    return (ambient + diffuse + specular);
}


void main(void){
	
	
	vec3 result = CalcPointLight2(pointLight, normals, frag_pos, normalize(viewPos - frag_pos), sp_color);    
	float gamma = 2.2;
    gl_FragColor.rgb = pow(result.rgb, vec3(1.0/gamma));
	gl_FragColor.a = 1.0;
}