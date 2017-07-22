package com.enoughspam.step.database.domains;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domains.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 04/07/17
 * Time: 17:26
 */

public class Country extends Domain {
    private int code;
    private String name;
    private String mask;

    public Country(@NonNegative final int code, @NonNull final String name,
                   @NonNull final String mask) {
        this.code = code;
        this.name = name;
        this.mask = mask;
    }

    public Country(@NonNegative final int id, @NonNegative final int code,
                   @NonNull final String name, @NonNull final String mask) {
        super(id);
        this.code = code;
        this.name = name;
        this.mask = mask;
    }

    public int getCode() {
        return code;
    }

    public void setCode(@NonNegative final int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull final String name) {
        this.name = name;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(@NonNull final String mask) {
        this.mask = mask;
    }
}
