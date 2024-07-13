package com.hyuseinlesho.powerlog.config;

import com.hyuseinlesho.powerlog.mapper.ContactMapper;
import com.hyuseinlesho.powerlog.mapper.ExerciseMapper;
import com.hyuseinlesho.powerlog.mapper.WeightLogMapper;
import com.hyuseinlesho.powerlog.mapper.WorkoutMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    ContactMapper contactMapper() {
        return Mappers.getMapper(ContactMapper.class);
    }

    @Bean
    ExerciseMapper exerciseMapper() {
        return Mappers.getMapper(ExerciseMapper.class);
    }

    @Bean
    WeightLogMapper weightLogMapper() {
        return Mappers.getMapper(WeightLogMapper.class);
    }

    @Bean
    WorkoutMapper workoutMapper() {
        return Mappers.getMapper(WorkoutMapper.class);
    }
}
