package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame implements ActionListener {
    private final String[] KEYS = {"7", "8", "9", "/", "sqrt", "4", "5", "6", "*", "%", "1", "2", "3", "-", "1/x", "0", "+/-", ".", "+", "="};
    //计算器上按键的显示名字
    private final String[] COMMAND = {"Backspace", "CE", "C"};
    //计算器上控制键的显示名字
    private final String[] M = {" ", "MC", "MR", "MS", "M+"};
    //计算器左边M系列按键的显示名字
    private JButton keys[] = new JButton[KEYS.length];//计算器上的按键按钮
    private JButton commands[] = new JButton[COMMAND.length];//计算器上控制键按钮
    private JButton m[] = new JButton[M.length];//计算器左边的M系列按键按钮
    private JTextField resultText = new JTextField("0");//计算结果文本框
    //标识用户按的是否是整个表达式的第一个数字，或者是运算符后的第一个数字
    private boolean firstDigit = true;
    private double resultNum = 0.0;//计算的中间结果
    private String operator = "=";//当前运算的运算符
    private boolean operateValidFlag = true;//操作是否合法

    /** 构造函数 */
    public Calculator(){
        super();
        init();//初始化计算器
        this.setBackground(Color.lightGray);//设置计算器的背景颜色
        this.setTitle("计算器");
        this.setLocation(500, 300);//在屏幕（500，300）处显示计算器
        this.setResizable(false); //不许修改计算器的大小
        this.pack();//使计算器中各组件的大小合适
    }

    /** 初始化计算器 */
    private void init(){
        resultText.setHorizontalAlignment(JTextField.RIGHT);
        //文本框中的内容采用右对齐方式
        resultText.setEditable(false);//不允许修改结果文本框
        resultText.setBackground(Color.white);//设置文本框背景颜色为白色

        JPanel calckeysPanel = new JPanel();
        //初始化计算器上的按键按钮，将案件放在一个面板内
        //使用网格布局管理器，4行5列，网格之间水平方向和垂直方向间隔为3像素
        calckeysPanel.setLayout(new GridLayout(4, 5, 3, 3));
        for (int i = 0; i < KEYS.length; ++i){
            keys[i] = new JButton(KEYS[i]);
            calckeysPanel.add(keys[i]);
            keys[i].setForeground(Color.blue);
        }
        //运算符键用红色显示，其他按键用蓝色显示
        keys[3].setForeground(Color.red);
        keys[8].setForeground(Color.red);
        keys[13].setForeground(Color.RED);
        keys[18].setForeground(Color.RED);
        keys[19].setForeground(Color.RED);

        //初始化控制键，动用红色显示，将控制键放在一个面板内
        JPanel commandsPanel = new JPanel();
        //使用网格布局管理器，1行3列，网格之间水平方向和垂直方向间隔为3像素
        commandsPanel.setLayout(new GridLayout(1, 3, 3, 3));
        for(int i = 0; i < COMMAND.length; ++i){
            commands[i] = new JButton(COMMAND[i]);
            commandsPanel.add(commands[i]);
            commands[i].setForeground(Color.RED);
        }

        //初始化M系列按键，用红色显示，将M系列按键放在一个面板内
        JPanel calmsPanel = new JPanel();
        //使用网格布局管理器，5行1列，网格之间水平方向和垂直方向为3像素
        calmsPanel.setLayout(new GridLayout(5, 1, 3, 3));
        for(int i = 0; i < M.length; ++i){
            m[i] = new JButton(M[i]);
            calmsPanel.add(m[i]);
            m[i].setForeground(Color.RED);
        }

        //将calckeysPanel和commandsPanel面板放在中部
        //将文本框放在上部,将calmsPanel面板放在计算器的左部

        //新建一个面板，将commandsPanel和calmsPanel面板放在该面板内
        JPanel panel1 = new JPanel();
        //面板采用边界布局管理器，面板中组件制键水平和垂直方向间隔都为3像素
        panel1.setLayout(new GridLayout(3, 3));
        panel1.add("North", commandsPanel);
        panel1.add("Center", calckeysPanel);

        //建立一个面板放文本框
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.add("Center", resultText);

        //整体布局
        getContentPane().setLayout(new BorderLayout(3, 5));
        getContentPane().add("North", top);
        getContentPane().add("Center", panel1);
        getContentPane().add("West",calmsPanel);
        //为各按钮添加事件侦听器
        //都使用同一个事件侦听器，即本对象
        for(int i = 0; i < KEYS.length; ++i){
            keys[i].addActionListener(this);
        }
        for(int i = 0; i < COMMAND.length; ++i){
            commands[i].addActionListener(this);
        }
        for(int i = 0; i < M.length; ++i){
            m[i].addActionListener(this);
        }
    }

    /** 处理事件 */
    public void actionPerformed(ActionEvent e){
        String label = e.getActionCommand();//获取事件源的标签
        if(label.equals(COMMAND[0])){
            handleBackspace();//用户按了“Banckspace”键
        }
        else if(label.equals(COMMAND[1])){
            resultText.setText("0");//用户按了“CE”键
        }
        else if (label.equals(COMMAND[2])){
             handleC();//用户按了“C”键
        }else if ("0123456789.".indexOf(label) >= 0){
            handleNumber(label);//用户按了运算符键
        }
    }

    /** 处理Backspace键被按下的事件 */
    private void handleBackspace() {
        String text = resultText.getText();
        int i = text.length();
        if(i > 0){
            //退格，将文本的最后一个字符去掉
            text = text.substring(0, i - 1);
            if(text.length() == 0){
                //如果文本没有了内容，则初始化计算器的各种值
                resultText.setText("0");
                firstDigit = true;
                operator = "=";
            }else {
                resultText.setText(text);
            }
        }
    }

    /** 处理数字键被按下的事件 */
    private void handleNumber(String key){
        if(firstDigit){
            //输入的死一个数字
            resultText.setText(key);
        }else if ((key.equals(".")) && (resultText.getText().indexOf(".") < 0)){
            //输入的是小数点，并且之前没有小数点，则将小数点附在结果文本框的后面
            resultText.setText(resultText.getText() + ".");
        }else if(!key.equals(".")){
            //如果输入的不是小数点，则将数字附在结果文本框的后面
            resultText.setText(resultText.getText() + key);
        }
        //以后输入的坑定不是第一个数字了
        firstDigit = false;
    }

    /** 处理C键被按下的事件 */
    private void  handleC(){
        //初始化计算器的各种值
        resultText.setText("0");
        firstDigit = true;
        operator = "=";
    }

    /** 处理运算符键被按下的事件 */
    private void handleOprator(String key){
        if(operator.equals("/")) {
            //除法运算
            //如果当前文本框中的值为0
            if(getNumberFromText() == 0.0){
                //操作不合法
                operateValidFlag = false;
                resultText.setText("除数不能为0");
            }else{
                resultNum /= getNumberFromText();
            }
        }else if (operator.equals("1/x")){
            //倒数运算
            if(resultNum == 0.0){
                //操作不合法
                operateValidFlag = false;
                resultText.setText("零没有倒数");
            }else {
                resultNum = 1 / resultNum;
            }
        }else if (operator.equals("+")){
            resultNum += getNumberFromText();//加法运算
        }else if (operator.equals("-")){
            resultNum -= getNumberFromText();//减法运算
        }else if (operator.equals("*")){
            resultNum *= getNumberFromText();//乘法运算
        }else if (operator.equals("sqrt")){
            resultNum = Math.sqrt(resultNum);//平方根运算
        }else if (operator.equals("%")){
            resultNum = resultNum / 100; //百分号运算
        }else if (operator.equals("+/-")){
            resultNum = resultNum * (-1);//正负数运算
        }else if (operator.equals("=")){
            resultNum = getNumberFromText();//赋值运算
        }
        if(operateValidFlag){
            //双精度浮点数运算
            long t1;
            double t2;
            t1 = (long)resultNum;
            t2 = resultNum - t1;
            if(t2 == 0){
                resultText.setText(String.valueOf(t1));
            }else {
                resultText.setText(String.valueOf(resultNum));
            }
        }
        //将用户按下的按钮赋值给变量
        operator = key;
        firstDigit = true;
        operateValidFlag = true;
    }

    /** 从结果文本框中获取数值 */
    private double getNumberFromText(){
        double result = 0;
        try {
            result = Double.valueOf(resultText.getText()).doubleValue();
        }catch (NumberFormatException e){

        }
        return result;
    }

    public static void main(String[] args) {
        Calculator calculator1 = new Calculator();
        calculator1.setVisible(true);
        calculator1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
