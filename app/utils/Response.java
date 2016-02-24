package utils;

import models.Page;
import models.Word;

import java.util.List;
import java.util.Map;

public class Response {

    List<Category> subCategories;
    List<? extends Object> items;

    public Response(List<Category> subCategories, List<?> items) {
        this.subCategories = subCategories;
        this.items = items;
    }

    public Response(List<Word> words) {
        this.items = words;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public List<Object> getItems() {
        return (List<Object>) items;
    }
}
