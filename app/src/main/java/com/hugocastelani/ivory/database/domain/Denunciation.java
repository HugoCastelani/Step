package com.hugocastelani.ivory.database.domain;

import android.support.annotation.NonNull;

/**
 * Created by Hugo Castelani
 * Date: 01/12/17
 * Time: 21:07
 */

public final class Denunciation {
    private Integer description;
    private Long amount;

    public Denunciation(@NonNull final Integer description, @NonNull final Long amount) {
        this.description = description;
        this.amount = amount;
    }

    public Integer getDescription() {
        return description;
    }

    public void setDescription(@NonNull final Integer description) {
        this.description = description;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(@NonNull final Long amount) {
        this.amount = amount;
    }
}
