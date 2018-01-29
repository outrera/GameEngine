package com.base.game;

import com.base.engine.core.CoreEngine;
import org.lwjgl.LWJGLException;

public class Main
{
	public static void main(String[] args) throws LWJGLException
	{
		CoreEngine engine = new CoreEngine(800, 600, 60, new TestGame());
		engine.createWindow("3D Game Engine");
		engine.start();
	}
}
