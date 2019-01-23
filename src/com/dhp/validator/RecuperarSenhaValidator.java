package com.dhp.validator;

import com.dhp.afiliados.service.AfiliadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.dhp.model.User;
import com.dhp.service.UserService;

@Component
public class RecuperarSenhaValidator implements Validator {

	@Autowired
	private UserService userService;

	@Autowired
	private AfiliadoService afiliadoService;

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		String cpfouEmail = (String) o;
		if(!cpfouEmail.matches("(.*)[a-zA-Z](.*)")) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cpf", "NotEmpty");
			if (!cpfouEmail.isEmpty() && userService.findByCpf(cpfouEmail) == null) {
				errors.rejectValue("cpf", "recuperarsenha.cpf.naoexiste");
			}
		}
		if(cpfouEmail.matches("(.*)[a-zA-Z](.*)")) {

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
			if (!cpfouEmail.isEmpty() && afiliadoService.findByEmail(cpfouEmail) == null) {
				errors.rejectValue("email", "recuperarsenha.email.naoexiste");
			}
		}

	}

}
