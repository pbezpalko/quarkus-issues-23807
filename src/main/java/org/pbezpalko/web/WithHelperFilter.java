package org.pbezpalko.web;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

// Please see also web.xml file.
@WebFilter(filterName = "SampleFilter", urlPatterns = "/hello/*")
public class WithHelperFilter implements javax.servlet.Filter {

    @Inject
    Helper helper;

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final FilterChain filterChain) throws IOException, ServletException {
        Optional<String> value = helper.getConfigValue("stage.environment");
        System.out.println(value + " from filter with helper class");

        filterChain.doFilter(servletRequest, servletResponse);
    }

}




