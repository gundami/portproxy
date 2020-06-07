package tcpudp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.netcrusher.core.reactor.NioReactor;
import org.netcrusher.datagram.DatagramCrusher;
import org.netcrusher.datagram.DatagramCrusherBuilder;
import org.netcrusher.tcp.TcpCrusher;
import org.netcrusher.tcp.TcpCrusherBuilder;

public class main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Properties pps = new Properties();
        pps.load(new FileInputStream("config.properties"));
        Enumeration enum1 = pps.propertyNames();//得到配置文件的名字
        int tcpThread=Integer.parseInt(pps.getProperty("tcp"));
        ExecutorService tcpPool = Executors.newFixedThreadPool(tcpThread);
        String tcp_des,tcp_source;
        int tcp_desPort,tcp_sourcePort;
        for(int i=1;i <= tcpThread;i++ ) {
        	tcp_des=pps.getProperty("tcp_des"+String.valueOf(i));
        	tcp_source=pps.getProperty("tcp_source"+String.valueOf(i));
        	tcp_desPort=Integer.parseInt(pps.getProperty("tcp_desPort"+String.valueOf(i)));
        	tcp_sourcePort=Integer.parseInt(pps.getProperty("tcp_sourcePort"+String.valueOf(i)));
        	tcpPool.execute((tcp(tcp_des,tcp_desPort,tcp_source,tcp_sourcePort)));
        	System.out.println("tcp"+String.valueOf(i)+" ok");
        }
        
        int udpThread=Integer.parseInt(pps.getProperty("udp"));
        ExecutorService udpPool = Executors.newFixedThreadPool(udpThread);
        String udp_des,udp_source;
        int udp_desPort,udp_sourcePort;
        for(int i=1;i <= udpThread;i++ ) {
        	udp_des=pps.getProperty("udp_des"+String.valueOf(i));
        	udp_source=pps.getProperty("udp_source"+String.valueOf(i));
        	udp_desPort=Integer.parseInt(pps.getProperty("udp_desPort"+String.valueOf(i)));
        	udp_sourcePort=Integer.parseInt(pps.getProperty("udp_sourcePort"+String.valueOf(i)));
        	udpPool.execute((udp(udp_des,udp_desPort,udp_source,udp_sourcePort)));
        	System.out.println("udp"+String.valueOf(i)+" ok");
        }

	}

	public static Runnable tcp(String des,int desPort,String source,int sourcePort) throws IOException {
		return new Runnable() {
            @Override
            public void run() {
            	NioReactor reactor;
				try {
					reactor = new NioReactor();
					TcpCrusher crusher = TcpCrusherBuilder.builder()
		        		    .withReactor(reactor)
		        		    .withBindAddress(des, desPort)
		        		    .withConnectAddress(source, sourcePort)
		        		    .buildAndOpen();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
            }
        };
		

	}
	public static Runnable udp(String des,int desPort,String source,int sourcePort) throws IOException {
		return new Runnable() {
            @Override
            public void run() {
            	NioReactor reactor;
				try {
					reactor = new NioReactor();
					DatagramCrusher crusher = DatagramCrusherBuilder.builder()
					    .withReactor(reactor)
					    .withBindAddress(des, desPort)
					    .withConnectAddress(source, sourcePort)
					    .buildAndOpen();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
            }
        };
		
	}
}