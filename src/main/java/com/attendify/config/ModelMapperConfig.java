package com.attendify.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Converter for converting entity to UUID
        Converter<Object, UUID> entityToUUIDConverter = ctx ->
                ctx.getSource() == null ? null : getIdFromEntity(ctx.getSource());

        // Converter for converting Set of entities to Set of UUIDs
        Converter<Set<?>, Set<UUID>> entitySetToUUIDSetConverter = ctx ->
                ctx.getSource() == null ? null :
                        ctx.getSource().stream()
                                .map(ModelMapperConfig::getIdFromEntity)
                                .collect(Collectors.toSet());

        // Converter for converting List<Entity> to List<DTO>
        Converter<List<?>, List<?>> entityListToDTOListConverter = ctx ->
                ctx.getSource() == null ? null :
                        ctx.getSource().stream()
                                .map(entity -> modelMapper.map(entity, ctx.getDestinationType().getComponentType()))
                                .collect(Collectors.toList());

        // Converter for converting Set<Entity> to Set<DTO>
        Converter<Set<?>, Set<?>> entitySetToDTOSetConverter = ctx ->
                ctx.getSource() == null ? null :
                        ctx.getSource().stream()
                                .map(entity -> modelMapper.map(entity, ctx.getDestinationType().getComponentType()))
                                .collect(Collectors.toSet());

        modelMapper.getConfiguration().setFieldMatchingEnabled(true);
        modelMapper.getConfiguration().setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        // Register converters
        modelMapper.addConverter(entityToUUIDConverter);
        modelMapper.addConverter(entitySetToUUIDSetConverter);
        modelMapper.addConverter(entityListToDTOListConverter);
        modelMapper.addConverter(entitySetToDTOSetConverter);

        return modelMapper;
    }

    private static UUID getIdFromEntity(Object entity) {
        try {
            return (UUID) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }
}
