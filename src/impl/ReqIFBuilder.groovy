package impl;

import java.util.jar.Attributes;

import java.util.Map;

import groovy.util.BuilderSupport;

class ReqIFBuilder extends BuilderSupport {

	int level = 0
	def result = new StringWriter()
	
	@Override
	protected void setParent(Object parent, Object child) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object createNode(Object name) {
		if (name == "build") {
			result << "Begin: \n"
			return "buildnode"
		} else 
			return handle(name, [:])
	}

	@Override
	protected Object createNode(Object name, Object value) {
		level++
		level.times {result << " "}
		result << name + " "
		
		if (value instanceof Integer) {
			result << "int[${value}]\n"
		} else
			result << "(${value})\n"
		level--
	}

	@Override
	protected Object createNode(Object name, Map attributes) {
		handle(name, attributes)
	}

	@Override
	protected Object createNode(Object name, Map attributes, Object value) {
		throw new Exception("Invalid format")
	}

	def propertyMissing(String name) {
		handle(name, [:])
	}
	
	void nodeCompleted(parent, node) {
		level--
		if (node == "buildnode")
			println result
	}
	
	def handle(String name, Map attributes) {
		level++
		level.times {result << " "}
		result << name
		result << printParameters(attributes)
		result << "\n"
	}
	
	def printParameters(attributes) {
		def values = ""
		if (attributes.size() > 0) {
			values += " ["
			def count = 0
			attributes.each { key, value ->
				if (count > 0)
					values += ","
				count++
				values += (count > 1 ? " " : "")
				values += "${key} -> ${value}"	
			}
			values += "]"
		}
		values
	}
	
	static main(args) {
		def b = new ReqIFBuilder()
		
		b.build {
			Blub(x: 1, y: 2)
			Blub(z: 3, w: 4)
			Blub("this is a parameter")
			Blub(1)	
		}
	}
}

