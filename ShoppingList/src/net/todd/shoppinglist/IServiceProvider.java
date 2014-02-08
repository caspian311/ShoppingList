package net.todd.shoppinglist;

public interface IServiceProvider {
	boolean isServiceBound();
	IDataService getService();
}
