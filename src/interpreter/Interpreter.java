package interpreter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import AST.*;
import expval.*;
import environment.Environment;

public class Interpreter implements TypeVisitor {
	Environment itpEnv;
	
	public Interpreter() {
		
		itpEnv = new Environment();
	}
	
	public Interpreter(Environment newEnv) {
		this.itpEnv = newEnv;
	}
	
	@Override
	public ExpType visit(ValueOf vo) {
		
		//System.out.println(env.toString());
		return vo.exp.visit(this);
	}

	@Override
	public ExpType visit(ConstExp exp) {
		return new NumVal(exp.number);
	}

	@Override
	public ExpType visit(LetExp exp) {
		String var = exp.var;
		
		Expression body = exp.body;
		
		//System.out.print("LetExp: " + var);
		
		ExpVal val = (ExpVal)exp.letexp.visit(this);
		
		if(val instanceof ProcVal) {
			//System.out.println("Re-extending Environment");
			Hashtable<String, ExpVal> procdef = new Hashtable<String, ExpVal>();
			procdef.put(var, val);
			((ProcVal) val).savedEnv.add(procdef);
			itpEnv.extendEnvRec(procdef);
			
		}
		//System.out.println(" = \n" + val.toString());
		
		itpEnv.extendEnv(var, val);
		
		
		ExpType letval = body.visit(this);
		
		//System.out.println(itpEnv.toString());
		itpEnv.leaveScope();
		
		return letval;

	}

	@Override
	public ExpType visit(DiffExp exp) {
		//System.out.println(env.toString());
		Integer left = ((NumVal) exp.left.visit(this)).number;
		Integer right = ((NumVal) exp.right.visit(this)).number;
		
		return new NumVal(left - right);
	}

	@Override
	public ExpType visit(AddExp exp) {
		Integer left = ((NumVal) exp.left.visit(this)).number;
		Integer right = ((NumVal) exp.right.visit(this)).number;
		
		return new NumVal(left + right);
	}

	@Override
	public ExpType visit(VarExp exp) {
		String var = exp.var;

		if(itpEnv.containsProc(var)) {
			ProcVal retrievedProc = (ProcVal)itpEnv.findProc(var);
			
			Hashtable<String, ExpVal> procValExt = new Hashtable<String, ExpVal>();
			procValExt.put(var, new ProcVal(retrievedProc.arg, retrievedProc.body, itpEnv));
			
			return new ProcVal(retrievedProc.arg, retrievedProc.body, itpEnv);
		}
		
		else if(itpEnv.containsVal(var)) {
			
			return itpEnv.findExpVal(var);
		
		}else {
			System.out.println("Returing null with var::" + var);
			return null;
		}
	}

	@Override
	public ExpType visit(IfExp exp) {
		//System.out.println("IfExp Eval("  + exp.toString() + " )");
		Expression ift = exp.ifthen;
		Expression ife = exp.ifelse;
		
		Boolean val = ((BoolVal) exp.arg.visit(this)).val;
		if(val == true) {
			return ift.visit(this);
		}
		else {
			return ife.visit(this);
		}
	}

	@Override
	public ExpType visit(ProcExp exp) {
		//System.out.println("!!----------PROC EXP----------!!");
		final Environment procEnv = new Environment();
		procEnv.addAll(itpEnv.SymbolTable);
		//System.out.println("ProcExpEnv====:::\n" + procEnv);
		return new ProcVal(exp.var, exp.body, procEnv);
		//return retProc;
	}

	@Override
	public ExpType visit(ProcVarExp exp) {
		ProcVal proc = (ProcVal)exp.procedure.visit(this);
		
		Environment holdEnv = new Environment();
		Environment procTempEnv = new Environment(); 
		
		holdEnv.addAll(itpEnv.SymbolTable);
		procTempEnv.addAll(proc.savedEnv);
		
		
		//System.out.println("Before::" + itpEnv.toString());
		
		itpEnv.SymbolTable = proc.savedEnv;
		//System.out.println("After::" + itpEnv.toString());
		//itpEnv.addAll(proc.savedEnv);
		//proc.savedEnv.clear();
		//proc.savedEnv.addAll(procTempEnv);
		
		Expression operand = exp.operand;

		String formalvar = proc.arg;

		ExpVal arg = (ExpVal)operand.visit(this);

		
		itpEnv.extendEnv(formalvar, arg);
	
		ExpType procval = proc.body.visit(this);
		
		
		itpEnv = holdEnv;
		
		return procval;
	}

	@Override
	public ExpType visit(LetRecExp exp) {
		//System.out.println("///====LetRecExp///=====");
		String procname = exp.fname;
		String boundvar = exp.arg;
		Expression procbody = exp.pbody;
		
		/*System.out.println("procname=" + procname 
						+" boundvar=" + boundvar 
						+ "\nprocbody=" + procbody);
		*/
		Environment holdEnv = new Environment();
		holdEnv.clear();
		holdEnv.addAll(itpEnv.SymbolTable);
		//itpEnv.clear();
		
		
		Environment letRecProcEnv = new Environment();
		//letRecProcEnv.clear();
		letRecProcEnv.addAll(itpEnv);
		
		Hashtable<String, ExpVal> rp = new Hashtable<String, ExpVal>();
		rp.put(procname, new ProcVal(boundvar, procbody, letRecProcEnv));
		itpEnv.extendEnvRec(rp);
		
		
		//itpEnv.clear();
		Expression letrecbody = exp.letrecbody;
		//itpEnv.clear();
		
		
		ExpType letrecval = (ExpVal)letrecbody.visit(this);
		//itpEnv.clear();
		itpEnv = holdEnv;
		holdEnv.clear();
		return letrecval;
	}

	@Override
	public ExpType visit(IsZeroExp exp) {
		Integer value = ((NumVal)exp.check.visit(this)).number;
		if(value == 0) {
			return new BoolVal(true);
		}else {
			return new BoolVal(false);
		}
	}
}
