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

    public User(@NonNegative int id, @NonNull String name) {
        super(id);
        this.name = name;
    }

    public User(@NonNull String socialId, @NonNull String name) {
        this.socialId = socialId;
        this.name = name;
    }

    public User(@NonNegative int id, @NonNull String socialId, @NonNull String name) {
        super(id);
        this.socialId = socialId;
        this.name = name;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(@NonNull String socialId) {
        this.socialId = socialId;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
