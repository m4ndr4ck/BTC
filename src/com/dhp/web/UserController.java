package com.dhp.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhp.afiliados.model.Afiliado;
import com.dhp.afiliados.service.AfiliadoService;
import com.dhp.pagamentos.CheckoutTransparente;
import com.dhp.service.*;
import com.dhp.util.PaymentWithPayPalServlet;
import com.dhp.util.SendBitcoin;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.multipart.MultipartFile;

import com.dhp.blockchain.Carteiras;
import com.dhp.model.ContaBancaria;
import com.dhp.model.EnderecosBTC;
import com.dhp.model.Pagamentos;
import com.dhp.model.ResgateBitcoin;
import com.dhp.model.ResgateContaBancaria;
import com.dhp.model.UploadedFile;
import com.dhp.model.User;
import com.dhp.pagamentos.PagSeg;
import com.dhp.util.GenericResponse;
import com.dhp.util.Helpers;
import com.dhp.validator.AdminValidator;
import com.dhp.validator.PagamentosValidator;
import com.dhp.validator.RecuperarSenhaValidator;
import com.dhp.validator.ResgateBitcoinValidator;
import com.dhp.validator.ResgateContaBancariaValidator;
import com.dhp.validator.SenhaValidator;
import com.dhp.validator.UserValidator;
import com.dhp.web.dto.PasswordDto;
import com.google.protobuf.TextFormat.ParseException;

import static com.dhp.web.BaseController.*;

@Controller
public class UserController {
	
	
	private static double getCotacaoCOMPRA(){return BaseController.getCotacaoCOMPRA();}
	private static double getCotacaoVENDA(){return BaseController.getCotacaoVENDA();}


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
    private MessageSource messages;

    @Autowired
    private Environment env;
	
	@Autowired
	private JavaMailSender mailSender;
	
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private EnderecosBTCService enderecosBTCService;
    
    @Autowired
    private PagamentosService pagamentosService;
    
    @Autowired
    private ResgateBitcoinService resgateBitcoinService;
    
    @Autowired 
    private ContaBancariaService contaBancariaService;

    @Autowired
    private ResgateContaBancariaService resgateContaBancariaService;
    
    @Autowired
    private AdminService adminService;

    @Autowired
    private UserValidator userValidator;
    
    @Autowired
    private SenhaValidator senhaValidator;
    
    @Autowired
    private ResgateContaBancariaValidator resgateContaBancariaValidator;
    
    @Autowired
    private ResgateBitcoinValidator resgateBitcoinValidator;
    
    @Autowired
    private RecuperarSenhaValidator recuperarSenhaValidator;
    
    @Autowired
    private PagamentosValidator pagamentosValidator; 
    
    @Autowired
    private AdminValidator adminValidator;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    AfiliadoService afiliadoService;

    @Autowired
    private HttpServletRequest context;

    Pagamentos pagamentosObj;
    ResgateBitcoin resgateBitcoinObj;
    ResgateContaBancaria resgateContaBancariaObj;
    
    Locale locale = new Locale("pt", "BR");
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    NumberFormat formatter = new DecimalFormat("0.00000000");

    String refAfiliado;

    @Bean
    public RequestContextFilter requestContextListener(){
        return new RequestContextFilter();
    }


    @Transactional
	@RequestMapping(value = "/retorno", method = {RequestMethod.POST, RequestMethod.GET}, headers = "content-type=application/x-www-form-urlencoded", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody String retorno(@RequestParam("notificationCode") String notificationCode, @RequestParam("notificationType") String notificationType) throws Exception {
    	
    	String refCode = Helpers.notificaPagSeg(notificationCode, false);
    	String referencia = refCode.replace("BTCM", "");
    	String status = Helpers.notificaPagSeg(Integer.parseInt(referencia), false);
    	String pagseguroid = Helpers.notificaPagSeg(Integer.parseInt(referencia), true);
    	System.out.println("Notification code: " + notificationCode);
    	System.out.println("Ref code: " + refCode);
    	System.out.println("Status: " + status);
    	System.out.println("Pagseguro ID: " + pagseguroid);
    	

    	//Atualiza status de pagamento
    	Pagamentos pagamento = entityManager.find(Pagamentos.class, Long.parseLong(referencia)); //Consider em as JPA EntityManager
        if(pagamento.getStatus() != 3) {
            pagamento.setStatus(Long.parseLong(status));
            if (pagamento.getPagseguroid() == null) pagamento.setPagseguroid(pagseguroid);
            entityManager.merge(pagamento);

            //Atualiza saldo
            if (Integer.valueOf(status) == 3) { // 3/4 = Aprovado
                EnderecosBTC cliente = entityManager.find(EnderecosBTC.class, pagamento.getClienteid()); //Consider em as JPA EntityManager

                double saldoAtual = cliente.getSaldo();
                double novoSaldo = saldoAtual + pagamento.getBitcoins();
                cliente.setSaldo(novoSaldo);
                entityManager.merge(cliente);
            }
        }
    	
		return null;
    }
	
	@RequestMapping(value = "/retorno", method = RequestMethod.GET)
    public String retorno() {
    	System.out.println("Notificacao Pagseguro: " + context.getParameter("notificationCode"));
    	return "redirect:/painel";
    }

    @RequestMapping(value = {"/", "/"}, method = RequestMethod.GET)
    public String home() {

        return "home";
    }

    @RequestMapping(value = {"/"}, params = {"ref"}, method = RequestMethod.GET)
    public String homeRef(@RequestParam(value="ref", required = false) String ref, Model model) {
        System.out.println("Ref: "+ref);
        if(ref!=null) {
            Afiliado afiliado = afiliadoService.findByRefFinal(ref);
            if (afiliado != null) {
                refAfiliado = afiliado.getRef();
            }
            else refAfiliado = "noref";
        } else{
            refAfiliado = "noref";
        }

        return "redirect:/";
    }

    @RequestMapping(value = {"/como-funciona"}, method = RequestMethod.GET)
    public String comoFunciona(Model model) {

        model.addAttribute("bitcoinFEE", getFeeLocalBitcoins());
        model.addAttribute("taxaBTC", formatter.format(getTaxaBTC()).replace(".", ","));


        return "home-como-funciona";
    }
    @RequestMapping(value = {"/simulador"}, method = RequestMethod.GET)
    public String simulador(Model model) {

        model.addAttribute("cotacaoCompraSemBRL", String.valueOf(getCotacaoCOMPRA()));
        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));
        model.addAttribute("taxaBTC", getTaxaBTC());
        model.addAttribute("taxaBTCcomvirgula", formatter.format(getTaxaBTC()).replace(".", ","));
        model.addAttribute("feeLocalBitcoinscomvirgula", getfeeLocalBitcoinscomvirgula());
        model.addAttribute("feeLocalBitcoins", getFeeLocalBitcoins());

        //model.asMap().clear();
        //return "redirect:/";


