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
    public String title;

    public String description;

    public String image;

    public String url;

    public Date date = new Date();
}
