package com.base.engine.rendering;

import com.base.engine.core.*;
import com.base.engine.components.*;
import com.base.engine.rendering.resourceManagement.MappedValues;
import java.util.ArrayList;
import java.util.HashMap;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

public class RenderingEngine extends MappedValues
{
    private final HashMap<String, Integer> samplerMap;
    private final ArrayList<BaseLight> lights;
    private BaseLight activeLight;
    
    private final Shader forwardAmbient;
    private Camera mainCamera;
    
    public RenderingEngine()
    {
        super();
        lights = new ArrayList<>();
        samplerMap = new HashMap<>();
        
        addVector3f("ambient", new Vector3f(0.1f, 0.1f, 0.1f));
        
        forwardAmbient = new Shader("forward-ambient");
        
        samplerMap.put("diffuse", 0);
        samplerMap.put("normal", 1);
        
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        // Faces created in clockwise form are front faces
        glFrontFace(GL_CW);
        // Cull all back faces
        glCullFace(cullFace);
        // Turn on culling
        glEnable(GL_CULL_FACE);
        // Track depth of pixels to properly layer objects
        glEnable(GL_DEPTH_TEST);
        
        glEnable(GL_DEPTH_CLAMP);
        
        glEnable(GL_TEXTURE_2D);
        
        //mainCamera = new Camera(70.0f, (float)Window.getWidth() / (float)Window.getHeight(), 0.01f, 1000f);
    }
    
    public void updateUniformStruct(Transform transform, Material material, Shader shader, String uniformName, String uniformType)
    {
        throw new IllegalArgumentException(uniformType + " is not a supported type in Rendering Engine");
    }
    
    public void render(GameObject object)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        lights.clear();
        
        object.addToRenderingEngine(this);
        
        object.render(forwardAmbient, this);
        
        // Enable blending of pixels
        glEnable(GL_BLEND);
        // Add each pixel times one
        glBlendFunc(GL_ONE, GL_ONE);
        // Disable depth mask
        glDepthMask(false);
        // Only adds the pixel if it has the exact same depth
        glDepthFunc(GL_EQUAL);
        
        for(BaseLight light : lights)
        {
            activeLight = light;
            object.render(light.getShader(), this);
        }
        
        // Resets the depth function
        glDepthFunc(GL_LESS);
        // Re-enable depth mask
        glDepthMask(true);
        // Disable blending of pixels
        glDisable(GL_BLEND);
    }
    
    private static int cullFace = GL_BACK;
    
    public static String getOpenGLVersion()
    {
        return glGetString(GL_VERSION);
    }
    /*
    Copied from source
    
    public static final int GL_POINTS = 0;
    public static final int GL_LINES = 1;
    public static final int GL_LINE_LOOP = 2;
    public static final int GL_LINE_STRIP = 3;
    public static final int GL_TRIANGLES = 4;
    public static final int GL_TRIANGLE_STRIP = 5;
    public static final int GL_TRIANGLE_FAN = 6;
    public static final int GL_QUADS = 7;
    public static final int GL_QUAD_STRIP = 8;
    public static final int GL_POLYGON = 9;
    */
    private static final String[] drawTypeNames = new String[] {
            "GL_POINTS", // 0
            "GL_LINES", // 1
            "GL_LINE_LOOP", // 2
            "GL_LINE_STRIP", // 3
            "GL_TRIANGLES", // 4
            "GL_TRIANGLE_STRIP", // 5
            "GL_TRIANGLE_FAN", // 6
            "GL_QUADS", // 7
            "GL_QUAD_STRIP", // 8
            "GL_POLYGON", // 9
    };
    
    public static void setDrawType(int type)
    {
        Mesh.setDrawType(type);
    }
    
    public static int getDrawType()
    {
        return Mesh.getDrawType();
    }
    
    public static void cycleDrawType()
    {
        int type = Mesh.getDrawType();
        if(++type > 9)
            type = 0;
        setDrawType(type);
        System.out.println("Set drawType to " + drawTypeNames[type]);
    }
    
    public static void setGammaFix(boolean enabled)
    {
        if(enabled)
        {
            glEnable(GL_FRAMEBUFFER_SRGB);
            System.out.println("Enabled gamma fix");
        }
        else
        {
            glDisable(GL_FRAMEBUFFER_SRGB);
            System.out.println("Disabled gamma fix");
        }
    }
    
    public static void toggleGammaFix()
    {
        setGammaFix(!glIsEnabled(GL_FRAMEBUFFER_SRGB));
    }
    
    private static void setClearColor(Vector3f color)
    {
        glClearColor(color.getX(), color.getY(), color.getZ(), 1.0f);
    }
    
    public static void setCullFace(int face)
    {
        glCullFace(face);
        cullFace = face;
    }
    
    public static void toggleCullFace()
    {
        if(cullFace == GL_BACK)
        {
            setCullFace(GL_FRONT);
            System.out.println("Set cull face to GL_FRONT");
        }
        else
        {
            setCullFace(GL_BACK);
            System.out.println("Set cull face to GL_BACK");
        }
    }
    
    public int getSamplerSlot(String samplerName)
    {
        return samplerMap.get(samplerName);
    }
    
    public void addCamera(Camera camera)
    {
        mainCamera = camera;
    }

    public Camera getMainCamera() {
        return mainCamera;
    }
    
    public void addLight(BaseLight light)
    {
        lights.add(light);
    }
    
    public BaseLight getActiveLight()
    {
        return activeLight;
    }
}
