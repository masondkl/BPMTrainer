package me.mason.bpmtrainer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class GUI {
    private static final Color
            BG = new Color(81, 94, 99),
            ACCENT_LIGHT = new Color(87, 131, 123),
            ACCENT_DARK = new Color(201, 216, 182),
            TEXT = new Color(241, 236, 195);
    private static final Font FONT = new Font("Latha", Font.BOLD, 14);
    private static final Font FONT_LARGE = new Font("Latha", Font.BOLD, 14);
    private static final Set<Character> VALID_TARGET_BPM_KEYS = new HashSet<>();

    static {
        for (char c : new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}) VALID_TARGET_BPM_KEYS.add(c);
    }

    public static void main(String[] args) {
        new GUI();
    }

    private final JPanel root = new JPanel();
    private final JButton tapButton = new JButton(), leftButton = new JButton(), rightButton = new JButton();
    private final JLabel tappingBPMLabel = new JLabel(), targetBPMLabel = new JLabel();
    private final JTextPane targetBPMInput = new JTextPane();
    private long startTapping = 0;
    private int taps = 0, targetBPM = 170;
    private char leftKey = ' ', rightKey = ' ';
    private boolean insideTapButton = false, choosingLeftKey = false, choosingRightKey = false;

    public GUI() {
        JFrame frame = new JFrame();

        frame.setTitle("BPM Trainer");
        frame.setSize(300, 150);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        resizeListener();
        moveListener();
        pressListener();
        stylize();

        tapButton.setText("TAP");

        leftButton.setText("LEFT KEY");
        rightButton.setText("RIGHT KEY");

        tappingBPMLabel.setText("NOT TAPPING");
        targetBPMLabel.setText("TARGET BPM:");

        targetBPMInput.setText("170");

        root.setLayout(null);
        root.add(tapButton);
        root.add(leftButton);
        root.add(rightButton);
        root.add(targetBPMLabel);
        root.add(targetBPMInput);
        root.add(tappingBPMLabel);

        frame.setContentPane(root);
        frame.setVisible(true);

        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!insideTapButton) return;

                long duration = System.currentTimeMillis() - startTapping;
                double tappingBPM = (double) taps / ((double) duration / (60000D/4D));

                tappingBPMLabel.setText(String.format("%.2f BPM %s", tappingBPM, tappingBPM > targetBPM ? "TAP SLOWER" : "TAP FASTER"));
            }
        });

        timer.start();
    }

    private void stylize() {
        root.setBackground(BG);

        targetBPMInput.setBackground(BG);

        tapButton.setBackground(ACCENT_LIGHT);
        tapButton.setForeground(TEXT);
        tapButton.setFont(FONT);
        tapButton.setBorderPainted(false);
        tapButton.setFocusPainted(false);
        tapButton.setContentAreaFilled(false);
        tapButton.setOpaque(true);
        tapButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                if (tapButton.getModel().isPressed()) {
                    tapButton.setBackground(ACCENT_DARK);
                } else if (tapButton.getModel().isRollover()) {
                    tapButton.setBackground(ACCENT_LIGHT);
                } else {
                    tapButton.setBackground(ACCENT_LIGHT);
                }
                tapButton.revalidate();
                tapButton.repaint();
            }
        });

        leftButton.setBackground(ACCENT_LIGHT);
        leftButton.setForeground(TEXT);
        leftButton.setFont(FONT);
        leftButton.setBorderPainted(false);
        leftButton.setFocusPainted(false);
        leftButton.setContentAreaFilled(false);
        leftButton.setOpaque(true);
        leftButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                if (leftButton.getModel().isPressed()) {
                    leftButton.setBackground(ACCENT_DARK);
                } else if (leftButton.getModel().isRollover()) {
                    leftButton.setBackground(ACCENT_LIGHT);
                } else {
                    leftButton.setBackground(ACCENT_LIGHT);
                }
                leftButton.revalidate();
                leftButton.repaint();
            }
        });

        rightButton.setBackground(ACCENT_LIGHT);
        rightButton.setForeground(TEXT);
        rightButton.setFont(FONT);
        rightButton.setBorderPainted(false);
        rightButton.setFocusPainted(false);
        rightButton.setContentAreaFilled(false);
        rightButton.setOpaque(true);
        rightButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                if (rightButton.getModel().isPressed()) {
                    rightButton.setBackground(ACCENT_DARK);
                } else if (rightButton.getModel().isRollover()) {
                    rightButton.setBackground(ACCENT_LIGHT);
                } else {
                    rightButton.setBackground(ACCENT_LIGHT);
                }
                rightButton.revalidate();
                rightButton.repaint();
            }
        });

        tappingBPMLabel.setHorizontalAlignment(SwingConstants.CENTER);
        targetBPMLabel.setHorizontalAlignment(SwingConstants.CENTER);

        SimpleAttributeSet attribs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
        targetBPMInput.setParagraphAttributes(attribs, true);
        setJTextPaneFont(targetBPMInput, FONT_LARGE, TEXT);

        targetBPMLabel.setFont(FONT_LARGE);
        targetBPMLabel.setForeground(TEXT);
        tappingBPMLabel.setFont(FONT_LARGE);
        tappingBPMLabel.setForeground(TEXT);
    }

    private void onSizeChange(int width, int height) {
        tapButton.setBounds(0, 0, width/2, height/2);

        leftButton.setBounds(width/2+1, 0, width/2-1, height/4);
        rightButton.setBounds(width/2+1, height/4+1, width/2-1, height/4);

        tappingBPMLabel.setBounds(0, height/2 + height/4, width, height/4);
        targetBPMLabel.setBounds(0, height/2, width/2, height/4);

        targetBPMInput.setBounds(width/2, height/2, width/2, height/4);

        root.revalidate();
        root.repaint();
    }

    private void pressListener() {
        leftButton.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) { }
            @Override public void keyPressed(KeyEvent e) {
                if (choosingLeftKey) {
                    leftKey = e.getKeyChar();
                    choosingLeftKey = false;
                    leftButton.setText(String.valueOf(Character.toUpperCase(leftKey)));
                }
            }
            @Override public void keyReleased(KeyEvent e) { }
        });
        rightButton.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) { }
            @Override public void keyPressed(KeyEvent e) {
                if (choosingRightKey) {
                    rightKey = e.getKeyChar();
                    choosingRightKey = false;
                    rightButton.setText(String.valueOf(Character.toUpperCase(rightKey)));
                }
            }
            @Override public void keyReleased(KeyEvent e) { }
        });
        tapButton.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) { }
            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();

                if (!insideTapButton || choosingLeftKey || choosingRightKey || (keyChar != leftKey && keyChar != rightKey)) return;
                if (taps == 0) startTapping = System.currentTimeMillis();

                taps++;
                tapButton.getModel().setPressed(true);

                if (taps >= 20) {
                    taps /= 2;
                    startTapping = startTapping + ((System.currentTimeMillis() - startTapping) / 2);
                }
            }
            @Override public void keyReleased(KeyEvent e) {
                tapButton.getModel().setPressed(false);
            }
        });
        targetBPMInput.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) { }
            @Override public void keyPressed(KeyEvent e) { }
            @Override public void keyReleased(KeyEvent e) {
                String current = targetBPMInput.getText();
                if (!VALID_TARGET_BPM_KEYS.contains(e.getKeyChar()) && current.length() > 0) targetBPMInput.setText(current.substring(0, current.length()-1));

                try { targetBPM = Integer.parseInt(targetBPMInput.getText()); } catch (NumberFormatException ignored) {}
            }
        });
        leftButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (choosingRightKey || choosingLeftKey) return;

                choosingLeftKey = true;
                leftButton.setText("CHOOSING");
            }
            @Override public void mousePressed(MouseEvent e) { }
            @Override public void mouseReleased(MouseEvent e) { }
            @Override public void mouseEntered(MouseEvent e) { }
            @Override public void mouseExited(MouseEvent e) { }
        });
        rightButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (choosingLeftKey || choosingRightKey) return;

                choosingRightKey = true;
                rightButton.setText("CHOOSING");
            }
            @Override public void mousePressed(MouseEvent e) { }
            @Override public void mouseReleased(MouseEvent e) { }
            @Override public void mouseEntered(MouseEvent e) { }
            @Override public void mouseExited(MouseEvent e) { }
        });
        targetBPMInput.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                targetBPMInput.setText("");
            }
            @Override public void mousePressed(MouseEvent e) { }
            @Override public void mouseReleased(MouseEvent e) { }
            @Override public void mouseEntered(MouseEvent e) { }
            @Override public void mouseExited(MouseEvent e) { }
        });
    }

    private void moveListener() {
        tapButton.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) { }
            @Override public void mousePressed(MouseEvent e) { }
            @Override public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) {
                insideTapButton = true;
                taps = 0;
                tapButton.requestFocus();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                insideTapButton = false;
                tappingBPMLabel.setText("NOT TAPPING");
            }
        });

        leftButton.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) { }
            @Override public void mousePressed(MouseEvent e) { }
            @Override public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) {
                leftButton.requestFocus();
            }
            @Override public void mouseExited(MouseEvent e) { }
        });

        rightButton.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) { }
            @Override public void mousePressed(MouseEvent e) { }
            @Override public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) {
                leftButton.requestFocus();
            }
            @Override public void mouseExited(MouseEvent e) { }
        });
    }

    private void resizeListener() {
        root.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                onSizeChange(root.getWidth(), root.getHeight());
            }
            @Override public void componentMoved(ComponentEvent e) { }
            @Override public void componentShown(ComponentEvent e) { }
            @Override public void componentHidden(ComponentEvent e) { }
        });
    }

    //http://javatechniques.com/blog/setting-jtextpane-font-and-color/
    /**
     * Utility method for setting the font and color of a JTextPane. The
     * result is roughly equivalent to calling setFont(...) and
     * setForeground(...) on an AWT TextArea.
     */
    public static void setJTextPaneFont(JTextPane jtp, Font font, Color c) {
        // Start with the current input attributes for the JTextPane. This
        // should ensure that we do not wipe out any existing attributes
        // (such as alignment or other paragraph attributes) currently
        // set on the text area.
        MutableAttributeSet attrs = jtp.getInputAttributes();

        // Set the font family, size, and style, based on properties of
        // the Font object. Note that JTextPane supports a number of
        // character attributes beyond those supported by the Font class.
        // For example, underline, strike-through, super- and sub-script.
        StyleConstants.setFontFamily(attrs, font.getFamily());
        StyleConstants.setFontSize(attrs, font.getSize());
        StyleConstants.setItalic(attrs, (font.getStyle() & Font.ITALIC) != 0);
        StyleConstants.setBold(attrs, (font.getStyle() & Font.BOLD) != 0);

        // Set the font color
        StyleConstants.setForeground(attrs, c);

        // Retrieve the pane's document object
        StyledDocument doc = jtp.getStyledDocument();

        // Replace the style for the entire document. We exceed the length
        // of the document by 1 so that text entered at the end of the
        // document uses the attributes.
        doc.setCharacterAttributes(0, doc.getLength() + 1, attrs, false);
    }

}
