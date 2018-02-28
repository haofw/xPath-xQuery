
import java.util.*;
import java.io.File;
import javax.xml.parsers.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XQueryExtendsVisitor extends XQueryBaseVisitor<ArrayList<Node>> {

	Document input = null;
	Document output = null;
	private HashMap<String, ArrayList<Node>> map = new HashMap<>();
	// private Stack<HashMap<String, ArrayList<Node>>> stack = new Stack<>();
	private ArrayList<Node> list = new ArrayList<>();

	private static ArrayList<Node> recordChildren(Node root) {
		ArrayList<Node> ret = new ArrayList<>();
		int index = 0;
		while(index < root.getChildNodes().getLength())
			ret.add(root.getChildNodes().item(index++));
		return ret;
	}

	@Override 
	public ArrayList<Node> visitXqAp(XQueryParser.XqApContext ctx) {
		return visit(ctx.ap());
	}

	@Override
	public ArrayList<Node> visitXqAll(XQueryParser.XqAllContext ctx) {// modified
		ArrayList<Node> ret = new ArrayList<>();
        Queue<Node> tempQueue = new LinkedList<>();
        ArrayList<Node> xqNode = visit(ctx.xq());
        ret.addAll(xqNode);
        tempQueue.addAll(xqNode);
        while(!tempQueue.isEmpty()){
            Node peek = tempQueue.poll();
            ArrayList<Node> children = new ArrayList<Node>(recordChildren(peek));
            ret.addAll(children);
            tempQueue.addAll(children);
        }
        list = ret;
        return visit(ctx.rp());
	}

	@Override
	public ArrayList<Node> visitXqVariable(XQueryParser.XqVariableContext ctx) {
		return map.get((ctx.getText()));
	}

	@Override
	public ArrayList<Node> visitXqChildRp(XQueryParser.XqChildRpContext ctx) {
		ArrayList<Node> xqNode = visit(ctx.xq());
		list = new ArrayList<Node>(xqNode);
		ArrayList<Node> children = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) 
			children.addAll(recordChildren(list.get(i)));
		list = children;

		ArrayList<Node> rpNode = new ArrayList<>(visit(ctx.rp()));
		Set<Node> set = new HashSet<>();
		for (Node n : rpNode) {
			set.add(n);
		}
		list = new ArrayList<Node>(set);
		return list;
	}

	@Override
	public ArrayList<Node> visitXqComma(XQueryParser.XqCommaContext ctx) { // modified
		ArrayList<Node> ret = visit(ctx.xq(0));
        ret.addAll(visit(ctx.xq(1)));
        return ret;
	}

	@Override
	public ArrayList<Node> visitXqStringConstant(XQueryParser.XqStringConstantContext ctx) {
		ArrayList<Node> ret = new ArrayList<>();
		String oriString = ctx.StringConstant().getText();
		ret.add(input.createTextNode(oriString.substring(1, oriString.length() - 1)));
		return ret;
	}

	@Override
	public ArrayList<Node> visitXqFLWR(XQueryParser.XqFLWRContext ctx) {
		ArrayList<Node> ret = new ArrayList<>();
		HashMap<String, ArrayList<Node>> ori_Map = new HashMap<>(map);
		int initial = 0;
		dfs(ctx, initial, ret);
		map = ori_Map;
		return ret;

	}
	private void dfs(XQueryParser.XqFLWRContext ctx, int i, ArrayList<Node> ret) {
		if(i < ctx.forClause().Var().size()){
			String st = ctx.forClause().Var(i).getText();
			ArrayList<Node> templist = visit(ctx.forClause().xq(i));
			for (Node node: templist) {
				templist = new ArrayList<>();
				templist.add(node);
				map.put(st, templist);
				dfs(ctx, i + 1, ret);
			}
		}
		else{
			HashMap<String, ArrayList<Node>> ori_Map = new HashMap<>(map);
			if (ctx.letClause() != null) {
				visit(ctx.letClause());
			}
			if (ctx.whereClause() != null && visit(ctx.whereClause()).isEmpty())
				return;
			ret.addAll(visit(ctx.returnClause()));
			map = ori_Map;
		}
	}

	@Override
	public ArrayList<Node> visitXqTag(XQueryParser.XqTagContext ctx) {
		if (output == null) {
			try {
				output = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();;
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
		ArrayList<Node> ret = new ArrayList<>();
        ArrayList<Node> xqret = visit(ctx.xq());
        // for (int i = 0; i< xqret.size(); i++) {
        //     System.out.println(xqret.get(i).getNodeName());
        // }
        ret.add(createNode(ctx.TAGNAME(0).getText(), xqret));
        return ret;
	}
	private Node createNode(String s, ArrayList<Node> nodeList) {
        Node result = output.createElement(s);
        for (Node temp : nodeList) {
            if (temp != null) {
                Node newNode = output.importNode(temp, true);
                result.appendChild(newNode);
            }
        }
        return result;
    }



	@Override
	public ArrayList<Node> visitXqWithP(XQueryParser.XqWithPContext ctx) {
		return visit(ctx.xq());
	}

	
	@Override
	public ArrayList<Node> visitXqLet(XQueryParser.XqLetContext ctx) {
		HashMap<String, ArrayList<Node>> mapOld = new HashMap<>(map);
		//stack.push(mapOld);
		visit(ctx.letClause());
        visit(ctx.xq());
        map = mapOld;
		// ArrayList<Node> result = visitChildren(ctx);
		//map = stack.pop();
		return list;
	}

	@Override
	public ArrayList<Node> visitLetClause(XQueryParser.LetClauseContext ctx) {
		for (int i = 0; i < ctx.Var().size(); i++) {
			map.put(ctx.Var(i).getText(), visit(ctx.xq(i)));
		}
		return null;
	}

	@Override 
	public ArrayList<Node> visitWhereClause(XQueryParser.WhereClauseContext ctx) {
		return visit(ctx.cond());
	}

	@Override
	public ArrayList<Node> visitReturnClause(XQueryParser.ReturnClauseContext ctx) {
		return visit(ctx.xq());
	}

	@Override
	public ArrayList<Node> visitCondWithP(XQueryParser.CondWithPContext ctx) {
		return visit(ctx.cond());
	}

	@Override
	public ArrayList<Node> visitCondOr(XQueryParser.CondOrContext ctx) {
		ArrayList<Node> first = visit(ctx.cond(0));
		ArrayList<Node> second = visit(ctx.cond(1));
		if(!(first.isEmpty() && second.isEmpty())){
	        if (!first.isEmpty()){
	            return first;
	        }
	        if (!second.isEmpty()){
	            return second;
	        }
    	}
    	return new ArrayList<>();

	}

	@Override
	public ArrayList<Node> visitCondAnd(XQueryParser.CondAndContext ctx) {
        ArrayList<Node> first = visit(ctx.cond(0));
        ArrayList<Node> second = visit(ctx.cond(1));
        if (!first.isEmpty() && !second.isEmpty()){
            return first;
        }
        return new ArrayList<>();
	}

	@Override
	public ArrayList<Node> visitCondEmpty(XQueryParser.CondEmptyContext ctx) {
		ArrayList<Node> temp = list;
		ArrayList<Node> ret = new ArrayList<>(visit(ctx.xq()));
		list = temp;
		if (ret.isEmpty()) {
			return list;
		} else {
			return new ArrayList<Node>();
		}
	}

	@Override 
	public ArrayList<Node> visitCondSome(XQueryParser.CondSomeContext ctx) { // modified
		HashMap<String, ArrayList<Node>> ori_Map = new HashMap<>(map);
		ArrayList ori_list = list;
        for (int i = 0; i < ctx.Var().size(); i++) {
            map.put(ctx.Var(i).getText(), visit(ctx.xq(i)));
        }
        ArrayList<Node> ret = visit(ctx.cond());
        map = ori_Map;
        list = ori_list;
        return ret;
	}



	@Override
	public ArrayList<Node> visitCondIs(XQueryParser.CondIsContext ctx) {
		ArrayList<Node> temp = list;
		ArrayList<Node> first = visit(ctx.xq(0));
		list = temp;
		ArrayList<Node> second = visit(ctx.xq(1));
		list = temp;
		int i = 0;
		int j = 0;
		while(i< first.size()){
			while(j < second.size()){
				if (first.get(i) == second.get(j)) {
					return list;
				}
				j++;
			}
			i++;
		}
		return new ArrayList<Node>();
	}

	@Override
	public ArrayList<Node> visitCondNot(XQueryParser.CondNotContext ctx) {
		ArrayList<Node> ret = new ArrayList<>();
		ArrayList<Node> temp = visit(ctx.cond());
        if (temp.isEmpty())
            ret.add(input.createElement("empty"));
        return ret;
	}	

	@Override
	public ArrayList<Node> visitCondEqual(XQueryParser.CondEqualContext ctx) {
		ArrayList<Node> temp = list;
		ArrayList<Node> first = visit(ctx.xq(0));
		list = temp;
		ArrayList<Node> second = visit(ctx.xq(1));
		list = temp;
		int i = 0;
		int j = 0;
		while(i< first.size()){
			while(j < second.size()){
				if (first.get(i).isEqualNode(second.get(j))) {
					return list;
				}
				j++;
			}
			i++;
		}
		return new ArrayList<Node>();
	}

	@Override
	public ArrayList<Node> visitApChildren(XQueryParser.ApChildrenContext ctx) {
		// visit(ctx.doc());
		// ArrayList<Node> result = new ArrayList<>();
		// for (int i = 0; i < list.size(); i++) {
		// 	result.addAll(recordChildren(list.get(i)));
		// }
		// list = result;
		// //for (int i = 0; i < list.size(); i++) {
		// //	System.out.println(result.get(i));
		// //}
		// return visit(ctx.rp());
		return visitChildren(ctx);
	}

	@Override
	public ArrayList<Node> visitApAll(XQueryParser.ApAllContext ctx) {
		visit(ctx.doc());
		Queue<Node> queue = new LinkedList<>(list);
		ArrayList<Node> result = new ArrayList<>();
		while (!queue.isEmpty()) {
			queue.addAll(recordChildren(queue.peek()));
			result.addAll(recordChildren(queue.peek()));
			queue.poll();
		}
		list = result;
		return visit(ctx.rp());
	}

	@Override
	public ArrayList<Node> visitDoc(XQueryParser.DocContext ctx) {
		if(input == null) {
            File xmlFile = new File(ctx.filename().getText());
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
                    input = docB.parse(xmlFile);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            input.getDocumentElement().normalize();
        }
        ArrayList<Node> result = new ArrayList<>();
        result.add(input);
        list = result;
        return result;
	}

	@Override
	public ArrayList<Node> visitRpParent(XQueryParser.RpParentContext ctx) {
		ArrayList<Node> resultWithDup = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			resultWithDup.add(list.get(i).getParentNode().getParentNode());
		}

     	ArrayList<Node> result = new ArrayList<>();
		for (Node n : resultWithDup) {
			if (!result.contains(n)) {
				result.add(n);
			}
		}
		list = result;
		return list;
	}

	@Override
	public ArrayList<Node> visitRpAttribute(XQueryParser.RpAttributeContext ctx) {
		ArrayList<Node> result = new ArrayList<>();
		// attribute_mark
		for (int i = 0; i < list.size(); i++) {
			Node currNode = list.get(i);
			for (int j = 0; j < currNode.getChildNodes().getLength(); j++) {
				Node childNode = currNode.getChildNodes().item(j);
				if (childNode.getNodeType() == Node.ATTRIBUTE_NODE && childNode.getNodeName().equals(ctx.TAGNAME().getText())) {
					result.add(currNode.getChildNodes().item(j));
				}
			}
		}
		list = result;
		return list;

	}

	// visitTxt
	@Override
	public ArrayList<Node> visitTxt(XQueryParser.TxtContext ctx) {
		ArrayList<Node> result = new ArrayList<>();
		for (Node childNode: list) {
			if (childNode.getNodeType() == Node.TEXT_NODE && !childNode.getTextContent().equals("\n")) {
				result.add(childNode);
			}
		}
		list = result;
		return list;

		// ArrayList<Node> ret = new ArrayList<>();
		// for (Node temp :list) {
  //           for (int i = 0; i < temp.getChildNodes().getLength(); i++) {
  //               if (temp.getChildNodes().item(i).getNodeType() == Node.TEXT_NODE && !temp.getChildNodes().item(i).getTextContent().equals("\n")) {
  //                   ret.add(temp);
  //               }
  //           }
  //       }
  //       list = ret;
  //       return list; 

	}

	@Override
	public ArrayList<Node> visitRpTagName(XQueryParser.RpTagNameContext ctx) {
		String TagName = ctx.TAGNAME().getText();
		ArrayList<Node> result = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			//System.out.println(list.get(i));
			String NodeName = list.get(i).getNodeName();
			if (NodeName.equals(TagName)) {
				result.add(list.get(i));
			}
		}
		list = result;
		return list;
	}

	@Override
	public ArrayList<Node> visitRpAllChildren(XQueryParser.RpAllChildrenContext ctx) {
		ArrayList<Node> ret = new ArrayList<>();
        for(Node temp : list) {
            ret.addAll(recordChildren(temp));
        }
        list = ret;
        return ret;
	}

	@Override
	public ArrayList<Node> visitRpFilter(XQueryParser.RpFilterContext ctx) {
		// ArrayList<Node> temp = visit(ctx.rp());
		// ArrayList<Node> result = new ArrayList<>();
		// // attribute
		// for (int i = 0; i < temp.size(); i++) {
		// 	list = new ArrayList<Node>();
		// 	list.add(temp.get(i));
		// 	ArrayList<Node> filterList = visit(ctx.filter());
		// 	if (!filterList.isEmpty()) {
		// 		result.add(temp.get(i));
		// 	}
		// }
		// list = result;
		// return list;
		ArrayList<Node> result = visit(ctx.rp());
        ArrayList<Node> filter= visit(ctx.filter());
        if (filter.isEmpty()) {
            return new ArrayList<>();
        }
        else return result;
	}

	@Override
	public ArrayList<Node> visitRpComma(XQueryParser.RpCommaContext ctx) {
		ArrayList<Node> result = new ArrayList<>();
		ArrayList<Node> temp = list;
		result.addAll(visit(ctx.rp(0)));
		list = temp;
		result.addAll(visit(ctx.rp(1)));
		list = result;
		return list;
	}

	@Override
	public ArrayList<Node> visitRpCurrent(XQueryParser.RpCurrentContext ctx) {
		//HashSet<Node> set = new HashSet<>();
		ArrayList<Node> resultWithDup = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			resultWithDup.add(list.get(i).getParentNode());
		}
		ArrayList<Node> result = new ArrayList<>();
		for (Node n : resultWithDup) {
			if (!result.contains(n)) {
				result.add(n);
			}
		}
		list = result;
		return list;
	}

	@Override
	public ArrayList<Node> visitRpWithP(XQueryParser.RpWithPContext ctx) {
		return visit(ctx.rp());
	}

	@Override
	public ArrayList<Node> visitRpAll(XQueryParser.RpAllContext ctx) {
		visit(ctx.rp(0));
		Queue<Node> queue = new LinkedList<>(list);
		ArrayList<Node> temp = new ArrayList<>();
		while (!queue.isEmpty()) {
			queue.addAll(recordChildren(queue.peek()));
			temp.addAll(recordChildren(queue.peek()));
			queue.poll();
		}
		list = temp;
		ArrayList<Node> resultWithDup = new ArrayList<>(visit(ctx.rp(1)));
		ArrayList<Node> result = new ArrayList<>();
		for (Node n : resultWithDup) {
			if (!result.contains(n)) {
				result.add(n);
			}
		}
		list = result;
		return list;
	}

	@Override
	public ArrayList<Node> visitRpChildren(XQueryParser.RpChildrenContext ctx) {
		visit(ctx.rp(0));
		ArrayList<Node> temp = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			temp.addAll(recordChildren(list.get(i)));
		}
		list = temp;
		ArrayList<Node> resultWithDup = new ArrayList<>(visit(ctx.rp(1)));
		ArrayList<Node> result = new ArrayList<>();
		for (Node n : resultWithDup) {
			if (!result.contains(n)) {
				result.add(n);
			}
		}
		list = result;
		return list;

	}

	@Override
	public ArrayList<Node> visitFltEqual(XQueryParser.FltEqualContext ctx) {
		ArrayList<Node> ori_list = list;
		ArrayList<Node> first = visit(ctx.rp(0));
		list = ori_list;
		ArrayList<Node> second = visit(ctx.rp(1));
		list = ori_list;
		int i = 0;
		int j = 0;
		while  (i<first.size()){
			while (j<second.size()){
				if (first.get(i).isEqualNode(second.get(j)))
					return list;
				j++;
			}
			i++;
		}
		return new ArrayList<Node>();
	}

	@Override
	public ArrayList<Node> visitFltNot(XQueryParser.FltNotContext ctx) {
        ArrayList<Node> ret = visit(ctx.filter());
        if (!ret.isEmpty()) {
            return list;
        }
        return new ArrayList<>();
	}

	@Override 
	public ArrayList<Node> visitFltWithP(XQueryParser.FltWithPContext ctx) {
		return visit(ctx.filter());
	}

	@Override
	public ArrayList<Node> visitFltOr(XQueryParser.FltOrContext ctx) {
		ArrayList<Node> first = visit(ctx.filter(0));
        ArrayList<Node> second = visit(ctx.filter(1));
        if(first.isEmpty() && second.isEmpty())
        	return new ArrayList<Node>();
        return list;
	}

	@Override
	public ArrayList<Node> visitFltAnd(XQueryParser.FltAndContext ctx) {
		ArrayList<Node> first = visit(ctx.filter(0));
        ArrayList<Node> second = visit(ctx.filter(1));
        if(first.size() == 0 || second.size() == 0)
        	return new ArrayList<Node>();
        return first;
	}

	@Override
	public ArrayList<Node> visitFltRp(XQueryParser.FltRpContext ctx) {
        ArrayList<Node> ori_list = list;
        ArrayList<Node> ret = visit(ctx.rp());
        list = ori_list;
        return ret;
	}

	@Override
	public ArrayList<Node> visitFltIs(XQueryParser.FltIsContext ctx) {
		ArrayList<Node> ori_list = list;
		ArrayList<Node> first = visit(ctx.rp(0));
		list = ori_list;
		ArrayList<Node> second = visit(ctx.rp(1));
		list = ori_list;
        int i=0;
        int j=0;
		while  (i<first.size()){
			while (j<second.size()){
				if (first.get(i) == second.get(j))
					return list;
				j++;
			}
			i++;
		}
		return new ArrayList<Node>();
	}


}