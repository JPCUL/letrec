package interpreter;

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
		
		//System.out.println(" = \n" + val.toString());
		
		itpEnv.extendEnv(var, val);
		
		ExpType letval = body.visit(this);
		//System.out.println("value:::  " + letval.toString());
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
		//System.out.println("===========VAR===EXP=========");
		String var = exp.var;
		//System.out.println("VAR:" + var);
		//System.out.println(itpEnv.toString());
		if(itpEnv.containsVal(var)) {
			//System.out.println("Returing " +var+ " with val::" + itpEnv.findExpVal(var));
			//System.out.println(itpEnv);
			return itpEnv.findExpVal(var);
		
		}else {
			System.out.println("Returing null with var::" + var);
			return null;
		}
	}

	@Override
	public ExpType visit(IfExp exp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExpType visit(ProcExp exp) {
		
		return new ProcVal(exp.var, exp.body, itpEnv);
		//return retProc;
	}

	@Override
	public ExpType visit(ProcVarExp exp) {
		//System.out.println("==============VAR+++CALL+++EXP============");
		ProcVal proc = (ProcVal)exp.procedure.visit(this);
		//proc.freeVar();
		//System.out.println("ITPENV11:::" + itpEnv);
		List<Hashtable<String, ExpVal>>holdEnv = itpEnv.SymbolTable;

		List<Hashtable<String, ExpVal>> tempEnv  = new LinkedList<>();
		//System.out.println("TempEnv::" + tempEnv + "  " + proc.envLevel);
		//System.out.println("SETTING ENV TO PROC SCOPE :: " + proc.setScope());
		tempEnv = proc.setScope();
		//System.out.println("TempEnv::" + tempEnv + "  " + proc.envLevel);
		itpEnv.SymbolTable = tempEnv;
		//System.out.println("SET SYMBOL TABLE::" + itpEnv);
		Expression operand = exp.operand;

		String formalvar = proc.arg;
		
		ExpVal arg = (ExpVal)operand.visit(this);
		
		//System.out.println(arg);
		itpEnv.extendEnv(formalvar, arg);

		ExpType procval = proc.body.visit(this);
		
		//System.out.println("HOLDENV:::" + holdEnv);
		
		itpEnv.envClear();
		itpEnv.addAll(holdEnv);
		
		//.out.println("==============END--VAR--CAR--EXP============");
		return procval;
	}
}
