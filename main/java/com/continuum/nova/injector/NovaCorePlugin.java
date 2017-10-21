package com.continuum.nova.injector;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({"com.continuum", "org.lwjgl", "net.minecraft.nbt", "net.minecraft.network", "io.netty"})
@MCVersion(value = "1.10")
public class NovaCorePlugin implements IFMLLoadingPlugin {
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
			"com.continuum.nova.injector.NovaClassTransformer"
		};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
	
	public static void log(String str) {
		System.out.println("TRANSFORMER: " + str);
	}
}
