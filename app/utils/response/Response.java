package utils.response;

import java.util.List;

public class Response {

    List<Category> subCategories;
    List<? extends Object> items;

    public Response(List<Category> subCategories, List<?> items) {
        this.subCategories = subCategories;
        this.items = items;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public List<Object> getItems() {
        return (List<Object>) items;
    }
}
