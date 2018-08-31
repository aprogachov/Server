import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 3333;
    
    public static void main(String[] ar) {
        try {
            ServerSocket ss = new ServerSocket(PORT);
            System.out.println("Waiting for a client...");
            
            while (true) {
                Socket socket = ss.accept();
                new Thread(new SThread(socket)).start();
            }
            
        } catch(IOException e) {
           e.printStackTrace(); 
        }
    }  
}    
    
    class SThread implements Runnable {    
        
        private Socket socketThread;
        
        SThread(Socket socketThread) {  
            this.socketThread = socketThread;
        }    
          
        @Override
        public void run() {        
            try { 
                InputStream ins = socketThread.getInputStream();
                OutputStream outs = socketThread.getOutputStream();
           
                DataInputStream dins = new DataInputStream(ins);
                DataOutputStream douts = new DataOutputStream(outs);
                
                String json = dins.readUTF(); // ожидаем пока клиент пришлет текст.
                Thread.sleep(1000);
                System.out.println("JSON from client: " + json);
                
                String[] parts = json.split(", \"");
                String one[]= parts[0].split(":");
                String two[]=parts[1].split(":"); 
                String name = one[1].replaceAll("[\"\\r\\n,:}{]", "").trim();
           
                String json1 = "{\"message\": \"Hello, " + name + "\"}";
                douts.writeUTF(json1); // отсылаем клиенту обратно
                douts.flush();      
            } catch(IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    socketThread.close();
                } catch (IOException e) {    
                    e.printStackTrace();
            }       
        }       
    }    
}