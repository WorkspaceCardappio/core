package com.cardappio.core.test;

import com.cardappio.core.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
class Person implements Entity<Long> {
    private final Long id;
    private final String name;
}