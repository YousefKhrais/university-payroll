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
        String viewToReturn = "error/error500";

        System.out.println("Req: " + request.toString());

        if (status != null) {
            switch (Integer.parseInt(status.toString())) {
                case 400:
                    viewToReturn = "error/error400";
                    break;
                case 401:
                    viewToReturn = "error/error401";
                    break;
                case 403:
                    viewToReturn = "error/error403";
                    break;
                case 404:
                    viewToReturn = "error/error404";
                    break;
                case 500:
                    viewToReturn = "error/error500";
                    break;
                case 503:
                    viewToReturn = "error/error503";
                    break;
            }
        }
        return viewToReturn;
    }
}
