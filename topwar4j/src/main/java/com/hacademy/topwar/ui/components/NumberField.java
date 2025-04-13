package com.hacademy.topwar.ui.components;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.DocumentFilter;

public class NumberField extends JTextField {
	private static final long serialVersionUID = 1L;

	public NumberField() {
		AbstractDocument document = (AbstractDocument) this.getDocument();
		document.setDocumentFilter(documentFilter);
	}

	private DocumentFilter documentFilter = new DocumentFilter() {
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
				throws javax.swing.text.BadLocationException {
			if (string.matches("\\d+")) {
				super.insertString(fb, offset, string, attr);
			}
		};

		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
				throws javax.swing.text.BadLocationException {
			if (text.matches("\\d+")) {
				super.replace(fb, offset, length, text, attrs);
			}
		};
	};
}
