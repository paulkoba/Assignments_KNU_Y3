import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsCategory {
    private int id;
    private String name;
    private List<Article> articles = new ArrayList<>();

    public NewsCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
