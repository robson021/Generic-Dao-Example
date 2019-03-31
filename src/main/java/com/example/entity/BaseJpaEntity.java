package com.example.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseJpaEntity {

    @Id
    @GeneratedValue
    public Long id;

    public Long getId() {
        return id;
    }
}
