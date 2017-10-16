package com.continuum.nova.injector.transformers;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class AllClassTransformer {

	public byte[] transform(String name, byte[] data) {
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(data);
		classReader.accept(classNode, 0);

		Iterator<MethodNode> methods = classNode.methods.iterator();
		
		while (methods.hasNext()) {
			MethodNode m = methods.next();
			
			for (int i = 0; i < m.instructions.size(); i++) {
				AbstractInsnNode insn = m.instructions.get(i);
				
				if (insn.getOpcode() == Opcodes.INVOKESTATIC) {
					MethodInsnNode node = (MethodInsnNode)insn;
					
					if (node.owner.equals("org/lwjgl/input/Keyboard")) {
						node.owner = "com/continuum/nova/input/Keyboard";
					}else if (node.owner.equals("org/lwjgl/input/Mouse")) {
						node.owner = "com/continuum/nova/input/Mouse";
					}else if (node.owner.equals("org/lwjgl/opengl/Display")) {
						//TODO Display class
					}
				}
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}
}
