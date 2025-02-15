package com.zuesshopfrontend.setting.Service;

import com.zuesshop.entity.Setting;
import com.zuesshop.entity.SettingCategory;
import com.zuesshopfrontend.setting.EmailSettingBag;
import com.zuesshopfrontend.setting.Repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepository repo;

    public List<Setting> getGeneralSettings(){
        return repo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

    public EmailSettingBag getEmailSettings(){
        List<Setting> settings = repo.findByCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(repo.findByCategory(SettingCategory.MAIL_TEMPLATES));

        return new EmailSettingBag(settings);
    }

}
