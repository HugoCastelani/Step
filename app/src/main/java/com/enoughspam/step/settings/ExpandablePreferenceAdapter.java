package com.enoughspam.step.settings;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.enoughspam.step.R;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.util.ThemeHandler;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 21/07/17
 * Time: 14:34
 */

public class ExpandablePreferenceAdapter extends RecyclerView.Adapter<ExpandablePreferenceAdapter.MyViewHolder> {
    private View mView;

    private List<String> mDescriptionStringList;
    private List<String> mTreatmentStringList;

    public ExpandablePreferenceAdapter(@NonNull final View view) {
        mView = view;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.preference_expandable_item, parent, false);

        mDescriptionStringList = Arrays.asList(
                view.getResources().getStringArray(R.array.description_list_titles));
        mTreatmentStringList = Arrays.asList(
                view.getResources().getStringArray(R.array.treatment_list_titles));

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
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

    // send selected treatment to shared preferences

    private int getSelectedTreatment(@NonNegative final int selectedDescription) {
        return PreferenceManager.getDefaultSharedPreferences(mView.getContext())
                .getInt("description_" + selectedDescription, -1);
    }

    private void setSelectedTreatment(@NonNegative final int selectedDescription,
                                      @NonNegative final int selectedTreatment) {
        PreferenceManager.getDefaultSharedPreferences(mView.getContext())
                .edit().putInt("description_" + selectedDescription, selectedTreatment).apply();
    }

    @Override
    public int getItemCount() {
        return mDescriptionStringList.size();
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder {
        final AestheticTextView description;

        public MyViewHolder(View itemView) {
            super(itemView);
            description = (AestheticTextView) itemView.findViewById(R.id.preference_expandable_item_text_view);
        }
    }
}
