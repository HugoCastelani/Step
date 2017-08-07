package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 21:58
 */

public class Friendship {
    private int id;
    private User added;
    private User adding;

    public Friendship(@NonNegative final int id, @NonNull final User added,
                      @NonNull final User adding) {
        this.id = id;
        this.added = added;
        this.adding = adding;
    }

    public int getId() {
        return id;
    }

    public void setId(@NonNegative final int id) {
        this.id = id;
    }

    public User getAdded() {
        return added;
    }

    public void setAdded(@NonNull final User added) {
        this.added = added;
    }

    public User getAdding() {
        return adding;
    }

    public void setAdding(@NonNull final User adding) {
        this.adding = adding;
    }
}
