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
 * 归并排序的链表实现
 * 不完美的地方：归并排序将链表拆解为一个一个的子链，在绘制图像时，只能从表头依次向下绘制
 * 因此断链会导致一部分递归排序好的子链无法显示在屏幕图像中
 */
public class MergeSort implements ILinkSort {

    @Override
    public String name() {
        return "归并排序";
    }

    @Override
    public void startSort(LinklistController lc) {
        mergeSort(lc.head);
    }
    
    Node mergeSort(Node node) {
        //  先判断链表长度是否大于1，小于1时无须排序
        if(node != null && node.getNext() != null){
            //  运用快慢指针，找到链表的中间节点
            Node fast = node.getNext();
            Node slow = node;
            while (fast != null && fast.getNext() != null) {
                fast = fast.getNext().getNext();  //  快指针每次走两格
                slow = slow.getNext();              //  慢指针每次走一格
            }

            //  将链表分成两部分进行分割
            Node tmp = slow.getNext();
            Node p1 = mergeSort(slow.getNext());
            slow.setNext(null);
            Node p2 = mergeSort(node);
            
            //  对两条子链进行归并
            Node p0 = new Node();
            Node p = p0;
            while (p1 != null && p2 != null) {
                LinklistVisualizer.mySleep(1);
                if (p1.getValue() < p2.getValue()) {
                    p.setNext(p1);
                    p1 = p1.getNext();
                } else {
                    p.setNext(p2);
                    p2 = p2.getNext();
                }
                p = p.getNext();
            }

            if (p1 != null) {
                p.setNext(p1);
            }

            if (p2 != null) {
                p.setNext(p2);
            }

            p = p0.getNext();
            
            return p;
        }

        return node;
    }

}
