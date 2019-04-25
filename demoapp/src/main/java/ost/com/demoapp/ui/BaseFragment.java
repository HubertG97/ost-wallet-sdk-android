/*
 * Copyright 2019 OST.com Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */

package ost.com.demoapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import ost.com.demoapp.R;
import ost.com.demoapp.customView.AppBar;

/*
 * Abstract Fragment that every other Fragment in this application must implement.
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    private BaseActivity baseActivity = null;
    private AppBar mAppBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            baseActivity = (BaseActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setUpAppBar(@NonNull ViewGroup view) {
        AppBar appBar = new AppBar(baseActivity);
        setUpAppBar(view, appBar);
    }

    protected void setUpAppBar(@NonNull ViewGroup view, @NonNull AppBar appBar) {
        ViewGroup viewGroup = view.findViewById(R.id.layout_holder);
        viewGroup.removeView(mAppBar);
        viewGroup.addView(appBar, 0 ,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mAppBar = appBar;
        mAppBar.setBackButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        showAppBar(true);
    }

    protected void showAppBar(boolean show) {
        if (null != mAppBar) {
            mAppBar.setVisibility(show? View.VISIBLE: View.GONE);
        }
    }

    @Override
    public void showToastMessage(String text) {
        if (null != baseActivity) baseActivity.showToastMessage(text);
    }

    @Override
    public void showToastMessage(int textRes) {
        if (null != baseActivity) baseActivity.showToastMessage(textRes);
    }

    @Override
    public void goBack() {
        if (null != baseActivity) baseActivity.goBack();
    }

    @Override
    public void close() {
        if (null != baseActivity) baseActivity.close();
    }

    @Override
    public void showProgress(boolean show) {
        if (null != baseActivity) baseActivity.showProgress(show);
    }

    public BaseActivity getBaseActivity() {
        return baseActivity;
    }
}