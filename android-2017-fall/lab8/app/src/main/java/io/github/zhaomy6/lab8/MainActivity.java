package io.github.zhaomy6.lab8;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.LinkedList;

public class MainActivity
        extends Activity
        implements View.OnClickListener,
            AdapterView.OnItemClickListener,
            AdapterView.OnItemLongClickListener {
    private DBHelper dbHelper = null;
    private ListView lv = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        this.dbHelper = new DBHelper(this, "Lab8DB", null, 1);
        LinkedList<Item> itemList = dbHelper.getAll();
        ItemAdapter ia = new ItemAdapter(itemList, this);
        this.lv = (ListView) findViewById(R.id.lv);
        this.lv.setAdapter(ia);
        this.lv.setOnItemClickListener(this);
        this.lv.setOnItemLongClickListener(this);

        Button btn = (Button) findViewById(R.id.add_btn);
        btn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            Item newItem = new Item(bundle.getString("name"),
                    bundle.getString("birth"),
                    bundle.getString("gift"));
            this.dbHelper.insertDBItem(newItem);
            ItemAdapter ia = (ItemAdapter) this.lv.getAdapter();
            ia.getList().add(newItem);
            ia.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_btn) {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final ItemAdapter ia = (ItemAdapter) parent.getAdapter();
        final Item item = (Item) ia.getItem(position);
        final LinkedList<Item> l = ia.getList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否删除?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteDBItem(item.getName());
                        l.remove(position);
                        ia.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                }).show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        View v = factory.inflate(R.layout.dialog_layout, null);

        final ItemAdapter ia = (ItemAdapter) parent.getAdapter();
        final Item item = (Item) ia.getItem(position);
        final LinkedList<Item> l = ia.getList();
        final EditText ed1 = (EditText) v.findViewById(R.id.row1_a);
        final EditText ed2 = (EditText) v.findViewById(R.id.row2_a);
        final EditText ed3 = (EditText) v.findViewById(R.id.row3_a);
        final EditText ed4 = (EditText) v.findViewById(R.id.row4_a);
        ed1.setText(item.getName());
        ed2.setText(item.getBirthday());
        ed3.setText(item.getGift());

        //  获取用户手机号
        String n = "";
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] { ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER },
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "=?" + " AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + "='" +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE + "'",
                new String[] {item.getName()}, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String number = cursor.getString(2);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(number))
                    continue;
                // 得到联系人名称
                String username = cursor.getString(1);
                if (username.equals(item.getName())) {
                    n += number;
                }
            }
            cursor.close();
        }
        if (!"".equals(n)) {
            ed4.setText(n);
        } else {
            ed4.setText(R.string.no_phone_number);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(v)
            .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String originName = ed1.getText().toString();
                    String birth = ed2.getText().toString();
                    String gift = ed3.getText().toString();
                    dbHelper.updateDBItem(originName,
                            new Item(originName, birth, gift));
                    for (Item ele : l) {
                        if (ele.getName().equals(originName)) {
                            ele.setBirthday(birth);
                            ele.setGift(gift);
                        }
                    }
                    ia.notifyDataSetChanged();
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //  pass
                }
            }).create().show();
    }
}
