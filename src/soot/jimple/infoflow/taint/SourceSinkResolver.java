package soot.jimple.infoflow.taint;

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

	static final PrintStream ps = System.out;
	private Infoflow infoflow;
	private InfoflowResults results;

	public static final List<String> sources = new ArrayList<String>();
	public static final List<String> sinks = new ArrayList<String>();

	private HashMap<String, PatchingChain<Unit>> methodToChain;
	private String[] args = null;
	private int accessPathLength = 5;

	static{
		TaintSourceSinkManager.setSourceSink("SourcesAndSinks.txt", sources, sinks);
	}


	/**
	 * Runs the whole TaintAnalysis and stores the results inside a Map<Sink, Set<Source>> 
	 * @param args[0] = path to jar file, args[1] EntryPoint for the program
	 * @param keepOrigNames = true to keep origional names to true
	 */
	public SourceSinkResolver(String[] args, boolean keepOrigNames){
		if(setArgs(args)){
			this.args = args;
			infoflow = new Infoflow();
			if(keepOrigNames)
				infoflow.setSootConfig(new SootConfigForProgramRepair());
		}
	}	

	/**
	 * Runs the whole TaintAnalysis and stores the results inside a Map<Sink, Set<Source>> 
	 * @param args[0] = path to jar file, args[1] EntryPoint for the program
	 */
	public SourceSinkResolver(String[] args){
		if(setArgs(args)){
			this.args = args;
			infoflow = new Infoflow();
		}
	}

	/**
	 * Sets the arguments for the analysis
	 * @param args args[0] = path to jar file, args[1] = EntryPoint for the program
	 * @return true if arguments are OK
	 */
	public boolean setArgs(String[] args){
		if(args.length != 2) {
			ps.println("Invalid Arguments");
			return false;
		} else{
			this.args = args;
			return true;
		}
	}

	/**
	 * 
	 * @param accessPathLength the maximum value of an access path. Lesser the no. 
	 * better performance but imprecise analysis
	 * Default value is 5. Min = 1
	 */
	public void setAccessPathLength(int accessPathLength){
		this.accessPathLength = accessPathLength;
	}

	public boolean runAnalysis(){
		if(args == null){
			ps.println("args not set please use setArgs(String[] args) to set it");
			return false;
		}
		else {
			try{
				Infoflow.setAccessPathLength(accessPathLength);
				infoflow.setEnableImplicitFlows(true);
				infoflow.setTaintWrapper(new EasyTaintWrapper("EasyTaintWrapperSource.txt"));
				infoflow.computeInfoflow(args[0],
						null, args[1], new DefaultSourceSinkManager(sources, sinks));

				this.methodToChain = infoflow.getMethodToChain();
				this.results = infoflow.getResults();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		return true;
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
			if(null == u)
				return true;
			Stmt stmt = (Stmt) u;
			Map<SinkInfo, Set<SourceInfo>> res = results.getResults(); 
			for (SinkInfo si : res.keySet()){
				if(si.getContext().equals(stmt))
				{
					//System.out.println(stmt);
					return false;
				}

				Set<SourceInfo> sources = res.get(si);
				for (SourceInfo src : sources) {
					if(src.getContext().equals(stmt))
					{
						System.out.println(stmt);
						return false;
					}
					for(Stmt path : src.path){
						if(path.equals(stmt))
						{
							//System.out.println(stmt);
							return false;
						}
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
	public boolean isSafe(Unit u, SootMethod m, int i)
	{	
		if(null == methodToChain || !methodToChain.containsKey(m.getSignature()))
		{
			return true;
		}
		PatchingChain<Unit> pc = methodToChain.get(m.getSignature());
		int ct = 0;
		for(Unit uu : pc)
		{
			if(i == ct++){
				/*
				 if(u.equals(uu))
					u = uu;
				else {
					u = uu;
					ps.println("Unit was not found Assuming the given unit is the expected unit");
				}
				// */
				u = uu;
				break;
			}
		}
		return isSafe(u);
	}

	public static void printSourceSink(){
		ps.println("Sources\n" + SourceSinkResolver.sources);
		ps.println("Sinks\n" + SourceSinkResolver.sinks);
	}

	/**
	 * @return String representation of the Map Stored
	 */
	public String toStringMethodToChainMap(){
		return methodToChain.toString();
	}

}
