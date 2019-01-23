package com.dhp.validator;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.dhp.model.User;

@Component
public class ResgateContaBancariaValidator implements Validator {

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "valorResgate", "NotEmpty");

		if(o instanceof Boolean){
			if((Boolean) o){
				errors.rejectValue("erros", "cadastro.semcontabancaria");
			}
		}

		if (o instanceof Map){
			Map<String, BigDecimal> saldos = (Map) o;
			BigDecimal saldoBRL = saldos.get("saldoBRL");
			BigDecimal valorResgate = saldos.get("valorResgate");
			int verificado = saldos.get("verificado").intValue();


			if (valorResgate.compareTo(saldoBRL) > 0)
			{
				errors.rejectValue("valorResgate", "saldo.insuficiente");
			} else if (verificado == 0)
			{
				errors.rejectValue("erros", "cadastro.naoverificado");
			}
		}

	}
}
