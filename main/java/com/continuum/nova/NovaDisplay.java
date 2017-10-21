package com.continuum.nova;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.PixelFormat;

import com.continuum.nova.utils.NovaDrawable;

/**
 * A replacement for LWJGL's Display class
 *
 * @author ddubois
 * @since 16-Oct-17
 */
public class NovaDisplay {
	
	private static NovaDrawable drawable = new NovaDrawable();
	
    public static int getWidth() {
        NovaNative.window_size windowSize = NovaNative.INSTANCE.get_window_size();
        return windowSize.width;
    }

    public static int getHeight() {
        NovaNative.window_size windowSize = NovaNative.INSTANCE.get_window_size();
        return windowSize.height;
    }

    public static boolean isActive() {
        return NovaNative.INSTANCE.display_is_active();
    }

    public static void setResizable(boolean resizable) {
        NovaNative.INSTANCE.set_resizable(resizable ? 1 : 0);
    }

    public static void setTitle(String title) {
        NovaNative.INSTANCE.set_window_title(title);
    }

    public static void create(PixelFormat pixelFormat) {
        // Nova creates its own window with its own pixel format, which may or may not be what MC wants (wanna support
        // HRD monitors at some point) so I have this method so this class has the same interface but it won't do
        // anything
    }

    public static void create() {
        // Nova creates its own window so I have this method so this class has the same interface but it won't do
        // anything
    }

    public static void setFullscreen(boolean fullscreen) {
        NovaNative.INSTANCE.set_fullscreen(fullscreen ? 1 : 0);
    }

    public static DisplayMode getDisplayMode() {
        NovaNative.window_size windowSize = NovaNative.INSTANCE.get_window_size();
        return new DisplayMode(windowSize.width, windowSize.height);
    }

    public static void setDisplayMode(DisplayMode displayMode) {
        // Intentionally ignored because Nova will handle its own display mode, thank you very much
    }

    public static boolean isCreated() {
        return NovaNative.INSTANCE.window_is_created();
    }

    public static boolean isCloseRequested() {
        return NovaNative.INSTANCE.should_close();
    }

    public static void sync(int framerateLimit) {
        // TODO: This method will be implemented when framerate limiting is a feature of Nova. For now it's
        // intentionally left blank
    }

    public static int setIcon(ByteBuffer[] iconBytes) {
        // TODO: This method will be implemented when I (or someone else I guess) figures out how to set the icon of a
        // GLFW window
    	return 1;
    }

    public static void destroy() {
        // Nove wil handle its own lifetime, thank you very much
    }

    public static void setVsyncEnabled(boolean vsyncEnabled) {
        // We're going to have to implement this using Vulkan and setting the present mode of the swapchain
    }

    public static List<DisplayMode> getAvailableDisplayModes() {
        // Look I don't even know. Do I have to return anything? Nova will handle its own display mode bollocks so I
        // guess I'll just return the bare minimum for Minecraft to not crash
        return Collections.singletonList(getDisplayMode());
    }

    public static DisplayMode getDesktopDisplayMode() {
        // The official docs for this method say "Returns the desktop display mode". Like, wow, really? A method called
        // getDesktopDisplayMode returns the desktop display mode? that definitely wasn't completely obvious from the
        // method name
        return getDisplayMode();
    }
    
	public static Drawable getDrawable() {
		return drawable;
	}
}
