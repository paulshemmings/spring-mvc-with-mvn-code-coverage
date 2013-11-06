package com.razor.SpringMvcCoverage;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring servlet configuration
 * This configuration class sets up the view resolvers and environment configuration.
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.razor.SpringMvcCoverage")
public class ServletConfiguration extends WebMvcConfigurerAdapter
{
    /**
     * pass through for static content
     *
     * @param configurer
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry configurer)
    {
        configurer.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/static/");
    }

    /**
     * Resource bundle for error messages, etc.
     *
     * @return
     */
    @Bean
    public MessageSource messageSource()
    {
        ResourceBundleMessageSource result = new ResourceBundleMessageSource();

        String[] basenames = {"messages"};

        result.setBasenames(basenames);

        return result;
    }

    /**
     * Spring view resolver for multiple view types.
     */

    @Bean
    public ContentNegotiatingViewResolver viewResolver()
    {
        ContentNegotiatingViewResolver contentNegotiatingViewResolver =
                new ContentNegotiatingViewResolver();
        contentNegotiatingViewResolver.setOrder(1);
        contentNegotiatingViewResolver.setFavorPathExtension(true);
        contentNegotiatingViewResolver.setFavorParameter(true);
        contentNegotiatingViewResolver.setIgnoreAcceptHeader(false);
        contentNegotiatingViewResolver.setUseNotAcceptableStatusCode(true);

        // media types
        Map<String, String> mediaTypes = new HashMap<String, String>();
        mediaTypes.put("view", "application/x-view");
        mediaTypes.put("view", "text/view");
        mediaTypes.put("view", "text/x-view");
        mediaTypes.put("view", "application/view");
        contentNegotiatingViewResolver.setMediaTypes(mediaTypes);

        // default views
        List<View> defaultViews = new ArrayList<View>();
        contentNegotiatingViewResolver.setDefaultViews(defaultViews);

        // chained view resolvers
        List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();
        viewResolvers.add(jspView());
        contentNegotiatingViewResolver.setViewResolvers(viewResolvers);
        return contentNegotiatingViewResolver;
    }

    /**
     * JSP view
     */

    @Bean
    public InternalResourceViewResolver jspView()
    {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /**
     * Configure the default servlet handler
     * @param configurer
     */

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
    {
        configurer.enable();
    }

}