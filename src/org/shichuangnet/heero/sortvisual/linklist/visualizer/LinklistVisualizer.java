/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shichuangnet.heero.sortvisual.linklist.visualizer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.shichuangnet.heero.sortvisual.linklist.sorts.BogoSort;
import org.shichuangnet.heero.sortvisual.linklist.sorts.BubbleSort;
import org.shichuangnet.heero.sortvisual.linklist.sorts.ILinkSort;
import org.shichuangnet.heero.sortvisual.linklist.sorts.InsertionSort;
import org.shichuangnet.heero.sortvisual.linklist.sorts.MergeSort;
import org.shichuangnet.heero.sortvisual.linklist.sorts.QuickSort;
import org.shichuangnet.heero.sortvisual.linklist.sorts.RadixLSDSort;
import org.shichuangnet.heero.sortvisual.linklist.sorts.SelectionSort;

/**
 *
 * @author JOJO
 * 链表可视器核心
 * 已知bug:当主窗口最大化或最小化时，设置窗口若处于可视状态，主窗口将覆盖设置窗口
 */
public class LinklistVisualizer extends JFrame {
    
    static final JFrame window = new JFrame();
    static UtilFrame utilFrame;  //  工具窗口
    static SettingView settingView;  //  设置窗口
    
    static LinklistController linklistController = new LinklistController(1000);
    static String heading = "请选择排序算法";  //  算法名
    static Font font = new Font("MONOSPACE", Font.PLAIN, (int)(640/1280.0*25));  //  绘制字符串的字体

    static double SLEEPRATIO = 1.0;  //  用于控制线程sleep的时间（控制绘图的帧数，值越大，帧数越少，动画越快）

    static Thread sortingThread;  //  排序线程
    static boolean SHUFFLEANIM = true;  //  是否展示打乱排序的过程
    public static boolean SHUFFLEMETHOD = true;  //  打乱模式为随机打乱或是调整为逆序
    
    static boolean running = false;
    
    static int COLORMETHOD = 1; //  绘制数组区域的色彩模式 0 = Solid, 1 = Rainbow
    static Color COLORSORT = new Color(0, 204, 0);
    
    static int cx = 0;  //  窗口内的X坐标
    static int cy = 0;  //  窗口内的Y坐标
    
