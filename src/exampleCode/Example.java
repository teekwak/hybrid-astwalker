/*
 * List of things in this code (line numbers)
 * 
 * 1 package (29)
 * 7 imports (31, 32, 33, 34, 35, 36, 37)
 * 4 regular classes (48, 68, 77, 79)
 * 2 super classes (48, 68)
 * 3 interfaces (39, 43, 49) 
 * 2 implements (69, 69)
 * 4 arrays (51, 108, 112, 116)
 * 1 catch clause (189)
 * 1 conditional expression (181)
 * 2 do statements (185, 198)
 * 4 for statements (97, 103, 157, 161)
 * 8 generics
 * 3 if statements
 * 10 infix expressions
 * 14 method declarations
 * 13 method invocations
 * 27 primitives
 * 1 throw statement
 * 1 try statement
 * 8 simple names
 * 1 switch statement
 * 1 while statement
 * 1 wildcard
 * 
 */

package exampleCode;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

interface someInterface {
	public double abstractAction();
}

interface ThisIsAnInterface {
	void print();
	int getSize();
}

class OuterClass1 extends SomethingElse {
	interface InnerInterface {
		abstract double[][] abstractFunction();
	}
	
	void print() {
		if(1 < 2) {
			System.out.println("always happens");
		}
	}
	
	void print(int a) {
		System.out.println(a);
	}
	
	void print(int a, int b) {
		System.out.println(a + b);
	}
}

class OuterClass2 extends SomethingElse implements Junk1, Junk2{
	OuterClass2 next;
	int data;
	
	OuterClass2(int data) {
		this.data = data;
	}
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
		List<Object> anotherList = new ArrayList<>();
		Map<String, Integer> map = new HashMap<String, Integer>();

		list.add("this");
		anotherList.add(1);
		map.put("hello", 2);
		
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
		
		OuterClass1 blah = new OuterClass1();
		blah.print();
		blah.print(1);
		blah.print(3, 4);
		
		HashMap<String, HashMap<String, Integer>> reallyBigMap = new HashMap<>();
		reallyBigMap.put("s", new HashMap<>());
		
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
			System.out.println(j);
			System.out.println(k);
		}
		
		
		int counter = 3;
		
		while(counter > 0) {
			System.out.println(counter);
			counter--;
		}
		
		ex.print();
		
		System.out.println(3 == 4);
		
		
		int condVar = 0;
		System.out.println(condVar < 2 ? "yes" : "no");
		
		try {
			int a = 0;
			do{
				a += 1;
			}
			while (a < -1);
		} catch (NullPointerException e) {
			int b = 1;
			if(b < 2) {
				// do something
			}
			throw new IOException();
		}
		
		int asdf = 200;
		do{
			System.out.println("hi");
		}
		while (asdf < 199);
		
	}
}






