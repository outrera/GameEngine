package com.base.engine.rendering.resourceManagement;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class TextureResource
{
    private final int id;
    private int refCount;
    
    public TextureResource()
    {
        this.id = glGenTextures();
        this.refCount = 1;
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        glDeleteBuffers(id);
        
        super.finalize();
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

    public int getID()
    {
        return this.id;
    }
}
