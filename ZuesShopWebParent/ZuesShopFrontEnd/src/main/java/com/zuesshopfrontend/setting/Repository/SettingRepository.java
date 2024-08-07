package com.zuesshopfrontend.setting.Repository;

import com.zuesshop.entity.Setting;
import com.zuesshop.entity.SettingCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends CrudRepository<Setting, String> {
    public List<Setting> findByCategory(SettingCategory category);

    @Query("select s from Setting s where s.category = ?1 or s.category = ?2")
    public List<Setting> findByTwoCategories(SettingCategory category1, SettingCategory category2);
}
