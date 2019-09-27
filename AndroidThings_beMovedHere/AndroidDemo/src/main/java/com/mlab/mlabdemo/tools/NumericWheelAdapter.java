/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.mlab.mlabdemo.tools;

import android.util.Log;

import java.util.List;

/**
 * Author: HF
 * Date:   2015-12-29
 * Description:
 */
public class NumericWheelAdapter implements WheelAdapter {
    public List<String> showStrListData;
    /**
     * 判断 滚轮中的是 字符串还是整形的数字；
     * true——字符串；若为 true 时，给出的 最大值 最小值 无效的；不会用到
     * int——数字 可以计算，如 给出最大值，最小值，和间隔。可自动计算。小：100；大：200；间隔50；
     * 则 滚轮中 则显示 100,150,200；
     */
    public boolean showStrOrInt;

    /**
     * The default min value
     */
    public static final int DEFAULT_MAX_VALUE = WheelViewConfig.DEFAULT_MAX_VALUE;

    /**
     * The default max value
     */
    private static final int DEFAULT_MIN_VALUE = WheelViewConfig.DEFAULT_MIN_VALUE;

    // Values
    private int minValue;
    private int maxValue;

    // format
    private String format;

    /**
     * Default constructor
     */
    public NumericWheelAdapter() {
        this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
    }

    /**
     * Constructor
     *
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     */
    public NumericWheelAdapter(int minValue, int maxValue) {
        this(minValue, maxValue, null);
    }

    /**
     * ljw 添加的方法
     * Constructor
     *
     * @param minValue     the wheel min value
     * @param maxValue     the wheel max value
     * @param showStrOrInt true 代表 显示的是字符串
     */
    public NumericWheelAdapter(int minValue, int maxValue, boolean showStrOrInt, List<String> showListSource) {
        this(minValue, maxValue, null);
        this.showStrListData = showListSource;
        this.showStrOrInt = showStrOrInt;
    }

    /**
     * Constructor
     *
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     * @param format   the format string
     */
    public NumericWheelAdapter(int minValue, int maxValue, String format) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
    }

    @Override
    public String getItem(int index) {

		/*// 原来 滚轮中显示的内容是数字的时候，只需要 这些代码
        if (index >= 0 && index < getItemsCount()) {
			//int value = minValue + (getItemsCount()-index-1)*WheelViewConfig.MIN_DELTA_FOR_SCROLLING;
			int tempData=getItemsCount()-((getItemsCount()-index-1))-1;
			int value = minValue +tempData*WheelViewConfig.MIN_DELTA_FOR_SCROLLING;
			return format != null ? String.format(format, value) : Integer.toString(value);
		}*/

        //现在 自己想增加 滚轮中显示的内容是 字符串的时候的情况，将代码修改为如下：
        if (showStrOrInt == true) {//代表显示的 滚轮中显示的内容 是 “字符串”

            return showStrListData.get(index);
        } else {//代表显示的 滚轮中显示的内容 是 “数字形式”，可以自动进行计算
            if (index >= 0 && index < getItemsCount()) {
                //int value = minValue + (getItemsCount()-index-1)*WheelViewConfig.MIN_DELTA_FOR_SCROLLING;
                int tempData = getItemsCount() - ((getItemsCount() - index - 1)) - 1;
                int value = minValue + tempData * WheelViewConfig.MIN_DELTA_FOR_SCROLLING;
                return format != null ? String.format(format, value) : Integer.toString(value);
            }
        }

        return null;
    }

    @Override
    public int getItemsCount() {
        //现在 自己想增加 滚轮中显示的内容是 字符串的时候的情况，将代码修改为如下：
        if (showStrOrInt == true) {//代表显示的 滚轮中显示的内容 是 “字符串”

            return showStrListData.size();
        } else {//代表显示的 滚轮中显示的内容 是 “数字形式”，可以自动进行计算
            int result = 0;
            result = (maxValue - minValue) / WheelViewConfig.MIN_DELTA_FOR_SCROLLING + 1;
            Log.i("ljw", " result :" + result);
            return result;
        }
    }

    @Override
    public int getMaximumLength() {
        Log.i("ljw", " max " + maxValue);
        Log.i("ljw", " min " + minValue);
        int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
        int maxLen = Integer.toString(max).length();
        if (minValue < 0) {
            maxLen++;
        }
        Log.i("ljw", " maxLen " + maxLen);

        //return maxLen;
        return maxLen;
    }
}
