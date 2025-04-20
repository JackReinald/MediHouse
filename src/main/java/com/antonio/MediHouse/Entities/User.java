package com.antonio.MediHouse.Entities;

import jakarta.persistence.Embeddable;

@Embeddable
public record User(
        String name,
        int age
) {}
