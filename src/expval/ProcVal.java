package expval;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import AST.Expression;
import environment.Environment;


public class ProcVal extends ExpVal{
	public String arg;
	public Expression body;
	public Environment savedEnv;
	final public int envLevel;
	private String freekey = new String();
	
	public ProcVal(String args, Expression body, Environment savedEnv) {
		this.arg = args;
		this.body = body;
		this.savedEnv = savedEnv;
		envLevel = savedEnv.retrieveSize();
		//System.out.println("ProcEnv Scope:: "+envLevel);
		//System.out.println(savedEnv.toString());
	}
	
	public void freeVar() {
		for(int i = 0; i < envLevel; i++) {
			//System.out.println("arg::" + arg);
			if(!savedEnv.containsVal(arg)) {
				freekey = savedEnv.getKeys(envLevel);
				//System.out.println(freekey);
			}
		}
	}
	
	public List<Hashtable<String, ExpVal>> setScope(){
		return savedEnv.retrieveProcScope(envLevel);
	}
/*	
	public ExpVal bindFree() {
		List<Hashtable<String, ExpVal>> current = savedEnv.retrieveProcScope(envLevel);
		return current.get(freekey);
	}*/
	
	public String toString() {
		StringBuilder procstring = new StringBuilder();
		procstring.append(arg.toString());
		procstring.append(" : ");
		procstring.append(body.toString());
		procstring.append("\n");
		
		//System.out.println(savedEnv.toString());
		return procstring.toString();
	}
}