package com.zhao.javalesson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties( Student.class)
public class StudentAutoConfiguration {
    private final  Student student;

    @Autowired
    public StudentAutoConfiguration(Student student) {
        this.student = student;
    }

    @Bean
    public  StudentService studentService(){

        return new StudentService(student.getNo(),student.getScore(),student.getName());
    }
}
