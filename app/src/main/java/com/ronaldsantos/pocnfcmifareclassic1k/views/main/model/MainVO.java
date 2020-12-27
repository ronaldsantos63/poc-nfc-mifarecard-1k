package com.ronaldsantos.pocnfcmifareclassic1k.views.main.model;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainVO {

    private Boolean keyASelected = true;
    private Boolean keyBSelected = false;
    private String block = "";
    private String value = "";

    public MainVO() {
    }

    public Boolean getKeyASelected() {
        return keyASelected;
    }

    public void setKeyASelected(Boolean keyASelected) {
        this.keyASelected = keyASelected;
    }

    public Boolean getKeyBSelected() {
        return keyBSelected;
    }

    public void setKeyBSelected(Boolean keyBSelected) {
        this.keyBSelected = keyBSelected;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MainVO)) return false;
        MainVO mainVO = (MainVO) o;
        return Objects.equals(getKeyASelected(), mainVO.getKeyASelected()) &&
                Objects.equals(getKeyBSelected(), mainVO.getKeyBSelected()) &&
                Objects.equals(getBlock(), mainVO.getBlock()) &&
                Objects.equals(getValue(), mainVO.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKeyASelected(), getKeyBSelected(), getBlock(), getValue());
    }

    @NotNull
    @Override
    public String toString() {
        return "MainVO{" +
                "keyASelected=" + keyASelected +
                ", keyBSelected=" + keyBSelected +
                ", block='" + block + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
