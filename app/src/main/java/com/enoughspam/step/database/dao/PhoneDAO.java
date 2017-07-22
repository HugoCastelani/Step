package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.abstracts.DAO;
import com.enoughspam.step.database.domains.Phone;
import com.enoughspam.step.database.domains.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 28/06/17
 * Time: 09:20
 */

public class PhoneDAO extends DAO {

    private static final String NUMBER = "number";
    private static final String COUNTRYID = "country_id";
    private static final String AREACODE = "area_code";
    private static final String USERID = "user_id";

    private PersonalDAO personalDAO;

    public PhoneDAO(@NonNull Context context) {
        super(context, "phone", "id");
        personalDAO = new PersonalDAO(context);
    }

    @Override
    public Phone generate(@NonNull Cursor cursor) {
        if (cursor.getInt(cursor.getColumnIndex(AREACODE)) == 0) {
            return new Phone(
                    super.generate(cursor).getId(),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    cursor.getInt(cursor.getColumnIndex(AREACODE)),
                    personalDAO.get()
            );

        } else {

            return new Phone(
                    super.generate(cursor).getId(),
                    cursor.getInt(cursor.getColumnIndex(COUNTRYID)),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    personalDAO.get()
            );
        }
    }

    @Override
    public boolean create(@NonNull Domain domain) {
        Phone phone = (Phone) domain;
        ContentValues values = new ContentValues();

        if (phone.getAreaCode() == 0) {
            values.put(NUMBER, phone.getNumber());
            values.put(COUNTRYID, phone.getCountryId());
            values.put(USERID, phone.getUser().getId());

        } else {

            values.put(NUMBER, phone.getNumber());
            values.put(AREACODE, phone.getAreaCode());
            values.put(USERID, phone.getUser().getId());
        }

        return getSqLiteDatabase().insert(TABLE, null, values) > 0;
    }

    @Override
    public Phone findById(@NonNegative int id) {
        return (Phone) super.findById(id);
    }
}
