package com.continuum.nova.injector;

import java.util.ArrayList;

import com.continuum.nova.injector.transformers.AllClassTransformer;
import com.continuum.nova.injector.transformers.EntityRendererTransformer;
import com.continuum.nova.injector.transformers.FontRendererTransformer;
import com.continuum.nova.injector.transformers.TextureAtlasSpriteTransformer;
import com.continuum.nova.injector.transformers.TextureMapTransformer;

import net.minecraft.launchwrapper.IClassTransformer;

public class NovaClassTransformer implements IClassTransformer {

	private static ArrayList<NovaTransformer> transformers;
	private static AllClassTransformer allClasses;
	
	static {
		allClasses = new AllClassTransformer();
		
		transformers = new ArrayList<NovaTransformer>();
		//transformers.add(new MinecraftTransformer());
		transformers.add(new EntityRendererTransformer());
		transformers.add(new TextureMapTransformer());
		transformers.add(new TextureAtlasSpriteTransformer());
		transformers.add(new FontRendererTransformer());
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] data) {
		data = allClasses.transform(name, data);
		
		for (NovaTransformer t : transformers) {
			data = t.transformClass(name, data);
		}
		return data;
	}
}
