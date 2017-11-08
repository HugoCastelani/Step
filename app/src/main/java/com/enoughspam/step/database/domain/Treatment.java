package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.domain.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 17:26
 */

public class Treatment extends Domain {
    private String treatment;

    public Treatment() {}

    public Treatment(@NonNull final String key, @NonNull final String treatment) {
        super(key);
        this.treatment = treatment;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(@NonNull final String treatment) {
        this.treatment = treatment;
    }
}
