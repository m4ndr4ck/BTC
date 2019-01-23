package com.dhp.web;

import com.dhp.afiliados.model.Afiliado;
import com.dhp.afiliados.service.AfiliadoService;
import com.dhp.model.User;
import com.dhp.util.Helpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class HomeController extends  BaseController {


    private SimpleMailMessage constructContato(String nome, String email, String mensagem) {
        return constructEmailAdmin("Nova Mensagem do Site", "Nome: "+nome+"\n"+
                "Email: " +email+"\n"+
                "Mensagem: "+mensagem+
                "\r\n\n");
    }

    private SimpleMailMessage constructEmailAdmin(String subject, String body) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo("admin@btcmoedas.com.br");
        email.setFrom("admin@btcmoedas.com.br");
        return email;
    }

}
