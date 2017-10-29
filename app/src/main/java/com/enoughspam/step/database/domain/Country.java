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

    public Country(@NonNegative final Integer id, @NonNegative final Integer code,
                   @NonNull final String name, @NonNull final String iso,
                   @NonNull final String mask) {
        super(id);
        this.code = code;
        this.name = name;
        this.iso = iso;
        this.mask = mask;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(@NonNegative final Integer code) {
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

    public void setIso(@NonNull final String iso) {
        this.iso = iso;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(@NonNull final String mask) {
        this.mask = mask;
    }
}
