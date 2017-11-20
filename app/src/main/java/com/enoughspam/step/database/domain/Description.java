package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.domain.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 13:28
 */

public final class Description extends Domain {
    private String description;
    private String treatmentKey;

    public Description() {}

    public Description(@NonNull final String key, @NonNull final String description,
                       @NonNull final String treatmentKey) {
        super(key);
        this.description = description;
        this.treatmentKey = treatmentKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull final String description) {
        this.description = description;
    }

    public String getTreatmentKey() {
        return treatmentKey;
    }

    public void setTreatmentKey(@NonNull final String treatmentKey) {
        this.treatmentKey = treatmentKey;
    }
}
