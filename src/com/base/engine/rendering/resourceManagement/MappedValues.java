package com.base.engine.rendering.resourceManagement;

import com.base.engine.core.Vector3f;
import java.util.HashMap;

public abstract class MappedValues
{
    private final HashMap<String, Vector3f> vector3fHashMap;
    private final HashMap<String, Float> floatHashMap;
    
    public MappedValues()
    {
        vector3fHashMap = new HashMap<>();
        floatHashMap = new HashMap<>();
    }
    
    public Vector3f getVector3f(String name)
    {
        if(vector3fHashMap.containsKey(name))
            return vector3fHashMap.get(name);
        
        return new Vector3f(0, 0, 0);
    }
    public float getFloat(String name)
    {
        if(floatHashMap.containsKey(name))
            return floatHashMap.get(name);
        
        return 0;
    }
    
    public void addVector3f(String name, Vector3f vector) { vector3fHashMap.put(name, vector); }
    public void addFloat(String name, float floatVal) { floatHashMap.put(name, floatVal); }
}
