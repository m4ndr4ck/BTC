package com.dhp.validator;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.dhp.model.Pagamentos;
import com.dhp.model.User;

@Component
public class PagamentosValidator implements Validator {

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {

;
		if (o instanceof Map){
			Map<String, Object> dadosValidacao = (Map) o;
			int verificado = Integer.valueOf((String)dadosValidacao.get("verificado"));
			BigDecimal valorPagamento = (BigDecimal)dadosValidacao.get("valorPagamento");
			BigDecimal minimo = new BigDecimal("20");
			BigDecimal maximo = new BigDecimal("50");


			if (valorPagamento.compareTo(minimo) < 0)
			{
				errors.rejectValue("erros", "pagamentos.valorminimo");
			} else if (valorPagamento.compareTo(maximo) > 0)
			{
				errors.rejectValue("erros", "pagamentos.valormaximo");
			} else if (verificado == 0)
			{
				errors.rejectValue("erros", "cadastro.naoverificado");
			}
		
		}

	}
}