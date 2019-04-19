package com.lib.core.utils;

import android.view.View;

/**
 * @author 周竹
 * @file OnItemClickListener
 * @brief
 * @date 2018/3/6 上午1:17
 * Copyright (c) 2017
 * All rights reserved.
 */

public interface OnItemClickListener<T> {
    void onItemClick(View v, int position, T t);
}
