package com.enoughspam.step.database.domains;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domains.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 17:26
 */

public class Treatment extends Domain {
    private String treatment;

    public Treatment(@NonNegative final int id, @NonNull final String treatment) {
        super(id);
        this.treatment = treatment;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(@NonNull final String treatment) {
        this.treatment = treatment;
    }
}
