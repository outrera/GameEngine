package com.base.engine.components;

import com.base.engine.core.Vector3f;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class DirectionalLight extends BaseLight
{
    private BaseLight base;
    
    public DirectionalLight(Vector3f color, float intensity)
    {
        super(color, intensity);
        
        setShader(new Shader("forward-directional"));
    }
    
    @Override
    public void addToRenderingEngine(RenderingEngine renderingEngine)
    {
        renderingEngine.addLight(this);
    }

    public Vector3f getDirection() {
        return getTransform().getTransformedRotation().getForward();
    }
}
