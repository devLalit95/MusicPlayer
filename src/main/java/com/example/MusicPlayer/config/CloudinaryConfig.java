package com.example.MusicPlayer.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dv56dpq4j",
                "api_key", "698489397941513",
                "api_secret", "sBarag95rg8-yguk7xy_nDer6p4",
                "secure", true
        ));
    }
}
