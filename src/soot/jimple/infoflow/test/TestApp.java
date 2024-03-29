package soot.jimple.infoflow.test;

import java.io.PrintStream;

import soot.jimple.infoflow.taint.SourceSinkResolver;

public class TestApp {

	final PrintStream ps = System.out;
	final static boolean DEBUG = true;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String[] args1 = new String[]{
				/*
				"D:\\BTSync\\Thesis\\checkstyle-6.0\\checkstyle-6.0.jar",
				"<com.puppycrawl.tools.checkstyle.Main: void main(java.lang.String[])>"
				//*/
				//*
				"D:\\BTSync\\Thesis\\Jazzy\\jazzy-core.jar",
				"<soot.jimple.infoflow.test.Test1: void main(java.lang.String[])>",
				"com.swabunga.util.StringUtility: void main(java.lang.String[])>",
				"<com.swabunga.spell.swing.JSpellForm: void main(java.lang.String[])>",
				"<com.swabunga.spell.engine.EditDistance: void main(java.lang.String[])>"
				// */
				};
		SourceSinkResolver ssr = new SourceSinkResolver(args1, true);
		//ssr.setAccessPathLength(3);
		ssr.runAnalysis();
		System.out.println(ssr.toStringMethodToChainMap());
		System.out.println(ssr.getUnitCount());
		System.out.println(ssr.getSafeUnitCount());
		System.out.println(ssr.getUnsafeUnitCount());
	}
}
