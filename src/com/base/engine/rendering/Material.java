package com.base.engine.rendering;

import com.base.engine.rendering.resourceManagement.MappedValues;
import java.util.HashMap;

public class Material extends MappedValues
{
      private final HashMap<String, Texture> textureHashMap;
    
    public Material()
    {
        super();
        textureHashMap = new HashMap<>();
    }
    
    public void addTexture(String name, Texture texture) { textureHashMap.put(name, texture); }
    
    
    public Texture getTexture(String name)
    {
        if(textureHashMap.containsKey(name))
            return textureHashMap.get(name);
        
        return new Texture("test0.png");
    }
}
