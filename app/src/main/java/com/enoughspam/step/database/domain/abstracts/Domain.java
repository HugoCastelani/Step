package com.enoughspam.step.database.domain.abstracts;

import com.enoughspam.step.annotation.NonNegative;

import java.io.Serializable;

/**
 * Created by Hugo Castelani
 * Date: 21/07/17
 * Time: 19:25
 */

public class Domain implements Serializable {
    private int id;

    public Domain() {}

    public Domain(@NonNegative final int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public void setID(@NonNegative final int id) {
        this.id = id;
    }
}
