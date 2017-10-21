package com.continuum.nova.utils;

import org.lwjgl.LWJGLException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.Drawable;

public class SharedDrawable implements Drawable {

	private Drawable d;
	
	public SharedDrawable(Drawable d) {
		this.d = d;
	}
	
	@Override
	public boolean isCurrent() throws LWJGLException {
		return d.isCurrent();
	}

	@Override
	public void makeCurrent() throws LWJGLException {
		d.makeCurrent();
	}

	@Override
	public void releaseContext() throws LWJGLException {
		d.releaseContext();
	}

	@Override
	public void destroy() {
		d.destroy();
	}

	@Override
	public void setCLSharingProperties(PointerBuffer properties) throws LWJGLException {
		d.setCLSharingProperties(properties);
	}
}
