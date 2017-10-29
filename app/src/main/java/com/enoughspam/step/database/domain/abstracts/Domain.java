package com.enoughspam.step.database.domain.abstracts;

import com.enoughspam.step.annotation.NonNegative;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Hugo Castelani
 * Date: 21/07/17
 * Time: 19:25
 */

@IgnoreExtraProperties
public class Domain implements Serializable {
    private Integer id;

    public Domain() {}

    public Domain(@NonNegative final Integer id) {
        this.id = id;
    }

    public Integer getID() {
        return id;
    }

    public void setID(@NonNegative final Integer id) {
        this.id = id;
    }
}
