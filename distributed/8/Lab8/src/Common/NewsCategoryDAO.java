package Common;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class NewsCategoryDAO {
    public Optional<NewsCategory> findById(int id) {
        final String sql = "SELECT * FROM news_categories WHERE id = ?";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            Optional<NewsCategory> result = Optional.empty();

            try (ResultSet rs = statement.executeQuery();) {
                if (rs.next()) {
                    NewsCategory manufacture = new NewsCategory();
                    manufacture.setId(rs.getInt("id"));
                    manufacture.setName(rs.getString("name"));
                    result = Optional.of(manufacture);
                }
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<NewsCategory> findAll() {
        final String sql = "SELECT * FROM news_categories";

        try (Connection connection = Connector.getConnection();
             Statement statement = connection.createStatement()) {

            List<NewsCategory> result = new ArrayList<>();

            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    NewsCategory manufacture = new NewsCategory();
                    manufacture.setId(rs.getInt("id"));
                    manufacture.setName(rs.getString("name"));
                    result.add(manufacture);
                }
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(NewsCategory updated) {
        final String sql = "UPDATE news_categories SET name = ? WHERE id = ?";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, updated.getName());
            statement.setInt(2, updated.getId());
            int updatedRecords = statement.executeUpdate();

            return updatedRecords > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteById(int id) {
        final String sql = "DELETE FROM news_categories WHERE id = ?";

        try (Connection connection = Connector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int deletedRecords = statement.executeUpdate();

            return deletedRecords > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insert(NewsCategory toInsert) {
        final String sql = "INSERT INTO news_categories(id, name) VALUES(?, ?)";

        try (Connection connection = Connector.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, new Random().nextInt());
            statement.setString(2, toInsert.getName());
            int insertedCount = statement.executeUpdate();

            return insertedCount > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}