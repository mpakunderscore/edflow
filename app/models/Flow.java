package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by pavelkuzmin on 01/08/16.
 */
@Entity
public class Flow extends Model {

    @Id
    public Long id;

    @Column(length=100)
    public String title;

    @Column(length=1000)
    public String categories;

    @Constraints.Required
    @Column(unique=true)
    public String url;

    //words count
    //unique words count
    //average word complexity

    public Date time = new Date();

    public Flow(String url, String title, String categories) {

        this.url = url;

        this.title = title.substring(0, Math.min(title.length(), 100));
        this.categories = categories;
        this.time = new Date();
    }
}
