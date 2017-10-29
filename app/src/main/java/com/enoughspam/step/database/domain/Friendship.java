package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.google.firebase.database.Exclude;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 21:58
 */

public class Friendship {
    private User added;
    private Integer addedID;
    private User adding;
    private Integer addingID;

    private String addedIDAddingID;

    public Friendship() {}

    public Friendship(@NonNull final User added, @NonNull final User adding) {
        setAdded(added);
        setAdding(adding);
        setAddedIDAddingID();
    }

    @Exclude
    public User getAdded() {
        return added;
    }

    public Integer getAddedID() {
        return addedID;
    }

    public void setAdded(@NonNull final User added) {
        this.added = added;
        addedID = added.getID();
        setAddedIDAddingID();
    }

    public void setAddedID(@NonNegative final Integer addedID) {
        this.addedID = addedID;
        if (added != null) {
            added.setID(addedID);
        }
        setAddedIDAddingID();
    }

    public Integer getAddingID() {
        return addingID;
    }

    @Exclude
    public User getAdding() {
        return adding;
    }

    public void setAdding(@NonNull final User adding) {
        this.adding = adding;
        addingID = adding.getID();
        setAddedIDAddingID();
    }

    public void setAddingID(@NonNegative final Integer addingID) {
        this.addingID = addingID;
        if (adding != null) {
            adding.setID(addingID);
        }
        // some fucking how, this call can't be done
        //setAddedIDAddingID();
    }

    public String getAddedIDAddingID() {
        return addedIDAddingID;
    }

    public void setAddedIDAddingID() {
        addedIDAddingID = Integer.toString(addedID) + addingID;
    }
}
