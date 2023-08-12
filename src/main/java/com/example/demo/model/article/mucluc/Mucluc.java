package com.example.demo.model.article.mucluc;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "mucluc")
@Entity
public class Mucluc {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(name = "value", unique = true, nullable = false)
    private String value;

    public Mucluc(String value) {
        this.value = value;
    }

    protected Mucluc() {
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var mucluc = (Mucluc) o;
        return value.equals(mucluc.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

