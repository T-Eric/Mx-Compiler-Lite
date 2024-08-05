grammar Mx;

program: programPart* EOF;

programPart: (funcDef | (varDef Semi) | classDef);

funcDef:	type? Identifier LParen argList? RParen suite;
argList:	type Identifier (Comma type Identifier)*;

varDef:
	type varTerm (Comma varTerm)*; // Do we need comma at last?
varTerm:
	Identifier ((Assign expression) | (LParen exprList RParen))?;

classDef:
	Class Identifier LBrace ((varDef Semi) | funcDef)* RBrace Semi;

suite: LBrace statement* RBrace;

type: (Void | Bool | Int | String | Identifier (LParen RParen)?) arrayBlock*;

statement:
	suite # suiteStmt
	| If LParen expression RParen trueStmt = statement (
		Else falseStmt = statement
	)?											# ifStmt
	| While LParen expression RParen statement	# whileStmt
	| For LParen ((initExpr = expression) | varDef)? Semi (
		condExpr = expression
	)? Semi (stepExpr = expression)? RParen statement	# forStmt
	| Continue Semi										# continueStmt
	| Break Semi										# breakStmt
	| Return expression? Semi							# returnStmt
	| expression Semi									# singleExprStmt
	| varDef Semi										# varDefStmt
	| Semi												# emptyStmt;

expression:
	//term
	atom									# atomExpr
	| LParen expression RParen				# parenExpr
	| expression Dot Identifier				# memberExpr
	| expression arrayBlock+				# arrayExpr
	| expression LParen exprList? RParen	# funcExpr
	//new
	| <assoc = right> New type # newExpr
	//unary
	| expression op = (Inc | Dec)					# selfExpr
	| <assoc = right> op = (Inc | Dec) expression	# unaryExpr
	| <assoc = right> op = (Not | Tilde) expression	# unaryExpr
	| <assoc = right> op = (Add | Sub) expression	# unaryExpr
	// binary
	| left = expression op = (Add | Sub) right = expression						# binaryExpr
	| left = expression op = (Mul | Div | Mod) right = expression				# binaryExpr
	| left = expression op = (LShift | RShift) right = expression				# binaryExpr
	| left = expression op = (Ge | Geq | Le | Leq | Eq | Ne) right = expression	# binaryExpr
	| left = expression op = (And | Or | Xor) right = expression				# binaryExpr
	| left = expression op = (AndAnd | OrOr) right = expression					# binaryExpr
	// ternary
	| cond = expression Ques left = expression Colon right = expression # ternaryExpr
	// assign
	| <assoc = right> lvalue = expression Assign rvalue = expression # assignExpr
	// formatted string
	| 'f' Quot ('$' expression '$' | .)? Quot # formatStrExpr;

exprList:	expression (Comma expression)*;
arrayBlock:	LBrack expression? RBrack;

atom:
	This
	| Identifier
	| StringConst
	| DecimalInt
	| (True | False)
	| Null;

// keywords:

Void:		'void';
Bool:		'bool';
Int:		'int';
String:		'string';
New:		'new';
Class:		'class';
Null:		'null';
True:		'true';
False:		'false';
This:		'this';
If:			'if';
Else:		'else';
For:		'for';
While:		'while';
Break:		'break';
Continue:	'continue';
Return:		'return';

// symbols & operands

LParen:	'(';
RParen:	')';
LBrack:	'[';
RBrack:	']';
LBrace:	'{';
RBrace:	'}';
Dot:	'.';
Semi:	';';
Comma:	',';
Ques:	'?';
Colon:	':';
Quot:	'"';

Assign:	'=';
Add:	'+';
Sub:	'-';
Mul:	'*';
Div:	'/';
Mod:	'%';

Le:		'<';
Leq:	'<=';
Ge:		'>';
Geq:	'>=';
Ne:		'!=';
Eq:		'==';

Inc:	'++';
Dec:	'--';

And:	'&';
Or:		'|';
Xor:	'^';
Tilde:	'~'; // neg bitwise
LShift:	'<<';
RShift:	'>>';

AndAnd:	'&&';
OrOr:	'||';
Not:	'!';

// basics

Identifier: [a-zA-Z][a-zA-Z_0-9]*;

DecimalInt: [1-9][0-9]* | '0';

StringConst: '"' ('\\' . | .)*? '"';

Whitespace: [ \t+] -> skip;

Newline: ('\r' '\n'? | '\n') -> skip;

BlockComment: '/*' .*? '*/' -> skip;

LineComment: '//' ~[\r\n]* -> skip;