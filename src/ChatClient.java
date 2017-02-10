import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class ChatClient extends Frame {
	
	Socket s= null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	TextArea tarea = new TextArea();
	TextField tfield = new TextField();
	Thread tRecv= new Thread (new RecvThread());
	private boolean bConnected = false;

	public static void main(String[] args) {
		new ChatClient().lanchFrame();

	}
	public void lanchFrame(){
		this.setLocation(400,300);
		this.setSize(500,600);
		add(tarea,BorderLayout.NORTH);
		add(tfield,BorderLayout.SOUTH);
		pack();
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				disConnect();
				System.exit(0);
				
			}
		});
		tfield.addActionListener(new tfListener());
		this.setVisible(true);
		connect();
		
		tRecv.start();
		
	}
	
	public void connect(){
		try{
				 s =new Socket("10.69.231.133",10004);
				 dos =new DataOutputStream(s.getOutputStream());
				 dis = new DataInputStream(s.getInputStream());
System.out.println("Connected");
				bConnected = true;
		}
		catch(UnknownHostException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}

	}
	public void disConnect()
	{
		try
		{
			
			dos.close();
			dis.close();
			s.close();
		}
	
		catch(IOException e1)
		{
			e1.printStackTrace();
		}
		
	}
	private class tfListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String str =tfield.getText().trim();
			//tarea.setText(str);
			tfield.setText("");
			try{
				
			//DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(str);
			dos.flush();
			//dos.close();
			
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	private class RecvThread implements Runnable
	{
		public void run()
		{
			try{
				while(bConnected)
			{
				String str = dis.readUTF();
System.out.println(str);
				tarea.setText(tarea.getText()+str+"\n"); 
				
			}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
