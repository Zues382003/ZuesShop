package com.zuesshopfrontend.setting;

import com.zuesshop.entity.Setting;
import com.zuesshopfrontend.setting.Service.SettingService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
@Component
public class SettingFilter implements Filter {

    @Autowired
    private SettingService settingService;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String url = servletRequest.getRequestURL().toString();

        if(url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") || url.endsWith(".jpg")){
            chain.doFilter(request, response);
            return;
        }

        List<Setting> generalSettings = settingService.getGeneralSettings();

        generalSettings.forEach(setting -> {
            request.setAttribute(setting.getKey(), setting.getValue());// is stored in session of website. Mean all request which is sent, is stored in session of website
        });

        chain.doFilter(request,response);
    }
}
