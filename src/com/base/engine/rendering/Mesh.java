package com.base.engine.rendering;

import com.base.engine.core.Util;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.meshLoading.IndexedModel;
import com.base.engine.rendering.meshLoading.OBJModel;
import com.base.engine.rendering.resourceManagement.MeshResource;
import java.util.ArrayList;
import java.util.HashMap;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh
{
    private static HashMap<String, MeshResource> loadedModels = new HashMap<>();
    private MeshResource resource;
    private final String fileName;

    private static int drawType = GL_TRIANGLES;
    
    public Mesh(String fileName)
    {
        this.fileName = fileName;
        if(loadedModels.containsKey(fileName))
        {
            resource = loadedModels.get(fileName);
            resource.addReference();
        }
        else
        {
            loadMesh(fileName);
            loadedModels.put(fileName, resource);
        }
    }
    
    public Mesh(Vertex[] vertices, int[] indices)
    {
        this(vertices, indices, false);
    }
    
    public Mesh(Vertex[] vertices, int[] indices, boolean calcNormals)
    {
        this.fileName = "";
        addVertices(vertices, indices, calcNormals);
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        if(resource.removeReference() && !fileName.isEmpty())
        {
            loadedModels.remove(fileName);
        }
        
        super.finalize();
    }
    
    private void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals)
    {
        if(calcNormals)
            calcNormals(vertices, indices);
        
        resource = new MeshResource(indices.length);
        
        glBindBuffer(GL_ARRAY_BUFFER, resource.getVBO());
        glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
    }

    private Mesh loadMesh(String fileName) {
        if (!fileName.endsWith(".obj"))
        {
            throw new IllegalArgumentException("File is not a .obj file!");
        }
        
        ArrayList<Vertex> vertices = new ArrayList<>();
        
        OBJModel test = new OBJModel("./res/models/" + fileName);
        IndexedModel model = test.toIndexedModel();
        //model.calcNormals();
        
        for(int i = 0; i < model.getPositions().size(); i++)
        {
            vertices.add(new Vertex(model.getPositions().get(i),
                                    model.getTextureCoordinates().get(i),
                                    model.getNormals().get(i)));
        }
        
        Vertex[] vertexData = new Vertex[vertices.size()];
        vertices.toArray(vertexData);
        
        Integer[] indexData = new Integer[model.getIndices().size()];
        model.getIndices().toArray(indexData);
        
        addVertices(vertexData, Util.toIntArray(indexData), false);
        return this;
    }
    
    public void draw()
    {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        
        glBindBuffer(GL_ARRAY_BUFFER, resource.getVBO());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIBO());
        glDrawElements(drawType, resource.getSize(), GL_UNSIGNED_INT, 0);
        
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
    }
    
    private void calcNormals(Vertex[] vertices, int[] indices)
    {
        for(int i = 0; i < indices.length; i += 3)
        {
            int i0 = indices[i];
            int i1 = indices[i + 1];
            int i2 = indices[i + 2];
            
            Vector3f v1 = vertices[i1].getPos().sub(vertices[i0].getPos());
            Vector3f v2 = vertices[i2].getPos().sub(vertices[i0].getPos());
            
            Vector3f normal = v1.cross(v2).normalized();
            
            vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
            vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
            vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
        }
        
        for(Vertex vertex : vertices)
            vertex.setNormal(vertex.getNormal().normalized());
    }
    
    public static void setDrawType(int type)
    {
        drawType = type;
    }
    
    public static int getDrawType()
    {
        return drawType;
    }
}
