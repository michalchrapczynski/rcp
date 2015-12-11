package com.capgemini.library.ui.views;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AddAuthor extends Dialog {
	private Text addFirstName;
	private Text addLastName;
	private String firstName;
	private String lastName;
	private DataBindingContext ctx = new DataBindingContext();
	private Boolean statusOK = false;

	public AddAuthor(Shell parent) {
		super(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		Label lblTitle = new Label(container, SWT.NONE);
		lblTitle.setText("First Name");

		addFirstName = new Text(container, SWT.BORDER);
		addFirstName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblLibrary = new Label(container, SWT.NONE);
		lblLibrary.setText("Last Name");

		addLastName = new Text(container, SWT.BORDER);
		addLastName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		WritableValue value = new WritableValue();

		IObservableValue widgetValue = WidgetProperties.text(SWT.Modify).observe(addFirstName);
		IObservableValue modelValue = BeanProperties.value("firstName").observeDetail(value);

		IValidator validator = new IValidator() {
			@Override
			public IStatus validate(Object value) {
				if (value != null && value.toString() != "") {
					statusOK = true;
					return ValidationStatus.ok();
				}
				statusOK = false;
				return ValidationStatus.error("First Name must have at least one character");
			}
		};

		UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setBeforeSetValidator(validator);

		AggregateValidationStatus agg = new AggregateValidationStatus(ctx, AggregateValidationStatus.MAX_SEVERITY);
		agg.addValueChangeListener(new IValueChangeListener() {

			@Override
			public void handleValueChange(ValueChangeEvent event) {
				Status status = (Status) event.getObservableValue().getValue();

				if (status.isOK()) {
					System.out.println("yes, it is fine");

				}

			}
		});

		Binding bindValue = ctx.bindValue(widgetValue, modelValue, strategy, null);
		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);

		return container;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Selection dialog");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(300, 270);
	}

	@Override
	protected void okPressed() {
		firstName = addFirstName.getText();
		lastName = addLastName.getText();
		super.okPressed();
	}

	@Override
	public void create() {
		super.create();
		Button buttonOk = getButton(OK);
		buttonOk.setEnabled(false);

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
