package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Page extends Model {

    @Id
    public Long id;

    @Constraints.Required
    @Column(unique=true)
    public String title;

    @Column(length=10000)
    public String description;

    @Column(length=100000)
    public String text;

    @Column(length=1000)
    public String image;

    @Column(length=1000)
    public String categories;

    public String url;

    public Date time = new Date();

    transient boolean main;

    public Page(String title, String description, String text, String image, String categories) {
        this.title = title;
        this.description = description;
        this.text = text;
        this.image = image;
        this.categories = categories;
    }

    public void setMain() {
        main = true;
    }
}
