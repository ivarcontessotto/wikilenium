package ch.hslu.swt.wikilenium.ui;

import java.awt.GridLayout;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JWindow;

import com.google.common.base.Strings;

import ch.hslu.swt.wikilenium.client.TestClient;
import ch.hslu.swt.wikilenium.client.Wikilenium;
import ch.hslu.swt.wikilenium.ui.model.Language;

public class WikileniumUi {

    public static void main(String[] args) {
        WikileniumUi ui = new WikileniumUi();
        ui.open();
    }

    private void open() {

        JFrame frame = new JFrame("Wikilenium Test");
        GridLayout layout = new GridLayout(0, 2);
        frame.setLayout(layout);

        URL imgURL = WikileniumUi.class.getClassLoader().getResource("icon.png");
        ImageIcon icon = new ImageIcon(imgURL);
        frame.setIconImage(icon.getImage());

        // Add the components
        JComboBox<Language> languageComboBox = new JComboBox<>(Language.values());
        frame.add(new JLabel("Language"));
        frame.add(languageComboBox);

        JTextField startPageTextField = new JTextField();
        frame.add(new JLabel("Start Page"));
        frame.add(startPageTextField);

        JTextField endPageTextField = new JTextField();
        frame.add(new JLabel("Goal Page"));
        frame.add(endPageTextField);

        JTextField maximumNumberOfClicks = new JTextField();
        frame.add(new JLabel("Maximum Number of Clicks"));
        frame.add(maximumNumberOfClicks);

        JButton runButton = new JButton("Run");
        runButton.addActionListener(onClick -> {
            Language selectedLanguage = (Language) languageComboBox.getSelectedItem();
            String startPage = startPageTextField.getText();
            String endPage = endPageTextField.getText();
            String maximumNumber = maximumNumberOfClicks.getText();

            List<String> validationMessages = validateInput(selectedLanguage, startPage, endPage, maximumNumber);
            if (validationMessages.isEmpty()) {
                TestClient testClient = new Wikilenium().getChromeClient();
                if (testClient.language(selectedLanguage.getId()).startPage(startPage).goalPage(endPage)
                        .clickLimit(Integer.parseInt(maximumNumber)).run()) {
                    JOptionPane.showMessageDialog(frame, "Congratulations! The goal page was reached",
                            "Test failed", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "The goal page was not reached", "Test failed",
                            JOptionPane.ERROR_MESSAGE);
                }
                testClient.close();
            } else {
                JOptionPane.showMessageDialog(frame, validationMessages.stream().collect(Collectors.joining("\n")),
                        "Test failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        frame.add(new JLabel());
        frame.add(runButton);

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JWindow window = new JWindow(frame);
        window.setVisible(true);
    }

    private List<String> validateInput(Language selectedLanguage, String startPage, String endPage,
            String maximumNumber) {
        List<String> messages = new ArrayList<>();
        if (selectedLanguage == null) {
            messages.add("A language must be selected");
        }
        if (Strings.isNullOrEmpty(startPage)) {
            messages.add("A start page must be entered");
        }
        if (Strings.isNullOrEmpty(endPage)) {
            messages.add("A end page must be entered");
        }
        if (Strings.isNullOrEmpty(maximumNumber)) {
            messages.add("A maximum number of clicks must be entered");
            try {
                Integer.parseInt(maximumNumber);
            } catch (NumberFormatException e) {
                messages.add("The maximum number of clicks must be a number");
            }
        }
        return messages;
    }

}
