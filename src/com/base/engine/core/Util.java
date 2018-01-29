package com.base.engine.core;

import com.base.engine.rendering.Vertex;
import java.nio.*;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;

public class Util
{
    public static FloatBuffer createFloatBuffer(int size)
    {
        return BufferUtils.createFloatBuffer(size);
    }
    
    public static IntBuffer createIntBuffer(int size)
    {
        return BufferUtils.createIntBuffer(size);
    }
    
    public static ByteBuffer createByteBuffer(int size)
    {
        return BufferUtils.createByteBuffer(size);
    }
    
    public static IntBuffer createFlippedBuffer(int... values)
    {
        IntBuffer buffer = createIntBuffer(values.length);
        buffer.put(values);
        buffer.flip();
        
        return buffer;
    }
    
    public static FloatBuffer createFlippedBuffer(Vertex[] vertices)
    {
        FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.SIZE);
        
        for (Vertex vertex : vertices)
        {
            Vector3f pos = vertex.getPos();
            Vector2f tex = vertex.getTexCoord();
            Vector3f nrm = vertex.getNormal();
            
            buffer.put(pos.getX());
            buffer.put(pos.getY());
            buffer.put(pos.getZ());
            
            buffer.put(tex.getX());
            buffer.put(tex.getY());
            
            buffer.put(nrm.getX());
            buffer.put(nrm.getY());
            buffer.put(nrm.getZ());
        }
        
        buffer.flip();
        
        return buffer;
    }
    
    public static FloatBuffer createFlippedBuffer(Matrix4f value)
    {
        FloatBuffer buffer = createFloatBuffer(4 * 4);
        
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                buffer.put(value.get(i, j));
        
        buffer.flip();
        
        return buffer;
    }
    
    public static String[] removeEmptyStrings(String[] data)
    {
        ArrayList<String> result = new ArrayList<>();
        
        for(String str : data)
            if(!str.equals(""))
                result.add(str);
        
        String[] res = new String[result.size()];
        result.toArray(res);
        
        return res;
    }
    
    public static int[] toIntArray(Integer[] data)
    {
        int[] res = new int[data.length];
        
        for(int i = 0; i < data.length; i++)
            res[i] = data[i];
        
        return res;
    }
}
