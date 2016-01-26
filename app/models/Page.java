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

    @Column(length=1000)
    public String image;

    public String url;

    public Date date = new Date();

    transient boolean main;

    public Page(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public void setMain() {
        main = true;
    }
}
