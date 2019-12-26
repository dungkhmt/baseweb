package com.hust.baseweb.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("test..");
		List<String> list= new ArrayList<>();
		list.add("A");
		list.add("B");
		list.add("C");
		list.add("D");
		list.add("E");
		list.add("F");
		list=list.stream().map(sg->{
			return sg+"W";
		}).collect(Collectors.toList());
		list.forEach(sg-> System.out.println(sg));
		
		
	}

}
