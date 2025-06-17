package com.cardappio.core.test;

import com.cardappio.core.entity.EntityModel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
class Person implements EntityModel<Long> {
    private final Long id;
    private final String name;
}