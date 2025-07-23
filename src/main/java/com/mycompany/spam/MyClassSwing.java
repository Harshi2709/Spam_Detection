package com.mycompany.spam;
import java.awt.*;

import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import java.awt.event.*;
import java.sql.*;

class FirstPg extends JFrame implements ActionListener, ItemListener {
	
	JLabel spammax;
	JLabel lblTitle, lbllogin, lblpass, lblfrom, lblto, lblmsg, lblsub, lblis_spam, lbltype;
	JLabel lblid, lblfrom2, lblto2, lblmsg2, lblsub2, lblis_spam2, lbltype2, lbltable;
	JLabel lblid3;

	JTextField tfid, tffrom2, tfto2, tfmsg2, tfsub2, tftype2;
	JScrollPane sp_all, sp_block, sp_delete;
	JTextField tflogin, tffrom, tfto, tfmsg, tfsub, tftype;
	JTextArea tfdisplay;
	JComboBox l_is, cb_display;
	JPasswordField tfpass;
	JComboBox l_is2;
	JPanel pnl1, pnl_new, pnl_update, pnl_display;
	panelDelete pnl_delete;
	paneluser pnl_user;
	JButton btupdate, btlogin, btSubmit, btdelete, btview, bt;
	JButton button;
	JButton btback;
	JButton btnew,btcreate;
	
	Font f1, f2, f3, f4, f5;
	MenuBar mbar;
	JTable tbdisplay, tbdisplay_block, tbdelete;
	Color r1;
	MenuItem item1, item2, item3, item4, item5;

	// JDBC
	Connection con;

