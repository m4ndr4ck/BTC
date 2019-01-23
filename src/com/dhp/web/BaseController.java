package com.dhp.web;

import com.dhp.util.Helpers;
import org.hibernate.type.BigDecimalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.dhp.web.CotacaoControllerAdvice.FeeLocalBitcoins;

public class BaseController {

    static NumberFormat formatter = new DecimalFormat("0.00000000");
    static Locale locale = new Locale("pt", "BR");
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    static NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    ///public static double getCotacaoCOMPRA(){return Helpers.geraCotacaoBitvalor();}
    public static double getCotacaoCOMPRA(){return CotacaoControllerAdvice.geraCotacaoWall();}
    public static String getCotacaoCOMPRAcomBRL(){return    currencyFormatter.format(getCotacaoCOMPRA());}
    public static double getCotacaoVENDA(){return CotacaoControllerAdvice.getCotacaoVENDA();}

    public static double getTaxaBTC(){return 0.0003;}
    public static double getTaxaBTC(BigDecimal valorPagamento, double bitcoins) {
        double taxa = 0;
        BigDecimal cem = new BigDecimal("100");
        BigDecimal duzentos = new BigDecimal("200");
        BigDecimal trezentos = new BigDecimal("300");
        BigDecimal quatrocentos = new BigDecimal("400");
        BigDecimal quinhetos = new BigDecimal("500");

        if (valorPagamento.compareTo(cem) >= 0 && valorPagamento.compareTo(duzentos) < 0) {
            taxa = bitcoins * 0.35;
        } else if (valorPagamento.compareTo(duzentos) >= 0 && valorPagamento.compareTo(trezentos) < 0){
            taxa = bitcoins * 0.25;
        }else if (valorPagamento.compareTo(trezentos)>=0){
        taxa = bitcoins * 0.10;
        }



        //return taxa;
        return 0.0003;
    }


    public static String getTaxaBTCcomvirgula(){return formatter.format(getTaxaBTC()).replace(".", ",");}

    public static double getFeeLocalBitcoins(){return FeeLocalBitcoins();}
    public static String getfeeLocalBitcoinscomvirgula(){return formatter.format(getFeeLocalBitcoins()).replace(".", ",");}

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    protected HttpServletRequest context;

    @Autowired
    protected JavaMailSender mailSender;

}
