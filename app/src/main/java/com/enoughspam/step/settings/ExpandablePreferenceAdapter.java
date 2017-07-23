package com.enoughspam.step.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticTextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.enoughspam.step.R;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.DescriptionDAO;
import com.enoughspam.step.database.dao.TreatmentDAO;
import com.enoughspam.step.database.domains.Description;

import java.util.List;

/**
 * Created by hugo
 * Date: 21/07/17
 * Time: 14:34
 */

public class ExpandablePreferenceAdapter extends RecyclerView.Adapter<ExpandablePreferenceAdapter.MyViewHolder> {

    private final DescriptionDAO descriptionDAO;
    private final List<String> descriptionStringList;
    private final List<String> treatmentStringList;

    public ExpandablePreferenceAdapter(@NonNull final Context context) {
        descriptionDAO = new DescriptionDAO(context);
        this.descriptionStringList = descriptionDAO.getColumnList(descriptionDAO.DESCRIPTION);

        final TreatmentDAO treatmentDAO = new TreatmentDAO(context);
        this.treatmentStringList = treatmentDAO.getColumnList(treatmentDAO.TREATMENT);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.preference_expandable_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final String itemText = descriptionStringList.get(position);

        holder.description.setText(itemText);
        holder.description.setOnClickListener(v ->

            new MaterialDialog.Builder(holder.description.getContext())
                    .title(itemText)
                    .items(treatmentStringList)
                    .positiveText(R.string.done_button)
                    .negativeText(R.string.cancel_button)
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
        return (descriptionDAO.findById(position).getTreatmentId());
    }

    private void setSelectedTreatment(@NonNegative int position, @NonNegative int selected) {
        final Description description = descriptionDAO.findById(position);
        description.setTreatmentId(selected);
        descriptionDAO.update(description);
    }

    @Override
    public int getItemCount() {
        return descriptionStringList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        final AestheticTextView description;

        public MyViewHolder(View itemView) {
            super(itemView);
            description = (AestheticTextView) itemView.findViewById(R.id.preference_expandable_item_textview);
        }
    }
}
