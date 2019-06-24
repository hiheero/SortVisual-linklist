/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shichuangnet.heero.sortvisual.linklist.sorts;

import org.shichuangnet.heero.sortvisual.linklist.visualizer.LinklistController;
import static org.shichuangnet.heero.sortvisual.linklist.visualizer.LinklistVisualizer.mySleep;
import org.shichuangnet.heero.sortvisual.linklist.visualizer.Node;


/**
 *
 * @author JOJO
 * 链表的冒泡排序实现，有数值交换法和节点交换法
 */
public class BubbleSort implements ILinkSort {

    @Override
    public String name() {
        return "冒泡排序";
    }

    @Override
    public void startSort(LinklistController lc) {
       
        Node p, q, tail = null;
        
        //  数值交换法，更耗时
//        boolean hasChange = true;
//        for (p = lc.head.getNext(); p != null; p = lc.head.getNext()) {
//            if (!hasChange) break;
//            hasChange = false;
//            for ( q = p.getNext(); q != null; q = p.getNext()) {
//                mySleep(0.01);
//                if (p.getValue() > q.getValue()) {
//                    int tmp = p.getValue();
//                    p.setValue(q.getValue());
//                    q.setValue(tmp);
//                    hasChange = true;
//                }
//                if (q.getNext() == null)
//                    break;
//                else
//                    p = q;
//            }
//        }
        
//        LinklistVisualizer.shuffle(lc);
        //  交换节点法
        while((lc.head.getNext().getNext()) != tail) {
            p = lc.head;
            q = lc.head.getNext();
            while(q.getNext() != tail) {
                mySleep(0.01);
                if( q.getValue() > q.getNext().getValue()) {
                    p.setNext(q.getNext());
                    q.setNext(q.getNext().getNext());
                    p.getNext().setNext(q);
                    q = p.getNext();
                }
                q = q.getNext();
                p = p.getNext();
            }
            tail = q;
        }
    
    }
}
