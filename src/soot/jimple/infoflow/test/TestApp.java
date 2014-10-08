package soot.jimple.infoflow.test;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import soot.jimple.infoflow.Infoflow;
import soot.jimple.infoflow.source.DefaultSourceSinkManager;
import soot.jimple.infoflow.taint.SootConfigForProgramRepair;
import soot.jimple.infoflow.taint.TaintSourceSinkManager;
import soot.jimple.infoflow.taintWrappers.EasyTaintWrapper;

public class TestApp {

	final PrintStream ps = System.out;
	final static boolean DEBUG = true;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new TestApp().start(args);
	}

	private void start(String[] args) {
		// TODO Auto-generated method stub
		if(args.length < 1) {
			ps.println("Invalid Arguments");
			return;
		}
		
		Infoflow infoflow = new Infoflow();
		infoflow.setSootConfig(new SootConfigForProgramRepair());
		try {
			infoflow.setTaintWrapper(new EasyTaintWrapper("EasyTaintWrapperSource.txt"));
			TaintSourceSinkManager.setSourceSink("SourcesAndSinks.txt", sources, sinks);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		infoflow.computeInfoflow(args[0],
				null, args[1], new DefaultSourceSinkManager(sources, sinks));
		
	}
	
	static final List<String> sources = new ArrayList<String>();
	static final List<String> sinks = new ArrayList<String>();

}
