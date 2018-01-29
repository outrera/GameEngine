package com.base.engine.rendering.meshLoading;

public class OBJIndex implements Comparable<OBJIndex>
{
    public int vertexIndex;
    public int texCoordIndex;
    public int normalIndex;
    public int orderTag;
    
    @Override
    public int compareTo(OBJIndex objIndex)
    {
        final int BEFORE = -1, AFTER = 1, EQUALS = 0;
        
        if(vertexIndex == objIndex.vertexIndex)
            return EQUALS;
        else if(vertexIndex < objIndex.vertexIndex)
            return BEFORE;
        else
            return AFTER;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof OBJIndex))
            return false;
        
        OBJIndex index = (OBJIndex)obj;
        
        return vertexIndex == index.vertexIndex
                && texCoordIndex == index.texCoordIndex
                && normalIndex == index.normalIndex;
    }
    
    @Override
    public int hashCode()
    {
        final int BASE = 17;
        final int MULTIPLIER = 31;
        
        int result = BASE;
        result = MULTIPLIER * result + vertexIndex;
        result = MULTIPLIER * result + texCoordIndex;
        result = MULTIPLIER * result + normalIndex;
        
        return result;
    }
}
