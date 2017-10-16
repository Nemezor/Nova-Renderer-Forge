package com.continuum.nova.injector;

import java.util.HashMap;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class NovaTransformer {

	private HashMap<String, String> mcp = new HashMap<String, String>();
	private HashMap<String, String> obf = new HashMap<String, String>();
	private HashMap<String, String> funcMcp = new HashMap<String, String>();
	private HashMap<String, String> funcObf = new HashMap<String, String>();
	private HashMap<String, String> fieldMcp = new HashMap<String, String>();
	private HashMap<String, String> fieldObf = new HashMap<String, String>();
	private String nameObf;
	private String nameMcp;
	
	public abstract void initialize();
	public abstract void transformFunction(String name, MethodNode method, Tokens tokens);
	public abstract void transformField(String name, FieldNode field, Tokens tokens);
	public abstract void addData(String name, ClassNode clazz, Tokens tokens);
	
	public NovaTransformer() {
		initialize();
	}
	
	public void setClassName(String regular, String obfuscated) {
		nameObf = obfuscated;
		nameMcp = regular;
	}
	
	public void addToken(String key, String regular, String obfuscated) {
		mcp.put(key, regular);
		obf.put(key, obfuscated);
	}
	
	public void addMethod(String name, String regular, String obfuscated) {
		addToken(name, regular, obfuscated);
		funcMcp.put(regular, name);
		funcObf.put(obfuscated, name);
	}
	
	public void addField(String name, String regular, String obfuscated) {
		addToken(name, regular, obfuscated);
		fieldMcp.put(regular, name);
		fieldObf.put(obfuscated, name);
	}
	
	protected byte[] transformClassInternal(String className, byte[] data, boolean obfuscated) {
		NovaCorePlugin.log("Transforming '" + className);
		Tokens tokens = new Tokens(obfuscated ? obf : mcp);
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(data);
		classReader.accept(classNode, 0);
		
		addData(obfuscated ? nameObf : nameMcp, classNode, tokens);
		
		Iterator<FieldNode> fields = classNode.fields.iterator();
		
		while (fields.hasNext()) {
			FieldNode f = fields.next();
			String name = null;
			
			if (obfuscated) {
				if (fieldObf.containsKey(f.name)) {
					name = fieldObf.get(f.name);
					
				}
			}else {
				if (fieldMcp.containsKey(f.name)) {
					name = fieldMcp.get(f.name);
				}
			}
			if (name != null) {
				NovaCorePlugin.log("Patching field '" + name + "' ('" + f.name + "')");
				transformField(name, f, tokens);
			}
		}
		
		Iterator<MethodNode> methods = classNode.methods.iterator();

		while (methods.hasNext()) {
			MethodNode m = methods.next();
			String name = null;
			
			if (obfuscated) {
				if (funcObf.containsKey(m.name)) {
					name = funcObf.get(m.name);
					
				}
			}else {
				if (funcMcp.containsKey(m.name)) {
					name = funcMcp.get(m.name);
				}
			}
			if (name != null) {
				NovaCorePlugin.log("Patching method '" + name + "' ('" + m.name + "')");
				transformFunction(name, m, tokens);
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}
	
	public byte[] transformClass(String name, byte[] data) {
		if (name.equals(nameObf)) {
			return transformClassInternal(name, data, true);
		}else if (name.equals(nameMcp)) {
			return transformClassInternal(name, data, false);
		}else{
			return data;
		}
	}
}
