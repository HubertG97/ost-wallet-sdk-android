/*
 * Copyright 2019 OST.com Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */

package com.ost.walletsdk.ui.recovery;

import com.ost.walletsdk.ui.BaseView;

import org.json.JSONObject;

interface RecoveryView extends BaseView {

    void showEnterPin();

    void gotoDashboard(String workflowId);

    void onPostAuthentication(JSONObject contentConfig);
}