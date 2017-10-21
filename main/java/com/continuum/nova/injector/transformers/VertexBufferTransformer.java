package com.continuum.nova.injector.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.continuum.nova.injector.NovaCorePlugin;
import com.continuum.nova.injector.NovaTransformer;
import com.continuum.nova.injector.Tokens;

public class VertexBufferTransformer extends NovaTransformer {

	@Override
	public void initialize() {
		setClassName("net.minecraft.client.renderer.VertexBuffer", "???");
		
		addField("vertexCountField", "vertexCount", "???");
	}

	@Override
	public void transformFunction(String name, MethodNode method, Tokens tokens) {
		
	}

	@Override
	public void transformField(String name, FieldNode field, Tokens tokens) {
		if (name.equals("vertexCountField")) {
			field.access &= ~Opcodes.ACC_PRIVATE;
			field.access |= Opcodes.ACC_PROTECTED;
		}
	}

	@Override
	public void addData(String name, ClassNode clazz, Tokens tokens) {
		
	}
}
