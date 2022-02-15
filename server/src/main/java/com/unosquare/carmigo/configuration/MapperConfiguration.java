package com.unosquare.carmigo.configuration;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MapperConfiguration
{
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ModelMapper mapper()
    {
        final ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return mapper;
    }
}
