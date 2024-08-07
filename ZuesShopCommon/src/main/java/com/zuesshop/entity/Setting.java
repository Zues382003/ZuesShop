package com.zuesshop.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "settings")
public class Setting {
    @Id
    @Column(name = "`key`", nullable = false, length = 128)
    private String key;//word key duplicate syntax in mysql, so we use ``

    @Column(nullable = false, length = 1024)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private SettingCategory category;

    public Setting(){}

    public Setting(String key) {
        this.key = key;
    }

    public Setting(String key, String value, SettingCategory category) {
        this.key = key;
        this.value = value;
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SettingCategory getCategory() {
        return category;
    }

    public void setCategory(SettingCategory category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Setting setting)) return false;
        return Objects.equals(getKey(), setting.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    @Override
    public String toString() {
        return "Setting{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
