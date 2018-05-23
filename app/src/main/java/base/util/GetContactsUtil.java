package base.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.nevermore.muzhitui.R;
import com.nevermore.muzhitui.module.bean.ContactsList;

import java.util.ArrayList;

/**
 * Created by Simone on 2016/12/23.
 * 获取手机联系人
 */

public class GetContactsUtil {

    public static ArrayList getAllContacts(Context context) {
        ArrayList contacts = new ArrayList();
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //新建一个联系人实例
            ContactsList.PhoneListBean temp = new ContactsList.PhoneListBean();
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            temp.setTarget_name(name);
            //获取联系人所有电话号
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            while (phones.moveToNext()) {

                temp.setTarget_phone(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                   /* String phoneNumber = phones .getString(phones .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    temp.phones.add(phoneNumber);*/
            }
            phones.close();
            //获取联系人所有邮箱.
              /*  Cursor emails = context.getContentResolver().query( ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
                while (emails.moveToNext()) {
                    String email = emails .getString(emails .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    temp.emails.add(email);
                }
                emails.close();*/
            //获取联系人头像
//            temp.setPhoto(getContactPhoto(context, contactId, R.mipmap.ic_launcher));

            contacts.add(temp);
        }
        return contacts;
    }
}
