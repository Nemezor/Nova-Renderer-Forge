package com.continuum.nova.injector.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.continuum.nova.injector.NovaTransformer;
import com.continuum.nova.injector.Tokens;

public class TextureAtlasSpriteTransformer extends NovaTransformer {
	
	@Override
	public void initialize() {
		setClassName("net.minecraft.client.renderer.texture.TextureAtlasSprite", "???");
		
		addToken("locationFieldDesc", "Lnet/minecraft/util/ResourceLocation;", "???");
	}

	@Override
	public void transformFunction(String name, MethodNode method, Tokens tokens) {
		
	}

	@Override
	public void transformField(String name, FieldNode field, Tokens tokens) {
		
	}

	@Override
	public void addData(String name, ClassNode clazz, Tokens tokens) {
		FieldNode locationField = new FieldNode(Opcodes.ACC_PUBLIC, "location", tokens.lookup("locationFieldDesc"), null, null);
		
		clazz.fields.add(locationField);
	}
}
