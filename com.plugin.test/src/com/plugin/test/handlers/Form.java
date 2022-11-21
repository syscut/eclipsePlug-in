package com.plugin.test.handlers;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class Form extends Dialog {
	private Shell shell;

	protected Form(Shell parentShell) {
		super(parentShell);
		this.shell = parentShell;
	}

	private Text tName;
	private Button checkBoxLocation;
	private Label lblLocation;
	private Text tLocation;
	private Button buttonLocation;
	private Text tGroup;
	private Text tArtifact;
	private Text tVersion;
	private Text tDiscription;
	private Text tPackageName;
	private Text tDataBase;
	private Text tUser;
	private Text tPassword;
	private Text tPort;
	private String name = "";
	private String location = "";
	private String group = "";
	private String artifact = "";
	private String version = "";
	private String discription = "";
	private String packageName = "";
	private String dataBase = "";
	private String user = "";
	private String password = "";
	private String port = "";

	@Override
	protected void configureShell(Shell parent) {
		super.configureShell(parent);

		ImageRegistry ir = new ImageRegistry();
		ir.put("gfc", AbstractUIPlugin.imageDescriptorFromPlugin("com.plugin.test", "icons/Sample.png"));
		parent.setImage(ir.get("gfc"));
		parent.setText("GFC Spring Boot initializer V0.0.1");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(3, false);
		layout.marginRight = 10;
		layout.marginLeft = 10;
		layout.verticalSpacing = 15;
		container.setLayout(layout);

		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, false, false));
		lblName.setText("Name:");

		tName = new Text(container, SWT.BORDER);
		tName.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false, 2, 1));
		tName.setText(name);
		tName.addModifyListener(e -> {
			Text textWidget = (Text) e.getSource();
			String userText = textWidget.getText();
			name = userText;
		});

		checkBoxLocation = new Button(container, SWT.CHECK);
		checkBoxLocation.setText("Use Work Space");
		checkBoxLocation.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false, 3, 1));
		checkBoxLocation.setSelection(true);
		checkBoxLocation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setLocationStatus(!((Button) e.getSource()).getSelection());
			}
		});

		lblLocation = new Label(container, SWT.NONE);
		lblLocation.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, false, false));
		lblLocation.setText("Location:");

		tLocation = new Text(container, SWT.BORDER);
		tLocation.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		tLocation.setText(System.getProperty("user.dir"));
		tLocation.addModifyListener(e -> {
			Text textWidget = (Text) e.getSource();
			String userText = textWidget.getText();
			location = userText;
		});

		buttonLocation = new Button(container, SWT.PUSH);
		buttonLocation.setText("Browse");
		buttonLocation.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, false, false));
		buttonLocation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = new DirectoryDialog(shell).open();
				if (path != null) {
					tLocation.setText(path);
				}
			}
		});

		setLocationStatus(false);

		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, false, false));
		lblPassword.setText("Password:");

		tPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		tPassword.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		tPassword.setText(password);
		tPassword.addModifyListener(e -> {
			Text textWidget = (Text) e.getSource();
			String passwordText = textWidget.getText();
			password = passwordText;
		});
		return container;
	}

	// override method to use "Login" as label for the OK button
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Login", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 600);
	}

	@Override
	protected void okPressed() {
		name = tName.getText();
		password = tPassword.getText();
		super.okPressed();
	}

	protected void setLocationStatus(Boolean status) {
		lblLocation.setEnabled(status);
		tLocation.setEnabled(status);
		buttonLocation.setEnabled(status);
	}

	public String getUser() {
		return name;
	}

	public void setUser(String user) {
		this.name = user;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getArtifact() {
		return artifact;
	}

	public void setArtifact(String artifact) {
		this.artifact = artifact;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
