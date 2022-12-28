package aic.g3t1.interfaceserver.controller.cors;

import aic.g3t1.common.environment.EnvironmentVariables;
import aic.g3t1.common.exceptions.MissingEnvironmentVariableException;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CORSFilter implements Filter {
    public final static String corsOrigin;

    static {
        try {
            corsOrigin = EnvironmentVariables.getVariable("CORS_ORIGIN");
        } catch (MissingEnvironmentVariableException e) {
            System.err.println("Missing 'CORS_ORIGIN' environment variable.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Origin", corsOrigin);
        res.addHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(req, res);
    }
}
