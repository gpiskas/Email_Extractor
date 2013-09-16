/*
* Email Extractor
* Copyright (C) 2013 George Piskas
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License along
* with this program; if not, write to the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* Contact: geopiskas@gmail.com
*/

package email.extractor;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class Main extends JFrame {

    private static final long serialVersionUID = 1170212588986667828L;

    private static final String EMAIL_PATTERN = "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
    private JTextArea text, emails;
    private JScrollPane st, se;
    private boolean first = true;

    public Main() {
        super("Email Extractor by Geo Piskas | v1.1");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(4, 4));
        setSize(600, 600);

        JMenuBar menubar = new JMenuBar();
        add(menubar);
        setJMenuBar(menubar);

        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Append Emails to File");
        JMenuItem exit = new JMenuItem("Exit");
        file.add(save);
        file.add(exit);
        menubar.add(file);

        JMenu misc = new JMenu("Misc");
        JMenuItem copy = new JMenuItem("Copy Emails to Clipboard");
        JMenuItem about = new JMenuItem("About");
        misc.add(copy);
        misc.add(about);
        menubar.add(misc);

        text = new JTextArea(5, 20);
        text.setText("Paste text here...");
        st = new JScrollPane(text);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        add(st, BorderLayout.CENTER);
        text.addCaretListener(new ExtractEmails());

        emails = new JTextArea(5, 20);
        emails.setEditable(false);
        se = new JScrollPane(emails);

        emails.setLineWrap(true);
        emails.setWrapStyleWord(true);
        add(se, BorderLayout.EAST);

        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int result = JOptionPane.showConfirmDialog(
                        Main.this,
                        "Are you sure you want to save?\nLocation: "
                                + System.getProperty("user.dir")
                                + "\\emails.txt", "Confirm Action",
                        JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    try {
                        SaveToFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int result = JOptionPane.showConfirmDialog(Main.this,
                        "Are you sure you want to exit?", "Confirm Action",
                        JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    System.exit(0);
                }
            }

        });

        copy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                StringSelection ss = new StringSelection(emails.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(ss, null);
            }
        });

        about.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(Main.this,
                        "Email Extractor 1.0\nBy George Piskas.\ngeopiskas@gmail.com", "About",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        });
    }

    private String getEmails() {
        Pattern p = Pattern.compile(EMAIL_PATTERN);
        Matcher m = p.matcher(text.getText());
        StringBuilder emails = new StringBuilder("");
        while (m.find()) {
            emails.append(m.group() + "\r\n");
        }
        return emails.toString();
    }

    private class ExtractEmails implements CaretListener {

        @Override
        public void caretUpdate(CaretEvent arg0) {
            if (first) {
                text.setText("");
                first = false;
            }
            emails.setText(getEmails());
        }
    }

    private void SaveToFile() throws Exception {
        FileOutputStream out = new FileOutputStream("emails.txt", true);
        out.write(emails.getText().getBytes());
        out.close();
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }

}
