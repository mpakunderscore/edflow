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

    @Column(length=100)
    public String title;

    @Column(length=100000)
    public String text;

    @Column(length=1000)
    public String image;

    @Column(length=1000)
    public String categories;

    @Constraints.Required
    @Column(unique=true)
    public String url;

    //words count
    //unique words count
    //average word complexity

    public Date time = new Date();

    public Page(String url, String title, String text, String image, String categories) {

        this.url = url;

        if (title.length() != 0)
            this.title = title.substring(0, Math.min(title.length(), 100));
        else
            this.title = title.substring(0, Math.min(url.length(), 100));

        this.text = text.substring(0, Math.min(text.length(), 100000));
        this.image = image;
        this.categories = categories;
        this.time = new Date();
    }
}
