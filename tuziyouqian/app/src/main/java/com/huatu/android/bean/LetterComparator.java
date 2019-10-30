package com.huatu.android.bean;

import java.util.Comparator;

/**
 * @author 周竹
 * @file LetterComparator
 * @brief
 * @date 2018/4/27 上午10:54
 * Copyright (c) 2017
 * All rights reserved.
 */
public class LetterComparator implements Comparator<ContactsInfo> {
    @Override
    public int compare(ContactsInfo contactModel, ContactsInfo t1) {
        if (contactModel == null || t1 == null) {
            return 0;
        }
        String lhsSortLetters = contactModel.index.substring(0, 1).toUpperCase();
        String rhsSortLetters = t1.index.substring(0, 1).toUpperCase();
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}