	public FirstPg() {

		jdbcInitial();
		initializeComponents();
		registerListeners();
		addComponenetsToFrame();
		setVisible(true);
		setSize(335, 335);
		setTitle("Spam Detection");
		setLayout(null);
		pnl1.setBounds(82, 90, 150, 70);
		lblTitle.setBounds(84, 20, 165, 70);
		lblTitle.setFont(f1);
		btlogin.setBounds(125, 175, 75, 25);
		btnew.setBounds(120, 210, 85, 25);
		btlogin.setFont(f2);
		btnew.setFont(f2);

		// panel1();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		r1 = new Color(142, 183, 230);
		ImageIcon imgf;
		
		pnl_user = new paneluser();
		add(pnl_user);
		pnl_user.setVisible(false);
		pnl_user.setBounds(82, 82, 150, 118);
		pnl_user.setBackground(r1);
		
		btcreate = new JButton("Create");
		btcreate.setBounds(120, 210, 85, 25);
		add(btcreate);
		btcreate.setVisible(false);
		btcreate.addActionListener(this);
		
		btback = new JButton("<-");
		add(btback);
		btback.setVisible(false);
		getContentPane().setBackground(r1);

		try
		{
			Image imgfav = ImageIO.read(getClass().getResource("icon2.png"));
			imgf = new ImageIcon(imgfav);
			setIconImage(imgf.getImage());
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
		pnl1.setBackground(r1);
	}

	public void jdbcInitial() {
		try {
			// Fixed: Use MySQL driver instead of Oracle
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Network Error/ Driver error", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void display_blocked() {
		try {
			int c = 0;

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/spam_detection", "root", "harshitha@27");
		
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from blocked");
			while (rs.next())
				c++;

			String[][] data = new String[c][2];
			int i = 0;

			ResultSet rs1 = stmt.executeQuery("select * from blocked");

			while (rs1.next()) {
				data[i][0] = rs1.getString(1);
				data[i][1] = rs1.getString(2);
				i++;
			}

			String[] colname = { "EMAIL", "NAME" };
			tbdisplay_block = new JTable(data, colname);

			tbdisplay_block.setBounds(10, 30, 275, 175);
			sp_block = new JScrollPane();
			sp_block.setViewportView(tbdisplay_block);
			pnl_display.add(sp_block);
			sp_block.setBounds(10, 30, 275, 175);
			con.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void registerListeners() {
		btlogin.addActionListener(this);
		btnew.addActionListener(this);
	}

	public void showtable(int opt) {
		try {
			int c = 0;

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/spam_detection", "root", "harshitha@27");
			
			Statement stmt = con.createStatement();
			Statement stmt2 = con.createStatement();
			Statement stmt3 = con.createStatement();

			ResultSet rs1 = stmt.executeQuery(
					"SELECT Inbox.I_ID, Inbox.From_Mail,Inbox.To_Mail,Message.Content,Message.Subject FROM Inbox INNER JOIN Message ON Inbox.I_ID=Message.ID");
			
			ResultSet rs2 = stmt2.executeQuery(
					"SELECT SPAM.IS_SPAM,SPAM.TYPE FROM Inbox INNER JOIN SPAM ON Inbox.I_ID=SPAM.I_ID");

			ResultSet rs3 = stmt3.executeQuery(
				"SELECT S.TYPE,COUNT(S.TYPE) FROM SPAM S WHERE S.TYPE IS NOT NULL GROUP BY TYPE ORDER BY COUNT(S.TYPE) DESC");
			
			if (opt == 2) {
				this.remove(spammax);
			}	
			
			if(rs3.next()) {
				spammax = new JLabel("");
				spammax.setText("The frequent spam type: "+rs3.getString(1)+"\n (Count:"+rs3.getString(2)+")");
				add(spammax);
				spammax.setBounds(500, 275, 300, 20);
			
				String s1 = "Highly reported : "+rs3.getString(1);
				spammax.setToolTipText(s1);
				spammax.setForeground(Color.RED);			
			}
		
			String colnames[] = { "ID", "From", "To", "Date", "Content", "Subject","IS_SPAM","TYPE" };
			while (rs1.next())
				c++;

			String rows[][] = new String[c][9];
			
			ResultSet rs = stmt.executeQuery(
					"SELECT Inbox.I_ID, Inbox.From_Mail,Inbox.To_Mail,Inbox.Day,Message.Content,Message.Subject FROM Inbox INNER JOIN Message ON Inbox.I_ID=Message.ID");
			int i = 0;
			while (rs.next()) {
				rows[i][0] = rs.getString(1);
				rows[i][1] = rs.getString(2);
				rows[i][2] = rs.getString(3);
				rows[i][3] = rs.getString(4);
				rows[i][4] = rs.getString(5);
				rows[i][5] = rs.getString(6);
				i++;
			}
			i=0;
			while (rs2.next()) {
				rows[i][6]=rs2.getString(1);
				rows[i][7]=rs2.getString(2);
				i++;
			}

			if (opt == 2) {
				this.remove(sp_delete);
			}

			tbdelete = new JTable(rows, colnames);
			tbdelete.setRowHeight(40);
			TableColumnModel columnModel = tbdelete.getColumnModel();

			columnModel.getColumn(0).setPreferredWidth(40);
			columnModel.getColumn(1).setPreferredWidth(100);
			columnModel.getColumn(2).setPreferredWidth(100);
			columnModel.getColumn(3).setPreferredWidth(40);
			columnModel.getColumn(4).setPreferredWidth(200);
			columnModel.getColumn(5).setPreferredWidth(200);

			ListSelectionModel model = tbdelete.getSelectionModel();
			model.addListSelectionListener((ListSelectionListener) new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					int sel = model.getMinSelectionIndex();

					if (pnl_delete.isVisible()) {
						pnl_delete.tfid3.setText("" + tbdelete.getValueAt(sel, 0));
					} else if (pnl_update.isVisible()) {
						tfid.setText("" + tbdelete.getValueAt(sel, 0));
						tffrom2.setText("" + tbdelete.getValueAt(sel, 1));
						tfto2.setText("" + tbdelete.getValueAt(sel, 2));
						tfmsg2.setText("" + tbdelete.getValueAt(sel, 4));
						tfsub2.setText("" + tbdelete.getValueAt(sel, 5));
						tftype2.setText("" + tbdelete.getValueAt(sel, 7));
						String val = "" + tbdelete.getValueAt(sel,6);
						
						if(val.equals("Yes")) {
							l_is2.setSelectedIndex(0);
						} else {
							l_is2.setSelectedIndex(1);
						}
					}
				}
			});

			tbdelete.setBounds(10, 30, 275, 175);
			sp_delete = new JScrollPane(tbdelete);

			sp_delete.setBounds(375, 100, 570, 150);
			tbdelete.setToolTipText("Select a row to edit/delete");
			add(sp_delete);
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void panel1() {
		// VIEW INSERT
		mbar = new MenuBar();
		setMenuBar(mbar);
		Menu file = new Menu("Options");

		file.add(item1 = new MenuItem("New data"));
		file.add(item2 = new MenuItem("Update data"));
		file.add(item3 = new MenuItem("Delete"));

		mbar.add(file); // Adding Menu to MenuBar
		item1.addActionListener(this);
		item2.addActionListener(this);
		item3.addActionListener(this);

		String bool[] = { "Yes", "No" };
		lblTitle.setText("Inbox");
		lblTitle.setBounds(160, 20, 170, 70);
		lblfrom = new JLabel("From :");
		lblto = new JLabel("To :");
		lblmsg = new JLabel("Message :");
		lblsub = new JLabel("Subject :");
		lblis_spam = new JLabel("Spam ?");
		lbltype = new JLabel("Type :");

		tftype = new JTextField(10);
		l_is = new JComboBox(bool);
		tffrom = new JTextField(10);
		tfto = new JTextField(10);
		tfmsg = new JTextField(10);
		tfsub = new JTextField(10);

		l_is.setSelectedIndex(0);
		l_is.addItemListener(this);

		btSubmit = new JButton("Submit");
		btSubmit.addActionListener(this);
		add(btSubmit);
		btSubmit.setBounds(155, 275, 75, 25);

		pnl1.setVisible(false);
		pnl_new = new JPanel();
		pnl_new.setLayout(new GridLayout(6, 2));
		pnl_new.add(lblfrom);
		pnl_new.add(tffrom);
		pnl_new.add(lblto);
		pnl_new.add(tfto);
		pnl_new.add(lblmsg);
		pnl_new.add(tfmsg);
		pnl_new.add(lblsub);
		pnl_new.add(tfsub);
		pnl_new.add(lblis_spam);
		pnl_new.add(l_is);

		pnl_new.setVisible(true);

		add(pnl_new);

		pnl_new.setBounds(82, 90, 250, 170);
		btlogin.setVisible(false);

		setSize(1000, 425);

		lbltable = new JLabel("Table View");
		showtable(1);
		add(lbltable);
		lbltable.setBounds(600, 15, 100, 100);
		lbltable.setFont(f1);
		pnl_new.add(lbltype);
		pnl_new.add(tftype);
	}

	private void panel2() {
		// VIEW UPDATE
		pnl_update = new JPanel(new GridLayout(7, 2));
		pnl_update.setVisible(false);

		String bool2[] = { "Yes", "No" };
	
		lblid = new JLabel("ID");
		lblfrom2 = new JLabel("From :");
		lblto2 = new JLabel("To :");
		lblmsg2 = new JLabel("Message :");
		lblsub2 = new JLabel("Subject :");
		lblis_spam2 = new JLabel("Spam ?");
		lbltype2 = new JLabel("Type :");

		tfid = new JTextField(10);
		tfid.setText("Suggested to update using this");
		tftype2 = new JTextField(10);
		l_is2 = new JComboBox(bool2);
		tffrom2 = new JTextField(10);
		tfto2 = new JTextField(10);
		tfmsg2 = new JTextField(10);
		tfsub2 = new JTextField(10);

		l_is2.setSelectedIndex(0);
		l_is2.addItemListener(this);

		btupdate = new JButton("Update");
		add(btupdate);
		btupdate.setBounds(155, 275, 75, 25);

		btupdate.addActionListener(this);
		
		pnl_update.add(lblid);
		pnl_update.add(tfid);
		pnl_update.add(lblfrom2);
		pnl_update.add(tffrom2);
		pnl_update.add(lblto2);
		pnl_update.add(tfto2);
		pnl_update.add(lblmsg2);
		pnl_update.add(tfmsg2);
		pnl_update.add(lblsub2);
		pnl_update.add(tfsub2);
		pnl_update.add(lblis_spam2);
		pnl_update.add(l_is2);
		pnl_update.add(lbltype2);
		pnl_update.add(tftype2);

		tffrom2.setEditable(false);
		tfto2.setEditable(false);
		pnl_update.setBounds(82, 90, 265, 170);
		add(pnl_update);
	}

	private void panel3() {
		pnl_delete = new panelDelete();
		
		btdelete = new JButton("Delete");

		add(btdelete);
		add(pnl_delete);

		btdelete.setBounds(155, 250, 100, 30);
		btdelete.addActionListener(this);
	}

	public void panel_display() {
		String opt_display[] = { "USER_PROFILE", "BLOCKED", "INBOX" };
		pnl_display = new JPanel(null);
		pnl_display.setVisible(false);
		cb_display = new JComboBox(opt_display);

		cb_display.setBounds(60, 6, 120, 20);

		cb_display.addItemListener(this);

		add(pnl_display);
		pnl_display.setBounds(50, 100, 300, 250);
		pnl_display.add(cb_display);
	}

	private void initializeComponents() {
		btlogin = new JButton("Login");
		f1 = new Font(Font.SERIF, Font.BOLD, 20);
		f2 = new Font(Font.SERIF, Font.BOLD, 10);
		f3 = new Font(Font.SERIF, Font.BOLD, 30);
		f4 = new Font(Font.SERIF, Font.PLAIN, 15);
		lblTitle = new JLabel("Spam Detection");
		
		btnew = new JButton("New User");
		btnew.setToolTipText("Click to create a new account");
		pnl1 = new JPanel();

		lbllogin = new JLabel("Username:");
		lblpass = new JLabel("Password:");
		tflogin = new JTextField(10);
		tfpass = new JPasswordField(10);
	}

	private void addComponenetsToFrame() {
		add(lblTitle);
		pnl1.setLayout(new GridLayout(2, 2, 2, 10));
		pnl1.add(lbllogin);
		pnl1.add(tflogin);
		pnl1.add(lblpass);
		pnl1.add(tfpass);

		add(pnl1);
		add(btlogin);
		add(btnew);
	}

	public void jdbcdelete() {
		try {
			int todel = Integer.parseInt(pnl_delete.tfid3.getText());
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/spam_detection", "root", "harshitha@27");
			
			Statement stmt = con.createStatement();
			int rs1 = stmt.executeUpdate("delete from inbox where I_ID=" + todel);

			con.close();

			if(rs1==0) {
				throw new Exception("Delete Unsuccessfull/No data");
			}

			JOptionPane.showMessageDialog(this, "Successfully deleted", "Delete", JOptionPane.INFORMATION_MESSAGE);
		} 
		catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this,"Enter a number only", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this,e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void jdbcinsert() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/spam_detection", "root", "harshitha@27");

			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yy");
			
			Statement stmt = con.createStatement();
			String dat = myDateObj.format(myFormatObj);

			// Insert into inbox (I_ID is auto-increment)
			int s = stmt.executeUpdate("insert into inbox (From_Mail, To_Mail, Day) values ('"+tffrom.getText()+"','"+tfto.getText()+"','"+dat+"')");
			
			if(s==0) throw new Exception("Insert failed");

			// Get the generated ID
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
			int generatedId = 0;
			if(rs.next()) {
				generatedId = rs.getInt(1);
			}

			s = stmt.executeUpdate("insert into message values ("+generatedId+",'"+tfmsg.getText()+"','"+tfsub.getText()+"')");
			
			if(s==0) throw new Exception("Insert failed");

			String val = (String) l_is.getSelectedItem();
			String typeValue = val.equals("Yes") ? tftype.getText() : "NULL";
			
			if(val.equals("Yes")) {
				s = stmt.executeUpdate("insert into spam values ("+generatedId+",'"+val+"','"+tftype.getText()+"')");
			} else {
				s = stmt.executeUpdate("insert into spam (I_ID, IS_SPAM) values ("+generatedId+",'"+val+"')");
			}
			
			if(s==0) throw new Exception("Insert failed");

			JOptionPane.showMessageDialog(this, "Successfully inserted", "Insert", JOptionPane.INFORMATION_MESSAGE);
			
			con.close();

		} 
		catch (SQLIntegrityConstraintViolationException e1) {
			JOptionPane.showMessageDialog(this, "Enter a valid email id", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e1) {
			JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==button) {
			dispose();
			new FirstPg();
		}
		
		if(e.getActionCommand().equals("Create")) {
			createuser();			
		}
		
		if(e.getActionCommand().equals("<-")) {
			pnl1.setVisible(true);
			btlogin.setVisible(true);
			pnl_user.setVisible(false);
			btcreate.setVisible(false);
			btback.setVisible(false);
			btnew.setVisible(true);
		}
		
		if(e.getActionCommand().equals("New User")) {
			pnl1.setVisible(false);
			btlogin.setVisible(false);
			pnl_user.setVisible(true);
			btcreate.setVisible(true);
			btnew.setVisible(false);
			
			btback.setBounds(20, 20, 50, 25);
			btback.addActionListener(this);
			btback.setVisible(true);
		}

		if (e.getActionCommand().equals("Submit")) {
			jdbcinsert();
			showtable(2);
		}
		
		if (e.getActionCommand().equals("Display")) {
			pnl_delete.setVisible(false);
			pnl_new.setVisible(false);
			pnl_update.setVisible(false);
			pnl_display.setVisible(true);
			btSubmit.setVisible(false);
			btdelete.setVisible(false);
			btupdate.setVisible(false);
			lblTitle.setText("Display");
			lblTitle.setFont(f3);
			btnew.setVisible(false);
		}
		
		if (e.getActionCommand().equals("Update")) {
			jdbcupdate();
		}
		
		if (e.getActionCommand().equals("Update data")) {
			pnl_delete.setVisible(false);
			pnl_new.setVisible(false);
			pnl_update.setVisible(true);
			
			pnl_display.setVisible(false);
			btSubmit.setVisible(false);
			btdelete.setVisible(false);
			btupdate.setVisible(true);
			btupdate.setToolTipText("Click to update data");

			lblTitle.setText("Update");
			lblTitle.setFont(f3);
			btnew.setVisible(false);
		}
		
		if (e.getSource() == btdelete) {
			jdbcdelete();
			showtable(2);
		} else if (e.getActionCommand().equals("Delete")) {
			pnl_new.setVisible(false);
			pnl_update.setVisible(false);
			pnl_delete.setVisible(true);
			pnl_display.setVisible(false);
			btSubmit.setVisible(false);
			btupdate.setVisible(false);
			btdelete.setVisible(true);

			btdelete.setToolTipText("Click to delete data");

			lblTitle.setText("Delete");
			lblTitle.setFont(f3);
			this.setSize(1000, 450);
			btnew.setVisible(false);
		}
		
		if (e.getActionCommand().equals("New data")) {
			pnl_new.setVisible(true);
			pnl_update.setVisible(false);
			pnl_delete.setVisible(false);
			pnl_display.setVisible(false);
			
			btSubmit.setVisible(true);
			btupdate.setVisible(false);
			btdelete.setVisible(false);
			
			btSubmit.setToolTipText("Click to insert data");
			
			lblTitle.setText("Insert");
			lblTitle.setFont(f3);
		}
		
		if (e.getActionCommand().equals("Login")) {
			String s1 = tflogin.getText();
			String s2 = tfpass.getText();
		 	button = new JButton();	
  		
			try {
		    	Image img = ImageIO.read(getClass().getResource("icons8-login-rounded-30.png"));
		    	button.setIcon(new ImageIcon(img));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
	  		}

			add(button);
			button.setBounds(20, 300, 60, 40);
			button.addActionListener(this);
			
			try{ 
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/spam_detection", "root", "harshitha@27");
				  
				Statement stmt=con.createStatement(); 
				ResultSet rs=stmt.executeQuery("select * from login where email='"+s1+"'");
				  
				if(rs.next()) {
					if(s2.equals(rs.getString(2))) {
						panel1(); 
						panel2(); 
						panel3(); 
						panel_display();
					  	btdelete.setVisible(false); 
					  	btupdate.setVisible(false);
					  	pnl_new.setBackground(r1); 
					  	pnl_update.setBackground(r1);
					  	pnl_delete.setBackground(r1); 
						btnew.setVisible(false);
					} else {
						throw new Exception("Enter valid credentials");
					}
				} else {
					throw new Exception("Email not found");
				}
				con.close();
			}
			catch(Exception e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void createuser() {
		String s1=pnl_user.getTfemail().getText();
		String s2=pnl_user.getTfname().getText();
		String s3=pnl_user.getTfdob().getText();
		String s4=pnl_user.getTfphn().getText();
		String s5=pnl_user.getTfpass().getText();
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/spam_detection", "root", "harshitha@27");

			Statement stmt = con.createStatement();
			
			stmt.executeUpdate("insert into user_profile values ('"+s1+"','"+s2+"','"+s3+"','"+s4+"')");
			stmt.executeUpdate("insert into login values ('"+s1+"','"+s5+"')");
			
			JOptionPane.showMessageDialog(this, "Successfully inserted", "Insert", JOptionPane.INFORMATION_MESSAGE);
			
			con.close();

		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
        
	private void jdbcupdate() {
	try {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/spam_detection", "root", "harshitha@27");

		int val1 = Integer.parseInt(tfid.getText());

		Statement stmt = con.createStatement();
		
		int done=0;
		if (!tfmsg2.getText().equals("")) {
			String pt = "Update Message set content='" + tfmsg2.getText() + "' where id=" + val1;
			
			int insert = stmt.executeUpdate("Update Message set content='" + tfmsg2.getText() + "' where id=" + val1);
			
			done = insert;
			if(done==0) throw new Exception("Update Failed\nMight be due to ID D.N.E");
		}
		
		if (!tfsub2.getText().equals("")) {
			int insert1 = stmt.executeUpdate("Update Message set subject='" + tfsub2.getText() + "' where id=" + val1);
			
			done = insert1;

			if(done==0) throw new Exception("Update Failed");
		}
		
		if(!tftype2.getText().equals("")) {
			int insert2 = stmt.executeUpdate("Update Spam set Type='" + tftype2.getText() + "' where i_id=" + val1);
			
			done = insert2;

			if(done==0) throw new Exception("Update Failed");
		}
		
		String cbox = (String) l_is2.getSelectedItem();
		System.out.println(cbox);

		if(cbox.equals("No")) {
			int insert3 = stmt.executeUpdate("Update Spam set IS_SPAM='" + cbox + "' where i_id=" + val1);
			int insert2 = stmt.executeUpdate("Update Spam set Type=NULL where i_id=" + val1);

			done = insert2+insert3;

			if(done==0) throw new Exception("Update Failed");
		} else {
			int insert3 = stmt.executeUpdate("Update Spam set IS_SPAM='" + cbox + "' where i_id=" + val1);
			System.out.println("Update Spam set IS_SPAM='" + cbox + "' where i_id=" + val1);
			done = insert3;

			if(done==0) throw new Exception("Update Failed");
		}

		if(done>=1) {
			JOptionPane.showMessageDialog(null,"Successfully Updated","Update !!", JOptionPane.INFORMATION_MESSAGE);
		}
		con.close();

               } 
                catch(NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,"id : Enter a number only", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception e1) {
                    JOptionPane.showMessageDialog(null,e1.getMessage(),"alert", JOptionPane.ERROR_MESSAGE);
                }

                showtable(2);
        }
		
	public void itemStateChanged(ItemEvent e) {

		
		if (cb_display.getSelectedItem() == "BLOCKED") {
			
			tbdisplay.setVisible(false);
			sp_all.setVisible(false);
			display_blocked();

		}
		String s = e.paramString();
		
		if (l_is.getSelectedItem() == "Yes") {

			lbltype.setVisible(true);
			tftype.setVisible(true);
		} else {
			tftype.setText("");
			lbltype.setVisible(false);
			tftype.setVisible(false);
		}

		if (l_is2.getSelectedItem() == "Yes") {

			lbltype2.setVisible(true);
			tftype2.setVisible(true);
		} else {

			tftype2.setText("");
			lbltype2.setVisible(false);
			tftype2.setVisible(false);
		}

	}

}
public class MyClassSwing {

	public static void main(String... args) {
		
		try
		{
		new FirstPg();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

}