package org.quarkusissue.web;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

// Please see also web.xml file.
@WebFilter(filterName = "WithHelperFilter", urlPatterns = "/hello/*")
public class WithHelperFilter implements javax.servlet.Filter {

    @Inject
    Helper helper;

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final FilterChain filterChain) throws IOException, ServletException {

        final String usageType = "microprofile.Config indirectly via Helper class";
        String key;

        key = "stage.environment";
        LogUtil.log(key, helper.getConfigValue(key), usageType);

        key = "stage.colour";
        LogUtil.log(key, helper.getConfigValue(key), usageType);

        filterChain.doFilter(servletRequest, servletResponse);
    }

}




