package com.cardappio.core.adapter;

public interface Adapter<Entity, ListDTO, CreateDTO> {

    ListDTO toDTO(Entity entity);

    Entity toEntity(CreateDTO dto);
}
