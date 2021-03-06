package net.todd.shoppinglist;

public class ShoppingItem {
	private String value;
	private boolean isChecked;
	private String id;

	public ShoppingItem(String id, String value, boolean isChecked) {
		this.id = id;
		this.value = value;
		this.isChecked = isChecked;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (isChecked ? 1231 : 1237);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShoppingItem other = (ShoppingItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isChecked != other.isChecked)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
