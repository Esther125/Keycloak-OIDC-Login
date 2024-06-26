/*
 *    Copyright 2016-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.kazuki43zoo.jpetstore.ui;

import com.kazuki43zoo.jpetstore.domain.Item;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kazuki Shimizu
 */
@SessionScope
@Component
public class Cart implements Serializable {

	private static final long serialVersionUID = 8329559983943337176L;

	private final Map<String, CartItem> itemMap = new ConcurrentHashMap<>();
	private final List<CartItem> itemList = new ArrayList<>();

	public List<CartItem> getCartItems() {
		return itemList;
	}

	public boolean containsByItemId(String itemId) {
		return itemMap.containsKey(itemId);
	}

	public void addItem(Item item, boolean isInStock) {
		CartItem cartItem = itemMap.get(item.getItemId());
		if (cartItem == null) {
			cartItem = new CartItem();
			cartItem.setItem(item);
			cartItem.setQuantity(0);
			cartItem.setInStock(isInStock);
			itemMap.put(item.getItemId(), cartItem);
			itemList.add(cartItem);
		}
		cartItem.incrementQuantity();
	}

	public void removeItemById(String itemId) {
		Optional.ofNullable(itemMap.remove(itemId))
				.ifPresent(itemList::remove);
	}

	public void incrementQuantityByItemId(String itemId) {
		CartItem cartItem = itemMap.get(itemId);
		cartItem.incrementQuantity();
	}

	public void setQuantityByItemId(String itemId, int quantity) {
		CartItem cartItem = itemMap.get(itemId);
		cartItem.setQuantity(quantity);
	}

	public BigDecimal getSubTotal() {
		return getCartItems().stream()
				.map(x -> x.getItem().getListPrice().multiply(new BigDecimal(x.getQuantity())))
				.reduce(new BigDecimal(0), BigDecimal::add);
	}

	public boolean isEmpty() {
		return itemList.isEmpty();
	}

	public void clear() {
		itemMap.clear();
		itemList.clear();
	}

}
