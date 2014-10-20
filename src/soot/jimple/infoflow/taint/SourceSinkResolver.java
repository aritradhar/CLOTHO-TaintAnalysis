package soot.jimple.infoflow.taint;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.infoflow.Infoflow;
import soot.jimple.infoflow.InfoflowResults;
import soot.jimple.infoflow.InfoflowResults.SinkInfo;
import soot.jimple.infoflow.InfoflowResults.SourceInfo;
import soot.jimple.infoflow.source.DefaultSourceSinkManager;
import soot.jimple.infoflow.taintWrappers.EasyTaintWrapper;

public class SourceSinkResolver {

	final PrintStream ps = System.out;
	private Infoflow infoflow;
	private InfoflowResults results;

	static final List<String> sources = new ArrayList<String>();
	static final List<String> sinks = new ArrayList<String>();

	private final HashMap<String, PatchingChain<Unit>> methodToChain;

	static{
		TaintSourceSinkManager.setSourceSink("SourcesAndSinks.txt", sources, sinks);
	}


	/**
	 * Runs the whole TaintAnalysis and stores the results inside a Map<Sink, Set<Source>> 
	 * @param args[0] = path to jar file, args[1] EntryPoint for the program
	 */
	public SourceSinkResolver(String[] args){
		if(args.length < 2) {
			methodToChain = null;
			ps.println("Invalid Arguments");
			return;
		}

		infoflow = new Infoflow();
		infoflow.setSootConfig(new SootConfigForProgramRepair());
		try {
			infoflow.setTaintWrapper(new EasyTaintWrapper("EasyTaintWrapperSource.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		infoflow.computeInfoflow(args[0],
				null, args[1], new DefaultSourceSinkManager(sources, sinks));

		this.methodToChain = infoflow.getMethodToChain();
		this.results = infoflow.getResults();
	}	

	/**
	 * We need to decide what is the signature for the unit
	 * @param sinkSignature
	 * @return true if its a sink
	 */
	public boolean isSink(String sinkSignature){
		return results.containsSinkMethod(sinkSignature);
	}

	/**
	 * We need to decide what is the signature for the unit
	 * @param sinkSignature
	 * @return true if there is a path
	 */
	public boolean isOnPathSourceSink(String sink, String source){
		return results.isPathBetween(sink, source);
	}

	@Override
	public String toString(){
		return results.toString();
	}

	/**
	 * The unit to check for Taint 
	 * @param u Unit of a patching chain or a soot block
	 * @return true if its safe to patch the given unit
	 */
	public boolean isSafe(Unit u){
		try{
			Stmt stmt = (Stmt) u;
			Map<SinkInfo, Set<SourceInfo>> res = results.getResults(); 
			for (SinkInfo si : res.keySet()){
				if(si.getContext().equals(stmt))
					return false;
				
				Set<SourceInfo> sources = res.get(si);
				for (SourceInfo src : sources) {
					if(src.getContext().equals(stmt))
						return false;
					for(Stmt path : src.path){
						if(path.equals(stmt))
							return false;
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}

		return true;
	}
	
	/**
	 * The unit to check for taint
	 * @param u Unit to check
	 * @param m SootMethod of the unit
	 * @param i The iteration @which the unit was encountered
	 * @return true if its safe to Patch the given unit
	 */
	public boolean isSafe(Unit u, SootMethod m, int i){
		PatchingChain<Unit> pc = methodToChain.get(m.toString());
		int ct = 0;
		for(Unit uu : pc){
			if(i == ct++){
				if(u.equals(uu))
					u = uu;
				else 
					u = uu;
				break;
			}
		}
		return isSafe(u);
	}
	
	/**
	 * @return String representation of the Map Stored
	 */
	public String toStringMethodToChainMap(){
		return methodToChain.toString();
	}

}
