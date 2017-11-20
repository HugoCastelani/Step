package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 04/07/17
 * Time: 17:26
 */

public class Country extends Domain {
    private Integer code;
    private String name;
    private String iso;
    private String mask;

    public Country() {}

    public Country(@NonNull final String key, @NonNegative @NonNull final Integer code,
                   @NonNull final String name, @NonNull final String iso,
                   @NonNull final String mask) {
        super(key);
        setCode(code);
        setName(name);
        setISO(iso);
        setMask(mask);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(@NonNegative @NonNull final Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull final String name) {
        this.name = name;
    }

    public String getISO() {
        return iso;
    }

    public void setISO(@NonNull final String iso) {
        this.iso = iso;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(@NonNull final String mask) {
        this.mask = mask;
    }
}
