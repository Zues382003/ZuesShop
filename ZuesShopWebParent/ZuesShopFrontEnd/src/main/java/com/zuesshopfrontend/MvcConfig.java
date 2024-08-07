package com.zuesshopfrontend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ///
        AddResourceHandlers("../category-images", registry, "/category-images/**");
        ///
        AddResourceHandlers("../brand-logos", registry, "/brand-logos/**");
        //
        AddResourceHandlers("../product-images", registry, "/product-images/**");
        //
        AddResourceHandlers("../site-logo", registry, "/site-logo/**");
    }

    private void AddResourceHandlers(String dirEntity, ResourceHandlerRegistry registry, String pathPatterns){
        Path entityImagesDir = Paths.get(dirEntity);
        String entityImagesPath = entityImagesDir.toFile().getAbsolutePath();

        registry.addResourceHandler(pathPatterns)
                .addResourceLocations("file:/" + entityImagesPath + "/");
    }

}
