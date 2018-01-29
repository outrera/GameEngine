#version 120

varying vec2 texCoord0;
varying vec3 position0;

uniform vec3 color;
uniform sampler2D sampler;

void main()
{
    vec4 textureColor = texture2D(sampler, texCoord0.xy);

    if(textureColor == vec4(0, 0, 0, 1))
        gl_FragColor = vec4(position0, 1);
    else
        gl_FragColor = texture2D(sampler, texCoord0.xy) * vec4(color, 1.0);
}