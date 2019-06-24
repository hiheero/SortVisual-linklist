/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shichuangnet.heero.sortvisual.linklist.sorts;

import org.shichuangnet.heero.sortvisual.linklist.visualizer.LinklistController;
import org.shichuangnet.heero.sortvisual.linklist.visualizer.LinklistVisualizer;
import static org.shichuangnet.heero.sortvisual.linklist.visualizer.LinklistVisualizer.mySleep;
import org.shichuangnet.heero.sortvisual.linklist.visualizer.Node;

/**
 *
 * @author JOJO
 */
public class SelectionSort implements ILinkSort {

    @Override
    public String name() {
        return "选择排序";
    }

    @Override
    public void startSort(LinklistController lc) {
        Node p, q;
        //  交换数值法
        for (p = lc.head.getNext(); p != null; p = p.getNext()) {
            for (q = p.getNext(); q != null; q = q.getNext()) {
                mySleep(0.01);  //  sleep线程，不然排序速度太快绘图会跟不上
                if (p.getValue() > q.getValue()) {
                    int tmp = p.getValue();
                    p.setValue(q.getValue());
                    q.setValue(tmp);
                }
            }
        }
    }
    
}
