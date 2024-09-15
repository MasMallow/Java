package BasicCalculator;

import javax.swing.*;  // นำเข้าไลบรารีที่เกี่ยวกับส่วนติดต่อผู้ใช้ (GUI)
import java.awt.*;     // นำเข้าไลบรารีที่ใช้ในการจัดการเลย์เอาต์และองค์ประกอบต่างๆ
import java.awt.event.ActionEvent;   // นำเข้าเหตุการณ์ที่เกิดจากการกดปุ่ม
import java.awt.event.ActionListener; // นำเข้าอินเทอร์เฟซที่ใช้ฟังเหตุการณ์

// คลาสหลักของโปรแกรมที่สืบทอดมาจาก JFrame และ implements ActionListener เพื่อฟังเหตุการณ์จากปุ่ม
public class calculatorGUI extends JFrame implements ActionListener {

    private JTextField display;  // ช่องแสดงผลสำหรับเครื่องคิดเลข
    private double num1, num2, result;  // ตัวแปรเก็บตัวเลขแรก ตัวเลขที่สอง และผลลัพธ์
    private char operator;  // ตัวแปรเก็บเครื่องหมายการคำนวณ (+, -, *, /)
    private boolean operatorSet = false;  // ใช้เพื่อตรวจสอบว่าได้ตั้งค่าตัวดำเนินการแล้วหรือยัง
    private boolean resultDisplayed = false;  // ตรวจสอบว่ามีการแสดงผลลัพธ์แล้วหรือไม่

