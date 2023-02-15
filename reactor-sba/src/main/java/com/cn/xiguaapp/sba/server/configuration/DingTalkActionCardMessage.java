/*
 *     Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/19 下午4:40 >
 *
 *     Send: 1125698980@qq.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguaapp.sba.server.configuration;

import lombok.Builder;
import lombok.Data;


/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/19
 * @desc
 */
@Data
@Builder
public class DingTalkActionCardMessage implements DefaultDingTalkMessage {
    /**
     * markdown格式的消息
     * 数据格式：
     * 标题
     * # 一级标题
     * ## 二级标题
     * ### 三级标题
     * #### 四级标题
     * ##### 五级标题
     * ###### 六级标题
     *
     * 引用
     * > A man who stands for nothing will fall for anything.
     *
     * 文字加粗、斜体
     * **bold**
     * *italic*
     *
     * 链接
     * [this is a link](http://name.com)
     *
     * 图片
     * ![](http://name.com/pic.jpg)
     *
     * 无序列表
     * - item1
     * - item2
     *
     * 有序列表
     * 1. item1
     * 2. item2
     *
     */
    private String text;
    /**
     * 首屏会话透出的展示内容
     */
    private String title;
    /**
     * 单个按钮标题
     * (设置此项和singleURL后btns无效)
     */
    private String singleTitle;
    /**
     * 点击singleTitle按钮触发的URL
     */
    private String singleURL;
    /**
     * 0-按钮竖直排列，1-按钮横向排列
     */
    private String btnOrientation;
}
