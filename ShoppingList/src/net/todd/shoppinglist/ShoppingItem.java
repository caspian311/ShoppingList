package net.todd.shoppinglist;

public class ShoppingItem {
	private String value;
	private boolean isChecked;
	private String id;

	public ShoppingItem(String id, String value) {
		this.id = id;
		this.value = value;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getValue() {
		return this.value;
	}

	public void check() {
		isChecked = true;
	}
	
	public void uncheck() {
		isChecked = false;
	}
	
	public boolean isChecked() {
		return isChecked;
	}
}
