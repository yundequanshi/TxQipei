package com.txsh.friend;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * 获取手机通讯录中存在可以添加好友的手机号
 */
public class MLMobileContactsUtils {
    public static String getContacts(Context context) throws Exception {
        String sb = "";
        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        //获得一个ContentResolver数据共享的对象
        ContentResolver reslover = context.getContentResolver();
        //取得联系人中开始的游标，通过content://com.android.contacts/contacts这个路径获得
        Cursor cursor = reslover.query(uri, null, null, null, null);

        //上边的所有代码可以由这句话代替：Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //Uri.parse("content://com.android.contacts/contacts") == ContactsContract.Contacts.CONTENT_URI

        while (cursor.moveToNext()) {
            //获得联系人ID
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获得联系人姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //获得联系人手机号码
            Cursor phone = reslover.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
            while (phone.moveToNext()) { //取得电话号码(可能存在多个号码)
                int phoneFieldColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String phoneNumber = phone.getString(phoneFieldColumnIndex);
                sb += phoneNumber + ",";
            }
        }
        //Log.d("request", "替换前：" + sb.length());
        sb = sb.replace(" ", "");
        //Log.d("request", "替换后：" + sb.length());
        sb = sb.substring(0, sb.length() - 1);
        return sb;
    }
}
