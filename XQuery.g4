/* XQuery
 *
 *
 */

grammar XQuery;
import XPath;


xq
	: var														# Variable
	| StringConstant											# StringC
	| ap														# XqAp
	| '(' xq ')'												# XqwithP
	| xq ',' xq 											    # TwoXq
	| xq '/' rp													# XqRp
	| xq '//' rp 												# XqRpall
	| '<' NAME '>' '{' xq '}' '<' '/' NAME '>'					# XqConstructor
	| forClause letClause? whereClause? returnClause    		# FLWR
	| letClause xq 												# XqLet
	| joinClause												# XQJoin
	;

var
	: '$' NAME
	;

joinClause
	: 'join' '(' xq ',' xq ',' idList ',' idList ')';

forClause
	: 'for' var 'in' xq (',' var 'in' xq)*
	;

letClause
	: 'let' var ':=' xq (',' var ':=' xq)*
	;

whereClause
	: 'where' cond 
	;

returnClause
	: 'return' xq
	;

cond
	: xq '=' xq 											 # XqEqual
	| xq 'eq' xq 											 # XqEqual
	| xq '==' xq 											 # XqIs
	| xq 'is' xq 											 # XqIs
	| 'empty' '(' xq ')' 		 							 # XqEmpty
	| 'some' var 'in' xq (',' var 'in' xq)* 'satisfies' cond # XqSome
	| '(' cond ')' 											 # XqCondwithP
	| cond 'and' cond 										 # XqCondAnd
	| cond 'or' cond 										 # XqCondOr
	| 'not' cond 											 # XqCondNot
	;

idList: '[' NAME (',' NAME)* ']' ;
StringConstant: '"'+[a-zA-Z0-9,.!?; ''""-]+'"';
