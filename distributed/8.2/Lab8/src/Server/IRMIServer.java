package Server;

import Common.Article;
import Common.NewsCategory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRMIServer extends Remote {
    boolean insertCategory(NewsCategory category) throws RemoteException;
    boolean deleteCategory(int id) throws RemoteException;
    boolean insertArticle(Article article) throws RemoteException;
    boolean deleteArticle(int id) throws RemoteException;
    boolean updateArticle(Article article) throws RemoteException;
    boolean moveToAnotherCategory(int id, int categoryId) throws RemoteException;
    List<Article> findArticlesByCategoryName(String categoryName) throws RemoteException;
    List<NewsCategory> findAllCategories() throws RemoteException;
}
