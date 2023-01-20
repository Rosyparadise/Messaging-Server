import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client
{
    public static void main (String[] args)
    {

        try (Socket socket = new Socket(args[0], Integer.parseInt(args[1])))
        {
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(args[2]);
            out.println(args[3]);
            if (Integer.parseInt(args[2])>2 && !args[2].equals("4"))
            {
                out.println(args[4]);
                if (args[2].equals("3"))
                    out.println(args[5]);
            }

            String line = in.readLine();
            while (!"exit".equals(line))
            {
                System.out.println(line);
                line = in.readLine();
            }


        }
        catch (IOException a)
        {
            a.printStackTrace();
        }

    }

}
