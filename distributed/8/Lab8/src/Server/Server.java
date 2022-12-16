package Server;

import Common.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
public class Server {
    private final int port;
    private DataInputStream in;
    private DataOutputStream out;
    private final ArticleDAO articleDAO;
    private final NewsCategoryDAO newsCategoryDAO;

    public Server(int port) {
        this.port = port;
        this.articleDAO = new ArticleDAO();
        this.newsCategoryDAO = new NewsCategoryDAO();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client.Client accepted");

            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            while (processQuery());
        }
    }

    public static void main(String[] args) {
        Server server = new Server(1234);
        try {
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean processQuery() {
        try {
            String query = Utilities.readString(in);
            System.out.println(query);

            switch (query) {
                case "insertNewsCategory": {
                    NewsCategory category = Utilities.readNewsCategory(in);
                    boolean result = newsCategoryDAO.insert(category);
                    out.writeBoolean(result);
                    break;
                }

                case "deleteNewsCategory": {
                    int id = in.readInt();
                    boolean result = newsCategoryDAO.deleteById(id);
                    out.writeBoolean(result);
                    break;
                }

                case "insertArticle": {
                    System.out.println("A");
                    Article article = Utilities.readArticle(in);
                    System.out.println("B");
                    boolean result = articleDAO.insert(article);
                    System.out.println("C");
                    out.writeBoolean(result);
                    break;
                }

                case "deleteArticle": {
                    int id = in.readInt();
                    boolean result = articleDAO.deleteById(id);
                    out.writeBoolean(result);
                    break;
                }

                case "updateArticle": {
                    Article article = Utilities.readArticle(in);
                    boolean result = articleDAO.update(article);
                    out.writeBoolean(result);
                    break;
                }

                case "moveToAnotherNewsCategory": {
                    int playerId = in.readInt();
                    int newTeamId = in.readInt();
                    boolean result = articleDAO.moveToAnotherCategory(playerId, newTeamId);
                    out.writeBoolean(result);
                    break;
                }

                case "findArticlesByNewsCategoryName": {
                    String categoryName = Utilities.readString(in);
                    List<Article> result = articleDAO.findByCategoryName(categoryName);
                    writeListOfArticles(result);
                    break;
                }

                case "findAllNewsCategories": {
                    List<NewsCategory> categories = newsCategoryDAO.findAll();
                    writeListOfNewsCategories(categories);
                    break;
                }

                default: {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void writeListOfArticles(List<Article> articles) throws IOException {
        out.writeInt(articles.size());

        for (Article article : articles) {
            Utilities.writeArticle(out, article);
        }
    }

    private void writeListOfNewsCategories(List<NewsCategory> newsCategory) throws IOException {
        out.writeInt(newsCategory.size());

        for (NewsCategory category : newsCategory) {
            Utilities.writeNewsCategory(out, category);
        }
    }
}
