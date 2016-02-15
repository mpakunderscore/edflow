package utils;

import models.Page;
import models.Word;

import java.util.List;
import java.util.Map;

public class Response {

    List<Map<String, String>> subCategories;
    List<? extends Object> items;

    public Response(List<Map<String, String>> subCategories, List<?> items) {
        this.subCategories = subCategories;
        this.items = items;
    }

    public Response(List<Word> words) {
        this.items = words;
    }

    public List<Map<String, String>> getSubCategories() {
        return subCategories;
    }

    public List<Object> getItems() {
        return (List<Object>) items;
    }
}
