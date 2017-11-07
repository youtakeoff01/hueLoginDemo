/*package com.hand.hueLoginDemo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class Java8Test {
	public static void main(String[] args) {
		
		List<Person> people = Arrays.asList(new Person(),new Person());
		
		people.sort((person1,person2)-> person1.getBirthday().compareTo(person2.getBirthday()));
		
		people.sort(Person::compareByage);
		
		Optional<List<Person>> op = Optional.ofNullable(people);
		
		op.map(people1 -> people1 ).orElse(null);
		
		List<Person> lists = Arrays.asList(new Person("a",13),new Person("a",24),new Person("b",16));
		
		int num = lists.stream().
				filter(person -> "a".equals(person.getName())).mapToInt(person -> person.getAge()).sum();
	}
}
*/