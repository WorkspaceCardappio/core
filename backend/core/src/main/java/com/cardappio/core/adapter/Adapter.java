package com.cardappio.core.adapter;

public interface Adapter<V, T> {

    V toDTO(T entity);

    T toEntity(V dto);
}
