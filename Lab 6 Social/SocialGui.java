package social;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.Serial;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;


public class SocialGui extends JFrame {
	@Serial
	private static final long serialVersionUID = 1L;

	private static final int H = 300;
	private static final int W = 400;
	public JLabel l1 = new JLabel("ID:");
	public JTextField id = new JTextField("",10);
	public JButton login = new JButton("Login");
	public JLabel name = new JLabel("<user name>");
	public JList<String> friends = new JList<>();
	
	private final Social model;

	public SocialGui(Social m){
		
		this.model = m;
		
		setTitle("MyFacebook");
		setSize(W,H);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screen.width/2-W/2,screen.height/2-H/2);
		Image icon = new ImageIcon(SocialGui.class.getResource("PoliLogoBlu.png")).getImage();
		setIconImage(icon); // windows
//		Application.getApplication().setDockIconImage(icon); // os x
		
		setLayout(new BorderLayout());
		
		JPanel upper = new JPanel();
		upper.setLayout(new FlowLayout());
		upper.add(l1);
		upper.add(id);
		upper.add(login);
		add(upper,BorderLayout.NORTH);
		
		JPanel lower=new JPanel();
		lower.setLayout(new BorderLayout());
		
		JPanel infopanel = new JPanel();
		infopanel.add(name);
		lower.add(infopanel,BorderLayout.NORTH);

		JPanel details = new JPanel();
		details.setLayout(new GridLayout(1,1,5,5));
		
		details.add(friends);
		//details.add(groups);
		lower.add(details,BorderLayout.CENTER);
	
		add(lower,BorderLayout.CENTER);
		lower.setBorder(new LineBorder(Color.DARK_GRAY));
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		
		friends.setListData(new String[]{"friends","..."});
		//groups.setListData(new String[]{"groups","..."});
		
		login.addActionListener(event -> {
			if(event.getActionCommand().equals("Login")){
				doLogin();
			}
		});
		id.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.VK_ENTER){
					doLogin();
				}
			}
		});
	}


	private void doLogin() {
		try {
			String code = id.getText();
			String person = model.getPerson(code);
			name.setText(person);
			
			Collection<String> listOfFriends = model.listOfFriends(code);
			friends.setListData(listOfFriends.toArray(new String[listOfFriends.size()]));
		} catch (NoSuchCodeException e) {
			JOptionPane.showMessageDialog(this, "Invalid user code","Error",JOptionPane.ERROR_MESSAGE);
		}
	}

}
