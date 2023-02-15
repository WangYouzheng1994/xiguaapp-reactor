/*
 *        Copyright (C) <2018-2028>  <@author: xiguaapp @date: @today>
 *        Send: 1125698980@qq.com
 *        This program is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *        This program is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU General Public License for more details.
 *        You should have received a copy of the GNU General Public License
 *        along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguaapp.r2dbc.orm.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author xiguaapp
 * @desc ROBO数据库描述字段
 */

@Getter
@AllArgsConstructor
public enum RDBObjectType implements ObjectType {
    table("表"),
    column("列"),
    foreignKey("外键"),
    constraint("约束"),
    key("键"),
    dataType("数据类型"),
    index("索引"),
    view("视图"),
    function("函数");

    private String name;

    @Override
    public String getId() {
        return name();
    }

}
