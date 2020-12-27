package com.ronaldsantos.pocnfcmifareclassic1k.views.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ronaldsantos.pocnfcmifareclassic1k.views.main.MainActivity;
import com.ronaldsantos.pocnfcmifareclassic1k.views.main.model.MainVO;

public class MainActivityViewModel extends ViewModel {

    public final MutableLiveData<MainVO> mainVO = new MutableLiveData<>(new MainVO());


}
