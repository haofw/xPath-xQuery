grammar XQuery;


xq    
                  : Var                                               # XqVariable 
                  | StringConstant                                    # XqStringConstant
                  | ap                                                # XqAp
                  | '(' xq ')'                                        # XqWithP
                  | xq ',' xq                                         # XqComma
                  | xq '/' rp                                         # XqChildRp
                  | xq '//' rp                                        # XqAll
                  | '<' TAGNAME '>' '{' xq '}' '<' '/' TAGNAME '>'    # XqTag
                  | forClause letClause? whereClause? returnClause    # XqFLWR
                  | letClause xq                                      # XqLet
                  ;

Var          
                  : '$' TAGNAME
                  ;

forClause    
                  : 'for' Var 'in' xq (',' Var 'in' xq)* 
                  ;
letClause    
                  : 'let' Var ':=' xq (',' Var ':=' xq)* 
                  ;
whereClause  
                  : 'where' cond
                  ;
returnClause 
                  : 'return' xq
                  ;

cond         
                  : xq '=' xq                                                  # CondEqual
                  | xq 'eq' xq                                                 # CondEqual
                  | xq '==' xq                                                 # CondIs
                  | xq 'is' xq                                                 # CondIs
                  | 'empty' '(' xq ')'                                         # CondEmpty
                  | 'some' Var 'in' xq (',' Var 'in' xq)* 'satisfies' cond     # CondSome
                  | '(' cond ')'                                               # CondWithP
                  | cond 'and' cond                                            # CondAnd
                  | cond 'or' cond                                             # CondOr
                  | 'not' cond                                                 # CondNot
                  ;

StringConstant:  '"'+[a-zA-Z0-9,.!?:; _'"-]+'"';


ap
                  : doc '/' rp                                                # ApChildren
                  | doc '//' rp                                               # ApAll
                  ;

doc
                  : 'doc("' filename '")'
                  ;

filename
                  : TAGNAME ('.' TAGNAME)?
                  ;

rp
                  : TAGNAME                      # RpTagName
                  | '*'                          # RpAllChildren
                  | '.'                          # RpCurrent
                  | '..'                         # RpParent
                  | TXT                          # Txt
                  | '@' TAGNAME                  # RpAttribute
                  | '(' rp ')'                   # RpWithP
                  | rp '/' rp                    # RpChildren
                  | rp '//' rp                   # RpAll
                  | rp '[' filter ']'            # RpFilter
                  | rp ',' rp                    # RpComma
                  ;


filter
                  : rp                           # FltRp
                  | rp '=' rp                    # FltEqual
                  | rp 'eq' rp                   # FltEqual
                  | rp '==' rp                   # FltIs
                  | rp 'is' rp                   # FltIs
                  | '(' filter ')'               # FltWithP
                  | filter 'and' filter          # FltAnd
                  | filter 'or' filter           # FltOr
                  | 'not' filter                 # FltNot
                  ;

TAGNAME: [a-zA-Z0-9_-]+;
TXT: 'text()';
WS : [ \t\r\n]+ -> skip;