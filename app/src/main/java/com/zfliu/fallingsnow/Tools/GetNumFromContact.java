package com.zfliu.fallingsnow.Tools;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

/**
 * Created by zfliu on 12/24/2016.
 */

public class GetNumFromContact {

    private Activity activity = null;

    public GetNumFromContact(Activity activity){
        this.activity = activity;
    }

    public void getPermission(AcpListener listener){
        Acp.getInstance(activity).request(
                new AcpOptions.Builder().setPermissions(
                        Manifest.permission.READ_CONTACTS).build(),
                listener);
    }

    public void startActivityForResult(){
        Uri uri = Uri.parse("content://contacts/people");
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        activity.startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data,ContactListener listener){
        switch (requestCode){
            case 0:
                if(data==null)
                {
                    listener.onFail();
                    return;
                }
                //处理返回的data,获取选择的联系人信息
                Uri uri=data.getData();
                String[] contacts=getPhoneContacts(uri);
                if (contacts == null || contacts.length != 2){
                    listener.onFail();
                }else{
                    listener.onSucc(contacts[0],contacts[1]);
                }
                break;
        }
    }

    public class ContactListener {
        public void onSucc(String name,String tele){}
        public void onFail(){}
    }

    private String[] getPhoneContacts(Uri uri){
        String[] contact=new String[2];
        //得到ContentResolver对象
        ContentResolver cr = activity.getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor=cr.query(uri,null,null,null,null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex=cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0]=cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if(phone != null){
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        }
        else
        {
            return null;
        }
        return contact;
    }
}
