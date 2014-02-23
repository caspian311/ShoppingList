package net.todd.shoppinglist.service;

import java.util.List;

public interface ResponseParser<T> {
	List<T> parseResponse(String response) throws Exception;
}