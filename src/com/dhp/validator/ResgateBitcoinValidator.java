package com.dhp.validator;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dhp.blockchain.Consultas;
import com.dhp.model.User;

@Component
public class ResgateBitcoinValidator implements Validator {

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {

		if (o instanceof Map){
			Map<String, String> dadosValidacao = (Map) o;
			int verificado = Integer.valueOf(dadosValidacao.get("verificado"));
			double valordoresgate = Double.valueOf(dadosValidacao.get("valordoresgate"));
			double saldoBTC = Double.valueOf(dadosValidacao.get("saldoBTC"));
			double totalResgatado = Double.valueOf(dadosValidacao.get("totalresgatado").replace(",", "."));
			String enderecobitcoin = dadosValidacao.get("enderecobitcoin");

			System.out.println("Resgate: " +valordoresgate);

			
			if (valordoresgate > saldoBTC)
			{
				errors.rejectValue("erros", "saldo.insuficiente");
			} else if (!Consultas.enderecoExiste(enderecobitcoin))
			{
				errors.rejectValue("erros", "bitcoin.endereconaoexiste");
			} else if(totalResgatado < 0.0001){
				errors.rejectValue("erros", "saldo.minimo");
			} else if (verificado == 0)
			{
				errors.rejectValue("erros", "cadastro.naoverificado2");
			}

			// else if (verificado == 0)
			//{
			//	errors.rejectValue("erros", "cadastro.naoverificado");
			//}
		
		}

	}
}