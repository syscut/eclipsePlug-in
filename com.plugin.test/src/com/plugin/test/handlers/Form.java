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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.plugin.test.data.UserData;

public class Form extends Dialog {
	UserData userData = new UserData();
	private Shell shell;

	protected Form(Shell parentShell) {
		super(parentShell);
		this.shell = parentShell;
	}

	private Text tName;
	private Button checkBoxLocation;
	private Text tLocation;
	private Button buttonLocation;
	private Label lblLocation;
	private Text tGroup;
	private Text tArtifact;
	private Text tVersion;
	private Text tDiscription;
	private Text tPackageName;
	private Group connectionInfo;
	private Combo comboDataBase;
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
	private Integer dataBase = 0;
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
		GridLayout layout = new GridLayout(4, false);
		layout.marginRight = 10;
		layout.marginLeft = 10;
		layout.verticalSpacing = 15;
		container.setLayout(layout);

		new Label(container, SWT.NONE).setText("Name");
		tName = new Text(container, SWT.BORDER);
		tName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		tName.setText(userData.getName());
		tName.addModifyListener(e -> {
			name = ((Text) e.getSource()).getText();
		});

		// Location CheckBox
		checkBoxLocation = new Button(container, SWT.CHECK);
		checkBoxLocation.setText("Use Work Space");
		checkBoxLocation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		checkBoxLocation.setSelection(true);
		checkBoxLocation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setLocationStatus(!((Button) e.getSource()).getSelection());
			}
		});

		lblLocation = new Label(container, SWT.NONE);
		lblLocation.setText("Location");
		tLocation = new Text(container, SWT.BORDER);
		tLocation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		tLocation.setText(System.getProperty("user.dir"));
		tLocation.addModifyListener(e -> {
			location = ((Text) e.getSource()).getText();
		});

		buttonLocation = new Button(container, SWT.PUSH);
		buttonLocation.setText("Browse");
		buttonLocation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
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

		new Label(container, SWT.NONE).setText("Group");
		tGroup = new Text(container, SWT.BORDER);
		tGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		tGroup.setText(userData.getGroup());
		tGroup.addModifyListener(e -> {
			group = ((Text) e.getSource()).getText();
		});

		new Label(container, SWT.NONE).setText("Artifact");
		tArtifact = new Text(container, SWT.BORDER);
		tArtifact.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		tArtifact.setText(userData.getArtifact());
		tArtifact.addModifyListener(e -> {
			artifact = ((Text) e.getSource()).getText();
		});

		new Label(container, SWT.NONE).setText("Version");
		tVersion = new Text(container, SWT.BORDER);
		tVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		tVersion.setText(userData.getVersion());
		tVersion.addModifyListener(e -> {
			version = ((Text) e.getSource()).getText();
		});

		new Label(container, SWT.NONE).setText("Discription");
		tDiscription = new Text(container, SWT.BORDER);
		tDiscription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		tDiscription.setText(userData.getDiscription());
		tDiscription.addModifyListener(e -> {
			discription = ((Text) e.getSource()).getText();
		});

		new Label(container, SWT.NONE).setText("Package");
		tPackageName = new Text(container, SWT.BORDER);
		tPackageName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		tPackageName.setText(userData.getPackageName());
		tPackageName.addModifyListener(e -> {
			packageName = ((Text) e.getSource()).getText();
		});

// connectionInfo ---------------------------------------
		connectionInfo = new Group(container, SWT.NULL);
		connectionInfo.setText("Connection Information");
		connectionInfo.setLayout(new GridLayout(4, false));
		connectionInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		new Label(connectionInfo, SWT.NONE).setText("Database");

		comboDataBase = new Combo(connectionInfo, SWT.NULL);
		comboDataBase.setItems(new String[] { "192.6.3.1", "192.6.3.8", "192.6.3.1 & 192.6.3.8", "192.6.3.12" });
		comboDataBase.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboDataBase.select(userData.getDataBase());
		comboDataBase.addModifyListener(e -> {
			dataBase = ((Combo) e.getSource()).getSelectionIndex();
		});

		new Label(connectionInfo, SWT.NONE).setText("Port");
		tPort = new Text(connectionInfo, SWT.BORDER);
		tPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tPort.setText(userData.getPort());
		tPort.addModifyListener(e -> {
			port = ((Text) e.getSource()).getText();
		});

		new Label(connectionInfo, SWT.NONE).setText("User Name");
		tUser = new Text(connectionInfo, SWT.BORDER);
		tUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tUser.setText(userData.getUser());
		tUser.addModifyListener(e -> {
			user = ((Text) e.getSource()).getText();
		});

		new Label(connectionInfo, SWT.NONE).setText("Password:");
		tPassword = new Text(connectionInfo, SWT.BORDER | SWT.PASSWORD);
		tPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tPassword.setText(password);
		tPassword.addModifyListener(e -> {
			password = ((Text) e.getSource()).getText();
		});
		return container;
	}

	// override method to use "Login" as label for the OK button
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Generate", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 550);
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public Integer getDataBase() {
		return dataBase;
	}

	public void setDataBase(Integer dataBase) {
		this.dataBase = dataBase;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
