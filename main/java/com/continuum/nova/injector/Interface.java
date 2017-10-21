package com.continuum.nova.injector;

import java.awt.Color;
import java.util.Map;
import java.util.Optional;

import com.continuum.nova.NovaNative;
import com.continuum.nova.NovaRenderer;
import com.continuum.nova.gui.MemoryTextureAtlasSprite;
import com.continuum.nova.gui.NovaDraw;
import com.continuum.nova.input.Keyboard;
import com.continuum.nova.input.Mouse;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class Interface {

	private static NovaRenderer nova;
	
	public static ResourceLocation textureAtlasSprite_getLocation(TextureAtlasSprite textureAtlasSprite) {
		return textureAtlasSprite.location;
	}
	
    public static void textureMap_createWhiteTexture(TextureMap textureMap, ResourceLocation textureName) {
        textureMap.whiteImage = Optional.of(new MemoryTextureAtlasSprite(textureName.toString()));
        textureMap.whiteImage.ifPresent(whiteSprite -> {
            whiteSprite.setIconHeight(16);
            whiteSprite.setIconWidth(16);
            
            int[][] data = new int[textureMap.mipmapLevels + 1][];
            for(int mipLevel = 0; mipLevel < textureMap.mipmapLevels + 1; mipLevel++) {
                data[mipLevel] = new int[256];
                for(int i = 0; i < 256; i++) {
                    data[mipLevel][i] = 0xFFFFFFFF;
                }
            }

            whiteSprite.setFramesTextureData(Lists.newArrayList(new int[][][]{data}));
        });
    }
    
    public static Optional<TextureAtlasSprite> textureMap_getWhiteImage(TextureMap textureMap) {
    	return textureMap.whiteImage;
    }
    
    public static int textureMap_getWidth(TextureMap textureMap) {
    	return textureMap.width;
    }
    
    public static int textureMap_getHeight(TextureMap textureMap) {
    	return textureMap.height;
    }
    
    public static void textureMap_preStitch(TextureMap textureMap, Stitcher stitcher) {
    	textureMap.whiteImage.ifPresent(stitcher::addSprite);
    }
    
    public static void textureMap_postStitch(TextureMap textureMap, Stitcher stitcher) {
    	textureMap.width = stitcher.getCurrentWidth();
    	textureMap.height = stitcher.getCurrentHeight();
    }
    
    public static Map<String,TextureAtlasSprite> textureMap_getMapUploadedSprites(TextureMap textureMap) {
    	return textureMap.mapUploadedSprites;
    }
    
    public static float entityRenderer_getFogColorRed(EntityRenderer entityRenderer) {
    	return entityRenderer.fogColorRed;
    }
    
    public static float entityRenderer_getFogColorGreen(EntityRenderer entityRenderer) {
    	return entityRenderer.fogColorGreen;
    }
    
    public static float entityRenderer_getFogColorBlue(EntityRenderer entityRenderer) {
    	return entityRenderer.fogColorBlue;
    }
    
    public static DynamicTexture entityRenderer_getLightmap(EntityRenderer entityRenderer) {
    	return entityRenderer.lightmapTexture;
    }
    
    public static boolean entityRenderer_isLightmapUpdateNeeded(EntityRenderer entityRenderer) {
    	return entityRenderer.lightmapUpdateNeeded;
    }
    
    public static void entityRenderer_updateLightmap(EntityRenderer entityRenderer, float partialTicks) {
    	entityRenderer.updateLightmap(partialTicks);
    }
    
    private static Color color = Color.white;
    
    public static void fontRenderer_setColor(float r, float g, float b, float a) {
    	color = new Color(r, g, b, a);
    }
    
    public static float fontRenderer_renderDefaultChar(int ch, boolean italic, float posX, float posY, int[] charWidth, ResourceLocation locationFontTexture) {
        int i = ch % 16 * 8;
        int j = ch / 16 * 8;
        int k = italic ? 1 : 0;
        int l = charWidth[ch];
        float f = (float)l - 0.01F;

        NovaDraw.Vertex[] vertices = new NovaDraw.Vertex[]{
                new NovaDraw.Vertex(posX + (float)k, posY,  (float) i / 128.0F, (float) j / 128.0F, color),
                new NovaDraw.Vertex(posX - (float)k, posY + 7.99F,  (float)i / 128.0F, ((float)j + 7.99F) / 128.0F, color),
                new NovaDraw.Vertex(posX + f - 1.0F + (float)k, posY,  ((float)i + f - 1.0F) / 128.0F, (float)j / 128.0F, color),
                new NovaDraw.Vertex(posX + f - 1.0F - (float)k, posY + 7.99F,  ((float)i + f - 1.0F) / 128.0F, ((float)j + 7.99F) / 128.0F, color)
        };

        Integer[] indices = new Integer[] {0, 1, 2, 2, 1, 3};

        NovaDraw.draw(locationFontTexture, indices, vertices);

        return (float)l;
    }
    
    public static BlockFluidRenderer blockRendererDispatcher_getFluidRenderer(BlockRendererDispatcher blockRendererDispatcher) {
    	return blockRendererDispatcher.fluidRenderer;
    }
    
    public static int dynamicTexture_getWidth(DynamicTexture dynamicTexture) {
    	return dynamicTexture.width;
    }
    
    public static int dynamicTexture_getHeight(DynamicTexture dynamicTexture) {
    	return dynamicTexture.height;
    }
    
    public static boolean minecraft_preInitializeNova() {
    	nova = new NovaRenderer();
    	nova.preInit();
    	Mouse.create();
    	Keyboard.create();
    	return NovaNative.INSTANCE.display_is_active();
    }
    
    public static void minecraft_registerReloadListener() {
    	Minecraft.getMinecraft().mcResourceManager.registerReloadListener(nova);
    }
    
    public static void minecraft_loadStartupShaderpack() {
    	nova.loadShaderpack("default", Minecraft.getMinecraft().blockColors);
    }
}
