package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.table.TableColumnModel;

import com.threemc.controller.ControllerForEmployeeDetails;
import com.threemc.data.Employee;
import com.threemc.data.Position;
import com.threemc.data.User;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

public class Employees extends Dialog {

	// TODO
	private JTabbedPane tabPane;

	private JPanel panelTitle;
	private JPanel panelCenter;
	private JPanel panelTabForm;
	private JPanel panelTabList;
	private JPanel panelTable;
	private JPanel panelButton;
	private JPanel panelAccountForm;
	private JPanel panelTableAcc;
	private JPanel panelButtonAcc;
	private JPanel panelPosition;
	private JPanel panelPosTable;
	private JPanel panelPosButton;

	private JLabel lblTitle;
	private JLabel lblfirstname;
	private JLabel lblmiddlename;
	private JLabel lbllastname;
	private JLabel lbladdress;
	private JLabel lblcontactno;
	private JLabel lbldob;
	private JLabel lbldoe;
	private JLabel lblgender;
	private JLabel lblposition;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblUsertype;
	private JLabel lblEmp;
	private JLabel lblPosname;
	private JLabel lblPosdesc;

	private JTextField txtFirstname;
	private JTextField txtMiddlename;
	private JTextField txtLastname;
	private JTextField txtAddress;
	private JTextField txtContactno;
	private JTextField txtUsername;
	private JTextField txtPassword;
	private JTextField txtPosname;
	private JTextField txtPosdesc;

	private JDateChooser dpDateOfBirth;
	private JDateChooser dpDateOfEmployment;

	private JComboBox<String> cboGender;
	private JComboBox<String> cboPos;
	private JComboBox<String> cboUsertype;
	private JComboBox<String> cboEmployee;

	private JButton btnSave;
	private JButton btnSaveAccount;
	private JButton btnClear;
	private JButton btnNew;
	private JButton btnSavePos;
	private JButton btnClearPos;
	private JButton btnDelPos;

	private int id = 0;
	private int posid = 0;

	private DateFormat dateFormat;
	private DateFormat format;

	private Font f = CustomFont.setFont("Tahoma", Font.PLAIN, 15);

	CustomIcon ci = new CustomIcon();

	private ControllerForEmployeeDetails controller;
	private ArrayList<Employee> employeeList;
	private ArrayList<Employee> employeeListWacc;
	private ArrayList<Position> positionList;
	private ArrayList<User> userList;

	private JTable table;
	private EmployeeTableModel tableModel;

	private JTable tableAcc;
	private UserTableModel tableModelUser;

	private JTable tablePos;
	private EmployeePosTableModel tableModelPos;

	private Timer timer;

