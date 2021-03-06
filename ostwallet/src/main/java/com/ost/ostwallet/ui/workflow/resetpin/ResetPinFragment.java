/*
 * Copyright 2019 OST.com Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */

package com.ost.ostwallet.ui.workflow.resetpin;


import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ost.ostwallet.R;
import com.ost.ostwallet.ui.BaseFragment;
import com.ost.ostwallet.ui.dashboard.WebViewFragment;
import com.ost.ostwallet.ui.workflow.walletsetup.PinFragment;
import com.ost.ostwallet.util.ChildFragmentUtils;
import com.ost.ostwallet.util.DialogFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPinFragment extends BaseFragment implements ResetPinView,
        PinFragment.OnFragmentInteractionListener {


    ResetPinPresenter mResetPinPresenter = ResetPinPresenter.getInstance();

    public ResetPinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateSessionFragment.
     */
    public static ResetPinFragment newInstance() {
        ResetPinFragment fragment = new ResetPinFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_workflow_holder, container, false);

        mResetPinPresenter.attachView(this);
        mResetPinPresenter.onCreateView();
        return view;
    }

    @Override
    public void showSetNewPin() {
        ChildFragmentUtils.clearBackStackAndAddFragment(R.id.layout_container,
                PinFragment.newInstance("Add New Pin", getResources().getString(R.string.pin_sub_heading_add_pin)),
                this);
    }

    @Override
    public void showRetypePin() {
        ChildFragmentUtils.clearBackStackAndAddFragment(R.id.layout_container,
                PinFragment.newInstance("Confirm New Pin", getResources().getString(R.string.pin_sub_heading_confirm_pin)),
                this);
    }

    @Override
    public void showEnterCurrentPin() {
        ChildFragmentUtils.clearBackStackAndAddFragment(R.id.layout_container,
                PinFragment.newInstance("Enter Current Pin", getResources().getString(R.string.pin_sub_heading_current_pin)),
                this);
    }

    @Override
    public void gotoDashboard(long workflowId) {
        goBack();
    }

    @Override
    public void showPinErrorDialog() {
        Dialog dialog = DialogFactory.createSimpleOkErrorDialog(getContext(),
                "PIN doesn’t match.\n Please try again.",
                "",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mResetPinPresenter.resetResetPin();
                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onPinEntered(String pin) {
        mResetPinPresenter.onPinEntered(pin);
    }

    @Override
    public void openWebView(String url) {
        WebViewFragment fragment = WebViewFragment.newInstance(url);
        ChildFragmentUtils.addFragment(R.id.layout_container,
                fragment,
                this);
    }
}