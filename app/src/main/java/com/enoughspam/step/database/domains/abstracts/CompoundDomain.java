package com.enoughspam.step.database.domains.abstracts;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 20:34
 */

public class CompoundDomain {
    private int[] ids;

    public CompoundDomain() {}

    public CompoundDomain(@NonNull @NonNegative final int[] ids) {
        this.ids = ids;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(@NonNull @NonNegative final int[] ids) {
        this.ids = ids;
    }
}
