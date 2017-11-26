package environment;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import expval.ExpVal;
import expval.ProcVal;



public class Environment {
	public List<Hashtable<String, ExpVal>> SymbolTable; 
	
	public Environment() {
		SymbolTable = new LinkedList<Hashtable<String, ExpVal>>();
	}
	
	public boolean addAll(List<Hashtable<String,ExpVal>> in) {
		
		return SymbolTable.addAll(in);
	}
	
	public int retrieveSize() {
		return SymbolTable.size();
	}
	
	public List<Hashtable<String, ExpVal>> retrieveProcScope(int level) {
		List<Hashtable<String, ExpVal>> buildEnv = new LinkedList<Hashtable<String, ExpVal>>();
		for(int i = 0; i < level; i++) {
			//System.out.println("BUILDING PROC ENV::" + i + " ::" + SymbolTable.get(i));
			buildEnv.add(SymbolTable.get(i));
		}
		return buildEnv;
	}
	
	public String getKeys(int level) {
		Set<String> keys = SymbolTable.get(level).keySet();
		String keykey = null;
		for(String key: keys) {
			keykey = key;
		}
		return keykey;
	}
	
	public boolean containsVar(String var) {
		int lvl = SymbolTable.size();
		for(int i= lvl-1; i >=0; i--) {
			if(SymbolTable.get(i).contains(var)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsVal(String var) {
		int lvl = SymbolTable.size();
		for(int i = lvl-1; i >= 0; i--) {
			if(SymbolTable.get(i).get(var)!=null) {
				return true;
			}
		}
		return false;
	}
	
	public void extendEnv(String var, ExpVal val) {
		final Hashtable<String, ExpVal> insertion = new Hashtable<>();
		insertion.put(var, val);
		SymbolTable.add(insertion);
	}
	
	public ExpVal findExpVal(String var) {
		int lvl = SymbolTable.size();
		for(int i = lvl-1; i >= 0; i--) {
			if(SymbolTable.get(i).containsKey(var)) {
				return SymbolTable.get(i).get(var);
			}
		}
		System.out.println("Couldn't find Value, returning null");
		return null;
	}
	
	public void leaveScope() {
		//System.out.println("Popping Scope from level::" + SymbolTable.size());
		SymbolTable.remove(SymbolTable.size()-1);
	}
	
	public void envClear() {
		SymbolTable.clear();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int lvl = SymbolTable.size();
		sb.append("Env :: ");
		for(int i=0; i<lvl; i++) {
			sb.append("\n"+"ScopeLevel::" + i + "  " + SymbolTable.get(i));
		}
		sb.append("\nEnd Current Env\n");
		return sb.toString();
	}
}
