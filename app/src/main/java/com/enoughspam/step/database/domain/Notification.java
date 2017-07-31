package com.enoughspam.step.database.domain;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 31/07/17
 * Time: 16:05
 */

public class Notification extends Domain {
    private int phoneId;
    private int notifiedUserId;
    private int notifyingUserId;

    public Notification(@NonNegative final int id, @NonNegative final int phoneId,
                        @NonNegative final int notifiedUserId, @NonNegative final int notifyingUserId) {
        super(id);
        this.phoneId = phoneId;
        this.notifiedUserId = notifiedUserId;
        this.notifyingUserId = notifyingUserId;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(@NonNegative final int phoneId) {
        this.phoneId = phoneId;
    }

    public int getNotifiedUserId() {
        return notifiedUserId;
    }

    public void setNotifiedUserId(@NonNegative final int notifiedUserId) {
        this.notifiedUserId = notifiedUserId;
    }

    public int getNotifyingUserId() {
        return notifyingUserId;
    }

    public void setNotifyingUserId(@NonNegative final int notifyingUserId) {
        this.notifyingUserId = notifyingUserId;
    }
}
