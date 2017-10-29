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
    private Integer treatmentID;

    public Description() {}

    public Description(@NonNegative final Integer id, @NonNull final String description,
                       @NonNegative final Integer treatmentID) {
        super(id);
        this.description = description;
        this.treatmentID = treatmentID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull final String description) {
        this.description = description;
    }

    public Integer getTreatmentID() {
        return treatmentID;
    }

    public void setTreatmentID(@NonNegative final Integer treatmentID) {
        this.treatmentID = treatmentID;
    }
}
