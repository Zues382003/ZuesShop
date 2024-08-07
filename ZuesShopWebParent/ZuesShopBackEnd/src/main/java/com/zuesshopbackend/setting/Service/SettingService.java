package com.zuesshopbackend.setting.Service;

import com.zuesshop.entity.Setting;
import com.zuesshop.entity.SettingCategory;
import com.zuesshopbackend.setting.Repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepository repo;

    public List<Setting> getAllSettings(){
        return (List<Setting>) repo.findAll();
    }

    public GeneralSettingBag getGeneralSettingBag(){
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = repo.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);

        return new GeneralSettingBag(settings);
    }

    public void saveAll(Iterable<Setting> settings){
        repo.saveAll(settings);
    }

    public List<Setting> getMailServerSettings(){
        return repo.findByCategory(SettingCategory.MAIL_SERVER);
    }
    public List<Setting> getMailTemplatesSettings(){
        return repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

}
