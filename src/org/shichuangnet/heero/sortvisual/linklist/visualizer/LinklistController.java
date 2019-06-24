/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shichuangnet.heero.sortvisual.linklist.visualizer;

/**
 *
 * @author JOJO
 * Java 链表结构
 */
public class LinklistController {
    
    public Node head = new Node();  //  头节点
    
    public final int length;
    public long arrayAccess;     //  记录排序提取数据的次数
    public long comps;  //  记录排序的比较次数
    
    
    /**
     * 传入链表长度，通过尾插法创建链表
     */
    public LinklistController(int length) {
        this.length = length;
        head.setValue(length);
        length--;
        while (length >= 0) {
            Node tmp = new Node(length, head);
            head = tmp;
            length--;
        }
        head.setValue(-1);
    }
    
    /**
     * 遍历链表
     */
    public void showLinklist() {
        Node tmp = head.getNext();
        while (tmp != null) {
            System.out.print(tmp.getValue() + " ");
            tmp = tmp.getNext();
        }
        System.out.println();
    }
    
    /**
     * 通过元素下标得到节点
     */
    public Node getNodeByIndex(int index) {
        
        if (index > length-1)
            return null;
        Node tmp = head.getNext();
        for (int i = 0; i < index; i++) {
            if (tmp == null)
                return null;
            tmp = tmp.getNext();
        }
        
        return tmp;
    }
    
    /**
     * 通过 value 得到节点，只返回第一个拥有value值得节点
     */ 
    public Node getNodeByValue(int value) {
        Node tmp = head.getNext();
        
        while (tmp != null) {
            if (tmp.getValue() == value) {
                return tmp;
            }
            tmp = tmp.getNext();
        }
        return null;
    }

}
