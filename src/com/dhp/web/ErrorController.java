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
                errorMsg = "Requisi��o feita incorretamente";
                break;
            }
            case 401: {
                errorMsg = "Acesso s�o autorizado";
                break;
            }
            case 403:{
                return new ModelAndView("redirect:/");
            }
            case 404: {
                errorMsg = "P�gina n�o encontrada";
                break;
            }
            case 500: {
                errorMsg = "Houve um erro ao processar sua requisi��o";
                break;
            }
            default:
                errorMsg = "Erro ao processar sua requis��o";
        }
        errorPage.addObject("errorMsg", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }
}