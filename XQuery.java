import java.io.*;
import java.util.ArrayList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XQuery {
    public static void main(String[] args) throws IOException{
        String queryFile = "./XQueryTest.txt";
        String resultPath = "./output.xml";
        CharStream input = CharStreams.fromFileName(queryFile);
        XQueryParser parser = new XQueryParser(new CommonTokenStream(new XQueryLexer(input)));
        ParseTree tree = parser.xq();
        XQueryCustomizedVisitor evaluation = new XQueryCustomizedVisitor();
        ArrayList<Node> finalResult = evaluation.visit(tree);
        createResultFile(evaluation.outputDocument, finalResult, resultPath);

        System.out.println("Saving output.xml");
        if (!finalResult.isEmpty()){
            System.out.println("finalResult size: " + finalResult.size());
            for (Node n : finalResult) {
                System.out.println("Node name: " + n.getNodeName());
                if (n.getChildNodes() != null) {
                    for (int i = 0; i < n.getChildNodes().getLength(); i ++) {
                        System.out.println(n.getChildNodes().item(i).getTextContent());
                    }

                }
            }
        }
    }

    public static ArrayList evalRewrited(String rewrited) {
        try {
            ANTLRInputStream input = new ANTLRInputStream(rewrited);
            XQueryLexer lexer = new XQueryLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            XQueryParser parser = new XQueryParser(tokens);
            parser.removeErrorListeners();
            ParseTree tree = parser.xq();

            //for Visitor
            XQueryCustomizedVisitor visitor = new XQueryCustomizedVisitor();
            visitor.reWrite = false;
            ArrayList<Node> results = visitor.visit(tree);
            return results;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
        return null;

    }


    public static void createResultFile(Document doc, ArrayList<Node> finalResult, String resultPath) {
        doc.appendChild(finalResult.get(0));
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult res = new StreamResult(resultPath);
            transformer.transform(source, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
