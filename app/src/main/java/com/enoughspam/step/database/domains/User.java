package com.enoughspam.step.database.domains;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domains.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:02
 */

public class User extends Domain {
    private String socialId;
    private String name;

    public User(@NonNegative final int id, @NonNull final String name) {
        super(id);
        this.name = name;
    }

    public User(@NonNull final String socialId, @NonNull final String name) {
        this.socialId = socialId;
        this.name = name;
    }

    public User(@NonNegative final int id, @NonNull final String socialId,
                @NonNull final String name) {
        super(id);
        this.socialId = socialId;
        this.name = name;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(@NonNull final String socialId) {
        this.socialId = socialId;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull final String name) {
        this.name = name;
    }
}
