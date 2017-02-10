import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	boolean started=false;
	//boolean bConnected=false;
	ServerSocket ss = null;
	//DataInputStream dis = null;
	List<Client> clients = new ArrayList<Client>();

	public static void main(String[] args) {

		
		new ChatServer().start();

}
public void start()
{
	try
	{
		  ss=new ServerSocket(10004);
		  started=true;
	}
	catch(IOException e){
		
		e.printStackTrace();	
	}
	try
	{
		while(started){
			 Socket s =ss.accept();
			 Client c = new Client(s);
System.out.println("Client is connected");
			 new Thread(c).start();
			 clients.add(c);
			 
		}
	}
	catch(IOException e)
	{
		e.printStackTrace();
	}
	finally
	{
		try
		{
			ss.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
class Client implements Runnable
{
	private Socket s;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private boolean bConnected = false;
	public Client(Socket s)
	{
		this.s = s;
		try
		{
			dis =new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			
			bConnected=true;

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public void send(String str)
	{
		try
		{
			dos.writeUTF(str);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public void run()
	{
		try
		{
			while(bConnected)
			{
				
				String str = dis.readUTF();
System.out.println(str);
				for(int i=0;i<clients.size();i++)
				{
					Client c = clients.get(i);
					c.send(str);
				}
				//dis.close();
			}
			
		}
		catch(IOException e)
		{
            System.out.println("Client closed");
		}
		finally
		{
			try
			{
				if(dis != null) dis.close();
				if(dos != null) dos.close();
				if(s != null) s.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
}