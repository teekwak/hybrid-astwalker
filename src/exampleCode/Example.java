/*
 * List of things in this code (line numbers)
 * 
 * 1 package (30)
 * 7 imports (32, 33, 34, 35, 36, 37, 38)
 * 4 regular classes (49, 69, 78, 80)
 * 2 super classes (49, 69)
 * 3 interfaces (40, 44, 50) 
 * 2 implements (69, 69)
 * 4 arrays (108, 112, 116, 156)
 * 1 catch clause (189)
 * 1 conditional expression (181)
 * 2 do statements (185, 198)
 * 4 for statements (97, 103, 157, 161)
 * 8 generics (96, 102, 108, 112, 117, 118, 119, 144)
 * 3 if statements (55, 132, 191)
 * 10 infix expressions (55, 65, 132, 161, 170, 177, 181, 188, 191, 201)
 * 14 method declarations (41, 45, 46, 51, 54, 60, 64, 73, 91, 96, 102, 108, 112, 116)
 * 13 method invocations (56, 61, 65, 92, 93, 104, 121, 122, 123, 129, 136, 140, 141, 142, 145, 148, 150, 152, 158, 163, 164, 171, 175, 181, 199)
 * 27 primitives (60, 64, 64, 71, 73, 81, 85, 86, 87, 88, 108, 108, 108, 108, 112, 112, 112, 112, 131, 157, 161, 162, 168, 180, 184, 190, 197)
 * 1 throw statement (194)
 * 1 try statement (183)
 * 8 simple names (70, 84, 89, 97, 103, 125, 139, 189)
 * 1 switch statement (147)
 * 1 while statement (170)
 * 1 wildcard (96)
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

public class Example extends SomeSuperClass implements thing1, thing2, thing3 {
	interface inf {
		void print();
		String getName();
	}
	
	public static void main(String[] args) {
		System.out.println(true && true);
		System.out.println(1 > 0);
		throw new Exception();
	}

	public int returnOne() throws IllegalArgumentException {
		class insideMethod {
			String why;
		}
		return 1;
	}
	
	public void printLine() {
		try{
			// do something
		}
		catch(NullPointerException e) {
			
		}
		return;
	}

	class Another {
		public double num() {
			return 1.2;
		}
	}
}

/*

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
		for(Object e : c) {
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

*/