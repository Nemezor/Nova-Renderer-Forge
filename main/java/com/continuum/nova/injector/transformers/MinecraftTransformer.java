package com.continuum.nova.injector.transformers;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.continuum.nova.injector.NovaTransformer;
import com.continuum.nova.injector.Tokens;

public class MinecraftTransformer extends NovaTransformer {

	@Override
	public void initialize() {
		setClassName("net.minecraft.client.Minecraft", "???");
		
		addMethod("constructorMethod", "<init>", "<init>");
		addMethod("startGameMethod", "startGame", "???");
		
		addField("mcResourceManagerField", "mcResourceManager", "???");
		addField("blockColorsField", "blockColors", "???");
		
		addToken("theIntegratedServerField", "theIntegratedServer", "???");
		addToken("MinecraftClassName", "net/minecraft/client/Minecraft", "???");
		addToken("inGameHasFocusField", "inGameHasFocus", "???");
		addToken("setInitialDisplayModeMethod", "setInitialDisplayMode", "???");
		addToken("setFramebufferColorMethod", "setFramebufferColor", "???");
		addToken("OPEN_INVENTORYField", "OPEN_INVENTORY", "???");
		addToken("blockColorsInitMethod", "init", "???");
		addToken("blockColorsField", "blockColors", "???");
	}

	@Override
	public void transformFunction(String name, MethodNode method, Tokens tokens) {
		if (name.equals("constructorMethod")) {
			AbstractInsnNode lastInsn = null;
			AbstractInsnNode currInsn = null;
			
			for (int index = 0; index < method.instructions.size(); index++) {
				lastInsn = currInsn;
				currInsn = method.instructions.get(index);
				
				if (lastInsn == null) {
					continue;
				}
				if (lastInsn.getOpcode() == Opcodes.ACONST_NULL &&
						currInsn.getOpcode() == Opcodes.PUTFIELD && ((FieldInsnNode)currInsn).name.equals(tokens.lookup("theIntegratedServerField"))) {
					InsnList toInject = new InsnList();

					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/continuum/nova/injector/Interface", "minecraft_preInitializeNova", "()Z", false));
					toInject.add(new FieldInsnNode(Opcodes.PUTFIELD, tokens.lookup("MinecraftClassName"), tokens.lookup("inGameHasFocusField"), "Z"));
					
					toInject.add(new LabelNode(new Label()));
					
					method.instructions.insertBefore(method.instructions.get(index + 1), toInject);
				}
			}
		}else if (name.equals("startGameMethod")) {
			AbstractInsnNode lastInsn = null;
			AbstractInsnNode currInsn = null;
			boolean deleteInsn = false;
			
			for (int index = 0; index < method.instructions.size(); index++) {
				lastInsn = currInsn;
				currInsn = method.instructions.get(index);
				
				if (lastInsn == null) {
					continue;
				}
				
				if (lastInsn.getOpcode() == Opcodes.INVOKESPECIAL && ((MethodInsnNode)lastInsn).name.equals(tokens.lookup("setInitialDisplayModeMethod"))) {
					deleteInsn = true;
				}else if (lastInsn.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode)lastInsn).name.equals(tokens.lookup("setFramebufferColorMethod"))) {
					deleteInsn = false;
				}else if (lastInsn.getOpcode() == Opcodes.ALOAD && currInsn.getOpcode() == Opcodes.LDC && ((LdcInsnNode)currInsn).cst.equals("Pre startup")) {
					method.instructions.remove(lastInsn);
					index--;
					deleteInsn = true;
				}else if (lastInsn.getOpcode() == Opcodes.LDC && ((LdcInsnNode)lastInsn).cst.equals("Startup") && currInsn.getOpcode() == Opcodes.INVOKESPECIAL) {
					method.instructions.remove(currInsn);
					index--;
					deleteInsn = false;
					continue;
				}
				if (deleteInsn) {
					method.instructions.remove(currInsn);
					index--;
				}else {
					if (currInsn.getOpcode() == Opcodes.GETSTATIC && ((FieldInsnNode)currInsn).name.equals(tokens.lookup("OPEN_INVENTORYField"))) {
						InsnList toInject = new InsnList();

						toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/continuum/nova/injector/Interface", "minecraft_registerReloadListener", "()V", false));
						
						toInject.add(new LabelNode(new Label()));
						
						method.instructions.insertBefore(method.instructions.get(index + 1), toInject);
					}else if (lastInsn.getOpcode() == Opcodes.INVOKESTATIC && ((MethodInsnNode)lastInsn).name.equals(tokens.lookup("blockColorsInitMethod")) &&
							  currInsn.getOpcode() == Opcodes.PUTFIELD && ((FieldInsnNode)currInsn).name.equals(tokens.lookup("blockColorsField"))) {
						InsnList toInject = new InsnList();
						
						toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/continuum/nova/injector/Interface", "minecraft_loadStartupShaderpack", "()V", false));
						
						method.instructions.insert(currInsn, toInject);
					}
				}
			}
		}
	}

	@Override
	public void transformField(String name, FieldNode field, Tokens tokens) {
		if (name.equals("mcResourceManagerField") || name.equals("blockColorsField")) {
			field.access &= ~Opcodes.ACC_PRIVATE;
			field.access |= Opcodes.ACC_PUBLIC;
		}
	}

	@Override
	public void addData(String name, ClassNode clazz, Tokens tokens) {
		
	}
}
