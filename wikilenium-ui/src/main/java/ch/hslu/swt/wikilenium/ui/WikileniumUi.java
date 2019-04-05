package ch.hslu.swt.wikilenium.ui;

import java.awt.GridLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JWindow;

import ch.hslu.swt.wikilenium.ui.component.ExcelRunComponent;
import ch.hslu.swt.wikilenium.ui.component.SingleRunComponent;

public class WikileniumUi {

    public static void main(String[] args) {
        WikileniumUi ui = new WikileniumUi();
        ui.open();
    }

    private void open() {

        JFrame frame = new JFrame("Wikilenium Test");
        GridLayout layout = new GridLayout(0, 1);
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Run Single Test", new SingleRunComponent());
        tabPane.addTab("Run Excel File Test", new ExcelRunComponent());
        frame.setLayout(layout);

        URL imgURL = WikileniumUi.class.getClassLoader().getResource("icon.png");
        ImageIcon icon = new ImageIcon(imgURL);
        frame.setIconImage(icon.getImage());
        frame.add(tabPane);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JWindow window = new JWindow(frame);
        window.setVisible(true);
    }

}
