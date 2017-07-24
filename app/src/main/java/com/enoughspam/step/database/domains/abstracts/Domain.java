package com.enoughspam.step.database.domains.abstracts;

import com.enoughspam.step.annotation.NonNegative;

/**
 * Created by Hugo Castelani
 * Date: 21/07/17
 * Time: 19:25
 */

public class Domain {
    private int id;

    public Domain() {}

    public Domain(@NonNegative final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(@NonNegative final int id) {
        this.id = id;
    }
}
