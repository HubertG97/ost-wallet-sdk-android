/*
 * Copyright 2019 OST.com Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */

package com.ost.walletsdk.ui.walletsetup;


import android.app.Activity;

import com.ost.walletsdk.ui.BaseView;

interface SetUpView extends BaseView {

    void showAddPin();

    void showRetypePin();

    void gotoDashboard(String workflowId);

    void showPinErrorDialog();

    Activity getCurrentActivity();

    void onInitialize();
}
