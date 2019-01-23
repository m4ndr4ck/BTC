package com.dhp.afiliados.controller;


import com.dhp.afiliados.model.Afiliado;
import com.dhp.afiliados.model.EnderecosBTCAfiliado;
import com.dhp.afiliados.model.ResgateBitcoinAfiliado;
import com.dhp.afiliados.service.AfiliadoService;
import com.dhp.afiliados.service.ResgateBitcoinAfiliadoService;
import com.dhp.afiliados.validator.AfiliadoValidator;
import com.dhp.blockchain.Carteiras;
import com.dhp.model.EnderecosBTC;
import com.dhp.model.Pagamentos;
import com.dhp.model.ResgateBitcoin;
import com.dhp.model.User;
import com.dhp.service.CustomUser;
import com.dhp.service.PagamentosService;
import com.dhp.service.SecurityService;
import com.dhp.service.UserService;
import com.dhp.util.AfiliadoLinks;
import com.dhp.util.GenericResponse;
import com.dhp.validator.RecuperarSenhaValidator;
import com.dhp.validator.ResgateBitcoinValidator;
import com.dhp.validator.SenhaValidator;
import com.dhp.web.BaseController;
import com.dhp.web.CotacaoControllerAdvice;
import com.dhp.web.dto.PasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class AfiliadoController {

    @Autowired
    private MessageSource messages;

    @Autowired
    private Environment env;

    @Autowired
    AfiliadoValidator afiliadoValidator;

    @Autowired
    AfiliadoService afiliadoService;

    @Autowired
    SecurityService securityService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PagamentosService pagamentosService;

    @Autowired
    private UserService userService;

    @Autowired
    private SenhaValidator senhaValidator;

    @Autowired
    private ResgateBitcoinAfiliadoService resgateBitcoinAfiliadoService;

    @Autowired
    private ResgateBitcoinValidator resgateBitcoinValidator;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private RecuperarSenhaValidator recuperarSenhaValidator;

    NumberFormat formatter = new DecimalFormat("0.00000000");
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    ResgateBitcoinAfiliado resgateBitcoinAfiliadoObj;

    public String getSaldo(Principal principal){

        long id = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
        Afiliado afiliado = afiliadoService.findById(id);
        return formatter.format(afiliado.getSaldo()).replace(".",",");

    }

    @RequestMapping(value = {"/afiliados"}, method = RequestMethod.GET)
    public String afiliados() {

        return "afiliados";
    }


    @RequestMapping(value = "/afiliado/cadastro", method = RequestMethod.GET)
    public String cadastro(Model model) {
        model.addAttribute("afiliadoForm", new Afiliado());

        return "afiliado-cadastro";
    }

    @Transactional
    @RequestMapping(value = "/afiliado/cadastro", method = RequestMethod.POST)
    public String cadastro(@ModelAttribute("afiliadoForm") Afiliado afiliadoForm, BindingResult bindingResult, Model model, Principal principal) {
        afiliadoValidator.validate(afiliadoForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "afiliado-cadastro";
        }
        afiliadoService.save(afiliadoForm);

        String ref = afiliadoService.findByEmail(afiliadoForm.getEmail()).getRef();
        long id = afiliadoService.findByEmail(afiliadoForm.getEmail()).getId();

        AfiliadoLinks  afiliadoLinks = new AfiliadoLinks();
        //String linkAfiliado = afiliadoLinks.geraLinkAfiliado(ref);
        String linkAfiliado = "https://btcmoedas.com.br/?ref="+ref.substring(ref.length() - 7);

        Afiliado afiliado = entityManager.find(Afiliado.class, id); //Consider em as JPA EntityManager
        afiliado.setLink(linkAfiliado);
        entityManager.merge(afiliado);

        model.addAttribute("linkAfiliado", linkAfiliado);

        securityService.autologin(afiliadoForm.getEmail(), afiliadoForm.getConfirmaSenha());

        model.asMap().clear();
        return "redirect:/afiliado/painel";
    }

    @RequestMapping(value = "/afiliado/painel", method = RequestMethod.GET)
    public String painel(Model model, Principal principal) {

        model.addAttribute("saldo", getSaldo(principal));

        long id = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();


        Afiliado afiliado = afiliadoService.findById(id);
        String ref = afiliado.getRef();
        String linkAfiliado = afiliado.getLink();

        model.addAttribute("linkAfiliado", linkAfiliado);
        model.addAttribute("ref", ref);

        return "afiliado-painel";
    }

    @RequestMapping(value = "/afiliado/login", method = RequestMethod.GET)
    public String afiliadologin(Model model, String error, String logout) {
        model.addAttribute("afiliadoForm", new User());
        if (error != null)
            model.addAttribute("error", "Login inválido, tente novamente.");
        return "afiliado-login";
    }

    @RequestMapping(value = {"/afiliado/painel/minha-conta"}, method = RequestMethod.GET)
    public String minhaConta(Principal principal, Model model) {

        model.addAttribute("saldo", getSaldo(principal));


        return "afiliado-minha-conta";
    }

    @RequestMapping(value = {"/afiliado/painel/meus-afiliados"}, method = RequestMethod.GET)
    public String meusAfiliados(Principal principal, Model model) {
        model.addAttribute("saldo", getSaldo(principal));

        long id = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();


        Afiliado afiliado = afiliadoService.findById(id);
        String ref = afiliado.getRef();
        List<User> user = userService.findByRef(ref);

        List<User> afiliadosList = new ArrayList<>();
        long comprasAprovadas = 0;

        if(user !=null){

            for(User afiliadoUser: user) {

                for (Pagamentos pagamento : pagamentosService.findPagamentos(afiliadoUser.getId())) {
                    if(pagamento.getStatus() == 3 || pagamento.getStatus() == 4)
                    comprasAprovadas++;

                }
                afiliadoUser.setComprasAprovadas(comprasAprovadas);
                afiliadosList.add(afiliadoUser);
                comprasAprovadas = 0;
            }
            model.addAttribute("Afiliados", afiliadosList);

        }


        return "afiliado-meus-afiliados";
    }

    @RequestMapping(value = {"/afiliado/painel/minhas-comissoes"}, method = RequestMethod.GET)
    public String minhasComissoes(Principal principal, Model model) {
        model.addAttribute("saldo", getSaldo(principal));

        long id = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();


        Afiliado afiliado = afiliadoService.findById(id);
        String ref = afiliado.getRef();
        List<User> user = userService.findByRef(ref);

        if(user !=null){
            List<Pagamentos> pagamentosAfiliados = new ArrayList<>();
            for(User afiliadoUser: user) {
                List<Pagamentos> pagamentos = null;
                pagamentos = pagamentosService.findPagamentos(afiliadoUser.getId());

                for (Pagamentos pagamento : pagamentos) {
                    pagamento.setDataHora(LocalDateTime.parse(
                            pagamento.getDataHora().toString().length() < 18 ?
                                    pagamento.getDataHora().toString() + ":01" :
                                    pagamento.getDataHora().toString()
                    ));
                    pagamento.setNome(afiliadoUser.getNome().split("\\s+")[0] +
                    " "+afiliadoUser.getNome().split("\\s+")[1]
                    );
                    pagamentosAfiliados.add(pagamento);
                }
            }
            model.addAttribute("Pagamentos", pagamentosAfiliados);

        }


        return "afiliado-minhas-comissoes";
    }

    @RequestMapping(value = {"/afiliado/painel/alterar-senha"}, method = RequestMethod.GET)
    public String alterarSenha(Model model, Principal principal) {
        model.addAttribute("saldo", getSaldo(principal));


        model.addAttribute("senhaAlterarForm", new PasswordDto());
        return "afiliado-alterar-senha";
    }

    @RequestMapping(value = {"/afiliado/painel/alterar-senha"}, method = RequestMethod.POST)
    public String alterarSenha(@ModelAttribute("senhaAlterarForm") PasswordDto senhaAlterarForm, BindingResult bindingResult, HttpServletRequest request, Model model, Principal principal) {
        model.addAttribute("saldo", getSaldo(principal));


        long id = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
        Afiliado afiliado = afiliadoService.findById(id);


        senhaValidator.validate(senhaAlterarForm, bindingResult);

        if (!afiliadoService.checkIfValidOldPassword(afiliado, senhaAlterarForm.getOldPassword())) {
            bindingResult.rejectValue("oldPassword", "message.invalidOldPassword");
            return "afiliado-alterar-senha";
        }

        if (bindingResult.hasErrors()) {
            return "afiliado-alterar-senha";
        }
        afiliadoService.changeUserPassword(afiliado, senhaAlterarForm.getNewPassword());
        String response = new GenericResponse(
                messages.getMessage("message.resetPasswordEmail", null,
                        request.getLocale())).getMessage();
        request.setAttribute("response", response);


        return "afiliado-alterar-senha";
    }

    @RequestMapping(value = {"/afiliado/painel/resgatar/carteira-bitcoin"}, method = RequestMethod.GET)
    public String resgatarCarteiraBitcoin(Model model, Principal principal) {
        model.addAttribute("saldo", getSaldo(principal));

        
        model.addAttribute("Resgate", new ResgateBitcoinAfiliado());

        long afiliadoId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
        Afiliado afiliado = afiliadoService.findById(afiliadoId);
        //Double saldoBTC = Consultas.verificaSaldo(afiliadoId);
        double saldoBTC = afiliado.getSaldo();
        BigDecimal bd = new BigDecimal(saldoBTC);
        DecimalFormat f = new DecimalFormat("0.00000000");
        String saldo = f.format(bd).replace('.', ',');
        model.addAttribute("saldoBTC", saldo);
        return "afiliado-resgatar-carteira-bitcoin";
    }

    @RequestMapping(value = {"/afiliado/painel/resgatar/carteira-bitcoin"}, method = RequestMethod.POST)
    public String resgatarCarteiraBitcoin(@ModelAttribute("Resgate") ResgateBitcoin resgateBitcoin, BindingResult bindingresult, Model model, Principal principal) {
        model.addAttribute("saldo", getSaldo(principal));


        long afiliadoId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
        Afiliado afiliado = afiliadoService.findById(afiliadoId);
        double bitcoinantesdafee = Double.valueOf(context.getParameter("bitcoins").replace(',', '.'));
        double valordoresgate = bitcoinantesdafee - BaseController.getFeeLocalBitcoins();
        //Double saldoBTC = Consultas.verificaSaldo(afiliadoId);
        double saldoBTC = afiliado.getSaldo();
        BigDecimal bd = new BigDecimal(valordoresgate);
        DecimalFormat f = new DecimalFormat("0.00000000");
        String totalresgatado = f.format(bd);
        int verificado = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getVerificado();


        Map<String, String> dadosValidacao = new HashMap<>();
        dadosValidacao.put("valordoresgate", context.getParameter("bitcoins").replace(',', '.'));
        dadosValidacao.put("saldoBTC", String.valueOf(saldoBTC).replace(',', '.'));
        dadosValidacao.put("enderecobitcoin", context.getParameter("enderecobitcoin"));
        dadosValidacao.put("verificado", String.valueOf(verificado));
        context.setAttribute("bitcoins", 0.0);
        model.addAttribute("bitcoins", context.getParameter("bitcoins").replace(',', '.'));
        resgateBitcoinValidator.validate(dadosValidacao, bindingresult);

        if (bindingresult.hasErrors()){
//  	  	Double saldoretorno = Consultas.verificaSaldo(afiliadoId);
            double saldoretorno = afiliado.getSaldo();

            model.addAttribute("saldoBTC", String.valueOf(saldoretorno).replace('.', ','));

            for (ObjectError error : bindingresult.getAllErrors()){
                if (!error.getCode().equals("typeMismatch"))
                    return "afiliado-resgatar-carteira-bitcoin";
            }
        }

        model.addAttribute("dataHora", LocalDateTime.now().format(format).toString());
        model.addAttribute("bitcoins", context.getParameter("bitcoins").replace('.', ','));
        model.addAttribute("totalresgatado", totalresgatado.replace('.', ','));
        model.addAttribute("feeBTC", formatter.format(BaseController.getFeeLocalBitcoins()).replace('.', ','));
        model.addAttribute("enderecobitcoin", context.getParameter("enderecobitcoin"));

        resgateBitcoinAfiliadoObj = new ResgateBitcoinAfiliado();
        resgateBitcoinAfiliadoObj.setAfiliadoid(afiliadoId);
        resgateBitcoinAfiliadoObj.setBitcoins(bitcoinantesdafee);
        resgateBitcoinAfiliadoObj.setTotalresgatado(totalresgatado);
        resgateBitcoinAfiliadoObj.setDataHora(LocalDateTime.parse(LocalDateTime.now().format(format).toString(), format));
        resgateBitcoinAfiliadoObj.setEnderecobitcoin(context.getParameter("enderecobitcoin"));

        return "afiliado-confirmar-resgate-bitcoin";
    }

    @RequestMapping(value = {"/afiliado/painel/resgatar/confirmar-resgate-bitcoin"}, method = RequestMethod.GET)
    public String confirmarResgateBitcoin(Model model, Principal principal) {
        model.addAttribute("saldo", getSaldo(principal));

        model.addAttribute("feeBTC", BaseController.getfeeLocalBitcoinscomvirgula());

        return "afiliado-confirmar-resgate-bitcoin";
    }

    @RequestMapping(value = {"/afiliado/painel/resgatar/confirmar-resgate-bitcoin"}, method = RequestMethod.POST)
    public String confirmarResgateBitcoinPost(Model model, Principal principal) {
        model.addAttribute("saldo", getSaldo(principal));


        //if (logout != null)
        //   model.addAttribute("message", "You have been logged out successfully.");

        long afiliadoId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
        Afiliado afiliado = afiliadoService.findById(( (CustomUser) ((Authentication)principal).getPrincipal() ).getId());

        resgateBitcoinAfiliadoService.save(resgateBitcoinAfiliadoObj);
        mailSender.send(constructResgateBitcoinEmail(afiliado, resgateBitcoinAfiliadoObj));


        return "redirect:/afiliado/painel/resgatar/finalizar-resgate-bitcoin";
    }

    @RequestMapping(value = {"/afiliado/painel/resgatar/finalizar-resgate-bitcoin"}, method = RequestMethod.GET)
    public String finalizarResgateBitcoin(Model model, Principal principal) {
        model.addAttribute("saldo", getSaldo(principal));


        return "afiliado-finalizar-resgate-bitcoin";
    }

    @RequestMapping(value = {"/afiliado/painel/meus-resgates-carteira-bitcoin"}, method = RequestMethod.GET)
    public String meusResgatesCarteriaBitcoin(Principal principal, Model model) {

        model.addAttribute("saldo", getSaldo(principal));

        long afiliadoid = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
        List<ResgateBitcoinAfiliado> resgates = resgateBitcoinAfiliadoService.findResgates(afiliadoid);
        model.addAttribute("Resgates", resgates);

        return "afiliado-meus-resgates-carteira-bitcoin";
    }

    @RequestMapping(value = {"/afiliado/recuperar-senha"}, method = RequestMethod.GET)
    public String esqueciaMinhaSenha(Principal principal, Model model) {
        model.addAttribute("afiliadoForm", new Afiliado());
        return "afiliado-senha-recuperar";
    }


    @RequestMapping(value = "/afiliado/recuperar-senha", method = RequestMethod.POST)
    public String resetPassword(@ModelAttribute("afiliadoForm") Afiliado userForm, BindingResult bindingResult, HttpServletRequest request,
                                @RequestParam("email") String email) throws Exception {
        Afiliado afiliado = afiliadoService.findByEmail(email);

        recuperarSenhaValidator.validate(email, bindingResult);
        if (bindingResult.hasErrors()) {
            return "afiliado-senha-recuperar";
        }

        if (afiliado == null) {
            throw new Exception();
        }

        String token = UUID.randomUUID().toString();
        afiliadoService.createPasswordResetTokenForUser(afiliado, token);
        mailSender.send(constructResetTokenEmail(getAppUrl(request),
                request.getLocale(), token, afiliado));
        String response = new GenericResponse(
                messages.getMessage("message.resetPasswordEmail", null,
                        request.getLocale())).getMessage();
        request.setAttribute("response", response);

        return "afiliado-senha-recuperar";
    }

    @RequestMapping(value = "/afiliado/mudar-senha", method = RequestMethod.GET)
    public String showChangePasswordPage(final Locale locale, final Model model, @RequestParam("email") final String email, @RequestParam("token") final String token) {
        final String result = afiliadoService.validatePasswordResetToken(email, token);
        if (result != null) {
            model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
            return "redirect:/afiliado/login?lang=" + locale.getLanguage();
        }
        return "redirect:/afiliado/atualizar-senha?lang=" + locale.getLanguage();
    }

    @RequestMapping(value = {"/afiliado/atualizar-senha"}, method = RequestMethod.GET)
    public String atualizarSenha(Principal principal, Model model) {
        model.addAttribute("senhaForm", new PasswordDto());

        return "afiliado-senha-atualizar";
    }

    @RequestMapping(value = "/afiliado/atualizar-senha", method = RequestMethod.POST)
    public String savePassword(@ModelAttribute("senhaForm") PasswordDto senhaForm,
                               BindingResult bindingResult, Locale locale, HttpServletRequest request) {

        senhaValidator.validate(senhaForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "afiliado-senha-atualizar";
        }

        Afiliado afiliado =
                (Afiliado) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();

        afiliadoService.changeUserPassword(afiliado, senhaForm.getNewPassword());
        String response = new GenericResponse(
                messages.getMessage("message.resetPasswordSuc", null, locale)).getMessage();
        request.setAttribute("response", response);
        return "afiliado-senha-atualizar";
    }

    @RequestMapping(value = "/afiliado/painel/material-divulgacao", method = RequestMethod.GET)
    public String materialDivulgacao(Model model, Principal principal) {

        model.addAttribute("saldo", getSaldo(principal));


        long id = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();


        Afiliado afiliado = afiliadoService.findById(id);
        String ref = afiliado.getRef();
        String linkAfiliado = afiliado.getLink();

        model.addAttribute("linkAfiliado", linkAfiliado);


        return "afiliado-material-divulgacao";
    }

    @RequestMapping(value = "/afiliado/painel/material-divulgacao/template-email", method = RequestMethod.GET)
    public String templateEmail(Model model, Principal principal) {

        model.addAttribute("saldo", getSaldo(principal));


        long id = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();


        Afiliado afiliado = afiliadoService.findById(id);
        String ref = afiliado.getRef();
        String linkAfiliado = afiliado.getLink();

        model.addAttribute("linkAfiliado", linkAfiliado);
        model.addAttribute("nome", afiliado.getNome());


        return "afiliado-template-email";
    }



    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final Afiliado afiliado) {
        final String url = contextPath + "/afiliado/mudar-senha?email=" + afiliado.getEmail() + "&token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Recuperar senha", message + " \r\n\n" + url, afiliado);
    }

    private SimpleMailMessage constructEmail(String subject, String body, Afiliado afiliado) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(afiliado.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private MimeMessage constructResgateBitcoinEmail(final Afiliado afiliado, ResgateBitcoinAfiliado resgatebitcoin) {
        String nome = afiliado.getNome().split("\\s+")[0];
        String mensagem ="<div style='font-size: 12px; line-height: 14px; font-family: Ubuntu, Tahoma, Verdana, Segoe, sans-serif;' data-mce-style='font-size: 12px; line-height: 14px; font-family: Ubuntu, Tahoma, Verdana, Segoe, sans-serif;'>"+
                "<p style='font-size: 14px; line-height: 16px;' data-mce-style='font-size: 14px; line-height: 16px;'>"+
                "Ol&aacute; <strong>"+nome+"</strong>,"+
                "<br />"+
                "<br />"+
                "Sua solicita&ccedil;&atilde;o de resgate para carteira bitcoin foi feita com sucesso."+
                "<br />"+
                "<br />"+
                "Vamos fazer a seguinte transfer&ecirc;ncia:"+
                "<br />"+
                "<br />"+
                "Valor do Saque: "+"<b>"+formatter.format(resgatebitcoin.getBitcoins())+" BTC"+"</b>"+
                "<br />"+
                "Taxa de Transfer&ecirc;ncia: "+"<b>"+ BaseController.getfeeLocalBitcoinscomvirgula()+" BTC</b>"+
                "<br />"+
                "Total Sacado: "+"<b>"+resgatebitcoin.getTotalresgatado()+" BTC"+"</b>"+
                "<br />"+
                "Endere&ccedil;o bitcoin: "+"<b>"+resgatebitcoin.getEnderecobitcoin()+"</b>"+
                "<br />"+
                "<br />"+
                "O prazo para transfer&ecirc;ncia de bitcoin &eacute; de <b>2</b>&nbsp;at&eacute; <b>24</b>&nbsp;horas.</p>"+
                "<p style='font-size: 14px; line-height: 16px;' data-mce-style='font-size: 14px; line-height: 16px;'>"+
                "Caso tenha qualquer d&uacute;vida basta responder esta e-mail ou ent&atilde;o nos contatar pelo n&uacute;mero <b>41</b> <b>99973-7902</b>."+
                "<br />"+
                "<br />Atenciosamente,"+
                "<br />BTC Moedas"+
                "</p>"+
                "</div>";
        return constructEmailHtml("[BTC Moedas]Seu resgate para carteira bitcoin", mensagem, afiliado);
    }

    private MimeMessage constructEmailHtml(String subject, String mensagem, Afiliado afiliado) {
        MimeMessage email = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(email, true);
            helper.setSubject(subject);
            helper.setText(mensagem, true);
            helper.setTo(afiliado.getEmail());
            helper.setFrom("admin@btcmoedas.com.br");
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return email;
    }

    private static String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }


}