	// TODO
	public Employees(final Window parent, Dialog.ModalityType modal) {
		super(parent, "Profiling", modal);
		set(parent);
		initUI();
		layoutComponents();
		loadFirst();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				Employees.this.dispose();
			}
		});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (controller.connect().equals("ok")) {
						controller.addEmployee(getEmployeeEventForm());
						controller.saveEmployee();
						clearText();
						btnSave.setEnabled(false);
						btnNew.setEnabled(true);
						disableComp();
						JOptionPane.showMessageDialog(Employees.this,
								"Successfully saved Employee's Information",
								"Profiling", JOptionPane.INFORMATION_MESSAGE);
						tabPane.setSelectedIndex(1);
						loadFirst();
						refreshEmployeeList();
					} else {
						System.out.println(controller.connect());
					}
				} catch (NullPointerException e) {
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnSaveAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (employeeListWacc.size() != 0) {
					System.out.println(employeeListWacc.size());
					int employee_id = employeeListWacc.get(
							cboEmployee.getSelectedIndex()).getId();
					String username = txtUsername.getText().trim();
					String password = txtPassword.getText().trim();
					String type = cboUsertype.getSelectedItem().toString()
							.trim();
					Date date = new Date(System.currentTimeMillis());
					String dates = format.format(date);
					if (!username.isEmpty()) {
						if (CustomPatternChecker
								.checkStringLettersWithNumbers(username)) {
							if (!password.isEmpty()) {
								if (CustomPatternChecker
										.checkStringLettersWithNumbers(password)) {
									User user = new User(0, employee_id,
											username, password, dates, type,
											"Inactive");
									try {
										if (controller.connect().equals("ok")) {
											controller.saveUser(user);
											txtUsername.setText("");
											txtPassword.setText("");
											refreshEmployeeList();
											loadUserAccounts();
											JOptionPane
													.showMessageDialog(
															Employees.this,
															"Account Added",
															"Employees",
															JOptionPane.INFORMATION_MESSAGE);
										} else {
											System.out.println(controller
													.connect());
										}
									} catch (Exception e1) {
										e1.printStackTrace();
									}
								} else {
									JOptionPane.showMessageDialog(
											Employees.this,
											"Aplhanumeric Characters only",
											"Employees",
											JOptionPane.ERROR_MESSAGE);
								}
							}
						} else {
							JOptionPane.showMessageDialog(Employees.this,
									"Aplhanumeric Characters only",
									"Employees", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});

		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearText();
			}
		});

		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enableComp();
				btnSave.setEnabled(true);
			}
		});

		btnSavePos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (controller.connect().equals("ok")) {
						if (posid != 10000) {
							String posname = txtPosname.getText();
							String posdesc = txtPosdesc.getText();
							Position pos = new Position(posid, posname, posdesc);
							controller.savePosition(pos);
							loadPosition();
							JOptionPane.showMessageDialog(Employees.this,
									"Save Position successfully",
									"Employees - Position",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane
									.showMessageDialog(
											Employees.this,
											"Default Records can't be updated or deleted.",
											"Employees - Position",
											JOptionPane.WARNING_MESSAGE);
						}
					} else {
						System.out.println(controller.connect());
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		btnClearPos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtPosname.setText("");
				txtPosdesc.setText("");
				posid = 0;
			}
		});

		btnDelPos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (controller.connect().equals("ok")) {
						int row = tablePos.getSelectedRow();
						if (row != -1) {
							Position pos = positionList.get(row);
							if(pos.getId() != 10000) {
								int okcancel = JOptionPane
										.showConfirmDialog(
												Employees.this,
												"Are you sure you want to delete \"" +pos.getPositionName() +"\" in the List? \n\nYes or no?",
												"Employees",
												JOptionPane.YES_NO_OPTION,
												JOptionPane.WARNING_MESSAGE);
								if(okcancel == JOptionPane.YES_OPTION) {
									System.out.println("del");
									controller.updateBeforeDelete(pos.getId());
									controller.deletePositionInfo(pos.getId());
									loadFirst();
									loadPosition();
									loadUserAccounts();
								}
							} else {
								JOptionPane
								.showMessageDialog(
										Employees.this,
										"Default Records can't be updated or deleted.",
										"Employees - Position",
										JOptionPane.WARNING_MESSAGE);
							}
						}
					} else {
						System.out.println(controller.connect());
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		txtContactno.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				super.keyPressed(arg0);
				if (txtContactno.getText().length() >= 12) {
					txtContactno.setText(txtContactno.getText()
							.substring(0, 11));
				}
			}
		});

		cboPos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				posid = positionList.get(cboPos.getSelectedIndex()).getId();
			}
		});

		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (e.getClickCount() == 2) {
					tableToTextboxes();
				}
			}
		});

		tablePos.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int row = tablePos.getSelectedRow();
				if (row != -1) {
					txtPosname.setText(positionList.get(row).getPositionName());
					txtPosdesc.setText(positionList.get(row).getPositionDesc());
					posid = positionList.get(row).getId();
				}
			}
		});
	}

	// TODO
	private void tableToTextboxes() {
		int row = table.getSelectedRow();
		Employee emp = employeeList.get(row);
		id = emp.getId();
		txtFirstname.setText(emp.getEmpFirstName());
		txtMiddlename.setText(emp.getEmpMiddleName());
		txtLastname.setText(emp.getEmpLastName());
		txtAddress.setText(emp.getEmpAddress());
		txtContactno.setText(emp.getEmpContactno());
		if (emp.getEmpGender().equals("male")) {
			cboGender.setSelectedIndex(0);
		} else if (emp.getEmpGender().equals("female")) {
			cboGender.setSelectedIndex(1);
		}
		cboPos.setSelectedItem(emp.getEmpPosition());

		try {
			Date d = dateFormat.parse(emp.getEmpDateOfBirth());
			dpDateOfBirth.setDate(d);
		} catch (ParseException eh) {
			eh.printStackTrace();
		}

		try {
			Date d = dateFormat.parse(emp.getEmpDateOfEmployment());
			dpDateOfEmployment.setDate(d);
		} catch (ParseException es) {
			es.printStackTrace();
		}
		enableComp();
		tabPane.setSelectedIndex(0);
	}

	private void refreshEmployeeList() {
		DefaultComboBoxModel<String> jj = new DefaultComboBoxModel<String>();
		try {
			if (controller.connect().equals("ok")) {
				controller.loaddAllEmployeesWithoutAccount();
				employeeListWacc = controller.getEmpcc();
				if (employeeListWacc.size() != -1) {
					for (Employee emp : employeeListWacc) {
						jj.addElement(emp.getEmpLastName() + " , "
								+ emp.getEmpFirstName() + " "
								+ emp.getEmpMiddleName());
					}
				}
				cboEmployee.setModel(jj);
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void loadUserAccounts() {
		try {
			if (controller.connect().equals("ok")) {
				controller.loadAllUsers();
				userList = controller.getUsers();
				setDataUser(userList);
				refreshAcc();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadPosition() {
		try {
			if (controller.connect().equals("ok")) {
				DefaultComboBoxModel<String> ss = new DefaultComboBoxModel<String>();
				controller.loadPosition();
				positionList = controller.getPosition();
				setDataPos(positionList);
				refreshPos();
				if (positionList != null) {
					for (Position position : positionList) {
						ss.addElement(position.getPositionName());
					}
					cboPos.setModel(ss);
					cboPos.setSelectedIndex(0);
				}
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private EmployeeEventForm getEmployeeEventForm() {
		EmployeeEventForm emp = null;

		String fname = txtFirstname.getText().toString();
		String mname = txtMiddlename.getText().toString();
		String lname = txtLastname.getText().toString();
		String address = txtAddress.getText().toString();
		String contact = txtContactno.getText().toString();
		String gender = cboGender.getSelectedItem().toString();
		String dob = dateFormat.format(dpDateOfBirth.getDate());
		String doe = dateFormat.format(dpDateOfEmployment.getDate());
		String position = cboPos.getSelectedItem().toString();
		int posid = positionList.get(cboPos.getSelectedIndex()).getId();

		if (!fname.isEmpty()
				&& CustomPatternChecker.checkStringSomeCharsAllowed(fname)) {
			if (!fname.isEmpty()
					&& CustomPatternChecker.checkStringSomeCharsAllowed(fname)) {
				if (!fname.isEmpty()
						&& CustomPatternChecker
								.checkStringSomeCharsAllowed(fname)) {
					if (!fname.isEmpty()
							&& CustomPatternChecker
									.checkStringSomeCharsAllowed(fname)) {
						if (!fname.isEmpty()
								&& CustomPatternChecker
										.checkStringSomeCharsAllowed(fname)) {
							emp = new EmployeeEventForm(this, id, fname, mname,
									lname, dob, doe, position, address,
									contact, gender);
							emp.setEmpPosId(posid);
						}
					}
				}
			}
		}
		return emp;
	}

	private void clearText() {
		id = 0;
		txtFirstname.setText("");
		txtMiddlename.setText("");
		txtLastname.setText("");
		txtAddress.setText("");
		txtContactno.setText("");

		Date date = new Date(System.currentTimeMillis());
		dpDateOfBirth.setDate(date);
		dpDateOfEmployment.setDate(date);
	}

	private void disableComp() {
		txtFirstname.setEnabled(false);
		txtMiddlename.setEnabled(false);
		txtLastname.setEnabled(false);
		txtAddress.setEnabled(false);
		txtContactno.setEnabled(false);
		cboGender.setEnabled(false);
		cboPos.setEnabled(false);
		dpDateOfBirth.setEnabled(false);
		dpDateOfEmployment.setEnabled(false);
	}

	private void enableComp() {
		txtFirstname.setEnabled(true);
		txtMiddlename.setEnabled(true);
		txtLastname.setEnabled(true);
		txtAddress.setEnabled(true);
		txtContactno.setEnabled(true);
		cboGender.setEnabled(true);
		cboPos.setEnabled(true);
		dpDateOfBirth.setEnabled(true);
		dpDateOfEmployment.setEnabled(true);
		btnSave.setEnabled(true);
		btnNew.setEnabled(false);
	}

	private void refresh() {
		tableModel.fireTableDataChanged();
	}

	private void setData(ArrayList<Employee> db) {
		tableModel.setData(db);
	}

	private void refreshAcc() {
		tableModelUser.fireTableDataChanged();
	}

	private void setDataUser(ArrayList<User> db) {
		tableModelUser.setData(db);
	}

	private void refreshPos() {
		tableModelPos.fireTableDataChanged();
	}

	private void setDataPos(ArrayList<Position> db) {
		tableModelPos.setData(db);
	}

	private void loadFirst() {
		try {
			if (controller.connect().equals("ok")) {
				controller.loadAllEmployees();
				employeeList = controller.getEmployee();
				setData(employeeList);
				refresh();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkIfSomeoneLogged() {
		try {
			if (controller.connect().equals("ok")) {
				if (controller.getIfUsersLogged() > 0) {
					controller.updateIfUserIsLogged();
					loadUserAccounts();
				}
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO
	private void layoutComponents() {
		panelTitle.add(lblTitle);

		add(panelTitle, BorderLayout.NORTH);

		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(3, 5, 3, 5);

		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = inset;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;

		gc.gridy = 0;
		gc.gridx = 0;

		panelTabForm.add(lblfirstname, gc);

		gc.gridx = 1;

		panelTabForm.add(lblmiddlename, gc);

		gc.gridx = 2;

		panelTabForm.add(lbllastname, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelTabForm.add(txtFirstname, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		panelTabForm.add(txtMiddlename, gc);

		gc.gridx = 2;

		panelTabForm.add(txtLastname, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		panelTabForm.add(lbladdress, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 3;
		panelTabForm.add(txtAddress, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 1;
		panelTabForm.add(lblcontactno, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelTabForm.add(txtContactno, gc);

		gc.gridy++;
		gc.gridx = 0;
		panelTabForm.add(lblgender, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		panelTabForm.add(cboGender, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;
		panelTabForm.add(lbldob, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelTabForm.add(dpDateOfBirth, gc);

		gc.gridy++;
		gc.gridx = 0;
		panelTabForm.add(lbldoe, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelTabForm.add(dpDateOfEmployment, gc);

		gc.gridy++;
		gc.gridx = 0;
		panelTabForm.add(lblposition, gc);

		gc.gridy++;
		gc.gridx = 0;
		panelTabForm.add(cboPos, gc);

		panelButton.add(btnNew);
		panelButton.add(btnSave);
		panelButton.add(btnClear);

		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 1;
		panelTabForm.add(panelButton, gc);

		panelTable
				.add(new JScrollPane(table,
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
						BorderLayout.NORTH);
		panelTabList.add(panelTable, BorderLayout.NORTH);

		// ---------------------------

		GridBagConstraints gx = new GridBagConstraints();

		gx.weightx = 1;
		gx.weighty = 0;
		gx.fill = GridBagConstraints.HORIZONTAL;
		gx.insets = inset;

		gx.gridy = 0;
		gx.gridx = 0;

		panelAccountForm.add(lblEmp, gx);

		gx.gridy++;
		gx.gridx = 0;

		panelAccountForm.add(cboEmployee, gx);

		gx.gridy++;
		gx.gridx = 0;

		panelAccountForm.add(lblUsername, gx);

		gx.gridx = 1;

		panelAccountForm.add(lblPassword, gx);

		gx.gridx = 2;

		panelAccountForm.add(lblUsertype, gx);

		gx.gridy++;
		gx.gridx = 0;

		panelAccountForm.add(txtUsername, gx);

		gx.gridx = 1;

		panelAccountForm.add(txtPassword, gx);

		gx.gridx = 2;

		panelAccountForm.add(cboUsertype, gx);

		panelTableAcc.add(new JScrollPane(tableAcc,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

		gx.gridy++;
		gx.gridx = 0;
		gx.weightx = 1;
		gx.weighty = 1;
		gx.gridwidth = 3;
		gx.fill = GridBagConstraints.BOTH;

		panelAccountForm.add(panelTableAcc, gx);

		gx.gridy++;
		gx.gridx = 0;
		gx.weightx = 0;
		gx.fill = GridBagConstraints.NONE;

		gx.anchor = GridBagConstraints.FIRST_LINE_START;
		panelAccountForm.add(btnSaveAccount, gx);

		gx.anchor = GridBagConstraints.FIRST_LINE_START;

		// ---------------------------

		GridBagConstraints gp = new GridBagConstraints();

		gp.insets = inset;
		gp.weighty = 0;
		gp.weightx = 1;
		gp.fill = GridBagConstraints.HORIZONTAL;
		gp.anchor = GridBagConstraints.LINE_START;

		gp.gridy = 0;
		gp.gridx = 0;

		panelPosition.add(lblPosname, gp);

		gp.gridx++;

		panelPosition.add(lblPosdesc, gp);

		gp.gridy++;
		gp.gridx = 0;

		panelPosition.add(txtPosname, gp);

		gp.gridx++;

		panelPosition.add(txtPosdesc, gp);

		gp.gridy++;
		gp.gridx = 0;
		gp.weighty = 1;
		gp.gridwidth = 2;
		gp.fill = GridBagConstraints.BOTH;

		panelPosTable.add(new JScrollPane(tablePos,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);

		panelPosition.add(panelPosTable, gp);

		gp.gridy++;
		gp.gridx = 0;
		gp.fill = GridBagConstraints.NONE;
		gp.anchor = GridBagConstraints.FIRST_LINE_START;

		panelPosButton.add(btnSavePos);
		panelPosButton.add(btnClearPos);
		panelPosButton.add(btnDelPos);

		panelPosition.add(panelPosButton, gp);

		panelCenter.add(tabPane, BorderLayout.CENTER);
		add(panelCenter, BorderLayout.CENTER);
	}

	// TODO
	private void initUI() {
		controller = new ControllerForEmployeeDetails();

		panelTitle = new JPanel();
		panelTitle.setLayout(new GridBagLayout());
		panelTitle.setPreferredSize(CustomDimension.setDimensionHeight(100));

		lblTitle = new JLabel("sad");
		lblTitle.setIcon(ci.createIcon("/res/employeetop.png"));

		panelTabForm = new JPanel();
		panelTabForm.setLayout(new GridBagLayout());
		panelTabForm.setBackground(CustomColor.bgColor());
		panelTabForm.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		panelTabList = new JPanel();
		panelTabList.setLayout(new BorderLayout());
		panelTabList.setBackground(CustomColor.bgColor());
		panelTabList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		panelAccountForm = new JPanel();
		panelAccountForm.setLayout(new GridBagLayout());
		panelAccountForm.setBackground(CustomColor.bgColor());
		panelAccountForm.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		panelButtonAcc = new JPanel();
		panelButtonAcc.setLayout(new GridBagLayout());
		panelButtonAcc.setBackground(CustomColor.bgColor());
		panelButtonAcc.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		panelPosition = new JPanel();
		panelPosition.setLayout(new GridBagLayout());
		panelPosition.setBackground(CustomColor.bgColor());
		panelPosition.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		panelCenter = new JPanel();
		panelCenter.setLayout(new BorderLayout());
		panelCenter.setBorder(BorderFactory.createEtchedBorder());

		panelTable = new JPanel();
		panelTable.setLayout(new BorderLayout());
		panelTable.setBackground(CustomColor.bgColor());

		panelTableAcc = new JPanel();
		panelTableAcc.setLayout(new BorderLayout());
		panelTableAcc.setBackground(CustomColor.bgColor());

		panelPosTable = new JPanel();
		panelPosTable.setLayout(new BorderLayout());
		panelPosTable.setBackground(CustomColor.bgColor());

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButton.setBackground(CustomColor.bgColor());
		panelButton.setBorder(BorderFactory.createEtchedBorder());

		panelPosButton = new JPanel();
		panelPosButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelPosButton.setBackground(CustomColor.bgColor());
		panelPosButton.setBorder(BorderFactory.createEtchedBorder());

		tabPane = new JTabbedPane();
		tabPane.setFont(new Font("Tahoma", Font.BOLD, 16));
		tabPane.add("Employee Record Form", panelTabForm);
		tabPane.add("Employee Record List", panelTabList);
		tabPane.addTab("Log in Account", panelAccountForm);
		tabPane.addTab("Position Record List", panelPosition);

		lblfirstname = new JLabel("First name: ");
		lblmiddlename = new JLabel("Middle name: ");
		lbllastname = new JLabel("Last name: ");
		lbladdress = new JLabel("Address: ");
		lblcontactno = new JLabel("Contact number: ");
		lbldob = new JLabel("Date of Birth: ");
		lbldoe = new JLabel("Date of Employment: ");
		lblgender = new JLabel("Gender: ");
		lblposition = new JLabel("Position: ");
		lblUsername = new JLabel("Username: ");
		lblPassword = new JLabel("Password: ");
		lblUsertype = new JLabel("User Type: ");
		lblEmp = new JLabel("Employees w/o Account");
		lblPosname = new JLabel("Position Name: ");
		lblPosdesc = new JLabel("Position Description: ");

		lblfirstname.setFont(f);
		lblmiddlename.setFont(f);
		lbllastname.setFont(f);
		lbladdress.setFont(f);
		lblcontactno.setFont(f);
		lbldob.setFont(f);
		lbldoe.setFont(f);
		lblgender.setFont(f);
		lblposition.setFont(f);
		lblUsername.setFont(f);
		lblPassword.setFont(f);
		lblUsertype.setFont(f);
		lblEmp.setFont(f);
		lblPosname.setFont(f);
		lblPosdesc.setFont(f);

		txtFirstname = new JTextField(20);
		txtMiddlename = new JTextField(20);
		txtLastname = new JTextField(20);
		txtAddress = new JTextField(20);
		txtContactno = new JTextField(20);
		txtUsername = new JTextField(20);
		txtPassword = new JTextField(20);
		txtPosdesc = new JTextField(20);
		txtPosname = new JTextField(20);

		txtFirstname.setFont(f);
		txtMiddlename.setFont(f);
		txtLastname.setFont(f);
		txtAddress.setFont(f);
		txtContactno.setFont(f);
		txtUsername.setFont(f);
		txtPassword.setFont(f);
		txtPosdesc.setFont(f);
		txtPosname.setFont(f);

		btnClear = new JButton("Clear", ci.createIcon("/res/clear.png"));
		btnClearPos = new JButton("Clear", ci.createIcon("/res/clear.png"));
		btnNew = new JButton("New", ci.createIcon("/res/add_client.png"));
		btnSave = new JButton("Save", ci.createIcon("/res/save.png"));
		btnSaveAccount = new JButton("Save", ci.createIcon("/res/save.png"));
		btnSavePos = new JButton("Save", ci.createIcon("/res/save.png"));
		btnDelPos = new JButton("Delete", ci.createIcon("/res/delete.png"));
		btnSave.setEnabled(false);

		dateFormat = new SimpleDateFormat(" MMMMM dd , yyyy - EEEEE");
		format = new SimpleDateFormat("MMMMM/dd/yyyy");

		Dimension d = getPreferredSize();
		d.width = 470;
		d.height = 420;

		JCalendar cal = new JCalendar();
		cal.setDecorationBordersVisible(true);
		cal.setDecorationBackgroundColor(Color.WHITE);
		cal.setTodayButtonVisible(true);
		cal.setSundayForeground(Color.RED);
		cal.setPreferredSize(d);

		dpDateOfBirth = new JDateChooser(cal, null, null, null);
		dpDateOfBirth.setDate(Calendar.getInstance().getTime());
		dpDateOfBirth.setDateFormatString(" MMMMM dd , yyyy - EEEEE");
		dpDateOfBirth.setBackground(CustomColor.bgColor());
		dpDateOfBirth.setFont(f);

		JCalendar cali = new JCalendar();
		cali.setDecorationBordersVisible(true);
		cali.setDecorationBackgroundColor(Color.WHITE);
		cali.setTodayButtonVisible(true);
		cali.setSundayForeground(Color.RED);
		cali.setPreferredSize(d);

		dpDateOfEmployment = new JDateChooser(cali, null, null, null);
		dpDateOfEmployment.setDate(Calendar.getInstance().getTime());
		dpDateOfEmployment.setDateFormatString(" MMMMM dd , yyyy - EEEEE");
		dpDateOfEmployment.setBackground(CustomColor.bgColor());
		dpDateOfEmployment.setFont(f);

		cboGender = new JComboBox<String>();
		cboGender.setFont(f);
		DefaultComboBoxModel<String> aa = new DefaultComboBoxModel<String>();
		aa.addElement("Male");
		aa.addElement("Female");
		cboGender.setModel(aa);

		cboUsertype = new JComboBox<String>();
		cboUsertype.setFont(f);
		DefaultComboBoxModel<String> lss = new DefaultComboBoxModel<String>();
		lss.addElement("user");
		lss.addElement("manager");
		cboUsertype.setModel(lss);

		cboEmployee = new JComboBox<String>();
		cboEmployee.setFont(f);
		refreshEmployeeList();
		cboPos = new JComboBox<String>();
		cboPos.setFont(f);

		disableComp();

		tableModel = new EmployeeTableModel();
		table = new JTable(tableModel);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		table.getTableHeader().setFont(f);
		table.setFont(f);
		table.setRowHeight(20);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(300);
		tcm.getColumn(1).setPreferredWidth(150);
		tcm.getColumn(2).setPreferredWidth(250);
		tcm.getColumn(3).setPreferredWidth(100);
		tcm.getColumn(4).setPreferredWidth(250);
		tcm.getColumn(5).setPreferredWidth(400);
		tcm.getColumn(6).setPreferredWidth(200);

		tableModelUser = new UserTableModel();
		tableAcc = new JTable(tableModelUser);

		tableAcc.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableAcc.getTableHeader().setReorderingAllowed(false);
		tableAcc.getTableHeader().setResizingAllowed(true);
		tableAcc.getTableHeader().setFont(f);
		tableAcc.setFont(f);
		tableAcc.setRowHeight(20);
		tableAcc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableColumnModel tcma = tableAcc.getColumnModel();
		tcma.getColumn(0).setPreferredWidth(200);
		tcma.getColumn(1).setPreferredWidth(200);
		tcma.getColumn(2).setPreferredWidth(200);
		tcma.getColumn(3).setPreferredWidth(200);

		tableModelPos = new EmployeePosTableModel();
		tablePos = new JTable(tableModelPos);

		tablePos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tablePos.getTableHeader().setReorderingAllowed(false);
		tablePos.getTableHeader().setResizingAllowed(true);
		tablePos.getTableHeader().setFont(f);
		tablePos.setFont(f);
		tablePos.setRowHeight(20);
		tablePos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		loadUserAccounts();

		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkIfSomeoneLogged();
			}
		});

		timer.start();
		loadPosition();
	}

	// TODO
	private void set(final Window parent) {
		setLayout(new BorderLayout());
		setSize(800, 700);
		setLocationRelativeTo(parent);
		setResizable(false);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png"))
				.getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
	}
}
