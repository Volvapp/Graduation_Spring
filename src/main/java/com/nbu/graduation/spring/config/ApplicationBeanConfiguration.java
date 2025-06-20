package com.nbu.graduation.spring.config;

import com.nbu.graduation.spring.model.Student;
import com.nbu.graduation.spring.model.service.StudentServiceModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.addMappings(new PropertyMap<Student, StudentServiceModel>() {
            @Override
            protected void configure() {
                skip(destination.getCommitteeGrades());
            }
        });

        mapper.addMappings(new PropertyMap<StudentServiceModel, Student>() {
            @Override
            protected void configure() {
                skip(destination.getCommitteeGrades());
            }
        });

        return mapper;
    }


}
