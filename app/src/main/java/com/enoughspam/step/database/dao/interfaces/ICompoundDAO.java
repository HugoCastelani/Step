package com.enoughspam.step.database.dao.interfaces;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 20:17
 */

public interface ICompoundDAO<T> {
    boolean delete(@NonNull @NonNegative final int[] ids);
    T findByIds(@NonNull @NonNegative final int[] ids);
}
