package com.ronaldsantos.pocnfcmifareclassic1k.domain.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;

public class NfcUseCase {

    private final int REQUEST_NFC = 0x1;

    private Context context;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] techList;
    private MifareClassic mifareClassic;

    public NfcUseCase(Context context){
        this.context = context;
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        pendingIntent = PendingIntent.getActivity(context, REQUEST_NFC, new Intent(context, context.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            filter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e){
            e.printStackTrace();
        }
        intentFilters = new IntentFilter[]{filter};
        techList = new String[][]{new String[]{MifareClassic.class.getName()}};
    }

    public Boolean isEnabled() throws NullPointerException {
        if (nfcAdapter == null){
            throw new NullPointerException("NfcAdapter is null");
        }
        return nfcAdapter.isEnabled();
    }

    public void enableForegroundDispatch() throws NullPointerException {
        if (nfcAdapter == null){
            throw new NullPointerException("NfcAdapter is null");
        }
        nfcAdapter.enableForegroundDispatch((Activity) context, pendingIntent, intentFilters, techList);
    }

    public void disableForegroundDispatch() throws NullPointerException {
        if (nfcAdapter == null){
            throw new NullPointerException("NfcAdapter is null");
        }
        nfcAdapter.disableForegroundDispatch((Activity) context);
    }

    public void handleIntent(Intent intent){
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())){
            mifareClassic = MifareClassic.get(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
        }
    }

    public byte[] readBlock(byte[] key, int block){
        if (mifareClassic == null){

        }
    }


}
