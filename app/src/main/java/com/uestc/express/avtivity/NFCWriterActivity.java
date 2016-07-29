package com.uestc.express.avtivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.uestc.express.R;

import java.nio.charset.Charset;
import java.util.Locale;

public class NFCWriterActivity extends BaseActivity {

    public static void startActivity(Activity activity, String text) {
        Intent intent = new Intent(activity, NFCWriterActivity.class);
        intent.putExtra("text", text);
        activity.startActivity(intent);
    }

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;

    String mText;
    boolean canWrite = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcwriter);

        mText = getIntent().getStringExtra("text");
        if (mText == null) {
            Toast.makeText(this, getText(R.string.error_nfc_writer), Toast.LENGTH_SHORT).show();
            finish();
        }

        mAdapter = NfcAdapter.getDefaultAdapter(this);

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (!canWrite) {
            return;
        }

        canWrite = false;
        showProgress("正在写入，请稍后");
        //获取Tag对象
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //创建NdefMessage对象和NdefRecord对象
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{createTextRecord(mText)});
        //开始向标签写入文本
        if (writeTag(ndefMessage, tag)) {
            finish();
        } else {
            canWrite = true;
            dismissProgress();
        }
    }

    //创建一个封装要写入的文本的NdefRecord对象
    public NdefRecord createTextRecord(String text) {
        //生成语言编码的字节数组，中文编码
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
        //将要写入的文本以UTF_8格式进行编码
        Charset utfEncoding = Charset.forName("UTF-8");
        //由于已经确定文本的格式编码为UTF_8，所以直接将payload的第1个字节的第7位设为0
        byte[] textBytes = text.getBytes(utfEncoding);
        int utfBit = 0;
        //定义和初始化状态字节
        char status = (char) (utfBit + langBytes.length);
        //创建存储payload的字节数组
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        //设置状态字节
        data[0] = (byte) status;
        //设置语言编码
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        //设置实际要写入的文本
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
                textBytes.length);
        //根据前面设置的payload创建NdefRecord对象
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return record;
    }

    //将NdefMessage对象写入标签，成功写入返回ture，否则返回false
    boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;

        try {
            //获取Ndef对象
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                //允许对标签进行IO操作
                ndef.connect();

                if (!ndef.isWritable()) {
                    Toast.makeText(this, "NFC Tag是只读的！", Toast.LENGTH_LONG)
                            .show();
                    return false;

                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(this, "NFC Tag的空间不足！", Toast.LENGTH_LONG)
                            .show();
                    return false;
                }

                //向标签写入数据
                ndef.writeNdefMessage(message);
                Toast.makeText(this, "已成功写入数据！", Toast.LENGTH_LONG).show();
                return true;

            } else {
                //获取可以格式化和向标签写入数据NdefFormatable对象
                NdefFormatable format = NdefFormatable.get(tag);
                //向非NDEF格式或未格式化的标签写入NDEF格式数据
                if (format != null) {
                    try {
                        //允许对标签进行IO操作
                        format.connect();
                        format.format(message);
                        Toast.makeText(this, "已成功写入数据！", Toast.LENGTH_LONG).show();
                        return true;

                    } catch (Exception e) {
                        Toast.makeText(this, "写入NDEF格式数据失败！", Toast.LENGTH_LONG).show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "NFC标签不支持NDEF格式！", Toast.LENGTH_LONG).show();
                    return false;

                }
            }
        } catch (Exception e) {
            Log.i("nfc","noknown error");
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }

    }

}
