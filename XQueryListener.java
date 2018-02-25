// Generated from XQuery.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link XQueryParser}.
 */
public interface XQueryListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code ApChildren}
	 * labeled alternative in {@link XQueryParser#ap}.
	 * @param ctx the parse tree
	 */
	void enterApChildren(XQueryParser.ApChildrenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ApChildren}
	 * labeled alternative in {@link XQueryParser#ap}.
	 * @param ctx the parse tree
	 */
	void exitApChildren(XQueryParser.ApChildrenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ApAll}
	 * labeled alternative in {@link XQueryParser#ap}.
	 * @param ctx the parse tree
	 */
	void enterApAll(XQueryParser.ApAllContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ApAll}
	 * labeled alternative in {@link XQueryParser#ap}.
	 * @param ctx the parse tree
	 */
	void exitApAll(XQueryParser.ApAllContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#doc}.
	 * @param ctx the parse tree
	 */
	void enterDoc(XQueryParser.DocContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#doc}.
	 * @param ctx the parse tree
	 */
	void exitDoc(XQueryParser.DocContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#filename}.
	 * @param ctx the parse tree
	 */
	void enterFilename(XQueryParser.FilenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#filename}.
	 * @param ctx the parse tree
	 */
	void exitFilename(XQueryParser.FilenameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Txt}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterTxt(XQueryParser.TxtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Txt}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitTxt(XQueryParser.TxtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpParent}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpParent(XQueryParser.RpParentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpParent}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpParent(XQueryParser.RpParentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpAttribute}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpAttribute(XQueryParser.RpAttributeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpAttribute}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpAttribute(XQueryParser.RpAttributeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpAllChildren}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpAllChildren(XQueryParser.RpAllChildrenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpAllChildren}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpAllChildren(XQueryParser.RpAllChildrenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpAll}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpAll(XQueryParser.RpAllContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpAll}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpAll(XQueryParser.RpAllContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpWithP}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpWithP(XQueryParser.RpWithPContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpWithP}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpWithP(XQueryParser.RpWithPContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpComma}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpComma(XQueryParser.RpCommaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpComma}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpComma(XQueryParser.RpCommaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpTagName}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpTagName(XQueryParser.RpTagNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpTagName}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpTagName(XQueryParser.RpTagNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpChildren}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpChildren(XQueryParser.RpChildrenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpChildren}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpChildren(XQueryParser.RpChildrenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpCurrent}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpCurrent(XQueryParser.RpCurrentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpCurrent}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpCurrent(XQueryParser.RpCurrentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RpFilter}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRpFilter(XQueryParser.RpFilterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RpFilter}
	 * labeled alternative in {@link XQueryParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRpFilter(XQueryParser.RpFilterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FltEqual}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFltEqual(XQueryParser.FltEqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FltEqual}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFltEqual(XQueryParser.FltEqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FltRp}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFltRp(XQueryParser.FltRpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FltRp}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFltRp(XQueryParser.FltRpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FltAnd}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFltAnd(XQueryParser.FltAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FltAnd}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFltAnd(XQueryParser.FltAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FltIs}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFltIs(XQueryParser.FltIsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FltIs}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFltIs(XQueryParser.FltIsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FltWithP}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFltWithP(XQueryParser.FltWithPContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FltWithP}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFltWithP(XQueryParser.FltWithPContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FltOr}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFltOr(XQueryParser.FltOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FltOr}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFltOr(XQueryParser.FltOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FltNot}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFltNot(XQueryParser.FltNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FltNot}
	 * labeled alternative in {@link XQueryParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFltNot(XQueryParser.FltNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqComma}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqComma(XQueryParser.XqCommaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqComma}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqComma(XQueryParser.XqCommaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqAp}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqAp(XQueryParser.XqApContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqAp}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqAp(XQueryParser.XqApContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqWithP}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqWithP(XQueryParser.XqWithPContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqWithP}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqWithP(XQueryParser.XqWithPContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqAll}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqAll(XQueryParser.XqAllContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqAll}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqAll(XQueryParser.XqAllContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqVariable}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqVariable(XQueryParser.XqVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqVariable}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqVariable(XQueryParser.XqVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqStringConstant}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqStringConstant(XQueryParser.XqStringConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqStringConstant}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqStringConstant(XQueryParser.XqStringConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqFLWR}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqFLWR(XQueryParser.XqFLWRContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqFLWR}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqFLWR(XQueryParser.XqFLWRContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqTag}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqTag(XQueryParser.XqTagContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqTag}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqTag(XQueryParser.XqTagContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqLet}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqLet(XQueryParser.XqLetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqLet}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqLet(XQueryParser.XqLetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code XqChildRp}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void enterXqChildRp(XQueryParser.XqChildRpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code XqChildRp}
	 * labeled alternative in {@link XQueryParser#xq}.
	 * @param ctx the parse tree
	 */
	void exitXqChildRp(XQueryParser.XqChildRpContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#forClause}.
	 * @param ctx the parse tree
	 */
	void enterForClause(XQueryParser.ForClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#forClause}.
	 * @param ctx the parse tree
	 */
	void exitForClause(XQueryParser.ForClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#letClause}.
	 * @param ctx the parse tree
	 */
	void enterLetClause(XQueryParser.LetClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#letClause}.
	 * @param ctx the parse tree
	 */
	void exitLetClause(XQueryParser.LetClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(XQueryParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(XQueryParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQueryParser#returnClause}.
	 * @param ctx the parse tree
	 */
	void enterReturnClause(XQueryParser.ReturnClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQueryParser#returnClause}.
	 * @param ctx the parse tree
	 */
	void exitReturnClause(XQueryParser.ReturnClauseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CondWithP}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCondWithP(XQueryParser.CondWithPContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CondWithP}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCondWithP(XQueryParser.CondWithPContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CondOr}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCondOr(XQueryParser.CondOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CondOr}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCondOr(XQueryParser.CondOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CondAnd}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCondAnd(XQueryParser.CondAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CondAnd}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCondAnd(XQueryParser.CondAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CondEmpty}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCondEmpty(XQueryParser.CondEmptyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CondEmpty}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCondEmpty(XQueryParser.CondEmptyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CondSome}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCondSome(XQueryParser.CondSomeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CondSome}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCondSome(XQueryParser.CondSomeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CondIs}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCondIs(XQueryParser.CondIsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CondIs}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCondIs(XQueryParser.CondIsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CondNot}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCondNot(XQueryParser.CondNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CondNot}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCondNot(XQueryParser.CondNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CondEqual}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCondEqual(XQueryParser.CondEqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CondEqual}
	 * labeled alternative in {@link XQueryParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCondEqual(XQueryParser.CondEqualContext ctx);
}