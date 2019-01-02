package org.sel.data.column;

public class Column {
	private String name;
	private String comment;
	private String type;
	private boolean isPrimary;
	// 是否去重字段
	private boolean deDuplication = false;
	private Object defaultValue;

	public Column(String name, String comment, String type, boolean isPrimary) {
		this.name = name;
		this.comment = comment;
		this.type = type;
		this.isPrimary = isPrimary;
	}

	public boolean isDeDuplication() {
		return deDuplication;
	}

	public void setDeDuplication(boolean deDuplication) {
		this.deDuplication = deDuplication;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public String getComment() {
		return comment;
	}

	public boolean isPrimary() {
		return isPrimary;
	}

	public String getType() {
		return type;
	}

}
