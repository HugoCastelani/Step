package com.enoughspam.step.settings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.enoughspam.step.R;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.Description;
import com.enoughspam.step.database.wideDao.DescriptionDAO;
import com.enoughspam.step.database.wideDao.TreatmentDAO;
import com.enoughspam.step.util.ThemeHandler;

import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 21/07/17
 * Time: 14:34
 */

public class ExpandablePreferenceAdapter extends RecyclerView.Adapter<ExpandablePreferenceAdapter.MyViewHolder> {

    private final List<String> mDescriptionStringList;
    private final List<String> mTreatmentStringList;

    public ExpandablePreferenceAdapter() {
        mDescriptionStringList = DescriptionDAO.getColumnList(DescriptionDAO.DESCRIPTION);
        mTreatmentStringList = TreatmentDAO.getColumnList(TreatmentDAO.TREATMENT);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.preference_expandable_item, parent, false);
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

    /*
     * ID isn't automatically generated in description and suspicious_treatment
     * tables, so it doesn't start with 1, which allows me to use list's positions
     */

    private int getSelectedTreatment(@NonNegative int position) {
        return (DescriptionDAO.findById(position).getTreatmentId());
    }

    private void setSelectedTreatment(@NonNegative int position, @NonNegative int selected) {
        final Description description = DescriptionDAO.findById(position);
        description.setTreatmentId(selected);
        DescriptionDAO.update(description);
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
