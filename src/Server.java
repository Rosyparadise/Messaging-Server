import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.SourceVersion;

public class Server
{
    private static int identifier=0;
    private static ArrayList<Account> accounts = new ArrayList<>();

    public static void main(String[] args)
    {
        ServerSocket server = null;
        try
        {
            server = new ServerSocket(Integer.parseInt(args[0]));
            while (true)
            {
                Socket client = server.accept();
                ClientHandler clientSock = new ClientHandler(client);
                new Thread(clientSock).start();
            }
        }
        catch (IOException a)
        {
            a.printStackTrace();
        }

    }

    private static class ClientHandler implements Runnable
    {
        private final Socket clientSocket;
        private static int messagenum=0;
        public ClientHandler(Socket clientSocket)
        {
            this.clientSocket= clientSocket;
        }

        public static int matchToken(int authtoken)
        {
            for (int i=0;i<accounts.size();i++)
            {
                if (authtoken==accounts.get(i).authToken)
                    return i;
            }
            return -1;
        }

        public static boolean searchUsername(String username)
        {
            for (int i=0;i<accounts.size();i++)
            {
                if (username.equals(accounts.get(i).getUsername()))
                    return false;
            }
            return true;
        }


        public void run()
        {
            PrintWriter out = null;
            BufferedReader in = null;
            try
            {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String choice = in.readLine();
                if (choice.equals("1"))
                {
                    String username=in.readLine();

                    if (searchUsername(username))
                    {
                        if (SourceVersion.isIdentifier(username))
                        {
                            accounts.add(new Account(username, identifier));
                            out.println(identifier);
                            identifier++;
                        }
                        else
                            out.println("Invalid Username");
                    }
                    else
                        out.println("Sorry, the user already exists");
                    out.println("exit");
                }
                else if (choice.equals("2"))
                {
                    if (matchToken(Integer.parseInt(in.readLine()))!=-1)
                    {
                        for (int i=0;i<accounts.size();i++)
                        {
                            out.println((i+1)+") "+accounts.get(i).getUsername());
                        }
                    }
                    else
                        out.println("Invalid Auth Token");
                    out.println("exit");
                }
                else if (choice.equals("3"))
                {
                    int authtoken=matchToken(Integer.parseInt(in.readLine()));
                    if (authtoken!=-1)
                    {
                        String username;
                        username=accounts.get(authtoken).getUsername();
                        String recipient=in.readLine();
                        for (int i=0;i<accounts.size();i++)
                        {
                            if (recipient.equals(accounts.get(i).username))
                            {
                                messagenum++;
                                accounts.get(i).insertMessage(new Message(false,username,recipient,in.readLine(),messagenum));
                                out.println("OK");
                                out.println("exit");
                            }
                        }
                        out.println("User does not exist");
                    }
                    else
                        out.println("Invalid Auth Token");
                    out.println("exit");


                }
                else if (choice.equals("4"))
                {
                    int authtoken=matchToken(Integer.parseInt(in.readLine()));
                    if (authtoken!=-1)
                    {
                        Account user=accounts.get(authtoken);
                        for (int i=0;i<user.messageBox.size();i++)
                        {
                            out.print(user.messageBox.get(i).id+"." + " from: "+user.messageBox.get(i).sender);
                            if (!user.messageBox.get(i).isRead)
                                out.print("*");
                            out.println();
                        }
                    }
                    else
                        out.println("Invalid Auth Token");
                    out.println("exit");

                }
                else if (choice.equals("5"))
                {
                    int authtoken=matchToken(Integer.parseInt(in.readLine()));
                    if (authtoken!=-1)
                    {
                        Account user=accounts.get(authtoken);
                        int mes_choice=Integer.parseInt(in.readLine());
                        for (int i=0;i<user.messageBox.size();i++)
                        {
                            if (user.messageBox.get(i).id==mes_choice)
                            {
                                out.println("(" + user.messageBox.get(i).sender + ") "+ user.messageBox.get(i).body);
                                user.messageBox.get(i).isRead=true;
                                out.println("exit");
                                break;
                            }
                        }
                        out.println("Message ID does not exist");
                    }
                    else
                        out.println("Invalid Auth Token");

                    out.println("exit");
                }
                else if (choice.equals("6"))
                {
                    int authtoken=matchToken(Integer.parseInt(in.readLine()));
                    if (authtoken!=-1)
                    {
                        Account user=accounts.get(authtoken);
                        int mes_choice=Integer.parseInt(in.readLine());
                        for (int i=0;i<user.messageBox.size();i++)
                        {
                            if (user.messageBox.get(i).id==mes_choice)
                            {
                                user.messageBox.remove(i);
                                out.println("OK");
                                out.println("exit");
                                break;
                            }
                        }
                        out.println("Message does not exist");
                    }
                    else
                        out.println("Invalid Auth Token");

                    out.println("exit");
                }
            }
            catch (IOException a)
            {
                a.printStackTrace();
            }
        }
    }

    private static class Account
    {
        private String username;
        private int authToken;
        private List<Message> messageBox;

        public Account()
        {
            username="";
        }
        public Account(String username, int authToken)
        {
            this.username=username;
            this.authToken=authToken;
            messageBox = new ArrayList<>();
        }

        public String getUsername()
        {
            return username;
        }

        public void insertMessage(Message mes)
        {
            messageBox.add(mes);
        }

    }

    protected static class Message
    {
        private boolean isRead;
        private String sender;
        private String receiver;
        private String body;
        private int id;

        public Message(){}
        public Message(boolean isRead,String sender, String receiver, String body,int id)
        {
            this.isRead=isRead;
            this.sender=sender;
            this.receiver=receiver;
            this.body=body;
            this.id=id;
        }

    }
}