/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shichuangnet.heero.sortvisual.linklist.visualizer;

/**
 *
 * @author JOJO
 * 用于接口回调，操控视图
 * 有些方法和swing的方法的原型是一样的，目的是方便对接口进行swing的操作
 */
interface IView {
    abstract void reposition();  //  需要手动实现
    abstract boolean isVisible();
    abstract void dispose();
    abstract void setState(int state);
}
