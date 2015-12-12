package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;

import net.sf.dynamicreports.report.exception.DRException;

import com.threemc.controller.ControllerForClients;
import com.threemc.controller.ControllerForAdmin;
import com.threemc.data.Admin;
import com.threemc.data.HomeData;
import com.threemc.events.AdminAccountListener;
import com.threemc.events.MainFrameCenterPanelEventListener;
import com.threemc.model.CheckAndCreateDatabase;

public class MainSystemInterface extends JFrame {
	// Date Started: 06/26/2015
	private MainFrameTopPanelButtons panelTopCustom;
	private MainFrameWestLogoPanel panelWestLogo;
	private MainFrameBottomPanelStatus panelBottom;
	private MainFrameCenterPanel panelCenter;

	private BookingsList bookl;
	private AdminAccount ad;
	private Employees prof;
	private Clients client;
	private Bookings book;
	private Packages pack;
	private Payments pay;
	private Outputs out;
	private Reports rep;
	private Logs log;

	private ProgressbarDialog prog;

	private ControllerForClients controllerc;
	private ControllerForAdmin controlleru;
	private Login login;
	private Admin user;
	private Timer timer;

	// TODO
	public MainSystemInterface() {
		super("Three McQueens Eventi Automated System");
		setLayout(new BorderLayout());

		System.out.println(CheckAndCreateDatabase.checkDatabaseifExisting());

		prog = new ProgressbarDialog(MainSystemInterface.this, ModalityType.APPLICATION_MODAL);

		initUILookAndFeel();
		set();
		initVal();
		layoutComponents();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					if (user != null) {
						if (user.getAdminUserStatus().equals("Active")) {
							if(controlleru.connect().equals("ok")) {
								controlleru.updateUserStatus(user.getId(),"Inactive");
							} else {
								System.out.println(controlleru.connect());
							}
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		panelTopCustom.setListenerButtonTopPanel(new ListenerButtonTopPanel() {
			public void openReportsAction() {
				prog.setIndeterminate(true);
				prog.setVisible(true);
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						try {
							rep = new Reports(MainSystemInterface.this,
									Dialog.ModalityType.MODELESS);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (DRException e) {
							e.printStackTrace();
						}
						return null;
					}
					protected void done() {
						prog.dispose();
						rep.setVisible(true);
					}
				};
				worker.execute();
			}

			public void openPaymentAction() {
				prog.setIndeterminate(true);
				prog.setVisible(true);
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						pay = new Payments(MainSystemInterface.this, Dialog.ModalityType.APPLICATION_MODAL);
						return null;
					}
					
					protected void done() {
						prog.dispose();
						pay.setVisible(true);
					}
				};
				worker.execute();
				
			}

			public void openPackagesAction() {
				prog.setIndeterminate(true);
				prog.setVisible(true);
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						pack = new Packages(MainSystemInterface.this, Dialog.ModalityType.APPLICATION_MODAL);
						return null;
					}
					
					protected void done() {
						prog.dispose();
						pack.setVisible(true);
					}
				};
				worker.execute();
			}

			public void openOutputsAction() {
				prog.setIndeterminate(true);
				prog.setVisible(true);
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						out = new Outputs(MainSystemInterface.this, Dialog.ModalityType.APPLICATION_MODAL);
						return null;
					}
					
