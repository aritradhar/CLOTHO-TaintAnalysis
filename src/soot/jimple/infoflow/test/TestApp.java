package soot.jimple.infoflow.test;

import java.io.PrintStream;

import soot.jimple.infoflow.taint.SourceSinkResolver;

public class TestApp {

	final PrintStream ps = System.out;
	final static boolean DEBUG = true;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SourceSinkResolver ssr = new SourceSinkResolver(args);
		System.out.println(ssr.toStringMethodToChainMap());
	}
}
