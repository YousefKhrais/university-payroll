package com.yousef.payroll.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorsController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String viewToReturn = "errors/error500";

        if (status != null) {
            switch (Integer.parseInt(status.toString())) {
                case 400:
                    viewToReturn = "errors/error400";
                    break;
                case 401:
                    viewToReturn = "errors/error401";
                    break;
                case 403:
                    viewToReturn = "errors/error403";
                    break;
                case 404:
                    viewToReturn = "errors/error404";
                    break;
                case 500:
                    viewToReturn = "errors/error500";
                    break;
                case 503:
                    viewToReturn = "errors/error503";
                    break;
            }
        }
        return viewToReturn;
    }
}
