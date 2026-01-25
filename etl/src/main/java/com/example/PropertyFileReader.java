package com.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileReader 
{
    public static Properties readPropertyFile(String file) throws IOException {
        Properties props = new Properties();

        try (InputStream input =
                PropertyFileReader.class
                    .getClassLoader()
                    .getResourceAsStream(file)) {

            if (input == null) {
                throw new FileNotFoundException("Property file not found: " + file);
            }
            props.load(input);
        }   
        return props;
    }
}

