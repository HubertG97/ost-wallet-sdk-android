/*
 * Copyright 2019 OST.com Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */

package com.ost.walletsdk.network;

import android.util.Log;

import com.ost.walletsdk.OstConstants;
import com.ost.walletsdk.OstSdk;
import com.ost.walletsdk.ecKeyInteracts.OstApiSigner;
import com.ost.walletsdk.models.entities.OstDevice;
import com.ost.walletsdk.models.entities.OstDeviceManager;
import com.ost.walletsdk.models.entities.OstDeviceManagerOperation;
import com.ost.walletsdk.models.entities.OstRule;
import com.ost.walletsdk.models.entities.OstSession;
import com.ost.walletsdk.models.entities.OstToken;
import com.ost.walletsdk.models.entities.OstTokenHolder;
import com.ost.walletsdk.models.entities.OstTransaction;
import com.ost.walletsdk.models.entities.OstUser;
import com.ost.walletsdk.workflows.errors.OstErrors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class OstApiHelper implements OstHttpRequestClient.ResponseParser {
    private static final String TAG = "OstApiHelper";

    String mUserId;
    OstApiHelper(String userId) {
        mUserId = userId;
    }

    @Override
    public void parse(JSONObject jsonObject) {
        updateWithApiResponse(jsonObject);
    }

    synchronized private void updateWithApiResponse(JSONObject jsonObject) {
        if ( null == jsonObject || !jsonObject.optBoolean(OstConstants.RESPONSE_SUCCESS) ) {
            OstApiError apiError =  new OstApiError("nw_api_helper_uwapir_1", OstErrors.ErrorCode.OST_PLATFORM_API_ERROR, jsonObject);
            if ( apiError.isApiSignerUnauthorized() ) {
                try {
                    OstApiSigner signer = new OstApiSigner(mUserId);
                    signer.apiSignerUnauthorized(apiError);
                } catch (Throwable th) {
                    //Do Nothing.
                }
            }
            throw apiError;
        }

        try {
            JSONObject jsonData = jsonObject.getJSONObject(OstConstants.RESPONSE_DATA);

            if (jsonData.has(OstSdk.USER)) {
                OstUser.parse(jsonData.getJSONObject(OstSdk.USER));
            }
            if (jsonData.has(OstSdk.TRANSACTION)) {
                OstTransaction.parse(jsonData.getJSONObject(OstSdk.TRANSACTION));
            }
            if (jsonData.has(OstSdk.TOKEN_HOLDER)) {
                OstTokenHolder.parse(jsonData.getJSONObject(OstSdk.TOKEN_HOLDER));
            }
            if (jsonData.has(OstSdk.TOKEN)) {
                OstToken.parse(jsonData.getJSONObject(OstSdk.TOKEN));
            }
            if (jsonData.has(OstSdk.SESSION)) {
                OstSession.parse(jsonData.getJSONObject(OstSdk.SESSION));
            }
            if (jsonData.has(OstSdk.SESSIONS)) {
                JSONArray jsonArray = jsonData.getJSONArray(OstSdk.SESSIONS);
                for (int i = 0; i < jsonArray.length(); i++) {
                    OstRule.parse(jsonArray.getJSONObject(i));
                }
            }
            if (jsonData.has(OstSdk.RULE)) {
                OstRule.parse(jsonData.getJSONObject(OstSdk.RULE));
            }
            if (jsonData.has(OstSdk.RULES)) {
                JSONArray jsonArray = jsonData.getJSONArray(OstSdk.RULES);
                for (int i = 0; i < jsonArray.length(); i++) {
                    OstRule.parse(jsonArray.getJSONObject(i));
                }
            }
            if (jsonData.has(OstSdk.DEVICE_OPERATION)) {
                OstDeviceManagerOperation.parse(jsonData.getJSONObject(OstSdk.DEVICE_OPERATION));
            }
            if (jsonData.has(OstSdk.DEVICE_MANAGER)) {
                OstDeviceManager.parse(jsonData.getJSONObject(OstSdk.DEVICE_MANAGER));
            }
            if (jsonData.has(OstSdk.DEVICE)) {
                OstDevice.parse(jsonData.getJSONObject(OstSdk.DEVICE));
            }
            if (jsonData.has(OstSdk.DEVICES)) {
                JSONArray jsonArray = jsonData.getJSONArray(OstSdk.DEVICES);
                for (int i = 0; i < jsonArray.length(); i++) {
                    OstDevice.parse(jsonArray.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSONException");
        }
    }
}