    public static void main (String[] args) {
        
        /* Set the BeautyEye look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        //  BeautyEye是一款Java Swing跨平台外观（look and feel）实现；
        //  得益于Android的GUI基础技术，BeautyEye的实现完全不同于其它外观；
        //  BeautyEye是免费的，您可以研究、学习甚至商业用途。
        //  代码托管： https://github.com/JackJiang2011/beautyeye       
        try {

            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            UIManager.put("RootPane.setupButtonVisible", false);
            Font frameTitleFont = (Font) UIManager.get("InternalFrame.titleFont");
            frameTitleFont = frameTitleFont.deriveFont(Font.PLAIN);
            UIManager.put("InternalFrame.titleFont", frameTitleFont);

            BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //</editor-fold>

        
        //  初始化主窗口界面
        window.setSize(640, 480);
        window.setLocationRelativeTo(null);  //  屏幕中心显示
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setAlwaysOnTop(true);
        window.setTitle("排序算法可视化");
        //  设置窗口最小化时将辅助窗口一并最小化
        window.addWindowListener(new WindowAdapter() {
            public void windowIconified(WindowEvent evt) {
                if (settingView != null && settingView.isVisible()) {
                    settingView.setState(JFrame.ICONIFIED);
                }
                if (utilFrame != null && utilFrame.isVisible()) {
                    utilFrame.setState(JFrame.ICONIFIED);
                }
            }
            public void windowDeiconified(WindowEvent evt) {
                if (settingView != null && settingView.isVisible())
                    settingView.setState(JFrame.NORMAL);
                if (utilFrame != null && utilFrame.isVisible())
                    utilFrame.setState(JFrame.NORMAL);
            }
        });
        utilFrame = new UtilFrame(window);
        utilFrame.setVisible(false);
        shuffle(linklistController);  //  打乱链表
        
        //  绘图线程
        //  实现原理：该线程不断的根据数组中的元素大小和顺序绘制 image，并将该image 写入JFame中
        //  异步的排序线程对数组进行排序，从而实现了排序算法可视化
        new Thread() {
            @Override
            public void run() {
                int cw = 640;  //  默认的 窗口（绘图区域） 大小
                int ch = 480;
                
                //  创建一个可在 Jframe 外绘制的图象
                Image img = window.createVolatileImage(cw, ch);
                Graphics2D g = (Graphics2D)img.getGraphics();  //  获取该 img 的画笔
                
                double xscl, yscl;  //  每一个数组数据柱形的宽和高
                
                while(true) {
                    //  当窗口的长宽、窗口默认的XY坐标发生改变
                    //  根据当前窗口的长宽改变菜单窗口的位置
                    if (window.getWidth()!=cw|| window.getHeight()!=ch || window.getX() != cx || window.getY() != cy){
                        utilFrame.reposition();
                        if(settingView != null && settingView.isVisible())
                            settingView.reposition();
                        cx = window.getX();
                        cy = window.getY();
                    }
                    
                    //  窗口大小发生变化后需要重新根据新窗口大小获取 img
                    if (window.getWidth()!=cw || window.getHeight()!=ch) {
                        cw = window.getWidth();
                        ch = window.getHeight();
                        img = window.createVolatileImage(cw, ch);
                        font = new Font("MONOSPACE", Font.PLAIN, (int)(cw/1280.0*25));
                        g = (Graphics2D)img.getGraphics();
                    }
                    
                    //  绘制背景
                    int bgRgb = 32;
                    g.setColor(new Color(bgRgb, bgRgb, bgRgb));  //  设置画笔颜色
                    g.fillRect(0, 0, img.getWidth(null), img.getHeight(null));  //  填充的长度为 img 画布大小 
                    //  每一个柱形的长度
                    xscl = (double)window.getWidth() / linklistController.length;
                    //  当数据大小为1时，柱形所拥有的高度
                    yscl = (double)(window.getHeight()-30) / linklistController.length;
                    int amt = 0;  //  当前绘制的数组中的单个数据的坐标X （画笔的坐标X）
                    
                    //  设置画笔的粗度
                    float strokew = 3f*(window.getWidth()/1920f);  //  默认画笔粗度为 1
                    g.setStroke(new BasicStroke(strokew));
                    
                    Node node = linklistController.head.getNext();
                    //  绘制数据区域
                    for (int i = 0; i < linklistController.length; i++, node = node.getNext()) {
                        
                        if (node == null) break;
                        g.setColor(getIntColor(node.getValue()));  
                            
                        int y;  //  绘制的柱形的 Y 坐标
                        int width = (int)(xscl*i)-amt;  //  填充的柱形的宽度
                        
                        if (width > 0) {
                            //  该柱形的Y坐标为当前窗口高度 - 柱形最大高度 * 该数据点的大小（数据值越大，Y坐标越小，绘制的柱形越长）
                            y = (int)(window.getHeight()-node.getValue()*yscl);
                            //  填充的长度至少为 1
                            g.fillRect(amt, y, width, Math.max((int)(node.getValue()*yscl), 1));
                        }
                        amt+=width;  //  坐标增加
                    }
                    
                    //  绘制左上角的算法性能分析部分
                    int coltmp = 255;
                    g.setColor(new Color(coltmp, coltmp, coltmp));
                    //  如果不设置字体，则窗口大小改变时字体大小不会
                    Font f = g.getFont();
                    g.setFont(font);
                    g.drawString(heading, 10, (int)(cw/1280.0*20)+50);
                    
                    //  得到画笔，将绘制的整张图片输出到屏幕上
                    Graphics g2 = window.getGraphics();
                    g2.setColor(Color.BLACK);
                    g2.drawImage(img, 0, 0, null);
                }
            }
            
            /**
             * 根据数据值得到特定的颜色，根据 COLORMETHOD 更改色彩模式
             * 0 = Solid, 1 = Rainbow
             */
            public Color getIntColor(int i) {
                if(COLORMETHOD == 1)
                    return Color.getHSBColor(((float)i/linklistController.length), 1.0F, 0.8F);
                return COLORSORT;
            }
        }.start();
        
