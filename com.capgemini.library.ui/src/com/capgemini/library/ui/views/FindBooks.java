package com.capgemini.library.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.capgemini.library.logic.model.BookVO;
import com.capgemini.library.logic.rest.FindBooksRest;
import com.capgemini.library.logic.rest.RemoveBooksRest;
import com.capgemini.library.ui.filters.BookTitleFilter;

public class FindBooks extends ViewPart {
	private Table table;
	private FindBooksRest findBooksService = new FindBooksRest();
	private RemoveBooksRest removeBookService = new RemoveBooksRest();
	private TableViewer tableBooks;
	private Text findByTitle;
	private WritableList input;
	private BookTitleFilter filter;
	private String prefix = "";

	public FindBooks() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(3, false));
		new Label(parent, SWT.NONE);

		Label lblNewLabel = new Label(parent, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 301;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Seach Books");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		findByTitle = new Text(parent, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 335;
		findByTitle.setLayoutData(gd_text);
		findByTitle.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

		prefix = findByTitle.getText();

		createViewer(parent, prefix);

		{
			Button btnAddBook = new Button(parent, SWT.LEFT);
			btnAddBook.addSelectionListener(actionAddBook(parent));
			btnAddBook.setText("Add Book");
		}
		new Label(parent, SWT.LEFT);
		{
			Button btnRemoveBook = new Button(parent, SWT.NONE);
			btnRemoveBook.addSelectionListener(actionDelate());
			btnRemoveBook.setText("Delete book");
		}
		new Label(parent, SWT.NONE);

		Menu menu = new Menu(parent);
		parent.setMenu(menu);

		MenuItem mntmAddBook = new MenuItem(menu, SWT.NONE);
		mntmAddBook.setText("Add Book");
		mntmAddBook.addSelectionListener(actionAddBook(parent));

		filter = new BookTitleFilter(findByTitle.getText());
		tableBooks.addFilter(filter);
		findByTitle.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				prefix = findByTitle.getText();
				filter.setPrefix(prefix);
				tableBooks.refresh();
			}
		});

	}

	private void createViewer(Composite parent, String prefix) {
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		tableBooks = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableBooks.getTable();
		GridData gd_table = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_table.widthHint = 497;
		gd_table.heightHint = 311;
		table.setLayoutData(gd_table);
		createColumns(parent, tableBooks);

		List<BookVO> books = new ArrayList<>();
		try {
			books = findBooksService.sendGet(prefix);
		} catch (Exception e) {
			e.printStackTrace();
		}

		input = new WritableList(books, BookVO.class);
		IBeanValueProperty[] values = BeanProperties.values(new String[] { "id", "title", "authors", "libraryName" });
		ViewerSupport.bind(tableBooks, input, values);

		{
			Menu menu = new Menu(table);
			table.setMenu(menu);

			MenuItem mntmDelate = new MenuItem(menu, SWT.NONE);
			mntmDelate.setText("Delate Book");
			mntmDelate.addSelectionListener(actionDelate());

			MenuItem mntmAdd = new MenuItem(menu, SWT.NONE);
			mntmAdd.setText("Add Book");
			mntmAdd.addSelectionListener(actionAddBook(parent));

		}

	}

	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Id", "Title", "Author", "Library" };
		int[] bounds = { 25, 150, 120, 150 };

		createTableViewerColumn(titles[0], bounds[0], 0);
		createTableViewerColumn(titles[1], bounds[1], 1);
		createTableViewerColumn(titles[2], bounds[2], 2);
		createTableViewerColumn(titles[3], bounds[3], 3);
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(tableBooks, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	@Override
	public void setFocus() {
		tableBooks.getControl().setFocus();

	}

	private SelectionAdapter actionAddBook(Composite parent) {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell parentShell = parent.getShell();
				AddBook a = new AddBook(parentShell);
				a.open();

				try {
					List<BookVO> books = new ArrayList<>();
					try {
						books = findBooksService.sendGet(prefix);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					input.clear();
					input.addAll(books);

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		};
	}

	private SelectionAdapter actionDelate() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!tableBooks.getSelection().isEmpty()) {
					IStructuredSelection selection = tableBooks.getStructuredSelection();
					BookVO p = (BookVO) selection.getFirstElement();
					String elementRemove = p.getId().toString();
					String titleElement = p.getTitle();

					try {
						removeBookService.removeBook(elementRemove);
						List<BookVO> books = new ArrayList<>();
						try {
							books = findBooksService.sendGet(prefix);
						} catch (Exception e1) {
							e1.printStackTrace();
						}

						input.clear();
						input.addAll(books);

						MessageDialog.openInformation(null, "Dalete book success!",
								"Book index: " + elementRemove + " Title: " + titleElement + " was deleted");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		};
	}

}