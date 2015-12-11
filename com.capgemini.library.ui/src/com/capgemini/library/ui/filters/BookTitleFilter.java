package com.capgemini.library.ui.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.capgemini.library.logic.model.BookVO;

public class BookTitleFilter extends ViewerFilter {

	private String prefix;

	public BookTitleFilter(String prefix) {
		this.prefix = ".*" + prefix + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (prefix == null || prefix.length() == 0) {
			return true;
		}
		BookVO p = (BookVO) element;
		if (p.getTitle().matches(prefix)) {
			return true;
		}

		return false;
	}

	public void setPrefix(String prefix) {
		this.prefix = ".*" + prefix + ".*";
	}

}
