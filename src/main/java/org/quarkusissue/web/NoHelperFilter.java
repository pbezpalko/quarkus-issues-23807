package org.quarkusissue.web;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.eclipse.microprofile.config.Config;

// Please see also web.xml file.
@WebFilter(filterName = "NoHelperFilter", urlPatterns = "/hello/*")
public class NoHelperFilter implements javax.servlet.Filter {

    @Inject
    Config config;

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final FilterChain filterChain) throws IOException, ServletException {

        final String usageType = "microprofile.Config directly from javax.Filter";
        String key;

        key = "stage.environment";
        LogUtil.log(key, config.getOptionalValue(key, String.class), usageType);

        key = "stage.colour";
        LogUtil.log(key, config.getOptionalValue(key, String.class), usageType);

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
