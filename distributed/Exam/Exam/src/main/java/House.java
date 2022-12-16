import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
public class House implements Serializable {
    private String id;
    private Integer apartmentNumber;
    private Integer area;
    private Integer floor;
    private Integer roomsCount;
    private String street;
    private String type;
    private Integer lifetime;
}