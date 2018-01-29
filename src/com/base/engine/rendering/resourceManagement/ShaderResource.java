package com.base.engine.rendering.resourceManagement;

import java.util.HashMap;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderResource
{
    private final int program;
    private final HashMap<String, Integer> uniforms;
    private HashMap<String, String> uniformData;
    private int refCount;
    
    public ShaderResource()
    {
        this.program = glCreateProgram();
        
        uniforms = new HashMap<>();
        uniformData = new HashMap<>();
        
        if(program == 0)
            throw new IllegalStateException("Shader creation failed: Could not find valid memory location in constructor");
        
        this.refCount = 1;
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        glDeleteShader(program);
    }
    /**
     * Add a reference to this resource
     */
    public void addReference()
    {
        refCount++;
    }
    /**
     * Remove a reference and return true if TextureResource is no longer referenced
     * @return True if refCount <= 0
     */
    public boolean removeReference()
    {
        refCount--;
        return (refCount <= 0);
    }

    public int getProgram()
    {
        return this.program;
    }
    
    public HashMap<String, Integer> getUniforms()
    {
        return uniforms;
    }
    
    public HashMap<String, String> getUniformData()
    {
        return uniformData;
    }
}
