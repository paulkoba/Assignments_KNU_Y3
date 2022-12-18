import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DOMParser {
    public static class SimpleErrorHandler implements ErrorHandler {
        public void warning(SAXParseException e) throws SAXException {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
        public void error(SAXParseException e) throws SAXException {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
        public void fatalError(SAXParseException e) throws SAXException {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
    }

    public static Storage parse(String path) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new SimpleErrorHandler());
        Document doc = builder.parse(new File(path));
        doc.getDocumentElement().normalize();

        Storage showsalon = new Storage();
        NodeList nodes = doc.getElementsByTagName("NewsCategory");

        for(int i = 0; i < nodes.getLength(); ++i) {
            Element n = (Element)nodes.item(i);
            NewsCategory category = new NewsCategory();
            category.setId(Integer.parseInt(n.getAttribute("id")));
            category.setName(n.getAttribute("name"));
            showsalon.addNewsCategory(category);
        }

        nodes = doc.getElementsByTagName("Article");
        for(int j =0; j < nodes.getLength(); ++j) {
            Element e = (Element) nodes.item(j);
            Article article = new Article();
            article.setId(Integer.parseInt(e.getAttribute("id")));
            article.setCategoryId(Integer.parseInt(e.getAttribute("categoryID")));
            article.setName(e.getAttribute("name"));
            showsalon.addArticle(article, e.getAttribute("categoryID"));
        }

        return showsalon;
    }

    public static void write(Storage showsalon, String path) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("Storage");
        doc.appendChild(root);

        Map<String, NewsCategory> authors = showsalon.getNewsCategories();
        for(Map.Entry<String, NewsCategory> entry : authors.entrySet()) {
            Element gnr = doc.createElement("NewsCategory");
            gnr.setAttribute("id", Integer.toString(entry.getValue().getId()));
            gnr.setAttribute("name", entry.getValue().getName());
            root.appendChild(gnr);

            for(Article brand : entry.getValue().getArticles()) {
                Element mv = doc.createElement("Article");
                mv.setAttribute("id", Integer.toString(brand.getId()));
                mv.setAttribute("categoryID", Integer.toString(brand.getCategoryId()));
                mv.setAttribute("name", brand.getName());
                gnr.appendChild(mv);
            }
        }
        Source domSource = new DOMSource(doc);
        Result fileResult = new StreamResult(new File(path));
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer transformer = tfactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "data.dtd");
        transformer.transform(domSource, fileResult);
    }
}