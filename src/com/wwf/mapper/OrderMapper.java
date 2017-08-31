package com.wwf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wwf.domain.Order;

public interface OrderMapper {

	void genOrder(@Param("order") Order order, @Param("customerId") String customerId);

	List<Order> findOrderByCustomerid(String id);

}
