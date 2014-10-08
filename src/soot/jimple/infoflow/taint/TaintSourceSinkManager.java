package soot.jimple.infoflow.taint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import soot.jimple.infoflow.source.DefaultSourceSinkManager;

public class TaintSourceSinkManager extends DefaultSourceSinkManager{
	
	public TaintSourceSinkManager(List<String> sources, List<String> sinks,
			List<String> parameterTaintMethods, List<String> returnTaintMethods) {
		super(sources, sinks, parameterTaintMethods, returnTaintMethods);
		// TODO Auto-generated constructor stub
	}
	
	public TaintSourceSinkManager(List<String> sources, List<String> sinks) {
		super(sources, sinks);
		// TODO Auto-generated constructor stub
	}
	
	public static void setSourceSink(String sourceSink, List<String> sources, List<String> sinks){		
		try {
			BufferedReader br = new BufferedReader(new FileReader(sourceSink));
			String line;
			while(null != ( line = br.readLine())){
				if(line.startsWith("%")) 
					continue;
				
				if(line.endsWith("_SINK_")) {
					try{
						sinks.add(line.substring(0, line.lastIndexOf('-')).trim());
					}catch(ArrayIndexOutOfBoundsException e) {}
				}
				else if(line.endsWith("_SOURCE_")){
					try{
						sources.add(line.substring(0, line.lastIndexOf('-')).trim());
					}catch(ArrayIndexOutOfBoundsException e) {}
				}
				else{
					System.err.println("@Suresh Source-Sink invalid type " + line);
				}
			}
			
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
