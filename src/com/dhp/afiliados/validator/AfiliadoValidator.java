package com.dhp.afiliados.validator;

import com.dhp.afiliados.model.Afiliado;
import com.dhp.afiliados.service.AfiliadoService;
import com.dhp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AfiliadoValidator implements Validator {

    @Autowired
    private AfiliadoService afiliadoService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Afiliado afiliado = (Afiliado) o;

        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "NotEmpty");
        if (afiliado.getNome().length() < 6 || afiliado.getNome().length() > 50) {
            errors.rejectValue("nome", "cadastro.nome");
        }

        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cpf", "NotEmpty");
        if (afiliadoService.findByEmail(afiliado.getEmail()) != null) {
            errors.rejectValue("email", "cadastro.cpf.duplicado");
        }

        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        if (!isEmailValid(afiliado.getEmail())) {
            errors.rejectValue("email", "cadastro.email");
        }

        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "senha", "NotEmpty");
        if (afiliado.getSenha().length() < 6 || afiliado.getSenha().length() > 32) {
            errors.rejectValue("senha", "cadastro.senha");
        }

        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmaSenha", "NotEmpty");
        if (!afiliado.getConfirmaSenha().equals(afiliado.getSenha())) {
            errors.rejectValue("confirmaSenha", "cadastro.senha.confirma");
        }
    }



    public static boolean isEmailValid(String email) {
        if ((email == null) || (email.trim().length() == 0))
            return false;

        String emailPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
