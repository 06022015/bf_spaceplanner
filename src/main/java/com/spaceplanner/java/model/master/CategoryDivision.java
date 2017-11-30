package com.spaceplanner.java.model.master;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 11/30/15
 * Time: 11:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name = "category_division")
public class CategoryDivision {
    
    private Long id;
    private String category;
    private String division;
    private String description;


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String toString() {
        return "CategoryDivision{" +
                "category='" + category + '\'' +
                ", division='" + division + '\'' +
                '}';
    }
}
