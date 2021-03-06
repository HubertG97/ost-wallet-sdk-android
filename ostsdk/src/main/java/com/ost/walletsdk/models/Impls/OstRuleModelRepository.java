/*
 * Copyright 2019 OST.com Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */

package com.ost.walletsdk.models.Impls;

import com.ost.walletsdk.database.OstSdkDatabase;
import com.ost.walletsdk.database.daos.OstBaseDao;
import com.ost.walletsdk.database.daos.OstRuleDao;
import com.ost.walletsdk.models.OstRuleModel;
import com.ost.walletsdk.models.entities.OstRule;

import org.web3j.crypto.Keys;

class OstRuleModelRepository extends OstBaseModelCacheRepository implements OstRuleModel {

    private static final int LRU_CACHE_SIZE = 5;
    private OstRuleDao mOstRuleDao;

    OstRuleModelRepository() {
        super(LRU_CACHE_SIZE);
        OstSdkDatabase db = OstSdkDatabase.getDatabase();
        mOstRuleDao = db.ruleDao();
    }

    @Override
    OstBaseDao getModel() {
        return mOstRuleDao;
    }

    @Override
    public OstRule getEntityById(String id) {
        return (OstRule)super.getById(Keys.toChecksumAddress(id));
    }

    @Override
    public OstRule[] getEntitiesByParentId(String id) {
        return (OstRule[]) super.getByParentId(id);
    }
}
