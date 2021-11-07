package com.hust.tacocloud.data;

import com.hust.tacocloud.Order;

public interface OrderRepository {
    Order save(Order order);
}
