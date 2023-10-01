
package thang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class phanquyetthang_2520235161 extends JFrame {
    private JTextField display;
    private double currentValue = 0;
    private String currentOperator = "";
    private List<String> calculationHistory = new ArrayList<>();

    public phanquyetthang_2520235161() {
        setTitle("Máy Tính Đơn Giản");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Hiển thị kết quả
        display = new JTextField("0");
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        // Panel chứa các nút số và toán tử
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4));

        String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C", "Lưu Lịch Sử"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);

        // Đổi giao diện sang Nimbus
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pack();
        setLocationRelativeTo(null);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.matches("[0-9]") || command.equals(".")) {
                if (display.getText().equals("0")) {
                    display.setText(command);
                } else {
                    display.setText(display.getText() + command);
                }
            } else if (command.matches("[+\\-*/]")) {
                if (!currentOperator.isEmpty()) {
                    calculate();
                }
                currentOperator = command;
                currentValue = Double.parseDouble(display.getText());
                display.setText("0");
            } else if (command.equals("=")) {
                if (!currentOperator.isEmpty()) {
                    calculate();
                    currentOperator = "";
                }
            } else if (command.equals("C")) {
                display.setText("0");
            } else if (command.equals("Lưu Lịch Sử")) {
                try {
                    saveHistoryToFile("cal_history.txt");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void calculate() {
        double newValue = Double.parseDouble(display.getText());
        switch (currentOperator) {
            case "+":
                currentValue += newValue;
                break;
            case "-":
                currentValue -= newValue;
                break;
            case "*":
                currentValue *= newValue;
                break;
            case "/":
                if (newValue != 0) {
                    currentValue /= newValue;
                } else {
                    display.setText("Lỗi");
                    return;
                }
                break;
        }
        display.setText(Double.toString(currentValue));
        calculationHistory.add(currentValue + " " + currentOperator + " " + newValue + " = " + currentValue);
    }

    private void saveHistoryToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
        for (String historyItem : calculationHistory) {
            writer.write(historyItem);
            writer.newLine();
        }
        writer.close();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            phanquyetthang_2520235161 calculator = new phanquyetthang_2520235161();
            calculator.setVisible(true);
        });
    }
}
