package com.base.engine.components;

import com.base.engine.core.*;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Window;

public class Camera extends GameComponent
{
        public static int moveSpeed = 10;
	public static final Vector3f yAxis = new Vector3f(0,1,0);
	
//	private Vector3f pos;
//	private Vector3f forward;
//	private Vector3f up;
        private Matrix4f projection;
	
	public Camera(float fov, float aspect, float zNear, float zFar)
	{
//            this.pos = new Vector3f(0, 0, 0);
//            this.forward = new Vector3f(0, 0, 1).normalized();
//            this.up = new Vector3f(0, 1, 0).normalized();
            this.projection = new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
	}
        
        public Matrix4f getViewProjection()
        {
            Matrix4f cameraRotation = getTransform().getTransformedRotation().conjugate().toRotationMatrix();
            Vector3f cameraPos = getTransform().getTransformedPosition().mul(-1);
            
            Matrix4f cameraTranslation = new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
            
            return projection.mul(cameraRotation.mul(cameraTranslation));
        }
        
        @Override
        public void addToRenderingEngine(RenderingEngine renderingEngine)
        {
            renderingEngine.addCamera(this);
        }
        
        boolean mouseLocked = false;
        Vector2f centerPosition = new Vector2f(Window.getWidth()/2, Window.getHeight()/2);
        
        @Override
	public void input(float delta)
	{
            float sensitivity = 0.4f;
            float movAmt = (float)(moveSpeed * delta);
//          float rotAmt = (float)(100 * Time.getDelta());

            if(Input.getKey(Input.KEY_ESCAPE))
            {
                Input.setCursor(true);
                mouseLocked = false;
            }
            if(Input.getMouseDown(0))
            {
                Input.setMousePosition(centerPosition);
                Input.setCursor(false);
                mouseLocked = true;
            }

            if(Input.getKey(Input.KEY_W))
                    move(getTransform().getRotation().getForward(), movAmt);
            if(Input.getKey(Input.KEY_S))
                    move(getTransform().getRotation().getForward(), -movAmt);
            if(Input.getKey(Input.KEY_A))
                    move(getTransform().getRotation().getLeft(), movAmt);
            if(Input.getKey(Input.KEY_D))
                    move(getTransform().getRotation().getRight(), movAmt);
            
            if(mouseLocked)
            {
                Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);
                
                boolean rotY = deltaPos.getX() != 0;
                boolean rotX = deltaPos.getY() != 0;
                
                if(rotY)
                    getTransform().rotate(yAxis, (float)Math.toRadians(deltaPos.getX() * sensitivity));
                if(rotX)
                    getTransform().rotate(getTransform().getRotation().getRight(), (float)Math.toRadians(-deltaPos.getY() * sensitivity));
                
                if(rotY || rotX)
                    Input.setMousePosition(new Vector2f(Window.getWidth()/2, Window.getHeight()/2));
            }
            
            // Key look
//          if(Input.getKey(Input.KEY_UP))
//              rotateX(-rotAmt);
//          if(Input.getKey(Input.KEY_DOWN))
//              rotateX(rotAmt);
//          if(Input.getKey(Input.KEY_LEFT))
//		rotateY(-rotAmt);
//          if(Input.getKey(Input.KEY_RIGHT))
//		rotateY(rotAmt);
	}
	
	public void move(Vector3f dir, float amt)
	{
            getTransform().setPosition(getTransform().getPosition().add(dir.mul(amt)));
            //pos = pos.add(dir.mul(amt));
	}
//	
//	public void rotateY(float angle)
//	{
//		Vector3f Haxis = yAxis.cross(forward).normalized();
//		
//		forward = forward.rotate(yAxis, angle).normalized();
//		
//		up = forward.cross(Haxis).normalized();
//	}
//	
//	public void rotateX(float angle)
//	{
//		Vector3f Haxis = yAxis.cross(forward).normalized();
//		
//		forward = forward.rotate(Haxis, angle).normalized();
//		
//		up = forward.cross(Haxis).normalized();
//	}
//	
//	public Vector3f getLeft()
//	{
//		return forward.cross(up).normalized();
//	}
//	
//	public Vector3f getRight()
//	{
//		return up.cross(forward).normalized();
//	}
//	
//	public Vector3f getPos()
//	{
//		return pos;
//	}
//
//	public void setPos(Vector3f pos)
//	{
//		this.pos = pos;
//	}
//
//	public Vector3f getForward()
//	{
//		return forward;
//	}
//
//	public void setForward(Vector3f forward)
//	{
//		this.forward = forward;
//	}
//
//	public Vector3f getUp()
//	{
//		return up;
//	}
//
//	public void setUp(Vector3f up)
//	{
//		this.up = up;
//	}
}
