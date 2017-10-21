package com.continuum.nova.utils;

import org.lwjgl.LWJGLException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.Drawable;

public class NovaDrawable implements Drawable {

	boolean current = false;
	
	@Override
	public boolean isCurrent() throws LWJGLException {
		return current;
	}

	@Override
	public void makeCurrent() throws LWJGLException {
		current = true;
	}

	@Override
	public void releaseContext() throws LWJGLException {
		current = false;
	}

	@Override
	public void destroy() {
		current = false;
	}

	@Override
	public void setCLSharingProperties(PointerBuffer properties) throws LWJGLException {
		
	}
}
