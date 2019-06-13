package com.stackroute.zuulservice.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import javax.servlet.http.HttpServletRequest;

public class PreFilter extends ZuulFilter {

    @Override
    public String filterType() { return "error";}

    @Override
    public int filterOrder() {return 1;}

    @Override
    public boolean shouldFilter() {return true;}

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        System.out.println("Request Method : " + request.getMethod() + " Request url " + request.getRequestURL().toString());
        return null;
    }
}
