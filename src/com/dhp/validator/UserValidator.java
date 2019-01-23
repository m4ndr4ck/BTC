package com.dhp.validator;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.dhp.model.User;
import com.dhp.service.UserService;
import com.dhp.util.Helpers;

@Component
public class UserValidator implements Validator {

	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		User user = (User) o;

		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "NotEmpty");
		if (user.getNome().length() < 6 || user.getNome().length() > 50) {
			errors.rejectValue("nome", "cadastro.nome");
		}

		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cpf", "NotEmpty");
		if (userService.findByCpf(user.getCpf()) != null) {
			errors.rejectValue("cpf", "cadastro.cpf.duplicado");
		}

		if (!Helpers.isCPF(user.getCpf())) {
			errors.rejectValue("cpf", "cadastro.cpf.invalido");
		}

		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "celular", "NotEmpty");
		if (user.getCelular().length() < 8 || user.getCelular().length() > 16) {
			errors.rejectValue("celular", "cadastro.celular");
		}

		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
		if (!isEmailValid(user.getEmail())) {
			errors.rejectValue("email", "cadastro.email");
		}

		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "senha", "NotEmpty");
		if (user.getSenha().length() < 6 || user.getSenha().length() > 32) {
			errors.rejectValue("senha", "cadastro.senha");
		}

		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmaSenha", "NotEmpty");
		if (!user.getConfirmaSenha().equals(user.getSenha())) {
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
