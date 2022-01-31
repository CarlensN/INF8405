package com.example.myapplication.Class;

public class MovesListener {
    public interface OnIntegerChangeListener{
        void OnIntegerChanged(int value);
    }

    private OnIntegerChangeListener listener;
    private int value;

    public void setOnIntegerChangeListener(OnIntegerChangeListener listener){
        this.listener = listener;
    }

    public int get(){
        return value;
    }

    public void set(int value){
        this.value = value;

        if (listener != null){
            listener.OnIntegerChanged(value);
        }
    }
}
