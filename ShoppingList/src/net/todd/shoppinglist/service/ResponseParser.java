package net.todd.shoppinglist.service;

public interface ResponseParser<T> {
	T parseResponse(String response) throws Exception;
}