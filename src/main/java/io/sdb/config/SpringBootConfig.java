package io.sdb.config;

import com.jfinal.template.ext.spring.JFinalViewResolver;
import com.jfinal.template.source.ClassPathSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBootConfig {
    @Bean(name = "jfinalViewResolver")
    public JFinalViewResolver getJfinalViewRrsolver() {
        JFinalViewResolver jfr = new JFinalViewResolver();
        jfr.setDevMode(true);

        jfr.setSourceFactory(new ClassPathSourceFactory());
        jfr.setPrefix("/templates/");
        jfr.setSuffix(".html");
        jfr.setContentType("text/html;charset=UTF-8");
        jfr.setOrder(0);
//        jfr.addSharedFunction("/view/common/_layout.html");
        return jfr;
    }
}
