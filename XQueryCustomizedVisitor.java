import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XQueryCustomizedVisitor extends XQueryBaseVisitor<ArrayList<Node>> {
    private boolean hasAttribute = false;
    private Document inputDocument = null;
    Document outputDocument = null;
    private HashMap<String, ArrayList<Node>> contextMap = new HashMap<>();
    private Stack<HashMap<String, ArrayList<Node>>> contextStack = new Stack<>();
    private ArrayList<Node> curr = new ArrayList<>();
    boolean DoReWrite = true;

    @Override
    public ArrayList<Node> visitXqAp(XQueryParser.XqApContext context) {
        return visit(context.ap());
    }

    @Override
    public ArrayList<Node> visitXqConstructor(XQueryParser.XqConstructorContext context) {
        if (outputDocument == null){
            try {
                DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
                DocumentBuilder docB = docBF.newDocumentBuilder();
                outputDocument = docB.newDocument();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
        ArrayList<Node> result = new ArrayList<>();

        ArrayList<Node> xqRes = visit(context.xq());

        // System.out.println("xqRes size: " + xqRes.size());
        // for (Node n : xqRes) {
        //     System.out.println(n.getNodeName());
        // }

        result.add(createNode(context.NAME(0).getText(), xqRes));

        // System.out.println("result size: " + result.size());
//         for (Node n : result) {
//             System.out.println("result node name: " + n.getNodeName());
//             System.out.println("result node children size: " + n.getChildNodes().getLength());
//             System.out.println("First node name: " + n.getFirstChild().getNodeName());
//             System.out.println("Last node name: " + n.getLastChild().getNodeName());
//         }
        return result;
    }

    private void permute(XQueryParser.FLWRContext context, int k, ArrayList<Node> result){
        if (k == context.forClause().var().size()){
            HashMap<String, ArrayList<Node>> contextMapOld = new HashMap<>(contextMap);
            if (context.letClause() != null) {
                visit(context.letClause());
            }
            if (context.whereClause() != null) {
                if(visit(context.whereClause()).size()==0){
                    return;
                }
            }
            ArrayList<Node> c = visit(context.returnClause());
            //System.out.println("c: " + c.size());
            if (c != null) {
                result.addAll(visit(context.returnClause()));
            }
            contextMap = contextMapOld;
            // System.out.println("Permute:" + result.size());
            // for (Node n: result) {
            //     System.out.println("permuteNode: " + n.getNodeName());
            // }
        }
        else {
            String var = context.forClause().var(k).getText();
            // System.out.println("forClause.getText(): " + var);
            ArrayList<Node> varNodes = visit(context.forClause().xq(k));
            // System.out.println("varNode: " + varNodes.size());
            for (Node temp : varNodes){
                // System.out.println("temp node name:" + temp.getTextContent());
                contextMap.remove(var);
                ArrayList<Node> nList = new ArrayList<>();
                nList.add(temp);
                contextMap.put(var, nList);
                permute(context, k + 1, result);
            }

        }
    }

    @Override
    public ArrayList<Node> visitFLWR(XQueryParser.FLWRContext context) {
        ArrayList<Node> result = new ArrayList<>();
        HashMap<String, ArrayList<Node>> contextMapOld = new HashMap<>(contextMap);
        contextStack.push(contextMapOld);
        if(! DoReWrite)
            permute(context, 0, result);
        else{
            String re = rewrite_Xq(context);
            if (re  == ""){
                permute(context, 0, result);
            }
            else
                result = XQuery.evalRewrited(re);
        }
        contextMap = contextStack.pop();
        return result;
    }

    private String rewrite_Xq(XQueryParser.FLWRContext ctx){
        //PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
        String output = "";
        int element_for = ctx.forClause().var().size();
        Map<Integer, Set<String>> division = new HashMap<>();
        //List<HashSet<String>> classify = new ArrayList<HashSet<String>>();

        //get the division of different join
        int numOfD = 0;
        for(int i=0; i < element_for;i++) {
            String key = ctx.forClause().var(i).getText();
            String parent = ctx.forClause().xq(i).getText().split("/")[0];
            int size = division.size();
            boolean inSet = false;
            // construct the classification
            for(int j = 0; j < size; j++) {
                Set<String> thisSet = division.get(j);
                if(thisSet.contains(parent)) {
                    thisSet.add(key);
                    inSet = true;
                    break;
                }
            }
            if(!inSet) {
                Set<String> set = new HashSet<String>();
                set.add(key);
                //classify.add(set);
                division.put(numOfD ++,set);
            }
        }
        //where clause
        String[] where = ctx.whereClause().cond().getText().split("and");
        // each condition:

        String[][] cond = new String[where.length][2];
        for(int i = 0; i < where.length;i++) {
            cond[i][0] = where[i].split("eq|=")[0];
            cond[i][1] = where[i].split("eq|=")[1];
        }

        if(division.size() == 1) {
            System.out.println("can't join");
            return "";
        }
        int[][] relaWhere = new int[cond.length][2];

        for(int i=0; i < cond.length; i++) {
            String cur0 = cond[i][0];
            String cur1 = cond[i][1];
            relaWhere[i][0] = -1;
            relaWhere[i][1] = -1;
            for(int j = 0; j < division.size();j++) {
                if(division.get(j).contains(cur0)) {
                    relaWhere[i][0] = j;
                }
                if(division.get(j).contains(cur1)) {
                    relaWhere[i][1] = j;
                }
            }
        }


        int size = division.size();
        //print out
        output += "for $tuple in";
        System.out.print("for $tuple in");
        for (int i = 1; i < size; i++) {
                output += " join (";
                System.out.print(" join (");
        }
        output = printJoin(division, ctx, output,cond,relaWhere);

        String retClause = ctx.returnClause().xq().getText();
        String[] tempRet = retClause.split("\\$");
        for (int i = 0; i < tempRet.length - 1; i++) {
            tempRet[i] = tempRet[i]+"$tuple/";
        }
        retClause  = tempRet[0];
        for (int i = 1; i < tempRet.length; i++) {
            String[] cur1 = tempRet[i].split(",",2);
            String[] cur2 = tempRet[i].split("}",2);
            String[] cur3 = tempRet[i].split("/",2);
            String[] cur = cur1;
            if(cur2[0].length() < cur[0].length()) {
                cur = cur2;
            }
            if(cur3[0].length() < cur[0].length()) {
                cur = cur3;
            }
            tempRet[i] = cur[0] + "/*";
            if(cur == cur1) {
                tempRet[i] += ",";
            }else if(cur == cur2) {
                tempRet[i] += "}";
            }else {
                tempRet[i] += "/";
            }
            tempRet[i] += cur[1];
            retClause = retClause + tempRet[i];
        }

        output += "return\n";
        output += retClause+"\n";
        System.out.println("return");
        System.out.println(retClause);
        writer w = new writer();
        w.writing("rewrite.txt",output);
        return output;
    }
    private String printJoin(Map<Integer, Set<String>> division, XQueryParser.FLWRContext ctx, String output,String[][] cond,int[][] relaWhere) 
    {
        int element_for = ctx.forClause().var().size();
        for(int p = 0; p < division.size(); p++) {
            Set<String> curSet = division.get(p);
            String tuples = "";
            int count = 0;
            //print for
            for(int k = 0; k < element_for; k++) {
                String key = ctx.forClause().var(k).getText();
                if(curSet.contains(key)){
                    if(count == 0) {
                        output += "for " + key + " in " + ctx.forClause().xq(k).getText();
                        System.out.print("for " + key + " in " + ctx.forClause().xq(k).getText());
                        count++;
                    }else {
                        output += ",\n";
                        output += "                   " + key + " in " + ctx.forClause().xq(k).getText();
                        System.out.println(",");
                        System.out.print("                   " + key + " in " + ctx.forClause().xq(k).getText());
                    }
                    if(tuples.equals("")) {
                        tuples = tuples + " <" + key.substring(1) + "> " + " {" + key + "} " + " </" + key.substring(1) + ">";
                    }else {
                        tuples = tuples + ", <" + key.substring(1) + "> " + " {" + key + "} " + " </" + key.substring(1) + ">";
                    }
                }
            }
            output += "\n";
            System.out.print("\n");

            //print where
            for(int j = 0;j < cond.length;j++) {
                int count1 = 0;
                if(curSet.contains(cond[j][0]) && relaWhere[j][1] == -1 ) {
                    if(count1 == 0){
                        count1++;
                        output += "where " + cond[j][0] + " eq " + cond[j][1] +"\n";
                        System.out.println("where " + cond[j][0] + " eq " + cond[j][1]);
                    }else {
                        output += " and  " + cond[j][0] + " eq " + cond[j][1] + "\n";
                        System.out.println(" and  " + cond[j][0] + " eq " + cond[j][1]);
                    }
                }
            }
            //print return
            tuples = "<tuple> "+tuples+" </tuple>,";
            output += "                  return" + tuples + "\n";
            System.out.println("                  return" + tuples);

            if(p == 0)
                continue;

            //return
            ArrayList<String> left = new ArrayList<String>();
            ArrayList<String> right = new ArrayList<String>();
            for(int i = 0; i < cond.length; i++) 
            {
                if (relaWhere[i][1] != -1 &&  relaWhere[i][0] == p && relaWhere[i][0] > relaWhere[i][1]) 
                {
                    left.add(cond[i][1].substring(1));
                    right.add(cond[i][0].substring(1));
                }
                else if(relaWhere[i][0] != -1 &&  relaWhere[i][1] == p && relaWhere[i][0]<relaWhere[i][1]) 
                {
                    left.add(cond[i][0].substring(1));
                    right.add(cond[i][1].substring(1));
                }
            }
            //output = PrintJoinCond(ret0,ret1,output);
            output += "                 [";
            System.out.print("                 [");
            for(int i = 0; i < left.size();i++) {
                output +=left.get(i);
                System.out.print(left.get(i));
                if(i != left.size()-1) {
                    output +=",";
                    System.out.print(",");
                }
            }
            output +="], [";
            System.out.print("], [");
            for(int i = 0; i < right.size();i++) {
                output +=right.get(i);
                System.out.print(right.get(i));
                if(i != right.size()-1) {
                    output +=",";
                    System.out.print(",");
                }
            }
            output += "]  ";
            System.out.print("]  ");

            output += ")\n";
            System.out.println(")");
        }
        return output;
    }

    // private String PrintJoinCond(ArrayList<String> ret0, ArrayList<String> ret1, String output) {
    //     output += "                 [";
    //     System.out.print("                 [");
    //     for(int i = 0; i < ret0.size();i++) {
    //         output +=ret0.get(i);
    //         System.out.print(ret0.get(i));
    //         if(i != ret0.size()-1) {
    //             output +=",";
    //             System.out.print(",");
    //         }
    //     }
    //     output +="], [";
    //     System.out.print("], [");
    //     for(int i = 0; i < ret1.size();i++) {
    //         output +=ret1.get(i);
    //         System.out.print(ret1.get(i));
    //         if(i != ret1.size()-1) {
    //             output +=",";
    //             System.out.print(",");
    //         }
    //     }
    //     output += "]  ";
    //     System.out.print("]  ");
    //     return output;
    // }

 


    @Override
    public ArrayList<Node> visitTwoXq(XQueryParser.TwoXqContext context) {
        ArrayList<Node> result = visit(context.xq(0));
        result.addAll(visit(context.xq(1)));
        return result;
    }

    @Override
    public ArrayList<Node> visitVariable(XQueryParser.VariableContext context) {
        // System.out.println("context.getText(): " + context.getText());
        return contextMap.get(context.getText());
    }

    @Override
    public ArrayList<Node> visitXqRpall(XQueryParser.XqRpallContext context) {
        ArrayList<Node> result = new ArrayList<>();
        LinkedList<Node> ll = new LinkedList<>();
        ArrayList<Node> temp = visit(context.xq());
        result.addAll(temp);
        ll.addAll(temp);
        while(!ll.isEmpty()){
            Node tempNode = ll.poll();
            result.addAll(children(tempNode));
            ll.addAll(children(tempNode));
        }
        curr = result;
        return visit(context.rp());
    }

    @Override
    public ArrayList<Node> visitXqwithP(XQueryParser.XqwithPContext context) {
        return visit(context.xq());
    }

    @Override
    public ArrayList<Node> visitStringC(XQueryParser.StringCContext context) {
        Node temp = createText(context.StringConstant().getText().substring(1, context.StringConstant().getText().length()-1));
        ArrayList<Node> result = new ArrayList<>();
        result.add(temp);
        return result;
    }

    @Override
    public ArrayList<Node> visitXqLet(XQueryParser.XqLetContext context) {
        HashMap<String, ArrayList<Node>> contextMapOld = new HashMap<>(contextMap);
        contextStack.push(contextMapOld);
        ArrayList<Node> result = visitChildren(context);
        contextMap = contextStack.pop();
        return result;
    }

    @Override
    public ArrayList<Node> visitXqRp(XQueryParser.XqRpContext context) {
        curr = visit(context.xq());
        return visit(context.rp());
    }

    private ArrayList<Node> getItems (int v, XQueryParser.ForClauseContext context) {
        ArrayList<Node> result = new ArrayList<>();
        ArrayList<Node> tempList = visit(context.xq(v));
        if(context.xq().size() == 1) {
            for(Node temp: tempList) {
                ArrayList<Node> tempList2 = new ArrayList<>();
                tempList2.add(temp);
                contextMap.put(context.var(v).getText(), tempList2);
                result.add(temp);
            } 
            return result;
        }
        else {
            for(Node temp: tempList) {
                HashMap<String, ArrayList<Node>> contextMapOld = new HashMap<>(contextMap);
                contextStack.push(contextMapOld);
                ArrayList<Node> tempList2 = new ArrayList<>();
                tempList2.add(temp);
                contextMap.put(context.var(v).getText(), tempList2);
                result.addAll(getItems(v + 1, context));
                contextMap = contextStack.pop();
            }
            return result;
        }
    }

    @Override 
    public ArrayList<Node> visitJoinClause(XQueryParser.JoinClauseContext ctx){
        ArrayList<Node> left = visit(ctx.xq(0));
        ArrayList<Node> right = visit(ctx.xq(1));
        int idSize = ctx.idList(0).NAME().size();
        String [] idListLeft = new String [idSize];
        String [] idListRight = new String [idSize];
        for (int i = 0; i < idSize; i++){
            idListLeft[i] = ctx.idList(0).NAME(i).getText();
            idListRight[i] = ctx.idList(1).NAME(i).getText();
        }
        HashMap<String, ArrayList<Node>> hashMapOnLeft = buildHashTable(left, idListLeft);
        ArrayList<Node> result = probeJoin(hashMapOnLeft, right, idListLeft, idListRight);

        return result;

    }

    private ArrayList<Node> probeJoin(HashMap<String, ArrayList<Node>> hashMapOnLeft, ArrayList<Node> right, String [] idListLeft, String []idListRight){
        ArrayList<Node> result = new ArrayList<>();
        for (Node tuple: right){
            ArrayList<Node> children = children(tuple);
            String key = "";
            for (String hashAtt: idListRight) {
                for (Node child: children){
                    if (hashAtt.equals(child.getNodeName())) {
                        key += child.getFirstChild().getTextContent();
                    }
                }
            }

            if (hashMapOnLeft.containsKey(key))
                result.addAll(product(hashMapOnLeft.get(key),tuple));
        }
        return result;
    }
    private ArrayList<Node> product(ArrayList<Node> leftList, Node right){
        ArrayList<Node> result = new ArrayList<>();
        for (Node left: leftList){
            ArrayList<Node> newTupleChildren = children(left);
            newTupleChildren.addAll(children(right));
            result.add(createNode("tuple", newTupleChildren));
        }
        return result;
    }


    private HashMap buildHashTable(ArrayList<Node> tupleList, String [] hashAtts){
        HashMap<String, ArrayList<Node>> result = new HashMap<>();
        for (Node tuple: tupleList){
            ArrayList<Node> children = children(tuple);
            String key = "";
            for (String hashAtt: hashAtts) {
                for (Node child: children){
                    if (hashAtt.equals(child.getNodeName()))
                        key += child.getFirstChild().getTextContent();
                }
            }
            if (result.containsKey(key))
                result.get(key).add(tuple);
            else{
                ArrayList<Node> value = new ArrayList<>();
                value.add(tuple);
                result.put(key, value);
            }
        }
        return result;
    }



    @Override 
    public ArrayList<Node> visitXQJoin(XQueryParser.XQJoinContext context){
        return visit(context.joinClause());
    }

    @Override 
    public ArrayList<Node> visitForClause(XQueryParser.ForClauseContext context) { 
        ArrayList<Node> result = new ArrayList<>();
        result.addAll(getItems(0, context));
        return result;
    }
    
    @Override
    public ArrayList<Node> visitLetClause(XQueryParser.LetClauseContext context) {
        for (int i = 0; i < context.var().size(); i++) {
            contextMap.put(context.var(i).getText(), visit(context.xq(i)));
        }
        return null;
    }

    @Override
    public ArrayList<Node> visitWhereClause(XQueryParser.WhereClauseContext context) {
        return visit(context.cond());
    }

    @Override 
    public ArrayList<Node> visitReturnClause(XQueryParser.ReturnClauseContext context) { 
        return visit(context.xq());
    }
   
    @Override
    public ArrayList<Node> visitXqEqual(XQueryParser.XqEqualContext context) {
        ArrayList<Node> tempList = curr;
        ArrayList<Node> left = visit(context.xq(0));
        curr = tempList;
        ArrayList<Node> right = visit(context.xq(1));
        curr = tempList;
        ArrayList<Node> result = new ArrayList<>();
        for (Node i : left) {
            for (Node j : right) {
                if (i.isEqualNode(j)) {
                    result.add(i);
                    return result;
                }
            }
        }
        return result;
    }

    @Override
    public ArrayList<Node> visitXqEmpty(XQueryParser.XqEmptyContext context) {
        ArrayList<Node> xqResult = visit(context.xq());
        ArrayList<Node> result = new ArrayList<>();
        if (xqResult.isEmpty()){
            Node dummy = inputDocument.createElement("dummy");
            result.add(dummy);
        }
        return result;
    }

    @Override
    public ArrayList<Node> visitXqCondOr(XQueryParser.XqCondOrContext context) {
        ArrayList<Node> left = visit(context.cond(0));
        if (!left.isEmpty()){
            return left;
        }
        ArrayList<Node> right = visit(context.cond(1));
        if (!right.isEmpty()){
            return right;
        }
        return new ArrayList<>();
    }

    // @Override
    // public ArrayList<Node> visitXqSome(XQueryParser.XqSomeContext context) {
    //     for (int i = 0; i < context.var().size(); i++) {
    //         contextMap.put(context.var(i).getText(), visit(context.xq(i)));
    //     }
    //     return visit(context.cond());
    // }
    @Override
    public ArrayList<Node> visitXqSome(XQueryParser.XqSomeContext ctx) {
        HashMap<String, ArrayList<Node>> copyContext = new HashMap<>(contextMap);
        ArrayList<Node> nodes = curr;
        int numVars = ctx.var().size();
        for (int i = 0; i < numVars; ++i) {
            String varName = ctx.var(i).getText();
            ArrayList<Node> xquery = visit(ctx.xq(i));
            contextMap.put(varName, xquery);
        }
        ArrayList<Node> cond = visit(ctx.cond());
        contextMap = copyContext;
        curr = nodes;
        return cond;
    }


    @Override
    public ArrayList<Node> visitXqIs(XQueryParser.XqIsContext context) {
        ArrayList<Node> tempList = curr;
        ArrayList<Node> left = visit(context.xq(0));
        curr = tempList;
        ArrayList<Node> right = visit(context.xq(1));
        curr = tempList;
        ArrayList<Node> result = new ArrayList<>();
        for (Node i : left) {
            for (Node j : right) {
                if (i == j) {
                    result.add(i);
                    return result;
                }
            }
        }
        return result;
    }

    @Override
    public ArrayList<Node> visitXqCondNot(XQueryParser.XqCondNotContext context) {
        ArrayList<Node> notList = visit(context.cond());
        ArrayList<Node> result = new ArrayList<>();
        if (notList.isEmpty()){
            Node dummy = inputDocument.createElement("dummy");
            result.add(dummy);
        }
        return result;
    }

    @Override
    public ArrayList<Node> visitXqCondwithP(XQueryParser.XqCondwithPContext context) {
        return visit(context.cond());
    }

    @Override
    public ArrayList<Node> visitXqCondAnd(XQueryParser.XqCondAndContext context) {
        ArrayList<Node> left = visit(context.cond(0));
        ArrayList<Node> right = visit(context.cond(1));
        if (!left.isEmpty() && !right.isEmpty()){
            return left;
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Node> visitApChildren(XQueryParser.ApChildrenContext context) {
        return visitChildren(context);
    }

    @Override
    public ArrayList<Node> visitApAll(XQueryParser.ApAllContext context) {
        ArrayList<Node> result = new ArrayList<>();
        LinkedList<Node> ll = new LinkedList<>();
        visit(context.doc());
        result.addAll(curr);
        ll.addAll(curr);
        while(!ll.isEmpty()) {
            Node temp = ll.poll();
            result.addAll(children(temp));
            ll.addAll(children(temp));
        }
        curr = result;
        return visit(context.rp());
    }

    @Override
    public ArrayList<Node> visitDoc(XQueryParser.DocContext context) {
        if(inputDocument == null) {
            File xmlFile = new File(context.fname().getText());
            DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
            docBF.setIgnoringElementContentWhitespace(true);
            DocumentBuilder docB = null;
            try {
                docB = docBF.newDocumentBuilder();
            } catch (ParserConfigurationException pE1) {
                pE1.printStackTrace();
            }

            try {
                if (docB != null) {
                    inputDocument = docB.parse(xmlFile);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            inputDocument.getDocumentElement().normalize();
        }
        ArrayList<Node> result = new ArrayList<>();
        result.add(inputDocument);
        curr = result;
        return result;
    }

    @Override
    public ArrayList<Node> visitAllChildren(XQueryParser.AllChildrenContext context) {
        ArrayList<Node> result = new ArrayList<>();
        for(Node temp : curr) {
            result.addAll(children(temp));
        }
        curr = result;
        return result;
    }

    @Override
    public ArrayList<Node> visitRpwithP(XQueryParser.RpwithPContext context) {
        return visit(context.rp());
    }

    @Override
    public ArrayList<Node> visitTagName(XQueryParser.TagNameContext context) {
        ArrayList<Node> result = new ArrayList<>();
        String tName = context.getText();
        for(Node temp : curr) {
            ArrayList<Node> list = children(temp);
            for(Node i : list) {
                if(i.getNodeName().equals(tName)) result.add(i);
            }
        }
        curr = result;
        return result;
    }

    @Override
    public ArrayList<Node> visitRpAll(XQueryParser.RpAllContext context) {
        ArrayList<Node> result = new ArrayList<>();
        LinkedList<Node> ll = new LinkedList<>();
        visit(context.rp(0));
        result.addAll(curr);
        ll.addAll(curr);
        while(!ll.isEmpty()) {
            Node temp = ll.poll();
            result.addAll(children(temp));
            ll.addAll(children(temp));
        }
        curr = result;
        return visit(context.rp(1));
    }

    @Override
    public ArrayList<Node> visitParent(XQueryParser.ParentContext context) {
        ArrayList<Node> result = new ArrayList<>();
        for(Node temp : curr) {
            if(!result.contains(temp.getParentNode())) result.add(temp.getParentNode());
        }
        curr = result;
        return result;
    }

    @Override
    public ArrayList<Node> visitAttribute(XQueryParser.AttributeContext context) {
        ArrayList<Node> result = new ArrayList<>();
        hasAttribute = true;
        for (Node temp : curr) {
            Element e = (Element) temp;
            String attr = e.getAttribute(context.NAME().getText());
            if (!attr.equals("")) {
                result.add(temp);
                attr = context.NAME().getText()+"=\""+ attr +"\"";
            }
        }
        curr = result;
        return result;
    }

    @Override
    public ArrayList<Node> visitRpChildren(XQueryParser.RpChildrenContext context) {
        visit(context.rp(0));
        ArrayList<Node> result = visit(context.rp(1));
        curr = result;
        return result;
    }

    @Override
    public ArrayList<Node> visitText(XQueryParser.TextContext context) {
        ArrayList<Node> result = new ArrayList<>();
        for (Node n : curr) {
            ArrayList<Node> list = children(n);
            for (Node child : list) {
                if (child.getNodeType() == Node.TEXT_NODE){
                    result.add(child);
                }
            }
        }
        curr = result;
        return result;
    }

    @Override
    public ArrayList<Node> visitCurrent(XQueryParser.CurrentContext context) {
        return curr;
    }

    @Override
    public ArrayList<Node> visitTwoRp(XQueryParser.TwoRpContext context) {
        ArrayList<Node> result1 = new ArrayList<>();
        ArrayList<Node> result2 = new ArrayList<>();
        ArrayList<Node> tempList = new ArrayList<>(curr);
        result1.addAll(visit(context.rp(0)));
        curr = tempList;
        result2.addAll(visit(context.rp(1)));
        result1.addAll(result2);

        curr = result1;
        return result1;
    }

    @Override
    public ArrayList<Node> visitRpFilter(XQueryParser.RpFilterContext context) {
        ArrayList<Node> result = visit(context.rp());
        ArrayList<Node> filter= visit(context.filter());
        if (hasAttribute) {
            curr = filter;
            hasAttribute = false;
            return filter;
        }
        else if (filter.isEmpty()) {
            return new ArrayList<>();
        }
        else return result;
    }

    @Override
    public ArrayList<Node> visitFilAnd(XQueryParser.FilAndContext context) {
        ArrayList<Node> left = visit(context.filter(0));
        ArrayList<Node> right = visit(context.filter(1));
        if (!left.isEmpty() && !right.isEmpty()) {
            return left;
        }
        else return new ArrayList<>();
    }

    @Override
    public ArrayList<Node> visitFilEqual(XQueryParser.FilEqualContext context) {
        ArrayList<Node> tempList = curr;
        ArrayList<Node> left = visit(context.rp(0));
        curr = tempList;
        ArrayList<Node> right = visit(context.rp(1));
        curr = tempList;
        for (Node i : left) {
            for (Node j : right) {
                if (i.isEqualNode(j)) {
                    return tempList;
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Node> visitFilNot(XQueryParser.FilNotContext context) {
        ArrayList<Node> result = visit(context.filter());
        if (!result.isEmpty()) {
            return curr;
        }
        else return new ArrayList<>();
    }

    @Override
    public ArrayList<Node> visitFilOr(XQueryParser.FilOrContext context) {
        ArrayList<Node> left = visit(context.filter(0));
        ArrayList<Node> right = visit(context.filter(1));
        if (!left.isEmpty() || !right.isEmpty()) {
            return curr;
        }
        else return new ArrayList<>();
    }

    @Override
    public ArrayList<Node> visitFilIs(XQueryParser.FilIsContext context) {
        ArrayList<Node> tempList = curr;
        ArrayList<Node> left = visit(context.rp(0));
        curr = tempList;
        ArrayList<Node> right = visit(context.rp(1));
        curr = tempList;
        for (Node i : left) {
            for (Node j : right) {
                if (i == j) {
                    return tempList;
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Node> visitFilwithP(XQueryParser.FilwithPContext context) {
        return visit(context.filter());
    }

    @Override
    public ArrayList<Node> visitFilRp(XQueryParser.FilRpContext context) {
        ArrayList<Node> tempList = curr;
        ArrayList<Node> result = visit(context.rp());
        curr = tempList;
        return result;
    }

    private Node createNode(String s, ArrayList<Node> nodeList) {
        Node result = outputDocument.createElement(s);
        for (Node temp : nodeList) {
            if (temp != null) {
                Node newNode = outputDocument.importNode(temp, true);
                result.appendChild(newNode);
            }
        }
        return result;
    }

    private Node createText(String s) {
        return inputDocument.createTextNode(s);
    }

    private static ArrayList<Node> children(Node n){
        ArrayList<Node> childrenList = new ArrayList<>();
        for(int i = 0; i < n.getChildNodes().getLength(); ++i){
            childrenList.add(n.getChildNodes().item(i));
        }
        return childrenList;
    }

    public static ArrayList<Node> getChildren(ArrayList<Node> n){
        /**
         * return the children of the a node (just the next level)
         */
        ArrayList<Node> childrenList = new ArrayList<Node>();
        for(int j = 0; j < n.size(); j++) {
            Node curNode = n.get(j);
            for (int i = 0; i < curNode.getChildNodes().getLength(); i++) {
                childrenList.add(curNode.getChildNodes().item(i));
            }
        }
        return childrenList;
    }





}
