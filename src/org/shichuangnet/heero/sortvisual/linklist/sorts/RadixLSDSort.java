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
 * 基数排序 LSD
 */
public class RadixLSDSort implements ILinkSort {

    @Override
    public String name() {
        return "基数排序LSD";
    }

    @Override
    public void startSort(LinklistController lc) {
        int count = Integer.toString(lc.length).length();  //  得到数据最大值的位数
        radixLSDSort(lc.head, count);
    }

    /**
     * @param count 参加排序的数值的最大位数
     */
    void radixLSDSort(Node head, int count) {

        NodeArray p = new NodeArray();
        
        for (int i = 0; i < count; i++) {
            Node l = head.getNext();
            //  将节点不断从头节点脱离，并加入至对应位数的节点数组中
            while (l != null) {
                //  得到对应位数的基数：将数字转为 string，利用charAt()得到对应位数的基数，再将基数转为int型
                String num = Integer.toString(l.getValue());
                char radixNum;
                if (i > num.length() - 1) {  //  超出数字位数时返回 0
                    radixNum = '0';
                } else {
                    radixNum = num.charAt(num.length() - 1 - i);
                }
                int index = Integer.valueOf("" + radixNum);
                
                //  将节点按基数值分配给对应的节点数组
                head.setNext(l.getNext());
                l.setNext(null);  //  断开节点
                Node t = p.getNode(index);  //  找到数组的末尾
                while (t.getNext() != null) t = t.getNext();
                t.setNext(l);
                l = head.getNext();
            }

            //  将排序后的节点重新插入表头节点中
            for (int j = 0; j < 10; j++) {
                l = head;
                while (l.getNext() != null) {
                    l = l.getNext();
                }
                l.setNext(p.getNode(j).getNext());
                p.getNode(j).setNext(null);
                LinklistVisualizer.mySleep(100);
                //  从数组中一个一个节点插入头节点中
                //  采用这种方式可以使得绘图更好看
//                Node t = p.getNode(j).getNext();
//                while (t!=null) {
//                    Node s = new Node(t.getValue());
//                    l.setNext(s);
//                    l = l.getNext();
//                    t = t.getNext();
//                    LinklistVisualizer.mySleep(1);
//                }
                p.getNode(j).setNext(null);
            }
//            LinklistVisualizer.mySleep(1500);
        }
    }
    
    private class NodeArray {
        private Node n0, n1, n2, n3, n4, n5, n6, n7, n8, n9;

        public NodeArray() {
            n0 = new Node();
            n1 = new Node();
            n2 = new Node();
            n3 = new Node();
            n4 = new Node();
            n5 = new Node();
            n6 = new Node();
            n7 = new Node();
            n8 = new Node();
            n9 = new Node();
        }
        
        public Node getNode(int index) {
            if (index < 0 || index > 9) return null;
            switch(index) {
                case 0:
                    return n0;
                case 1:
                    return n1;
                case 2:
                    return n2;
                case 3:
                    return n3;
                case 4:
                    return n4;
                case 5:
                    return n5;
                case 6:
                    return n6;
                case 7:
                    return n7;
                case 8:
                    return n8;
                case 9:
                    return n9;
                default:
                    return null;
            }
        }
        
        public void clearArray() {
            n0 = new Node();
            n1 = new Node();
            n2 = new Node();
            n3 = new Node();
            n4 = new Node();
            n5 = new Node();
            n6 = new Node();
            n7 = new Node();
            n8 = new Node();
            n9 = new Node();
        }
    }
    
}
