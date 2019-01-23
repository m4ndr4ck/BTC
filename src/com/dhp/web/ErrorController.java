package com.dhp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {

    @RequestMapping(value = "errors", method = RequestMethod.GET)
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {

        ModelAndView errorPage = new ModelAndView("errorPage");
        String errorMsg = "";
        int httpErrorCode = getErrorCode(httpRequest);
        switch (httpErrorCode) {
            case 400: {
                errorMsg = "Requisição feita incorretamente";
                break;
            }
            case 401: {
                errorMsg = "Acesso são autorizado";
                break;
            }
            case 403:{
                return new ModelAndView("redirect:/");
            }
            case 404: {
                errorMsg = "Página não encontrada";
                break;
            }
            case 500: {
                errorMsg = "Houve um erro ao processar sua requisição";
                break;
            }
            default:
                errorMsg = "Erro ao processar sua requisção";
        }
        errorPage.addObject("errorMsg", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }
}