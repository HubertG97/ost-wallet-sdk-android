/*
 * Copyright 2019 OST.com Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */

package com.ost.ostwallet.uicomponents.uiutils;

import android.graphics.Typeface;

public interface Font {
    Typeface getRegular();

    Typeface getLightItalic();

    Typeface getLight();

    Typeface getItalic();

    Typeface getBoldItalic();

    Typeface getBold();
}