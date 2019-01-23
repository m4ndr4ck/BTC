package com.dhp.validator;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.dhp.model.Role;
import com.dhp.model.User;
import com.dhp.service.UserService;
import com.dhp.web.dto.PasswordDto;

@Component
public class AdminValidator implements Validator {
	@Autowired
	private UserService userService;
	
	 @Override
	    public boolean supports(Class<?> aClass) {
	        return PasswordDto.class.equals(aClass);
	    }

	    @Override
	    public void validate(Object o, Errors errors) {
	    	User user = (User) o;
	    	
	
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cpf", "NotEmpty");
	    	if (!user.getCpf().equals("05864388904")) {
				errors.rejectValue("cpf", "erro.generico");
			}
	    	
	    	if (user.getCpf() == null) {
				errors.rejectValue("cpf", "erro.generico");
			}
	    }

}
