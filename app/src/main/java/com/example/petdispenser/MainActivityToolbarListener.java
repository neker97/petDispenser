package com.example.petdispenser;

import android.view.View;

public interface MainActivityToolbarListener {
    void changeTitle(String title);
    void showBackButton(boolean show);
    void goBack();
    void setToggleListner(View.OnClickListener listner);
}
