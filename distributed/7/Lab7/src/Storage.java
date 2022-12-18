import java.util.HashMap;
import java.util.Map;

public class Storage {
    private final Map<String, NewsCategory> categories = new HashMap<>();
    private final Map<String, String> categoryNames = new HashMap<>();
    private final Map<String, Article> articles = new HashMap<>();
    private final Map<String, String> articleNames = new HashMap<>();

    public Storage() { }

    public void createId(NewsCategory manufacture) {
        int id = categories.size();
        String idToString = "100" + id;
        while(categories.get(idToString) != null) {
            id++;
            idToString = "100" + id;
        }
        manufacture.setId(Integer.parseInt(idToString));
    }

    public void createId(Article brand) {
        int id = articles.size();
        String idToString = "200" + id;
        while(categories.get(idToString) != null) {
            id++;
            idToString = "200" + id;
        }
        brand.setId(Integer.parseInt(idToString));
    }

    public void addNewsCategory(NewsCategory manufacture) {
        categories.put(Integer.toString(manufacture.getId()), manufacture);
        categoryNames.put(manufacture.getName(), Integer.toString(manufacture.getId()));
    }

    public void addArticle(Article article, String id) {
        NewsCategory manufacture = categories.get(id);
        article.setCategoryId(Integer.parseInt(id));
        articles.put(Integer.toString(article.getId()), article);
        articleNames.put(article.getName(), Integer.toString(article.getId()));
        manufacture.getArticles().add(article);
    }

    public void removeNewsCategory(NewsCategory manufacture) {
        categories.remove(Integer.toString(manufacture.getId()));
        categoryNames.remove(manufacture.getName());
    }

    public void removeArticle(Article article) {
        NewsCategory manufacture = categories.get(article.getCategoryId());
        articles.remove(article.getId());
        articleNames.remove(article.getName());
        categories.get(Integer.toString(article.getCategoryId())).getArticles().remove(article);
        manufacture.getArticles().remove(article);
    }

    public void changeArticlesNewsCategory(Article brand, NewsCategory manufacture) {
        NewsCategory old = categories.get(brand.getId());
        if(old != null) {
            old.getArticles().remove(brand);
        }
        brand.setId(manufacture.getId());
    }

    public void renameArticle(Article brand, String newName) {
        articleNames.remove(brand.getName());
        brand.setName(newName);
        articleNames.put(brand.getName(), Integer.toString(brand.getId()));
    }

    public void renameNewsCategory(NewsCategory manufacture, String newName) {
        categoryNames.remove(manufacture.getName());
        manufacture.setName(newName);
        categoryNames.put(manufacture.getName(), Integer.toString(manufacture.getId()));
    }

    public Article getArticle(String name) {
        String id = articleNames.get(name);
        if(id != null) {
            return articles.get(id);
        }
        return null;
    }

    public NewsCategory getNewsCategory(String name) {
        String id = categoryNames.get(name);
        if(id != null) {
            return categories.get(id);
        }
        return null;
    }

    public Map<String, NewsCategory> getNewsCategories() {
        return categories;
    }

    public void outputAllContents(){
        for (NewsCategory category : categories.values()){
            System.out.println(category);
        }

        for (Article article : articles.values()) {
            System.out.println(article);
        }
    }
}