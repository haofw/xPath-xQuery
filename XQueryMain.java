/*************************************************************************
    > File Name: Main.java 
    > Created Time: 2018-02-20 21:54
 ************************************************************************/
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.io.*;
import java.util.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

public class XQueryMain {
    public static void main(String[] args) throws IOException{
        String testF = "XQueryTest.txt";
        String ret = "./output.xml";
        CharStream input = CharStreams.fromFileName(testF);
        XQueryParser parser = new XQueryParser(new CommonTokenStream(new XQueryLexer(input)));
        ParseTree tree = parser.xq();
        XQueryExtendsVisitor myvisitor = new XQueryExtendsVisitor();
        ArrayList<Node> fi = myvisitor.visit(tree);
        Document doc = myvisitor.output;
        doc.appendChild(fi.get(0));
        try {
            Transformer tran = TransformerFactory.newInstance().newTransformer();
            tran.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult stream_ret = new StreamResult(ret);
            tran.transform(source, stream_ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
