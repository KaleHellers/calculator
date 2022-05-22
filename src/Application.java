import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ContentHandler;
import java.text.BreakIterator;
import javax.swing.*;
import javax.swing.undo.CannotUndoException;

import static java.lang.Math.abs;

/*
Назначение кнопок:
0-9 : ввести цифру
. : поставить точку
C : очистить поле ввода, и забыть операцию
B : стереть однц цифру
+,-,*,/ : простейшие операции
^ : возведение в степень
NO PLS : тупа хз, не стоит нажимать
*/

public class Application extends JFrame {

    static StringBuilder FirstNum = new StringBuilder("");
    static StringBuilder CurSign = new StringBuilder("");
    static StringBuilder CurNum = new StringBuilder("");
    static JLabel result = new JLabel(CurNum.toString());

    static boolean BROKEN = false;

    public static void calculate() {

        if(!FirstNum.toString().equals("") && !FirstNum.toString().equals("Error") && !CurNum.toString().equals("") && !CurNum.toString().equals("Error")) {
            double fst = Double.parseDouble(FirstNum.toString());
            double scd = Double.parseDouble(CurNum.toString());
            double res = 0;

            if(CurSign.toString().equals("+")) {
                res = fst + scd;
            }
            if(CurSign.toString().equals("-")) {
                res = fst - scd;
            }
            if(CurSign.toString().equals("*")) {
                res = fst * scd;
            }
            if(CurSign.toString().equals("/")) {
                res = fst / scd;
            }
            if(CurSign.toString().equals("^")) {
                res = Math.pow(fst, scd);
            }

            CurNum.setLength(0);
            CurNum.append(Double.toString(res));
            if(CurNum.charAt(CurNum.length() - 1) == '0' && CurNum.charAt(CurNum.length() - 2) == '.') {
                CurNum.setLength(CurNum.length() - 2);
            }

            if(scd == 0.0 && CurSign.toString().equals("/")) {
                CurNum.setLength(0);
                CurNum.append("ERROR");
            }

            FirstNum.setLength(0);
            CurSign.setLength(0);
        }
    }

    public void buttonsInit(JPanel buttons) {
        String signs[] = {"C", "NO PLS", "B", "^", "1", "2", "3", "+", "4", "5", "6", "-", "7", "8", "9", "*", ".", "0", "=", "/"};

        for(String sign : signs) {
            JButton btn = new JButton(sign);

            ActionListener listener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    JButton btn = (JButton) e.getSource();

                    String command = btn.getText();

                    if(command == "FIX") {
                        BROKEN = false;
                        btn.setText("NO PLS");
                        CurNum.setLength(0);
                    }

                    if(BROKEN) return;

                    if(
                            command.equals("0") ||
                            command.equals("1") ||
                            command.equals("2") ||
                            command.equals("3") ||
                            command.equals("4") ||
                            command.equals("5") ||
                            command.equals("6") ||
                            command.equals("7") ||
                            command.equals("8") ||
                            command.equals("9") ||
                            command.equals(".") //Можно было завезти регулярку, но мне было лень, соре...
                    ) {
                        if(command == "." && CurNum.indexOf(".") != -1) {
                            return;
                        }

                        CurNum.append(command);
                    } else if(command == "+" || command == "-" || command == "*" || command == "/" || command == "^") {
                        FirstNum.setLength(0);
                        FirstNum.append(CurNum.toString());

                        CurSign.setLength(0);
                        CurSign.append(command);
                        CurNum.setLength(0);
                    } else if(command == "=") {
                        calculate();
                    } else if(command == "C") {
                        FirstNum.setLength(0);
                        CurSign.setLength(0);
                        CurNum.setLength(0);
                    } else if(command == "B") {
                        if(CurNum.length() <= 1) {
                            CurNum.setLength(0);
                        } else if(CurNum.charAt(CurNum.length() - 2) == '.') {
                            CurNum.setLength(CurNum.length() - 2);
                        } else {
                            CurNum.setLength(CurNum.length() - 1);
                        }
                    } else if(command == "NO PLS") {
                        CurNum.setLength(0);
                        CurNum.append("YOU BROKE MY CALCULATOR");
                        btn.setText("FIX");
                        BROKEN = true;
                    }

                    result.setText(CurNum.toString());
                }
            };

            btn.addActionListener(listener);

            buttons.add(btn);
        }
    }



    public Application() {
        super("Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(500, 300);

        JPanel buttons = new JPanel(new GridLayout(5, 4, 5, 5));
        JPanel num = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        buttonsInit(buttons);
        num.add(result);

        Container container = getContentPane();

        container.add(num);
        container.add(buttons, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args)
    {
        JFrame.setDefaultLookAndFeelDecorated(true);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Application();
            }
        });
    }
}
