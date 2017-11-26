package interpreter;

import AST.*;


//Visitor design pattern
public interface TypeVisitor {
	ExpType visit(ConstExp exp);
	ExpType visit(LetExp exp);
	ExpType visit(ValueOf exp);
	ExpType visit(DiffExp exp);
	ExpType visit(AddExp exp);
	ExpType visit(VarExp exp);
	ExpType visit(IfExp exp);
	ExpType visit(ProcExp exp);
	ExpType visit(ProcVarExp exp);
	
}
