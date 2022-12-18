import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        String path = "src/data.xml";
        Storage showsalon = DOMParser.parse(path);
        showsalon.outputAllContents();

        System.out.println("\nAdd a new news article category.\n");
        NewsCategory manufacture_1 = new NewsCategory(123, "EzVictory");
        showsalon.addNewsCategory(manufacture_1);
        showsalon.outputAllContents();

        System.out.println("\nAdd some new articles into this category\n");
        Article first_brand = new Article(345, 678, "Article #1");
        showsalon.addArticle(first_brand, Integer.toString(manufacture_1.getId()));
        Article second_brand = new Article(123, 234, "Article #2");
        showsalon.addArticle(second_brand, Integer.toString(manufacture_1.getId()));
        showsalon.outputAllContents();

        System.out.println("\nChange article name\n");
        showsalon.renameArticle(showsalon.getArticle("Article #2"), "Article #42");
        showsalon.outputAllContents();

        System.out.println("\nChange category name.\n");
        showsalon.renameNewsCategory(showsalon.getNewsCategory("Category 1"), "Category 2");
        showsalon.outputAllContents();

        System.out.println("\nGet all categories:\n");
        Map<String, NewsCategory> manufactureMap = showsalon.getNewsCategories();
        for (String id : manufactureMap.keySet()){
            System.out.println(manufactureMap.get(id));
        }

        System.out.println("\nDelete category...\n");
        showsalon.removeNewsCategory(manufacture_1);
        showsalon.outputAllContents();

        DOMParser.write(showsalon, path);
    }
}