package com.capgemini.library.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.capgemini.library.logic.model.AuthorVO;
import com.capgemini.library.logic.model.BookVO;
import com.capgemini.library.logic.model.LibraryVO;
import com.capgemini.library.logic.rest.AddBookRest;

public class AddBook extends Dialog {
	private Text addTitle;
	private Text addLibrary;
	private Table table;
	private List<AuthorVO> authors = new ArrayList<>();
	private TableViewer authorsTable;
	private WritableList input;
	private AddBookRest addBookService = new AddBookRest();

	public AddBook(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		Label lblTitle = new Label(container, SWT.NONE);
		lblTitle.setText("Title");

		addTitle = new Text(container, SWT.BORDER);
		addTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblAuthors = new Label(container, SWT.NONE);
		lblAuthors.setText("Authors");

		Button btnAddAuthor = new Button(container, SWT.NONE);
		btnAddAuthor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell parentShell = parent.getShell();
				AddAuthor addAuthor = new AddAuthor(parentShell);

				if (addAuthor.open() == Window.OK) {
					input.add(new AuthorVO(0L, addAuthor.getFirstName(), addAuthor.getLastName()));

				}
			}
		});
		btnAddAuthor.setText("Add Author");

		authorsTable = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = authorsTable.getTable();
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
		gd_table.heightHint = 37;
		table.setLayoutData(gd_table);
		createColumns(parent, authorsTable);

		input = new WritableList(authors, AuthorVO.class);
		IBeanValueProperty[] values = BeanProperties.values(new String[] { "firstName", "lastName" });
		ViewerSupport.bind(authorsTable, input, values);

		Label lblLibrary = new Label(container, SWT.NONE);
		lblLibrary.setText("Library");

		addLibrary = new Text(container, SWT.BORDER);
		addLibrary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		return container;
	}

	// overriding this methods allows you to set the
	// title of the custom dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Selection dialog");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 436);
	}

	public void addAuthorToList(String firstName, String lastName) {
		authors.add(new AuthorVO((long) 0, firstName, lastName));

	}

	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "First Name", "Last Name" };
		int[] bounds = { 160, 160 };

		createTableViewerColumn(titles[0], bounds[0], 0);
		createTableViewerColumn(titles[1], bounds[1], 1);

	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(authorsTable, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	@Override
	protected void okPressed() {
		BookVO book = new BookVO((long) 0, addTitle.getText(), authors, new LibraryVO((long) 0, addLibrary.getText()));
		try {
			addBookService.addBook(book);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		super.okPressed();
	}
}
