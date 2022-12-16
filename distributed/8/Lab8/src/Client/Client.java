package Client;

import Common.Article;
import Common.NewsCategory;
import Common.Utilities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 1234);

        try {
            client.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Connected");
        client.deleteArticle(5);
        System.out.println("Delete article with id 5");
        client.deleteArticle(10);
        System.out.println("Delete article with id 10 ");
        client.deleteNewsCategory(15);
        System.out.println("Delete news category with id 15");
        client.insertArticle(new Article(20, 25, "A terrible article"));
        System.out.println("Inserted new article");
        client.updateArticle(new Article(25, 30, "An article not so terrible"));
        System.out.println("Updated article");
        client.insertNewsCategory(new NewsCategory(30, "Horror"));
        System.out.println("Inserted a news category");
        client.moveToAnotherNewsCategory(20, 30);
        System.out.println("Changed item category");
        System.out.println(client.findArticlesByNewsCategoryName("Found items with given category"));
        System.out.println("Found all articles by news category");
        System.out.println(client.findAllNewsCategories());
        System.out.println("Find all news categories");
        client.disconnect();

        System.out.println("Disconnected");
    }
    private Socket socket;
    private final String host;
    private final int port;
    private DataInputStream in;
    private DataOutputStream out;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void disconnect() throws IOException {
        socket.close();
    }

    public boolean insertNewsCategory(NewsCategory manufacture) throws IOException {
        Utilities.writeString(out, "insertNewsCategory");
        Utilities.writeNewsCategory(out, manufacture);

        return in.readBoolean();
    }

    public boolean deleteNewsCategory(int id) throws IOException {
        Utilities.writeString(out, "deleteNewsCategory");
        out.writeInt(id);

        return in.readBoolean();
    }

    public boolean insertArticle(Article article) throws IOException {
        Utilities.writeString(out, "insertArticle");
        Utilities.writeArticle(out, article);

        return in.readBoolean();
    }

    public boolean deleteArticle(int id) throws IOException {
        Utilities.writeString(out, "deleteArticle");
        out.writeInt(id);

        return in.readBoolean();
    }

    public boolean updateArticle(Article article) throws IOException {
        Utilities.writeString(out, "updateArticle");
        Utilities.writeArticle(out, article);

        return in.readBoolean();
    }

    public boolean moveToAnotherNewsCategory(int articleId, int newNewsCategoryId) throws IOException {
        Utilities.writeString(out, "moveToAnotherNewsCategory");
        out.writeInt(articleId);
        out.writeInt(newNewsCategoryId);

        return in.readBoolean();
    }

    public List<Article> findArticlesByNewsCategoryName(String manufactureName) throws IOException {
        Utilities.writeString(out, "findArticlesByNewsCategoryName");
        Utilities.writeString(out, manufactureName);

        return readArticles();
    }

    public List<NewsCategory> findAllNewsCategories() throws IOException {
        Utilities.writeString(out, "findAllNewsCategories");

        return readNewsCategories();
    }

    private List<Article> readArticles() throws IOException {
        List<Article> result = new ArrayList<>();
        int listSize = in.readInt();

        for (int i = 0; i < listSize; i++) {
            result.add(Utilities.readArticle(in));
        }

        return result;
    }

    private List<NewsCategory> readNewsCategories() throws IOException {
        List<NewsCategory> result = new ArrayList<>();
        int listSize = in.readInt();
        System.out.println(listSize);

        for (int i = 0; i < listSize; i++) {
            result.add(Utilities.readNewsCategory(in));
        }

        return result;
    }
}