package com.zuesshopfrontend.customer;

import com.zuesshop.entity.Country;
import com.zuesshop.entity.Customer;
import com.zuesshop.entity.SettingBag;
import com.zuesshopfrontend.Utility;
import com.zuesshopfrontend.setting.EmailSettingBag;
import com.zuesshopfrontend.setting.Service.SettingService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired private CustomerService customerService;
    @Autowired private SettingService settingService;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        List<Country> listAllCountries = customerService.listAllCountries();

        model.addAttribute("listCountries", listAllCountries);
        model.addAttribute("pageTitle", "Customer Registration");
        model.addAttribute("customer", new Customer());

        return "register/register_form";
    }

    @PostMapping("/create_customer")
    public String createCustomer(Customer customer, Model model, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        customerService.registerCustomer(customer);
        sendVerificationEmail(request, customer);
        model.addAttribute("pageTitle", "Registration Succeeded");

        return "/register/register_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSetting = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSetting);

        String toAddress = customer.getEmail();
        String subject = emailSetting.getCustomerVerifySubject();
        String content = emailSetting.getCustomerVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSetting.getFromAddress(),emailSetting.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]",customer.getFullName());

        String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
        content = content.replace("[[URL]]",verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code")String code, Model model){
        boolean verified = customerService.verify(code);
        return "/register/" + (verified ? "verify_success" : "verify_fail");
    }
}
