/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shichuangnet.heero.sortvisual.linklist.visualizer;

/**
 *
 * @author JOJO
 * 用于实现链表节点
 */
public class Node {
    
    private int value = -1;
    private Node next = null;
    
    public Node() {}
    
    public Node(int value) {
        this.value = value;
    }
    
    public Node(int value, Node next) {
        this.value = value;
        this.next = next;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    public Node getNext() {
        return next;
    }
    
    public void setNext (Node next) {
        this.next = next;
    }
}
