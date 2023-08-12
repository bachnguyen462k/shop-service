package com.example.demo.model.category;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "category")
@Entity
public class Category {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(name = "value", unique = true, nullable = false)
    private String value;

    public Category(String value) {
        this.value = value;
    }

    protected Category() {
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var category = (Category) o;
        return value.equals(category.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
