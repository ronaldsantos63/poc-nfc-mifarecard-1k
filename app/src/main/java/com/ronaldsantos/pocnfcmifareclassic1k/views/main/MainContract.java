package com.ronaldsantos.pocnfcmifareclassic1k.views.main;

import com.ronaldsantos.pocnfcmifareclassic1k.views.main.model.MainVO;

public interface MainContract {

    interface View{
        void onReadSuccessful(String data);
        void onWriteSuccessful();
        void onChangePermissionsSuccessful();

    }

    interface Presenter{

        void setView(View view);
        void onBtnReadClick(MainVO mainVO);
        void onBtnWriteClick(MainVO mainVO);
        void onBtnChangePermissionsClick(MainVO mainVO);

    }

}
