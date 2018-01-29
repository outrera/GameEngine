package com.base.engine.rendering.resourceManagement;

import static org.lwjgl.opengl.GL15.*;

public class MeshResource
{
    private int vbo;
    private int ibo;
    private final int size;
    private int refCount;
    
    public MeshResource(int size)
    {
        vbo = glGenBuffers();
        ibo = glGenBuffers();
        this.size = size;
        this.refCount = 1;
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);
        
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
     * Remove a reference and return true if MeshResource is no longer referenced
     * @return True if refCount <= 0
     */
    public boolean removeReference()
    {
        refCount--;
        return (refCount <= 0);
    }

    public int getVBO() {
        return vbo;
    }

    public void setVBO(int vbo) {
        this.vbo = vbo;
    }

    public int getIBO() {
        return ibo;
    }

    public void setIBO(int ibo) {
        this.ibo = ibo;
    }
    
    public int getSize()
    {
        return this.size;
    }
}
