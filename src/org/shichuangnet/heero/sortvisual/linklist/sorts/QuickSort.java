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
public class QuickSort implements ILinkSort {

    @Override
    public String name() {
        return "快速排序";
    }

    @Override
    public void startSort(LinklistController lc) {
        quickSort(lc.head, null);
    }
    
    void quickSort (Node head, Node end) {
        if(head == null || head == end)             //如果头指针为空或者链表为空，直接返回  
            return ;  
        int t;  
        Node p = head.getNext();                  //用来遍历的指针  
        Node small = head;  
        while (p != end) {  
            LinklistVisualizer.mySleep(0.45);
            if (p.getValue() < head.getValue()) {      //对于小于轴的元素放在左边  
                small = small.getNext();  
                t = small.getValue();  
                small.setValue(p.getValue());  
                p.setValue(t);  
            }  
            p = p.getNext();  
        }  
        t = head.getValue();     //遍历完后，对左轴元素与small指向的元素交换  
        head.setValue(small.getValue());  
        small.setValue(t);  
        quickSort(head, small);  //对左右进行递归  
        quickSort(small.getNext(), end);  
    }
    
}
