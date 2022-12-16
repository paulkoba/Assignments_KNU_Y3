package Common;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ArticleDAO {
    public Optional<Article> findById(int id) {
        final String sql = "SELECT * FROM Articles WHERE id = ?";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            Optional<Article> result = Optional.empty();

            try (ResultSet rs = statement.executeQuery();) {
                if (((ResultSet) rs).next()) {
                    Article Article = new Article();
                    Article.setId(rs.getInt("id"));
                    Article.setCategoryId(rs.getInt("category_id"));
                    Article.setName(rs.getString("name"));
                    result = Optional.of(Article);
                }
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Article> findAll() {
        final String sql = "SELECT * FROM Articles";

        try (Connection connection = Connector.getConnection();
             Statement statement = connection.createStatement()) {

            List<Article> result = new ArrayList<>();

            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    Article Article = new Article();
                    Article.setId(rs.getInt("id"));
                    Article.setCategoryId(rs.getInt("category_id"));
                    Article.setName(rs.getString("name"));
                    result.add(Article);
                }
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(Article updated) {
        final String sql = "UPDATE articles SET category_id = ?, name = ? WHERE id = ?";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, updated.getCategoryId());
            statement.setString(2, updated.getName());
            statement.setInt(3, updated.getId());
            int updatedRecords = statement.executeUpdate();

            return updatedRecords > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteById(int id) {
        final String sql = "DELETE FROM articles WHERE id = ?";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            int deletedRecords = statement.executeUpdate();

            return deletedRecords > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insert(Article toInsert) {
        final String sql = "INSERT INTO articles(id, category_id, name) VALUES (?, ?, ?)";

        try (Connection connection = Connector.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, new Random().nextInt());
            statement.setInt(2, toInsert.getCategoryId());
            statement.setString(3, toInsert.getName());
            int insertedCount = statement.executeUpdate();

            return insertedCount > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Article> findByCategoryName(String manufactureName) {
        final String sql = "SELECT * FROM articles WHERE category_id = (SELECT id FROM news_categories WHERE news_categories.name = ?)";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, manufactureName);
            List<Article> result;

            try (ResultSet rs = statement.executeQuery()) {
                result = getArticlesFromResultSet(rs);
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean moveToAnotherCategory(int ArticleId, int newsCategory) {
        Optional<Article> optionalArticle = findById(ArticleId);

        if (optionalArticle.isEmpty()) {
            return false;
        }

        Article Article = optionalArticle.get();
        Article.setCategoryId(newsCategory);
        update(Article);

        return true;
    }

    private List<Article> getArticlesFromResultSet(ResultSet rs) throws SQLException {
        List<Article> result = new ArrayList<>();

        while (rs.next()) {
            Article Article = new Article();
            Article.setId(rs.getInt("id"));
            Article.setCategoryId(rs.getInt("category_id"));
            Article.setName(rs.getString("name"));;
            result.add(Article);
        }

        return result;
    }
}