package com.continuum.nova.injector.transformers;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.continuum.nova.injector.NovaTransformer;
import com.continuum.nova.injector.Tokens;

public class FontRendererTransformer extends NovaTransformer {

	@Override
	public void initialize() {
		setClassName("net.minecraft.client.gui.FontRenderer", "???");
		
		addMethod("renderDefaultCharMethod", "renderDefaultChar", "???");
		addMethod("setColorMethod", "setColor", "???");
		
		addToken("locationFontTextureField", "locationFontTexture", "???");
		addToken("locationFontTextureFieldType", "Lnet/minecraft/util/ResourceLocation;", "???");
		addToken("charWidthField", "charWidth", "???");
		addToken("posXField", "posX", "???");
		addToken("posYField", "posY", "???");
		addToken("renderDefaultCharMethodDesc", "(IZFF[ILnet/minecraft/util/ResourceLocation;)F", "???");
		addToken("fontRendererClassName", "net/minecraft/client/gui/FontRenderer", "???");
		
		addToken("localVariable_ch", "ch", "???");
		addToken("localVariable_italic", "italic", "???");
		addToken("localVariable_r", "r", "???");
		addToken("localVariable_g", "g", "???");
		addToken("localVariable_b", "b", "???");
		addToken("localVariable_a", "a", "???");
	}
	
	@Override
	public void transformFunction(String name, MethodNode method, Tokens tokens) {
		if (name.equals("renderDefaultCharMethod")) {
			AbstractInsnNode currInsn = null;
			
			for (int index = 0; index < method.instructions.size(); index++) {
				currInsn = method.instructions.get(index);
				
				if (currInsn.getOpcode() == Opcodes.ILOAD) {
					InsnList toInject = new InsnList();

					LabelNode L0 = new LabelNode(new Label());
					LabelNode L1 = new LabelNode(new Label());
					
					toInject.add(L0);
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 1));
					toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, tokens.lookup("fontRendererClassName"), tokens.lookup("posXField"), "F"));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, tokens.lookup("fontRendererClassName"), tokens.lookup("posYField"), "F"));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, tokens.lookup("fontRendererClassName"), tokens.lookup("charWidthField"), "[I"));
					toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInject.add(new FieldInsnNode(Opcodes.GETFIELD, tokens.lookup("fontRendererClassName"), tokens.lookup("locationFontTextureField"), tokens.lookup("locationFontTextureFieldType")));
					toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/continuum/nova/injector/Interface", "fontRenderer_renderDefaultChar", tokens.lookup("renderDefaultCharMethodDesc"), false));
					toInject.add(new InsnNode(Opcodes.FRETURN));
					toInject.add(L1);
					
					method.localVariables.clear();
					method.localVariables.add(new LocalVariableNode("this", "L" + tokens.lookup("fontRendererClassName") + ";", null, L0, L1, 0));
					method.localVariables.add(new LocalVariableNode(tokens.lookup("localVariable_ch"), "I", null, L0, L1, 1));
					method.localVariables.add(new LocalVariableNode(tokens.lookup("localVariable_italic"), "Z", null, L0, L1, 2));

					method.instructions.clear();
					method.instructions.add(toInject);
					
					break;
				}
			}
		}else if (name.equals("setColorMethod")) {
			AbstractInsnNode currInsn = null;
			
			for (int index = 0; index < method.instructions.size(); index++) {
				currInsn = method.instructions.get(index);
				
				if (currInsn.getOpcode() == Opcodes.FLOAD) {
					InsnList toInject = new InsnList();

					LabelNode L0 = new LabelNode(new Label());
					LabelNode L1 = new LabelNode(new Label());
					
					toInject.add(L0);
					toInject.add(new VarInsnNode(Opcodes.FLOAD, 1));
					toInject.add(new VarInsnNode(Opcodes.FLOAD, 2));
					toInject.add(new VarInsnNode(Opcodes.FLOAD, 3));
					toInject.add(new VarInsnNode(Opcodes.FLOAD, 4));
					toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/continuum/nova/injector/Interface", "fontRenderer_setColor", "(FFFF)V", false));
					toInject.add(new InsnNode(Opcodes.RETURN));
					toInject.add(L1);
					
					method.localVariables.clear();
					method.localVariables.add(new LocalVariableNode("this", "L" + tokens.lookup("fontRendererClassName") + ";", null, L0, L1, 0));
					method.localVariables.add(new LocalVariableNode(tokens.lookup("localVariable_r"), "F", null, L0, L1, 1));
					method.localVariables.add(new LocalVariableNode(tokens.lookup("localVariable_g"), "F", null, L0, L1, 2));
					method.localVariables.add(new LocalVariableNode(tokens.lookup("localVariable_b"), "F", null, L0, L1, 3));
					method.localVariables.add(new LocalVariableNode(tokens.lookup("localVariable_a"), "F", null, L0, L1, 4));

					method.instructions.clear();
					method.instructions.add(toInject);
					
					break;
				}
			}
		}
	}

	@Override
	public void transformField(String name, FieldNode field, Tokens tokens) {
		
	}

	@Override
	public void addData(String name, ClassNode clazz, Tokens tokens) {
		
	}
}
