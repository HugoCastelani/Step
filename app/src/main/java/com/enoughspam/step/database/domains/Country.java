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

    public Country(@NonNegative int code, @NonNull String name, @NonNull String mask) {
        this.code = code;
        this.name = name;
        this.mask = mask;
    }

    public Country(@NonNegative int id, @NonNegative int code,
                   @NonNull String name, @NonNull String mask) {
        super(id);
        this.code = code;
        this.name = name;
        this.mask = mask;
    }

    public int getCode() {
        return code;
    }

    public void setCode(@NonNegative int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(@NonNull String mask) {
        this.mask = mask;
    }
}
