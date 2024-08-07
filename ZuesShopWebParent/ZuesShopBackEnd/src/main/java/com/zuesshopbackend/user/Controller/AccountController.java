package com.zuesshopbackend.user.Controller;

import com.zuesshop.entity.User;
import com.zuesshopbackend.FileUploadUtil;
import com.zuesshopbackend.security.ShopZuesUserDetails;
import com.zuesshopbackend.user.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {
    @Autowired
    private UserService service;

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal ShopZuesUserDetails loggefUser, Model model){
        String email = loggefUser.getUsername();
        User user = service.getByEmail(email);
        model.addAttribute("user", user);

        return "/users/account_form";
    }

    @PostMapping("/account/update")
    public String saveDetails(User user,@AuthenticationPrincipal ShopZuesUserDetails logged , RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty())
        {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String photos = StringUtils.cleanPath(fileName);
            user.setPhotos(photos);
            User saveUser = service.updateAccount(user);

            String uploadDir = "./user-photos/" + saveUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }else{
            if(user.getPhotos().isEmpty()){
                user.setPhotos(null);
            }
            service.updateAccount(user);
        }

        logged.setFirstname(user.getFirstName());
        logged.setLastname(user.getLastName());
        logged.setPhotosimagepath(user.getPhotos());

        redirectAttributes.addFlashAttribute("messageSuccess","Your account details has been updated");

        return "redirect:/account";
    }
}
