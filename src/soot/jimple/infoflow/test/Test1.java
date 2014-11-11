package soot.jimple.infoflow.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test1 {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		new Test1().work();
	}

	public void work() throws IOException{
		Runnable r = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
				int x;
				try {
					x = Integer.parseInt(new String(is.readLine()));
					int y = x;
					System.out.println(y);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}				
		};
		
		new Thread(r).start();
	}

}
