package com.hugocastelani.ivory.ui.settings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hugocastelani.ivory.R;
import com.hugocastelani.ivory.annotation.NonNegative;
import com.hugocastelani.ivory.persistence.HockeyProvider;
import com.hugocastelani.ivory.ui.viewholder.DenunciationDescriptionViewHolder;
import com.hugocastelani.ivory.util.ThemeHandler;
import com.orhanobut.hawk.Hawk;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 21/07/17
 * Time: 14:34
 */

public final class ExpandablePreferenceAdapter extends RecyclerView.Adapter<DenunciationDescriptionViewHolder> {

    private List<String> mDescriptionStringList;
    private List<String> mTreatmentStringList;

    public ExpandablePreferenceAdapter(@NonNull final View view) {
        mDescriptionStringList = Arrays.asList(
                view.getResources().getStringArray(R.array.description_list_titles));
        mTreatmentStringList = Arrays.asList(
                view.getResources().getStringArray(R.array.treatment_list_titles));
    }

    @Override
    public DenunciationDescriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.preference_expandable_item, parent, false);
        return new DenunciationDescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DenunciationDescriptionViewHolder holder, final int position) {
        final String itemText = mDescriptionStringList.get(position);

        holder.description.setText(itemText);
        holder.description.setOnClickListener(v ->

            new MaterialDialog.Builder(holder.description.getContext())
                    .title(itemText)
                    .items(mTreatmentStringList)
                    .backgroundColor(ThemeHandler.getBackground())
                    .positiveText(R.string.done_button)
                    .positiveColor(ThemeHandler.getAccent())
                    .negativeText(R.string.cancel_button)
                    .negativeColor(ThemeHandler.getAccent())
                    .itemsCallbackSingleChoice(getSelectedTreatment(position),
                            ((dialog, itemView, which, text) -> true))
                    .onPositive(((dialog, which) ->
                            setSelectedTreatment(position, dialog.getSelectedIndex())))
                    .show()
        );
    }

    private int getSelectedTreatment(@NonNegative @NonNull final Integer selectedDescription) {
        return Hawk.get(HockeyProvider.DESCRIPTION + selectedDescription, HockeyProvider.DESCRIPTION_DF);
    }

    private void setSelectedTreatment(@NonNegative @NonNull final Integer selectedDescription,
                                      @NonNegative @NonNull final Integer selectedTreatment) {
        Hawk.put(HockeyProvider.DESCRIPTION + selectedDescription, selectedTreatment);
    }

    @Override
    public int getItemCount() {
        return mDescriptionStringList.size();
    }
}
