package com.enoughspam.step.database.domains.abstracts;

import com.enoughspam.step.annotation.NonNegative;

/**
 * Created by hugo
 * Date: 21/07/17
 * Time: 19:25
 */


public class Domain {
    private int id;

    public Domain() {}

    public Domain(@NonNegative int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(@NonNegative int id) {
        this.id = id;
    }
}
