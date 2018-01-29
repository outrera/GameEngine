package com.base.engine.rendering;

import com.base.engine.core.Util;
import com.base.engine.rendering.resourceManagement.TextureResource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Texture
{
    private static final HashMap<String, TextureResource> loadedTextures = new HashMap<>();
    private TextureResource resource;
    private String fileName;
    
    public Texture(String fileName)
    {
        this.fileName = fileName;
        if(loadedTextures.containsKey(fileName))
        {
            resource = loadedTextures.get(fileName);
            resource.addReference();
        }
        else
        {
        resource = loadTexture(fileName);
        loadedTextures.put(fileName, resource);
        }
    }

    private static TextureResource loadTexture(String fileName) {
        try
        {
            BufferedImage image = ImageIO.read(new File("./res/textures/" + fileName));
            int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

            ByteBuffer buffer = Util.createByteBuffer(image.getHeight() * image.getWidth() * 4);
            boolean hasAlpha = image.getColorModel().hasAlpha();

            for(int y = 0; y < image.getHeight(); y++)
            {
                for(int x = 0; x < image.getWidth(); x++)
                {
                    int pixel = pixels[y * image.getWidth() + x];

                    buffer.put((byte)((pixel >> 16) & 0xFF));
                    buffer.put((byte)((pixel >> 8) & 0xFF));
                    buffer.put((byte)((pixel) & 0xFF));
                    if(hasAlpha)
                        buffer.put((byte)((pixel >> 24) & 0xFF));
                    else
                        buffer.put((byte)(0xFF));
                }
            }

            buffer.flip();

            TextureResource resource = new TextureResource();
            glBindTexture(GL_TEXTURE_2D, resource.getID());

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            return resource;
        }
        catch(IOException e)
        {
            System.err.println("Failed to load texture " + fileName);
            e.printStackTrace();
            return null;
        }
    }
    
    public void bind()
    {
        bind(0);
    }
    
    public void bind(int samplerSlot)
    {
        assert(samplerSlot >= 0 && samplerSlot <= 31);
        glActiveTexture(GL_TEXTURE0 + samplerSlot);
        glBindTexture(GL_TEXTURE_2D, resource.getID());
    }
    
    public int getID()
    {
        return resource.getID();
    }
}
