package com.enoughspam.step.ui.intangible;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;

/**
 * Created by Hugo Castelani
 * Date: 20/11/17
 * Time: 14:43
 */

public interface SnackbarTrigger {
    Snackbar createSnackbar(final @NonNull @StringRes Integer message);
    Snackbar createSnackbar(final @NonNull String message);
    Snackbar buildSnackbar(final @NonNull String message);
    Snackbar createSnackbarAndClose(final @NonNull @StringRes Integer message);
}
