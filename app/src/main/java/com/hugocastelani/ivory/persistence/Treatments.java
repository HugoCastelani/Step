package com.hugocastelani.ivory.persistence;

import android.support.annotation.NonNull;

/**
 * Created by Hugo Castelani
 * Date: 02/12/17
 * Time: 18:25
 */

public class Treatments {
    private final TreatmentsEnum option;

    public Treatments(@NonNull final Integer option) {
        switch (option) {
            case 0: this.option = TreatmentsEnum.DONOTHING;
                break;
            case 1: this.option = TreatmentsEnum.SILENCE;
                break;
            case 2: this.option = TreatmentsEnum.BLOCK;
                break;
            default: this.option = null;
        }
    }

    public TreatmentsEnum getOption() {
        return option;
    }

    public enum TreatmentsEnum {
        DONOTHING, SILENCE, BLOCK
    }
}