        //  设置面板可见时锁死线程
        settingView = new SettingView(window);
        while(settingView.isVisible()) try {
            Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(LinklistVisualizer.class.getName()).log(Level.SEVERE, null, ex);
            }
        utilFrame.setVisible(true);
    }
   
    static double addamt = 0.0;
    
    /**
     * sleep线程,不然绘图的速度跟不上排序的速度
     * 同时对不同的数据量做不同的sleep时间长度优化
     * @param milis 毫秒
     */
    public static void mySleep(double milis) {
        if(milis <= 0)
            return;
        //  以1000个数的排序作为1秒的基准，数据量越大优化时间越短，数据量越越小sleep时间越长
        double tmp = (milis*(1000.0/linklistController.length));
        tmp = tmp * (1/SLEEPRATIO);  //  可以由用户控制的变量调节 sleep 的时间
        addamt += tmp;
        if(addamt<1.0)
            return;
        try{
            long actual = System.nanoTime();
            Thread.sleep((long)addamt);
            actual = (System.nanoTime()-actual);
            addamt-=(double)actual/1000000.0;
        }catch(InterruptedException ex){
            Logger.getLogger(LinklistVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 重置链表
     * @throws java.lang.Exception
     */
    public static void refreshLinklist() throws Exception {
        
        if(running){
            running = false;
            Thread.sleep(1000);
        }
        
        Thread.sleep(1000);
        
        initLinklist(linklistController);  //  确保每次排序前链表是有序的
        shuffle(linklistController);  //  打乱链表
        heading = "";
        
        Thread.sleep(500);
        running = true;
    }
    
    /**
     * 初始化链表中的数据（有序）
     * @param lc LinklistController 对象
     */
    public static void initLinklist(LinklistController lc) {
        Node tmp = lc.head.getNext();
            int i = 0;
            while (tmp != null) {
                tmp.setValue(i);
                tmp = tmp.getNext();
                i++;
            }
    }
    
    /**
     * 打乱数组或是逆置数组
     * @param array
     */
    public static void shuffle(LinklistController lc) {
        String string = heading;
        heading = "数据重置中...";
        
        if ( SHUFFLEMETHOD == true) {  //  打乱数组
            //  随机获得一个不超过数组长度的下标，将其与 i 指向的元素交换位置
            for(int i = 0; i < lc.length; i++) {
                // 0 ~ array.length-1 的数
                int randIndex = (int)(Math.random()*lc.length);
//                System.out.println(randIndex + " " + i);
                Node p = linklistController.getNodeByIndex(i);
                Node q = linklistController.getNodeByIndex(randIndex);
                int temp = p.getValue();
                p.setValue(q.getValue());
                q.setValue(temp);
                if(SHUFFLEANIM)  //  是否展示打乱数组的过程
                    mySleep(1);
            }
        } else {
            //  将数组逆置
            Node tmp = lc.head.getNext();
            int i = lc.length;
            while (tmp != null) {
                tmp.setValue(i);
                tmp = tmp.getNext();
                i--;
                if(SHUFFLEANIM)
                    mySleep(1);
            } 
        }
        
        heading = string;
    }
    
    /**
     * 开始运行所有排序算法
     */
    public synchronized static void runAllSorts() {
        if(sortingThread != null)
            while(sortingThread.isAlive()) try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(LinklistVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }

        sortingThread = new Thread(){
            @Override
            public void run(){
                try{
                    for (ILinkSort sort : new ILinkSort[] {
                            new SelectionSort(),
                            new BubbleSort(),
                            new InsertionSort(),
                            new QuickSort(),
                            new MergeSort(),
                            new RadixLSDSort()
                        })
                    {
                        refreshLinklist();
                        heading = sort.name();
                        sort.startSort(linklistController);
                    }
                }catch (Exception ex){
                    Logger.getLogger(LinklistVisualizer.class.getName()).log(Level.SEVERE, null, ex);
                }
                running = false;
            }
        };
        sortingThread.start();
    }
    
    /**
     * 演示单个比较排序算法
     * @param index 下标
     */
    public static void comparativeSort(int index) {
        //  如果线程排序线程正在执行，不接受响应
        if(sortingThread != null && sortingThread.isAlive())
            return;
        
        sortingThread = new Thread(){
            @Override
            public void run() {
                try{
                    refreshLinklist();  //  重置数组
                    ILinkSort sort;
                    switch (index) {
                        case 0:
                            sort = new SelectionSort(); break;
                        case 1:
                            sort = new BubbleSort(); break;
                        case 2:
                            sort = new InsertionSort(); break;
                        case 3:
                            sort = new QuickSort(); break;
                        case 4:
                            sort = new MergeSort(); break;
                        default:
                            sort = null; break;
                    }
                    if (sort != null) {
                        heading = sort.name();
                        sort.startSort(linklistController);
                    }
                } catch(Exception ex) {
                    Logger.getLogger(LinklistVisualizer.class.getName()).log(Level.SEVERE, null, ex);
                }
                running = false;
            }
        };
        sortingThread.start();
    }
    
    /**
     * 演示单个非比较排序算法
     */
    public static void distributiveSort(int index) {
        //  如果线程排序线程正在执行，不接受响应
        if(sortingThread != null && sortingThread.isAlive())
            return;
        
        sortingThread = new Thread(){
            @Override
            public void run() {
                try{
                    refreshLinklist();  //  重置数组
                    ILinkSort sort;
                    switch (index) {
                        case 0:
                            sort = new RadixLSDSort(); break;
                        case 1:
                            sort = new BogoSort(); break;
                        default:
                            sort = null; break;
                    }
                    if (sort != null) {
                        heading = sort.name();
                        sort.startSort(linklistController);
                    }
                } catch(Exception ex) {
                    Logger.getLogger(LinklistVisualizer.class.getName()).log(Level.SEVERE, null, ex);
                }
                running = false;
            }
        };
        sortingThread.start();
    }
}
