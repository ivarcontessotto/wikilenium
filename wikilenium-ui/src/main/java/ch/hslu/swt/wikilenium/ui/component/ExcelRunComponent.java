package ch.hslu.swt.wikilenium.ui.component;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.common.base.Strings;

import ch.hslu.swt.wikilenium.core.TestBatchRunner;
import ch.hslu.swt.wikilenium.core.Wikilenium;
import ch.hslu.swt.wikilenium.ui.model.Language;

public class ExcelRunComponent extends JPanel {

    private File excelFile;
    private JLabel fileLabel;

    public ExcelRunComponent() {
        super();
        GridLayout layout = new GridLayout(0, 2);
        this.setLayout(layout);
        // Add the components
        JComboBox<Language> languageComboBox = new JComboBox<>(Language.values());
        add(new JLabel("Language"));
        add(languageComboBox);

        JButton chooseFile = new JButton("Choose File");
        chooseFile.addActionListener(this::onChooseFile);
        add(new JLabel("Excel File"));
        add(chooseFile);

        fileLabel = new JLabel("Please select a file");
        add(new JLabel());
        add(fileLabel);

        JTextField goalPageTextField = new JTextField();
        goalPageTextField.setText("Philosophie");
        add(new JLabel("Goal Page"));
        add(goalPageTextField);

        JTextField maximumNumberOfClicks = new JTextField();
        maximumNumberOfClicks.setText("20");
        add(new JLabel("Maximum Number of Clicks"));
        add(maximumNumberOfClicks);

        JButton runButton = new JButton("Run");
        runButton.addActionListener(onClick -> {
            Language selectedLanguage = (Language) languageComboBox.getSelectedItem();
            String goalPage = goalPageTextField.getText();
            String maximumNumber = maximumNumberOfClicks.getText();

            List<String> validationMessages = validateInput(selectedLanguage, goalPage, excelFile, maximumNumber);
            if (validationMessages.isEmpty()) {
                try {
                    TestBatchRunner testRunner = new Wikilenium().getTestBatchRunner(Wikilenium.Browser.CHROME);
                    testRunner.language(selectedLanguage.getId()).inputOutputFile(excelFile).goalPage(goalPage)
                            .clickLimit(Integer.parseInt(maximumNumber)).run();
                    JOptionPane.showMessageDialog(this, "Finished tests based on Excel File", "Tests finished",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }

            } else {
                JOptionPane.showMessageDialog(this, validationMessages.stream().collect(Collectors.joining("\n")),
                        "Validation failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(new JLabel());
        add(runButton);
        this.setVisible(true);
    }

    private List<String> validateInput(Language selectedLanguage, String goalPage, File excelFile,
            String maximumNumber) {
        List<String> messages = new ArrayList<>();
        if (selectedLanguage == null) {
            messages.add("A language must be selected");
        }
        if (Strings.isNullOrEmpty(goalPage)) {
            messages.add("A goal page must be entered");
        }
        if (excelFile == null) {
            messages.add("A file must be selected");
        } else if (!excelFile.getAbsolutePath().endsWith(".xlsx")) {
            messages.add("The selected file must be an Excel-File");
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

    private void onChooseFile(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
        int returnVal = fc.showOpenDialog(ExcelRunComponent.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            excelFile = fc.getSelectedFile();
            fileLabel.setText(excelFile.getAbsolutePath());

        } else {
            excelFile = null;
            fileLabel.setText("Please select a file");
        }
    }
}
