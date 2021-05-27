package rnd.tool.code.model;

public class ElementInfoImpl implements ElementInfo {

	private String modifiers;
	private String type;
	private String name;
	private String init;

	public ElementInfoImpl(String type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getType() {
		return this.type;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}
	
	@Override
	public String getModifier() {
		return this.modifiers;
	}

	public void setInit(String init) {
		this.init = init;
	}

	@Override
	public String getInit() {
		return init;
	}

	public String toString() {
		return "[name=" + name + ", type=" + type + "]";
	}
}