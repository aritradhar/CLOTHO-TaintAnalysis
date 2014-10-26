package soot.jimple.infoflow.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test1 {

	public static void main(String[] args){
		// TODO Auto-generated method stub
		new Test1().work();
	}
	
	public void work(){
		try{
			BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
			int x = Integer.parseInt(new String(is.readLine()));
			int y = x;
			System.out.println(y);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
