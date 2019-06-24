/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shichuangnet.heero.sortvisual.linklist.sorts;

import org.shichuangnet.heero.sortvisual.linklist.visualizer.LinklistController;
import org.shichuangnet.heero.sortvisual.linklist.visualizer.LinklistVisualizer;
import org.shichuangnet.heero.sortvisual.linklist.visualizer.Node;

/**
 *
 * @author JOJO
 * 猴子排序
 */
public class BogoSort implements ILinkSort {

    @Override
    public String name() {
        return "猴子排序";
    }

    @Override
    public void startSort(LinklistController lc) {
        while (!isSorted(lc)) {
            shuffle(lc);
        }
    }
    
    private boolean isSorted(LinklistController lc) {
        Node p = lc.head.getNext();
        Node q = p.getNext();
        while ( p != null && q != null) {
            if (p.getValue() > q.getValue())
                return false;
            p = p.getNext();
            q = p.getNext();
        }
        return true;
    }
    
    private Node shuffle(LinklistController lc) {
        for(int i = 0; i < lc.length; i++) {
            // 0 ~ array.length-1 的数
            int randIndex = (int)(Math.random()*lc.length);
            Node p = lc.getNodeByIndex(i);
            Node q = lc.getNodeByIndex(randIndex);
            int temp = p.getValue();
            p.setValue(q.getValue());
            q.setValue(temp);
            LinklistVisualizer.mySleep(1);
        }
        return lc.head;
    }
}
