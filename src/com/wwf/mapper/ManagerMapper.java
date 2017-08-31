package com.wwf.mapper;

import java.util.List;
import java.util.Map;

import com.wwf.domain.Manager;


public interface ManagerMapper {
	/**
	 * map={"name":name, "password":password}
	 * @param map
	 * @return
	 */
	List<Manager> findManagerByNameAndPassword(Map<String, String> map);
}
