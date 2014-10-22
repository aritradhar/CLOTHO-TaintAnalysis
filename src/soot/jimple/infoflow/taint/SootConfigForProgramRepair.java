package soot.jimple.infoflow.taint;

import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.options.Options;

public class SootConfigForProgramRepair implements IInfoflowConfig{

	@Override
	public void setSootOptions(Options options) {
		// TODO Auto-generated method stub
		Options.v().setPhaseOption("jb", "use-original-names:true");
	}

}
