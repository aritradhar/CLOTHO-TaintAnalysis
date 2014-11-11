package soot.jimple.infoflow.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Filter {
	
	static final String suresh = "@Suresh : ";

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File("output.txt")));
		FileWriter fs = new FileWriter("SourcesAndSinks.txt", true);
		
		String line;
		while((line = br.readLine()) != null){
			if(line.contains(" get") && line.startsWith(suresh)){
				fs.append(line.substring(suresh.length()) + " -> _SINK_\n");
			}
			if(line.contains(" set") && line.startsWith(suresh)){
				fs.append(line.substring(suresh.length()) + " -> _SOURCE_\n");
			}
		}
		
		fs.close();
		br.close();
		
		System.out.println("All Done");
	}
}
