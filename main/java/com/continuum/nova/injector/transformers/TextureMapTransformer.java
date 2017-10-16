package com.continuum.nova.injector.transformers;

import java.util.Optional;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.continuum.nova.injector.NovaTransformer;
import com.continuum.nova.injector.Tokens;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class TextureMapTransformer extends NovaTransformer {

	@Override
	public void initialize() {
		setClassName("net.minecraft.client.renderer.texture.TextureMap", "???");
		
		addField("mipmapLevelsField", "mipmapLevels", "???");
		addField("mapUploadedSpritesField", "mapUploadedSprites", "???");
		
		addMethod("loadTextureAtlasMethod", "loadTextureAtlas", "???");
		
		addToken("addSpriteMethod", "net/minecraft/client/renderer/texture/Stitcher.addSprite", "???");
		addToken("loadTextureAtlasMethodDesc", "(Lnet/minecraft/client/resources/IResourceManager;)V", "???");
		addToken("missingImageField", "missingImage", "???");
		addToken("prepostStitchMethodDesc", "(Lnet/minecraft/client/renderer/texture/TextureMap;Lnet/minecraft/client/renderer/texture/Stitcher;)V", "???");
		addToken("whiteImageFieldDesc", "Ljava/util/Optional<Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;>;", "???");
		addToken("doStitchMethod", "net/minecraft/client/renderer/texture/Stitcher.doStitch", "???");
	}
	
	@Override
	public void transformFunction(String name, MethodNode method, Tokens tokens) {
		if (name.equals("loadTextureAtlasMethod")) {
			if (method.desc.equals(tokens.lookup("loadTextureAtlasMethodDesc"))) {
				AbstractInsnNode lastInsn = null;
				AbstractInsnNode currInsn = null;
				
				for (int index = 0; index < method.instructions.size(); index++) {
					lastInsn = currInsn;
					currInsn = method.instructions.get(index);
					
					if (lastInsn == null) {
						continue;
					}
					if (lastInsn.getOpcode() == Opcodes.GETFIELD && ((FieldInsnNode)lastInsn).name.equals(tokens.lookup("missingImageField")) &&
							currInsn.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode)currInsn).name.equals(tokens.lookup("addSpriteMethod"))) {
						InsnList toInject = new InsnList();

						toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
						toInject.add(new VarInsnNode(Opcodes.ALOAD, 3));
						toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/continuum/nova/injector/Interface", "textureMap_preStitch", tokens.lookup("prepostStitchMethodDesc"), false));
						toInject.add(new LabelNode(new Label()));
						
						method.instructions.insertBefore(method.instructions.get(index + 1), toInject);
					}else if (currInsn.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode)currInsn).name.equals(tokens.lookup("doStitchMethod"))) {
						InsnList toInject = new InsnList();

						toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
						toInject.add(new VarInsnNode(Opcodes.ALOAD, 3));
						toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/continuum/nova/injector/Interface", "textureMap_postStitch", tokens.lookup("prepostStitchMethodDesc"), false));
						toInject.add(new LabelNode(new Label()));
						
						method.instructions.insertBefore(currInsn, toInject);
					}
				}
			}
		}
	}

	@Override
	public void transformField(String name, FieldNode field, Tokens tokens) {
		if (name.equals("mipmapLevelsField")) {
			if (field.desc.equals("I")) {
				field.access &= ~Opcodes.ACC_PRIVATE;
				field.access |= Opcodes.ACC_PUBLIC;
			}
		}else if (name.equals("mapUploadedSpritesField")) {
			field.access &= ~Opcodes.ACC_PRIVATE;
			field.access |= Opcodes.ACC_PUBLIC;
		}
	}

	@Override
	public void addData(String name, ClassNode clazz, Tokens tokens) {
		FieldNode whiteImageField = new FieldNode(Opcodes.ACC_PUBLIC, "whiteImage", tokens.lookup("whiteImageFieldDesc"), null, null);
		FieldNode widthField = new FieldNode(Opcodes.ACC_PUBLIC, "width", "I", null, null);
		FieldNode heightField = new FieldNode(Opcodes.ACC_PUBLIC, "height", "I", null, null);
		
		clazz.fields.add(whiteImageField);
		clazz.fields.add(widthField);
		clazz.fields.add(heightField);
	}
}
