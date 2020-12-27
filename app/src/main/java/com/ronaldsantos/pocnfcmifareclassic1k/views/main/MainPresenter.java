package com.ronaldsantos.pocnfcmifareclassic1k.views.main;

import com.ronaldsantos.pocnfcmifareclassic1k.views.main.model.MainVO;

public class MainPresenter implements MainContract.Presenter{

    private MainContract.View view;

    @Override
    public void setView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void onBtnReadClick(MainVO mainVO) {
        view.onReadSuccessful(mainVO.toString());
    }

    @Override
    public void onBtnWriteClick(MainVO mainVO) {

    }

    @Override
    public void onBtnChangePermissionsClick(MainVO mainVO) {

    }
}
