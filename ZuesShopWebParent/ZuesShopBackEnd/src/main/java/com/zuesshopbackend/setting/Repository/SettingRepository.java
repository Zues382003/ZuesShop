package com.zuesshopbackend.setting.Repository;

import com.zuesshop.entity.Setting;
import com.zuesshop.entity.SettingCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends CrudRepository<Setting, String> {
    public List<Setting> findByCategory(SettingCategory category);
}
