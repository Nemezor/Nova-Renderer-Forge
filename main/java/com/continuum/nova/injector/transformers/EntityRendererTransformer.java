package com.continuum.nova.injector.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.continuum.nova.injector.NovaCorePlugin;
import com.continuum.nova.injector.NovaTransformer;
import com.continuum.nova.injector.Tokens;

public class EntityRendererTransformer extends NovaTransformer {

	@Override
	public void initialize() {
		setClassName("net.minecraft.client.renderer.EntityRenderer", "???");
		
		addField("fogColorRedField", "fogColorRed", "???");
		addField("fogColorGreenField", "fogColorGreen", "???");
		addField("fogColorBlueField", "fogColorBlue", "???");
	}

	@Override
	public void transformFunction(String name, MethodNode method, Tokens tokens) {
		
	}

	@Override
	public void transformField(String name, FieldNode field, Tokens tokens) {
		if (name.equals("fogColorRedField") || name.equals("fogColorGreenField") || name.equals("fogColorBlueField")) {
			if (field.desc.equals("F")) {
				field.access &= ~Opcodes.ACC_PRIVATE;
				field.access |= Opcodes.ACC_PUBLIC;
			}
		}
	}

	@Override
	public void addData(String name, ClassNode clazz, Tokens tokens) {
		
	}
}
