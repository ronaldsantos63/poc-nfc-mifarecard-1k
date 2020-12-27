package com.ronaldsantos.pocnfcmifareclassic1k.views.main;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.TagLostException;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.ronaldsantos.pocnfcmifareclassic1k.R;
import com.ronaldsantos.pocnfcmifareclassic1k.databinding.ActivityMainBinding;
import com.ronaldsantos.pocnfcmifareclassic1k.utils.Helper;
import com.ronaldsantos.pocnfcmifareclassic1k.views.main.viewmodel.MainActivityViewModel;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private final int REQUEST_NFC = 0x1;

//    private RadioButton radioKeyA;
//    private RadioButton radioKeyB;
//    private EditText edtBlock;
//    private EditText edtValue;
//    private Button btnRead;
//    private Button btnWrite;
//    private Button btnChangePermissions;


    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] techList;
    private MifareClassic mifareClassic;

    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;
    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        presenter = new MainPresenter();
        presenter.setView(this);
        binding.setPresenter(presenter);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.setMainViewModel(viewModel);

        setupNfc();

//        bindView();

        bindListeners();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatch();
    }

    @Override
    protected void onPause() {
        disableForegroundDispatch();
        super.onPause();
    }

    private void setupNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null){
            showMessage("Você não tem suporte ao NFC!", ((dialog, which) -> finish()));
            return;
        }

        if (!nfcAdapter.isEnabled()){
            showMessage("É necessário habilitar o Nfc!");
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, REQUEST_NFC, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            filter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e){
            e.printStackTrace();
        }
        intentFilters = new IntentFilter[]{filter};
        techList = new String[][]{new String[]{MifareClassic.class.getName()}};
    }

    private void enableForegroundDispatch(){
        if (nfcAdapter != null){
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, techList);
        }
    }

    private void disableForegroundDispatch(){
        if (nfcAdapter != null){
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    private void handleIntent(Intent intent){
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())){
            mifareClassic = MifareClassic.get(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
        }
    }

    private byte[] readNfc(byte[] key, int blockIndex){
        if (blockIndex < 0){
            showMessage("Block não pode ser menor que zero");
            return null;
        }

        if (mifareClassic == null){
            showMessage("Nenhuma tag encontrada!");
            return null;
        }

        try {
            if (!mifareClassic.isConnected()){
                mifareClassic.connect();
            }

            int sectorIndex = mifareClassic.blockToSector(blockIndex);
            boolean auth = false;
            if (mifareClassic.authenticateSectorWithKeyA(sectorIndex, key)){
                auth = true;
            } else if (mifareClassic.authenticateSectorWithKeyB(sectorIndex, key)){
                auth = true;
            }

            if (auth){
                return mifareClassic.readBlock(blockIndex);
            }
            return null;

        } catch (TagLostException e){
            showMessage("Não mexa o cartão durante a leitura!");
            return null;
        }
        catch (IOException e){
            showMessage("Ocorreu um erro ao ler cartão nfc");
            return null;
        } finally {
            try {
                mifareClassic.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String writeNfc(byte[] key, byte[] data, int blockIndex){
        if (blockIndex < 0){
            showMessage("Block não pode ser menor que zero");
            return null;
        }

        if (mifareClassic == null){
            return "Nenhuma tag encontrada!";
        }

        try {
            if (!mifareClassic.isConnected()){
                mifareClassic.connect();
            }

            int sectorIndex = mifareClassic.blockToSector(blockIndex);
            boolean auth = false;
            if (mifareClassic.authenticateSectorWithKeyA(sectorIndex, key)){
                auth = true;
            } else if (mifareClassic.authenticateSectorWithKeyB(sectorIndex, key)){
                auth = true;
            }

            if (auth){
                mifareClassic.writeBlock(blockIndex, data);
                return "Escrito com sucesso!";
            }
            return "Erro de autenticação";

        } catch (TagLostException e){
            return "Não mexa o cartão durante a leitura!";
        }
        catch (IOException e){
            return "Ocorreu um erro ao escrever no cartão nfc";
        } finally {
            try {
                mifareClassic.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void bindListeners() {
//        binding.btnRead.setOnClickListener(v -> {
//            if (binding.edtBlock.getText().toString().isEmpty()){
//                binding.edtBlock.setError("Campo obrigatório");
//                return;
//            }
//            int bloco = Integer.parseInt(binding.edtBlock.getText().toString());
//
//            byte[] key = getKeyForAuthentication();
//
//
//            byte[] data = readNfc(key, bloco);
//            if (data != null){
//                showMessage(Helper.convertBytes2String(data, false));
//                binding.edtValue.setText(Helper.convertBytes2String(data, false));
//            }
//        });
        binding.btnWrite.setOnClickListener(v -> {
            if (binding.edtBlock.getText().toString().isEmpty()){
                binding.edtBlock.setError("Campo obrigatório!");
                return;
            }
            int bloco = Integer.parseInt(binding.edtBlock.getText().toString());

            if (binding.edtValue.getText().toString().isEmpty()){
                binding.edtValue.setError("Campo obrigatório!");
                return;
            }

            String result = writeNfc(getKeyForAuthentication(), Helper.convertString2Bytes(binding.edtValue.getText().toString()), bloco);
            showMessage(result);
        });
        binding.btnChangePermissions.setOnClickListener(v -> {
            if (binding.edtBlock.getText().toString().isEmpty()){
                binding.edtBlock.setError("Campo obrigatório!");
                return;
            }
            int bloco = Integer.parseInt(binding.edtBlock.getText().toString());
            int trailerBlock = getTrailerBlock(bloco);
            byte[] keyB = getKeyB();
            try {
                byte[] dataAccessForSector = getPermission(MifareClassic.KEY_DEFAULT, keyB);
                String result = writeNfc(MifareClassic.KEY_DEFAULT, dataAccessForSector, trailerBlock);
                showMessage(result);
            }catch (IOException e){
                showMessage(e.getMessage());
            }

        });
    }

    private byte[] getKeyForAuthentication() {
        byte[] key;
        if (binding.radioKeyA.isChecked()){
            key = MifareClassic.KEY_DEFAULT;
        } else {
            key = getKeyB();
        }
        return key;
    }

    private int getTrailerBlock(int bloco){

        for (int trailerBlock = bloco; trailerBlock < 36; trailerBlock++){
            if ((trailerBlock + 1) % 4 == 0){
                return trailerBlock;
            }
        }

        return -1;

    }

    private byte[] getKeyB(){
        byte[] keyBFull = Helper.convertString2Bytes("eXBTL4"); //TODO: Chave personalizada para escrever no cartão nfc
        byte[] keyB = new byte[6];
        System.arraycopy(keyBFull, 0, keyB, 0, 6);
        return keyB;
    }

    private byte[] getPermission(byte[] keyA, byte[] keyB) throws IOException {
        if (keyA == null || keyB == null) {
            throw new IOException("KeyA e KeyB não podem ser nulos");
        }

        byte[] ret = new byte[16];
        byte[] permissions = {(byte) 0x78, (byte) 0x77, (byte) 0x88, (byte) 0x00};

        System.arraycopy(keyA, 0, ret, 0, 6);
        System.arraycopy(permissions, 0, ret, 6, 4);
        System.arraycopy(keyB, 0, ret, 10, 6);
        return ret;
    }

    public void showMessage(String message, DialogInterface.OnClickListener callBack){
        new AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton("Ok", callBack)
        .show();
    }

    public void showMessage(String message){
        showMessage(message, null);
    }

    @Override
    public void onReadSuccessful(String data) {
        showMessage(data);
    }

    @Override
    public void onWriteSuccessful() {

    }

    @Override
    public void onChangePermissionsSuccessful() {

    }
}