    // คอนสตรัคเตอร์ของคลาส calculatorGUI
    public calculatorGUI() {
        setTitle("เครื่องคิดเลข");  // ตั้งชื่อหน้าต่าง
        setSize(400, 520);  // กำหนดขนาดหน้าต่าง (กว้าง x สูง)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // ปิดโปรแกรมเมื่อปิดหน้าต่าง

        // สร้างช่องแสดงผลและกำหนดขนาดและตำแหน่ง
        display = new JTextField();
        display.setBounds(26, 40, 340, 50);  // ตำแหน่ง (x, y, กว้าง, สูง)
        add(display);  // เพิ่มช่องแสดงผลลงในหน้าต่าง

        // สร้าง JPanel สำหรับปุ่ม Cancel
        JPanel cancelPanel = new JPanel();
        cancelPanel.setBounds(26, 100, 340, 50);  // กำหนดขนาดและตำแหน่งของ Panel
        cancelPanel.setLayout(new GridLayout(1,1,10,10));  // ใช้เลย์เอาต์ GridLayout แถวเดียว
        JButton cancelButton = new JButton("Cancel");  // สร้างปุ่ม Cancel
        cancelButton.addActionListener(this);  // ผูกการทำงานของปุ่มกับ ActionListener
        cancelPanel.add(cancelButton);  // เพิ่มปุ่ม Cancel ลงใน Panel
        add(cancelPanel);  // เพิ่ม Panel ของ Cancel ลงในหน้าต่าง

        // สร้างปุ่มตัวเลขและเครื่องหมายคำนวณ
        String[] buttons = { "7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "0", ".", "=", "+" };
        JPanel panel = new JPanel();
        panel.setBounds(26, 160, 340, 300);  // กำหนดขนาดและตำแหน่งของ Panel สำหรับปุ่มตัวเลขและเครื่องหมาย
        panel.setLayout(new GridLayout(4, 4, 10, 10));  // ใช้ GridLayout 4x4

        // วนลูปเพิ่มปุ่มตัวเลขและเครื่องหมายคำนวณลงใน Panel
        for (String text : buttons) {
            JButton button = new JButton(text);  // สร้างปุ่มใหม่ด้วยข้อความจาก array
            button.addActionListener(this);  // ผูกการทำงานของปุ่มกับ ActionListener
            panel.add(button);  // เพิ่มปุ่มลงใน Panel
        }
        add(panel);  // เพิ่ม Panel ของปุ่มตัวเลขลงในหน้าต่าง
        setLayout(null);  // ใช้เลย์เอาต์แบบ null เพื่อควบคุมการวางตำแหน่งเอง
        setVisible(true);  // แสดงหน้าต่าง
    }

    // เมธอดที่ทำงานเมื่อมีการกดปุ่ม (implement จาก ActionListener)
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();  // เก็บข้อความที่ปรากฏบนปุ่มที่ถูกกด

        if (command.equals("Cancel")) {  // ถ้ากดปุ่ม Cancel
            display.setText("");  // ล้างช่องแสดงผล
            num1 = num2 = result = 0;  // รีเซ็ตตัวเลขและผลลัพธ์ทั้งหมดเป็น 0
            operatorSet = false;  // ตั้งค่าให้ยังไม่ได้เลือกตัวดำเนินการ
            resultDisplayed = false;  // ตั้งค่าว่าไม่มีผลลัพธ์ที่แสดงอยู่
        }
        else if ((command.charAt(0) >= '0' && command.charAt(0) <= '9') || command.equals(".")) {  // กดปุ่มตัวเลขหรือจุดทศนิยม
            if (resultDisplayed) {  // ถ้ามีการแสดงผลลัพธ์อยู่
                display.setText(command);  // ล้างช่องแสดงผลแล้วแสดงตัวเลขใหม่
                resultDisplayed = false;  // ตั้งค่าว่าไม่ได้แสดงผลลัพธ์แล้ว
            } else {
                display.setText(display.getText() + command);  // เพิ่มตัวเลขที่กดเข้ากับตัวเลขที่แสดงอยู่
            }
        } else if (command.equals("=")) {  // ถ้ากดปุ่ม "=" เพื่อคำนวณผลลัพธ์
            String[] parts = display.getText().split(" ");  // แยกข้อความในช่องแสดงผลเพื่อดูตัวเลขและตัวดำเนินการ
            if (parts.length == 3) {  // ถ้ามีครบทั้งตัวเลขสองตัวและเครื่องหมาย
                num2 = Double.parseDouble(parts[2]);  // แปลงตัวเลขที่สองเป็น double
                switch (operator) {  // คำนวณตามเครื่องหมายที่เลือก
                    case '+':
                        result = num1 + num2;
                        break;
                    case '-':
                        result = num1 - num2;
                        break;
                    case '*':
                        result = num1 * num2;
                        break;
                    case '/':
                        result = num1 / num2;
                        break;
                }
                display.setText(display.getText() + " = " + result);  // แสดงผลลัพธ์ในช่องแสดงผล
                resultDisplayed = true;  // ตั้งค่าว่ามีการแสดงผลลัพธ์แล้ว
            }
        } else {  // กรณีที่กดปุ่มเครื่องหมายคำนวณ
            if (resultDisplayed) {  // ถ้ามีการแสดงผลลัพธ์อยู่แล้ว
                display.setText("");  // ล้างช่องแสดงผลเพื่อคำนวณใหม่
                operatorSet = false;  // รีเซ็ตค่าเครื่องหมาย
                resultDisplayed = false;  // ตั้งค่าว่าไม่มีผลลัพธ์แล้ว
            }
            if (operatorSet) {  // ถ้ามีการตั้งค่าตัวดำเนินการแล้ว
                String currentText = display.getText();  // เก็บข้อความในช่องแสดงผล
                int lastSpaceIndex = currentText.lastIndexOf(' ');  // หาตำแหน่งของช่องว่างสุดท้าย
                if (lastSpaceIndex != -1) {  // ถ้าพบช่องว่าง
                    currentText = currentText.substring(0, lastSpaceIndex);  // ตัดข้อความส่วนสุดท้ายออก
                    display.setText(currentText);  // อัปเดตข้อความในช่องแสดงผล
                }
            }
            num1 = Double.parseDouble(display.getText());  // แปลงตัวเลขแรกเป็น double
            operator = command.charAt(0);  // เก็บเครื่องหมายคำนวณที่กด
            display.setText(display.getText() + " " + operator + " ");  // แสดงเครื่องหมายในช่องแสดงผล
            operatorSet = true;  // ตั้งค่าว่าได้เลือกเครื่องหมายแล้ว
        }
    }

    // เมธอด main สำหรับรันโปรแกรม
    public static void main(String[] args) {
        new calculatorGUI();  // สร้างอินสแตนซ์ของ calculatorGUI และแสดงหน้าต่าง
    }
}
