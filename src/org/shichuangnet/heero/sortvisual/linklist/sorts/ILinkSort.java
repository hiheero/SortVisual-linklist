/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shichuangnet.heero.sortvisual.linklist.sorts;

import org.shichuangnet.heero.sortvisual.linklist.visualizer.LinklistController;

/**
 *
 * @author JOJO
 * 所有排序方法应该实现的接口
 */
public interface ILinkSort {
    String name();  //  返回排序名称
    public void startSort(final LinklistController lc);  //  排序的实现
}
