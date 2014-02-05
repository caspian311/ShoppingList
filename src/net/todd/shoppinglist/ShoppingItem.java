package net.todd.shoppinglist;

public class ShoppingItem {
	private String value;
	private boolean isChecked;

	public ShoppingItem(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

	public void check() {
		isChecked = true;
	}
	
	public void unCheck() {
		isChecked = false;
	}
	
	public boolean isChecked() {
		return isChecked;
	}
}
