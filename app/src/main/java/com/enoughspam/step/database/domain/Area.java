package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.abstracts.Domain;
import com.google.firebase.database.Exclude;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 13:10
 */

public class Area extends Domain {
    private Integer code;
    private String name;
    private State state;
    private Integer stateID;

    public Area() {}

    public Area(@NonNegative final Integer id, @NonNegative final Integer code,
                @NonNull final String name, @NonNull final State state) {
        super(id);
        this.code = code;
        this.name = name;
        setState(state);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(@NonNegative final Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull final String name) {
        this.name = name;
    }

    @Exclude
    public State getState() {
        return state;
    }

    public Integer getStateID() {
        return stateID;
    }

    public void setState(@NonNull final State state) {
        this.state = state;
        setStateID(state.getID());
    }

    private void setStateID(@NonNegative final Integer stateID) {
        this.stateID = stateID;
        if (state != null) {
            state.setID(stateID);
        }
    }
}
