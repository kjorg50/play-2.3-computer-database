package models;

import java.util.*;
import javax.persistence.*;

import play.Logger;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * Computer entity managed by Ebean
 */
@Entity
public class Computer extends Model {

    private static final long serialVersionUID = 1L;

	@Id
    public Long id;

    @Constraints.Required
    public String name;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date introduced;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date discontinued;

    @ManyToOne
    public Company company;

    /**
     * Generic query helper for entity Computer with id Long
     */
    public static Finder<Long,Computer> find = new Finder<Long,Computer>(Long.class, Computer.class);

    /**
     * Return a page of computer
     *
     * @param page Page to display
     * @param pageSize Number of computers per page
     * @param sortBy Computer property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page<Computer> page(int page, int pageSize, String sortBy, String order, String filter) {
        return
            find.where()
                .ilike("name", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .fetch("company")
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page);
    }

    public List<ValidationError> validate() {

        // Note that changes in this model where NOT required, I made them to keep code cleaner.
        // I.e. You don't need to create getters for fields like name, or id as they are public

        List<ValidationError> errors = new ArrayList<>();
        if (Computer.find.where().ilike("name", name).ne("id", id).findRowCount() != 0) {
            errors.add(new ValidationError("name", "Name must be unique. That value is already taken."));
        }

        return errors.isEmpty() ? null : errors;
    }
}

