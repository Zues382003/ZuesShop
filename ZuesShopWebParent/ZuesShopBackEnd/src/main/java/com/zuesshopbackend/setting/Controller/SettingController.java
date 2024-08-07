package com.zuesshopbackend.setting.Controller;

import com.zuesshop.entity.Currency;
import com.zuesshop.entity.Setting;
import com.zuesshopbackend.FileUploadUtil;
import com.zuesshopbackend.setting.Repository.CurrencyRepository;
import com.zuesshopbackend.setting.Service.GeneralSettingBag;
import com.zuesshopbackend.setting.Service.SettingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.cellprocessor.ParseInt;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class SettingController {

    @Autowired
    private SettingService settingService;
    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping("/settings")
    public String listAll(Model model){
        List<Setting> listSettings = settingService.getAllSettings();
        List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();

        model.addAttribute("listCurrencies", listCurrencies);

        listSettings.forEach(setting -> {
            model.addAttribute(setting.getKey(), setting.getValue());
        });

        return "settings/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam("FileImageSite")MultipartFile multipartFile, HttpServletRequest request, RedirectAttributes ra) throws IOException {

        GeneralSettingBag generalSettingBag = settingService.getGeneralSettingBag();
        saveSiteLogo(multipartFile, generalSettingBag);
        saveCurrencySymbol(request, generalSettingBag);
        updateAllSettingValueFromForm(request, generalSettingBag.getListSettings());

        ra.addFlashAttribute("messageSuccess","General Settings have been saved.");

        return "redirect:/settings";
    }

    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag generalSettingBag) throws IOException {
        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String value = "/site-logo/" + fileName;
            generalSettingBag.updateSiteLogo(value);

            String uploadDir = "../site-logo/";
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag generalSettingBag){
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> findByIdResult= currencyRepository.findById(currencyId);

        if(findByIdResult.isPresent()){ //if object has exist in database, it will return true else false
            Currency currency = findByIdResult.get();
            generalSettingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }

    private void updateAllSettingValueFromForm(HttpServletRequest request, List<Setting> listSettings){
        for(Setting setting : listSettings){
            String value = request.getParameter(setting.getKey());
            if(value != null){
                setting.setValue(value);
            }
        }
        settingService.saveAll(listSettings);
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSetting(HttpServletRequest request, RedirectAttributes ra){
        List<Setting> mailServerSettings = settingService.getMailServerSettings();
        updateAllSettingValueFromForm(request,mailServerSettings);

        ra.addFlashAttribute("messageSuccess", "Mail Server Settings have been saved");

        return "redirect:/settings";
    }

    @PostMapping("/settings/save_mail_templates")
    public String saveMailTemplatesSetting(HttpServletRequest request, RedirectAttributes ra){
        List<Setting> mailServerSettings = settingService.getMailTemplatesSettings();
        updateAllSettingValueFromForm(request,mailServerSettings);

        ra.addFlashAttribute("messageSuccess", "Mail Templates Settings have been saved");

        return "redirect:/settings";
    }
}
