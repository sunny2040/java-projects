package com.data;

import java.util.HashMap;

public class StudentTest {

	String name;
	Integer id;
	HashMap<String, Integer> map;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public HashMap<String, Integer> getMap() {
		return map;
	}

	public void setMap(HashMap<String, Integer> map) {
		this.map = map;
	}

	@Override
	public String toString() {
		return "StudentTest [name=" + name + ", id=" + id + ", map=" + map + "]";
	}

}
