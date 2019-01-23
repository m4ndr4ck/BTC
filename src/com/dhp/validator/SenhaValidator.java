package com.dhp.validator;

import org.springframework.stereotype.Component;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.dhp.web.dto.PasswordDto;

@Component
public class SenhaValidator implements Validator {
	
	 @Override
	    public boolean supports(Class<?> aClass) {
	        return PasswordDto.class.equals(aClass);
	    }

	    @Override
	    public void validate(Object o, Errors errors) {
	    	PasswordDto passwordDto = (PasswordDto) o;

	        if (!passwordDto.getConfirmPassword().equals(passwordDto.getNewPassword())) {
	            errors.rejectValue("confirmPassword", "message.confirmPassword");
	        }
	    }

}
