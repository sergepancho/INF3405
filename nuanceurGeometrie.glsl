#version 430
layout(triangles) in;layout(triangle_strip, max_vertices = 4) out;


in Attribs {
    vec4 Color;
} AttribsIn[];



out Attribs {
    vec4 Color;
} AttribsOut;


void main()
{
    // émettre les sommets
    for ( int i = 0 ; i < gl_in.length() ; ++i )
    {
        gl_ViewportIndex = 0;
        gl_Position = gl_in[i].gl_Position;
        AttribsOut.Color= AttribsIn[i].Color;
        
        EmitVertex();
    }
    EndPrimitive();

    for ( int i = 0 ; i < gl_in.length() ; ++i )
    {
        gl_ViewportIndex = 1;
        gl_Position = gl_in[i].gl_Position;
		AttribsOut.Color=1- AttribsIn[i].Color;
      
        EmitVertex();
    }
    EndPrimitive();
}