package utils;

import models.Page;

import java.util.List;
import java.util.Map;

public class Response {

    List<Map<String, String>> subCategories;
    List<Page> pages;

    public Response(List<Map<String, String>> subCategories, List<Page> pages) {
        this.subCategories = subCategories;
        this.pages = pages;
    }

    public List<Map<String, String>> getSubCategories() {
        return subCategories;
    }

    public List<Page> getPages() {
        return pages;
    }
}
