/*
 * List of things in this code
 * 
 * 1 package
 * 6 imports
 * 
 */

package exampleCode;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class Example {
	class firstInnerClass {
		
	}
	
	class secondInnerClass {
		
	}
	
	class thirdInnerClass {
		
	}
}

class firstOuterClass {
	
}














/*

class Blah implements SomethingElse {
	void print() {
		System.out.println("this is blah");
		if(1 == 2) {
			System.out.println("never happens");
		}
	}
	
	void print(int a) {
		System.out.println(a);
	}
	
	void print(int a, int b) {
		System.out.println(a + b);
	}
}

class Node extends LinkedList implements Junk1, Junk2{
	Node next;
	int data;
	
	Node(int data) {
		this.data = data;
	}
}

interface Ni {
	void print();
	int getSize();
}

public class Example {
	
	class meh {
		double score = 0;
	}
	
	String name = "";
	int neighbors;
	double doubles;
	float points;
	boolean things;
	Integer biginter;
	
	public void print() {
		System.out.println(this.name);
		System.out.println(this.neighbors);
	}
	
	public void printCollection(Collection<?> c) {
	    for (Object e : c) {
	        System.out.println(e);
	    }
	}
	
	public void printCollection2(Collection<Object> d) {
		for(Object f : d) {
			System.out.println(f);
		}
	}
	
	public int returnSomethingAmazing(int a, double b, boolean c, float d, Map<String, Integer> e, int[][] f) {
		return 2;
	}
	
	public int returnSomethingAmazing(int a, double b, boolean c, float d, Map<String, Integer> e, double[][] f) {
		return 4;
	}
	
	public static void main(String[] args) throws IOException{
		List<String> list = new LinkedList<>();
		Map<String, Integer> map = new HashMap<String, Integer>();

		Example ex = new Example();
		ex.neighbors = 5;
		ex.name = "example_1";
		
		ex.print();
				
		int i = 0;
		if(i == 1) {
			System.out.println("this is somehow right");
		}
		else {
			System.out.println("nvm");
		}
		
		Blah blah = new Blah();
		blah.print();
		blah.print(1);
		blah.print(3, 4);
		
		HashMap<String, HashMap<String, Integer>> reallyBigMap = new HashMap<>();
		
		switch(i) {
			case 1: System.out.println("1");
				break;
			case 2: System.out.println("2");
				break;
			default: System.out.println("default");
				break;
		}
		
		int[] arr = {1,2,3,4,5};
		for(int a : arr) {
			System.out.println(a);
		}
		
		for(int k = 0; k < 10; k++) {
			double j = Math.sqrt(100);
			System.out.println(k);
		}
		
		
		int counter = 3;
		
		while(counter > 0) {
			System.out.println(counter);
			counter--;
		}
		
		ex.print();
		
		System.out.println(3 == 4);
		
		System.out.println(1 == 2 ? "yes" : "no");
		
		try {
			int a = 0;
		} catch (NullPointerException e) {
			int b = 1;
			throw new IOException();
		}
		
		int asdf = 200;
		do{
			System.out.println("hi");
		}
		while (asdf < 199);
		
	}
}
*/