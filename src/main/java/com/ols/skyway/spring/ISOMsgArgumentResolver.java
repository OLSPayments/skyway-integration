package com.ols.skyway.spring;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.XMLPackager;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class ISOMsgArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    return methodParameter.getParameterAnnotation(iISOMsg.class) != null;
  }

  @Override
  public Object resolveArgument(
      MethodParameter methodParameter,
      ModelAndViewContainer modelAndViewContainer,
      NativeWebRequest nativeWebRequest,
      WebDataBinderFactory webDataBinderFactory) throws Exception {

    HttpServletRequest request
        = (HttpServletRequest) nativeWebRequest.getNativeRequest();

    ISOMsg msg = new ISOMsg();
    msg.setPackager(new XMLPackager());
    msg.unpack(request.getInputStream());

    return msg;
  }
}
