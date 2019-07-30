/*
 * Copyright 2019 OST.com Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */

package com.ost.walletsdk.ui.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.ost.walletsdk.OstConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.Keys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.ost.walletsdk.OstSdk.getContext;

public class CommonUtils {
    private static final String LOG_TAG = "OstCommonUtils";
    private static final String DATA = "data";

    public CommonUtils() {
    }

    public JSONArray listToJSONArray(List<String> list) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            jsonArray.put(list.get(i));
        }
        return jsonArray;
    }

    public List<String> jsonArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "jsonArray to list exception", e);
        }
        return list;
    }

    public List<String> toCheckSumAddresses(List<String> addressList) {
        for (int i = 0; i < addressList.size(); i++) {
            String address = Keys.toChecksumAddress(addressList.get(i));
            addressList.set(i, address);
        }
        return addressList;
    }

    private static final byte[] nonSecret = ("LETS_CLEAR_BYTES" + String.valueOf((int) (System.currentTimeMillis()))).getBytes();

    public static void clearBytes(byte[] secret) {
        if (null == secret) {
            return;
        }
        for (int i = 0; i < secret.length; i++) {
            secret[i] = nonSecret[i % nonSecret.length];
        }
    }

    public String parseStringResponseForKey(JSONObject jsonObject, String key) {
        try {
            JSONObject resultType = (JSONObject) parseResponseForResultType(jsonObject);
            return resultType.getString(key);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON Exception");
        }
        return null;
    }

    public boolean isValidResponse(JSONObject jsonObject) {
        try {
            if (jsonObject.getBoolean(OstConstants.RESPONSE_SUCCESS)) {
                return true;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON Exception");
        }
        return false;
    }

    public Object parseResponseForResultType(JSONObject jsonObject) throws JSONException {
        if (!isValidResponse(jsonObject)) {
            Log.e(LOG_TAG, "JSON response false");
            return null;
        }
        JSONObject jsonData = jsonObject.getJSONObject(OstConstants.RESPONSE_DATA);
        return jsonData.get(jsonData.getString(OstConstants.RESULT_TYPE));
    }

    public Object parseObjectResponseForKey(JSONObject jsonObject, String key) {
        try {
            JSONObject resultType = (JSONObject) parseResponseForResultType(jsonObject);
            return resultType.get(key);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON Exception");
        }
        return null;
    }

    public JSONObject parseJSONData(JSONObject jsonObject) {
        try {
            return jsonObject.getJSONObject(DATA);
        } catch (JSONException e) {
            return null;
        }
    }

    public JSONObject deepMergeJSONObject(JSONObject firstObject, JSONObject secondObject) {
        JSONObject mergedObj = new JSONObject();

        Iterator firstIterator = firstObject.keys();
        Iterator secondIterator = secondObject.keys();
        String tmp_key;
        try {
            while (firstIterator.hasNext()) {
                tmp_key = (String) firstIterator.next();
                mergedObj.put(tmp_key, firstObject.get(tmp_key));
            }
            while (secondIterator.hasNext()) {
                tmp_key = (String) secondIterator.next();
                if (secondObject.get(tmp_key) instanceof JSONObject) {
                    if (mergedObj.has(tmp_key)) {
                        mergedObj.put(tmp_key, deepMergeJSONObject(mergedObj.getJSONObject(tmp_key), secondObject.getJSONObject(tmp_key)));
                    } else {
                        mergedObj.put(tmp_key, deepMergeJSONObject(new JSONObject(), secondObject.getJSONObject(tmp_key)));
                    }
                } else {
                    mergedObj.put(tmp_key, secondObject.get(tmp_key));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mergedObj;
    }

    public Map<String, Object> convertJsonToMap(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                map.put(key, convertJsonToMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                map.put(key, convertJsonToArray((JSONArray) value));
            } else if (value instanceof Boolean) {
                map.put(key, (Boolean) value);
            } else if (value instanceof Integer) {
                map.put(key, (Integer) value);
            } else if (value instanceof Double) {
                map.put(key, (Double) value);
            } else if (value instanceof String) {
                map.put(key, (String) value);
            } else {
                map.put(key, value.toString());
            }
        }
        return map;

    }

    public List<Object> convertJsonToArray(JSONArray jsonArray) throws JSONException {
        List<Object> array = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                array.add(convertJsonToMap((JSONObject) value));
            } else if (value instanceof  JSONArray) {
                array.add(convertJsonToArray((JSONArray) value));
            } else if (value instanceof  Boolean) {
                array.add((Boolean) value);
            } else if (value instanceof  Integer) {
                array.add((Integer) value);
            } else if (value instanceof  Double) {
                array.add((Double) value);
            } else if (value instanceof String)  {
                array.add((String) value);
            } else {
                array.add(value.toString());
            }
        }
        return array;
    }

    public boolean isBioMetricEnrolled() {
        return isBioMetric(false);
    }

    public boolean isBioMetricHardwareAvailable() {
        return isBioMetric(true);
    }

    private boolean isBioMetric(boolean checkForHardware) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) getContext()
                    .getSystemService(Context.FINGERPRINT_SERVICE);
            if (null != fingerprintManager) {
                if (checkForHardware) {
                    return fingerprintManager.isHardwareDetected();
                } else {
                    return fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints();
                }
            }
        }
        return false;
    }

    public void showEnableBiometricDialog(Activity currentActivity, DialogInterface.OnClickListener onCancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setCancelable(true);
        builder.setMessage("No biometrics available on this device. Please enable via your device settings.");
        builder.setTitle("Enable Biometric");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                currentActivity.startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
            }
        });
        builder.setNegativeButton("Cancel", onCancelListener);
        builder.create().show();
    }
}