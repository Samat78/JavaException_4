/**
 * Подтверждение пароля
 * Samat, Absamatov
 * 20 Августа 2023 г.
 * Используйте 5 критериев правил для проверки надежности паролей
 */


import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

public class PasswordVerifier extends JFrame
{
    //Создаем экземпляры всех компонентов JFrame
    private Container contents;
    private JTextArea instructions, brokenRules;
    private JLabel strength;
    private JTextField usrPWord;

    /**
     * Класс PasswordVerifier создает графический интерфейс JFrame и размещает все
     * компоненты в панель содержимого.
     */
    public PasswordVerifier()
    {
        //Установить макет JFrame
        super("Тестер надежности пароля");
        contents = getContentPane();
        contents.setLayout( new FlowLayout(FlowLayout.LEFT) );
        instructions = new JTextArea("Пожалуйста, введите желаемый пароль.\n"
                + "\nВаш пароль должен соответствовать этим условиям:\n"
                + "Правило 1: он должен содержать не менее 8 символов и не должен содержать пробелов.\n"
                + "Правило 2: он должен содержать хотя бы один специальный символ, " +
                "который не является буквой или цифрой.\n"
                + "Правило 3: он должен содержать хотя бы одну заглавную букву.\n"
                + "Правило 4: Он должен содержать хотя бы одну строчную букву.\n"
                + "Правило 5: он должен содержать хотя бы одну цифру.");
        instructions.setOpaque(false);
        instructions.setEditable(false);

        //Это объяснит, насколько надежен введенный пароль.
        strength = new JLabel("");

        //Это TextArea будет отображать правила, которые нарушил пароль.
        brokenRules = new JTextArea("");
        brokenRules.setEditable(false);
        brokenRules.setOpaque(false);
        brokenRules.setForeground( Color.RED);

        usrPWord = new JTextField( "", 15 );

        contents.add( instructions );
        contents.add( usrPWord );
        contents.add( strength );
        contents.add( brokenRules );

        //Создаем textFieldHandler для ввода данных пользователем
        TextFieldHandler tfh = new TextFieldHandler();

        usrPWord.addActionListener( tfh );

        setSize( 600, 300 );
        setVisible( true );
    }

    /**
     * TextFieldHandler определяет, как программа будет реагировать на различные
     * пользовательский ввод в JTextField.
     */
    private class TextFieldHandler implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            /**
             * Вызывает функции для проверки каждого правила
             * если бы пароль соответствовал правилу, функция вернула бы 1
             * в противном случае функция вернет 0.
             */
            String password = usrPWord.getText();
            int length, spChar, lowCase, upCase, digit;
            length = lengthChecker(password);
            spChar = specialCharacter(password);
            upCase = upperCase(password);
            lowCase = lowerCase(password);
            digit = digitize(password);

            // Запускаем pStrength и добавляем 1 или 0 в зависимости от того, что вернула функция.
            // pStrength — это сила пароля из 5 возможных.
            int pStrength = 0;
            pStrength = length + spChar + upCase + lowCase + digit;


            strength.setText("Ваш пароль имеет силу: " + pStrength
                    + " out of 5.");

            //Определить разные результаты для разных сильных сторон.
            if (pStrength == 5)
            {
                brokenRules.setForeground( Color.BLUE );
                brokenRules.setText("Поздравляем, вы профессионал в области паролей!");
            }

            else if (pStrength == 0 )
            {
                brokenRules.setForeground( Color.RED );
                brokenRules.setText("Ты серьезно?! ты нарушил каждое правило!");
            }
            else
            {
                brokenRules.setForeground( Color.RED );
                brokenRules.setText("Ваш пароль нарушил следующее правило(s):\n");

                /**
                 * Эти правила будут добавлены в список нарушенных правил, если какая-либо из функций
                 * вернул 0. что позволяет нарушить несколько правил, а также
                 * допуская любую возможную комбинацию нарушенных правил.
                 */
                if (length == 0)
                {
                    brokenRules.append("  Правило 1: должно быть не менее 8 символов., "
                            + "и он не должен содержать пробелов.\n");
                }
                if (spChar == 0)
                {
                    brokenRules.append("  Правило 2: он должен содержать хотя бы один специальный символ., "
                            + "что не является буквой или цифрой.\n");
                }
                if (upCase == 0)
                    brokenRules.append("  Правило 3: он должен содержать хотя бы одну заглавную букву.\n");
                if (lowCase == 0)
                    brokenRules.append("  Правило 4: Он должен содержать хотя бы одну строчную букву.\n");
                if (digit == 0)
                    brokenRules.append("  Правило 5: он должен содержать хотя бы одну цифру.\n");
            }
        }
    }

    /**
     * Это основная функция, которая вызывает созданный нами фрейм для отображения пользовательского
     * дружественное окно для пользователя.
     */
    public static void main( String [] args )
    {
        PasswordVerifier passVerifier = new PasswordVerifier( );
        passVerifier.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Следует отметить, что все следующие функции возвращают 1 или
     * 0 в зависимости от того, соответствует ли пароль правилу или нет.
     * Если пароль соответствует правилу, будет возвращена 1, увеличивающая надежность
     * пароля.
     * Если пароль не соответствует правилу, будет возвращен 0, без добавления
     * любая сила пароля.
     */

    /**
     * Эта функция проверяет наличие пробелов в пароле.
     * Он также проверяет, соответствует ли пароль требуемому минимуму из 8 символов.
     */
    public static int lengthChecker(String s)
    {
        if (s.contains(" "))
        {
            return 0;
        }
        else
        {
            if (s.length() >= 8)
                return 1;
            else
                return 0;
        }
    }

    /**
     * Эта функция проверяет, содержит ли введенный пароль специальный символ.
     * Я убедился, что пробел не считается специальным символом.
     */
    public static int specialCharacter(String s)
    {
        /**
         * Шаблон регулярного выражения, который я создал для специальных символов, в основном любой
         * символ, который не является буквой алфавита, цифрой или пробелом.
         */
        Pattern spChar = Pattern.compile("[^A-Za-z0-9 ]");
        Matcher m = spChar.matcher(s);
        if (m.find())
            return 1;
        else
            return 0;

    }

    /**
     * Эта функция использует регулярное выражение [A-Z], которое проверяет наличие
     * заглавная буква в пароле.
     */
    public static int upperCase(String s)
    {
        Pattern upper = Pattern.compile("[A-Z]");
        Matcher m = upper.matcher(s);
        if (m.find())
            return 1;
        else
            return 0;
    }

    /**
     * Эта функция использует регулярное выражение [a-z], которое проверяет, существует ли
     * строчная буква в пароле.
     */
    public static int lowerCase(String s)
    {
        Pattern lower = Pattern.compile("[a-z]");
        Matcher m = lower.matcher(s);
        if (m.find())
            return 1;
        else
            return 0;
    }

    /**
     * Эта функция использует регулярное выражение [0-9], которое проверяет, есть ли
     * — число в пароле.
     */
    public static int digitize(String s)
    {
        Pattern digit = Pattern.compile("[0-9]");
        Matcher m = digit.matcher(s);
        if (m.find())
            return 1;
        else
            return 0;
    }
}