					protected void done() {
						prog.dispose();
						out.setVisible(true);
					}
				};
				worker.execute();
			}

			public void openLogsAction() {
				log = new Logs(MainSystemInterface.this,
						Dialog.ModalityType.APPLICATION_MODAL);
				log.setVisible(true);	
			}

			public void openClientAction() {
				prog.setIndeterminate(true);
				prog.setVisible(true);
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						client = new Clients(MainSystemInterface.this, Dialog.ModalityType.APPLICATION_MODAL);
						return null;
					}
					
					protected void done() {
						prog.dispose();
						client.setVisible(true);
					}
				};
				worker.execute();
			}

			public void openCalculatorAction() {
				System.out.println("Calc");
			}

			public void openBookingAction() {
				prog.setIndeterminate(true);
				prog.setVisible(true);
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						book = new Bookings(MainSystemInterface.this,
								Dialog.ModalityType.APPLICATION_MODAL);
						return null;
					}
					protected void done() {
						prog.dispose();
						try {
							book.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				worker.execute();
			}

			public void openBookingListAction() {
				prog.setIndeterminate(true);
				prog.setVisible(true);
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						bookl = new BookingsList(MainSystemInterface.this, Dialog.ModalityType.APPLICATION_MODAL);
						return null;
					}
					
					protected void done() {
						prog.dispose();
						bookl.setVisible(true);
					}
				};
				worker.execute();
			}

			public void openSettingAction() {
				ad = new AdminAccount(MainSystemInterface.this,
						Dialog.ModalityType.APPLICATION_MODAL);
				ad.setAdmin(user);
				ad.setAdminnAccountListener(new AdminAccountListener() {
					public void logOutAdmin() {
						ad.dispose();
						login();
					}
				});
				ad.setVisible(true);
			}

			public void openProfilingAction() {
				prog.setIndeterminate(true);
				prog.setVisible(true);
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					protected Void doInBackground() throws Exception {
						prof = new Employees(MainSystemInterface.this, Dialog.ModalityType.APPLICATION_MODAL);
						return null;
					}
					
					protected void done() {
						prog.dispose();
						prof.setVisible(true);
					}
				};
				worker.execute();
			}
		});

		panelCenter.setMainFrameCenterPanelEventListener(new MainFrameCenterPanelEventListener() {
			public void invokeEventListener(String msg) {
				panelBottom.setUpdates(msg);
			}

			public void openBookingDetailsAction(HomeData hd) {
				int client_id = hd.getClient_id();
				int event_id = hd.getEvent_id();
				BookingsShowList bl = new BookingsShowList(MainSystemInterface.this,
								Dialog.ModalityType.APPLICATION_MODAL);
				bl.setClientAndEventID(client_id, event_id);
				bl.setClientDetails();
				bl.setPaymentDetails();
				bl.setBookingDetails();
				bl.showData();
				bl.setVisible(true);
			}

			@Override
			public void invokeListClickListener() {
				panelBottom.setEastPanelClickedSeeAll();
				
			}
		});
	}

	public void login() {
		try {
			if (user != null) {
				if (user.getAdminUserStatus().equals("Active")) {
					controlleru.connect();
					controlleru.updateUserStatus(user.getId(), "Inactive");
					timer.stop();
				}
			}

			login = new Login(MainSystemInterface.this,
					Dialog.ModalityType.APPLICATION_MODAL);
			login.setVisible(true);
			user = login.getUser();
			login.dispose();
			
			panelBottom.setUserLoggedIn(user.getAdminUsername());

			timer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showIfEventisNow() {
		panelCenter.showIfEventisNow();
	}

	// TODO
	private void set() {
		setJMenuBar(createMenuBar());
//		setSize(1500, 800);
//		setLocationRelativeTo(null);
//		setMinimumSize(new Dimension(1500, 800));
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setExtendedState(MAXIMIZED_BOTH);
//		setVisible(true);
	}

	private JMenuBar createMenuBar() {
		// parent menu bar
		JMenuBar menuBar = new JMenuBar();

		// parent menu item
		JMenu mnFile = new JMenu("File");
		JMenu mnSetting = new JMenu("Settings");

		// child menu item
		JMenuItem mniExit = new JMenuItem("Exit");
		mniExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		JMenuItem mniDatabase = new JMenuItem("Databse Settings");

		// add item to file menu
		mnFile.add(mniExit);

		// add item to settings menu
		mnSetting.add(mniDatabase);

		menuBar.add(mnFile);
		menuBar.add(mnSetting);
		return menuBar;
	}

	// TODO
	private void initVal() {
		panelTopCustom = new MainFrameTopPanelButtons("BUTTONS", 40, 40);
		panelWestLogo = new MainFrameWestLogoPanel();
		panelBottom = new MainFrameBottomPanelStatus();
		panelCenter = new MainFrameCenterPanel();
		panelCenter.setParent(MainSystemInterface.this);
		
		controllerc = new ControllerForClients();
		controlleru = new ControllerForAdmin();
		
		timer = new Timer(1500, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(controlleru.connect().equals("ok")) {
						if (!controlleru.checkUserStatus(user.getId()).equals(
								"Active")) {
							JOptionPane
									.showMessageDialog(
											MainSystemInterface.this,
											"Your session has Expired! Please Log in again.",
											"Session Expired",
											JOptionPane.ERROR_MESSAGE);
							login();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// TODO
	private void layoutComponents() {
		add(panelCenter, BorderLayout.CENTER);
		add(panelTopCustom, BorderLayout.NORTH);
		add(panelWestLogo, BorderLayout.WEST);
		add(panelBottom, BorderLayout.SOUTH);
	}

	// TODO
	private void initUILookAndFeel() {
		// UI Look and Feel
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (Exception e) {

		}
	}
}
