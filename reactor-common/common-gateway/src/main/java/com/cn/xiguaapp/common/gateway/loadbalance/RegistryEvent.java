/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:46 >
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

package com.cn.xiguaapp.common.gateway.loadbalance;


import com.cn.xiguaapp.common.gateway.bean.InstanceDefinition;

import java.util.function.Consumer;

/**
 * 新的实例注册事件
 *
 * @author xiguaapp
 */
public interface RegistryEvent {

    /**
     * 新实例注册进来时触发
     * @param instanceDefinition 实例信息
     */
    void onRegistry(InstanceDefinition instanceDefinition);

    /**
     * 服务下线时触发
     *  <p>serviceId</p> 服务id
     */
//    void onRemove(String serviceId);
    Consumer<String>onRemove();
}
