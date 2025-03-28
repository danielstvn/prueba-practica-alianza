package com.alianza.test.cm.configuration;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfiguration {

    /**
     * Support for Java date and time API.
     * @return the corresponding Jackson module.
     */
    @Bean
    JavaTimeModule javaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();

        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        System.out.println("Jackson JavaTimeModule load...");
        return module;
    }

    @Bean
    Jdk8Module jdk8TimeModule() {
        return new Jdk8Module();
    }

}
