package com.enoughspam.step.database.domains;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domains.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 13:28
 */

public class Description extends Domain {
    private String description;

    public Description(@NonNegative final int id, @NonNull final String description) {
        super(id);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
