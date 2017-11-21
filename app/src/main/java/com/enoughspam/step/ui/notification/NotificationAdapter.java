package com.enoughspam.step.ui.notification;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.wide.NotificationDAO;
import com.enoughspam.step.ui.intangible.UsersNumbersAdapter;
import com.enoughspam.step.util.Listeners;

/**
 * Created by Hugo Castelani
 * Date: 20/11/17
 * Time: 18:09
 */

public class NotificationAdapter extends UsersNumbersAdapter {

    public NotificationAdapter(@NonNull final NotificationFragment fragment) {
        super(fragment);
        NotificationDAO.get().getNotificationList(getListListener(), getAnswerListener());
    }

    @Override
    protected void removeItemFromDatabase(@NonNull String phoneKey) {
        NotificationDAO.get().delete(phoneKey, new Listeners.AnswerListener() {
            @Override public void onAnswerRetrieved() {}
            @Override public void onError() {}
        });
    }

    @Override
    protected Boolean isSwipeable() {
        return true;
    }

    @Override
    protected void onClick() {

    }

    @Override
    protected void onLongClick() {

    }
}
