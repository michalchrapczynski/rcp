package com.capgemini.library.logic.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class BookVO {

	private Long id;
	private String title;
	private List<AuthorVO> authors;
	private LibraryVO libraryName;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public BookVO() {
		super();
	}

	public BookVO(Long id, String title, List<AuthorVO> authors, LibraryVO libraryName) {
		super();
		this.id = id;
		this.title = title;
		this.authors = authors;
		this.libraryName = libraryName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<AuthorVO> getAuthors() {
		return authors;
	}

	public void setAuthors(List<AuthorVO> authors) {
		this.authors = authors;
	}

	public LibraryVO getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(LibraryVO libraryName) {
		this.libraryName = libraryName;
	}

	@Override
	public String toString() {
		return "BookVO [id=" + id + ", title=" + title + ", authors=" + authors + ", libraryName=" + libraryName + "]";
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
}
