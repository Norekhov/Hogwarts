package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/**
*Creating a service for working with ports
*/
@Service
public class InfoService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    @Value("${server.port}")
    private String port;

    public String getPort() {
        logger.info("Был вызван метод для \"getPort\"");
        logger.debug("Номер порта={} был передан",port);
        return port;
    }

}
