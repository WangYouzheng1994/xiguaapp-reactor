/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:04 >
 *
 *       Send: 1125698980@qq.com
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.cn.xiguaapp.common.gateway.manage;

import java.util.Map;

/**
 * 参数格式化
 *
 * @author xiguaapp
 */
public interface Formatter<T extends Map<String, Object>> {

    /**
     * 参数格式化，即动态修改请求参数
     *
     * @param requestParams 原始请求参数，在此基础上追加或修改参数
     */
    void format(T requestParams);
}
