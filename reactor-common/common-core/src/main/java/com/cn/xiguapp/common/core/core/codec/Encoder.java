/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午4:38 >
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

package com.cn.xiguapp.common.core.core.codec;

import com.cn.xiguapp.common.core.core.msg.Payload;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/21
 * @desc 加码器接口
 */
public interface Encoder<T> {
    /**
     * 加码
     * @param content
     * @return
     */
    Payload encode(T content);
}
