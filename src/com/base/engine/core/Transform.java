package com.base.engine.core;

import static com.base.engine.components.Camera.yAxis;

public class Transform
{
    private Transform parent;
    private Matrix4f parentMatrix;
    
    private Vector3f position;
    private Quaternion rotation;
    private Vector3f scale;
    
    private Vector3f oldPos;
    private Quaternion oldRot;
    private Vector3f oldScale;
    
    public Transform()
    {
        position = new Vector3f(0, 0, 0);
        rotation = new Quaternion(0, 0, 0, 1);
        scale = new Vector3f(1, 1, 1);
        
        parentMatrix = new Matrix4f().initIdentity();
    }
    
    public void update()
    {
        if(oldPos == null)
        {
            oldPos = new Vector3f(0, 0, 0).set(position).add(1.0f);
            oldRot = new Quaternion(0, 0, 0, 0).set(rotation).mul(0.5f);
            oldScale = new Vector3f(0, 0, 0).set(scale).add(1.0f);
        }
        else
        {
            oldPos.set(position);
            oldRot.set(rotation);
            oldScale.set(scale);
        }
    }
    
    public void rotate(Vector3f axis, float angle)
    {
        rotation = new Quaternion(axis, angle).mul(rotation).normalized();
    }
    
    public boolean hasChanged()
    {
        if(parent != null && parent.hasChanged())
            return true;
        
        if(!position.equals(oldPos))
            return true;
        
        if(!rotation.equals(oldRot))
            return true;
        
        if(!scale.equals(oldScale))
            return true;
        
        return false;
    }
    
    private Matrix4f getParentMatrix()
    {
        if(parent != null && parent.hasChanged())
        {
            parentMatrix = parent.getTransformation();
        }
        
        return parentMatrix;
    }
    
    public Matrix4f getTransformation()
    {
        Matrix4f translateMatrix = new Matrix4f().initTranslation(position.getX(), position.getY(), position.getZ());
        Matrix4f rotationMatrix = new Quaternion(rotation.getX(), rotation.getY(), rotation.getZ(), 1).toRotationMatrix();
        Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());
        
        return getParentMatrix().mul(translateMatrix.mul(rotationMatrix.mul(scaleMatrix)));
    }
    
//    public Matrix4f getProjectedTransformation(Camera camera)
//    {
//        return camera.getViewProjection().mul(getTransformation());
//    }
    
    public void setParent(Transform parent)
    {
        this.parent = parent;
    }
    
    public Vector3f getTransformedPosition()
    {
        return getParentMatrix().transform(position);
    }
    
    public Quaternion getTransformedRotation()
    {
        Quaternion parentRotation = new Quaternion(0, 0, 0, 1);
        
        if(parent != null)
        {
            parentRotation = parent.getTransformedRotation();
        }
        
        return parentRotation.mul(rotation);
    }

    public Vector3f getPosition() {
        return position;
    }
    
    public void setPosition(Vector3f position) {
        this.position = position;
    }
    
    public Quaternion getRotation() {
        return rotation;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}
