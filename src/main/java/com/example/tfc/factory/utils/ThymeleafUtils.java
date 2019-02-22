package com.example.tfc.factory.utils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public abstract class ThymeleafUtils {

    private static TemplateEngine templateEngine;

    public static TemplateEngine tsTemplateEngine() {

        if (templateEngine == null) {
            SpringTemplateEngine tsTemplateEngine = new SpringTemplateEngine();

            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setOrder(Integer.valueOf(1));
            templateResolver.setPrefix("/snippets/");
            templateResolver.setSuffix(".tss");
            templateResolver.setTemplateMode(TemplateMode.JAVASCRIPT);
            templateResolver.setCharacterEncoding("UTF-8");
            templateResolver.setCacheable(false);

            tsTemplateEngine.addTemplateResolver(templateResolver);

            templateEngine = tsTemplateEngine;
        }

        return templateEngine;

    }

}
