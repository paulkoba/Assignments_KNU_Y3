package Server;

import Common.Article;
import Common.ArticleDAO;
import Common.NewsCategory;
import Common.NewsCategoryDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RMIServer extends UnicastRemoteObject implements IRMIServer {
    private final ArticleDAO articleDAO;
    private final NewsCategoryDAO categoryDao;
    private final transient ReadWriteLock lock;

    public RMIServer() throws RemoteException {
        this.articleDAO = new ArticleDAO();
        this.categoryDao = new NewsCategoryDAO();
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public boolean insertCategory(NewsCategory article) throws RemoteException {
        try {
            lock.writeLock().lock();
            return categoryDao.insert(article);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean deleteCategory(int id) throws RemoteException {
        try {
            lock.writeLock().lock();
            return categoryDao.deleteById(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean insertArticle(Article brand) throws RemoteException {
        try {
            lock.writeLock().lock();
            return articleDAO.insert(brand);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean deleteArticle(int id) throws RemoteException {
        try {
            lock.writeLock().lock();
            return articleDAO.deleteById(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean updateArticle(Article brand) throws RemoteException {
        try {
            lock.writeLock().lock();
            return articleDAO.update(brand);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean moveToAnotherCategory(int categoryId, int newNewsCategoryId) throws RemoteException {
        try {
            lock.writeLock().lock();
            return articleDAO.moveToAnotherCategory(categoryId, newNewsCategoryId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<Article> findArticlesByCategoryName(String categoryName) throws RemoteException {
        try {
            lock.readLock().lock();
            return articleDAO.findByCategoryName(categoryName);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<NewsCategory> findAllCategories() throws RemoteException {
        try {
            lock.readLock().lock();
            return categoryDao.findAll();
        } finally {
            lock.readLock().unlock();
        }
    }
}
