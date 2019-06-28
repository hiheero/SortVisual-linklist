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
 */
public class InsertionSort implements ILinkSort {

    @Override
    public String name() {
        return "直接插入排序";
    }

    @Override
    public void startSort(LinklistController lc) {
    
        //  交换节点法
//         Node p, q, r;
//         p = lc.head.getNext();
//         q = p.getNext();
//         p.setNext(null);  //  断开链表头
//         p = q;
//         while (p != null) {
//             q = p.getNext();
//             r = lc.head;
//             while (r.getNext() != null && r.getNext().getValue() <= p.getValue()) {
//                 LinklistVisualizer.mySleep(0.02);
//                 r = r.getNext();
//             }
//             p.setNext(r.getNext());
//             r.setNext(p);
//             p = q;
//             //  数组逆置时增长sleep时间（排的太快了）
//             if (!LinklistVisualizer.SHUFFLEMETHOD) {
//                 LinklistVisualizer.mySleep(1);
//             }
//         }
        //  交换节点法，不断开头结点
        Node p, q, r;
        p = lc.head.getNext();  //  p始终指向有序区域末尾
        q = p.getNext();  //  q始终指向无序空间起始
        while (q != null) {
            r = lc.head;
            while (r != p) {  //  寻找 p 应该插入的位置 
                if (r.getNext().getValue() > q.getValue())
                    break;
                r = r.getNext();
            }
            if (r == p) {  //  插入的位置是有序空间的末尾
                p = q;
                q = q.getNext();
            } else {
                p.setNext(q.getNext());
                q.setNext(r.getNext());
                r.setNext(q);
                q = p.getNext();
            }
            LinklistVisualizer.mySleep(3);
        }
    }
    
            //  交换数值法，但是非常耗时
//        for (int i = 1; i < lc.length; i++) {
//            int pos = i;
//            while (pos > 0 && 
//                    lc.getNodeByIndex(pos).getValue() <= lc.getNodeByIndex(pos-1).getValue())
//            {
//                
//                LinklistVisualizer.mySleep(0.01);
//                int temp = lc.getNodeByIndex(pos).getValue();
//                lc.getNodeByIndex(pos).setValue(lc.getNodeByIndex(pos-1).getValue());
//                lc.getNodeByIndex(pos-1).setValue(temp);
//                pos--;
//            }
//        }
    
}
