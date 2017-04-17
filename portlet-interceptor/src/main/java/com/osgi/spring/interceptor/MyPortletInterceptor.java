package com.osgi.spring.interceptor;

import com.google.gson.Gson;
import com.osgi.spring.annotation.LoadJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.handler.HandlerInterceptorAdapter;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.lang.reflect.Method;

public class MyPortletInterceptor extends HandlerInterceptorAdapter {

    public Gson gson;

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void postHandleRender(RenderRequest request, RenderResponse response, Object portletController, ModelAndView modelAndView) throws Exception {
        Class<?> clazz = portletController.getClass();

        Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(clazz);
        for (Method method : methods) {
            if (method.isAnnotationPresent(LoadJson.class)) {
                Object result = method.invoke(portletController);
                modelAndView.addObject("LoadJson", gson.toJson(result));
                break;
            }
        }
    }
}