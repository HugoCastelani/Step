package com.hugocastelani.blockbook.database.domain;

import android.support.annotation.NonNull;
import com.hugocastelani.blockbook.database.domain.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 17:26
 */

public final class Treatment extends Domain {
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