        return "home-simulador";
    }
    @RequestMapping(value = {"/comprar-bitcoin"}, method = RequestMethod.GET)
    public String comprarBitcoin(Model model) {

        model.addAttribute("cotacaoCompraSemBRL", String.valueOf(getCotacaoCOMPRA()));
        model.addAttribute("cotacaoCOMPRA", getCotacaoCOMPRAcomBRL());

        return "home-comprar-bitcoin";
    }
    @RequestMapping(value = {"/contato"}, method = RequestMethod.GET)
    public String contatoGet() {

        return "home-contato";
    }
    @RequestMapping(value = {"/contato"}, method = RequestMethod.POST)
    public String contatoPost() {

        String nome = context.getParameter("nome");
        String email = context.getParameter("email");
        String mensagem = context.getParameter("mensagem");
        mailSender.send(constructContato(nome, email, mensagem  ));
        context.setAttribute("sucesso", true);

        return "home-contato";
    }
    @RequestMapping(value = {"/sobre"}, method = RequestMethod.GET)
    public String sobre() {

        return "home-sobre-nos";
    }
    @RequestMapping(value = {"/comprar-bitcoin-com-cartao-de-credito"}, method = RequestMethod.GET)
    public String comprarBitcoinComCartaoDeCredito() {

        return "home-comprar-bitcoin-com-cartao-de-credito";
    }


    @RequestMapping(value = "/cadastro", method = RequestMethod.GET)
    public String registration(Model model) {
        User user = new User();

        //if (!refAfiliado.isEmpty())
            user.setRef(refAfiliado);

        model.addAttribute("userForm", user);
        model.addAttribute("referencia", refAfiliado);

        return "cadastro";
    }

    @RequestMapping(value = "/cadastro/{ref}", method = RequestMethod.GET)
    public String registrationRef(@PathVariable(value="ref") String ref, Model model) {
        Afiliado afiliado = afiliadoService.findByRefFinal(ref);

        String referencia = afiliado.getRef();

        User user = new User();
        if (!referencia.isEmpty())
        user.setRef(referencia);

        model.addAttribute("userForm", user);
        model.addAttribute("ref", referencia);

        return "cadastro";
    }

    @RequestMapping(value = "/cadastro", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model, Principal principal) {
        userValidator.validate(userForm, bindingResult);



        if (bindingResult.hasErrors()) {
            return "cadastro";
        }

        //char[] possibleCharacters = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%*")).toCharArray();
        //String apikey = RandomStringUtils.random( 12, 0, possibleCharacters.length-1, false, false, possibleCharacters, new SecureRandom() );
        //String encryptedApiKey = Crypto.encrypt(apikey);

        //serForm.setApikey(encryptedApiKey);
        userForm.setCelular(userForm.getCelular()
                .replace("(", "")
                .replace(")", "")
                .replace("-", "")
                .replace(" ", ""));
        userService.save(userForm);

        //CreateWalletResponse enderecoBTC = Carteiras.criarCarteira(apikey, userForm.getNome(), userForm.getEmail());
        int clienteId = Integer.valueOf(userService.findByCpf(userForm.getCpf()).getId().toString());
        String enderecoBTC = Carteiras.criarEndereco(clienteId);
        String cpf = userForm.getCpf();
        
        //enderecosBTCService.save(clienteId, enderecoBTC.getAddress(), enderecoBTC.getIdentifier());
        enderecosBTCService.save(clienteId, enderecoBTC);

        securityService.autologin(userForm.getCpf(), userForm.getConfirmaSenha());

    	File dir = new File("/home/dhp/" + cpf);
    	if (!dir.exists()) dir.mkdirs();

        EnderecosBTC enderecoBTCpage = enderecosBTCService.findByClienteid((long)clienteId);
        //Double saldoBTC = Consultas.verificaSaldo(clienteId);
        double saldoBTC = enderecoBTCpage.getSaldo();
        double saldoBRL = getCotacaoVENDA() * saldoBTC;

        model.addAttribute("enderecoBTC", enderecoBTCpage.getEndereco());

        BigDecimal bd = new BigDecimal(saldoBTC);
        DecimalFormat f = new DecimalFormat("0.00000000");
        String saldo = f.format(bd);

        model.addAttribute("saldoBTC", saldo);
        model.addAttribute("saldoBRL", currencyFormatter.format(saldoBRL));

        int verificado = userService.findById(clienteId).getVerificado();
        model.addAttribute("verificado", verificado);

        model.asMap().clear();
        return "redirect:/painel";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Login inválido, tente novamente.");

        //if (logout != null)
        //   model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }
    
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
            logger.info("logout ok");
        }
        return "login";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }
    
    @RequestMapping(value = "/admlogin", method = RequestMethod.GET)
    public String admin(Model model, String error, String logout) {
    	model.addAttribute("adminForm", new User());
        //if (logout != null)
        //   model.addAttribute("message", "You have been logged out successfully.");

        return "admlogin";
    }

    
	/**@RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adm(Model model) {
        model.addAttribute("userForm", new User());

        return "admin";
    }*/

    @RequestMapping(value = {"/painel"}, method = RequestMethod.GET)
    public String painel(Model model, Principal principal) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        String nome = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getNome();
        model.addAttribute("message", nome);
        long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();

        EnderecosBTC enderecoBTC = enderecosBTCService.findByClienteid(clienteId);
        //Double saldoBTC = Consultas.verificaSaldo(clienteId);
        double saldoBTC = enderecoBTC.getSaldo();
        double saldoBRL = 0;
        
        model.addAttribute("enderecoBTC", enderecoBTC.getEndereco());
        
        BigDecimal bd = new BigDecimal(saldoBTC);
        DecimalFormat f = new DecimalFormat("0.00000000");
        String saldo = f.format(bd);
        
        model.addAttribute("saldoBTC", saldo);
        model.addAttribute("saldoBRL", currencyFormatter.format(saldoBRL));
        
    	int verificado = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getVerificado();
    	model.addAttribute("verificado", verificado);
        
        return "painel";
    }
    
    @RequestMapping(value = {"/painel/comprar-bitcoin"}, method = RequestMethod.GET)
    public String comprar(@ModelAttribute("Pagamento") Pagamentos pagamento, BindingResult bindingresult, Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        model.addAttribute("cotacaoCompraSemBRL", String.valueOf(getCotacaoCOMPRA()));

    	
        return "comprar";
    }
    
    @RequestMapping(value = {"/painel/comprar-bitcoin"}, method = RequestMethod.POST)
    public String comprar(@ModelAttribute("Pagamento") Pagamentos pagamento, BindingResult bindingresult, Model model, Principal principal) throws ParseException, java.text.ParseException {



        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        model.addAttribute("Pagamento", pagamento);
    	model.addAttribute("cotacaoCompraSemBRL", String.valueOf(getCotacaoCOMPRA()));
    	
        long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
    	int verificado = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getVerificado();
    	BigDecimal valorPagamento = Helpers.parse(context.getParameter("valorPagamento"), locale);
    	
    	//BigDecimal valorPagamentoComTaxaBD = valorPagamento.multiply(new BigDecimal(1.0999)).add(new BigDecimal(5));
        //BigDecimal valorPagamentoComTaxaBD = valorPagamento.add(new BigDecimal(5));
        //Sem taxa
        BigDecimal valorPagamentoComTaxaBD = valorPagamento.add(new BigDecimal(0));


        String valorPagamentoComTaxa = currencyFormatter.format(Double.valueOf(valorPagamentoComTaxaBD.toString()));
    	
    	
    	Map<String, Object> dadosValidacao = new HashMap<>();
    	dadosValidacao.put("verificado", String.valueOf(verificado));
    	dadosValidacao.put("valorPagamento", valorPagamento);

    	pagamentosValidator.validate(dadosValidacao, bindingresult);
    	
  	  	if (bindingresult.hasErrors()){
  	  	for (ObjectError error : bindingresult.getAllErrors()){
  	  		if (!error.getCode().equals("typeMismatch"))
  	  		return "comprar";
  	  		}
        }
        double bitcoins = Double.valueOf(context.getParameter("bitcoins").replace(",", "."));
        double taxa = getTaxaBTC(valorPagamento, bitcoins);
        double bitcoinsComTaxa = bitcoins - taxa;

    	model.addAttribute("cotacao", pagamento.getCotacao());
    	model.addAttribute("dataHora", LocalDateTime.now().format(format).toString());
    	model.addAttribute("diaMes", LocalDateTime.now().getDayOfMonth());
        model.addAttribute("taxaBTCcomvirgula", formatter.format(taxa).replace(".", ","));
    	model.addAttribute("bitcoins2",
                formatter.format(bitcoinsComTaxa).replace(".", ","));
        model.addAttribute("bitcoins", context.getParameter("bitcoins"));
        model.addAttribute("valorPagamento", valorPagamentoComTaxa);
//    	model.addAttribute("tipoPagamento", context.getParameter("tipoPagamento"));

        Locale locBrazil = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getInstance(locBrazil);

    	pagamentosObj = new Pagamentos();
    	pagamentosObj.setCotacao(currencyFormatter.format(getCotacaoCOMPRA()));
    	pagamentosObj.setClienteid(clienteId);
    	pagamentosObj.setDataHora(LocalDateTime.parse(LocalDateTime.now().format(format).toString(), format));
    	pagamentosObj.setBitcoins(bitcoins - taxa);
    	pagamentosObj.setTaxa(taxa);
    	//pagamentosObj.setTipoPagamento(Integer.valueOf(context.getParameter("tipoPagamento")));
        pagamentosObj.setTipoPagamento(2);
        pagamentosObj.setValorPagamento((Double.valueOf(nf.parse(context.getParameter("valorPagamento").replace("R$ ", "")).doubleValue())));
    	pagamentosObj.setValorPagamentoComTaxa(valorPagamentoComTaxa);
    	
    		
    	return "confirmar";
    }

    @RequestMapping(value = {"/painel/checkout"}, method = RequestMethod.GET)
    public String checkout(Model model) {

        double valorPagamento = pagamentosObj.getValorPagamento();
        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        return "checkout";
    }

    @RequestMapping(value = {"/painel/checkout/cartao"}, method = RequestMethod.GET)
    public String checkoutCartao(Model model, Principal principal) {

        if(pagamentosObj.getValorPagamento()>200) {
            model.asMap().clear();
            return "redirect:/painel";
        }

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();
        User user = userService.findByCpf(cpf);
        String nome = user.getNome();
        String sessaoid = PagSeg.criaSessao();

        model.addAttribute("nome", nome);
        model.addAttribute("cpf", cpf);
        model.addAttribute("sessaoid", sessaoid);

        return "checkout-cartao";
    }

    @Transactional
    @RequestMapping(value = {"/painel/checkout/cartao"}, method = RequestMethod.POST)
    public @ResponseBody String checkoutCartaoEfetiva(Model model, Principal principal) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        //Salva pedido na base de dados
        pagamentosService.save(pagamentosObj);

        String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();
        User user = userService.findByCpf(cpf);

        String nome = context.getParameter("nome");
        String nascimento = context.getParameter("nascimento");
        String email = user.getEmail();
        String celDDD = user.getCelular().substring(0, 2);
        String celNum = user.getCelular().substring(2);
        String cep = context.getParameter("cep").replaceAll("-", "");
        String logradouro = context.getParameter("logradouro");
        String numero = context.getParameter("numero");
        String complemento = context.getParameter("complemento");
        String bairro = context.getParameter("bairro").equals("") ? "Centro" : context.getParameter("bairro");
        String cidade = context.getParameter("cidade");
        String uf = context.getParameter("uf");
        String tokencartao = context.getParameter("tokencartao");
        String senderhash = context.getParameter("senderhash");

        String bitcoin = formatter.format(pagamentosObj.getBitcoins()).replace('.',',');
        String preco = String.valueOf(pagamentosObj.getValorPagamento());
        String referencia = "BTC"+pagamentosObj.getId();

        Map<String, String> dadosComprador = new HashMap<>();
        dadosComprador.put("nome", nome);
        dadosComprador.put("cpf", cpf);
        dadosComprador.put("nascimento", nascimento);
        dadosComprador.put("email", email);
        dadosComprador.put("celDDD", celDDD);
        dadosComprador.put("celNum", celNum);
        dadosComprador.put("cep", cep);
        dadosComprador.put("logradouro", logradouro);
        dadosComprador.put("numero", numero);
        dadosComprador.put("complemento", complemento);
        dadosComprador.put("bairro", bairro);
        dadosComprador.put("cidade", cidade);
        dadosComprador.put("uf", uf);
        dadosComprador.put("tokencartao", tokencartao);
        dadosComprador.put("senderhash", senderhash);
        dadosComprador.put("bitcoin", bitcoin);
        dadosComprador.put("preco", preco);
        dadosComprador.put("referencia", referencia);
        String response = CheckoutTransparente.pagamentoCartao(dadosComprador);

        //Envia e-mail para o cliente
        mailSender.send(constructComprarBitcoinEmail(user, pagamentosObj));
        //Envia e-mail para admin
        mailSender.send(constructComprarBitcoinEmailAdmin(user, pagamentosObj));

        // Se tudo der certo, salva pedido na base de dados
        if (response.equals("erro")){
            return "erro";
        }else{

        return "ok";
        }
    }



    @RequestMapping(value = {"/painel/checkout/debito"}, method = RequestMethod.GET)
    public String checkoutDebito(Model model, Principal principal) {

        if(pagamentosObj.getValorPagamento()>200) {
            model.asMap().clear();
            return "redirect:/painel";
        }

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();
        User user = userService.findByCpf(cpf);
        String nome = user.getNome();

        model.addAttribute("nome", nome);
        model.addAttribute("cpf", cpf);

        return "checkout-debito";
    }

    @Transactional
    @RequestMapping(value = {"/painel/checkout/debito"}, method = RequestMethod.POST)
    public @ResponseBody String checkoutDebitoEfetiva(Model model, Principal principal) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        //Salva pedido na base de dados
        pagamentosService.save(pagamentosObj);

        String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();
        User user = userService.findByCpf(cpf);

        String nome = context.getParameter("nome");
        String nascimento = context.getParameter("nascimento");
        String email = user.getEmail();
        String celDDD = user.getCelular().substring(0, 2);
        String celNum = user.getCelular().substring(2);
        String cep = context.getParameter("cep").replaceAll("-", "");
        String logradouro = context.getParameter("logradouro");
        String numero = context.getParameter("numero");
        String complemento = context.getParameter("complemento");
        String bairro = context.getParameter("bairro").equals("") ? "Centro" : context.getParameter("bairro");
        String cidade = context.getParameter("cidade");
        String uf = context.getParameter("uf");
        String banco = context.getParameter("banco");
        String senderhash = context.getParameter("senderhash");

        String bitcoin = formatter.format(pagamentosObj.getBitcoins()).replace('.',',');
        String preco = String.valueOf(pagamentosObj.getValorPagamento());
        String referencia = "BTC"+pagamentosObj.getId();

        Map<String, String> dadosComprador = new HashMap<>();
        dadosComprador.put("nome", nome);
        dadosComprador.put("cpf", cpf);
        dadosComprador.put("nascimento", nascimento);
        dadosComprador.put("email", email);
        dadosComprador.put("celDDD", celDDD);
        dadosComprador.put("celNum", celNum);
        dadosComprador.put("cep", cep);
        dadosComprador.put("logradouro", logradouro);
        dadosComprador.put("numero", numero);
        dadosComprador.put("complemento", complemento);
        dadosComprador.put("bairro", bairro);
        dadosComprador.put("cidade", cidade);
        dadosComprador.put("uf", uf);
        dadosComprador.put("banco", banco);
        dadosComprador.put("senderhash", senderhash);
        dadosComprador.put("bitcoin", bitcoin);
        dadosComprador.put("preco", preco);
        dadosComprador.put("referencia", referencia);
        String linkPagamento = CheckoutTransparente.pagamentoDebito(dadosComprador); //Link pagamento

        //Envia e-mail para o cliente
        mailSender.send(constructComprarBitcoinEmail(user, pagamentosObj));
        //Envia e-mail para admin
        mailSender.send(constructComprarBitcoinEmailAdmin(user, pagamentosObj));

        // Se tudo der certo, salva pedido na base de dados
        //if (!response.equals("erro")) pagamentosService.save(pagamentosObj);

        return linkPagamento;

    }

    @RequestMapping(value = {"/painel/checkout/boleto"}, method = RequestMethod.GET)
    public String checkoutBoleto(Model model, Principal principal) {

        if(pagamentosObj.getValorPagamento()>200) {
            model.asMap().clear();
            return "redirect:/painel";
        }

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();
        User user = userService.findByCpf(cpf);
        String nome = user.getNome();

        model.addAttribute("nome", nome);
        model.addAttribute("cpf", cpf);

        return "checkout-boleto";
    }

    @Transactional
    @RequestMapping(value = {"/painel/checkout/boleto"}, method = RequestMethod.POST)
    public @ResponseBody String checkoutBoletoEfetiva(Model model, Principal principal) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        //Salva pedido na base de dados
        pagamentosService.save(pagamentosObj);

        String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();
        User user = userService.findByCpf(cpf);

        String nome = context.getParameter("nome");
        String nascimento = context.getParameter("nascimento");
        String email = user.getEmail();
        String celDDD = user.getCelular().substring(0, 2);
        String celNum = user.getCelular().substring(2);
        String cep = context.getParameter("cep").replaceAll("-", "");
        String logradouro = context.getParameter("logradouro");
        String numero = context.getParameter("numero");
        String complemento = context.getParameter("complemento");
        String bairro = context.getParameter("bairro").equals("") ? "Centro" : context.getParameter("bairro");
        String cidade = context.getParameter("cidade");
        String uf = context.getParameter("uf");
        String banco = context.getParameter("banco");
        String senderhash = context.getParameter("senderhash");

        String bitcoin = formatter.format(pagamentosObj.getBitcoins()).replace('.',',');
        String preco = String.valueOf(pagamentosObj.getValorPagamento());
        String referencia = "BTC"+pagamentosObj.getId();

        Map<String, String> dadosComprador = new HashMap<>();
        dadosComprador.put("nome", nome);
        dadosComprador.put("cpf", cpf);
        dadosComprador.put("nascimento", nascimento);
        dadosComprador.put("email", email);
        dadosComprador.put("celDDD", celDDD);
        dadosComprador.put("celNum", celNum);
        dadosComprador.put("cep", cep);
        dadosComprador.put("logradouro", logradouro);
        dadosComprador.put("numero", numero);
        dadosComprador.put("complemento", complemento);
        dadosComprador.put("bairro", bairro);
        dadosComprador.put("cidade", cidade);
        dadosComprador.put("uf", uf);
        dadosComprador.put("banco", banco);
        dadosComprador.put("senderhash", senderhash);
        dadosComprador.put("bitcoin", bitcoin);
        dadosComprador.put("preco", preco);
        dadosComprador.put("referencia", referencia);
        String linkPagamento = CheckoutTransparente.pagamentoBoleto(dadosComprador); //Link pagamento

        //Envia e-mail para o cliente
        mailSender.send(constructComprarBitcoinEmail(user, pagamentosObj));
        //Envia e-mail para admin
        mailSender.send(constructComprarBitcoinEmailAdmin(user, pagamentosObj));

        // Se tudo der certo, salva pedido na base de dados
        //if (!response.equals("erro")) pagamentosService.save(pagamentosObj);

        return linkPagamento;

    }

    @RequestMapping(value = {"/painel/checkout/transferencia"}, method = RequestMethod.GET)
    public String checkoutTransferencia(Model model, Principal principal) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();
        User user = userService.findByCpf(cpf);
        String nome = user.getNome();
        String valorPagamento = currencyFormatter.format(pagamentosObj.getValorPagamento());

        model.addAttribute("nome", nome);
        model.addAttribute("cpf", cpf);
        model.addAttribute("valorPagamento", valorPagamento);

        return "checkout-transferencia";
    }

    @Transactional
    @RequestMapping(value = {"/painel/checkout/transferencia"}, method = RequestMethod.POST)
    public @ResponseBody void checkoutTransferenciaEfetiva(Model model, Principal principal) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        //Salva pedido na base de dados
        pagamentosService.save(pagamentosObj);

        String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();
        User user = userService.findByCpf(cpf);

        //Envia e-mail para o cliente
        mailSender.send(constructComprarBitcoinEmail(user, pagamentosObj));
        //Envia e-mail para admin
        mailSender.send(constructComprarBitcoinEmailAdmin(user, pagamentosObj));

        // Se tudo der certo, salva pedido na base de dados
        //if (!response.equals("erro")) pagamentosService.save(pagamentosObj);

    }

    @RequestMapping(value = {"/painel/checkout/paypal"}, method = RequestMethod.GET)
    public String paypal(Model model, HttpServletResponse response, Principal principal) {




        String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();
        User user = userService.findByCpf(cpf);

        String valorPagamento = String.valueOf(pagamentosObj.getValorPagamento());
        String bitcoin = formatter.format(pagamentosObj.getBitcoins());
        PaymentWithPayPalServlet paymentWithPayPalServlet = new PaymentWithPayPalServlet();
        Payment payment = paymentWithPayPalServlet.createPayment(context, response, valorPagamento, bitcoin);

        //Salva pedido na base de dados
        pagamentosObj.setPagseguroid(payment.getId());
        pagamentosService.save(pagamentosObj);

        String linkPagamento = "";
        for(Links link :payment.getLinks())
            if(link.getHref().contains("express"))
            linkPagamento =link.getHref();

        //Envia e-mail para o cliente
        mailSender.send(constructComprarBitcoinEmail(user, pagamentosObj));
        //Envia e-mail para admin
        mailSender.send(constructComprarBitcoinEmailAdmin(user, pagamentosObj));


        return "redirect:"+linkPagamento;
    }


    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "/painel/paypal")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, Model model){
        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        try {
            APIContext apiContext = new APIContext("AXcYXWKFSE_oDldOgFQB927EY4CRo_Pz0eWP-oDj9EkxXcneejXKsH94QIBvYyynmaoHF-Ovyq01dQOg", "EEuXcQb0CozpkqseeizPsJcsvC1a3L2kIepeMSlIP5psDtfA86aQklMMYRBxCjrbh-tcS_XJXOhRmr5j", "live");

            String linkPagamento = "";
            Payment payment = new Payment();
            payment.setId(paymentId);
            PaymentExecution paymentExecute = new PaymentExecution();
            paymentExecute.setPayerId(payerId);
            Payment returnP = payment.execute(apiContext, paymentExecute);

            //Obtem pagamento pelo paymentid
            Pagamentos pagamento = pagamentosService.findByPagseguroid(paymentId);

            //Atualiza status do pagamento e ret
               if(returnP.getState().equals("approved")){
                   EnderecosBTC cliente = entityManager.find(EnderecosBTC.class, pagamento.getClienteid()); //Consider em as JPA EntityManager
                   double saldoAtual = cliente.getSaldo();
                   double novoSaldo = saldoAtual + pagamento.getBitcoins();
                   cliente.setSaldo(novoSaldo);
                   pagamento.setStatus(3L);
                   entityManager.merge(cliente);
                return "paypal-success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/painel";
    }

        @RequestMapping(value = {"/painel/checkout/pagseguro"}, method = RequestMethod.GET)
    public String pagseguro(Model model, HttpServletResponse response, Principal principal) {

        int tipoPagamento = pagamentosObj.getTipoPagamento();

        //Salva pedido na base de dados
        pagamentosService.save(pagamentosObj);

        String cpf = ((CustomUser) ((Authentication) principal).getPrincipal()).getCpf();
        User user = userService.findByCpf(cpf);
        String nome = user.getNome();
        String email = user.getEmail();
        long referencia = pagamentosObj.getId();
        String urlRedir = "";

            urlRedir = PagSeg.NovoPagamento(
        String.valueOf(pagamentosObj.getValorPagamento()),
        formatter.format(pagamentosObj.getBitcoins()).replace('.',','),
        nome,
        email,
        cpf,
        referencia
        );

        //Envia e-mail para o cliente
        mailSender.send(constructComprarBitcoinEmail(user, pagamentosObj));
        //Envia e-mail para admin
        mailSender.send(constructComprarBitcoinEmailAdmin(user, pagamentosObj));

        return "redirect:"+urlRedir;
    }


    @RequestMapping(value = {"/painel/comprar-bitcoin/confirmar"}, method = RequestMethod.GET)
    public String confirmar(Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        return "confirmar";
    }
    
    @RequestMapping(value = {"/painel/comprar-bitcoin/confirmar"}, method = RequestMethod.POST)
    public String confirmar(@ModelAttribute("Pagamento") Pagamentos pagamento, BindingResult result, Model model, Principal principal) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        int tipoPagamento =  pagamentosObj.getTipoPagamento();

        //Salva pedido na base de dados
        //pagamentosService.save(pagamentosObj);

        String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();
        User user = userService.findByCpf(cpf);
        String nome = user.getNome();
        String email = user.getEmail();
        String urlRedir = "";
        
        /**if(tipoPagamento==2){
        urlRedir = PagSeg.NovoPagamento(
        		String.valueOf(pagamentosObj.getValorPagamento()),
                formatter.format(pagamentosObj.getBitcoins()).replace('.',','),
        		nome,
        		email,
        		cpf,
        		referencia
        		);
        }*/
        if(tipoPagamento==2){
            model.addAttribute("valorPagamento", pagamentosObj.getValorPagamento());
            return "checkout";
        }

        if(tipoPagamento==1) {
            //Salva pedido na base de dados
            pagamentosService.save(pagamentosObj);
            long referencia = pagamentosObj.getId();
            urlRedir = PagSeg.NovaAssinatura(
        			String.valueOf(pagamentosObj.getValorPagamento()),
        			String.valueOf(formatter.format(pagamentosObj.getBitcoins())),
            		nome,
            		email,
            		cpf,
                    referencia
            		);
        }
        //Envia e-mail para o cliente
    	mailSender.send(constructComprarBitcoinEmail(user, pagamentosObj));
        //Envia e-mail para admin
        mailSender.send(constructComprarBitcoinEmailAdmin(user, pagamentosObj));
        return "redirect:"+urlRedir;
    }
    
    @RequestMapping(value = {"/painel/resgatar"}, method = RequestMethod.GET)
    public String resgatar(Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        return "resgatar";
    }

    @RequestMapping(value = {"/painel/resgatar/conta-bancaria"}, method = RequestMethod.GET)
    public String resgatarContaBancaria(Principal principal, Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        model.addAttribute("Resgatar", new ResgateContaBancaria());
    	
    	long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
    	double cotacaoVenda = getCotacaoVENDA();
        
        EnderecosBTC enderecoBTC = enderecosBTCService.findByClienteid(clienteId);
    	//Double saldoBTC = Consultas.verificaSaldo(clienteId);
        double saldoBTC = enderecoBTC.getSaldo();
        double saldoBRL = cotacaoVenda * saldoBTC;
        model.addAttribute("saldoBRL", currencyFormatter.format(saldoBRL));
        model.addAttribute("cotacaoVENDA", currencyFormatter.format(cotacaoVenda));

        model.asMap().clear();
        return "redirect:/painel";
        //return "resgatar-conta-bancaria";
    }
    
    @RequestMapping(value = {"/painel/resgatar/conta-bancaria"}, method = RequestMethod.POST)
    public String resgatarContaBancaria(@ModelAttribute("Resgatar") ResgateContaBancaria resgateContaBancaria, BindingResult bindingresult, Principal principal, Model model) throws ParseException, java.text.ParseException {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));


        long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
    	List<ContaBancaria> contacadastrada = (List<ContaBancaria>) contaBancariaService.findByClienteid(clienteId);
    	ContaBancaria conta = contacadastrada.size() > 0 ? contacadastrada.get(0) : null;
    	double cotacaoVenda = getCotacaoVENDA();
    	String valoResgateform = context.getParameter("valorResgate");
        EnderecosBTC enderecoBTC = enderecosBTCService.findByClienteid(clienteId);
    	//Double saldoBTC = Consultas.verificaSaldo(clienteId);
        double saldoBTC = enderecoBTC.getSaldo();
        double saldoBRL = cotacaoVenda * saldoBTC;
        model.addAttribute("saldoBRL", currencyFormatter.format(saldoBRL));

        if (conta == null){
            resgateContaBancariaValidator.validate(true, bindingresult);
            return "resgatar-conta-bancaria";
        }
    	
    	if (valoResgateform.isEmpty()){
    		resgateContaBancariaValidator.validate(valoResgateform, bindingresult);
    		return "resgatar-conta-bancaria";
    	}

        BigDecimal valorResgate = Helpers.parse(context.getParameter("valorResgate"), locale);
        int verificado = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getVerificado();

        Map<String, BigDecimal> saldos = new HashMap<>();
        saldos.put("saldoBRL", BigDecimal.valueOf(saldoBRL));
        saldos.put("valorResgate", valorResgate);
        saldos.put("verificado", BigDecimal.valueOf(verificado));
        
        resgateContaBancariaValidator.validate(saldos, bindingresult);
        
        if(bindingresult.hasErrors()){
        	return "resgatar-conta-bancaria";
        }
    	
    	String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();
    	String nome = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getNome();
    	model.addAttribute("dataHora", LocalDateTime.now().format(format).toString());
    	model.addAttribute("contaCadastrada", contacadastrada);
    	model.addAttribute("valorResgate", context.getParameter("valorResgate"));
    	model.addAttribute("cpf", cpf);
    	model.addAttribute("nome", nome);
    	
    	resgateContaBancariaObj = new ResgateContaBancaria();
    	resgateContaBancariaObj.setValorResgate(context.getParameter("valorResgate").replace(',', '.').replace("R$ ", ""));
    	resgateContaBancariaObj.setClienteid(clienteId);
    	resgateContaBancariaObj.setBanco(conta.getBanco());
    	resgateContaBancariaObj.setTipodeconta(conta.getTipodeconta());
    	resgateContaBancariaObj.setAgencia(conta.getAgencia());
    	resgateContaBancariaObj.setConta(conta.getConta());
    	resgateContaBancariaObj.setDadosadicionais(conta.getDadosadicionais());
    	
    	return "confirmar-resgate-conta-bancaria";
    	
    }
    
    @RequestMapping(value = {"/painel/resgatar/confirmar-resgate-conta-bancaria"}, method = RequestMethod.GET)
    public String confirmarResgateContaBancaria(Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        return "confirmar-resgate-conta-bancaria";
    }
    
    @RequestMapping(value = {"/painel/resgatar/confirmar-resgate-conta-bancaria"}, method = RequestMethod.POST)
    public String confirmarResgateContaBancaria(@ModelAttribute("Resgate") ResgateContaBancaria resgateContaBancaria, BindingResult result, Principal principal, Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
    	User user = userService.findByCpf(( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf());
    	List<ContaBancaria> contacadastrada = (List<ContaBancaria>) contaBancariaService.findByClienteid(clienteId);
    	ContaBancaria conta = contacadastrada.get(0);
    	String valorResgate = resgateContaBancariaObj.getValorResgate();
    	resgateContaBancariaObj.setValorResgate(valorResgate);
    	resgateContaBancariaObj.setClienteid(clienteId);
    	resgateContaBancariaObj.setBanco(conta.getBanco());
    	resgateContaBancariaObj.setTipodeconta(conta.getTipodeconta());
    	resgateContaBancariaObj.setAgencia(conta.getAgencia());
    	resgateContaBancariaObj.setConta(conta.getConta());
    	resgateContaBancariaObj.setDadosadicionais(conta.getDadosadicionais());
    	
   
    	resgateContaBancariaService.save(resgateContaBancariaObj);
    	mailSender.send(constructResgateContaBancariaEmail(user, resgateContaBancariaObj));

        return "finalizar-resgate-conta-bancaria";
    }
    
    @RequestMapping(value = {"/painel/resgatar/finalizar-resgate-conta-bancaria"}, method = RequestMethod.GET)
    public String finalizarResgateContaBancaria(Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        return "finalizar-resgate-conta-bancaria";
    }
    
    @RequestMapping(value = {"/painel/resgatar/carteira-bitcoin"}, method = RequestMethod.GET)
    public String resgatarCarteiraBitcoin(Model model, Principal principal) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        model.addAttribute("Resgate", new ResgateBitcoin());
    	
        long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
        EnderecosBTC enderecoBTC = enderecosBTCService.findByClienteid(clienteId);
        //Double saldoBTC = Consultas.verificaSaldo(clienteId);
        double saldoBTC = enderecoBTC.getSaldo();
        BigDecimal bd = new BigDecimal(saldoBTC);
        DecimalFormat f = new DecimalFormat("0.00000000");
        String saldo = f.format(bd).replace('.', ',');
        model.addAttribute("saldoBTC", saldo);
        model.addAttribute("fee", BaseController.getfeeLocalBitcoinscomvirgula());
        return "resgatar-carteira-bitcoin";
    }
    
    @RequestMapping(value = {"/painel/resgatar/carteira-bitcoin"}, method = RequestMethod.POST)
    public String resgatarCarteiraBitcoin(@ModelAttribute("Resgate") ResgateBitcoin resgateBitcoin, BindingResult bindingresult, Model model, Principal principal) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
    	double bitcoinantesdafee = Double.valueOf(context.getParameter("bitcoins").replace(',', '.'));
    	double valordoresgate = bitcoinantesdafee - getFeeLocalBitcoins();
    	EnderecosBTC enderecoBTC = enderecosBTCService.findByClienteid(clienteId);
    	//Double saldoBTC = Consultas.verificaSaldo(clienteId);
        double saldoBTC = enderecoBTC.getSaldo();
        BigDecimal bd = new BigDecimal(valordoresgate);
        DecimalFormat f = new DecimalFormat("0.00000000");
        String totalresgatado = f.format(bd);
        int verificado = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getVerificado();


        Map<String, String> dadosValidacao = new HashMap<>();
    	dadosValidacao.put("valordoresgate", context.getParameter("bitcoins").replace(',', '.'));
    	dadosValidacao.put("totalresgatado", totalresgatado);
        dadosValidacao.put("saldoBTC", String.valueOf(saldoBTC).replace(',', '.'));
    	dadosValidacao.put("enderecobitcoin", context.getParameter("enderecobitcoin"));
        dadosValidacao.put("verificado", String.valueOf(verificado));
        context.setAttribute("bitcoins", 0.0);
    	model.addAttribute("bitcoins", context.getParameter("bitcoins").replace(',', '.'));
    	resgateBitcoinValidator.validate(dadosValidacao, bindingresult);
    	   
  	  	if (bindingresult.hasErrors()){
            BigDecimal bd2 = new BigDecimal(saldoBTC);
            String saldo = f.format(bd2).replace('.', ',');
            model.addAttribute("saldoBTC", saldo);
  	  	for (ObjectError error : bindingresult.getAllErrors()){
  	  		if (!error.getCode().equals("typeMismatch")) {
                model.addAttribute("fee", BaseController.getfeeLocalBitcoinscomvirgula());
                return "resgatar-carteira-bitcoin";
                }
  	  		}
        }

    	model.addAttribute("dataHora", LocalDateTime.now().format(format).toString());
    	model.addAttribute("bitcoins", context.getParameter("bitcoins").replace('.', ','));
    	model.addAttribute("totalresgatado", totalresgatado.replace('.', ','));
        model.addAttribute("feeBTC", formatter.format(getFeeLocalBitcoins()).replace('.', ','));
        model.addAttribute("enderecobitcoin", context.getParameter("enderecobitcoin"));
    	  	
    	resgateBitcoinObj = new ResgateBitcoin();
    	resgateBitcoinObj.setClienteid(clienteId);
    	resgateBitcoinObj.setBitcoins(bitcoinantesdafee);
    	resgateBitcoinObj.setTotalresgatado(totalresgatado);
    	resgateBitcoinObj.setDataHora(LocalDateTime.parse(LocalDateTime.now().format(format).toString(), format));
    	resgateBitcoinObj.setEnderecobitcoin(context.getParameter("enderecobitcoin"));
    	
    	return "confirmar-resgate-bitcoin";
    }
    
    @RequestMapping(value = {"/painel/resgatar/confirmar-resgate-bitcoin"}, method = RequestMethod.GET)
    public String confirmarResgateBitcoin(Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));
        model.addAttribute("feeBTC", getFeeLocalBitcoins());

        return "confirmar-resgate-bitcoin";
    }

    @Transactional
    @RequestMapping(value = {"/painel/resgatar/confirmar-resgate-bitcoin"}, method = RequestMethod.POST)
    public String confirmarResgateBitcoin(Model model, Principal principal) throws Exception {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        String enderecoBTC = resgateBitcoinObj.getEnderecobitcoin();
        double valorResgate = Double.valueOf(resgateBitcoinObj.getBitcoins());
        double feeBTC = getFeeLocalBitcoins();
        String totalResgatado = String.valueOf(valorResgate - feeBTC);

        BigDecimal bd = new BigDecimal(totalResgatado);
        DecimalFormat f = new DecimalFormat("0.00000000");
        String totalresgatadoFormatado = f.format(bd).replace(",", ".");


        SendBitcoin sendBitcoin = new SendBitcoin();
        String result = sendBitcoin.send(enderecoBTC, totalresgatadoFormatado);

        if(result.equals("200")){
            //Atualiza saldo
            EnderecosBTC cliente = entityManager.find(EnderecosBTC.class, resgateBitcoinObj.getClienteid()); //Consider em as JPA EntityManager
            double saldoAtual = cliente.getSaldo();
            double novoSaldo = saldoAtual - valorResgate;
            cliente.setSaldo(novoSaldo);
            entityManager.merge(cliente);
        } else{
            return "erro-resgate-bitcoin";
        }
    	
    	long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
    	User user = userService.findByCpf(( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf());
    	
    	resgateBitcoinService.save(resgateBitcoinObj);
    	mailSender.send(constructResgateBitcoinEmail(user, resgateBitcoinObj));
        //Envia e-mail para admin
        mailSender.send(constructResgateBitcoinEmailAdmin(user, resgateBitcoinObj));
    	
    	
    	return "redirect:/painel/resgatar/finalizar-resgate-bitcoin";
    }
    
    @RequestMapping(value = {"/painel/resgatar/finalizar-resgate-bitcoin"}, method = RequestMethod.GET)
    public String finalizarResgateBitcoin(Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        return "finalizar-resgate-bitcoin";
    }
    
    @RequestMapping(value = {"/painel/minha-conta"}, method = RequestMethod.GET)
    public String minhaConta(Principal principal, Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        int verificado = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getVerificado();
    	model.addAttribute("verificado", verificado);
        return "minha-conta";
    }
    
    @RequestMapping(value = {"/painel/minhas-compras"}, method = RequestMethod.GET)
    public String minhasCompras(Principal principal, Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
    	List<Pagamentos> pagamentos = pagamentosService.findPagamentos(clienteId);
    	for(Pagamentos pagamento : pagamentos){
            pagamento.setDataHora(LocalDateTime.parse(
                    pagamento.getDataHora().toString().length() < 18 ?
                            pagamento.getDataHora().toString() +":01":
                            pagamento.getDataHora().toString()
            ));
        }
    	model.addAttribute("Pagamentos", pagamentos);
    	

        return "minhas-compras";
    }
    
    @RequestMapping(value = {"/painel/meus-resgates"}, method = RequestMethod.GET)
    public String meusResgates(Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        return "meus-resgates";
    }
    // ADICIONAR CONTA BANCARIA - INICIO //
    @RequestMapping(value = {"/painel/adicionar-conta-bancaria"}, method = RequestMethod.GET)
    public String adiconarContaBancaria(Principal principal, Model model, ContaBancaria contabancaria) {

    	model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

    	Long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
    	List<ContaBancaria> contacadastrada = (List<ContaBancaria>) contaBancariaService.findByClienteid(clienteId);
    	model.addAttribute("contaCadastrada", contacadastrada);
    	
        return "adicionar-conta-bancaria";
    }
    
    @RequestMapping(value = {"/painel/adicionar-conta-bancaria"}, method = RequestMethod.POST)
    public String adiconarContaBancaria(@ModelAttribute("ContaBancaria") ContaBancaria contabancaria, Principal principal, Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
    	contabancaria.setClienteid(clienteId);
    	contaBancariaService.save(contabancaria);
    	
        return "redirect:/painel/adicionar-conta-bancaria";
    }
    
    @RequestMapping(value = {"/painel/remover-conta-bancaria"}, method = RequestMethod.POST)
    public String removerContaBancaria(Principal principal, Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));


        long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
    	contaBancariaService.delete(clienteId);
    	
        return "redirect:/painel/adicionar-conta-bancaria";
    }
    // ADICIONAR CONTA BANCARIA - FIM //
    
    
    @RequestMapping(value = {"/painel/alterar-senha"}, method = RequestMethod.GET)
    public String alterarSenha(Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        model.addAttribute("senhaAlterarForm", new PasswordDto());
        return "alterar-senha";
    }
    
    @RequestMapping(value = {"/painel/alterar-senha"}, method = RequestMethod.POST)
    public String alterarSenha(@ModelAttribute("senhaAlterarForm") PasswordDto senhaAlterarForm, BindingResult bindingResult, HttpServletRequest request, Model model, Principal principal) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();
    	User user = userService.findByCpf(cpf);
    	
    	
    	senhaValidator.validate(senhaAlterarForm, bindingResult);
    	
    	if (!userService.checkIfValidOldPassword(user, senhaAlterarForm.getOldPassword())) {
    		bindingResult.rejectValue("oldPassword", "message.invalidOldPassword");
    		return "alterar-senha";
        }
    	
  	  	if (bindingResult.hasErrors()) {
            return "alterar-senha";
        }
    	userService.changeUserPassword(user, senhaAlterarForm.getNewPassword());
    	String response = new GenericResponse(
    			messages.getMessage("message.resetPasswordEmail", null, 
    					request.getLocale())).getMessage();
    	request.setAttribute("response", response);

    	
        return "alterar-senha";
    }
    
    @RequestMapping(value = {"/painel/solicitar-verificacao"}, method = RequestMethod.POST)
    public String solicitarVerificao(Principal principal, Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        User user = userService.findByCpf(( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf());
        String nome = user.getNome() ;
        String email = user.getEmail();


        mailSender.send(constructSolicitarVerificao(nome, email));
    	
        return "solicitar-verificacao";
    }
    
    @RequestMapping(value = {"/painel/enviar-documentos"}, method = RequestMethod.GET)
    public String enviarDocumentos(Principal principal, Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();
    	List<String> documentosAnexados = Helpers.listarArquivos("/home/dhp/" + cpf);
    	model.addAttribute("documentosAnexados", documentosAnexados);
    	
        return "enviar-documentos";
    }
    
    @RequestMapping(value = "/painel/enviar-documentos", method = RequestMethod.POST)
	public String enviarDocumentos(@ModelAttribute("Documentos") UploadedFile uploadedFile, Principal principal) {

    	MultipartFile file = uploadedFile.getFile();
    	String name = uploadedFile.getFile().getOriginalFilename();
    	String cpf = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getCpf();

    	if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				//File dir = new File(rootPath + File.separator + "tmpFiles");
				File dir = new File("/home/dhp/" + cpf);
				
				//if (!dir.exists())
				//	dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				logger.info("Server File Location="
						+ serverFile.getAbsolutePath());
				System.out.println("You successfully uploaded file=" + name);
				return "enviar-documentos";
				
			} catch (Exception e) {
				System.out.println("Erro ao fazer upload do arquivo" + e.getStackTrace());
				return "enviar-documentos";
				}
		} else {
			System.out.println("You failed to upload " + name + " because the file was empty.");
			return "enviar-documentos";
		}
	}
    
    @RequestMapping(value = {"/painel/meus-resgates-conta-bancaria"}, method = RequestMethod.GET)
    public String meusResgatesContaBancaria(Principal principal, Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
    	List<ResgateContaBancaria> resgates = resgateContaBancariaService.findResgates(clienteId);
    	model.addAttribute("Resgates", resgates);
    	
        return "meus-resgates-conta-bancaria";
    }
    
    @RequestMapping(value = {"/painel/meus-resgates-carteira-bitcoin"}, method = RequestMethod.GET)
    public String meusResgatesCarteriaBitcoin(Principal principal, Model model) {

        model.addAttribute("cotacaoCOMPRA", currencyFormatter.format(getCotacaoCOMPRA()));

        long clienteId = ( (CustomUser) ((Authentication)principal).getPrincipal() ).getId();
    	List<ResgateBitcoin> resgates = resgateBitcoinService.findResgates(clienteId);
    	model.addAttribute("Resgates", resgates);
    	
        return "meus-resgates-carteira-bitcoin";
    }
    
    
    @RequestMapping(value = {"/recuperar-senha"}, method = RequestMethod.GET)
    public String esqueciaMinhaSenha(Principal principal, Model model) {
    	model.addAttribute("userForm", new User());
        return "senha-recuperar";
    }
    
    
    @RequestMapping(value = "/recuperar-senha", method = RequestMethod.POST)
    public String resetPassword(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, HttpServletRequest request, 
    		@RequestParam("cpf") String cpf) throws Exception {
    	User user = userService.findByCpf(cpf);
    	
    	recuperarSenhaValidator.validate(cpf, bindingResult);
    	if (bindingResult.hasErrors()) {
            return "senha-recuperar";
        }
    	
    	if (user == null) {
    		throw new Exception();
    	}
    	
    	String token = UUID.randomUUID().toString();
    	userService.createPasswordResetTokenForUser(user, token);
    	mailSender.send(constructResetTokenEmail(getAppUrl(request), 
    			request.getLocale(), token, user));
    	String response = new GenericResponse(
    			messages.getMessage("message.resetPasswordEmail", null, 
    					request.getLocale())).getMessage();
    	request.setAttribute("response", response);
    	
    	return "senha-recuperar";
}
    
    @RequestMapping(value = "/mudar-senha", method = RequestMethod.GET)
    public String showChangePasswordPage(final Locale locale, final Model model, @RequestParam("cpf") final String cpf, @RequestParam("token") final String token) {
        final String result = userService.validatePasswordResetToken(cpf, token);
        if (result != null) {
            model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
            return "redirect:/login?lang=" + locale.getLanguage();
        }
        return "redirect:/atualizar-senha?lang=" + locale.getLanguage();
    }
    
    @RequestMapping(value = {"/atualizar-senha"}, method = RequestMethod.GET)
    public String atualizarSenha(Principal principal, Model model) {
    	model.addAttribute("senhaForm", new PasswordDto());
    	
        return "senha-atualizar";
    }
    
    @RequestMapping(value = "/atualizar-senha", method = RequestMethod.POST)
    public String savePassword(@ModelAttribute("senhaForm") PasswordDto senhaForm, 
    		BindingResult bindingResult, Locale locale, HttpServletRequest request) {
    	
    	senhaValidator.validate(senhaForm, bindingResult);
    	
    	  if (bindingResult.hasErrors()) {
              return "senha-atualizar";
          }
    	
        User user = 
          (User) SecurityContextHolder.getContext()
                                      .getAuthentication().getPrincipal();
         
        userService.changeUserPassword(user, senhaForm.getNewPassword());
        String response = new GenericResponse(
          messages.getMessage("message.resetPasswordSuc", null, locale)).getMessage();
        request.setAttribute("response", response);
        return "senha-atualizar";
    }
    
    
    // ============== ADMIN AREA ============ //
    
    @RequestMapping(value = {"/admin/clientes"}, method = RequestMethod.GET)
    public String adminClientes(Model model) {
    	List<User> clientes = adminService.retrieveAllCustomers();
    	model.addAttribute("Clientes", clientes);
    	double saldoBTCTotal = 0;
    	List<EnderecosBTC> enderecosBTC = enderecosBTCService.findAll();

    	for(EnderecosBTC endereco : enderecosBTC)
            saldoBTCTotal += endereco.getSaldo();

        NumberFormat formatter = new DecimalFormat("0.00000000");

        model.addAttribute("SaldoBTCTotal", formatter.format(saldoBTCTotal));


        return "admin-clientes";
    }
    
    @RequestMapping(value = {"/admin/clientes/{ID}"}, method = RequestMethod.GET)
    public String adminClienteHome(@PathVariable(value="ID") String id, Model model) {
    	User cliente = userService.findById(Long.valueOf(id));
    	String cpf = cliente.getCpf();
    	int verificado = cliente.getVerificado();

        NumberFormat formatter = new DecimalFormat("0.00000000");

        Double saldoBTC = enderecosBTCService.findByClienteid(Long.valueOf(id)).getSaldo();
        model.addAttribute("SaldoBTC", formatter.format(saldoBTC));

        List<Pagamentos> compras = pagamentosService.findPagamentos(Long.valueOf(id));
        for(Pagamentos pagamento : compras){
            pagamento.setDataHora(LocalDateTime.parse(
                    pagamento.getDataHora().toString().length() < 18 ?
                            pagamento.getDataHora().toString() +":01":
                            pagamento.getDataHora().toString()
            ));
        }
        model.addAttribute("Compras", compras);

        List<ResgateBitcoin> resgates = resgateBitcoinService.findResgates(Long.valueOf(id));
        for(ResgateBitcoin resgate : resgates){
            resgate.setDataHora(LocalDateTime.parse(
                    resgate.getDataHora().toString().length() < 18 ?
                            resgate.getDataHora().toString() +":01":
                            resgate.getDataHora().toString()
            ));
        }

        model.addAttribute("Resgates", resgates);
    	
    	List<String> documentosAnexados = Helpers.listarArquivos("/home/dhp/" + cpf);
    	model.addAttribute("DocumentosAnexados", documentosAnexados);
    	model.addAttribute("Cliente", cliente);
    	model.addAttribute("verificado", verificado);
    	model.addAttribute("id", id);
        return "admin-cliente-home";
    }
    
    
    @RequestMapping(value = {"/admin/compras"}, method = RequestMethod.GET)
    public String adminCompras(Model model) {
    	List<Pagamentos> compras = adminService.retrieveAllCompras();
    	
    	for(Pagamentos compra : compras){
    	    compra.setDataHora(LocalDateTime.parse(
    	            compra.getDataHora().toString().length() < 18 ?
                            compra.getDataHora().toString() +":01":
                            compra.getDataHora().toString()
            ));
    		User user = userService.findById(compra.getClienteid());
    		compra.setNome(user.getNome());
    	}
    	
    	model.addAttribute("Compras", compras);
    
        return "admin-compras";
    }
    
    @RequestMapping(value = {"/admin/resgates/bitcoin"}, method = RequestMethod.GET)
    public String adminResgatesBitcoin(Model model) {
    	List<ResgateBitcoin> resgates = adminService.retrieveAllResgateBitcoin();
    	
    	for(ResgateBitcoin resgate : resgates){
            resgate.setDataHora(LocalDateTime.parse(
                    resgate.getDataHora().toString().length() < 18 ?
                            resgate.getDataHora().toString() +":01":
                            resgate.getDataHora().toString()
            ));
    	    User user = userService.findById(resgate.getClienteid());
    		resgate.setNome(user.getNome());
    	}
    	
    	model.addAttribute("Resgates", resgates);
    
        return "admin-resgates-bitcoin";
    }
    
    @RequestMapping(value = {"/admin/resgates/conta-bancaria"}, method = RequestMethod.GET)
    public String adminResgatesContaBancaria(Model model) {
    	List<ResgateContaBancaria> resgates = adminService.retrieveAllResgateContaBancaria();
    	
    	for(ResgateContaBancaria resgate : resgates){
    		User user = userService.findById(resgate.getClienteid());
    		resgate.setNome(user.getNome());
    	}
    	
    	model.addAttribute("Resgates", resgates);
    
        return "admin-resgates-conta-bancaria";
    }
    @Transactional
    @RequestMapping(value = {"/admin/verificar-cadastro"}, method = RequestMethod.POST)
    public String adminVerificarCadastro(Model model) {
    	long id = Long.valueOf((String)context.getParameter("id"));
    	User cliente = entityManager.find(User.class, id); //Consider em as JPA EntityManager
    	String flag = context.getParameter("flag");

        SimpleMailMessage email = constructEmail("[BTC Moedas]Cadastro verificado",
                "Oi "+ cliente.getNome().split("\\s+")[0]+ ",\n\n" +
                        "Seu cadastro foi verificado com sucesso.\n\n"
                        + "Você já pode comprar bitcoin."
                        + "\n\n"
                        + "Atenciosamente,\n"
                        + "Davi Junior\n" +
                        "BTC Moedas",
                cliente
        );


    	if (flag.equals("v")){
            mailSender.send(email);
            cliente.setVerificado(1);
    	} else if (flag.equals("r")){
    		cliente.setVerificado(0);	
    	}
    	entityManager.merge(cliente);

        return "redirect:/admin/clientes/"+id;
    }


    @Transactional
    @RequestMapping(value = {"/admin/atualizar-saldo"}, method = RequestMethod.POST)
    public String adminAtualizarSaldo(Model model) {
        long id = Long.valueOf((String)context.getParameter("id"));
        EnderecosBTC cliente = entityManager.find(EnderecosBTC.class, id); //Consider em as JPA EntityManager
        Double saldoBTC = Double.valueOf(context.getParameter("saldoBTC").toString());

        cliente.setSaldo(saldoBTC);
        entityManager.merge(cliente);

        return "redirect:/admin/clientes/"+id;
    }

    @Transactional
    @RequestMapping(value = {"/admin/confirmar-compra"}, method = RequestMethod.POST)
    public String confirmarCompra(Model model) {

        long id = Long.valueOf((String)context.getParameter("id"));
        User user = userService.findById(id);


        mailSender.send(constructConfirmaCompra(user));
        return "redirect:/admin/clientes/"+id;
    }



    // ============== ADMIN AREA ============ //
    
    
    
    
    // ============== NON-API ============
    
    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
        final String url = contextPath + "/mudar-senha?cpf=" + user.getCpf() + "&token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Recuperar senha", message + " \r\n\n" + url, user);
    }
    
    private MimeMessage constructComprarBitcoinEmail(final User user, Pagamentos pagamentos) {
        NumberFormat formatter = new DecimalFormat("0.00000000");
    	String nome = user.getNome().split("\\s+")[0];
    	String tipodepagamento =  pagamentosObj.getTipoPagamento() == 2 ? "Compra Avulsa" : "Mensal";
    	String mensagem = "<div style='font-size: 14px; line-height: 14px; font-family: Ubuntu, Tahoma, Verdana, Segoe, sans-serif;' data-mce-style='font-size: 12px; line-height: 14px; font-family: Ubuntu, Tahoma, Verdana, Segoe, sans-serif;'>"+
"<p style='font-size: 14px; line-height: 16px;' data-mce-style='font-size: 14px; line-height: 16px;'>"+
"Ol&aacute; <strong>"+nome+"</strong>,"+
"<br />"+
"<br />"+
"Obrigado por comprar bitcoin atrav&eacute;s de nossa plataforma. Confira os detalhes da sua compra:"+
"<br />"+
"<br />"+
"Cota&ccedil;&atilde;o: "+"<b>"+pagamentosObj.getCotacao()+"</b>"+
"<br />"+
"Taxa de transação: "+"<b>"+formatter.format(pagamentosObj.getTaxa()).replace('.', ',')+" BTC</b>"+
"<br />"+
"Bitcoin: "+"<b>"+formatter.format(pagamentosObj.getBitcoins()+pagamentosObj.getTaxa()).replace('.', ',')+" BTC</b>"+
"<br />"+
"Valor total da compra: "+"<b>"+pagamentosObj.getValorPagamentoComTaxa()+"</b>"+
"<br />"+
"Você vai receber: "+"<b>"+formatter.format(pagamentosObj.getBitcoins()).replace('.', ',')+" BTC</b>"+
"<br />"+
"O prazo para libera&ccedil;&atilde;o dos bitcoins &eacute; de <b>2</b> at&eacute; <b>24</b> horas ap&oacute;s aprova&ccedil;&atilde;o do pagamento."+
"</p>"+
"<p style='font-size: 14px; line-height: 16px;' data-mce-style='font-size: 14px; line-height: 16px;'>"+
"Se tiver qualquer d&uacute;vida basta responder este e-mail ou ent&atilde;o nos contatar pelo n&uacute;mero <b>41</b> <b>99973-7902</b>."+
"<br />"+
"<br />Atenciosamente,"+
"<br />BTC Moedas"+
"</p>"+
"</div>";
       return constructEmailHtml("[BTC Moedas]Sua compra de bitcoin", mensagem, user);
    }

    private MimeMessage constructComprarBitcoinEmailAdmin(final User user, Pagamentos pagamentos) {
        NumberFormat formatter = new DecimalFormat("0.00000000");
        DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd/MM/YYY HH:mm");
        String formattedDateTime = pagamentosObj.getDataHora().format(dateformatter);

        String nome = user.getNome().split("\\s+")[0];
        String tipodepagamento =  pagamentosObj.getTipoPagamento() == 2 ? "Compra Avulsa" : "Mensal";
        String mensagem = "<div style='font-size: 14px; line-height: 14px; font-family: Ubuntu, Tahoma, Verdana, Segoe, sans-serif;' data-mce-style='font-size: 12px; line-height: 14px; font-family: Ubuntu, Tahoma, Verdana, Segoe, sans-serif;'>"+
                "<p style='font-size: 14px; line-height: 16px;' data-mce-style='font-size: 14px; line-height: 16px;'>"+
                "Ol&aacute;,"+
                "<br />"+
                "<br />"+
                "Uma nova compra de bitcoin foi feita:"+
                "<br />"+
                "<br />"+
                "Cota&ccedil;&atilde;o:  "+"<b>"+pagamentosObj.getCotacao()+"</b>"+
                "<br />"+
                "Data e Hora:  "+"<b>"+formattedDateTime+"</b>"+
                "<br />"+
                "<br />"+
                "<br />"+
                "Cliente:  "+"<b>"+user.getNome()+"</b>"+
                "<br />" +
                "CPF:  "+"<b>"+user.getCpf()+"</b>"+
                "<br />"+
                "E-mail:  "+"<b>"+user.getEmail()+"</b>"+
                "<br />"+
                "Celular:  "+"<b>"+user.getCelular()+"</b>"+
                "<br />"+
                "<br />"+
                "Tipo de pagamento:  "+"<b>"+tipodepagamento+"</b>"+
                "<br />"+
                "Quantidade de bitcoin:  "+"<b>"+formatter.format(pagamentosObj.getBitcoins()).replace('.', ',')+" BTC</b>"+
                "<br />"+
                "Taxa de transação:  "+"<b>"+formatter.format(pagamentosObj.getTaxa()).replace('.', ',')+" BTC</b>"+
                "<br />"+
                "Valor total:  "+"<b>"+pagamentosObj.getValorPagamentoComTaxa()+"</b>"+
                "<br />"+
                "<br />"+
                "O prazo para libera&ccedil;&atilde;o dos bitcoins &eacute; de <b>2</b> at&eacute; <b>24</b> horas ap&oacute;s aprova&ccedil;&atilde;o do pagamento."+
                "</p>"+
                "<p style='font-size: 14px; line-height: 16px;' data-mce-style='font-size: 14px; line-height: 16px;'>"+
                "<br />Atenciosamente,"+
                "<br />BTC Moedas"+
                "</p>"+
                "</div>";
        return constructEmailAdminHtml("[Admin]Nova compra de bitcoin", mensagem, user);
    }
    
    private MimeMessage constructResgateContaBancariaEmail(final User user, ResgateContaBancaria resgatecontabancaria) {
    	String nome = user.getNome().split("\\s+")[0];
    	String tipodeconta = resgatecontabancaria.getTipodeconta().equals("1") ? "Conta Corrente" : "Poupança";
    	String valordoresgate = currencyFormatter.format(Double.valueOf(resgatecontabancaria.getValorResgate()));
    	String mensagem ="<div style='font-size: 12px; line-height: 14px; font-family: Ubuntu, Tahoma, Verdana, Segoe, sans-serif;' data-mce-style='font-size: 12px; line-height: 14px; font-family: Ubuntu, Tahoma, Verdana, Segoe, sans-serif;'>"+
"<p style='font-size: 14px; line-height: 16px;' data-mce-style='font-size: 14px; line-height: 16px;'>"+
"Ol&aacute; <strong>"+nome+"</strong>,"+
"<br />"+
"<br />"+
"Sua solicita&ccedil;&atilde;o de resgate para conta banc&aacute;ria foi feita com sucesso."+
"<br />"+
"<br />"+
"Vamos fazer a seguinte transfer&ecirc;ncia:"+
"<br />"+
"<br />"+
"Banco: "+"<b>"+resgatecontabancaria.getBanco()+"</b>"+
"<br />"+
"Tipo de Conta: "+"<b>"+tipodeconta+"</b>"+
"<br />"+
"Ag&ecirc;ncia: "+"<b>"+resgatecontabancaria.getAgencia()+"</b>"+
"<br />"+
"Conta com d&iacute;gito:"+"<b>"+resgatecontabancaria.getConta()+"</b>"+
"<br />"+
"Nome: "+"<b>"+user.getNome()+"</b>"+
"<br />"+
"CPF: "+"<b>"+user.getCpf()+"</b>"+
"<br />"+
"Valor: "+"<b>"+valordoresgate+"</b>"+
"<br />"+
"<br />"+
"Se o seu banco n&atilde;o for conveniado (Ita&uacute;, Bradesco, Caixa, BB e Santander), iremos deduzir o valor de R$8,90 referente a taxa de transfer&ecirc;ncia banc&aacute;ria."+
"<br />"+
"<br />"+
"O prazo para transfer&ecirc;ncia do dinheiro &eacute; de <b>3</b>&nbsp;at&eacute; <b>5</b>&nbsp;dias &uacute;teis.</p>"+
"<p style='font-size: 14px; line-height: 16px;' data-mce-style='font-size: 14px; line-height: 16px;'>"+
"Caso tenha qualquer d&uacute;vida basta responder esta e-mail ou ent&atilde;o nos contatar pelo n&uacute;mero <b>41</b> <b>99973-7902</b>."+
"<br />"+
"<br />Atenciosamente,"+
"<br />BTC Moedas"+
"</p>"+
"</div>";
    			
    	return constructEmailHtml("[BTC Moedas]Seu resgate para conta bancaria", mensagem, user);
    }
    
    private MimeMessage constructResgateBitcoinEmail(final User user, ResgateBitcoin resgatebitcoin) {
    	String nome = user.getNome().split("\\s+")[0];
    	String mensagem ="<div style='font-size: 12px; line-height: 14px; font-family: Ubuntu, Tahoma, Verdana, Segoe, sans-serif;' data-mce-style='font-size: 12px; line-height: 14px; font-family: Ubuntu, Tahoma, Verdana, Segoe, sans-serif;'>"+
    			"<p style='font-size: 14px; line-height: 16px;' data-mce-style='font-size: 14px; line-height: 16px;'>"+
    			"Ol&aacute; <strong>"+nome+"</strong>,"+
    			"<br />"+
    			"<br />"+
    			"Sua solicita&ccedil;&atilde;o de saque para carteira bitcoin foi feita com sucesso."+
    			"<br />"+
    			"<br />"+
    			"Vamos fazer a seguinte transfer&ecirc;ncia:"+
    			"<br />"+
    			"<br />"+
    			"Valor do Saque: "+"<b>"+formatter.format(resgatebitcoin.getBitcoins())+" BTC"+"</b>"+
    			"<br />"+
    			"Taxa de Transfer&ecirc;ncia: "+"<b>"+ getfeeLocalBitcoinscomvirgula()+" BTC</b>"+
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
    	return constructEmailHtml("[BTC Moedas]Seu saque para carteira bitcoin", mensagem, user);
    }


    private MimeMessage constructResgateBitcoinEmailAdmin(final User user, ResgateBitcoin resgatebitcoin) {
          String mensagem ="<div style='font-size: 12px; line-height: 14px; font-family: Ubuntu, Tahoma, Verdana, Segoe, sans-serif;' data-mce-style='font-size: 12px; line-height: 14px; font-family: Ubuntu, Tahoma, Verdana, Segoe, sans-serif;'>"+
                "<p style='font-size: 14px; line-height: 16px;' data-mce-style='font-size: 14px; line-height: 16px;'>"+
                "Ol&aacute; <strong>Davi</strong>,"+
                "<br />"+
                "<br />"+
                "Foi efetuado com sucesso o seguinte saque bitcoin:"+
                "<br />"+
                "<br />"+
                "Cliente: "+"<b>"+user.getNome()+"</b>"+
                "<br />"+
                "E-mail: "+"<b>"+user.getEmail()+"</b>"+
                "<br />"+
                "Valor do Saque: "+"<b>"+formatter.format(resgatebitcoin.getBitcoins())+" BTC"+"</b>"+
                "<br />"+
                "Taxa de Transfer&ecirc;ncia: "+"<b>"+ getfeeLocalBitcoinscomvirgula()+" BTC</b>"+
                "<br />"+
                "Total Sacado: "+"<b>"+resgatebitcoin.getTotalresgatado()+" BTC"+"</b>"+
                "<br />"+
                "Endere&ccedil;o bitcoin: "+"<b>"+resgatebitcoin.getEnderecobitcoin()+"</b>"+
                "<br />"+
                "<br />"+
                "<br />Atenciosamente,"+
                "<br />BTC Moedas"+
                "</p>"+
                "</div>";
        return constructEmailHtmlResgate("[BTC Moedas]Novo saque bitcoin", mensagem, user);
    }

    
    private SimpleMailMessage constructSolicitarVerificao(String nome, String email) {
    	return constructEmailAdmin("Solicitação de Verificação de Documentos - "+nome, "O cliente "+nome+" - "+email+" solicitou a verificação de seus documentos."+"\r\n\n");
    }

    private SimpleMailMessage constructContato(String nome, String email, String mensagem) {
        return constructEmailAdmin("Nova Mensagem do Site", "Nome: "+nome+"\n"+
                "Email: " +email+"\n"+
                "Mensagem: "+mensagem+
                "\r\n\n");
    }

    private SimpleMailMessage constructConfirmaCompra(User user) {
        return constructEmail(
                "[BTC Moedas]Compra confirmada",
                "Olá " + user.getNome().split("\\s+")[0]+","+
                "\n\n"+"Obrigado por comprar bitcoin conosco."+
                "\n\n"+"Sua compra de bitcoin foi confirmada e o bitcoin já está disponível na sua conta."+
                "\n\n"+"Se tiver qualquer dúvida, só me chamar aqui ou pelo cel/whatsapp 41 99973-7902."+
                "\n\n"+"Atenciosamente,"+
                "\n"+"Davi Junior"+
                "\n"+"BTC Moedas"
                ,
                user);
    }
    
    private MimeMessage constructEmailHtml(String subject, String mensagem, User user) {
        MimeMessage email = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
        helper = new MimeMessageHelper(email, true, "ISO-8859-1");
		helper.setSubject(subject);
        helper.setText(mensagem, true);
        helper.setTo(user.getEmail());
        helper.setFrom("admin@btcmoedas.com.br");
        } catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return email;
    }

    private MimeMessage constructEmailHtmlResgate(String subject, String mensagem, User user) {
        MimeMessage email = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(email, true, "ISO-8859-1");
            helper.setSubject(subject);
            helper.setText(mensagem, true);
            helper.setTo("admin@btcmoedas.com.br");
            helper.setFrom("admin@btcmoedas.com.br");
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return email;
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom("admin@btcmoedas.com.br");
        return email;
    }

    private SimpleMailMessage constructEmailAdmin(String subject, String body) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo("admin@btcmoedas.com.br");
        email.setFrom("admin@btcmoedas.com.br");
        return email;
    }

    private MimeMessage constructEmailAdminHtml(String subject, String mensagem, User user) {
        MimeMessage email = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(email, true);
            helper.setSubject(subject);
            helper.setText(mensagem, true);
            helper.setTo("admin@btcmoedas.com.br");
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
