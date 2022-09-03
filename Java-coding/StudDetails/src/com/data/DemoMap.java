package com.data;

import java.util.HashMap;

public class DemoMap {

	public static void main(String[] args) {
		StudentTest s1 = new StudentTest();
		s1.setId(1);
		s1.setName("pushpa");
		print(s1);

	}

	public static void print(StudentTest s1) {

		HashMap<String, Integer> hi = new HashMap<String, Integer>();
		hi.put("sem1", 1723);
		hi.put("sem2", 8987);
		s1.setMap(hi);

		System.out.println(s1);
	}

}
