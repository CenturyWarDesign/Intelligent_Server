package centurywar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
	private int port = 8080;
    private ServerSocket serverSocket;
    private ExecutorService executorService;//线程池
    private final int POOL_SIZE=10;//单个CPU线程池大小

	public Main() throws IOException {
        serverSocket=new ServerSocket(port);
        //Runtime的availableProcessor()方法返回当前系统的CPU数目.
        executorService=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
        System.out.println("服务器启动");

    }



    public void service(){
        while(true){
            Socket socket=null;
            try {
                //接收客户连接,只要客户进行了连接,就会触发accept();从而建立连接
                socket=serverSocket.accept();
                executorService.execute(new Handler(socket));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
		new Main().service();
    }

}

class Handler implements Runnable{
    private Socket socket;
	int i = 0;
    public Handler(Socket socket){
        this.socket=socket;
    }
    private PrintWriter getWriter(Socket socket) throws IOException{
        OutputStream socketOut=socket.getOutputStream();
        return new PrintWriter(socketOut,true);
    }
    private BufferedReader getReader(Socket socket) throws IOException{
        InputStream socketIn=socket.getInputStream();
        return new BufferedReader(new InputStreamReader(socketIn));
    }

    public String echo(String msg){
        return "echo:"+msg;
    }

	public int getvip(int i) {
		try {
			ResultSet rs = JDBC.query("select * from user_vip limit " + i
					+ ",1");
			// return 0;
			while (rs.next() && rs != null) {
				return rs.getInt("gameuid");
			}
		} catch (Exception e) {
			System.out.println("[hero]" + e);
		}
		return 0;
	}

    public void run(){
        try {
            System.out.println("New connection accepted "+socket.getInetAddress()+":"+socket.getPort());
			// BufferedReader br = getReader(socket);
            PrintWriter pw=getWriter(socket);
			// String msg = null;
			while (true) {
				pw.println(getvip(i++) + "");
				// pw.println("1000");
				try {
					Thread.sleep(1000); // 由类名调用
				} catch (Exception e) {
					return;
				}
			}
			// while((msg=br.readLine())!=null){
			// System.out.println(msg);
			// pw.println(echo(msg));
			// if(msg.equals("bye"))
			// break;
			// }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(socket!=null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}