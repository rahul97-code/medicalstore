package hms.store.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class FocusTextField extends JTextField {
    {
        addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                FocusTextField.this.select(0, getText().length());
            }

            @Override
            public void focusLost(FocusEvent e) {
                FocusTextField.this.select(0, 0);
            }
        });
    }
}