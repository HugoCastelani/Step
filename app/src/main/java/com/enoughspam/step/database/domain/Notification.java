package com.enoughspam.step.database.domain;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 31/07/17
 * Time: 16:05
 */

public class Notification extends Domain {
    private Integer phoneId;
    private Integer notifiedUserId;
    private Integer notifyingUserId;

    public Notification() {}

    public Notification(@NonNegative final Integer id, @NonNegative final Integer phoneId,
                        @NonNegative final Integer notifiedUserId, @NonNegative final Integer notifyingUserId) {
        super(id);
        this.phoneId = phoneId;
        this.notifiedUserId = notifiedUserId;
        this.notifyingUserId = notifyingUserId;
    }

    public Integer getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(@NonNegative final Integer phoneId) {
        this.phoneId = phoneId;
    }

    public Integer getNotifiedUserId() {
        return notifiedUserId;
    }

    public void setNotifiedUserId(@NonNegative final Integer notifiedUserId) {
        this.notifiedUserId = notifiedUserId;
    }

    public Integer getNotifyingUserId() {
        return notifyingUserId;
    }

    public void setNotifyingUserId(@NonNegative final Integer notifyingUserId) {
        this.notifyingUserId = notifyingUserId;
    }
}
