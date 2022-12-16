package Common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static java.lang.String.join;

public final class Utilities {

    private Utilities() {}

    public static void writeString(DataOutputStream out, String str) throws IOException {
        out.writeUTF(str);
    }

    public static String readString(DataInputStream in) throws IOException {
        return in.readUTF();
    }

    public static Article readArticle(DataInputStream in) throws IOException {
        Article brand = new Article();

        String data = in.readUTF();
        String[] brandData = data.split(";");
        brand.setId(Integer.parseInt(brandData[0]));
        brand.setCategoryId(Integer.parseInt(brandData[1]));
        brand.setName(brandData[2]);

        return brand;
    }

    public static NewsCategory readNewsCategory(DataInputStream in) throws IOException {
        NewsCategory manufacture = new NewsCategory();

        String data = in.readUTF();
        String[] manufactureData = data.split(";");
        manufacture.setId(Integer.parseInt(manufactureData[0]));
        manufacture.setName(manufactureData[1]);

        return manufacture;
    }

    public static void writeArticle(DataOutputStream out, Article brand) throws IOException {
        out.writeUTF(join(";", Integer.toString(brand.getId()),  Integer.toString(brand.getCategoryId()), brand.getName()));
    }

    public static void writeNewsCategory(DataOutputStream out, NewsCategory manufacture) throws IOException {
        out.writeUTF(join(";", Integer.toString(manufacture.getId()), manufacture.getName()));
    }

}