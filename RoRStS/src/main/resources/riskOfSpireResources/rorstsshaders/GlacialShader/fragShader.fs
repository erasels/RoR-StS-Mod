uniform sampler2D u_texture;
varying vec4 v_color;
varying vec2 v_texCoord;

void main() {
    vec4 tex = texture2D(u_texture, v_texCoord);
    vec3 texColor = texture2D(u_texture, v_texCoord).rgb;
    float r = (tex.r + tex.a*0.5)/2;
    float g = (tex.g + tex.a*0.8)/2;
    float b = (tex.b + tex.a*0.8)/2;
    gl_FragColor = vec4(r, g, b, tex.a);
}