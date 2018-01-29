package com.base.engine.core;

import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;

public class CoreEngine {
    private boolean isRunning;
    private Game game;
    private RenderingEngine renderingEngine;
    private int width;
    private int height;
    private double frameTime;
    private String title;
    
    public CoreEngine(int width, int height, double frameTime, Game game)
    {
        this.isRunning = false;
        this.game = game;
        this.width = width;
        this.height = height;
        this.frameTime = 1.0/frameTime;
    }
    
    public void createWindow(String title) throws LWJGLException
    {
        Window.createWindow(width, height, title);
        this.renderingEngine = new RenderingEngine();
        this.title = title;
    }
    
    public void start()
    {
        if(isRunning)
            return;
        
        run();
    }
    
    public void stop()
    {
        if(!isRunning)
            return;
        
        isRunning = false;
    }
    
    private void run()
    {
        isRunning = true;
        System.out.println("FrameTime = " + frameTime);
        
        int frames = 0;
        double frameCounter = 0;
        
        game.init();
        
        double lastTime = Time.getTime();
        double unprocessedTime = 0;
        
        while(isRunning)
        {
            boolean render = false;
            
            double startTime = Time.getTime();
            double passedTime = startTime - lastTime;
            lastTime = startTime;
            
            unprocessedTime += passedTime;
            frameCounter += passedTime;
            
            while(unprocessedTime > frameTime)
            {
                render = true;
                
                unprocessedTime -= frameTime;
                
                if(Window.isCloseRequested())
                    stop();
                
                game.input((float)frameTime);
                //Game.input() MUST be before Input.update()
                Input.update();
                game.update((float)frameTime);
                
                if(frameCounter >= 1)
                {
                    Window.setTitle(title + " | FPS:" + frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }
            if(render)
            {
                game.render(renderingEngine);
                Window.render();
                frames++;
            }
            else
            {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CoreEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        cleanup();
    }
    
    private void cleanup()
    {
        Window.dispose();
    }
}
