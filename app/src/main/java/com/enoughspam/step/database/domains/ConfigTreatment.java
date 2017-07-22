package com.enoughspam.step.database.domains;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domains.abstracts.CompoundDomain;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 20:41
 */

public class ConfigTreatment extends CompoundDomain {
    public ConfigTreatment(@NonNull @NonNegative final int[] ids) {
        super(ids);
    }
}
