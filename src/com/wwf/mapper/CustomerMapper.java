package com.wwf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wwf.domain.Customer;

public interface CustomerMapper {

	void add(Customer customer);

	List<Customer> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
