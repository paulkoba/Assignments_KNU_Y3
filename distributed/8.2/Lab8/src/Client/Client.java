package Client;

import Common.Article;
import Common.NewsCategory;

import Server.IRMIServer;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

public class Client {
    public static void main(String[] args) throws IOException, NotBoundException {
        IRMIServer server = (IRMIServer) Naming.lookup("//localhost:22222/server");

        System.out.println("Connected");
        server.deleteArticle(5);
        System.out.println("Delete article with id 5");
        server.deleteArticle(10);
        System.out.println("Delete article with id 10 ");
        server.deleteCategory(15);
        System.out.println("Delete news category with id 15");
        server.insertArticle(new Article(20, 25, "A terrible article"));
        System.out.println("Inserted new article");
        server.updateArticle(new Article(25, 30, "An article not so terrible"));
        System.out.println("Updated article");
        server.insertCategory(new NewsCategory(30, "Horror"));
        System.out.println("Inserted a news category");
        server.moveToAnotherCategory(20, 30);
        System.out.println("Changed item category");
        System.out.println(server.findArticlesByCategoryName("Found items with given category"));
        System.out.println("Found all articles by news category");
        System.out.println(server.findAllCategories());
        System.out.println("Find all news categories");

        System.out.println("Disconnected");
    }
}