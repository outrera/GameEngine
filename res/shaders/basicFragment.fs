#version 330

in vec2 texCoord0;
in vec3 position0;

out vec4 fragColor;

uniform vec3 color;
uniform sampler2D sampler;

void main()
{
    vec4 textureColor = texture2D(sampler, texCoord0.xy);

    if(textureColor == vec4(0, 0, 0, 1))
        fragColor = vec4(position0, 1);
    else
        fragColor = texture2D(sampler, texCoord0.xy) * vec4(color, 1.0);
}