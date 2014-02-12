package net.todd.shoppinglist.service;

public class ShoppingListChange {
	public static final int CREATED = 0;
	public static final int DELETED = 1;
	public static final int CHECKED = 2;
	public static final int UNCHECKED = 3;
	
	private String id;
	private int changeType;
	private String name;

	public ShoppingListChange(String id, int changeType, String name) {
		this.id = id;
		this.changeType = changeType;
		this.name = name;
		
	}
	
	public String getId() {
		return id;
	}
	
	public int getChangeType() {
		return changeType;
	}
	
	public String getName() {
		return name;
	}
}
