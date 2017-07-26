package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 13:28
 */

public class Description extends Domain {
    private String description;
    private int treatmentId;

    public Description(@NonNegative final int id, @NonNull final String description,
                       @NonNegative final int treatmentId) {
        super(id);
        this.description = description;
        this.treatmentId = treatmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull final String description) {
        this.description = description;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(@NonNegative final int treatmentId) {
        this.treatmentId = treatmentId;
    }
}
