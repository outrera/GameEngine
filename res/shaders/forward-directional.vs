#version 120
// VERTEX SHADER

attribute vec3 position;
attribute vec2 texCoord;
attribute vec3 normal;

varying vec2 texCoord0;
varying vec3 normal0;
varying vec3 worldPos0;

uniform mat4 T_model;
uniform mat4 T_MVP;

//UNIFORM KEYWORD TEST
//This uniform does this and that
//ATTRIBUTE KEYWORD TEST
//This attribute does this and that

void main()
{
        gl_Position = T_MVP * vec4(position, 1.0);
        texCoord0 = texCoord;
		normal0 = (T_model * vec4(normal, 0.0)).xyz;
		worldPos0 = (T_model * vec4(position, 1.0)).xyz;
}