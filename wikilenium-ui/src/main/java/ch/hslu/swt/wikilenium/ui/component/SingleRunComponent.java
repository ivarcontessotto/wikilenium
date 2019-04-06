package ch.hslu.swt.wikilenium.ui.component;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.google.common.base.Strings;

import ch.hslu.swt.wikilenium.core.TestResult;
import ch.hslu.swt.wikilenium.core.TestRunner;
import ch.hslu.swt.wikilenium.core.Wikilenium;
import ch.hslu.swt.wikilenium.ui.model.Language;

public class SingleRunComponent extends JPanel {

    public SingleRunComponent() {
        super();
        GridLayout layout = new GridLayout(0, 2);
        this.setLayout(layout);
        // Add the components
        JComboBox<Language> languageComboBox = new JComboBox<>(Language.values());
        add(new JLabel("Language"));
        add(languageComboBox);

        JTextField startPageTextField = new JTextField();
        add(new JLabel("Start Page"));
        add(startPageTextField);

        JTextField endPageTextField = new JTextField();
        add(new JLabel("Goal Page"));
        add(endPageTextField);

        JTextField maximumNumberOfClicks = new JTextField();
        add(new JLabel("Maximum Number of Clicks"));
        add(maximumNumberOfClicks);

        JButton runButton = new JButton("Run");
        runButton.addActionListener(onClick -> {
            Language selectedLanguage = (Language) languageComboBox.getSelectedItem();
            String startPage = startPageTextField.getText();
            String endPage = endPageTextField.getText();
            String maximumNumber = maximumNumberOfClicks.getText();

            List<String> validationMessages = validateInput(selectedLanguage, startPage, endPage, maximumNumber);
            if (validationMessages.isEmpty()) {
                TestRunner testRunner = new Wikilenium().getTestRunner(Wikilenium.Browser.CHROME);
                TestResult result = testRunner.language(selectedLanguage.getId()).startPage(startPage).goalPage(endPage)
                        .clickLimit(Integer.parseInt(maximumNumber)).run();
                if (result.isPassed()) {
                    JOptionPane.showMessageDialog(this, "Congratulations! The goal page was reached", "Test passed",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, result.getFailReason(), "Test failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, validationMessages.stream().collect(Collectors.joining("\n")),
                        "Test failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(new JLabel());
        add(runButton);
        this.setVisible(true);
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
        } else {
            try {
                Integer.parseInt(maximumNumber);
            } catch (NumberFormatException e) {
                messages.add("The maximum number of clicks must be a number");
            }
        }
        return messages;
    }
}
